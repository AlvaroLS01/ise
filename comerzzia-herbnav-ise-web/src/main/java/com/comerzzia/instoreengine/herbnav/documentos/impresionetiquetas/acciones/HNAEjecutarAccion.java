package com.comerzzia.instoreengine.herbnav.documentos.impresionetiquetas.acciones;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.util.db.Connection;
import com.comerzzia.core.util.db.Database;
import com.comerzzia.instoreengine.util.AppISE;
import com.comerzzia.instoreengine.web.documentos.impresionetiquetas.acciones.EjecutarAccion;
import com.comerzzia.model.general.clientes.ClienteBean;
import com.comerzzia.model.ventas.tarifas.TarifaBean;
import com.comerzzia.model.versionado.VersionadoBean;
import com.comerzzia.model.versionado.VersionadoKey;
import com.comerzzia.persistencia.ventas.tarifas.TarifasDao;
import com.comerzzia.servicios.general.clientes.ClienteException;
import com.comerzzia.servicios.general.clientes.ClienteNotFoundException;
import com.comerzzia.servicios.general.clientes.ServicioClientesImpl;
import com.comerzzia.servicios.versionado.ServicioVersionadoImpl;
import com.comerzzia.web.base.WebKeys;


public class HNAEjecutarAccion extends EjecutarAccion {

	
        /**
         * HNA-165: imprimir solo por cambio de versión de tarifa
         */
        @Override
        protected void comprobarVersion(HttpServletRequest request) throws SQLException, ClienteException, ClienteNotFoundException {
		HttpSession sesion = request.getSession();
		DatosSesionBean datosSesion = (DatosSesionBean) sesion.getAttribute(WebKeys.DATOS_SESION);
		java.sql.Connection connSql = Database.getConnection();
		Connection conn = new Connection(connSql);
		try {
			Long versionTarifa = 0L;
			
			VersionadoKey keyTarifa = new VersionadoKey();
			keyTarifa.setUidActividad(datosSesion.getUidActividad());
			keyTarifa.setIdClase(VersionadoKey.CLASE_IMPRESION_ETIQUETAS);
			keyTarifa.setIdObjeto("VERSION_TARIFA");

			VersionadoBean versionTarifaActual = ServicioVersionadoImpl.get().selectByPrimaryKey(keyTarifa);
			if(versionTarifaActual != null) {
				versionTarifa = versionTarifaActual.getVersion();
			}
			
			String codcli = AppISE.getPosConfig().getTienda().getCodCliente();
			ClienteBean clienteTienda = ServicioClientesImpl.get().consultarEnTienda(codcli, datosSesion);
			String codTarifa = clienteTienda.getCodTar();
			if(codTarifa == null) {
				codTarifa = TarifaBean.TARIFA_GENERAL;
			}

			// Obtenemos los datos de la tarifa
	        TarifaBean tarifa = TarifasDao.consultarEnTienda(conn, datosSesion.getConfigEmpresa(), codTarifa);
	        Long versionMaxTarifa = tarifa.getVersion();
	        
	        if(versionTarifa.longValue() == versionMaxTarifa.longValue()) {
	        	request.setAttribute("noEtiquetasPendientes", true);
	        }
		}finally {
			conn.close();
		}
		
	}
	
}
