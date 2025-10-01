package com.comerzzia.bricodepot.backoffice.services.procesamiento.ventas.intervenciones;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.comerzzia.bricodepot.backoffice.persistence.intervenciones.Intervenciones;
import com.comerzzia.bricodepot.backoffice.services.intervenciones.ServicioIntervencionesSCOImpl;
import com.comerzzia.bricodepot.backoffice.services.intervenciones.exceptions.IntervencionesException;
import com.comerzzia.core.model.ventas.tickets.TicketBean;
import com.comerzzia.core.servicios.procesamiento.IProcesadorDocumento;
import com.comerzzia.core.servicios.procesamiento.ProcesadorDocumentoException;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.util.db.Connection;
import com.comerzzia.core.util.xml.XMLDocument;

public class ProcesadorIntervencionesSCO implements IProcesadorDocumento{
	
	protected static Logger log = Logger.getLogger(ProcesadorIntervencionesSCO.class);

	@Override
	public boolean procesaDocumento(Connection conn, DatosSesionBean datosSesion, TicketBean ticket, SqlSession sqlSession) throws ProcesadorDocumentoException {
		
		log.debug("procesaDocumento() - Iniciamos el procesamiento del Documento " + ticket.getIdTipoDocumento() + " - " + ticket.getIdTicket());
		try {
			XMLDocument xml = new XMLDocument(ticket.getXml());
			Intervenciones inter = new Intervenciones();
	
			inter.setUidActividad(datosSesion.getUidActividad());
			inter.setUidTicket(xml.getNodo("uidTicket").getValue());
			inter.setCodalm(xml.getNodo("codalm").getValue());
			inter.setCodcaja(xml.getNodo("codcaja").getValue());
			
	    	String fechaRecibida = xml.getNodo("fecha").getValue();
	    	
	    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
	        LocalDateTime dateTime = LocalDateTime.parse(fechaRecibida.substring(0, 19), formatter);
	        Date fecha = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());

			inter.setFecha(fecha);
	    	
			inter.setIdUsuario(Long.parseLong(xml.getNodo("idUsuario").getValue()));
			
	    	Integer resultado = ServicioIntervencionesSCOImpl.get().insertarIntervencionSCO(sqlSession, inter);
			
	    	if(resultado == 0){
	    		String mensajeError = "No se ha insertado correctamente la Auditoria";
	    		throw new IntervencionesException(mensajeError);
	    	}
		}catch(Exception e){
	    	String mensajeError = e.getMessage();
	    	log.error("procesaDocumento() - " + mensajeError, e);
	    	return Boolean.FALSE;
	    }
	    log.debug("procesaDocumento() - Finalizado correctamente el procesamiento del Documento " + ticket.getIdTipoDocumento() + " - " + ticket.getIdTicket());
		
		return Boolean.TRUE;
	}

}
