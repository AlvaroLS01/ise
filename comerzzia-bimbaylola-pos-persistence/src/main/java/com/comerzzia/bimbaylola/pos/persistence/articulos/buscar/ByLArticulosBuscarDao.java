package com.comerzzia.bimbaylola.pos.persistence.articulos.buscar;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.comerzzia.core.util.db.Connection;
import com.comerzzia.core.util.db.PreparedStatement;
import com.comerzzia.pos.persistence.articulos.buscar.ArticuloBuscarBean;
import com.comerzzia.pos.persistence.articulos.buscar.ArticulosBuscarDao;
import com.comerzzia.pos.persistence.articulos.buscar.ArticulosParamBuscar;

public class ByLArticulosBuscarDao extends ArticulosBuscarDao{
	
	private static final String TABLA_ARTICULOS = "D_ARTICULOS_TBL";
    private static final String TABLA_CODBAR = "D_ARTICULOS_CODBAR_TBL";
    private static final String TABLA_GRUPOS_DESGLOSES = "D_DESGLOSES_GRUPOS_DET_TBL";

    private static final Logger log = Logger.getLogger(ByLArticulosBuscarDao.class);
	
	/**
     * Búsqueda de artículos según parámetros de búsqueda (con desglose)
     * @param conn
     * @param param
     * @return
     * @throws SQLException
     */
    public static List<ArticuloBuscarBean> buscarArticulosDesgloseByL(Connection conn, ArticulosParamBuscar param) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;

        List<ArticuloBuscarBean> articulos = new ArrayList<ArticuloBuscarBean>();
        
        String condActivo = param.getActivo() != null ? "AND ACTIVO = ? " : ""; 

        sql = "SELECT DISTINCT A.CODART, A.DESART, C.CODIGO_BARRAS, C.DESGLOSE1, C.DESGLOSE2, G1.ORDEN AS ORDEN_DESGLOSE1, G2.ORDEN AS ORDEN_DESGLOSE2 FROM " + getNombreElemento(TABLA_ARTICULOS) + " A "
                + "LEFT JOIN " + getNombreElemento(TABLA_CODBAR) + " C "
                + "ON (A.UID_ACTIVIDAD = C.UID_ACTIVIDAD AND A.CODART = C.CODART) "
                + "LEFT JOIN " + getNombreElemento(TABLA_GRUPOS_DESGLOSES) + " G1 "
                + "ON (A.UID_ACTIVIDAD = G1.UID_ACTIVIDAD AND A.CODGRUPODES_DESGLOSE1 = G1.CODGRUPODES AND C.DESGLOSE1 = G1.DESGLOSE)"
                + "LEFT JOIN " + getNombreElemento(TABLA_GRUPOS_DESGLOSES) + " G2 "
                + "ON (A.UID_ACTIVIDAD = G2.UID_ACTIVIDAD AND A.CODGRUPODES_DESGLOSE2 = G2.CODGRUPODES AND C.DESGLOSE2 = G2.DESGLOSE)"
                + "WHERE A.UID_ACTIVIDAD = ? "
                + "AND UPPER(A.CODART) LIKE UPPER(?) "
                + "AND UPPER (A.DESART) LIKE UPPER(?) "
                + condActivo
                + "ORDER BY CODART, ORDEN_DESGLOSE1, ORDEN_DESGLOSE2";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, param.getUidActividad());
            pstmt.setString(2, param.getCodigo().trim() + "%");
            pstmt.setString(3, "%" + param.getDescripcion() + "%");
            if(param.getActivo() != null){
            	String activo = "";
            	if(param.getActivo()){
            		activo = "S";
            	}else{
            		activo = "N";
            	}
            	pstmt.setString(4, activo);
            }
            log.debug("buscarArticulosDesglose() - " + pstmt);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                ByLArticuloBuscarBean articuloBuscar = new ByLArticuloBuscarBean();
                articuloBuscar.setCodArticulo(rs.getString("CODART"));
                articuloBuscar.setDesArticulo(rs.getString("DESART"));
                articuloBuscar.setCodigoBarras(rs.getString("CODIGO_BARRAS"));
                articuloBuscar.setValorDesglose1(rs.getString("DESGLOSE1"));
                articuloBuscar.setValorDesglose2(rs.getString("DESGLOSE2"));
                articulos.add(articuloBuscar);
            }
            return articulos;
        }
        finally {
            try {
                rs.close();
            }
            catch (Exception ignore) {}
            try {
                pstmt.close();
            }
            catch (Exception ignore) {}
        }
    }

}
