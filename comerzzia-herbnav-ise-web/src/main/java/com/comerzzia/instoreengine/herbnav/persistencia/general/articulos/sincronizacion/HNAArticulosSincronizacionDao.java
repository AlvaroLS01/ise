package com.comerzzia.instoreengine.herbnav.persistencia.general.articulos.sincronizacion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.comerzzia.core.model.empresas.ConfigEmpresaBean;
import com.comerzzia.core.util.db.Connection;
import com.comerzzia.core.util.db.PreparedStatement;
import com.comerzzia.model.general.articulos.codigosbarras.CodigoBarrasArticuloBean;

public class HNAArticulosSincronizacionDao {
	
	protected static Logger log = Logger.getLogger(HNAArticulosSincronizacionDao.class);
	
	private static String TABLA_CODBAR = "D_ARTICULOS_CODBAR_TBL";
	private static String TABLA_ARTICULOS = "D_ARTICULOS_TBL";
	private static String TABLA_ETIQUETAS = "D_ETIQUETAS_ENL_TBL";
	private static String TABLA_TARIFAS = "D_TARIFAS_DET_TBL";
	
	public static List<CodigoBarrasArticuloBean> consultarCodigosBarrasSincronizacion(Connection conn,
			ConfigEmpresaBean config, String codalm, long versionTienda, long versionActual, boolean controlSurtido, String codTienda) throws SQLException {
		PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	String sql = null;
    	
    	List<CodigoBarrasArticuloBean> codigosBarras = new ArrayList<CodigoBarrasArticuloBean>();
    	
    	sql = "SELECT DISTINCT COD.CODART, COD.DESGLOSE1, COD.DESGLOSE2, COD.CODIGO_BARRAS, COD.DUN14, COD.FACTOR_CONVERSION, COD.PRINCIPAL " +
              "FROM " +TABLA_ARTICULOS + " ART " +
              "INNER JOIN " + TABLA_CODBAR + " COD ON (COD.UID_ACTIVIDAD = ART.UID_ACTIVIDAD AND COD.CODART = ART.CODART) "
              + "INNER JOIN " + TABLA_ETIQUETAS + " AS etiquetas ON  etiquetas.UID_ACTIVIDAD = ART.UID_ACTIVIDAD and etiquetas.ID_OBJETO = ART.CODART ";
              if(controlSurtido) {
            	  sql += "INNER JOIN D_ALMACENES_ARTICULOS_TBL ALMART ON(ALMART.UID_ACTIVIDAD = ART.UID_ACTIVIDAD AND ALMART.CODART = ART.CODART AND ALMART.CODALM = ? AND ALMART.ACTIVO = 'S') ";
              }
              sql += "WHERE COD.UID_ACTIVIDAD = ? " +
              	"AND ART.VERSION > ? " +
                "AND ART.VERSION <= ?"
                + " AND etiquetas.UID_ETIQUETA = ?";


    	try {
			pstmt = new PreparedStatement(conn, sql);
			if(controlSurtido) {
				pstmt.setString(1, codalm);
				pstmt.setString(2, config.getUidActividad());
				pstmt.setLong(3, versionTienda);
				pstmt.setLong(4, versionActual);
				pstmt.setString(5,codTienda);
			}else {
				pstmt.setString(1, config.getUidActividad());
				pstmt.setLong(2, versionTienda);
				pstmt.setLong(3, versionActual);
				pstmt.setString(4,codTienda);
			}
			
			
			log.debug("consultarCodigosBarrasSincronizacion() - " + pstmt);
			
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
        		codigosBarras.add(codigo);
			}
			
			return codigosBarras;
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

	public static List<CodigoBarrasArticuloBean> findBarcodesTagSync(Connection conn,
			ConfigEmpresaBean config, long versionTienda, long versionActual, String codTarifa, String codTienda) throws SQLException {
		PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	String sql = null;
    	
    	List<CodigoBarrasArticuloBean> codigosBarras = new ArrayList<CodigoBarrasArticuloBean>();
    	
    	sql = "SELECT COD.CODART, COD.DESGLOSE1, COD.DESGLOSE2, COD.CODIGO_BARRAS, COD.DUN14, COD.FACTOR_CONVERSION, COD.PRINCIPAL " +
              "FROM " + TABLA_CODBAR + " COD " +
              "INNER JOIN " + TABLA_TARIFAS + " TAR " +
              "ON (TAR.UID_ACTIVIDAD = COD.UID_ACTIVIDAD AND TAR.CODART = COD.CODART AND TAR.DESGLOSE1 = COD.DESGLOSE1 AND TAR.DESGLOSE2 = COD.DESGLOSE2 ) " +
              "INNER JOIN " + TABLA_ETIQUETAS + " AS etiquetas ON  etiquetas.UID_ACTIVIDAD = COD.UID_ACTIVIDAD and etiquetas.ID_OBJETO = COD.CODART " +
              "WHERE COD.UID_ACTIVIDAD = ? " +
              	"AND TAR.VERSION > ? " +
                "AND TAR.VERSION <= ? " +
              	"AND TAR.CODTAR = ?"
              	+ " AND etiquetas.UID_ETIQUETA = ?";
    	
    	try {
			pstmt = new PreparedStatement(conn, sql);
			pstmt.setString(1, config.getUidActividad());
			pstmt.setLong(2, versionTienda);
			pstmt.setLong(3, versionActual);
			pstmt.setString(4, codTarifa);
			pstmt.setString(5, codTienda);
			
			log.debug("consultarCodigosBarrasTarifasSincronizacion() - " + pstmt);
			
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
        		codigosBarras.add(codigo);
			}
			
			return codigosBarras;
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
}
