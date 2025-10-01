package com.comerzzia.bricodepot.backoffice.services.general.clientes;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.comerzzia.bricodepot.backoffice.persistence.general.clientes.BricodepotClienteBean;
import com.comerzzia.bricodepot.backoffice.persistence.general.clientes.BricodepotClientesDao;
import com.comerzzia.core.model.empresas.ConfigEmpresaBean;
import com.comerzzia.core.servicios.contadores.ContadorException;
import com.comerzzia.core.servicios.contadores.ServicioContadoresImpl;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.servicios.sesion.IDatosSesion;
import com.comerzzia.core.util.base.Estado;
import com.comerzzia.core.util.base.KeyConstraintViolationException;
import com.comerzzia.core.util.db.Connection;
import com.comerzzia.core.util.db.Database;
import com.comerzzia.core.util.paginacion.PaginaResultados;
import com.comerzzia.model.general.clientes.ClienteBean;
import com.comerzzia.model.versionado.VersionadoBean;
import com.comerzzia.persistencia.general.clientes.ClientesDao;
import com.comerzzia.persistencia.general.clientes.ParametrosBuscarClientesBean;
import com.comerzzia.servicios.general.clientes.ClienteConstraintViolationException;
import com.comerzzia.servicios.general.clientes.ClienteException;
import com.comerzzia.servicios.general.clientes.ClienteNotFoundException;
import com.comerzzia.servicios.general.clientes.ServicioClientesImpl;
import com.comerzzia.servicios.versionado.ServicioVersionadoImpl;
import com.comerzzia.servicios.versionado.VersionadoException;

@SuppressWarnings("deprecation")
public class BricodepotServicioClientesImpl extends ServicioClientesImpl{
	
	protected static Logger log = Logger.getLogger(BricodepotServicioClientesImpl.class);
	
	protected static BricodepotServicioClientesImpl instance;
	
	public static BricodepotServicioClientesImpl get(){
		if(instance == null){
			instance = new BricodepotServicioClientesImpl();
		}
		return instance;
	}
	
	public static void setCustomInstance(BricodepotServicioClientesImpl instance){
		BricodepotServicioClientesImpl.instance = instance;
	}
	
	public List<BricodepotClienteBean> consultarClientes(BricodepotParametrosBuscarClientesBean param, DatosSesionBean datosSesion) throws BricodepotClienteException {
		Connection conn = new Connection();
		
		try {
			log.debug("consultarClientes() - Consultando clientes");
			conn.abrirConexion(Database.getConnection());
			return BricodepotClientesDao.consultarClientes(conn, datosSesion.getConfigEmpresa(), param);
		}
		catch (SQLException e) {
			log.error("consultarClientes() - " + e.getMessage());
			String mensaje = "Error al consultar clientes: " +  e.getMessage();
			
            throw new BricodepotClienteException(mensaje, e);
		}
		finally {
			conn.cerrarConexion();
		}
	}
	
	public int salvar(BricodepotClienteBean cliente, DatosSesionBean datosSesion) throws BricodepotClienteException, BricodepotClienteConstraintViolationException {
		int cambios = 0;
		
		switch (cliente.getEstadoBean()) {
			case Estado.NUEVO:
				cambios = crear(cliente, datosSesion);
				break;
			      
			case Estado.MODIFICADO:
				cambios =  modificar(cliente, datosSesion);
				break;
			      
			//BRICO-253 no se permite elimiar desde pos/api
//			case Estado.BORRADO:
//				cambios = eliminar(cliente.getCodCliente());
//				break;
		}
		
		return cambios;
	}
	
	@Override
	public void crear(Connection conn, ClienteBean cliente, ConfigEmpresaBean config) throws ClienteException, ClienteConstraintViolationException {
		try {
			log.debug("crear() - Creando nuevo Cliente");
			
			//comprobarExistenciaClientePorCif(config, cliente, conn);
			
			//Indicamos que la fecha del alta será hoy
			cliente.setFechaAlta(new Date());
			                         
			//Si se da de alta inactivo ponemos fecha de baja
			if(!cliente.isActivo()){
				cliente.setFechaBaja(new Date());
			}
			                         
			ClientesDao.insert(conn, config, cliente);
			versionar(conn, obtenerDatosSesion(config), cliente.getCodCli());
			
		}
		catch(KeyConstraintViolationException e){
			log.error("crear() - No se ha podido crear el cliente: " + e.getMessage());
			throw new ClienteConstraintViolationException(e.getDefaultMessage(config.getTranslation()));
		}
		catch (Exception e) {
			String msg = "Error creando cliente: " + e.getMessage();
			log.error("crear() - " + msg, e);
			                     
			throw new ClienteException(msg, e);
		}
	}
	
	public int crear(BricodepotClienteBean cliente, DatosSesionBean datosSesion)
			throws BricodepotClienteException, BricodepotClienteConstraintViolationException {
		return crear(cliente, datosSesion.getConfigEmpresa());
	}

	public int crear(BricodepotClienteBean cliente, ConfigEmpresaBean config) 
			throws BricodepotClienteException, BricodepotClienteConstraintViolationException {
		Connection conn = new Connection();
		int resultado = 0;
		
		try {
			log.debug("crear() - Creando nuevo Cliente");
			conn.abrirConexion(Database.getConnection());
			conn.iniciaTransaccion();

			resultado = crear(conn, cliente, config);

			conn.commit();
			conn.finalizaTransaccion();
			return resultado;
		} catch (SQLException e) {
			conn.deshacerTransaccion();
			String msg = "Error creando cliente al obtener conexión: "
					+ e.getMessage();
			log.error("crear() - " + msg);
			throw new BricodepotClienteException(msg, e);
		} finally {
			conn.cerrarConexion();
		}
	}
	
	public int crear(Connection conn, BricodepotClienteBean cliente,
			DatosSesionBean datosSesion) throws BricodepotClienteException,
			BricodepotClienteConstraintViolationException {
		return crear(conn, cliente, datosSesion.getConfigEmpresa());
	}
	
	public int crear(Connection conn, BricodepotClienteBean cliente, ConfigEmpresaBean config) 
			throws BricodepotClienteException, BricodepotClienteConstraintViolationException {
		try {
			log.debug("crear() - Creando nuevo Cliente");
			int resultado = 0;
			
			//comprobarExistenciaClientePorCif(config, cliente, conn);
			
			//Indicamos que la fecha del alta será hoy
			cliente.setFechaAlta(new Date());
			                         
			//Si se da de alta inactivo ponemos fecha de baja
			if(!cliente.getActivo()){
				cliente.setFechaBaja(new Date());
			}
			                         
			resultado = BricodepotClientesDao.insert(conn, config, cliente);
			versionar(conn, obtenerDatosSesion(config), cliente.getCodCliente());
			
			return resultado;
		}
		catch(KeyConstraintViolationException e){
			log.error("crear() - No se ha podido crear el cliente: " + e.getMessage());
			throw new BricodepotClienteConstraintViolationException(e.getDefaultMessage(config.getTranslation()));
		}
		catch (Exception e) {
			String msg = "Error creando cliente: " + e.getMessage();
			log.error("crear() - " + msg, e);
			                     
			throw new BricodepotClienteException(msg, e);
		}
	}
	
	@Override
	public void modificar(ClienteBean cliente, ConfigEmpresaBean configEmpresa) throws ClienteException, ClienteConstraintViolationException {
		Connection conn = new Connection();
		try {
			log.debug("modificar() - Modificando Cliente " + cliente.getCodCli());
			conn.abrirConexion(Database.getConnection());
			conn.iniciaTransaccion();

			modificar(conn, cliente, configEmpresa);
			versionar(conn, obtenerDatosSesion(configEmpresa), cliente.getCodCli());

			conn.commit();
			conn.finalizaTransaccion();
		}
		catch (Exception e) {
			conn.deshacerTransaccion();
			String msg = "Error modificando cliente al obtener conexión: " + e.getMessage();
			log.error("modificar() - " + msg, e);
			throw new ClienteException(msg, e);
		}
		finally {
			conn.cerrarConexion();
		}
	}
	
	public int modificar(BricodepotClienteBean cliente, DatosSesionBean datosSesion) throws BricodepotClienteException, BricodepotClienteConstraintViolationException {
		return modificar(cliente, datosSesion.getConfigEmpresa());
	}
	
	public int modificar(BricodepotClienteBean cliente, ConfigEmpresaBean configEmpresa) throws BricodepotClienteException, BricodepotClienteConstraintViolationException {
		Connection conn = new Connection();
		int resultado = 0;
		try {
			log.debug("modificar() - Modificando Cliente " + cliente.getCodCliente());
			conn.abrirConexion(Database.getConnection());
			conn.iniciaTransaccion();
			
			resultado = modificar(conn, cliente, configEmpresa);
			
			conn.commit();
			conn.finalizaTransaccion();
			return resultado;
		}
		catch (SQLException e) {
			conn.deshacerTransaccion();
			String msg = "Error modificando cliente al obtener conexión: " + e.getMessage();
    		log.error("modificar() - " + msg);
    		throw new BricodepotClienteException(msg, e);
    	}
    	finally {
    		conn.cerrarConexion();
    	}
	}

	public int modificar(Connection conn, BricodepotClienteBean cliente, DatosSesionBean datosSesion) 
			throws BricodepotClienteException, BricodepotClienteConstraintViolationException {
		return modificar(conn, cliente, datosSesion.getConfigEmpresa());
	}
	
	public int modificar(Connection conn, BricodepotClienteBean cliente, ConfigEmpresaBean configEmpresa) 
			throws BricodepotClienteException, BricodepotClienteConstraintViolationException {
		try {
			int resultado = 0;
			log.debug("modificar() - Modificando Cliente " + cliente.getCodCliente());
			
			//comprobarExistenciaClientePorCif(configEmpresa, cliente, conn);
			
			//Si el cliente está inactivo y antes no tenía fecha de baja se pone la de hoy
			if(!cliente.getActivo() && cliente.getFechaBaja() == null){
				cliente.setFechaBaja(new Date());
			}
			//Si el cliente está activo y antes estaba de baja se pone la fecha de baja a nula
			else if(cliente.getActivo() && cliente.getFechaBaja() != null){
				cliente.setFechaBaja(null);
			}

			resultado = BricodepotClientesDao.update(conn, configEmpresa, cliente);
			versionar(conn, obtenerDatosSesion(configEmpresa), cliente.getCodCliente());
			return resultado;
		}
		catch(KeyConstraintViolationException e){
			log.error("modificar() - No se ha podido modificar el cliente: " + e.getMessage());
			throw new BricodepotClienteConstraintViolationException(e.getDefaultMessage(configEmpresa.getTranslation()));
		}
		catch (Exception e) {
			String msg = "Error modificando cliente: " + e.getMessage();
    		log.error("modificar() - " + msg, e);
    		
    		throw new BricodepotClienteException(msg, e);
    	}
	}

	@Override
	public PaginaResultados consultar(ParametrosBuscarClientesBean param, DatosSesionBean datosSesion) throws ClienteException {
		log.debug("consultar() - Se pone el CIF a mayuscula antes de realizar la consulta de clientes.");
		param.setCif(param.getCif().toUpperCase());
		return super.consultar(param, datosSesion);
	}
	
	public static ClienteBean consultarClienteList(String cif, String codTipoIden, String codPais, String desCli, DatosSesionBean datosSesion) throws ClienteNotFoundException {
		ClienteBean response = null;
		Connection conn = new Connection();
		log.debug("consultarCliente() - consultando cliente por cif, codPais y codTipoIden");
		try {
			conn.abrirConexion(Database.getConnection());
			List<ClienteBean> list = BricodepotClientesDao.consultarPorCifTipoIdenLista(conn, datosSesion.getConfigEmpresa(), codPais, cif, codTipoIden, desCli);
			log.debug("consultarCliente() - clientes encontrados : " + list.size());
			List<ClienteBean> listaClientesSeleccionados = new ArrayList<>();
			List<ClienteBean> listaClientesSeleccionadosInactivos = new ArrayList<>();
			
			if (list != null && !list.isEmpty()) {
				if (list.size() > 1) {
					for (ClienteBean clienteBean : list) {
						if (clienteBean.isActivo()) {
							listaClientesSeleccionados.add(clienteBean);
						}
						else {
							listaClientesSeleccionadosInactivos.add(clienteBean);
						}
					}
				}
				else {
					listaClientesSeleccionados.add(list.get(0));
				}

				if (listaClientesSeleccionados.isEmpty() && !listaClientesSeleccionadosInactivos.isEmpty()) {
					for (ClienteBean clienteBean : listaClientesSeleccionadosInactivos) {
						listaClientesSeleccionados.add(clienteBean);
					}
				}

				if (listaClientesSeleccionados == null || listaClientesSeleccionados.isEmpty()) {
					throw new ClienteNotFoundException();
				}

				if (listaClientesSeleccionados.size() > 1) {
					response = clienteDuplicado();
				}
				else {
					response = listaClientesSeleccionados.get(0);
				}
			}
		}
		catch (SQLException e) {
			log.error("consultarClienteList()- Error en consulta de clientes" + e.getMessage(), e);
		}

		finally {
			conn.cerrarConexion();
		}
		return response;
	}

	private static ClienteBean clienteDuplicado() {
		ClienteBean cliente = new ClienteBean();
			cliente.setActivo(true);
			cliente.setCodCli("DUPLICADO");
			cliente.setCodCli("DUPLICADO");
			cliente.setDesCli("DUPLICADO");
			cliente.setNombreComercial("DUPLICADO");
			cliente.setDomicilio("DUPLICADO");
			cliente.setPoblacion("DUPLICADO");
			cliente.setLocalidad("DUPLICADO");
			cliente.setProvincia("DUPLICADO");
			cliente.setCp("DUPLICADO");
			cliente.setTelefono1("DUPLICADO");
			cliente.setTelefono2("DUPLICADO");
			cliente.setFax("DUPLICADO");
			cliente.setCodPais("DUPLICADO");
			cliente.setDesPais("DUPLICADO");
			cliente.setPersonaContacto("DUPLICADO");
			cliente.setEmail("DUPLICADO");
			cliente.setCif("DUPLICADO");
			cliente.setCodTipoIden("DUPLICADO");
			cliente.setDesTipoIden("DUPLICADO");
			cliente.setIdTratImp(0L);
			cliente.setCodTratImp("DUPLICADO");
			cliente.setDesTratImp("DUPLICADO");
			cliente.setIdMedioPagoVencimiento(null);
			cliente.setDesMedioPagoVencimiento("DUPLICADO");
			cliente.setCodMedioPago("DUPLICADO");
			cliente.setCodTar("DUPLICADO");
			cliente.setDesTar("DUPLICADO");
			cliente.setCodSec("DUPLICADO");
			cliente.setDesSec("DUPLICADO");
			cliente.setBanco("DUPLICADO");
			cliente.setDomicilioBanco("DUPLICADO");
			cliente.setPoblacionBanco("DUPLICADO");
			cliente.setCcc("DUPLICADO");
			cliente.setObservaciones("DUPLICADO");
			cliente.setFechaAlta(null);
			cliente.setFechaBaja(null);
			cliente.setCodAlmacenTienda("DUPLICADO");
			cliente.setDesAlmacenTienda("DUPLICADO");
			cliente.setRiesgoConcedido(null);
			cliente.setCodcliFactura("DUPLICADO");
			cliente.setDeposito("DUPLICADO");
			cliente.setCodlengua("DUPLICADO");
			cliente.setDeslengua("DUPLICADO");
		return cliente;
	}

	public static List<BricodepotClienteBean> consultarClienteListPOS(String cif, String codTipoIden, String codPais, String desCli, DatosSesionBean datosSesion) throws ClienteNotFoundException {
		List<BricodepotClienteBean> response = null;
		Connection conn = new Connection();
		log.debug("consultarClientePOS() - consultando cliente por cif, codPais y codTipoIden");
		try {
			conn.abrirConexion(Database.getConnection());
			response = BricodepotClientesDao.consultarPorCifTipoIdenListaPOS(conn, datosSesion.getConfigEmpresa(), codPais, cif, codTipoIden, desCli);
			log.debug("consultarCliente() - clientes encontrados : " + response.size());
		}
		catch (SQLException e) {
			log.error("consultarClienteList()- Error en consulta de clientes" + e.getMessage(), e);
		}

		finally {
			conn.cerrarConexion();
		}
		return response;
	}
	
	public void versionar(Connection conn, IDatosSesion datosSesion, String codCli) throws VersionadoException, ContadorException {
		VersionadoBean versionCliente = new VersionadoBean();
		versionCliente.setIdClase("D_CLIENTES_TBL.CODCLI");
		versionCliente.setIdObjeto(codCli);
		versionCliente.setVersion(ServicioContadoresImpl.get().obtenerValorContador(datosSesion, "VERSION_CLIENTE"));
		ServicioVersionadoImpl.get().versionar(conn, datosSesion, versionCliente);
	}
	
	private DatosSesionBean obtenerDatosSesion(ConfigEmpresaBean config) {
		DatosSesionBean datosSesion = new DatosSesionBean();
		try {
			datosSesion.setUidActividad(config.getUidActividad());
			datosSesion.setUidInstancia(config.getUidInstancia());
		}
		catch (Exception e) {
			String msg = "Error obteniendo los datos de la sesion: " + e.getMessage();
			log.error("obtenerDatosSesion() - " + msg, e);
		}
		return datosSesion;
	}
	
}
