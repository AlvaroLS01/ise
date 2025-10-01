package com.comerzzia.bricodepot.backoffice.services.ventas.tickets.albaranes.pagos;

import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;

import com.comerzzia.bricodepot.backoffice.persistence.albaranes.venta.pagos.CustomAlbaranPag;
import com.comerzzia.bricodepot.backoffice.persistence.albaranes.venta.pagos.CustomAlbaranPagMapper;
import com.comerzzia.bricodepot.backoffice.persistence.movimientos.albaranes.pagos.CustomCuentaMovimientoKey;
import com.comerzzia.bricodepot.backoffice.persistence.movimientos.albaranes.pagos.CustomCuentaMovimientoMapper;
import com.comerzzia.core.model.empresas.ConfigEmpresaBean;
import com.comerzzia.core.util.db.Connection;
import com.comerzzia.model.ventas.albaranes.pagos.PagoAlbaranVentaBean;
import com.comerzzia.model.ventas.albaranes.pagos.PagoTarjetaRegaloBean;
import com.comerzzia.persistencia.ventas.albaranes.pagos.PagosAlbaranesVentasDao;
import com.comerzzia.servicios.ventas.albaranes.pagos.PagoAlbaranVentaException;
import com.comerzzia.servicios.ventas.albaranes.pagos.ServicioPagosAlbaranesVentasImpl;

public class CustomServicioPagosAlbaranesVentasImpl extends ServicioPagosAlbaranesVentasImpl {

	public void crear(PagoAlbaranVentaBean pago, ConfigEmpresaBean configEmpresa, Connection conn)
			throws PagoAlbaranVentaException {
		try {
			log.debug("crear() - Añadiendo pago al albaran de venta " + pago.getIdAlbaran());

			PagosAlbaranesVentasDao.insert(conn, pago, configEmpresa);
			creaPagoCustom(pago, configEmpresa, conn);
			if (pago.getPagosTarjetaRegalo() != null && !pago.getPagosTarjetaRegalo().isEmpty()) {
				log.debug("crear() - Se añade la relación entre el pago y la cuenta movimiento");
				creaCuentaMovAlbaranPag(pago, configEmpresa, conn);

			}
		} catch (SQLException e) {
			String msg = "Error añadiendo pago a albaran de venta: " + e.getMessage();
			log.error("crear() - " + msg);

			throw new PagoAlbaranVentaException(msg, e);
		}
	}

	private void creaPagoCustom(PagoAlbaranVentaBean pago, ConfigEmpresaBean configEmpresa, Connection conn) {
		log.debug("creaPagoCustom()");
		SqlSession sesion = null;
		sesion = conn.getSqlSession();

		CustomAlbaranPagMapper mapper = sesion.getMapper(CustomAlbaranPagMapper.class);
		CustomAlbaranPag albaranPag = new CustomAlbaranPag();
		if (StringUtils.isNotBlank(((String) pago.getExtension("num_pedido")))) {
			log.debug("creaPagoCustom() - Numero Pedido: " + (String) pago.getExtension("num_pedido"));
			albaranPag.setUidActividad(configEmpresa.getUidActividad());
			albaranPag.setIdClieAlbaran(pago.getIdAlbaran());
			albaranPag.setLinea(pago.getLinea());
			albaranPag.setNumPedido((String) pago.getExtension("num_pedido"));
			mapper.insert(albaranPag);
			log.debug("creaPagoCustom() - Insercion realizada");
		}
	}

	private void creaCuentaMovAlbaranPag(PagoAlbaranVentaBean pago, ConfigEmpresaBean configEmpresa, Connection conn) {
		SqlSession sesion = null;
		sesion = conn.getSqlSession();

		for (PagoTarjetaRegaloBean pagoTarjetaRegalo : pago.getPagosTarjetaRegalo()) {

			CustomCuentaMovimientoMapper mapper = sesion.getMapper(CustomCuentaMovimientoMapper.class);
			CustomCuentaMovimientoKey customCuentaMovimientoKey = new CustomCuentaMovimientoKey();
			customCuentaMovimientoKey.setIdClieAlbaran(pago.getIdAlbaran());
			customCuentaMovimientoKey.setUidTransaccion(pagoTarjetaRegalo.getUidTransaccionTarjetaRegalo());
			customCuentaMovimientoKey.setLinea(pago.getLinea());
			customCuentaMovimientoKey.setUidActividad(configEmpresa.getUidActividad());
			customCuentaMovimientoKey.setUidInstancia(configEmpresa.getUidInstancia());
			mapper.insert(customCuentaMovimientoKey);

		}

	}
}
