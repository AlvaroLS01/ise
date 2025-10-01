package com.comerzzia.bricodepot.backoffice.admin.variables.acciones;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.comerzzia.core.model.variables.VariableBean;
import com.comerzzia.core.util.base.Estado;
import com.comerzzia.core.util.numeros.Numero;
import com.comerzzia.web.core.variables.acciones.LeerFormularioAccion;

public class CustomLeerFormularioAccion extends LeerFormularioAccion {

	/**
	 * Logger
	 */
	protected static Logger log = Logger.getLogger(CustomLeerFormularioAccion.class);


	@Override
	protected void leerVariablesTickets(Map<String, VariableBean> variables, HttpServletRequest request) {
		// Usa descuento en linea
		if (variables.containsKey("TICKETS.USA_DESCUENTO_EN_LINEA")) {
			String ticketsUsa = request.getParameter("ticketsUsa") == null ? "N" : "S";
			if (!ticketsUsa.equals(variables.get("TICKETS.USA_DESCUENTO_EN_LINEA").getValorDefecto())) {
				variables.get("TICKETS.USA_DESCUENTO_EN_LINEA").setValor(ticketsUsa);
				variables.get("TICKETS.USA_DESCUENTO_EN_LINEA").setEstadoBean(Estado.MODIFICADO);
			} else {
				variables.get("TICKETS.USA_DESCUENTO_EN_LINEA").setValor(null);
				variables.get("TICKETS.USA_DESCUENTO_EN_LINEA").setEstadoBean(Estado.BORRADO);
			}
		}

		// permite realizar la venta de artículos con precio 0
		if (variables.containsKey("TPV.PERMITIR_VENTA_PRECIO_CERO")) {
			String ticketsVenta0 = request.getParameter("ticketsVenta0") == null ? "N" : "S";
			if (!ticketsVenta0.equals(variables.get("TPV.PERMITIR_VENTA_PRECIO_CERO").getValorDefecto())) {
				variables.get("TPV.PERMITIR_VENTA_PRECIO_CERO").setValor(ticketsVenta0);
				variables.get("TPV.PERMITIR_VENTA_PRECIO_CERO").setEstadoBean(Estado.MODIFICADO);
			} else {
				variables.get("TPV.PERMITIR_VENTA_PRECIO_CERO").setValor(null);
				variables.get("TPV.PERMITIR_VENTA_PRECIO_CERO").setEstadoBean(Estado.BORRADO);
			}
		}

		// permitir devolver mas cantidad del total de la devolucion
		if (variables.containsKey("CAJA.REINTENTOS_CIERRE")) {
			String numReintentoCerrarCajaDescuadres = request.getParameter("numReintentoCerrarCajaDescuadres");
			if (!numReintentoCerrarCajaDescuadres.equals(variables.get("CAJA.REINTENTOS_CIERRE").getValorDefecto())) {
				variables.get("CAJA.REINTENTOS_CIERRE").setValor(numReintentoCerrarCajaDescuadres);
				variables.get("CAJA.REINTENTOS_CIERRE").setEstadoBean(Estado.MODIFICADO);
			} else {
				variables.get("CAJA.REINTENTOS_CIERRE").setValor(null);
				variables.get("CAJA.REINTENTOS_CIERRE").setEstadoBean(Estado.BORRADO);
			}
		}

		if (variables.containsKey("CAJA.IMPORTE_MAXIMO_DESCUADRE")) {
			String numImporteMaximoDescuadre = request.getParameter("numImporteMaximoDescuadre").replace(",", ".");
			if (!numImporteMaximoDescuadre.equals(variables.get("CAJA.IMPORTE_MAXIMO_DESCUADRE").getValorDefecto())) {
				variables.get("CAJA.IMPORTE_MAXIMO_DESCUADRE").setValor(numImporteMaximoDescuadre);
				variables.get("CAJA.IMPORTE_MAXIMO_DESCUADRE").setEstadoBean(Estado.MODIFICADO);
			} else {
				variables.get("CAJA.IMPORTE_MAXIMO_DESCUADRE").setValor(null);
				variables.get("CAJA.IMPORTE_MAXIMO_DESCUADRE").setEstadoBean(Estado.BORRADO);
			}
		}

		if (variables.containsKey("CAJA.CIERRE_CAJA_DIARIO_OBLIGATORIO")) {
			String cajaCierreDiarioObligatorio = request.getParameter("cajaCierreDiarioObligatorio") == null ? "N"
					: "S";
			if (!cajaCierreDiarioObligatorio
					.equals(variables.get("CAJA.CIERRE_CAJA_DIARIO_OBLIGATORIO").getValorDefecto())) {
				variables.get("CAJA.CIERRE_CAJA_DIARIO_OBLIGATORIO").setValor(cajaCierreDiarioObligatorio);
				variables.get("CAJA.CIERRE_CAJA_DIARIO_OBLIGATORIO").setEstadoBean(Estado.MODIFICADO);
			} else {
				variables.get("CAJA.CIERRE_CAJA_DIARIO_OBLIGATORIO").setValor(null);
				variables.get("CAJA.CIERRE_CAJA_DIARIO_OBLIGATORIO").setEstadoBean(Estado.BORRADO);
			}
		}

		if (variables.containsKey("CAJA.APERTURA_AUTOMATICA")) {
			String cajaAperturaAutomatica = request.getParameter("cajaAperturaAutomatica") == null ? "N" : "S";
			if (!cajaAperturaAutomatica.equals(variables.get("CAJA.APERTURA_AUTOMATICA").getValorDefecto())) {
				variables.get("CAJA.APERTURA_AUTOMATICA").setValor(cajaAperturaAutomatica);
				variables.get("CAJA.APERTURA_AUTOMATICA").setEstadoBean(Estado.MODIFICADO);
			} else {
				variables.get("CAJA.APERTURA_AUTOMATICA").setValor(null);
				variables.get("CAJA.APERTURA_AUTOMATICA").setEstadoBean(Estado.BORRADO);
			}
		}

		if (variables.containsKey("TICKETS.PERMITE_CAMBIO_PRECIO")) {
			String ticketsPermiteCambioPrecio = request.getParameter("ticketsPermiteCambioPrecio") == null ? "N" : "S";
			if (!ticketsPermiteCambioPrecio.equals(variables.get("TICKETS.PERMITE_CAMBIO_PRECIO").getValorDefecto())) {
				variables.get("TICKETS.PERMITE_CAMBIO_PRECIO").setValor(ticketsPermiteCambioPrecio);
				variables.get("TICKETS.PERMITE_CAMBIO_PRECIO").setEstadoBean(Estado.MODIFICADO);
			} else {
				variables.get("TICKETS.PERMITE_CAMBIO_PRECIO").setValor(null);
				variables.get("TICKETS.PERMITE_CAMBIO_PRECIO").setEstadoBean(Estado.BORRADO);
			}
		}

		if (variables.containsKey("TPV.COLUMNA_VENDEDOR_VISIBLE")) {
			String tpvColumnaVendedorVisible = request.getParameter("tpvColumnaVendedorVisible") == null ? "N" : "S";
			if (!tpvColumnaVendedorVisible.equals(variables.get("TPV.COLUMNA_VENDEDOR_VISIBLE").getValorDefecto())) {
				variables.get("TPV.COLUMNA_VENDEDOR_VISIBLE").setValor(tpvColumnaVendedorVisible);
				variables.get("TPV.COLUMNA_VENDEDOR_VISIBLE").setEstadoBean(Estado.MODIFICADO);
			} else {
				variables.get("TPV.COLUMNA_VENDEDOR_VISIBLE").setValor(null);
				variables.get("TPV.COLUMNA_VENDEDOR_VISIBLE").setEstadoBean(Estado.BORRADO);
			}
		}

		if (variables.containsKey("PROMOCIONES.APLICACION_PREVIA_DE_PROMOCION_PRECIO")) {
			String tpvPromoPrecioAplicacionPrevia = request.getParameter("tpvPromoPrecioAplicacionPrevia") == null ? "N"
					: "S";
			if (!tpvPromoPrecioAplicacionPrevia
					.equals(variables.get("PROMOCIONES.APLICACION_PREVIA_DE_PROMOCION_PRECIO").getValorDefecto())) {
				variables.get("PROMOCIONES.APLICACION_PREVIA_DE_PROMOCION_PRECIO")
						.setValor(tpvPromoPrecioAplicacionPrevia);
				variables.get("PROMOCIONES.APLICACION_PREVIA_DE_PROMOCION_PRECIO").setEstadoBean(Estado.MODIFICADO);
			} else {
				variables.get("PROMOCIONES.APLICACION_PREVIA_DE_PROMOCION_PRECIO").setValor(null);
				variables.get("PROMOCIONES.APLICACION_PREVIA_DE_PROMOCION_PRECIO").setEstadoBean(Estado.BORRADO);
			}
		}

		if (variables.containsKey("TPV.TRATAR_PROMOCIONES_MENOS_INGRESO")) {
			String tpvPromoPrecioAplicacionPrevia = request.getParameter("ticketsPromoMenosIngresos") == null ? "N"
					: "S";
			if (!tpvPromoPrecioAplicacionPrevia
					.equals(variables.get("TPV.TRATAR_PROMOCIONES_MENOS_INGRESO").getValorDefecto())) {
				variables.get("TPV.TRATAR_PROMOCIONES_MENOS_INGRESO").setValor(tpvPromoPrecioAplicacionPrevia);
				variables.get("TPV.TRATAR_PROMOCIONES_MENOS_INGRESO").setEstadoBean(Estado.MODIFICADO);
			} else {
				variables.get("TPV.TRATAR_PROMOCIONES_MENOS_INGRESO").setValor(null);
				variables.get("TPV.TRATAR_PROMOCIONES_MENOS_INGRESO").setEstadoBean(Estado.BORRADO);
			}
		}

		if (variables.containsKey("CONTADORES.CARACTER_SEPARADOR")) {
			String tpvCaracterSeparador = request.getParameter("caracterSeparador");
			if (!tpvCaracterSeparador.equals(variables.get("CONTADORES.CARACTER_SEPARADOR").getValorDefecto())) {
				variables.get("CONTADORES.CARACTER_SEPARADOR").setValor(tpvCaracterSeparador);
				variables.get("CONTADORES.CARACTER_SEPARADOR").setEstadoBean(Estado.MODIFICADO);
			} else {
				variables.get("CONTADORES.CARACTER_SEPARADOR").setValor(null);
				variables.get("CONTADORES.CARACTER_SEPARADOR").setEstadoBean(Estado.BORRADO);
			}
		}

		if (variables.containsKey("ARTICULOS.CONTROL_SURTIDO")) {
			String tpvControlSurtido = request.getParameter("controlSurtido") == null ? "N" : "S";
			if (!tpvControlSurtido.equals(variables.get("ARTICULOS.CONTROL_SURTIDO").getValorDefecto())) {
				variables.get("ARTICULOS.CONTROL_SURTIDO").setValor(tpvControlSurtido);
				variables.get("ARTICULOS.CONTROL_SURTIDO").setEstadoBean(Estado.MODIFICADO);
			} else {
				variables.get("ARTICULOS.CONTROL_SURTIDO").setValor(null);
				variables.get("ARTICULOS.CONTROL_SURTIDO").setEstadoBean(Estado.BORRADO);
			}
		}

		if (variables.containsKey("CAJA.IMPORTE_AVISO_RETIRADA")) {
			String tpvImporteAvisoRetirada = request.getParameter("importeAvisoRetirada");
			if (!tpvImporteAvisoRetirada.equals(variables.get("CAJA.IMPORTE_AVISO_RETIRADA").getValorDefecto())) {
				variables.get("CAJA.IMPORTE_AVISO_RETIRADA")
						.setValor(Numero.desformatea(tpvImporteAvisoRetirada, 0.0).toString());
				variables.get("CAJA.IMPORTE_AVISO_RETIRADA").setEstadoBean(Estado.MODIFICADO);
			} else {
				variables.get("CAJA.IMPORTE_AVISO_RETIRADA").setValor(null);
				variables.get("CAJA.IMPORTE_AVISO_RETIRADA").setEstadoBean(Estado.BORRADO);
			}
		}

		if (variables.containsKey("CAJA.IMPORTE_BLOQUEO_RETIRADA")) {
			String tpvImporteBloqueoRetirada = request.getParameter("importeBloqueoRetirada");
			if (!tpvImporteBloqueoRetirada.equals(variables.get("CAJA.IMPORTE_BLOQUEO_RETIRADA").getValorDefecto())) {
				variables.get("CAJA.IMPORTE_BLOQUEO_RETIRADA")
						.setValor(Numero.desformatea(tpvImporteBloqueoRetirada, 0.0).toString());
				variables.get("CAJA.IMPORTE_BLOQUEO_RETIRADA").setEstadoBean(Estado.MODIFICADO);
			} else {
				variables.get("CAJA.IMPORTE_BLOQUEO_RETIRADA").setValor(null);
				variables.get("CAJA.IMPORTE_BLOQUEO_RETIRADA").setEstadoBean(Estado.BORRADO);
			}
		}
		
		if (variables.containsKey("TPV.MAX_DIAS_PERMITIDOS_DEVOLUCION")) {
			String tpvMaxDiasPermitidosDevolucion = request.getParameter("numMaximoDiasDevolucion");
			if (!tpvMaxDiasPermitidosDevolucion.equals(variables.get("TPV.MAX_DIAS_PERMITIDOS_DEVOLUCION").getValorDefecto())) {
				variables.get("TPV.MAX_DIAS_PERMITIDOS_DEVOLUCION")
						.setValor(tpvMaxDiasPermitidosDevolucion);
				variables.get("TPV.MAX_DIAS_PERMITIDOS_DEVOLUCION").setEstadoBean(Estado.MODIFICADO);
			} else {
				variables.get("TPV.MAX_DIAS_PERMITIDOS_DEVOLUCION").setValor(null);
				variables.get("TPV.MAX_DIAS_PERMITIDOS_DEVOLUCION").setEstadoBean(Estado.BORRADO);
			}
		}
	}

}
