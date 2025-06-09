package com.comerzzia.instoreengine.herbnav.persistencia.codigoBarras;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.comerzzia.core.model.empresas.ConfigEmpresaBean;
import com.comerzzia.core.util.db.Connection;
import com.comerzzia.core.util.db.PreparedStatement;
import com.comerzzia.model.general.articulos.codigosbarras.CodigoBarrasArticuloBean;
import com.comerzzia.persistencia.general.articulos.ParametrosBuscarArticulosBean;
import com.comerzzia.persistencia.general.articulos.codigosbarras.CodigosBarrasArticulosDao;
import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.DecimalFormatSymbols;

public class HNACodBarDao {
	
	protected static Logger log = Logger.getLogger(CodigosBarrasArticulosDao.class);
	
	private static String TABLA_CODBAR = "D_ARTICULOS_CODBAR_TBL";
	private static String TABLA_ARTICULOS = "D_ARTICULOS_TBL";
	private static String TABLA_ETIQUETAS = "D_ETIQUETAS_ENL_TBL";
	
	public static List<CodigoBarrasArticuloBean> consultar(Connection conn, ConfigEmpresaBean config, ParametrosBuscarArticulosBean param, String codTienda, Long versionMaxArticulos) throws SQLException {
		PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	List<CodigoBarrasArticuloBean> listaCodigos = new ArrayList<CodigoBarrasArticuloBean>();
    	String sql = null;
    	
    	DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.'); // Establecemos el separador de miles como un punto
        
        // Creamos el objeto DecimalFormat con el nuevo símbolo
        DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);
        
        String numeroFormateado = decimalFormat.format(versionMaxArticulos);

        System.out.println(numeroFormateado);

    	sql = "SELECT codbar.CODART, codbar.DESGLOSE1, codbar.DESGLOSE2, codbar.CODIGO_BARRAS, codbar.DUN14, codbar.FACTOR_CONVERSION, codbar.PRINCIPAL " +
    	      "FROM " + TABLA_CODBAR + " AS codbar " +
    	      "INNER JOIN " + TABLA_ARTICULOS + " AS articulos ON codbar.UID_ACTIVIDAD = articulos.UID_ACTIVIDAD and codbar.CODART = articulos.CODART " +
    	      "INNER JOIN " + TABLA_ETIQUETAS + " AS etiquetas ON  etiquetas.UID_ACTIVIDAD = articulos.UID_ACTIVIDAD and etiquetas.ID_OBJETO = articulos.CODART" +
		      " WHERE codbar.UID_ACTIVIDAD = ? AND codbar.PRINCIPAL = 'S' AND etiquetas.UID_ETIQUETA = '" + codTienda + "' AND articulos.VERSION >= " + numeroFormateado;
    	 
    	String where = getClausulaWhereClausulaIn(param);
    	sql += where;
    	
    	try {
    		pstmt = new PreparedStatement(conn, sql);
    		pstmt.setString(1, config.getUidActividad());
    		
        	log.debug("consultar() - " + pstmt);
            rs = pstmt.executeQuery();
        	
        	while (rs.next()){
        		CodigoBarrasArticuloBean codigo = new CodigoBarrasArticuloBean();
        		codigo.setDesglose1(rs.getString("DESGLOSE1"));
        		codigo.setDesglose2(rs.getString("DESGLOSE2"));
        		codigo.setCodigoBarras(rs.getString("CODIGO_BARRAS"));
        		codigo.setDun14(rs.getString("DUN14"));
        		codigo.setFactorConversion(rs.getDouble("FACTOR_CONVERSION"));
        		codigo.setCodArticulo(rs.getString("CODART"));
        		codigo.setPrincipal(rs.getString("PRINCIPAL"));
        		listaCodigos.add(codigo);
        	}
        	
    		return listaCodigos;
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
	
	protected static String getClausulaWhereClausulaIn(ParametrosBuscarArticulosBean param) {
		String where = "";
		if(StringUtils.isNotBlank(param.getCodArticulo())) {
			where += "AND CODART ='" + StringEscapeUtils.escapeSql(param.getCodArticulo()) +"' ";
		}
		// DESARTICULO
		if (!param.getDesArticulo().isEmpty()) {
			where += "AND UPPER(DESART) LIKE UPPER('" + StringEscapeUtils.escapeSql(param.getDesArticulo()) + "%') ";
		}
		
		// CODFAM
		if (!param.getCodFamilia().isEmpty()) {
			where += "AND CODFAM = '" + StringEscapeUtils.escapeSql(param.getCodFamilia()) + "' ";
		}
		
		// CODPRO
		if (!param.getCodProveedor().isEmpty()) {
			where += "AND CODPRO = '" + StringEscapeUtils.escapeSql(param.getCodProveedor()) + "' ";
		}
		
		// ACTIVO
		if (!param.getActivo().isEmpty()) {
			where += "AND ACTIVO = '" + StringEscapeUtils.escapeSql(param.getActivo()) + "' ";
		}
		
		//CODCAT
		if(!param.getCodCategorizacion().isEmpty()){
			where += "AND CODCAT = '" + StringEscapeUtils.escapeSql(param.getCodCategorizacion()) + "' ";
		}
		
		//CODSECCION
		if(!param.getCodSeccion().isEmpty()){
			where += "AND CODSECCION = '" + StringEscapeUtils.escapeSql(param.getCodSeccion()) + "' ";
		}
		//GENERICO
		if(!param.getGenerico().isEmpty()){
			where += "AND GENERICO = '" + StringEscapeUtils.escapeSql(param.getGenerico()) + "' ";
		}

		return where;
	}
}
