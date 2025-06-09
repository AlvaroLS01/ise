package com.comerzzia.instoreengine.herbnav.servicios.general.articulos.generaciondatos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import com.comerzzia.core.model.empresas.ConfigEmpresaBean;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.servicios.sesion.IDatosSesion;
import com.comerzzia.core.util.base.Estado;
import com.comerzzia.core.util.db.Connection;
import com.comerzzia.core.util.db.Database;
import com.comerzzia.core.util.numeros.BigDecimalUtil;
import com.comerzzia.instoreengine.herbnav.persistencia.codigoBarras.HNACodBarDao;
import com.comerzzia.instoreengine.herbnav.persistencia.general.articulos.sincronizacion.HNAArticulosSincronizacionDao;
import com.comerzzia.model.general.articulos.ArticuloBean;
import com.comerzzia.model.general.articulos.VersionArticuloBean;
import com.comerzzia.model.general.articulos.codigosbarras.CodigoBarrasArticuloBean;
import com.comerzzia.model.general.articulos.generaciondatos.dto.GeneracionDatosArticuloDTO;
import com.comerzzia.model.general.articulos.generaciondatos.dto.GeneracionDatosArticuloKeyDTO;
import com.comerzzia.model.general.articulos.generaciondatos.dto.GeneracionDatosArticulosDTO;
import com.comerzzia.model.general.articulos.unidadesmedidas.UnidadMedidaArticuloBean;
import com.comerzzia.model.general.unidadesmedida.etiquetas.UnidadesMedidaEtiquetasBean;
import com.comerzzia.model.ventas.promociones.articulos.ArticuloPromocionBean;
import com.comerzzia.model.ventas.tarifas.TarifaBean;
import com.comerzzia.model.ventas.tarifas.articulos.ArticuloTarifaBean;
import com.comerzzia.model.versionado.VersionadoBean;
import com.comerzzia.model.versionado.VersionadoKey;
import com.comerzzia.persistencia.general.articulos.ParametrosBuscarArticulosBean;
import com.comerzzia.persistencia.general.articulos.sincronizacion.ArticulosSincronizacionDao;
import com.comerzzia.persistencia.ventas.promociones.articulos.ParametrosBuscarArticulosPromocionesBean;
import com.comerzzia.persistencia.ventas.tarifas.TarifasDao;
import com.comerzzia.servicios.general.articulos.ArticuloException;
import com.comerzzia.servicios.general.articulos.ArticuloNotFoundException;
import com.comerzzia.servicios.general.articulos.codigosbarras.CodigoBarrasArticuloException;
import com.comerzzia.servicios.general.articulos.codigosbarras.CodigoBarrasArticuloNotFoundException;
import com.comerzzia.servicios.general.articulos.generaciondatos.GeneracionDatosArticuloException;
import com.comerzzia.servicios.general.articulos.generaciondatos.ServicioGeneracionDatosArticulosImpl;
import com.comerzzia.servicios.general.unidadesmedida.etiquetas.UnidadesMedidaEtiquetaException;
import com.comerzzia.servicios.general.unidadesmedida.etiquetas.UnidadesMedidaEtiquetaNotFoundException;
import com.comerzzia.servicios.ventas.promociones.articulos.ArticuloPromocionException;
import com.comerzzia.servicios.ventas.tarifas.articulos.ArticuloTarifaException;
import com.comerzzia.servicios.ventas.tarifas.articulos.ArticuloTarifaNotFoundException;

@Service
//@Primary
public class HNAServicioGeneracionDatosArticulosImpl extends ServicioGeneracionDatosArticulosImpl{
	private Long versionMaxArticulos;
	
	private Long versionMaxTarifa;
	
	@SuppressWarnings("deprecation")
	@Override
	public GeneracionDatosArticulosDTO selectItemDataByVersion(IDatosSesion sessionData, String classId, String rateCode, Date vigency, ParametrosBuscarArticulosBean param)
	        throws GeneracionDatosArticuloException, SQLException {
		java.sql.Connection connSql = Database.getConnection();
		Connection conn = new Connection(connSql);
		
		GeneracionDatosArticulosDTO result = new GeneracionDatosArticulosDTO();
		
		List<CodigoBarrasArticuloBean> barcodesRate = new ArrayList<CodigoBarrasArticuloBean>();
		
		if(vigency == null) {
			vigency = new Date();
		}
		
		try {
			log.debug("selectByVersion() - Generando datos de artículos para la clase "+classId+", tarifa "+rateCode+" y vigencia "+sdf.format(vigency));
			// Obtenemos la versión actual de artículos
			
			ConfigEmpresaBean config = new ConfigEmpresaBean();
			config.setUidActividad(sessionData.getUidActividad());
			
			VersionArticuloBean versionArticulos = ArticulosSincronizacionDao.consultarVersionActual(conn, config);
			versionMaxArticulos = versionArticulos.getVersion();
		
//			// Obtenemos los datos de la tarifa
			TarifaBean tarifa = TarifasDao.consultarEnTienda(conn, config, rateCode);
			versionMaxTarifa = tarifa.getVersion();
			
			VersionadoKey key = new VersionadoKey();
			key.setUidActividad(sessionData.getUidActividad());
			key.setIdClase(classId);
			
			// Si es una actualización forzada, no mirar las versiones y hacer una consulta completa
//			if (versionArticuloActual.getVersion()!= 0) {
////				ParametrosBuscarArticulosBean paramArticulos = new ParametrosBuscarArticulosBean();
////				paramArticulos.setNumPagina(1);
////				paramArticulos.setTamañoPagina(10000000);
////				log.debug("selectByVersion() - Actualización forzada. Se consultan todos los artículos");
////				barcodes = consultarCodigosBarras(conn, paramArticulos, (DatosSesionBean) sessionData, versionMaxArticulos);
//				
//			} else {
//				
//			}
			key.setIdObjeto("VERSION_TARIFA");
			
			VersionadoBean versionTarifaActual = servicioVersionado.selectByPrimaryKey(key);
			
			if (versionTarifaActual == null) {
				versionTarifaActual = new VersionadoBean();
				versionTarifaActual.setEstadoBean(Estado.NUEVO);
				versionTarifaActual.setUidActividad(sessionData.getUidActividad());
				versionTarifaActual.setIdClase(classId);
				versionTarifaActual.setIdObjeto("VERSION_TARIFA");
				versionTarifaActual.setVersion(0L);
			}
			
			String codTienda = ((DatosSesionBean)sessionData).getAtributos().get("CODALMACEN_SELECCIONADO").toString();
			
			// Obtenemos los artículos por cambios en tarifa
			barcodesRate = HNAArticulosSincronizacionDao.findBarcodesTagSync(conn, config, versionTarifaActual.getVersion(), versionMaxTarifa, tarifa.getCodTar(), codTienda);
			HashSet<GeneracionDatosArticuloKeyDTO> articulosKeySet = new HashSet<GeneracionDatosArticuloKeyDTO>();
			
			processBarcodeList(articulosKeySet, barcodesRate);
			
			result = HNAcontruyeArticulosDTO(conn, sessionData, articulosKeySet, rateCode, vigency, param, versionMaxArticulos, versionMaxTarifa);
		} catch (Exception e) {
			String msg = "Ha ocurrido un error generando los datos de artículos para la clase "+classId+", tarifa "+rateCode+" y vigencia "+sdf.format(vigency);
			log.error("selectByVersion() - "+msg, e);
			throw new GeneracionDatosArticuloException(msg);
		} finally {
			conn.close();
		}
		
		return result;
	}
	
	protected GeneracionDatosArticulosDTO HNAcontruyeArticulosDTO(Connection conn, IDatosSesion datosSesion, HashSet<GeneracionDatosArticuloKeyDTO> articulosKeySet, String codtar, Date vigencia, ParametrosBuscarArticulosBean param, Long versionMaxArticulos, Long versionMaxTarifa) throws ArticuloNotFoundException, ArticuloException, ArticuloTarifaException, ArticuloPromocionException, ArticuloTarifaNotFoundException, UnidadesMedidaEtiquetaException, UnidadesMedidaEtiquetaNotFoundException, SQLException {
		GeneracionDatosArticulosDTO result = new GeneracionDatosArticulosDTO();
		for(GeneracionDatosArticuloKeyDTO key : articulosKeySet) {
			GeneracionDatosArticuloDTO dto = construyeArticuloDTO(conn, datosSesion, key, codtar, vigencia, param);
			if(dto != null) {
				result.getArticulos().add(dto);
			}	
		}
		result.setMaxVersionArticulo(versionMaxArticulos);
		result.setMaxVersionTarifa(versionMaxTarifa);
		
		return result;
	}

		protected GeneracionDatosArticuloDTO construyeArticuloDTO(Connection conn, IDatosSesion datosSesion, GeneracionDatosArticuloKeyDTO key, String codtar, Date vigencia, ParametrosBuscarArticulosBean param) throws ArticuloNotFoundException, ArticuloException, ArticuloTarifaException, ArticuloTarifaNotFoundException, SQLException, UnidadesMedidaEtiquetaException, UnidadesMedidaEtiquetaNotFoundException, ArticuloPromocionException {
			GeneracionDatosArticuloDTO dto = null;
			
			ArticuloBean articulo = servicioArticulos.consultarEnTienda(conn, key.getCodart(), datosSesion);
			
			if(param == null || (param != null && cumpleFiltro(param, articulo))) {
				dto = new GeneracionDatosArticuloDTO();
				ArticuloTarifaBean articuloTarifa = null;
				try {
					articuloTarifa = servicioArticulosTarifas.consultarArticuloTarifaEnTienda(conn, codtar, key.getCodart(), key.getDesglose1(), key.getDesglose2(), vigencia, ((DatosSesionBean) datosSesion).getConfigEmpresa());
				}catch(Exception ignore) {}
				CodigoBarrasArticuloBean ean = null;
				try {
					ean = servicioCodigosBarrasArticulos.consultarEanPorDesgloses(conn, key.getCodart(), key.getDesglose1(), key.getDesglose2(), (DatosSesionBean) datosSesion);
				}catch(CodigoBarrasArticuloNotFoundException ignore) {}
				
				BigDecimal unidadesCaja = BigDecimal.ONE;
					UnidadMedidaArticuloBean umArticulo = servicioUnidadesMedidasArticulos.consultarUnidadMedidaEnTienda(conn, datosSesion, key.getCodart(), "CAJA");
					if(umArticulo != null) {
						unidadesCaja = new BigDecimal(umArticulo.getFactorConversion().toString());
					}
				UnidadesMedidaEtiquetasBean unidadMedidaEtiqueta = null;
				if(articulo.getCodUnidadMedidaEtiq() != null && !articulo.getCodUnidadMedidaEtiq().isEmpty()) {
					try {
						unidadMedidaEtiqueta = servicioUnidadesMedidasEtiquetas.consultar(articulo.getCodUnidadMedidaEtiq(), (DatosSesionBean) datosSesion);
					}catch(UnidadesMedidaEtiquetaNotFoundException ignore) {}
				}

				
				ArticuloPromocionBean articuloPromocion = null;
				
				ParametrosBuscarArticulosPromocionesBean paramArticulosPromociones = new ParametrosBuscarArticulosPromocionesBean();
				paramArticulosPromociones.setCodTar(codtar);
				paramArticulosPromociones.setCodArt(key.getCodart());
				paramArticulosPromociones.setDesglose1(key.getDesglose1());
				paramArticulosPromociones.setDesglose2(key.getDesglose2());
				paramArticulosPromociones.setVigencia(vigencia);
				paramArticulosPromociones.setOrden("PRECIO_TOTAL");
				List<ArticuloPromocionBean> articulosPromociones = servicioArticulosPromociones.consultarArticulosPromocionEnTienda(paramArticulosPromociones, (DatosSesionBean) datosSesion);
				if(!articulosPromociones.isEmpty()) {
					articuloPromocion = articulosPromociones.get(0);
				}
				dto.setUidActividad(datosSesion.getUidActividad());
				dto.setCodart(key.getCodart());
				dto.setDesglose1(key.getDesglose1());
				dto.setDesglose2(key.getDesglose2());
				dto.setCodtar(codtar);
				
				dto.setVersion(articulo.getVersion());
				dto.setDesart(articulo.getDesArticulo());
				dto.setCodcat(articulo.getCodCategorizacion());
				dto.setCodfam(articulo.getCodFamilia());
				dto.setCodseccion(articulo.getCodSeccion());
				dto.setBalanzaPlu(articulo.getBalanzaPlu());
				dto.setBalanzaSeccion(articulo.getBalanzaSeccion());
				dto.setBalanzaTipoArt(articulo.getBalanzaTipoArticulo());	
				dto.setCodmarca(articulo.getCodMarca());	
				dto.setDescat(articulo.getDesCategorizacion());
				// dto.setDesfam(articulo.getDesFamilia());

				// Se utilizará Desmarca para hacer referencia al campo referencia_proveedor
				dto.setDesmarca(articulo.getReferencia());

				// dto.setDesseccion(articulo.getDesSeccion());
				dto.setCodUMEtiqueta(articulo.getCodUnidadMedidaEtiq());
				dto.setDesUMEtiqueta(articulo.getDesetiqueta());
				if(articulo.getCantidadUnidadMedidaEtiq() != null) {
					dto.setCantidadUMEtiqueta(new BigDecimal(articulo.getCantidadUnidadMedidaEtiq().toString()));
				}
				
				if(ean != null) {
					dto.setEan(ean.getCodigoBarras());
					dto.setEanPrincipal(ean.isPrincipal());
				}
				
				if(articuloTarifa != null) {
					dto.setFechaInicioTarifa(articuloTarifa.getFechaInicio());
					dto.setPrecioTotal(new BigDecimal(articuloTarifa.getPrecioTotal().toString()));
					dto.setPrecioVenta(new BigDecimal(articuloTarifa.getPrecioVenta().toString()));
				}
				
				
				BigDecimal precioTotalUnidadUMEtiqueta = dto.getPrecioTotal();
				if(articuloPromocion != null) {
					dto.setIdPromocion(articuloPromocion.getIdPromocion());
					if(articuloPromocion.getPrecioTotal() != null) {
						dto.setPrecioPromocion(new BigDecimal(articuloPromocion.getPrecioTotal().toString()));
						precioTotalUnidadUMEtiqueta = dto.getPrecioPromocion();
					}
					
					dto.setFechaFinPromocion(articuloPromocion.getFechaFin());
					dto.setFechaInicioPromocion(articuloPromocion.getFechaInicio());
					
				}
				
				
				BigDecimal precioUMEtiqueta = null;
				if(unidadMedidaEtiqueta != null && precioTotalUnidadUMEtiqueta != null && dto.getCantidadUMEtiqueta() != null) {
					if(BigDecimalUtil.isIgualACero(dto.getCantidadUMEtiqueta())) {
						precioUMEtiqueta = BigDecimal.ZERO;
					}else {
						precioUMEtiqueta = new BigDecimal(unidadMedidaEtiqueta.getFactor().toString()).multiply(precioTotalUnidadUMEtiqueta).divide(dto.getCantidadUMEtiqueta(), 2, RoundingMode.HALF_UP);
					}
					
				}
				dto.setPrecioUMEtiqueta(precioUMEtiqueta);
				dto.setUnidadesCaja(unidadesCaja);
				
				dto.setMapaPropiedadesDinamicas(construyeMapaPropiedades(datosSesion, key.getCodart()));
				
				
				
			}

			return dto;
	}
	
	public List<CodigoBarrasArticuloBean> consultarCodigosBarras(Connection conn, ParametrosBuscarArticulosBean param, DatosSesionBean datosSesion, Long versionMaxArticulos) 
			throws CodigoBarrasArticuloException {
		try {
			log.debug("consultarCodigosBarras() - Consultando codigos de barras por filtro de artículos ");
			String codTienda = datosSesion.getAtributos().get("CODALMACEN_SELECCIONADO").toString();
			return HNACodBarDao.consultar(conn, datosSesion.getConfigEmpresa(), param, codTienda, versionMaxArticulos);
		}
		catch(SQLException e){
			String msg = "Error consultando códigos de barras: " + e.getMessage();
    		log.error("consultarCodigosBarras() - " + msg);
    		
    		throw new CodigoBarrasArticuloException(msg, e);
		}
	}

}
