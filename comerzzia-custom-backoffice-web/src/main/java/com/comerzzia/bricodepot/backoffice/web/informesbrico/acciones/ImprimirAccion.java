package com.comerzzia.bricodepot.backoffice.web.informesbrico.acciones;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.comerzzia.core.model.informes.TrabajoInformeBean;
import com.comerzzia.core.servicios.informes.InformeException;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.util.fechas.FechaException;
import com.comerzzia.core.util.fechas.Fechas;
import com.comerzzia.web.base.InformeAccion;
import com.comerzzia.web.base.Vista;
import com.comerzzia.web.base.WebKeys;

public class ImprimirAccion extends InformeAccion {

	@Override
	public String getNombreInforme() {
		return "informesBrico.lstInformesBrico";
	}

	@Override
	public Vista vistaInforme(HttpServletRequest request) {
		Date hoy = new Date();
		Date semanaPasada = Fechas.sumaDias(hoy, -7);

		request.setAttribute("fechaDesde", semanaPasada);
		request.setAttribute("fechaHasta", hoy);

		return new Vista("backoffice/informesBrico/buscar/jsp/informe.jsp", Vista.INTERNA);
	}

	@Override
	public void imprimirInforme(HttpServletRequest request, TrabajoInformeBean trabajoInforme) throws InformeException {
		try {
			HttpSession sesion = request.getSession();
			DatosSesionBean datosSesion = (DatosSesionBean) sesion.getAttribute(WebKeys.DATOS_SESION);

			trabajoInforme.addParametro("UID_ACTIVIDAD", datosSesion.getUidActividad());
			if (!request.getParameter("fechaDesde").isEmpty()) {
				Date fechaDesde = Fechas.getFecha(request.getParameter("fechaDesde"));
				trabajoInforme.addParametro("FECHA_DESDE", Fechas.toSqlTimestamp(fechaDesde));
			}
			if (!request.getParameter("fechaHasta").isEmpty()) {
				Date fechaHasta = Fechas.getFecha(request.getParameter("fechaHasta"));
				trabajoInforme.addParametro("FECHA_HASTA", Fechas.toSqlTimestamp(fechaHasta));
			}
		}
		catch (FechaException e) {
		}

		// Tienda desde
		if (!request.getParameter("codTieDesde").isEmpty()) {
			trabajoInforme.addParametro("COD_TIENDA_DESDE", request.getParameter("codTieDesde"));
			trabajoInforme.addParametro("DES_TIENDA_DESDE", request.getParameter("desTieDesde"));
		}

		// Tienda hasta
		if (!request.getParameter("codTieHasta").isEmpty()) {
			trabajoInforme.addParametro("COD_TIENDA_HASTA", request.getParameter("codTieHasta"));
			trabajoInforme.addParametro("DES_TIENDA_HASTA", request.getParameter("desTieHasta"));
		}

		// caja desde
		if (!request.getParameter("cajaDesde").isEmpty()) {
			trabajoInforme.addParametro("CAJA_DESDE", request.getParameter("cajaDesde"));
		}

		// caja hasta
		if (!request.getParameter("cajaHasta").isEmpty()) {
			trabajoInforme.addParametro("CAJA_HASTA", request.getParameter("cajaHasta"));
		}

		// cajero
		if (!request.getParameter("cajero").isEmpty()) {
			trabajoInforme.addParametro("CAJERO", request.getParameter("cajero"));
		}

		// Fecha Emision Uso tarjeta
		if (!request.getParameter("fechaEmisionUsoTarjeta").isEmpty()) {
			try {
				Date fechaEmisionUsoTarjeta = Fechas.getFecha(request.getParameter("fechaEmisionUsoTarjeta"));
				trabajoInforme.addParametro("FECHA_EMISION_USO_TARJETA", fechaEmisionUsoTarjeta);
			}
			catch (FechaException e) {
			}
		}

		// Fecha Recepcion Emision Tarjeta
		if (!request.getParameter("fechaRecepcionEmisionTarjeta").isEmpty()) {
			try {
				Date fechaRecepcionEmisionTarjeta = Fechas.getFecha(request.getParameter("fechaRecepcionEmisionTarjeta"));
				trabajoInforme.addParametro("FECHA_RECEPCION_EMISION_TARJETA", fechaRecepcionEmisionTarjeta);
			}
			catch (FechaException e) {
			}
		}

		// Forma de pago
		if (!request.getParameter("codFormaDePago").isEmpty()) {
			trabajoInforme.addParametro("COD_FORMA_PAGO", request.getParameter("codFormaDePago"));
			trabajoInforme.addParametro("DES_FORMA_PAGO", request.getParameter("desFormaDePago"));
		}

		// Tipo tarjeta
		if (!request.getParameter("codTipoTarjeta").isEmpty()) {
			trabajoInforme.addParametro("COD_TIPO_TARJETA", request.getParameter("codTipoTarjeta"));
			trabajoInforme.addParametro("DES_TIPO_TARJETA", request.getParameter("desTipoTarjeta"));
		}

		// Documento
		if (!request.getParameter("documento").isEmpty()) {
			trabajoInforme.addParametro("DOCUMENTO", request.getParameter("documento"));
		}

		// Factura desde
		if (!request.getParameter("facturaDesde").isEmpty()) {
			trabajoInforme.addParametro("FACTURA_DESDE", request.getParameter("facturaDesde"));
		}

		// Factura hasta
		if (!request.getParameter("facturaHasta").isEmpty()) {
			trabajoInforme.addParametro("FACTURA_HASTA", request.getParameter("facturaHasta"));
		}

		// Promociones
		if (!request.getParameter("codPromocion").isEmpty()) {
			trabajoInforme.addParametro("COD_PROMOCION", request.getParameter("codPromocion"));
			trabajoInforme.addParametro("DES_PROMOCION", request.getParameter("desPromocion"));
		}

		// Colectivos
		if (!request.getParameter("codColectivo").isEmpty()) {
			trabajoInforme.addParametro("COD_COLECTIVO", request.getParameter("codColectivo"));
			trabajoInforme.addParametro("DES_COLECTIVO", request.getParameter("desColectivo"));
		}

		// Etiquetas
		if (!request.getParameter("codEtiqueta").isEmpty()) {
			trabajoInforme.addParametro("COD_ETIQUETA", request.getParameter("codEtiqueta"));
			trabajoInforme.addParametro("DES_ETIQUETA", request.getParameter("desEtiqueta"));
		}

		// Campañas
		if (!request.getParameter("codCampania").isEmpty()) {
			trabajoInforme.addParametro("COD_CAMPANIA", request.getParameter("codCampania"));
			trabajoInforme.addParametro("DES_CAMPANIA", request.getParameter("desCampania"));
		}

	}

}
