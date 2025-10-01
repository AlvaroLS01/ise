package com.comerzzia.bricodepot.backoffice.services.general.importacionmasiva;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.comerzzia.bricodepot.backoffice.persistence.albaranes.venta.pagos.anticipos.Anticipos;
import com.comerzzia.bricodepot.backoffice.persistence.albaranes.venta.pagos.anticipos.AnticiposMapper;
import com.comerzzia.bricodepot.backoffice.services.fidelizacion.fidelizados.versioning.CustomFidelizadoVersionControlServiceImpl;
import com.comerzzia.bricodepot.backoffice.util.anticipos.AnticiposConstants;
import com.comerzzia.core.model.acciones.AccionBean;
import com.comerzzia.core.model.etiquetas.categorias.EtiquetaBean;
import com.comerzzia.core.model.etiquetas.categorias.EtiquetaCategoriaBean;
import com.comerzzia.core.model.etiquetas.enlaces.EtiquetaEnlaceBean;
import com.comerzzia.core.servicios.acciones.AccionException;
import com.comerzzia.core.servicios.acciones.AccionNotFoundException;
import com.comerzzia.core.servicios.acciones.ServicioAccionesImpl;
import com.comerzzia.core.servicios.contadores.ContadorException;
import com.comerzzia.core.servicios.etiquetas.EtiquetasConstraintViolationException;
import com.comerzzia.core.servicios.etiquetas.EtiquetasException;
import com.comerzzia.core.servicios.etiquetas.ServicioEtiquetasImpl;
import com.comerzzia.core.servicios.etiquetas.categorias.EtiquetasCategoriasConstraintViolationException;
import com.comerzzia.core.servicios.etiquetas.categorias.EtiquetasCategoriasException;
import com.comerzzia.core.servicios.etiquetas.categorias.ServicioEtiquetasCategoriasImpl;
import com.comerzzia.core.servicios.etiquetas.enlaces.EtiquetasEnlacesConstraintViolationException;
import com.comerzzia.core.servicios.etiquetas.enlaces.EtiquetasEnlacesException;
import com.comerzzia.core.servicios.etiquetas.enlaces.ServicioEtiquetasEnlacesImpl;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.util.base.Estado;
import com.comerzzia.core.util.db.Connection;
import com.comerzzia.core.util.db.Database;
import com.comerzzia.core.util.fechas.Fechas;
import com.comerzzia.core.util.numeros.Numero;
import com.comerzzia.model.fidelizacion.fidelizados.FidelizadoBean;
import com.comerzzia.model.fidelizacion.movimientos.MovimientoBean;
import com.comerzzia.model.fidelizacion.tarjetas.TarjetaBean;
import com.comerzzia.model.general.articulos.ArticuloBean;
import com.comerzzia.model.general.articulos.codigosbarras.CodigoBarrasArticuloBean;
import com.comerzzia.persistencia.general.articulos.ArticulosDao;
import com.comerzzia.servicios.fidelizacion.fidelizados.FidelizadoNotFoundException;
import com.comerzzia.servicios.fidelizacion.fidelizados.ServicioFidelizadosImpl;
import com.comerzzia.servicios.fidelizacion.fidelizados.versioning.FidelizadoVersion;
import com.comerzzia.servicios.fidelizacion.movimientos.ServicioMovimientosImpl;
import com.comerzzia.servicios.fidelizacion.tarjetas.ServicioTarjetasImpl;
import com.comerzzia.servicios.general.articulos.ArticuloException;
import com.comerzzia.servicios.general.articulos.ArticuloNotFoundException;
import com.comerzzia.servicios.general.articulos.ContadorArticulos;
import com.comerzzia.servicios.general.articulos.ServicioArticulosImpl;
import com.comerzzia.servicios.general.importacionmasiva.ServicioImportacionMasivaImpl;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

@Service
public class CustomServicioImportacionMasivaImpl extends ServicioImportacionMasivaImpl {

	protected static Logger log = Logger.getLogger(CustomServicioImportacionMasivaImpl.class);

	protected static CustomServicioImportacionMasivaImpl instance;
	
	public static CustomServicioImportacionMasivaImpl get() {
		if (instance == null) {
			instance = new CustomServicioImportacionMasivaImpl();
		}
		return instance;
	}

	public List<String> importarCodigosAbono(Long idAccion, byte[] datosFichero, DatosSesionBean datosSesion) {
		List<String> mensajes = new ArrayList<>();
		BufferedReader csvReader;
		InputStream is = new ByteArrayInputStream(datosFichero);

		csvReader = new BufferedReader(new InputStreamReader(is));

		String row = null;
		try {
			while ((row = csvReader.readLine()) != null) {

				if (!StringUtils.isBlank(row)) {
					TarjetaBean tarjeta = new TarjetaBean();
					try {
						String[] data = row.split(",");

						
						tarjeta.setUidInstancia(datosSesion.getUidInstancia());
						// TODO revisar el tipo de tarjeta ABONO que se va a configurar
						tarjeta.setCodTipoTarj("AB");
						tarjeta.setNumeroTarjeta(data[0]);
						tarjeta.setFechaActivacion(new Date());
						tarjeta.setFechaEmision(new Date());
						tarjeta.setEstadoBean(Estado.NUEVO);

						ServicioTarjetasImpl.get().salvar(tarjeta, datosSesion);

						String importeTA = data[1];
						crearMovimientoTA(tarjeta.getIdTarjeta(), importeTA, datosSesion);
						
						mensajes.add("Importación exitosa de la tarjeta de abono " + tarjeta.getNumeroTarjeta());
					}
					catch (Exception e) {
						mensajes.add("Ha ocurrido un error con la tarjeta de abono " + tarjeta.getNumeroTarjeta());
					}
				}
			}

			csvReader.close();
			is.close();

		}
		catch (IOException e) {
		}

		return mensajes;
	}
	
	public List<String> importarAnticipos(Long idAccion, byte[] datosFichero, DatosSesionBean datosSesion) {
		List<String> mensajes = new ArrayList<>();
		BufferedReader csvReader;
		InputStream is = new ByteArrayInputStream(datosFichero);
		
		SqlSession sqlSession = Database.getSqlSession();
		AnticiposMapper mapper = sqlSession.getMapper(AnticiposMapper.class);

		csvReader = new BufferedReader(new InputStreamReader(is));

		String row = null;
		try {
			while ((row = csvReader.readLine()) != null) {

				if (!StringUtils.isBlank(row)) {
					Anticipos anticipo = new Anticipos();
					try {
						String[] data = row.split(",");

						anticipo.setUidActividad(datosSesion.getUidActividad());
						
						anticipo.setNumAnticipo(data[0]);
						anticipo.setImporte(data[2]);
						anticipo.setEstado(AnticiposConstants.PARAMETRO_ESTADO_DISPONIBLE);
						
						Long idClieAlbaran = Long.parseLong(data[1]);
						
						anticipo.setIdClieAlbaran(idClieAlbaran);
						mapper.insert(anticipo);
						
						mensajes.add("Importación exitosa del anticipo " + anticipo.getNumAnticipo());
					 }
					catch (Exception e) {
						mensajes.add("Ha ocurrido un error con el anticipo " + anticipo.getNumAnticipo());
					}
				}
			}

			csvReader.close();
			is.close();

		}
		catch (IOException e) {
		} finally {
			sqlSession.close();
		}

		return mensajes;
	}

	private void crearMovimientoTA(Long idTarjeta, String importeTA, DatosSesionBean datosSesion) {

		MovimientoBean movimiento = new MovimientoBean();

		movimiento.setFecha(Fechas.toSqlTimestamp(new Date()));
		movimiento.setEntrada(Double.valueOf(importeTA));
		movimiento.setSalida(0.0);
		movimiento.setIdTarjeta(idTarjeta);
		movimiento.setEstadoMovimiento(1);
		
		movimiento.setConcepto("Importacion Masiva Tarjetas Abono");

		// salvamos el movimiento
		ServicioMovimientosImpl.get().crear(movimiento, datosSesion);
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<String> importarEtiquetas(Long idAccion, byte[] datosFichero, DatosSesionBean datosSesion)
			throws AccionException, AccionNotFoundException, EtiquetasException, BiffException, IOException,
			ContadorException, SQLException {
//		SqlSession sqlSession = datosSesion.getSqlSessionFactory().openSession();
		SqlSession sqlSession = Database.getSqlSession();
		List<String> mensajes = new ArrayList<String>();
		try{
			
			AccionBean accion = ServicioAccionesImpl.get().obtenerAccion(sqlSession, idAccion);
			if(accion != null){
				String ejecucion = accion.getEjecucion();
				String idClase = "";
				if("etiquetarArticulos".equals(ejecucion)){
					idClase = "D_ARTICULOS_TBL.CODART";					
				}else if("etiquetarProductos".equals(ejecucion)){
					idClase = "D_PRODUCTOS_TBL.CODPRODUCTO";
				}else if("etiquetarFidelizados".equals(ejecucion)){
					idClase = "F_FIDELIZADOS_TBL.ID_FIDELIZADO";
				}else if("etiquetarEAN".equals(ejecucion)){
					idClase = "D_ARTICULOS_CODBAR_TBL.CODIGO_BARRAS";
				}else if("etiquetarCategorias".equals(ejecucion)){
					idClase = "D_CATEGORIZACION_TBL.CODCAT";
				}
				if(StringUtils.isNotBlank(idClase)) {					
					mensajes = crearEnlaces(sqlSession, datosFichero, idClase, datosSesion);
				}else {
					mensajes = importDynamicProperties(sqlSession, datosFichero, datosSesion);
				}
				
			}

		} catch (ContadorException e) {
			log.debug("Error al consultar el valor del contador" + e.getMessage());
			sqlSession.rollback();
			throw e;
		} catch (SQLException e) {
			log.debug("Error en la base de datos" + e.getMessage());
			sqlSession.rollback();
			throw e;
		} catch (Exception e) {
			log.debug("Error al intentar asignarle la etiqueta al fidelizado " + e.getMessage());
			sqlSession.rollback();
			throw e;
		}
		finally{
			if(sqlSession != null) {
				sqlSession.close();
			}
		}
		return mensajes;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected List<String> crearEnlaces(SqlSession sqlSession, byte[] datosFichero, String idClase,
			DatosSesionBean datosSesion)
			throws BiffException, IOException, EtiquetasException, ContadorException, SQLException {
		List<String> mensajes = new ArrayList<String>();
		Map<String, ArticuloBean> articulosAVersionar = new HashMap<String, ArticuloBean>();		
		Connection conn = new Connection(sqlSession.getConnection());
		FidelizadoVersion fidelizadoVersion = new FidelizadoVersion(conn);
		int lineasImportadas = 0;
		int lineasTotalesImportadas = 0;
		int lineasEliminadas = 0;
		int lineasTotalesEliminadas = 0;
		int etiquetasAñadidas = 0;
		int categoriasAñadidas = 0;
		InputStream input = new ByteArrayInputStream(datosFichero);
		WorkbookSettings settings = new WorkbookSettings();
		settings.setEncoding("Cp1252");
		Workbook work = Workbook.getWorkbook(input, settings);
		Sheet sheet = work.getSheet(0);
		int numRows = sheet.getRows();
		Set<EtiquetaBean> desasignadas = new TreeSet<EtiquetaBean>(new Comparator<EtiquetaBean>() {
			@Override
			public int compare(EtiquetaBean o1, EtiquetaBean o2) {
				return o1.getUidEtiqueta().compareTo(o2.getUidEtiqueta());
				
			}
		});
		for(int i = 1; i<numRows; i++){
			
			boolean error = false;
			String idObjeto = sheet.getCell(0, i).getContents();
			if("F_FIDELIZADOS_TBL.ID_FIDELIZADO".equals(idClase)){
				try{
					FidelizadoBean fidelizado = ServicioFidelizadosImpl.get().consultarPorCodigo(sqlSession, idObjeto, datosSesion);
					if(fidelizado != null){
						idObjeto = String.valueOf(fidelizado.getIdFidelizado());
					}
				}catch(FidelizadoNotFoundException e){
					mensajes.add("El fidelizado con código "+idObjeto+" no existe (Línea "+i+")");
					idObjeto = null;					
				}
			}
			String etiquetaXSL = sheet.getCell(1, i).getContents();
			String categoria = "";
			try{
				categoria = sheet.getCell(2, i).getContents();
			}catch(Exception e){}
			String desasignar = "";
			try{
				desasignar = sheet.getCell(3, i).getContents();
			}catch(Exception e){}
			
			Object objeto = null;
			
			objeto = consultarObjeto(sqlSession, mensajes, idObjeto, idClase, datosSesion, i);
			if(objeto != null){
				if(objeto instanceof ArticuloBean){
					if(!articulosAVersionar.containsKey(((ArticuloBean) objeto).getCodArticulo())){
						articulosAVersionar.put(((ArticuloBean) objeto).getCodArticulo(), (ArticuloBean) objeto);
					}
				}else if(objeto instanceof CodigoBarrasArticuloBean){
					ArticuloBean articulo = null;
					try {
						articulo = ServicioArticulosImpl.get().consultarSinTarifa(conn, ((CodigoBarrasArticuloBean) objeto).getCodArticulo(), datosSesion);
					} catch (ArticuloNotFoundException | ArticuloException e) {}
					if(articulo != null){
						if(!articulosAVersionar.containsKey(articulo.getCodArticulo())){
							articulosAVersionar.put(articulo.getCodArticulo(), articulo);
						}
					}
				}
			}
			
			if("".equals(desasignar)){
				lineasTotalesImportadas++;
				
				
				if(objeto != null){
					EtiquetaBean eti = null;
						//Sopesamos la posibilidad de que nos vengan las etiquetas asignadas a un objeto en una misma fila separadas por comas
						String[] etiquetas = etiquetaXSL.split(",");
						
						
						for(int indexEti=0;indexEti<etiquetas.length;indexEti++){
							String etiqueta = etiquetas[indexEti];
							//Comprobamos si existe la etiqueta
							eti = ServicioEtiquetasImpl.get().consultarEtiquetaEnlazada(sqlSession, datosSesion, etiqueta);
							
							//Si no existe, creamos una nueva y la salvamos
							if(eti.getUidEtiqueta() == null){
								
								String uidEtiqueta = UUID.randomUUID().toString();
								eti.setEstadoBean(Estado.NUEVO);
								eti.setUidEtiqueta(uidEtiqueta);
								eti.setEtiqueta(etiqueta);
								try {
									ServicioEtiquetasImpl.get().salvar(eti, datosSesion, sqlSession);
									etiquetasAñadidas++;
								} catch (EtiquetasException
										| EtiquetasConstraintViolationException e) {
									mensajes.add("No se ha podido añadir la etiqueta "+eti.getEtiqueta()+" (Línea"+i+")");
									error = true;
								}
								//Si se ha especificado una categorÃ­a
								if(!"".equals(categoria) && !error){
									
									EtiquetaEnlaceBean etiEnlace = new EtiquetaEnlaceBean();
									etiEnlace.setEstadoBean(Estado.NUEVO);
									etiEnlace.setUidEtiqueta(uidEtiqueta);
									etiEnlace.setIdClase("D_ETIQUETAS_CATEGORIAS_TBL.UID_CAT_ETIQUETA");
									EtiquetaCategoriaBean etiCat = ServicioEtiquetasCategoriasImpl.get().consultarPorCategoria(sqlSession, categoria, datosSesion);
									//Comprobamos si la categoria existe
									if(etiCat == null){
										//Si no existe la categorÃ­a, la creamos
										etiCat = new EtiquetaCategoriaBean();
										etiCat.setEstadoBean(Estado.NUEVO);
										String uidCategoriaEtiqueta = UUID.randomUUID().toString();
										etiCat.setUidCategoriaEtiqueta(uidCategoriaEtiqueta);
										etiCat.setCategoria(categoria);
										etiCat.setAsignacionMultiple(true);							
										try {
											ServicioEtiquetasCategoriasImpl.get().crear(sqlSession, etiCat, datosSesion);
											categoriasAñadidas++;
										} catch (
												EtiquetasException
												| EtiquetasConstraintViolationException
												| EtiquetasEnlacesConstraintViolationException
												| EtiquetasEnlacesException e) {
											mensajes.add("No se ha podido crear la categoría "+etiCat.getCategoria()+" (Línea "+i+")");
											error = true;
										}
									}
									if(!error){
										etiEnlace.setIdObjeto(etiCat.getUidCategoriaEtiqueta());
										
										try {
											ServicioEtiquetasEnlacesImpl.get().salvar(sqlSession, etiEnlace, datosSesion );
										} catch (
												EtiquetasEnlacesConstraintViolationException
												| EtiquetasEnlacesException e) {
											mensajes.add("No se ha podido añadir la etiqueta "+eti.getEtiqueta()+" a la categoría "+etiEnlace.getIdObjeto()+" (Línea "+i+")");
											error = true;
										}
									}
								}
							}else{
								if(!"".equals(categoria)){
									EtiquetaEnlaceBean etiEnlace = new EtiquetaEnlaceBean();
									etiEnlace.setEstadoBean(Estado.NUEVO);
									etiEnlace.setUidEtiqueta(eti.getUidEtiqueta());
									etiEnlace.setIdClase("D_ETIQUETAS_CATEGORIAS_TBL.UID_CAT_ETIQUETA");
									EtiquetaCategoriaBean etiCat = ServicioEtiquetasCategoriasImpl.get().consultarPorCategoria(sqlSession, categoria, datosSesion);
									if(etiCat == null){
										//Si no existe la categorÃ­a, la creamos
										etiCat = new EtiquetaCategoriaBean();
										etiCat.setEstadoBean(Estado.NUEVO);
										String uidCategoriaEtiqueta = UUID.randomUUID().toString();
										etiCat.setUidCategoriaEtiqueta(uidCategoriaEtiqueta);
										etiCat.setCategoria(categoria);
										etiCat.setAsignacionMultiple(true);							
										try {
											ServicioEtiquetasCategoriasImpl.get().crear(sqlSession, etiCat, datosSesion);
											categoriasAñadidas++;
										} catch (
												EtiquetasCategoriasException
												| EtiquetasCategoriasConstraintViolationException | EtiquetasEnlacesConstraintViolationException | EtiquetasEnlacesException e) {
											mensajes.add("No se ha podido crear la categoría "+etiCat.getCategoria()+" (Línea "+i+")");
											error = true;
										}
									}
									etiEnlace.setIdObjeto(etiCat.getUidCategoriaEtiqueta());
									EtiquetaEnlaceBean enlace = ServicioEtiquetasEnlacesImpl.get().consultar(sqlSession, etiEnlace.getIdClase(), etiEnlace.getIdObjeto(), eti.getUidEtiqueta(), datosSesion);
									if(enlace == null){
										try {
											ServicioEtiquetasEnlacesImpl.get().salvar(sqlSession, etiEnlace, datosSesion);
										} catch (
												EtiquetasEnlacesConstraintViolationException
												| EtiquetasEnlacesException e) {
											mensajes.add("No se ha podido añadir la etiqueta "+eti.getEtiqueta()+" a la categoría "+etiEnlace.getIdObjeto()+" (Línea "+i+")");
											error = true;
										}
									}
								}
							}
						
						if(!error){
							EtiquetaEnlaceBean enlace = ServicioEtiquetasEnlacesImpl.get().consultar(sqlSession, idClase, idObjeto, eti.getUidEtiqueta(), datosSesion);
							if(enlace == null){
								enlace = new EtiquetaEnlaceBean();
								enlace.setEstadoBean(Estado.NUEVO);
								enlace.setUidEtiqueta(eti.getUidEtiqueta());
								enlace.setIdObjeto(idObjeto);
								enlace.setIdClase(idClase);
								enlace.setUidActividad(datosSesion.getUidActividad());
								try {
									ServicioEtiquetasEnlacesImpl.get().salvar(sqlSession, enlace, datosSesion);
									if (objeto instanceof FidelizadoBean) {
										fidelizadoVersion.addFidelizado(Numero.desformateaLong(idObjeto, null));
									}
								} catch (EtiquetasEnlacesConstraintViolationException
										| EtiquetasEnlacesException e) {
									añadirMensajesError(idClase, idObjeto, objeto, eti, mensajes, i, true);
								}
							}else{
								añadirMensajesEnlacesYaAñadidos(idClase, idObjeto, objeto, eti, mensajes, i);
							}
						}else{
							añadirMensajesError(idClase, idObjeto, objeto, eti, mensajes, i, true);
						}
					}
						lineasImportadas++;
				}
			}else if("X".equals(desasignar.toUpperCase())){
				lineasTotalesEliminadas++;
				
				EtiquetaBean eti = ServicioEtiquetasImpl.get().consultarEtiquetaEnlazada(sqlSession, datosSesion, etiquetaXSL);
				if(eti.getUidEtiqueta() != null){
					EtiquetaEnlaceBean etiquetaEnlace = new EtiquetaEnlaceBean();
					etiquetaEnlace.setIdClase(idClase);
					etiquetaEnlace.setIdObjeto(idObjeto);
					etiquetaEnlace.setUidEtiqueta(eti.getUidEtiqueta());
					
					try {
						ServicioEtiquetasEnlacesImpl.get().eliminar(sqlSession, etiquetaEnlace, datosSesion);
						lineasEliminadas++;
						desasignadas.add(eti);
						if("F_FIDELIZADOS_TBL.ID_FIDELIZADO".equals(idClase)) {
							fidelizadoVersion.addFidelizado(Numero.desformateaLong(idObjeto, null));
						}
					} catch (EtiquetasEnlacesConstraintViolationException
							| EtiquetasEnlacesException e) {
						añadirMensajesError(idClase, idObjeto, objeto, eti, mensajes, i, false);
					}
				}else{
					mensajes.add("No existe la etiqueta "+etiquetaXSL+ " (Línea "+i+")");
				}
			}else{
				mensajes.add("Error en el formato (Línea "+i+")");
			}
			
		}
		//Actualizamos la versiÃ³n de los artÃ­culos enlazados con las etiquetas
		Long contadorVersion = ContadorArticulos.obtenerContador(conn, datosSesion);
		for(ArticuloBean articulo : articulosAVersionar.values()){
			articulo.setFechaVersion(new Date());
				
			articulo.setVersion(contadorVersion);					
			
			ArticulosDao.actualizarVersion(conn, datosSesion.getConfigEmpresa(), articulo);		
		}
		
		//Eliminamos las etiquetas desasignadas que no tengan enlaces.
		ServicioEtiquetasImpl.get().eliminarEtiquetasSinEnlace(datosSesion,sqlSession, desasignadas);
		
		CustomFidelizadoVersionControlServiceImpl.get().checkFidelizadosVersion(datosSesion, fidelizadoVersion);
		
		mensajes.add("");
		if(categoriasAñadidas > 1){
			mensajes.add("Añadidas "+categoriasAñadidas+" categorías nuevas");
		}
		else if(categoriasAñadidas == 1){
			mensajes.add("Añadida "+categoriasAñadidas+" categoría nueva");
		}
		if(etiquetasAñadidas > 1){
			mensajes.add("Añadidas "+etiquetasAñadidas+" etiquetas nuevas");
		}
		else if(etiquetasAñadidas == 1){
			mensajes.add("Añadida "+etiquetasAñadidas+" etiqueta nueva");
		}
		mensajes.add("Importadas "+lineasImportadas+" de "+lineasTotalesImportadas+" lineas de forma correcta");
		mensajes.add("Eliminadas "+lineasEliminadas+" de "+lineasTotalesEliminadas+" lineas de forma correcta");
		return mensajes;
	}
	
	
}
