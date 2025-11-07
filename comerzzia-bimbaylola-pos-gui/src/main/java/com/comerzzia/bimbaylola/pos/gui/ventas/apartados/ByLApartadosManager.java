package com.comerzzia.bimbaylola.pos.gui.ventas.apartados;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.comerzzia.bimbaylola.pos.services.apartados.ByLApartadosService;
import com.comerzzia.pos.gui.ventas.apartados.ApartadosManager;
import com.comerzzia.pos.gui.ventas.tickets.TicketManager;
import com.comerzzia.pos.persistence.apartados.ApartadosCabeceraBean;
import com.comerzzia.pos.persistence.apartados.detalle.ApartadosDetalleBean;
import com.comerzzia.pos.persistence.apartados.pagos.ApartadosPagoBean;
import com.comerzzia.pos.persistence.cajas.movimientos.CajaMovimientoBean;
import com.comerzzia.pos.persistence.mediosPagos.MedioPagoBean;
import com.comerzzia.pos.services.apartados.ApartadosService;
import com.comerzzia.pos.services.core.sesion.Sesion;
import com.comerzzia.pos.services.devices.DeviceException;
import com.comerzzia.pos.services.mediospagos.MediosPagosService;
import com.comerzzia.pos.services.ticket.Ticket;
import com.comerzzia.pos.services.ticket.TicketApartado;
import com.comerzzia.pos.services.ticket.TicketsServiceException;
import com.comerzzia.pos.services.ticket.lineas.LineaTicket;
import com.comerzzia.pos.services.ticket.pagos.tarjeta.DatosRespuestaPagoTarjeta;
import com.comerzzia.pos.servicios.impresion.ServicioImpresion;
import com.comerzzia.pos.util.config.SpringContext;
import com.comerzzia.pos.util.format.FormatUtil;

@Primary
@Component
public class ByLApartadosManager extends ApartadosManager {

	@Autowired
	protected Sesion sesion;
	@Autowired
	protected ApartadosService apartadosService;
	@Autowired
	protected MediosPagosService mediosPagosService;

	@Override
	public ApartadosDetalleBean introducirDetalleApartado(LineaTicket lineaArticulo) {

		ApartadosDetalleBean detalle = new ApartadosDetalleBean();

		detalle.setCantidad(lineaArticulo.getCantidad());
		detalle.setCodart(lineaArticulo.getCodArticulo());
		detalle.setCodimp(lineaArticulo.getCodImpuesto());
		detalle.setDescuento(lineaArticulo.getDescuento());
		detalle.setDesart(lineaArticulo.getDesArticulo());
		detalle.setDesglose1(lineaArticulo.getDesglose1());
		detalle.setDesglose2(lineaArticulo.getDesglose2());
		detalle.setEstadoLineaApartado((short) 0);
		detalle.setImporteTotal(lineaArticulo.getImporteTotalConDto());
		detalle.setPrecio(lineaArticulo.getPrecioTotalSinDto());
		detalle.setLinea(lineaArticulo.getIdLinea());
		detalle.setUidApartado(apartadoTicket.getCabecera().getUidApartado());
		detalle.setLinea(idLinea);
		detalle.setUidActividad(sesion.getAplicacion().getUidActividad());
		detalle.setFechaApartadoArticulo(new Date());
		detalle.setEstadoLineaApartado(ApartadosCabeceraBean.ESTADO_DISPONIBLE);
		detalle.setPrecioTotalSinDto(lineaArticulo.getPrecioTotalSinDto());
		detalle.setImporte(lineaArticulo.getImporteTotalSinDto());
		detalle.setPrecioCosto(lineaArticulo.getPrecioSinDto());
		detalle.setArticuloNuevo(true);
		detalle.setUsuario(sesion.getSesionUsuario().getUsuario().getUsuario());

		idLinea++;

		apartadoTicket.getArticulos().add(detalle);

		return detalle;
	}

	@Override
	public void nuevoTicketApartado() {
		apartadoTicket = SpringContext.getBean(TicketApartado.class);
		apartadoTicket.setArticulos(new ArrayList<ApartadosDetalleBean>());
		apartadoTicket.setMovimientos(new ArrayList<ApartadosPagoBean>());
		apartadoTicket.setCabecera(new ApartadosCabeceraBean());
		apartadoTicket.getCabecera().setSaldoCliente(BigDecimal.ZERO);
		idLinea = 1;

		setCliente(sesion.getAplicacion().getTienda().getCliente().clone());
	}

	public void grabarMovimientoDevolucion(ApartadosCabeceraBean cabeceraApartado, BigDecimal importe, MedioPagoBean medioPago, DatosRespuestaPagoTarjeta datosRespuestaPagoTarjeta)
	        throws TicketsServiceException {

		if(datosRespuestaPagoTarjeta == null) {
			grabarMovimientoDevolucion(cabeceraApartado, importe, medioPago);
		}else {
			grabarMovimientoDevolucionTarjeta(cabeceraApartado, importe, medioPago, datosRespuestaPagoTarjeta);
		}
		
	}
	
	@SuppressWarnings("rawtypes")
	public void grabarMovimientoDevolucionTarjeta(ApartadosCabeceraBean cabeceraApartado, BigDecimal importe, MedioPagoBean medioPago, DatosRespuestaPagoTarjeta datosRespuestaPagoTarjeta)
	        throws TicketsServiceException {
		
		CajaMovimientoBean detalleCaja = new CajaMovimientoBean();

		detalleCaja.setFecha(new Date());
		detalleCaja.setAbono(importe);
		detalleCaja.setCargo(BigDecimal.ZERO);
		detalleCaja.setCodConceptoMovimiento(ByLApartadosService.CODCONCEPTO_MOV_DEVOLUCION_RESERVA);
		detalleCaja.setConcepto("Apartado nº " + cabeceraApartado.getIdApartado());
		detalleCaja.setCodMedioPago(medioPago.getCodMedioPago());
		detalleCaja.setDesMedioPago(medioPago.getDesMedioPago());

		apartadosService.crearMovimientoDevolucion(cabeceraApartado, medioPago, detalleCaja);

		Long idTipoDocumento = null;

		Map<String, Object> mapaParametros = new HashMap<String, Object>();
		try {
			TicketManager ticketManager = SpringContext.getBean(TicketManager.class);
			ticketManager.nuevoTicket();
			Ticket ticketPrincipal = (Ticket) ticketManager.getTicket();
			((Ticket) ticketPrincipal).getCabecera().setTienda(sesion.getAplicacion().getTienda());
			((Ticket) ticketPrincipal).getCabecera().setEmpresa(sesion.getAplicacion().getEmpresa());
			idTipoDocumento = ticketPrincipal.getCabecera().getTipoDocumento();
			mapaParametros.put("ticket", ticketPrincipal);
		}
		catch (Exception e) {
			log.error("grabarMovimientoDevolucionTarjeta() - Ha habido un error al crear el ticket para la impresión: " + e.getMessage());
		}

		mapaParametros.put("apartado", cabeceraApartado);
		mapaParametros.put("importe", FormatUtil.getInstance().formateaImporte(importe));
		mapaParametros.put("fecha", FormatUtil.getInstance().formateaFechaHora(detalleCaja.getFecha()));
		mapaParametros.put("empresa", sesion.getAplicacion().getEmpresa());
		mapaParametros.put("cajero", sesion.getSesionUsuario().getUsuario().getDesusuario());
		mapaParametros.put("pago", detalleCaja);
		mapaParametros.put("datosRespuestaPagoTarjeta", datosRespuestaPagoTarjeta);
		
		if (mediosPagosService.isCodMedioPagoVale(medioPago.getCodMedioPago(), idTipoDocumento)) {
			mapaParametros.put("importeVale", importe);
		}
		try {
			ServicioImpresion.imprimir("informe_apartado", mapaParametros);
		}
		catch (DeviceException e) {
			log.error("grabarMovimientoDevolucionTarjeta() - " + e.getClass().getName() + " - " + e.getLocalizedMessage(), e);
		}
	}
}
