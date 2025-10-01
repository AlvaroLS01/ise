package com.comerzzia.bricodepot.backoffice.services.procesamiento.ventas.movimientos.retirada;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.comerzzia.aena.util.xml.MarshallUtil;
import com.comerzzia.bricodepot.backoffice.persistence.movimientos.retirada.DetMovRetiradaKey;
import com.comerzzia.bricodepot.backoffice.persistence.movimientos.retirada.MovimientosRetirada;
import com.comerzzia.bricodepot.backoffice.services.movimientos.retirada.MovimientosRetiradaService;
import com.comerzzia.bricodepot.backoffice.util.movimientos.retirada.RetiradaABancoDTO;
import com.comerzzia.core.model.ventas.tickets.TicketBean;
import com.comerzzia.core.omnichannel.engine.persistence.cajas.movimientos.CajaMovimientoBean;
import com.comerzzia.core.servicios.procesamiento.IProcesadorDocumento;
import com.comerzzia.core.servicios.procesamiento.ProcesadorDocumentoException;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.util.db.Connection;

public class ProcesadorMovimientosRetirada implements IProcesadorDocumento {
	
	protected static Logger log = Logger.getLogger(ProcesadorMovimientosRetirada.class);

	@Override
	public boolean procesaDocumento(Connection conn, DatosSesionBean datosSesion, TicketBean ticket,
			SqlSession sqlSession) throws ProcesadorDocumentoException {
		boolean procesado = false;
		try {
			RetiradaABancoDTO retiradaABancoDTO = (RetiradaABancoDTO) MarshallUtil.leerXML(ticket.getTicket(),
					RetiradaABancoDTO.class);

			MovimientosRetiradaService movimientosRetiradaService = MovimientosRetiradaService.get();

			CajaMovimientoBean mov91 = retiradaABancoDTO.getMovimiento91();
			List<DetMovRetiradaKey> movimientos90 = retiradaABancoDTO.getMovimientos90();
			if (movimientos90 != null && !movimientos90.isEmpty()) {
				for (DetMovRetiradaKey movimiento90 : movimientos90) {
					MovimientosRetirada movRetirada = new MovimientosRetirada();
					movRetirada.setLinea90(movimiento90.getLinea());
					movRetirada.setLinea91(mov91.getLinea());
					movRetirada.setUidDiarioCaja(movimiento90.getUidDiarioCaja());
					movRetirada.setUidActividad(mov91.getUidActividad());
					movRetirada.setFechaBolsa(mov91.getFecha());
					movRetirada.setImporte(mov91.getAbono());
					movRetirada.setUsuario(mov91.getUsuario());
					movRetirada.setDocumento(mov91.getDocumento());
					if (movimientosRetiradaService.existeMovimientoRetirada(sqlSession, datosSesion, movRetirada)) {
						log.warn("El movimiento ya existe, se omite: " + movRetirada);
					}
					else {
						movimientosRetiradaService.añadirMovimientoRetirada(sqlSession, datosSesion, movRetirada);
					}
				}
			}

			procesado = true;

		} catch (Exception e) {
			throw new ProcesadorDocumentoException("Error procesando el documento: " + e.getMessage(), e);
		}
		return procesado;
	}
}
