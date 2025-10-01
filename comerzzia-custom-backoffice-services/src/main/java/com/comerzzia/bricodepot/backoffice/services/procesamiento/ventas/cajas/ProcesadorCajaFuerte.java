package com.comerzzia.bricodepot.backoffice.services.procesamiento.ventas.cajas;

import org.apache.ibatis.session.SqlSession;

import com.comerzzia.aena.util.xml.MarshallUtil;
import com.comerzzia.bricodepot.backoffice.services.ventas.cajas.BricodepotServicioCajasImpl;
import com.comerzzia.bricodepot.backoffice.util.movimientos.cajasficticias.CajaFicticiaDTO;
import com.comerzzia.core.model.ventas.tickets.TicketBean;
import com.comerzzia.core.servicios.procesamiento.IProcesadorDocumento;
import com.comerzzia.core.servicios.procesamiento.ProcesadorDocumentoException;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.util.db.Connection;

import jxl.common.Logger;

public class ProcesadorCajaFuerte implements IProcesadorDocumento{
	//PROCESADOR CAJA 80
	protected static Logger log = Logger.getLogger(ProcesadorCajaFuerte.class);
	
	@Override
	public boolean  procesaDocumento(Connection conn, DatosSesionBean datosSesion, TicketBean ticket,
			SqlSession sqlSession) throws ProcesadorDocumentoException {
		log.debug("procesaDocumento() - procesando ticket con id : "+ticket.getIdTicket());
		boolean procesado = false;
		try {
			CajaFicticiaDTO caja = (CajaFicticiaDTO) MarshallUtil.leerXML(ticket.getTicket(),CajaFicticiaDTO.class);
			BricodepotServicioCajasImpl.get().insertarMovimientoCaja(datosSesion, ticket,sqlSession, caja);
			procesado = true;
		}catch (Exception e) {
			throw new ProcesadorDocumentoException("Error procesando el documento: " + e.getMessage(), e);
		}
		return procesado;
	}
}
