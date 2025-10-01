package com.comerzzia.bricodepot.backoffice.services.sincronizador.datos;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.comerzzia.core.model.empresas.ConfigEmpresaBean;
import com.comerzzia.core.servicios.actividades.ActividadNotFoundException;
import com.comerzzia.core.servicios.impuestos.porcentajes.PorcentajeImpuestoException;
import com.comerzzia.core.servicios.sincronizacion.datos.DatosSincronizadorException;
import com.comerzzia.core.util.db.Connection;
import com.comerzzia.core.util.xml.XMLDocumentException;
import com.comerzzia.core.util.xml.XMLDocumentTransformerException;
import com.comerzzia.model.general.tiendas.TiendaBean;
import com.comerzzia.servicios.sincronizacion.datos.ServicioDatosSincronizacion;

@Component
@Primary
public class CustomServicioDatosSincronizacion extends ServicioDatosSincronizacion {

	protected static Logger log = Logger.getLogger(CustomServicioDatosSincronizacion.class);
	protected static CustomServicioDatosSincronizacion instance;

	public static CustomServicioDatosSincronizacion get() {
		if (instance == null) {
			instance = new CustomServicioDatosSincronizacion();
		}
		return instance;
	}

	protected CustomDatosSincronizacionConfiguracion generarXmlSincronizacionConfiguracion(TiendaBean tienda, Connection conn, ConfigEmpresaBean config, String directorioScriptsTpv)
	        throws XMLDocumentException, SQLException, DatosSincronizadorException, XMLDocumentTransformerException, PorcentajeImpuestoException, ActividadNotFoundException {

		CustomDatosSincronizacionConfiguracion dsc = new CustomDatosSincronizacionConfiguracion(tienda, conn, config, directorioScriptsTpv);
		dsc.generaXML();
		return dsc;
	}

}
