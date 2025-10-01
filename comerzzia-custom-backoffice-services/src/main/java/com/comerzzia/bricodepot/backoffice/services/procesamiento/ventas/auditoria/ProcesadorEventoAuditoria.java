package com.comerzzia.bricodepot.backoffice.services.procesamiento.ventas.auditoria;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.comerzzia.bricodepot.backoffice.persistence.tickets.auditoria.EventoAuditoriaBean;
import com.comerzzia.bricodepot.backoffice.services.tickets.auditoria.ParseadorEventoAuditoria;
import com.comerzzia.bricodepot.backoffice.services.tickets.auditoria.ServicioEventosAuditoria;
import com.comerzzia.core.model.documentos.enlaces.DocumentoEnlaceKey;
import com.comerzzia.core.model.ventas.tickets.TicketBean;
import com.comerzzia.core.persistencia.documentos.enlaces.DocumentoEnlaceMapper;
import com.comerzzia.core.servicios.procesamiento.IProcesadorDocumento;
import com.comerzzia.core.servicios.procesamiento.ProcesadorDocumentoException;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.servicios.ventas.tickets.TicketService;
import com.comerzzia.core.util.db.Connection;


public class ProcesadorEventoAuditoria implements IProcesadorDocumento {

	protected static Logger log = Logger.getLogger(ProcesadorEventoAuditoria.class);

	@Autowired
	protected ServicioEventosAuditoria servicio;
	
	@Autowired
	protected TicketService ticketService;

	@Override
	public boolean procesaDocumento(Connection conn, DatosSesionBean datosSesion, TicketBean ticket,
			SqlSession sqlSession) throws ProcesadorDocumentoException {
		boolean procesado = false;
		try {
			EventoAuditoriaBean eventoAuditoria = ParseadorEventoAuditoria.parse(ticket.getXml().getDocumentElement());
			eventoAuditoria.setUidInstancia(datosSesion.getUidInstancia());
			eventoAuditoria.setUidTicketAuditoria(ticket.getUidTicket());
			eventoAuditoria.setIdTicketAuditoria(ticket.getIdTicket());

			// Si el evento no existe, se guarda
			if (ServicioEventosAuditoria.get().consultarUno(eventoAuditoria.getUidTicketAuditoria(),
					eventoAuditoria.getUidInstancia(), eventoAuditoria.getUidActividad(), datosSesion) == null) {
				ServicioEventosAuditoria.get().crear(sqlSession, eventoAuditoria, datosSesion);

				TicketBean ticketVenta;
				try {
					ticketVenta = ticketService.consultarTicketUid(eventoAuditoria.getUidTicketVenta(),
							datosSesion.getUidActividad());
					
					DocumentoEnlaceMapper mapper = sqlSession.getMapper(DocumentoEnlaceMapper.class);

					DocumentoEnlaceKey docEnlace = new DocumentoEnlaceKey();
					docEnlace.setUidActividad(datosSesion.getUidActividad());
					docEnlace.setIdClase("X_AUDITORIAS_TBL.UID_TICKET_AUDITORIA");
					docEnlace.setIdObjeto(eventoAuditoria.getUidTicketAuditoria());
					docEnlace.setUidDocumento(ticketVenta.getUidTicket());

					mapper.insertSelective(docEnlace);
				} catch (Exception ignore) {
					//ignore
					//significa que el ticket fue anulado
				}
			}

			procesado = true;

		} catch (Exception e) {
			throw new ProcesadorDocumentoException("Error procesando el documento: " + e.getMessage(), e);
		}
		return procesado;
	}

}
