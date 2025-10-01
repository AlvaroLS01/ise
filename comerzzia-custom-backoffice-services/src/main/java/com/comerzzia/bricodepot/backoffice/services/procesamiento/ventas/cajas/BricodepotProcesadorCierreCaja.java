package com.comerzzia.bricodepot.backoffice.services.procesamiento.ventas.cajas;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.comerzzia.bricodepot.backoffice.services.ventas.cajas.BricodepotServicioCajasImpl;
import com.comerzzia.core.model.ventas.tickets.TicketBean;
import com.comerzzia.core.servicios.procesamiento.ProcesadorDocumentoException;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.util.db.Connection;
import com.comerzzia.core.util.xml.XMLDocument;
import com.comerzzia.model.ventas.cajas.cabecera.CabeceraCaja;
import com.comerzzia.servicios.procesamiento.ventas.cajas.ProcesadorCierreCaja;
import com.comerzzia.servicios.ventas.cajas.CajaParser;
import com.comerzzia.servicios.ventas.cajas.cabecera.ServicioCabeceraCajasImpl;
import com.comerzzia.servicios.versionado.cajas.ServicioVersionadoCierresCajaImpl;

public class BricodepotProcesadorCierreCaja extends ProcesadorCierreCaja{

	protected static Logger log = Logger.getLogger(BricodepotProcesadorCierreCaja.class);
	
	
	@Override
	public boolean procesaDocumento(Connection conn, DatosSesionBean datosSesion, TicketBean ticket, SqlSession sqlSession) throws ProcesadorDocumentoException {
		boolean procesado = false;
		try {
			CajaParser cajaParser = new CajaParser();
			XMLDocument cierreCajaXML = new XMLDocument(ticket.getXml());
			CabeceraCaja cierreCaja = cajaParser.parse(cierreCajaXML);
		
			//Si el cierre de caja ya existe, no procesamos
			if ( !cierreCaja.getCodcaja().equals(BricodepotServicioCajasImpl.CAJA_PROSEGUR) && !cierreCaja.getCodcaja().equals(BricodepotServicioCajasImpl.CAJA_FUERTE)) {
				if(ServicioCabeceraCajasImpl.get().consultar(datosSesion.getConfigEmpresa(), cierreCaja.getUidDiarioCaja(), conn) == null) {
					ServicioCabeceraCajasImpl.get().crear(datosSesion, conn, cierreCaja);
					log.debug("procesaDocumento() - Documento de cierre de caja guardado en BDD para uidDiarioCaja: " + cierreCaja.getUidDiarioCaja());
				}
			}
			// Versionamos cierre de caja
			ServicioVersionadoCierresCajaImpl.get().versionar(sqlSession, datosSesion, cierreCaja.getUidDiarioCaja());
			
			procesado = true;
		}
		catch (Exception e) {
			throw new ProcesadorDocumentoException("Error procesando el documento: " + e.getMessage(), e);
		}
		
		return procesado;
	}
}
