package com.comerzzia.bricodepot.backoffice.services.procesamiento.ventas.anticipos;

import org.apache.ibatis.session.SqlSession;

import com.comerzzia.bricodepot.backoffice.persistence.albaranes.venta.pagos.anticipos.Anticipos;
import com.comerzzia.bricodepot.backoffice.services.anticipos.AnticiposServiceImpl;
import com.comerzzia.bricodepot.backoffice.services.ventas.tickets.anticipos.AnticiposParser;
import com.comerzzia.core.model.ventas.tickets.TicketBean;
import com.comerzzia.core.servicios.procesamiento.IProcesadorDocumento;
import com.comerzzia.core.servicios.procesamiento.ProcesadorDocumentoException;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.util.db.Connection;

public class ProcesadorAnticiposErroneos implements IProcesadorDocumento {

	@Override
	public boolean procesaDocumento(Connection conn, DatosSesionBean datosSesion, TicketBean ticket, SqlSession sqlSession) throws ProcesadorDocumentoException {

		boolean procesado = false;
		try {

			Anticipos anticipo = AnticiposParser.parse(ticket.getXml().getDocumentElement());

			AnticiposServiceImpl anticiposService = AnticiposServiceImpl.get();
			Anticipos anticipoAntiguo = anticiposService.consultarAnticipo(sqlSession, datosSesion, anticipo.getNumAnticipo());

			anticiposService.actualizaEstadoAnticipo(sqlSession, datosSesion, anticipoAntiguo, anticipo.getEstado());

			procesado = true;

		}
		catch (Exception e) {
			throw new ProcesadorDocumentoException("Error procesando el documento: " + e.getMessage(), e);
		}
		return procesado;
	}

}
