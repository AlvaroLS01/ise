package com.comerzzia.bricodepot.backoffice.persistence.general.clientes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.comerzzia.bricodepot.backoffice.services.general.clientes.BricodepotParametrosBuscarClientesBean;
import com.comerzzia.core.model.empresas.ConfigEmpresaBean;
import com.comerzzia.core.util.base.MantenimientoDao;
import com.comerzzia.core.util.db.Connection;
import com.comerzzia.core.util.db.PreparedStatement;
import com.comerzzia.core.util.fechas.Fechas;
import com.comerzzia.model.general.clientes.ClienteBean;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/* BRICO-253 copiado de com.comerzzia.persistencia.general.clientes.ClientesDao
 * - Se añade consultarClientes porque no lo tenía el estándar
 * - Se cambian insert y update para que devuelva int
 */
public class BricodepotClientesDao extends MantenimientoDao{

	private static String TABLA = "D_CLIENTES_TBL";
	private static String VISTA = "D_CLIENTES";
	
	protected static Logger log = Logger.getLogger(BricodepotClientesDao.class);
	
	public static List<BricodepotClienteBean> consultarClientes(Connection conn, ConfigEmpresaBean configEmpresa, BricodepotParametrosBuscarClientesBean param) throws SQLException {
		log.debug("consultarClientes() - Inicio de la consulta de clientes");
    	ResultSet rs = null;
    	PreparedStatement pstmt = null;
    	
    	// Inicializamos la página de resultados
    	List<BricodepotClienteBean> resultados = new ArrayList<BricodepotClienteBean>();
    	
    	// Consultas
    	String sql = obtenerConsulta(configEmpresa, param);
    	
    	try {
            log.debug("consultarClientes() - " + sql);
            
            pstmt = new PreparedStatement(conn, sql);
            rs = pstmt.executeQuery();
            
            while(rs.next()) {
            	BricodepotClienteBean cliente = new BricodepotClienteBean();
        		cliente.setCodCliente(rs.getString("CODCLI"));
        		cliente.setDesCliente(rs.getString("DESCLI"));
        		cliente.setNombreComercial(rs.getString("NOMBRE_COMERCIAL"));
        		cliente.setDomicilio(rs.getString("DOMICILIO"));
        		cliente.setPoblacion(rs.getString("POBLACION"));
        		cliente.setLocalidad(rs.getString("LOCALIDAD"));
        		cliente.setProvincia(rs.getString("PROVINCIA"));
        		cliente.setCp(rs.getString("CP"));
        		cliente.setTelefono1(rs.getString("TELEFONO1"));
        		cliente.setTelefono2(rs.getString("TELEFONO2"));
        		cliente.setFax(rs.getString("FAX"));
        		cliente.setCodpais(rs.getString("CODPAIS"));
        		cliente.setPais(rs.getString("DESPAIS"));
//        		cliente.setPersonaContacto(rs.getString("PERSONA_CONTACTO"));
        		cliente.setEmail(rs.getString("EMAIL"));
        		cliente.setCif(rs.getString("CIF"));
        		cliente.setTipoIdentificacion(rs.getString("CODTIPOIDEN"));//TODO ojo, puede ser des
//        		cliente.setCodTipoIden(rs.getString("DESTIPOIDEN"));
        		cliente.setIdTratImpuestos(rs.getLong("ID_TRAT_IMPUESTOS"));
//        		cliente.setCodTratImp(rs.getString("CODTRATIMP"));
//        		cliente.setDesTratImp(rs.getString("DESTRATIMP"));
//        		cliente.setIdMedioPagoVencimiento((rs.getString("ID_MEDPAG_VEN") != null) ? rs.getLong("ID_MEDPAG_VEN") : null);
//        		cliente.setDesMedioPagoVencimiento(rs.getString("DESMEDPAG_VEN"));
        		cliente.setCodtar(rs.getString("CODTAR"));
//        		cliente.setDesTar(rs.getString("DESTAR"));
//        		cliente.setCodSec(rs.getString("CODSEC"));
//        		cliente.setDesSec(rs.getString("DESSEC"));
        		cliente.setBanco(rs.getString("BANCO"));
        		cliente.setBancoDomicilio(rs.getString("BANCO_DOMICILIO"));
        		cliente.setBancoPoblacion(rs.getString("BANCO_POBLACION"));
        		cliente.setCcc(rs.getString("CCC"));
//        		cliente.setObservaciones(rs.getString("OBSERVACIONES"));
        		cliente.setActivo(rs.getString("ACTIVO")==null ? null : "S".equals(rs.getString("ACTIVO")));
//        		cliente.setFechaAlta(rs.getDate("FECHA_ALTA"));
//        		cliente.setFechaBaja(rs.getDate("FECHA_BAJA"));
//        		cliente.setDeposito(rs.getString("DEPOSITO"));
//        		cliente.setCodlengua(rs.getString("CODLENGUA"));
//        		cliente.setDeslengua(rs.getString("DESLENGUA"));
        		
        		resultados.add(cliente);
            }
            
            
            return resultados;
    	}
    	finally {
    		try {
    			rs.close();
    		}
    		catch(Exception ignore) {;}
    		try {
    			pstmt.close();
    		}
    		catch(Exception ignore) {;}
    	}
	}
	
	public static String obtenerConsulta(ConfigEmpresaBean config, BricodepotParametrosBuscarClientesBean param) {
		String sql = null;
		
		sql = "SELECT CODCLI, DESCLI, NOMBRE_COMERCIAL, DOMICILIO, POBLACION, LOCALIDAD, PROVINCIA, CP, TELEFONO1, TELEFONO2, FAX, CODPAIS, DESPAIS, PERSONA_CONTACTO, "
				+ "EMAIL, CIF, CODTIPOIDEN, DESTIPOIDEN, ID_TRAT_IMPUESTOS, CODTRATIMP, DESTRATIMP, ID_MEDPAG_VEN, DESMEDPAG_VEN, CODTAR, DESTAR, CODSEC, DESSEC, BANCO, "
				+ "BANCO_DOMICILIO, BANCO_POBLACION, CCC, OBSERVACIONES, ACTIVO, FECHA_ALTA, FECHA_BAJA, CODCLI_FACTURA, DEPOSITO, CODLENGUA, DESLENGUA " +
		       "FROM " + getNombreElemento(VISTA) +
		       " WHERE UID_ACTIVIDAD = '" + config.getUidActividad() + "' ";
		
		// Clausula WHERE
        String where = getClausulaWhere(param);
        if (where.length() > 0) {
            sql += where;
        }
        
		return sql;
	}
	
	protected static String getClausulaWhere(BricodepotParametrosBuscarClientesBean param) {
		String where = "";
		
		// DESCLI
		if (StringUtils.isNotEmpty(param.getDesCliente())) {
			where += " AND UPPER(DESCLI) LIKE UPPER('" + param.getDesCliente() + "%') ";
		}
		
		// NOMBRE_COMERCIAL
		if (StringUtils.isNotEmpty(param.getCodTipoIdent())) {
			where += " AND CODTIPOIDEN = '" + param.getCodTipoIdent() + "' ";
		}
		
		// CIF
		if (StringUtils.isNotEmpty(param.getIdent())) {
			where += " AND CIF = UPPER('" + param.getIdent() + "')";
		}
		
		return where;
	}
	
	public static int insert(Connection conn, ConfigEmpresaBean config, BricodepotClienteBean cliente) throws SQLException {
		return insert(conn, config.getUidActividad(), cliente);
	}
	
	public static int insert(Connection conn, String uidActividad, BricodepotClienteBean cliente) throws SQLException {
		PreparedStatement pstmt = null;
		String sql = null;
		
		sql = "INSERT INTO " + getNombreElemento(TABLA) + 
		      "(UID_ACTIVIDAD, CODCLI, DESCLI, NOMBRE_COMERCIAL, DOMICILIO, POBLACION, LOCALIDAD, PROVINCIA, CP, " + 
			   "TELEFONO1, TELEFONO2, FAX, CODPAIS, PERSONA_CONTACTO, EMAIL, CIF, CODTIPOIDEN, " + 
			   "ID_TRAT_IMPUESTOS, ID_MEDPAG_VEN, CODTAR, CODSEC, BANCO, BANCO_DOMICILIO, " + 
			   "BANCO_POBLACION, CCC, OBSERVACIONES, ACTIVO, FECHA_ALTA, FECHA_BAJA, RIESGO_CONCEDIDO, CODCLI_FACTURA, DEPOSITO, CODLENGUA) " +
		      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
		              "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		try {
			pstmt = new PreparedStatement(conn, sql);
			pstmt.setString(1, uidActividad);
			pstmt.setString(2, cliente.getCodCliente());
        	pstmt.setString(3, cliente.getDesCliente());
        	pstmt.setString(4, cliente.getNombreComercial());
			pstmt.setString(5, cliente.getDomicilio());
        	pstmt.setString(6, cliente.getPoblacion());
        	pstmt.setString(7, cliente.getLocalidad());
        	pstmt.setString(8, cliente.getProvincia());
			pstmt.setString(9, cliente.getCp());
        	pstmt.setString(10, cliente.getTelefono1());
        	pstmt.setString(11, cliente.getTelefono2());
			pstmt.setString(12, cliente.getFax());
        	pstmt.setString(13, cliente.getCodpais());
        	pstmt.setString(14, cliente.getPersonaContacto());
			pstmt.setString(15, cliente.getEmail());
        	pstmt.setString(16, cliente.getCif());
        	pstmt.setString(17, cliente.getTipoIdentificacion());
        	pstmt.setLong(18, cliente.getIdTratImpuestos());
        	pstmt.setLong(19, cliente.getIdMedioPagoVencimiento());
        	pstmt.setString(20, cliente.getCodtar());
			pstmt.setString(21, cliente.getCodsec());
        	pstmt.setString(22, cliente.getBanco());
        	pstmt.setString(23, cliente.getBancoDomicilio());
			pstmt.setString(24, cliente.getBancoPoblacion());
        	pstmt.setString(25, cliente.getCcc());
        	pstmt.setString(26, cliente.getObservaciones());
			pstmt.setString(27, cliente.getActivo()==null ? null : cliente.getActivo() ? "S":"N");
			pstmt.setDate(28, Fechas.toSqlDate(cliente.getFechaAlta()));
			pstmt.setDate(29, Fechas.toSqlDate(cliente.getFechaBaja()));
			pstmt.setDouble(30, cliente.getRiesgoConcedido());
			pstmt.setString(31,	cliente.getCodcliFactura());
			pstmt.setString(32,	cliente.getDeposito());
			pstmt.setString(33, cliente.getCodlengua());
        	
        	log.debug("insert() - " + pstmt);
        	int resultado = pstmt.executeUpdate();
        	return resultado;
		}
		catch (SQLException e) {
			throw getDaoException(e);
		}
		finally {
    		try {
    			pstmt.close();
    		}
    		catch(Exception ignore) {;}
    	}
	}
	
	public static int update(Connection conn, ConfigEmpresaBean config, BricodepotClienteBean cliente) throws SQLException {
		return update(conn, config.getUidActividad(), cliente);
	}
	
	
	public static int update(Connection conn, String uidActividad, BricodepotClienteBean cliente) throws SQLException {
		PreparedStatement pstmt = null;
		String sql = null;
		
		sql = "UPDATE " + getNombreElemento(TABLA) +
		         "SET DESCLI = ?, NOMBRE_COMERCIAL = ?, DOMICILIO = ?, POBLACION = ?, LOCALIDAD = ?, PROVINCIA = ?," +
		      	     "CP = ?, TELEFONO1 = ?, TELEFONO2 = ?, FAX = ?, CODPAIS = ?," +
		      	     "PERSONA_CONTACTO = ?, EMAIL = ?, CIF = ?, CODTIPOIDEN = ?, ID_TRAT_IMPUESTOS = ?, ID_MEDPAG_VEN = ?," +
		      	     " CODTAR = ?, CODSEC = ?, BANCO = ?, BANCO_DOMICILIO = ?, " +
		      	     "BANCO_POBLACION = ?, CCC = ?, OBSERVACIONES = ?, ACTIVO = ?, FECHA_BAJA = ?, RIESGO_CONCEDIDO = ?, CODCLI_FACTURA = ?, DEPOSITO = ?, CODLENGUA = ? " +
		       "WHERE UID_ACTIVIDAD = ? " +
		       "AND CODCLI = ? ";
		
		try {
			pstmt = new PreparedStatement(conn, sql);
        	pstmt.setString(1, cliente.getDesCliente());
        	pstmt.setString(2, cliente.getNombreComercial());
			pstmt.setString(3, cliente.getDomicilio());
        	pstmt.setString(4, cliente.getPoblacion());
        	pstmt.setString(5, cliente.getLocalidad());
        	pstmt.setString(6, cliente.getProvincia());
			pstmt.setString(7, cliente.getCp());
        	pstmt.setString(8, cliente.getTelefono1());
        	pstmt.setString(9, cliente.getTelefono2());
			pstmt.setString(10, cliente.getFax());
        	pstmt.setString(11, cliente.getCodpais());
        	pstmt.setString(12, cliente.getPersonaContacto());
			pstmt.setString(13, cliente.getEmail());
        	pstmt.setString(14, cliente.getCif());
        	pstmt.setString(15, cliente.getTipoIdentificacion());
        	pstmt.setLong(16, cliente.getIdTratImpuestos());
        	pstmt.setLong(17, cliente.getIdMedioPagoVencimiento());
        	pstmt.setString(18, cliente.getCodtar());
			pstmt.setString(19, cliente.getCodsec());
        	pstmt.setString(20, cliente.getBanco());
        	pstmt.setString(21, cliente.getBancoDomicilio());
			pstmt.setString(22, cliente.getBancoPoblacion());
        	pstmt.setString(23, cliente.getCcc());
        	pstmt.setString(24, cliente.getObservaciones());
			pstmt.setString(25, cliente.getActivo()==null ? null : cliente.getActivo() ? "S":"N");
			pstmt.setDate(26, Fechas.toSqlDate(cliente.getFechaBaja()));
			pstmt.setDouble(27, cliente.getRiesgoConcedido());
			pstmt.setString(28, cliente.getCodcliFactura());
			pstmt.setString(29, cliente.getDeposito());
			pstmt.setString(30, cliente.getCodlengua());
			pstmt.setString(31, uidActividad);
			pstmt.setString(32, cliente.getCodCliente());
        	
			log.debug("insert() - " + pstmt);
        	int resultado = pstmt.executeUpdate();
        	return resultado;
		}
		catch (SQLException e) {
			throw getDaoException(e);
		}
		finally {
    		try {
    			pstmt.close();
    		}
    		catch(Exception ignore) {;}
    	}
	}
	
	public static  List<ClienteBean> consultarPorCifTipoIdenLista(Connection conn, ConfigEmpresaBean config, String codPais, String cif, String codTipoIden, String desCli) throws SQLException {
		PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	ClienteBean cliente = null;
    	List<ClienteBean> lista = new ArrayList<>();
    	String sql = null;
    	
    	log.debug("consultarPorCifTipoIdenLista()");
    	
    	sql = "SELECT CLI.CODCLI, CLI.DESCLI, CLI.NOMBRE_COMERCIAL, CLI.DOMICILIO, CLI.POBLACION, CLI.LOCALIDAD, CLI.PROVINCIA, CLI.CP, CLI.TELEFONO1, CLI.TELEFONO2, CLI.FAX, CLI.CODPAIS, CLI.DESPAIS, CLI.PERSONA_CONTACTO, "
				+ "CLI.EMAIL, CLI.CIF, CLI.CODTIPOIDEN, CLI.DESTIPOIDEN, CLI.ID_TRAT_IMPUESTOS, CLI.CODTRATIMP, CLI.DESTRATIMP, CLI.ID_MEDPAG_VEN, CLI.DESMEDPAG_VEN, CLI.CODMEDPAG, CLI.CODTAR, CLI.DESTAR, CLI.CODSEC, "
				+ "CLI.DESSEC, CLI.BANCO, CLI.BANCO_DOMICILIO, CLI.BANCO_POBLACION, CLI.CCC, CLI.OBSERVACIONES, CLI.ACTIVO, CLI.FECHA_ALTA, CLI.FECHA_BAJA, CLI.CODALM_TIENDA, CLI.DESALM_TIENDA, CLI.RIESGO_CONCEDIDO, CLI.CODCLI_FACTURA, CLI.DEPOSITO, " 
				+ "CLI.CODLENGUA, CLI.DESLENGUA "+
    	        "FROM " + getNombreElemento(VISTA) + " CLI "+
    	        "INNER JOIN D_ACTIVIDADES_TBL ACT ON (ACT.UID_ACTIVIDAD = CLI.UID_ACTIVIDAD) "+  
		       "WHERE CLI.UID_ACTIVIDAD = ? " +
		       "AND UPPER(CLI.CIF) = ? ";
		
		if (StringUtils.isNotBlank(codPais)) {
			sql = sql + "AND CLI.CODPAIS = '"+codPais+"' ";
		}
		
		
		if (StringUtils.isNotBlank(desCli)) {
				sql = sql + "AND LOWER(CLI.DESCLI) LIKE LOWER('%"+desCli+"%') ";
		}
		    	
    	try {
    		pstmt = new PreparedStatement(conn, sql);
    		pstmt.setString(1, config.getUidActividad());
    		pstmt.setString(2, cif.toUpperCase());
    		if(!StringUtils.isBlank(codTipoIden)) {
    			pstmt.setString(3, codPais);
    			pstmt.setString(4, codTipoIden);
    		}
    		
        	log.debug("consultarPorCifTipoIdenLista() - " + pstmt);
            rs = pstmt.executeQuery();
        	
        	while (rs.next()){
        		cliente = new ClienteBean();
        		cliente.setCodCli(rs.getString("CODCLI"));
        		cliente.setDesCli(rs.getString("DESCLI"));
        		cliente.setNombreComercial(rs.getString("NOMBRE_COMERCIAL"));
        		cliente.setDomicilio(rs.getString("DOMICILIO"));
        		cliente.setPoblacion(rs.getString("POBLACION"));
        		cliente.setLocalidad(rs.getString("LOCALIDAD"));
        		cliente.setProvincia(rs.getString("PROVINCIA"));
        		cliente.setCp(rs.getString("CP"));
        		cliente.setTelefono1(rs.getString("TELEFONO1"));
        		cliente.setTelefono2(rs.getString("TELEFONO2"));
        		cliente.setFax(rs.getString("FAX"));
        		cliente.setCodPais(rs.getString("CODPAIS"));
        		cliente.setDesPais(rs.getString("DESPAIS"));
        		cliente.setPersonaContacto(rs.getString("PERSONA_CONTACTO"));
        		cliente.setEmail(rs.getString("EMAIL"));
        		cliente.setCif(rs.getString("CIF"));
        		cliente.setCodTipoIden(rs.getString("CODTIPOIDEN"));
        		cliente.setDesTipoIden(rs.getString("DESTIPOIDEN"));
        		cliente.setIdTratImp(rs.getLong("ID_TRAT_IMPUESTOS"));
        		cliente.setCodTratImp(rs.getString("CODTRATIMP"));
        		cliente.setDesTratImp(rs.getString("DESTRATIMP"));
        		cliente.setIdMedioPagoVencimiento((rs.getString("ID_MEDPAG_VEN") != null) ? rs.getLong("ID_MEDPAG_VEN") : null);
        		cliente.setDesMedioPagoVencimiento(rs.getString("DESMEDPAG_VEN"));
        		cliente.setCodMedioPago(rs.getString("CODMEDPAG"));
        		cliente.setCodTar(rs.getString("CODTAR"));
        		cliente.setDesTar(rs.getString("DESTAR"));
        		cliente.setCodSec(rs.getString("CODSEC"));
        		cliente.setDesSec(rs.getString("DESSEC"));
        		cliente.setBanco(rs.getString("BANCO"));
        		cliente.setDomicilioBanco(rs.getString("BANCO_DOMICILIO"));
        		cliente.setPoblacionBanco(rs.getString("BANCO_POBLACION"));
        		cliente.setCcc(rs.getString("CCC"));
        		cliente.setObservaciones(rs.getString("OBSERVACIONES"));
        		cliente.setActivo(rs.getString("ACTIVO"));
        		cliente.setFechaAlta(rs.getDate("FECHA_ALTA"));
        		cliente.setFechaBaja(rs.getDate("FECHA_BAJA"));
        		cliente.setCodAlmacenTienda(rs.getString("CODALM_TIENDA"));
        		cliente.setDesAlmacenTienda(rs.getString("DESALM_TIENDA"));
        		cliente.setRiesgoConcedido(rs.getDouble("RIESGO_CONCEDIDO"));
        		cliente.setCodcliFactura(rs.getString("CODCLI_FACTURA"));
        		cliente.setDeposito(rs.getString("DEPOSITO"));
        		cliente.setCodlengua(rs.getString("CODLENGUA"));
        		cliente.setDeslengua(rs.getString("DESLENGUA"));
        		lista.add(cliente);
        	}
        	
    		return lista;
    	}
    	finally {
    		try{
    			rs.close();
    		}
    		catch(Exception ignore) {;}
    		try{
    			pstmt.close();
    		}
    		catch(Exception ignore) {;}
    	}
	}
	
	
	public static  List<BricodepotClienteBean> consultarPorCifTipoIdenListaPOS(Connection conn, ConfigEmpresaBean config, String codPais, String cif, String codTipoIden, String desCli) throws SQLException {
		PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	BricodepotClienteBean cliente = null;
    	List<BricodepotClienteBean> lista = new ArrayList<>();
    	String sql = null;
    	
    	log.debug("consultarPorCifTipoIdenLista()");
    	
    	sql = "SELECT CLI.CODCLI, CLI.DESCLI, CLI.NOMBRE_COMERCIAL, CLI.DOMICILIO, CLI.POBLACION, CLI.LOCALIDAD, CLI.PROVINCIA, CLI.CP, CLI.TELEFONO1, CLI.TELEFONO2, CLI.FAX, CLI.CODPAIS, CLI.DESPAIS, CLI.PERSONA_CONTACTO, "
				+ "CLI.EMAIL, CLI.CIF, CLI.CODTIPOIDEN, CLI.DESTIPOIDEN, CLI.ID_TRAT_IMPUESTOS, CLI.CODTRATIMP, CLI.DESTRATIMP, CLI.ID_MEDPAG_VEN, CLI.DESMEDPAG_VEN, CLI.CODMEDPAG, CLI.CODTAR, CLI.DESTAR, CLI.CODSEC, "
				+ "CLI.DESSEC, CLI.BANCO, CLI.BANCO_DOMICILIO, CLI.BANCO_POBLACION, CLI.CCC, CLI.OBSERVACIONES, CLI.ACTIVO, CLI.FECHA_ALTA, CLI.FECHA_BAJA, CLI.CODALM_TIENDA, CLI.DESALM_TIENDA, CLI.RIESGO_CONCEDIDO, CLI.CODCLI_FACTURA, CLI.DEPOSITO, " 
				+ "CLI.CODLENGUA, CLI.DESLENGUA "+
    	        "FROM " + getNombreElemento(VISTA) + " CLI "+
    	        "INNER JOIN D_ACTIVIDADES_TBL ACT ON (ACT.UID_ACTIVIDAD = CLI.UID_ACTIVIDAD) "+  
    	        "INNER JOIN D_PAISES_TIPOS_IDENT_TBL IDE ON (IDE.CODTIPOIDEN = CLI.CODTIPOIDEN AND IDE.UID_INSTANCIA = ACT.UID_INSTANCIA) "+
		       "WHERE CLI.UID_ACTIVIDAD = ? " +
		       "AND UPPER(CLI.CIF) = ? " +
		       "AND CLI.ACTIVO = 'S' ";
		if(StringUtils.isBlank(codTipoIden)) {
			sql = sql + "AND (CLI.CODTIPOIDEN = '' OR CLI.CODTIPOIDEN IS NULL)";
		}
		else {
			sql = sql + "AND IDE.CODPAIS = ? AND CLI.CODTIPOIDEN = ? ";
		} 
		
		if (StringUtils.isNotBlank(codPais)) {
			sql = sql + "AND CLI.CODPAIS = '"+codPais+"' ";
		}
		
		
		if (StringUtils.isNotBlank(desCli)) {
				sql = sql + "AND LOWER(CLI.DESCLI) LIKE LOWER('%"+desCli+"%') ";
		}
		    	
    	try {
    		pstmt = new PreparedStatement(conn, sql);
    		pstmt.setString(1, config.getUidActividad());
    		pstmt.setString(2, cif.toUpperCase());
    		if(!StringUtils.isBlank(codTipoIden)) {
    			pstmt.setString(3, codPais);
    			pstmt.setString(4, codTipoIden);
    		}
    		
        	log.debug("consultarPorCifTipoIdenLista() - " + pstmt);
            rs = pstmt.executeQuery();
        	
        	while (rs.next()){
        		cliente = new BricodepotClienteBean();
        		cliente.setCodCliente(rs.getString("CODCLI"));
        		cliente.setDesCliente(rs.getString("DESCLI"));
        		cliente.setNombreComercial(rs.getString("NOMBRE_COMERCIAL"));
        		cliente.setDomicilio(rs.getString("DOMICILIO"));
        		cliente.setPoblacion(rs.getString("POBLACION"));
        		cliente.setLocalidad(rs.getString("LOCALIDAD"));
        		cliente.setProvincia(rs.getString("PROVINCIA"));
        		cliente.setCp(rs.getString("CP"));
        		cliente.setTelefono1(rs.getString("TELEFONO1"));
        		cliente.setTelefono2(rs.getString("TELEFONO2"));
        		cliente.setFax(rs.getString("FAX"));
        		cliente.setCodpais(rs.getString("CODPAIS"));
        		cliente.setPais(rs.getString("DESPAIS"));
        		cliente.setPersonaContacto(rs.getString("PERSONA_CONTACTO"));
        		cliente.setEmail(rs.getString("EMAIL"));
        		cliente.setCif(rs.getString("CIF"));
        		cliente.setTipoIdentificacion(rs.getString("CODTIPOIDEN"));
        		cliente.setDesTipoIden(rs.getString("DESTIPOIDEN"));
        		cliente.setIdTratImpuestos(rs.getLong("ID_TRAT_IMPUESTOS"));
        		cliente.setCodTratImp(rs.getString("CODTRATIMP"));
        		cliente.setDesTratImp(rs.getString("DESTRATIMP"));
        		cliente.setIdMedioPagoVencimiento((rs.getString("ID_MEDPAG_VEN") != null) ? rs.getLong("ID_MEDPAG_VEN") : null);
        		cliente.setDesMedioPagoVencimiento(rs.getString("DESMEDPAG_VEN"));
        		cliente.setCodMedioPago(rs.getString("CODMEDPAG"));
        		cliente.setCodtar(rs.getString("CODTAR"));
        		cliente.setDesTar(rs.getString("DESTAR"));
        		cliente.setCodsec(rs.getString("CODSEC"));
        		cliente.setDesSec(rs.getString("DESSEC"));
        		cliente.setBanco(rs.getString("BANCO"));
        		cliente.setBancoDomicilio(rs.getString("BANCO_DOMICILIO"));
        		cliente.setBancoPoblacion(rs.getString("BANCO_POBLACION"));
        		cliente.setCcc(rs.getString("CCC"));
        		cliente.setObservaciones(rs.getString("OBSERVACIONES"));
        		cliente.setActivo(rs.getString("ACTIVO").equals("S") ? true : false);
        		cliente.setFechaAlta(rs.getDate("FECHA_ALTA"));
        		cliente.setFechaBaja(rs.getDate("FECHA_BAJA"));
        		cliente.setCodAlmacenTienda(rs.getString("CODALM_TIENDA"));
        		cliente.setDesAlmacenTienda(rs.getString("DESALM_TIENDA"));
        		cliente.setRiesgoConcedido(rs.getDouble("RIESGO_CONCEDIDO"));
        		cliente.setCodcliFactura(rs.getString("CODCLI_FACTURA"));
        		cliente.setDeposito(rs.getString("DEPOSITO"));
        		cliente.setCodlengua(rs.getString("CODLENGUA"));
        		cliente.setDeslengua(rs.getString("DESLENGUA"));
        		lista.add(cliente);
        	}
        	
    		return lista;
    	}
    	finally {
    		try{
    			rs.close();
    		}
    		catch(Exception ignore) {;}
    		try{
    			pstmt.close();
    		}
    		catch(Exception ignore) {;}
    	}
	}
}
