package com.comerzzia.iskaypet.api.omnichannel.api.services.promociones.tipos.especificos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.comerzzia.core.basketcalculator.util.xml.XMLDocument;
import com.comerzzia.core.basketcalculator.util.xml.XMLDocumentException;
import com.comerzzia.core.basketcalculator.util.xml.XMLDocumentNode;
import com.comerzzia.pos.services.core.variables.VariablesServices;
import com.comerzzia.pos.services.promociones.DocumentoPromocionable;
import com.comerzzia.pos.services.promociones.LineaDocumentoPromocionable;
import com.comerzzia.pos.services.promociones.filtro.FiltroLineasPromocion;
import com.comerzzia.pos.services.promociones.filtro.LineasAplicablesPromoBean;
import com.comerzzia.pos.services.promociones.tipos.componente.CondicionPrincipalPromoBean;
import com.comerzzia.pos.services.promociones.tipos.especificos.PromocionPuntosBean;
import com.comerzzia.pos.services.ticket.lineas.LineaTicket;
import com.comerzzia.pos.services.ticket.promociones.IPromocionTicket;
import com.comerzzia.pos.services.ticket.promociones.PromocionTicket;
import com.comerzzia.pos.util.bigdecimal.BigDecimalUtil;

@Component()
@Scope("prototype")
public class IskaypetPromocionPuntosBean extends PromocionPuntosBean {

	private BigDecimal porcentajePuntos;

	@Autowired
	protected VariablesServices servicioVariables;

	public static final String VARIABLE_APLICA_PUNTOS = "POS.APLICA_REDONDEO_PUNTOS";

	@Override
	public boolean aplicarPromocion(DocumentoPromocionable<IPromocionTicket> documento) {
		log.trace("aplicarPromocion() - " + this);
		boolean esVenta = comprobarSignoLineas(documento.getLineasDocumentoPromocionable());

		if (porcentajePuntos != null) {
			puntosEuros = porcentajePuntos;
		}

		if (puntosEuros == null || puntosEuros.compareTo(BigDecimal.ZERO) == 0) {
			log.trace(this + " aplicarPromocion() - La promoción no se puede aplicar porque los puntos por euros configurados no lo permiten: " + puntosEuros);
			return false;
		}
		// Obtenemos las líneas aplicables según el filtro configurado
		FiltroLineasPromocion filtroLineas = createFiltroLineasPromocion(documento);
		List<LineaDocumentoPromocionable> lineasDocumentoPromocionable = documento.getLineasDocumentoPromocionable();
		filtroLineas.setFiltrarPromoExclusivas(false); // Da igual que las líneas tengan una promoción exclusiva
		LineasAplicablesPromoBean lineasAplicables = filtroLineas.getNumCombosCondicion(condiciones);
		if (lineasAplicables.isEmpty()) {
			log.debug(this + " aplicarPromocion() - La promoción no se puede aplicar por no existir líneas aplicables en el documento .");
			return false;
		}

		int puntosTotales = 0;
		lineasAplicables.setLineasAplicables(lineasDocumentoPromocionable);
		for (LineaDocumentoPromocionable linea : lineasAplicables.getLineasAplicables()) {
			LineaTicket lt = (LineaTicket) linea;

			BigDecimal precioUnitario = lt.getPrecioTotalSinDto();

			BigDecimal cantidad = lt.getCantidad();

			BigDecimal importeLinea = precioUnitario.multiply(cantidad);

			BigDecimal puntosLinea = importeLinea.setScale(0, esVenta ? RoundingMode.CEILING : RoundingMode.FLOOR);

			puntosTotales += puntosLinea.intValue();
		}

		if (puntosTotales == 0) {
			log.trace(this + " aplicarPromocion() - La promoción no se puede aplicar porque no se obtuvieron puntos en ninguna línea.");
			return false;
		}

		// Aplicamos la promoción sobre el documento usando el total de puntos calculado
		PromocionTicket promocionTicket = createPromocionTicket(customerCoupon);
		promocionTicket.setPuntos(puntosTotales);
		documento.addPromocion(promocionTicket);

		// Se aplica la promoción a las líneas y se suma el total de puntos obtenidos
		lineasAplicables.aplicaPromocionPuntos(promocionTicket, puntosEuros);
		documento.addPuntos(puntosTotales);

		return true;
	}

	private boolean comprobarSignoLineas(List<LineaDocumentoPromocionable> lineasDocumentoPromocionable) {
		boolean venta = true;
		int contadorLineasNegativas = 0;
		for (LineaDocumentoPromocionable linea : lineasDocumentoPromocionable) {
			if (BigDecimalUtil.isMenorACero(((LineaTicket) linea).getPrecioConDto()) || BigDecimalUtil.isMenorACero(((LineaTicket) linea).getPrecioTotalConDto())) {
				contadorLineasNegativas++;
			}
		}
		
		if (contadorLineasNegativas > 0) {
			venta = false;
		}
		
		return venta;
	}

	private BigDecimal redondeoPuntosPromocion(boolean esVenta, BigDecimal importeAplicable) {
		BigDecimal puntos;

		if (porcentajePuntos != null) {
			puntos = (importeAplicable.multiply(puntosEuros)).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);

			// En caso necesario se realiza el redondeo al alza de los puntos calculados
			if (isAplicaProcesoRedondeoPuntos()) {
				if (esVenta) {

					puntos = puntos.setScale(0, RoundingMode.CEILING);
				}
				else {
					puntos = puntos.abs().setScale(0, RoundingMode.FLOOR);
				}
			}
		}
		else {
			if (esVenta) {
				puntos = importeAplicable.divide(puntosEuros, 0, RoundingMode.CEILING);
			}
			else {
				puntos = importeAplicable.divide(puntosEuros, 0, RoundingMode.FLOOR);
			}
		}
		return puntos;
	}

	public Boolean isAplicaProcesoRedondeoPuntos() {
		log.debug("getVariableProcesoRedondeoPuntos() - GAP45.1.- Variable para control de proceso de redondeo de puntos....");
		return servicioVariables.getVariableAsBoolean(VARIABLE_APLICA_PUNTOS);
	}

	@Override
	public void leerDatosPromocion(byte[] datosPromocion) {
		porcentajePuntos = null;
		puntosEuros = null;
		try {
			XMLDocument xmlPromocion = new XMLDocument(datosPromocion);
			condiciones = new CondicionPrincipalPromoBean(xmlPromocion.getNodo("condicionLineas"));
			XMLDocumentNode porcentajePuntosNodo = xmlPromocion.getNodo("porcentajePuntos", true);
			XMLDocumentNode puntosEurosNodo = xmlPromocion.getNodo("puntosEuros", true);

			if (puntosEurosNodo != null && !puntosEurosNodo.getValue().isEmpty() && BigDecimalUtil.isMayorACero(puntosEurosNodo.getValueAsBigDecimal())) {
				puntosEuros = (puntosEurosNodo.getValueAsBigDecimal());
			}

			if (porcentajePuntosNodo != null && !porcentajePuntosNodo.getValue().isEmpty() && BigDecimalUtil.isMayorACero(xmlPromocion.getNodo("porcentajePuntos").getValueAsBigDecimal())) {
				porcentajePuntos = xmlPromocion.getNodo("porcentajePuntos").getValueAsBigDecimal();
			}

			String storeLanguageCode = getStoreLanguageCode();

			String textPromo = null;
			String textPromoDefault = null;
			List<XMLDocumentNode> textPromoNodes = xmlPromocion.getNodos("textoPromocion");
			for (XMLDocumentNode textPromoNode : textPromoNodes) {
				String textPromoLanguageCode = textPromoNode.getAtributoValue("lang", true);
				if (StringUtils.isNotBlank(textPromoLanguageCode)) {
					if (textPromoLanguageCode.equals(storeLanguageCode)) {
						textPromo = textPromoNode.getValue();
						break;
					}
				}
				else {
					textPromoDefault = textPromoNode.getValue();
				}
			}

			if (StringUtils.isBlank(textPromo)) {
				textPromo = textPromoDefault;
			}

			setTextoPromocion(textPromo);

		}
		catch (XMLDocumentException e) {
			log.error("Error al leer los datos de la promoción de tipo puntos: " + e.getMessage(), e);
		}
	}
}
