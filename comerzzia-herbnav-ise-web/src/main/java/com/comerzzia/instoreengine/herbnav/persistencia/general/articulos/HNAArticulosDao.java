package com.comerzzia.instoreengine.herbnav.persistencia.general.articulos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.comerzzia.core.model.empresas.ConfigEmpresaBean;
import com.comerzzia.core.util.db.Connection;
import com.comerzzia.core.util.db.PreparedStatement;
import com.comerzzia.model.general.articulos.ArticuloBean;
import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.DecimalFormatSymbols;


public class HNAArticulosDao{

	protected static Logger log = Logger.getLogger(HNAArticulosDao.class);
	
	public static ArrayList<ArticuloBean> consultarEnTienda(Connection conn, ConfigEmpresaBean config, String codTienda) throws SQLException {
		PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	ArrayList<ArticuloBean> listaArticulo = new ArrayList<ArticuloBean>();
    	String sql = null;
    	ArticuloBean articulo = null;
        
    	sql = 	"SELECT ART.CODART, ART.DESART, ART.FORMATO, ART.CODFAM, ART.CODSECCION, ART.CODCAT, ART.CODPRO, ART.REFERENCIA_PROVEEDOR, ART.DTO_PROVEEDOR, ART.CODFAB, "
    			+ "ART.COSTO_ACTUALIZADO, ART.CODIMP, ART.OBSERVACIONES, ART.ACTIVO, ART.NUMEROS_SERIE, ART.DESGLOSE1, ART.DESGLOSE2, ART.GENERICO, "
    			+ "ART.ESCAPARATE, ART.UNIDAD_MEDIDA_ALTERNATIVA,  ART.COD_UM_ETIQUETA, ART.CANTIDAD_UM_ETIQUETA, ART.VERSION, ART.CODMARCA, "
    			+ "ART.BALANZA_SECCION, ART.BALANZA_PLU, ART.BALANZA_TIPO_ART, ART.ID_TIPO_SUSTITUCION,  ART.CODGRUPODES_DESGLOSE1, ART.CODGRUPODES_DESGLOSE2, ART.CONFIRMAR_PRECIO_VENTA, "
    			+ "CAT.DESCAT, UM.DES_UM_ETIQUETA, UM.DESETIQUETA "
    			+ "FROM D_ARTICULOS_TBL ART "
    			+ "LEFT JOIN  D_CATEGORIZACION_TBL CAT ON (CAT.UID_ACTIVIDAD = ART.UID_ACTIVIDAD AND CAT.CODCAT = ART.CODCAT) "
    			+ "LEFT JOIN D_UNIDAD_MEDIDA_ETIQUETAS_TBL UM ON (UM.UID_ACTIVIDAD = ART.UID_ACTIVIDAD AND UM.COD_UM_ETIQUETA = ART.COD_UM_ETIQUETA) "
    			+ "inner join d_articulos_codbar_tbl codbar on (codbar.UID_ACTIVIDAD = ART.UID_ACTIVIDAD and codbar.CODART = ART.CODART) "
    			+ "inner join d_etiquetas_enl_tbl etiquetas on (etiquetas.UID_ACTIVIDAD = ART.UID_ACTIVIDAD and etiquetas.ID_OBJETO = ART.CODART) "
    			+ "WHERE ART.UID_ACTIVIDAD = ? and codbar.principal = 'S' and etiquetas.UID_ETIQUETA  = '" + codTienda;
    	
    	try {
    		pstmt = new PreparedStatement(conn, sql);
    		pstmt.setString(1, config.getUidActividad());
    		
        	log.debug("consultar() - " + pstmt);
            rs = pstmt.executeQuery();
        	
        	while (rs.next()){
        		articulo = new ArticuloBean();
        		articulo.setCodArticulo(rs.getString("CODART"));
        		articulo.setDesArticulo(rs.getString("DESART"));
        		articulo.setFormato(rs.getString("FORMATO"));
        		articulo.setCodFamilia(rs.getString("CODFAM"));
        		articulo.setCodSeccion(rs.getString("CODSECCION"));
        		articulo.setCodCategorizacion(rs.getString("CODCAT"));
        		articulo.setCodProveedor(rs.getString("CODPRO"));        	
        		articulo.setReferencia(rs.getString("REFERENCIA_PROVEEDOR"));
        		articulo.setDtoProveedor(rs.getDouble("DTO_PROVEEDOR"));
				articulo.setCodFabricante(rs.getString("CODFAB"));
				articulo.setFechaPrecioCosto(rs.getTimestamp("COSTO_ACTUALIZADO"));
        		articulo.setCodImpuesto(rs.getString("CODIMP"));
        		articulo.setObservaciones(rs.getString("OBSERVACIONES"));
        		articulo.setActivo(rs.getString("ACTIVO"));
        		articulo.setNumSeries(rs.getString("NUMEROS_SERIE"));
        		articulo.setDesglose1(rs.getString("DESGLOSE1"));
        		articulo.setDesglose2(rs.getString("DESGLOSE2"));
        		articulo.setGenerico(rs.getString("GENERICO"));
        		articulo.setEscaparate(rs.getString("ESCAPARATE"));
        		articulo.setUnidadMedAlt(rs.getString("UNIDAD_MEDIDA_ALTERNATIVA"));
        		articulo.setCodUnidadMedidaEtiq(rs.getString("COD_UM_ETIQUETA"));
        		articulo.setCantidadUnidadMedidaEtiq(rs.getDouble("CANTIDAD_UM_ETIQUETA"));
        		articulo.setVersion(rs.getLong("VERSION"));
        		articulo.setCodMarca(rs.getString("CODMARCA"));
        		articulo.setBalanzaSeccion(rs.getString("BALANZA_SECCION"));
        		articulo.setBalanzaPlu(rs.getInt("BALANZA_PLU"));
        		if(rs.wasNull()){
        			articulo.setBalanzaPlu(null);
        		}
        		articulo.setBalanzaTipoArticulo(rs.getString("BALANZA_TIPO_ART"));
        		articulo.setIdTipoSustitucion(rs.getLong("ID_TIPO_SUSTITUCION"));
        		articulo.setCodgrupodesDesglose1(rs.getString("CODGRUPODES_DESGLOSE1"));
        		articulo.setCodgrupodesDesglose2(rs.getString("CODGRUPODES_DESGLOSE2"));
        		articulo.setConfirmarPrecioVenta(rs.getString("CONFIRMAR_PRECIO_VENTA"));
        		articulo.setDesCategorizacion(rs.getString("DESCAT"));
        		articulo.setDesUnidadMedidaEtiq(rs.getString("DES_UM_ETIQUETA"));
        		articulo.setDesetiqueta(rs.getString("DESETIQUETA"));
        		listaArticulo.add(articulo);
        	}
        	
    		return listaArticulo;
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
