package com.comerzzia.bricodepot.backoffice.web.ventas.cajas.acciones;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import com.comerzzia.bricodepot.backoffice.persistence.ventas.cajas.movimientos.MovimientoJasperBean;
import com.comerzzia.bricodepot.backoffice.services.ventas.cajas.BricodepotServicioCajasImpl;
import com.comerzzia.core.model.informes.TrabajoInformeBean;
import com.comerzzia.core.model.informes.VersionInformeBean;
import com.comerzzia.core.omnichannel.engine.persistence.cajas.movimientos.CajaMovimientoBean;
import com.comerzzia.core.servicios.informes.InformeException;
import com.comerzzia.core.servicios.informes.InformeNotFoundException;
import com.comerzzia.core.servicios.informes.InformesService;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.util.fechas.Fecha;
import com.comerzzia.core.util.fechas.Fechas;
import com.comerzzia.model.fidelizacion.tarjetas.tipos.TipoTarjetaBean;
import com.comerzzia.model.general.mediospago.MedioPagoBean;
import com.comerzzia.servicios.fidelizacion.tarjetas.tipos.ServicioTiposTarjetasImpl;
import com.comerzzia.servicios.general.mediospago.MedioPagoException;
import com.comerzzia.servicios.general.mediospago.ServicioMediosPagoImpl;
import com.comerzzia.web.backoffice.ventas.cajas.acciones.ImprimirAccion;
import com.comerzzia.web.base.Vista;
import com.comerzzia.web.base.WebKeys;
import com.comerzzia.web.i18n.ui.i18nUtil;
import com.ibm.icu.text.DecimalFormat;

public class BricoDepotImprimirAccion extends ImprimirAccion {

	private static final String CODALMACEN_SELECCIONADO = "CODALMACEN_SELECCIONADO";
	private static final String EFECTIVO = "EFECTIVO";
	private static final String TARJETA = "TARJETA";
	private static final String TARJETA_MANUAL = "TARJETA MANUAL";

	@Autowired
	InformesService informesService;

	public String getNombreInforme() {
		return "ventas.cajas.lstCierreYMovimientoCaja";
	}

	public Vista vistaInforme(HttpServletRequest request) {

		HttpSession sesion = request.getSession();
		DatosSesionBean datosSesion = (DatosSesionBean) sesion.getAttribute(WebKeys.DATOS_SESION);

		// Inicializar datos del formulario de lanzamiento del informe
		Date hoy = new Date();
		request.setAttribute("fechaDesde", Fechas.sumaDias(hoy, -1));
		request.setAttribute("fechaHasta", hoy);

		request.setAttribute("mediosPagoDisponibles", getListaStringMediosPagoDisponible(datosSesion));
		request.setAttribute("tiposTarjetaDisponibles", getListaStringTiposTarjetaDisponible(datosSesion));

		return new Vista("backoffice/backoffice_out_store/ventas/cajas/buscar/jsp/imprimir.jsp", Vista.INTERNA);
	}

	public void imprimirInforme(HttpServletRequest request, TrabajoInformeBean trabajoInforme) throws InformeException {

		HttpSession sesion = request.getSession();
		DatosSesionBean datosSesion = (DatosSesionBean) sesion.getAttribute(WebKeys.DATOS_SESION);
		trabajoInforme.addParametro("UID_ACTIVIDAD", datosSesion.getUidActividad());
		// ALMACEN
		trabajoInforme.addParametro("CODALMACEN_DESDE", (String) datosSesion.getAtributos().get(CODALMACEN_SELECCIONADO));
		trabajoInforme.addParametro("CODALMACEN_HASTA", (String) datosSesion.getAtributos().get(CODALMACEN_SELECCIONADO));

		Fecha fechaDesde = null;
		Fecha fechaHasta = null;
		try {

			// FechaDesde
			if (request.getParameter("fechaDesde").isEmpty()) {
				trabajoInforme.addParametro("FECHA_DESDE", null);
			}
			else {
				fechaDesde = new Fecha(request.getParameter("fechaDesde"), "dd/MM/yyyy");
				trabajoInforme.addParametro("FECHA_DESDE", fechaDesde.getTimestamp());

			}

			// FechaHasta
			if (request.getParameter("fechaHasta").isEmpty()) {
				trabajoInforme.addParametro("FECHA_HASTA", null);
			}
			else {

				fechaHasta = new Fecha(request.getParameter("fechaHasta") + " 23:59:59", "dd/MM/yyyy HH:mm:ss");
				trabajoInforme.addParametro("FECHA_HASTA", fechaHasta.getTimestamp());
			}

			if (!request.getParameter("codCaja").isEmpty()) {
				trabajoInforme.addParametro("CODCAJA", request.getParameter("codCaja"));
			}
			if (!request.getParameter("numAnticipo").isEmpty()) {
				trabajoInforme.addParametro("NUM_ANTICIPO", request.getParameter("numAnticipo"));
			}
			if (!request.getParameter("estado").isEmpty()) {
				trabajoInforme.addParametro("ESTADO", request.getParameter("estado"));
			}

			if (!request.getParameter("desCajero").isEmpty()) {
				trabajoInforme.addParametro("CAJERO", request.getParameter("desCajero").toUpperCase());
			}

			if (request.getParameter("radioTipoFechaTarjeta") != null) {
				switch (request.getParameter("radioTipoFechaTarjeta")) {
					case "fechaEmision":
						trabajoInforme.addParametro("TIPO_FECHA_TARJETA", "fechaEmision");
						break;
					case "fechaRecepcion":
						trabajoInforme.addParametro("TIPO_FECHA_TARJETA", "fechaRecepcion");
						break;
				}
			}

			if (!request.getParameter("formaPago").isEmpty()) {
				trabajoInforme.addParametro("FORMA_PAGO", request.getParameter("formaPago"));
			}
			else {
				trabajoInforme.addParametro("FORMA_PAGO", null);
			}
			if (!request.getParameter("tipoTarjeta").isEmpty()) {
				trabajoInforme.addParametro("TIPO_TARJETA", request.getParameter("tipoTarjeta"));
			}
			else {
				trabajoInforme.addParametro("TIPO_TARJETA", null);
			}

			trabajoInforme.addParametro("ORIGENTIENDA", Boolean.TRUE);
		}
		catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	private List<MedioPagoBean> getListaStringMediosPagoDisponible(DatosSesionBean datosSesion) {

		List<MedioPagoBean> consulta = new ArrayList<MedioPagoBean>();
		List<MedioPagoBean> res = new ArrayList<MedioPagoBean>();

		try {
			consulta = ServicioMediosPagoImpl.get().consultarTodos(datosSesion.getConfigEmpresa());
		}
		catch (MedioPagoException e) {
			log.error("Error al consultar los medios de pago: " + e.getMessage(), e);
		}

		for (MedioPagoBean medioPago : consulta) {
			if (!medioPago.getDesMedioPago().equals(EFECTIVO) && !medioPago.getDesMedioPago().equals(TARJETA) && !medioPago.getDesMedioPago().equals(TARJETA_MANUAL)) {
				res.add(medioPago);
			}
		}

		return res;
	}

	private List<TipoTarjetaBean> getListaStringTiposTarjetaDisponible(DatosSesionBean datosSesion) {
		List<TipoTarjetaBean> consulta = new ArrayList<TipoTarjetaBean>();
		consulta = ServicioTiposTarjetasImpl.get().consultarTodos(datosSesion);
		return consulta;
	}

	@Override
	protected Vista imprimir(HttpServletRequest request, DatosSesionBean datosSesion, HttpSession session) throws InformeException, NumberFormatException, InformeNotFoundException {
		ERROR = ERROR_WIN;
		TrabajoInformeBean trabajoInforme = (TrabajoInformeBean) session.getAttribute(WebKeys.TRABAJO_INFORME);
		String idVersion = request.getParameter("idVersion");
		String tipo = request.getParameter("tipo");
		VersionInformeBean version = null;
		if (idVersion == null || idVersion.isEmpty()) {
			List<VersionInformeBean> listaVersiones = informesService.consultarVersionesInforme(trabajoInforme.getIdInforme());
			if (!listaVersiones.isEmpty()) {
				trabajoInforme.setVersion(listaVersiones.get(0));
			}
		}
		else {
			version = informesService.consultarVersionInforme(trabajoInforme.getIdInforme(), Long.parseLong(idVersion));
			trabajoInforme.setVersion(version);
		}

		if (!permisosInforme.isPuede(trabajoInforme.getVersion().getVersion())) {
			return SIN_PERMISO;
		}

		// añadimos como parámetros los datos de la empresa
		trabajoInforme = añadirDatosEmpresa(trabajoInforme, datosSesion);
		
		if(trabajoInforme.getVersion().getVersion().equals("infRetiradas")) {
			
			addParametrosMovimientos(trabajoInforme, datosSesion);
		}

		// Añadimos el locale para la internacionalización
		trabajoInforme.addParametro("REPORT_LOCALE", i18nUtil.getLocale(i18nUtil.getIdiomaSeleccionado(request, datosSesion)));

		imprimirInforme(request, trabajoInforme);
		if ("pdf".equalsIgnoreCase(tipo)) {
			trabajoInforme.addParametro("tipo", "pdf");
			return DOWNLOAD_PAGE;
		}
		else if ("rtf".equalsIgnoreCase(tipo)) {
			trabajoInforme.addParametro("tipo", "rtf");
			return DOWNLOAD_PAGE;
		}
		// else if ("excel".equalsIgnoreCase(tipo)) {
		else {
			trabajoInforme.addParametro("tipo", "excel");
			return DOWNLOAD_PAGE;
		}
	}
	
	@Override
	protected Vista imprimirRapido(HttpServletRequest request, DatosSesionBean datosSesion, HttpSession session) throws InformeException, InformeNotFoundException {
		TrabajoInformeBean trabajoInforme = new TrabajoInformeBean();
		VersionInformeBean version = null;
		Long idInforme = getAccionInforme().getIdAccion();

		// Intentamos obtener la versión por defecto del informe.

		version = informesService.getVersionPorDefecto(idInforme);

		trabajoInforme.setVersion(version);
		trabajoInforme.setIdInforme(idInforme);
		trabajoInforme.setInforme(getAccionInforme().getAccion());
		trabajoInforme.setTitulo(getAccionInforme().getTitulo());

		// añadimos como parámetros los datos de la empresa
		trabajoInforme = añadirDatosEmpresa(trabajoInforme, datosSesion);
		
		//Añadimos el locale para la internacionalización
		trabajoInforme.addParametro("REPORT_LOCALE", i18nUtil.getLocale(i18nUtil.getIdiomaSeleccionado(request , datosSesion)));

		imprimirInforme(request, trabajoInforme);
		
		trabajoInforme.addParametro("tipo", "pdf");
		
		session.setAttribute(WebKeys.TRABAJO_INFORME, trabajoInforme);
		return DOWNLOAD_PAGE;
	}
	
	private void addParametrosMovimientos(TrabajoInformeBean trabajoInforme, DatosSesionBean datosSesion) {
		CajaMovimientoBean movimiento91 = getUltimoMovimiento91deCaja90(datosSesion);
		if (movimiento91 == null) {
			movimiento91 = new CajaMovimientoBean();
		}
		Date fechaFiltro = movimiento91.getFecha() == null ? null : movimiento91.getFecha();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		trabajoInforme.getParametros().put("FECHAULTMOV", fechaFiltro == null ? null : sdf.format(fechaFiltro));

		List<CajaMovimientoBean> lista90 = getListaMovimientos90(datosSesion);
		if (lista90 == null) {
			lista90 = new ArrayList<CajaMovimientoBean>();
		}
		BigDecimal sumCargo = BigDecimal.ZERO;
		if (!lista90.isEmpty() && lista90 != null) {
			Collections.reverse(lista90);
			List<CajaMovimientoBean> cajaMovimientoPorFecha = lista90;
			if (fechaFiltro != null) {
				cajaMovimientoPorFecha = lista90.stream().filter(c -> c.getFecha().compareTo(fechaFiltro) > 0)
						.collect(Collectors.toList());
			}
			List<MovimientoJasperBean> listaJasper = new ArrayList<MovimientoJasperBean>();
			for (CajaMovimientoBean cajaMov : cajaMovimientoPorFecha) {
				MovimientoJasperBean movJasper = new MovimientoJasperBean();

				movJasper.setCargo(formatearNumero(cajaMov.getCargo(), 2));
				movJasper.setDocumento(cajaMov.getDocumento());
				movJasper.setFecha(sdf.format(cajaMov.getFecha()));
				movJasper.setUsuario(cajaMov.getUsuario());

				listaJasper.add(movJasper);
			}

			trabajoInforme.getParametros().put("LISTAMOV", listaJasper);

			for (CajaMovimientoBean caja : cajaMovimientoPorFecha) {
				sumCargo = sumCargo.add(caja.getCargo());
			}
			trabajoInforme.getParametros().put("SUMCARGO", formatearNumero(sumCargo, 2));
		}

		trabajoInforme.getParametros().put("CODALMACEN", datosSesion.getAtributos().get("CODALMACEN_SELECCIONADO"));
		trabajoInforme.getParametros().put("DESALMACEN", datosSesion.getAtributos().get("DESALMACEN_SELECCIONADO"));
		trabajoInforme.getParametros().put("USUARIO", datosSesion.getUser());
		Date fechaactual = new Date();
		trabajoInforme.getParametros().put("FECHAACTUAL", sdf.format(fechaactual));
		
	}
	
	private List<CajaMovimientoBean> getListaMovimientos90(DatosSesionBean datosSesion) {
		String codAlmacen = (String) datosSesion.getAtributos().get("CODALMACEN_SELECCIONADO");
		log.debug("getUltimoMovimiento90deCaja90() - Obteniendo lista de movimientos 90 de caja 90 para el almacen: "+ codAlmacen);
		List<CajaMovimientoBean> lista = BricodepotServicioCajasImpl.get().consultarMovimientoHistorico(datosSesion,"90", codAlmacen);
		return lista;

	}

	private CajaMovimientoBean getUltimoMovimiento91deCaja90(DatosSesionBean datosSesion) {
		String codAlmacen = (String) datosSesion.getAtributos().get("CODALMACEN_SELECCIONADO");
		log.debug("getUltimoMovimiento91deCaja90() - Obteniendo ultimo movimiento 91 de caja 90 para el almacen: "+ codAlmacen);
		CajaMovimientoBean movimiento91 = null;
		List<CajaMovimientoBean> lista = BricodepotServicioCajasImpl.get().consultarMovimientoHistorico(datosSesion,"91", codAlmacen);
		if (lista.size() > 0) {
			Collections.reverse(lista);
			movimiento91 = lista.get(0);
		}
		return movimiento91;
	}
	
	private String formatearNumero(BigDecimal numero, Integer decimales) {
		DecimalFormat format = new DecimalFormat();
		if (decimales != null && decimales >= 0) {
			format.setMinimumFractionDigits(decimales);
			format.setMaximumFractionDigits(decimales);
		}
		else { // Si el número tiene decimales, debería estar configurado. En cualquier caso permitimoe entre 0 y 4
			   // decimales
			format.setMinimumFractionDigits(0);
			format.setMaximumFractionDigits(4);
		}
		return format.format(numero);
	}

}
