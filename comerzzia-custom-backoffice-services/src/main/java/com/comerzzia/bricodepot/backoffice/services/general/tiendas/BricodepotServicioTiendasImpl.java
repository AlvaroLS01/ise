package com.comerzzia.bricodepot.backoffice.services.general.tiendas;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.comerzzia.bricodepot.backoffice.persistence.general.tiendas.atcud.AtcudMagento;
import com.comerzzia.bricodepot.backoffice.persistence.general.tiendas.atcud.Exception.BricodepotAtcudException;
import com.comerzzia.bricodepot.backoffice.services.general.tiendas.atcud.BricodepotServicioAtcud;
import com.comerzzia.core.servicios.etiquetas.EtiquetasConstraintViolationException;
import com.comerzzia.core.servicios.etiquetas.EtiquetasException;
import com.comerzzia.core.servicios.mediospago.configuracion.ConfiguracionMedioPagoConstraintViolationException;
import com.comerzzia.core.servicios.mediospago.configuracion.ConfiguracionMedioPagoException;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.servicios.sincronizacion.SincronizadorTiendas;
import com.comerzzia.core.servicios.sincronizacion.TrabajoSincronizadorException;
import com.comerzzia.core.servicios.sincronizacion.datos.DatosSincronizadorException;
import com.comerzzia.core.servicios.usuarios.UsuarioConstraintViolationException;
import com.comerzzia.core.servicios.usuarios.UsuarioException;
import com.comerzzia.core.servicios.usuarios.tiendas.AlmacenUsuarioConstraintViolationException;
import com.comerzzia.core.servicios.usuarios.tiendas.AlmacenUsuarioException;
import com.comerzzia.core.util.base.Estado;
import com.comerzzia.core.util.base.KeyConstraintViolationException;
import com.comerzzia.core.util.db.Connection;
import com.comerzzia.core.util.db.Database;
import com.comerzzia.core.util.xml.XMLDocument;
import com.comerzzia.core.util.xml.XMLDocumentNode;
import com.comerzzia.model.general.tiendas.variables.VariableTienda;
import com.comerzzia.persistencia.general.almacenes.AlmacenesDao;
import com.comerzzia.persistencia.general.tiendas.TiendasDao;
import com.comerzzia.servicios.general.almacenes.AlmacenConstraintViolationException;
import com.comerzzia.servicios.general.almacenes.AlmacenException;
import com.comerzzia.servicios.general.almacenes.ClienteAlmacenConstraintViolationException;
import com.comerzzia.servicios.general.almacenes.ServicioAlmacenesImpl;
import com.comerzzia.servicios.general.clientes.ClienteConstraintViolationException;
import com.comerzzia.servicios.general.clientes.ClienteException;
import com.comerzzia.servicios.general.clientes.ServicioClientesImpl;
import com.comerzzia.servicios.general.tiendas.ServicioTiendasImpl;
import com.comerzzia.servicios.general.tiendas.Tienda;
import com.comerzzia.servicios.general.tiendas.TiendaConstraintViolationException;
import com.comerzzia.servicios.general.tiendas.TiendaException;
import com.comerzzia.servicios.general.tiendas.mediospago.MedioPagoTiendaConstraintViolationException;
import com.comerzzia.servicios.general.tiendas.mediospago.MedioPagoTiendaException;
import com.comerzzia.servicios.general.tiendas.servicios.ServicioTiendaConstraintViolationException;
import com.comerzzia.servicios.general.tiendas.variables.VariableTiendaConstraintViolationException;
import com.comerzzia.servicios.general.tiendas.variables.VariableTiendaException;
import com.comerzzia.servicios.ventas.tarifas.TarifaException;
import com.comerzzia.servicios.ventas.tarifas.TarifaNotFoundException;

@SuppressWarnings("deprecation")
public class BricodepotServicioTiendasImpl extends ServicioTiendasImpl {

	
	protected static Logger log = Logger.getLogger(BricodepotServicioTiendasImpl.class);
	
@SuppressWarnings("unchecked")
public void salvarRegistrosAtcudTienda(Tienda tienda, Connection conn, DatosSesionBean datosSesion) throws BricodepotAtcudException {
	log.debug("salvarRegistrosAtcudTienda() - Salvando atcuds de tienda de la tienda: " + tienda.getCodAlm());
	
	if (tienda.getExtension("AtcudsTienda") == null) {
		log.debug("salvarRegistrosAtcudTienda() - No existen atcuds de tienda en las extensiones de la tienda: " + tienda.getCodAlm());
		return;
	}
	List<AtcudMagento> lista =( List<AtcudMagento>) tienda.getExtension("AtcudsTienda");
	
	if (lista.isEmpty()) {
		log.debug("salvarRegistrosAtcudTienda() - No existen atcuds de tienda en las extensiones de la tienda: " + tienda.getCodAlm());
		return;
	}
	
	for (AtcudMagento atcudTienda : lista) {
		atcudTienda.setUidActividad(datosSesion.getUidActividad());
		atcudTienda.setCodalm(tienda.getCodAlm());
		if (atcudTienda.getEstadoBean() == Estado.BORRADO) {
			
			log.debug("eliminarAtcudTienda() - Eliminando atcud  tienda para la tienda: " + atcudTienda.getCodalm());
			BricodepotServicioAtcud.get().eliminar(conn, atcudTienda, datosSesion);
		}
		else if (atcudTienda.getEstadoBean() == Estado.NUEVO) {
		
			log.debug("guardaAtcudTienda() - Guardando atcud para la tienda: " + atcudTienda.getCodalm());
			BricodepotServicioAtcud.get().crear(conn, atcudTienda, datosSesion);
		}
	}
}
@Override
public void salvar(Tienda tienda, DatosSesionBean datosSesion) 
		throws TiendaException, TiendaConstraintViolationException, AlmacenException, AlmacenConstraintViolationException, ClienteAlmacenConstraintViolationException, 
				ClienteException, ClienteConstraintViolationException, TarifaException, TarifaNotFoundException, AlmacenUsuarioException, 
				AlmacenUsuarioConstraintViolationException, MedioPagoTiendaException, MedioPagoTiendaConstraintViolationException, 
				EtiquetasException, EtiquetasConstraintViolationException {

	switch (tienda.getEstadoBean()) {
		case Estado.NUEVO:
			crear(tienda, datosSesion);
			break;
      
		case Estado.MODIFICADO:
			modificar(tienda, datosSesion);
			break;
      
		case Estado.BORRADO:
			eliminar(tienda.getCodAlm(), datosSesion);
	}
}

	@Override
	protected void salvarVariablesTienda(Tienda tienda, Connection conn, DatosSesionBean datosSesion) throws VariableTiendaConstraintViolationException, VariableTiendaException {
	
		crearVariablesTextoInformativoLegal(tienda);
	
		super.salvarVariablesTienda(tienda, conn, datosSesion);
	}
	
	private void crearVariablesTextoInformativoLegal(Tienda tienda) {
		VariableTienda[] variablesLegales = obtenerVariablesTextoInformativoLegal(tienda);
	
		separarVariablesTextoInformativoLegal(variablesLegales);
	}
	
	private void separarVariablesTextoInformativoLegal(VariableTienda[] variablesLegales) {
		VariableTienda variablePrincipal = variablesLegales[0];
		if (variablePrincipal != null && variablePrincipal.getValor() != null) {
			String textoCompleto = variablePrincipal.getValor();
			int longitudmaxima = 250;
	
			for (int i = 0; i < variablesLegales.length; i++) {
				int inicio = i * longitudmaxima;
				if (textoCompleto.length() > inicio && variablesLegales[i] != null) {
					int fin = textoCompleto.length() > (i + 1) * longitudmaxima ? (i + 1) * longitudmaxima : textoCompleto.length();
					variablesLegales[i].setValor(textoCompleto.substring(inicio, fin));
					actualizarEstado(variablesLegales[i]);
				}
			}
		}
	}
	
	private VariableTienda[] obtenerVariablesTextoInformativoLegal(Tienda tienda) {
		VariableTienda[] variablesLegales = new VariableTienda[4];
		for (VariableTienda variable : tienda.getVariables()) {
			switch (variable.getIdVariable()) {
				case "X_POS.TEXTO_INFORMATIVO_LEGAL":
					variablesLegales[0] = variable;
					break;
				case "X_POS.TEXTO_INFORMATIVO_LEGAL_2":
					variablesLegales[1] = variable;
					variable.setValor(" ");
					break;
				case "X_POS.TEXTO_INFORMATIVO_LEGAL_3":
					variablesLegales[2] = variable;
					variable.setValor(" ");
					break;
				case "X_POS.TEXTO_INFORMATIVO_LEGAL_4":
					variablesLegales[3] = variable;
					variable.setValor(" ");
					break;
			}
		}
		return variablesLegales;
	}
	
	private void actualizarEstado(VariableTienda variable) {
	    String valor = variable.getValor();
	    if (variable.getEstadoBean() == -1) {
	        variable.setEstadoBean(valor.isEmpty() ? -1 : Estado.NUEVO);
	    } else if (variable.getEstadoBean() == Estado.MODIFICADO) {
	        variable.setEstadoBean(valor.isEmpty() ? Estado.BORRADO : Estado.MODIFICADO);
	    }
	}


	@Override
	public void crear(Tienda tienda, DatosSesionBean datosSesion) throws TiendaException, TiendaConstraintViolationException {
		Connection conn = new Connection();
		
		try {
			log.debug("crear() - Creando nueva tienda");
			
			conn.abrirConexion(Database.getConnection());
			conn.iniciaTransaccion();
			
			// Crear el cliente si así se decició en el asistente
			if(tienda.getCliente() != null) {
				ServicioClientesImpl.get().crear(conn, tienda.getCliente(), datosSesion);
				tienda.setCodCliente(tienda.getCliente().getCodCli());
			}
			
			if(!tienda.getUsuariosNuevos().isEmpty()){
				crearUsuariosNuevos(tienda, datosSesion, conn);
			}
			
			if(tienda.isRemota()){
				XMLDocument doc = tienda.getConfiguracion();
				doc.setRoot(new XMLDocumentNode(doc, "sincronizacion"));
			}
			
			//Isertamos primero el almacen y despues la tienda relacionada	
			ServicioAlmacenesImpl.get().crear(obtenerAlmacen(tienda), datosSesion, conn);
			
			TiendasDao.insert(conn, datosSesion.getConfigEmpresa(), tienda.getBean());
			
			salvarEtiquetas(tienda, conn, datosSesion);
			salvarTarifasTienda(tienda, conn, datosSesion);
			salvarTrabajosTipoTienda(tienda, conn, datosSesion);
			salvarUsuariosTienda(tienda, conn, datosSesion);
			salvarCajasTienda(tienda, conn, datosSesion);
			salvarServiciosTienda(tienda, conn, datosSesion);
			salvarMediosPagoTienda(tienda, conn, datosSesion);
			salvarConfiguracionMedioPago(tienda, conn, datosSesion);
			salvarVariablesTienda(tienda, conn, datosSesion);
			salvarRegistrosAtcudTienda(tienda,conn,datosSesion);
			conn.commit();
			conn.finalizaTransaccion();
			
			try {
				//Por último registramos en el sincronizador los trabajos de la tienda creada
				SincronizadorTiendas.getInstance().actualizarTrabajos();
			}
			catch (Exception ignore) {
			} 
			
		}
		catch (KeyConstraintViolationException e) {
			conn.deshacerTransaccion();
			
			log.info("crear() - No se ha podido crear la tienda: " + e.getMessage());
			throw new TiendaConstraintViolationException(datosSesion.getTranslation()._("Error creando nueva tienda:") + " " + e.getDefaultMessage(datosSesion.getTranslation()));
		}
		catch (ClienteException e) {
			conn.deshacerTransaccion();
			
			log.info("crear() - No se ha podido crear el cliente: " + e.getMessage());
			throw new TiendaException(e.getMessage());
		}
		catch (ClienteConstraintViolationException e) {
			conn.deshacerTransaccion();
			
			log.info("crear() - No se ha podido crear el cliente: " + e.getMessage());
			throw new TiendaConstraintViolationException(e.getMessage());
		}
		catch (UsuarioException e) {
			conn.deshacerTransaccion();
			
			log.info("crear() - No se ha podido crear el nuevo usuario: " + e.getMessage());
			throw new TiendaException(e.getMessage());
		}
		catch (UsuarioConstraintViolationException e) {
			conn.deshacerTransaccion();
			
			log.info("crear() - No se ha podido crear el nuevo usuario: " + e.getMessage());
			throw new TiendaConstraintViolationException(e.getMessage());
		}
		catch (AlmacenException e) {
			conn.deshacerTransaccion();
			
			log.info("crear() - No se ha podido crear el almacén: " + e.getMessage());
			throw new TiendaConstraintViolationException(e.getMessage());
		}
		catch (AlmacenConstraintViolationException e) {
			conn.deshacerTransaccion();
			
			log.info("crear() - No se ha podido crear el almacén: " + e.getMessage());
			throw new TiendaConstraintViolationException(e.getMessage());
		}
		catch (ServicioTiendaConstraintViolationException e) {
			conn.deshacerTransaccion();
			
			log.info("crear() - No se ha podido crear el almacén: " + e.getMessage());
			throw new TiendaConstraintViolationException(e.getMessage());
		}
		catch (ConfiguracionMedioPagoConstraintViolationException e){
			conn.deshacerTransaccion();
			
			log.info("crear() - No se ha podido crear la configuración del medio de pago: " + e.getMessage());
			throw new TiendaConstraintViolationException(e.getMessage());
		}
		catch (ConfiguracionMedioPagoException e){
			conn.deshacerTransaccion();
			
			log.info("crear() - No se ha podido crear la configuración del medio de pago: " + e.getMessage());
			throw new TiendaException(e.getMessage());
		}
		catch (TrabajoSincronizadorException e) {
			conn.deshacerTransaccion();
			
			log.info("crear() - No se han podido crear los trabajos de sincronizacion: " + e.getMessage());
			throw new TiendaException(e.getMessage());
		}
		catch (SQLException e) {
			conn.deshacerTransaccion();
			
			String msg = "Error creando nueva tienda: " + e.getMessage();
			log.error("crear() - " + msg);
			
			throw new TiendaException(msg, e);
		}
		catch (Exception e) {
			conn.deshacerTransaccion();
			
			String msg = "Error creando nueva tienda: " + e.getMessage();
			log.error("crear() - " + msg, e);
			
			throw new TiendaException(msg, e);
		}
		finally {
			conn.cerrarConexion();
		}
	}
	
	@Override
	public void modificar(Tienda tienda, DatosSesionBean datosSesion) throws TiendaException, TiendaConstraintViolationException, AlmacenUsuarioException, AlmacenUsuarioConstraintViolationException,
	        MedioPagoTiendaException, MedioPagoTiendaConstraintViolationException, EtiquetasException, EtiquetasConstraintViolationException {
	Connection conn = new Connection();
		
		try {
			log.debug("modificar() - Modificando tienda " + tienda.getCodAlm());
			conn.abrirConexion(Database.getConnection());
			conn.iniciaTransaccion();
			
			XMLDocument doc = tienda.getConfiguracion();
			Element root = doc.getDocument().getDocumentElement();
			if(tienda.isRemota() && root==null){				
				doc.setRoot(new XMLDocumentNode(doc, "sincronizacion"));			
			}
			
			AlmacenesDao.updateAlmacenTienda(conn, datosSesion.getConfigEmpresa(), obtenerAlmacen(tienda));
			TiendasDao.update(conn, datosSesion.getConfigEmpresa(), tienda.getBean());
			salvarEtiquetas(tienda, conn, datosSesion);
			salvarTarifasTienda(tienda, conn, datosSesion);
			salvarUsuariosTienda(tienda, conn, datosSesion);
			salvarCajasTienda(tienda, conn, datosSesion);
			salvarServiciosTienda(tienda, conn, datosSesion);
			salvarMediosPagoTienda(tienda, conn, datosSesion);
			salvarConfiguracionMedioPago(tienda, conn, datosSesion);
			salvarVariablesTienda(tienda, conn, datosSesion);
			salvarRegistrosAtcudTienda(tienda,conn,datosSesion);
			
			conn.commit();
			conn.finalizaTransaccion();
		}
		catch (SQLException e) {
			conn.deshacerTransaccion();
			
			String msg = "Error modificando tienda: " + e.getMessage();
    		log.error("modificar() - " + msg);
    		
    		throw new TiendaException(msg, e);
    	} catch (DatosSincronizadorException e) {
    		conn.deshacerTransaccion();
			
			String msg = "Error modificando tienda: " + e.getMessage();
    		log.error("modificar() - " + msg);
    		
    		throw new TiendaException(msg, e);
		}
		catch (BricodepotAtcudException e) {
			conn.deshacerTransaccion();
			
			String msg = "Error modificando tienda: " + e.getMessage();
    		log.error("modificar() - " + msg);
    		
    		throw new TiendaException(msg, e);
		}
    	finally {
    		conn.cerrarConexion();
    	}
	}
}
