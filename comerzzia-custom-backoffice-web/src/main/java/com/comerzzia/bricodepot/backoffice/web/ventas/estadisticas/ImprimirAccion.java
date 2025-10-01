package com.comerzzia.bricodepot.backoffice.web.ventas.estadisticas;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.util.StringUtils;

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
	public Vista vistaInforme(HttpServletRequest request) {
		Date hoy = new Date();
		Date semanaPasada = Fechas.sumaDias(hoy, -7);

		request.setAttribute("fechaDesde", semanaPasada);
		request.setAttribute("fechaHasta", hoy);

		return new Vista("backoffice/devoluciones/estadisticas/buscar/jsp/informe.jsp", Vista.INTERNA);
	}

	@Override
	public void imprimirInforme(HttpServletRequest request, TrabajoInformeBean trabajoInforme) throws InformeException {
		
		HttpSession sesion = request.getSession();
		DatosSesionBean datosSesion = (DatosSesionBean) sesion.getAttribute(WebKeys.DATOS_SESION);

		trabajoInforme.addParametro("UID_ACTIVIDAD", datosSesion.getUidActividad());
		//ALMACEN
		if (!request.getParameter("codAlmacenDesde").isEmpty()) {
			trabajoInforme.addParametro("CODALMACEN_DESDE", request.getParameter("codAlmacenDesde"));
		}
		if (!request.getParameter("codAlmacenHasta").isEmpty()) {
			trabajoInforme.addParametro("CODALMACEN_HASTA", request.getParameter("codAlmacenHasta"));
		}
		if (!request.getParameter("desAlmacenDesde").isEmpty()) {
			trabajoInforme.addParametro("DESALMACEN_DESDE", request.getParameter("desAlmacenDesde"));
		}
		if (!request.getParameter("desAlmacenHasta").isEmpty()) {
			trabajoInforme.addParametro("DESALMACEN_HASTA", request.getParameter("desAlmacenHasta"));
		}
		
		// Artículos
		if (request.getParameter("codArtDesde").isEmpty()) {
			//trabajoInforme.addParametro("CODART_DESDE", "0");
		} else {
			trabajoInforme.addParametro("CODART_DESDE", request.getParameter("codArtDesde"));
		}
		if (request.getParameter("codArtHasta").isEmpty()) {
			//trabajoInforme.addParametro("CODART_HASTA", "ZZZZZ");
		} else {
			trabajoInforme.addParametro("CODART_HASTA", request.getParameter("codArtHasta"));
		}

		// Fechas
		Date hoy = new Date();
		try {

			if (request.getParameter("fechaDesde").isEmpty()) {
				//trabajoInforme.addParametro("FECHA_DESDE", Fechas.sumaDias(hoy, -7));
			} else {
				Date fechaDesde = Fechas.getFecha(request.getParameter("fechaDesde"));
				trabajoInforme.addParametro("FECHA_DESDE", Fechas.toSqlTimestamp(fechaDesde));
			}
			if (request.getParameter("fechaHasta").isEmpty()) {
				//trabajoInforme.addParametro("FECHA_HASTA", hoy);
			} else {
				Date fechaHasta = Fechas.getFecha(request.getParameter("fechaHasta"));
				trabajoInforme.addParametro("FECHA_HASTA", Fechas.toSqlTimestamp(fechaHasta));
			}
		} catch (FechaException e) {
		}

		// Familias
		if (request.getParameter("codFamDesde").isEmpty()) {
			//trabajoInforme.addParametro("CODFAM_DESDE", "0");
		} else {
			trabajoInforme.addParametro("CODFAM_DESDE", request.getParameter("codFamDesde"));
		}
		if (request.getParameter("codFamHasta").isEmpty()) {
			//trabajoInforme.addParametro("CODFAM_HASTA", "ZZZZZ");
		} else {
			trabajoInforme.addParametro("CODFAM_HASTA", request.getParameter("codFamHasta"));
		}

		// Motivos
		if (request.getParameter("codMot").isEmpty()) {
			//trabajoInforme.addParametro("CODMOTIVO_DESDE", "0");
			//trabajoInforme.addParametro("CODMOTIVO_HASTA", "ZZZZZ");
		} else {
			trabajoInforme.addParametro("CODMOTIVO_DESDE", request.getParameter("desMot"));
			trabajoInforme.addParametro("CODMOTIVO_HASTA", request.getParameter("desMot"));
		}

		// Usuario / supervisor
		if (request.getParameter("idUsuario").isEmpty()) {
			//trabajoInforme.addParametro("IDUSUARIO_DESDE", "0");
			//trabajoInforme.addParametro("IDUSUARIO_HASTA", "ZZZZZ");
		} else {
			trabajoInforme.addParametro("IDUSUARIO_DESDE", request.getParameter("idUsuario"));
			trabajoInforme.addParametro("IDUSUARIO_HASTA", request.getParameter("idUsuario"));
		}

		// Medios de pago
		if (request.getParameter("codmedpag").isEmpty()) {
			//trabajoInforme.addParametro("CODPAGO_DESDE", "0");
			//trabajoInforme.addParametro("CODPAGO_HASTA", "ZZZZZ");
		} else {
			trabajoInforme.addParametro("CODPAGO_DESDE", request.getParameter("codmedpag"));
			trabajoInforme.addParametro("CODPAGO_HASTA", request.getParameter("codmedpag"));
		}
		
		// Id del ticket
		if(request.getParameter("codticket").isEmpty()) {
			//trabajoInforme.addParametro("IDTICKET_DESDE", "0");
			//trabajoInforme.addParametro("IDTICKET_HASTA", "ZZZZZ");
		}else {
			trabajoInforme.addParametro("IDTICKET_DESDE", request.getParameter("codticket"));
			trabajoInforme.addParametro("IDTICKET_HASTA", request.getParameter("codticket"));
		}
	}

	@Override
	public String getNombreInforme() {
		return "devoluciones.lstMotivos";
	}

}
