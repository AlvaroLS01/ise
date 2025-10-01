package com.comerzzia.bricodepot.backoffice.services.procesamiento.ventas.cajas;

import org.apache.ibatis.session.SqlSession;

import com.comerzzia.aena.util.xml.MarshallUtil;
import com.comerzzia.bricodepot.backoffice.services.ventas.cajas.BricodepotServicioCajasImpl;
import com.comerzzia.bricodepot.backoffice.util.movimientos.retirada.RetiradaABancoDTO;
import com.comerzzia.core.model.ventas.tickets.TicketBean;
import com.comerzzia.core.servicios.procesamiento.IProcesadorDocumento;
import com.comerzzia.core.servicios.procesamiento.ProcesadorDocumentoException;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.util.db.Connection;

public class ProcesadorCajasFicticias implements IProcesadorDocumento {

	@Override
	public boolean procesaDocumento(Connection conn, DatosSesionBean datosSesion, TicketBean ticket,
			SqlSession sqlSession) throws ProcesadorDocumentoException {
		boolean procesado = false;
		try {
			RetiradaABancoDTO retiradaABancoDTO = (RetiradaABancoDTO) MarshallUtil.leerXML(ticket.getTicket(),
					RetiradaABancoDTO.class);

			BricodepotServicioCajasImpl.get().addSaldoCajaFicticia(datosSesion, retiradaABancoDTO.getUidActividad(), ticket.getCodCaja(), ticket.getCodAlmacen(), retiradaABancoDTO.getMovimiento91().getAbono().negate());
			procesado = true;

		} catch (Exception e) {
			throw new ProcesadorDocumentoException("Error procesando el documento: " + e.getMessage(), e);
		}
		return procesado;
	}
}
