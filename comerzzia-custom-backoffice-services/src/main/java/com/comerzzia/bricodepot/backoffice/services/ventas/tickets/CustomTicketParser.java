package com.comerzzia.bricodepot.backoffice.services.ventas.tickets;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.comerzzia.bricodepot.backoffice.persistence.albaranes.venta.informepromocion.InformePromociones;
import com.comerzzia.bricodepot.backoffice.persistence.albaranes.venta.presupuestos.PresupuestoVenta;
import com.comerzzia.bricodepot.backoffice.persistence.devoluciones.informe.InformeDevolucion;
import com.comerzzia.bricodepot.backoffice.persistence.devoluciones.maxDias.DiasDevolucion;
import com.comerzzia.bricodepot.backoffice.persistence.devoluciones.maxDias.DiasDevolucionKey;
import com.comerzzia.bricodepot.backoffice.persistence.devoluciones.maxDias.DiasDevolucionMapper;
import com.comerzzia.bricodepot.backoffice.persistence.motivos.Motivos;
import com.comerzzia.bricodepot.backoffice.persistence.motivos.MotivosMapper;
import com.comerzzia.bricodepot.backoffice.persistence.motivos.datos.MotivosDatos;
import com.comerzzia.bricodepot.backoffice.persistence.tickets.tipoImpresion.TicketTipoImpresion;
import com.comerzzia.bricodepot.backoffice.services.albaranes.ventas.cambioprecio.CambioPreciosAlbaranesServiceImpl;
import com.comerzzia.bricodepot.backoffice.services.motivos.MotivoException;
import com.comerzzia.core.model.ventas.tickets.TicketBean;
import com.comerzzia.core.omnichannel.engine.util.BigDecimalUtil;
import com.comerzzia.core.servicios.contadores.ServicioContadoresImpl;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.servicios.variables.ServicioVariablesImpl;
import com.comerzzia.core.servicios.ventas.tickets.ServicioTicketsImpl;
import com.comerzzia.core.servicios.ventas.tickets.TicketException;
import com.comerzzia.core.servicios.ventas.tickets.TicketNotFoundException;
import com.comerzzia.core.servicios.ventas.tickets.TicketOrigenNotFoundException;
import com.comerzzia.core.util.base.Estado;
import com.comerzzia.core.util.db.Connection;
import com.comerzzia.core.util.numeros.Numero;
import com.comerzzia.core.util.xml.XMLDocumentException;
import com.comerzzia.core.util.xml.XMLDocumentUtils;
import com.comerzzia.model.general.marketing.acciones.CampAccMarketingBean;
import com.comerzzia.model.general.mediospago.vencimientos.VencimientoBean;
import com.comerzzia.model.ventas.albaranes.AlbaranVentaBean;
import com.comerzzia.model.ventas.albaranes.articulos.ArticuloAlbaranVentaBean;
import com.comerzzia.model.ventas.albaranes.pagos.PagoAlbaranVentaBean;
import com.comerzzia.model.ventas.albaranes.pagos.PagoTarjetaRegaloBean;
import com.comerzzia.persistencia.general.mediospago.vencimientos.VencimientosDao;
import com.comerzzia.servicios.general.marketing.acciones.CampAccMarketingNotFoundException;
import com.comerzzia.servicios.general.marketing.acciones.ServicioCampAccMarketingImpl;
import com.comerzzia.servicios.general.marketing.acciones.enl.ServicioCampAccEnlMarketingImpl;
import com.comerzzia.servicios.ventas.albaranes.AlbaranVenta;
import com.comerzzia.servicios.ventas.albaranes.AlbaranVentaException;
import com.comerzzia.servicios.ventas.albaranes.AlbaranVentaNotFoundException;
import com.comerzzia.servicios.ventas.albaranes.ServicioAlbaranesVentasImpl;
import com.comerzzia.servicios.ventas.tickets.TicketParser;

@SuppressWarnings("deprecation")
public class CustomTicketParser extends TicketParser {


	private Logger log = Logger.getLogger(CustomTicketParser.class);

	protected List<String> codigoMotivoDev = new ArrayList<String>();
//	protected List<String> codigosMotivos = new ArrayList<String>();
	
	protected List<InformeDevolucion> listaDetalleMotivos;
	protected List<MotivosDatos> listaMotivosDatos;	
	
	public static final String ID_CONTADOR = "ID_CLIE_ALBARAN";
	public static final String ID_VARIABLE = "TPV.MAX_DIAS_PERMITIDOS_DEVOLUCION";
	public static final String COD_MOTIVO_DEV = "COD_MOTIVO_DEV";

	public static final String XML_MOTIVO = "motivo";
	public static final String XML_DESCRIPCION = "descripcion";
	public static final String XML_COMENTARIO = "comentario";
	public static final String XML_UID_ACTIVIDAD = "uidActividad";
	public static final String XML_CODIGO_TIPO = "codigo_tipo";
	public static final String XML_PRECIO_ARTICULO_ORIGINAL = "precio_articulo_original";
	public static final String XML_PRECIO_ARTICULO_APLICADO = "precio_articulo_aplicado";
	public static final String XML_COD_ART = "codart";
	public static final String XML_CANTIDAD = "cantidad";

	public static final String UID_DEVOLUCION_FLEXPOINT = "UID_DEVOLUCION_FLEXPOINT";

	public static final String TAG_XML_HEADER_LOYAL_MAIL = "email_envio_ticket";
	
	public static final String USUARIO_SCO = "SELF_CHECKOUT";

	public static final String NUM_PRESUPUESTO = "numPresupuesto";

	public CustomTicketParser(Connection conn, DatosSesionBean datosSesion, TicketBean ticket, SqlSession sqlSession) {
		super(conn, datosSesion, ticket, sqlSession);
	}

	@Override
	public AlbaranVenta parse() throws TicketException, TicketOrigenNotFoundException {
		AlbaranVenta albaran = super.parse();
		
		DiasDevolucionMapper maximoDiasMapper = sqlSession.getMapper(DiasDevolucionMapper.class);
		if (!codigoMotivoDev.isEmpty()) { // Si está vacio es debido a que no es una devolución
			try {
				// GAP #1729: Aviso cuando se pase la fecha limite de devolución
				// Al ser una devolucion, se busca el registro de la venta a la que hace
				// referencia y se setea la fecha de devolucion
				DiasDevolucionKey key = new DiasDevolucionKey();
				key.setUidActividad(datosSesion.getUidActividad());
				key.setIdAlbaran(albaran.getBean().getCodAlbaranOrigen());
				DiasDevolucion maximoDias = maximoDiasMapper.selectByPrimaryKey(key);
				if (maximoDias != null) {
					maximoDias.setFechaDevolucion(LocalDate.now().toString());
					maximoDiasMapper.updateByPrimaryKey(maximoDias);
				}
				else {
					log.warn("parse() - No se han encontrado el albarán origen a la hora de calcular el máximo de días para su devolución");
				}

			}
			catch (Exception e) {
				log.error("Ha ocurrido un error. El error es: " + e.getMessage(), e);
			}
		}
		else { // Es una venta
			try {
				// GAP #1729: Aviso cuando se pase la fecha limite de devolución
				// Al ser una venta, se guarda el id del albaran de venta junto a su fecha
				// máxima de devolución
				// antes de que salte el aviso en el POS.

				DiasDevolucion maximoDias = new DiasDevolucion();
				String contador = ServicioVariablesImpl.get().consultarValor(conn, datosSesion, ID_VARIABLE);
				if (contador != null) {
					Long diasMaximosParaDevolucion = (Long.parseLong(contador));

					maximoDias.setFechaMaxDevolucion(LocalDate.now().plusDays(diasMaximosParaDevolucion).toString());
					maximoDias.setIdAlbaran(albaran.getCodAlbaran());
					maximoDias.setUidActividad(datosSesion.getUidActividad());
					if (maximoDiasMapper.updateByPrimaryKey(maximoDias) == 0) {
						maximoDiasMapper.insert(maximoDias);
					}
				}
			}
			catch (Exception e) {
				log.error("Ha ocurrido un error al guardar la fecha máxima de devolucion. El error es: " + e.getMessage(), e);
			}

		}
		
		addListaMotivosAlbaranExt(albaran);
		
		return albaran;
	}
	
	@Override
	protected ArticuloAlbaranVentaBean obtenerDetalleArticulo(Element linea) throws XMLDocumentException {
		if (linea.getElementsByTagName(XML_MOTIVO).getLength() != 0) {
			Motivos motivo = new Motivos();
			MotivosMapper mapper = sqlSession.getMapper(MotivosMapper.class);
			Element motivoTag = XMLDocumentUtils.getElement(linea, XML_MOTIVO, false);
			try {
				motivo.setUidActividad(XMLDocumentUtils.getTagValueAsString(motivoTag, XML_UID_ACTIVIDAD, false));
				motivo.setCodigo(String.valueOf(ServicioContadoresImpl.get().obtenerValorContador(datosSesion, COD_MOTIVO_DEV).intValue()));
				motivo.setDescripcion(XMLDocumentUtils.getTagValueAsString(motivoTag, XML_DESCRIPCION, false));
				motivo.setCodigoTipo(XMLDocumentUtils.getTagValueAsString(motivoTag, XML_CODIGO_TIPO, false));

				Integer idLinea = Integer.parseInt(XMLDocumentUtils.getAttribute(linea, "idlinea", false));
				
				String comentario = XMLDocumentUtils.getTagValueAsString(motivoTag, XML_COMENTARIO, true);
				if (StringUtils.isBlank(comentario)) {
					log.debug("La línea procesada contiene un comentario vacío. Se procede a su inserción con dicho comentario");
					// Se setea este comentario porque si no se guarda como NULL, y eso no es lo que
					// debe hacer. Los que tienen comentario=NULL son los creados en la interfaz web
					// y son los que se deben enviar a los diversos POS.
					comentario = "Motivo generado desde la Caja.";
				}
				motivo.setComentario(comentario);
				mapper.insert(motivo);

				rellenaDetalleMotivo(motivo.getCodigo(), idLinea);
				
				/* Para rellenar en la tabla x_motivos_datos_tbl */
				BigDecimal cantidad = new BigDecimal(XMLDocumentUtils.getTagValueAsDouble(linea, XML_CANTIDAD, false));
				String codArt = XMLDocumentUtils.getTagValueAsString(linea, XML_COD_ART, false);
				addMotivosDatos(motivo, motivoTag, idLinea, codArt, cantidad);
				
				if (motivo.getCodigoTipo().equals("1")) {
					codigoMotivoDev.add(motivo.getCodigo());
				}
			}
			catch (Exception ex) {
				log.error("obtenerDetalleArticulo() - Ha habido un error al parsear el motivo asociado: " + ex.getMessage(), ex);
			}
		}
		return super.obtenerDetalleArticulo(linea);
	}

	@Override
	protected void obtenerDetallesAlbaran(Element detalles) throws Exception {
		// Obtenemos los artículos del ticket
		List<Element> lstElementos = XMLDocumentUtils.getChildElements(detalles);

		Double totalADevolver = 0D;

		for (int i = 0; i < lstElementos.size(); i++) {
			Element linea = lstElementos.get(i);

			ArticuloAlbaranVentaBean articulo = obtenerDetalleArticulo(linea);

			// Si la línea tiene promoción la tratamos
			Element promociones = XMLDocumentUtils.getElement(linea, "promociones", true);
			if (promociones != null) {
				Element promocion = XMLDocumentUtils.getElement(promociones, "promocion", true);
				if (promocion != null) {
					articulo.setIdPromocion(XMLDocumentUtils.getTagValueAsLong(promocion, "idpromocion", false)); 
					articulo.setDescripcionAmpliada(XMLDocumentUtils.getTagValueAsString(promocion, "texto_promocion", true));
				}

				Map<String, Double> mapPrecios = new HashMap<String, Double>();
				mapPrecios.put("precioEntrada", Double.valueOf(0));
				mapPrecios.put("precioSalida", Double.valueOf(0));
				obtenerDetallePromociones(linea, promociones, mapPrecios);
			}
			else {
				Element promocion = XMLDocumentUtils.getElement(linea, "promocion", true);
				if (promocion != null) {
					articulo.setIdPromocion(XMLDocumentUtils.getTagValueAsLong(promocion, "idpromocion", false));
					Double puntosAcumulados = XMLDocumentUtils.getTagValueAsDouble(promocion, "puntos", false);
					articulo.setDescripcionAmpliada(XMLDocumentUtils.getTagValueAsString(promocion, "texto_promocion", true));
					articulo.setPuntos(puntosAcumulados);

					// Si tenemos descuento en línea añadimos una nueva línea de tipo PROMOCION
					Element descuentoEnLinea = XMLDocumentUtils.getElement(promocion, "descuento_en_linea", true);
					if (descuentoEnLinea != null) {
						double cantidad = XMLDocumentUtils.getTagValueAsDouble(promocion, "cantidad_promocion", false);
						double importe = XMLDocumentUtils.getTagValueAsDouble(descuentoEnLinea, "importe_promocion", false);
						double importeTotal = XMLDocumentUtils.getTagValueAsDouble(descuentoEnLinea, "importe_total_promocion", false);

						ArticuloAlbaranVentaBean articuloPromocion = crearDetalleArticuloBean();
						articuloPromocion.setEstadoBean(Estado.NUEVO);
						articuloPromocion.setTipoLinea(ArticuloAlbaranVentaBean.LINEA_PROMOCION);
						articuloPromocion.setCodArticulo(articulo.getCodArticulo());
						articuloPromocion.setDesglose1(articulo.getDesglose1());
						articuloPromocion.setDesglose2(articulo.getDesglose2());
						articuloPromocion.setDesArticulo(articulo.getDesArticulo());
						articuloPromocion.setCantidad(cantidad);
						articuloPromocion.setCantidadMedida(new Double(0));
						if (cantidad != 0) {
							articuloPromocion.setPrecio((-1) * Numero.redondea(importe / cantidad, 4));
							articuloPromocion.setPrecioTotal((-1) * Numero.redondea(importeTotal / cantidad, 2));
						}
						else {
							articuloPromocion.setPrecio(new Double(0));
							articuloPromocion.setPrecioTotal(new Double(0));
						}
						articuloPromocion.setImporte((-1) * importe);
						articuloPromocion.setImporteTotal((-1) * importeTotal);
						articuloPromocion.setCodImpuesto(articulo.getCodImpuesto());
						articuloPromocion.setDescuento(new Double(0));
						articuloPromocion.setPrecioCosto(articulo.getPrecioCosto());

						// añadir idPromocion a la linea que define el descuento de la promoción
						articuloPromocion.setIdPromocion(XMLDocumentUtils.getTagValueAsLong(promocion, "idpromocion", false));
						// Añadimos la linea de promoción al albarán
						albaran.getArticulos().add(articuloPromocion);
					}
				}
				else if (articulo.getDescuento() != 0) { // Entrará tanto en POS Java como TPV Delphi cuando tengo
				                                         // descuento manual sin promociones
					// TODO-REE: Cuando se traten promociones en POS Java para registrar lista de descuentos/promociones
					// de cada línea, el descuento manual tendría que ser un descuento más igual que las promociones.
					articulo.setDescripcionAmpliada("Se ha aplicado un descuento del " + articulo.getDescuento() + " %");
				}

			}

			// Comprobamos si la línea tiene un cajero auxiliar
			Element cajeroAuxiliar = XMLDocumentUtils.getElement(linea, "vendedor", true);
			if (cajeroAuxiliar != null) {
				articulo.setCajeroAuxiliar(XMLDocumentUtils.getTagValueAsString(cajeroAuxiliar, "usuario", false));
			}

			// Comprobamos si tiene puntos a devolver
			Double puntosDevueltos = XMLDocumentUtils.getTagValueAsDouble(linea, "puntos_a_devolver", true);
			if (puntosDevueltos != null) {
				totalADevolver += puntosDevueltos;
			}
			tratarPromocionesDescuentoManualCuponesPorLinea(linea, articulo);
			// Añadimos el artículo al albarán
			//albaran.getArticulos().add(articulo);
		}

		if (totalADevolver != 0) {
			albaran.setPuntosADevolver(totalADevolver);
		}
	}
	
	protected void tratarPromocionesDescuentoManualCuponesPorLinea(Element linea, ArticuloAlbaranVentaBean articulo) throws Exception {
		
		CambioPreciosAlbaranesServiceImpl.get().tratarPromocionesDescuentoManualCupones(datosSesion, sqlSession, linea, articulo, albaran,ticket);
		albaran.getArticulos().add(articulo);
	}

	protected void obtenerPagosAlbaran(Element pagos) throws Exception {
		List<Element> lstElementos = XMLDocumentUtils.getChildElements(pagos);

		for (int i = 0; i < lstElementos.size(); i++) {
			Element linea = lstElementos.get(i);

			// Si es un nodo de pago o de entrega a cuenta
			if (linea.getNodeName().equals("pago") || linea.getNodeName().equals("entregaCuenta")) {
				log.debug("obtenerPagosAlbaran() - NODO " + linea.getNodeName());
				// Obtenemos el primer vencimiento de la forma del pago del ticket
				String codMedioPago = XMLDocumentUtils.getTagValueAsString(linea, "codmedpag", false);
				log.debug("obtenerPagosAlbaran() - Codigo medio de pago a tratar " + codMedioPago);
				
				List<VencimientoBean> vencimientos = VencimientosDao.consultar(conn, datosSesion.getConfigEmpresa(), codMedioPago);
				if (vencimientos.size() == 0) {
					throw new TicketException("No se han definido vencimientos para el medio de pago " + codMedioPago);
				}

				PagoAlbaranVentaBean pago = crearPagoAlbaranVentaBean();
				pago.setEstadoBean(Estado.NUEVO);
				pago.setLinea(i);
				pago.setIdMedioPagoVencimiento(vencimientos.get(0).getIdMedioPagoVencimiento());
				pago.setImporte(XMLDocumentUtils.getTagValueAsDouble(linea, "importe", false));
				pago.setNumeroOperacion(XMLDocumentUtils.getTagValueAsString(linea, "numero_operacion", true));
				// Obtenemos un pago con una giftcard
				Element datosGiftcard = XMLDocumentUtils.getElement(linea, "giftcards", true);
				if (datosGiftcard != null) {
					for (Element datoGiftcard : XMLDocumentUtils.getChildElements(datosGiftcard)) {
						String numeroTarjetaRegalo = (XMLDocumentUtils.getTagValueAsString(datoGiftcard, "numero_tarjeta", true));
						String uidTransaccionRegalo = (XMLDocumentUtils.getTagValueAsString(datoGiftcard, "uid_transaccion", true));
						if (numeroTarjetaRegalo == null || numeroTarjetaRegalo.isEmpty()) {
							throw new TicketException("Pago con giftcard asociada - No se encuentra nÃºmero de tarjeta " + numeroTarjetaRegalo);
						}
						if (uidTransaccionRegalo == null || uidTransaccionRegalo.isEmpty()) {
							throw new TicketException("Pago con giftcard asociada - No se encuentra uid de transacciÃ³n de tarjeta regalo " + numeroTarjetaRegalo);
						}

						PagoTarjetaRegaloBean pagoTarjetaRegalo = new PagoTarjetaRegaloBean();
						pagoTarjetaRegalo.setNumeroTarjetaRegalo(numeroTarjetaRegalo);
						pagoTarjetaRegalo.setUidTransaccionTarjetaRegalo(uidTransaccionRegalo);
						pagoTarjetaRegalo.setImporte(XMLDocumentUtils.getTagValueAsDouble(datoGiftcard, "importe_pago", false));
						pago.addPago(pagoTarjetaRegalo);
					}
				}
				Element extendedData = XMLDocumentUtils.getElement(linea, "extendedData", true);
				String numDocumento = XMLDocumentUtils.getTagValueAsString(extendedData, "documento", true);
				if (StringUtils.isNotBlank(numDocumento)) {
					log.debug("obtenerPagosAlbaran() - Documento: " + numDocumento);
					pago.putExtension("num_pedido", numDocumento);
				}
				albaran.addPago(pago);
			}
		}
	}

	@Override
	protected void obtenerCabeceraAlbaran(Element cabecera) throws Exception {
		super.obtenerCabeceraAlbaran(cabecera);

		if (ticket.getCodTipoDocumento().equalsIgnoreCase("NC") || ticket.getCodTipoDocumento().equalsIgnoreCase("FR")) {
			log.debug("obtenerCabeceraAlbaran() - Se trata de una devolucion: " + ticket.getCodTipoDocumento());

			getIdSupervisorDevolucion(cabecera);
		}

		getTipoImpresion(cabecera);
		obtenerNumPresupuesto(cabecera);
		tratarPromocionesInforme(cabecera, ticket, albaran);
	}

	private void getIdSupervisorDevolucion(Element cabecera) throws XMLDocumentException {
		log.debug("getIdSupervisorDevolucion() - Cabecera obtenida");
		Element eventosAuditoria = XMLDocumentUtils.getElement(cabecera, "eventos_auditoria", true);

		if (eventosAuditoria != null) {
			log.debug("getIdSupervisorDevolucion() - Eventos auditoria obtenidos");
			List<Element> lstElementos = XMLDocumentUtils.getChildElements(eventosAuditoria);

			if (lstElementos != null && !lstElementos.isEmpty()) {
				Element linea = lstElementos.get(0);

				Long idSupervisor = XMLDocumentUtils.getTagValueAsLong(linea, "id_supervisor", true);
				log.debug("getIdSupervisorDevolucion() - IdSupervisor: " + idSupervisor);

				albaran.putExtension("idSupervisorDevolucion", idSupervisor);
			}
		}
	}

	public String getNumAnticipo() { // Se recupera el número de anticipo.
		log.debug("getNumAnticipo()");
		try {
			String numAnticipo = null;
			Element root = ticket.getXml().getDocumentElement();
			NodeList node = root.getElementsByTagName("numAnticipo");
			if (node != null && node.getLength() > 0) {
				if (node.item(0) != null && node.item(0).getFirstChild() != null) {
					numAnticipo = node.item(0).getFirstChild().getNodeValue().toString();
				}
			}
			return numAnticipo;
		}
		catch (Exception e) {
			log.error("getNumAnticipo() - Número de anticipo no informado: " + e.getMessage());
			return null;
		}
	}

	public String getImporteAnticipo() { // Se recupera el importe de anticipo.
		log.debug("getImporteAnticipo()");
		try {
			String importeAnticipo = null;
			Element root = ticket.getXml().getDocumentElement();
			NodeList node = root.getElementsByTagName("importeAnticipo");
			if (node != null && node.getLength() > 0) {
				if (node.item(0) != null && node.item(0).getFirstChild() != null) {
					importeAnticipo = node.item(0).getFirstChild().getNodeValue().toString();
				}
			}
			return importeAnticipo;
		}
		catch (Exception e) {
			log.error("getImporteAnticipo() - Importe de anticipo no informado: " + e.getMessage());
			return null;
		}
	}

	public String getVentaCodigoPostal() {
		log.debug("getVentaCodigoPostal()");
		try {
			String ventaCodigoPostal = null;
			Element root = ticket.getXml().getDocumentElement();
			NodeList node = root.getElementsByTagName("codigo_postal_venta");
			if (node != null && node.getLength() > 0) {
				if (node.item(0) != null && node.item(0).getFirstChild() != null) {
					ventaCodigoPostal = node.item(0).getFirstChild().getNodeValue().toString();
				}
			}
			return ventaCodigoPostal;
		}
		catch (Exception e) {
			log.error("getVentaCodigoPostal() - Importe de anticipo no informado: " + e.getMessage());
			return null;
		}
	}

	public String getOperacionAnticipo() {
		log.debug("getOperacionAnticipo()");
		try {
			String operacionAnticipo = null;
			Element root = ticket.getXml().getDocumentElement();
			NodeList node = root.getElementsByTagName("operacionAnticipo");
			if (node != null && node.getLength() > 0) {
				if (node.item(0) != null && node.item(0).getFirstChild() != null) {
					operacionAnticipo = node.item(0).getFirstChild().getNodeValue().toString();
				}
			}
			return operacionAnticipo;
		}
		catch (Exception e) {
			log.error("getOperacionAnticipo() - Operación de anticipo no informado: " + e.getMessage());
			return null;
		}
	}

	public void addListaMotivosAlbaranExt(AlbaranVenta albaran) {
		if (listaDetalleMotivos != null && !listaDetalleMotivos.isEmpty()) {
			log.debug("addListaMotivosAlbaranExt() - Añadimos la lista de movimientos como extensión al albaran");

			albaran.getBean().putExtension("listaInformesDevolucion", listaDetalleMotivos);
		}
	}

	public void rellenaDetalleMotivo(String codMotivo, Integer idLinea) {
		log.debug("rellenaDetalleMotivo() - Rellenamos el detalle de motivo");
		
		if (listaDetalleMotivos == null) {
			listaDetalleMotivos = new ArrayList<InformeDevolucion>();
		}
		
		/* El idClieAlbaran se añadirá despues de la creación del albaran */
		InformeDevolucion informe = new InformeDevolucion();
		informe.setCodigo(codMotivo);
		informe.setLinea(idLinea);
		informe.setUidActividad(datosSesion.getUidActividad());

		listaDetalleMotivos.add(informe);		
	}

	@Override
	protected void obtenerDatosDocumentoOrigen(Element cabecera) throws XMLDocumentException, AlbaranVentaException, AlbaranVentaNotFoundException, TicketOrigenNotFoundException {
		// Obtenemos del documento origen
		Element datosDocumentoOrigen = XMLDocumentUtils.getElement(cabecera, "datos_documento_origen", true);
		if (datosDocumentoOrigen != null) {
			AlbaranVenta albaranOrigen = null;

			String uidDocumentoOrigen = XMLDocumentUtils.getTagValueAsString(datosDocumentoOrigen, "uidTicket", true);
			// Comprobamos si veien definido el tag de uidTicket del documento de origen
			String codTipoDocumento = XMLDocumentUtils.getTagValueAsString(datosDocumentoOrigen, "cod_tipo_documento", true);
			if(codTipoDocumento.equals("FLEX")) {
				String codTicketOrigen = XMLDocumentUtils.getTagValueAsString(datosDocumentoOrigen, "cod_ticket", true);
				albaran.setCodAlbaranOrigen(codTicketOrigen);
			}else {
				Long idTipoDocumentoOrigen = new Long(XMLDocumentUtils.getTagValueAsString(datosDocumentoOrigen, "id_tipo_documento", true));
				// Si viene definido el uidTicket, se busca directamente el albarÃ¡n a travÃ©s
				// de este valor.
				if (uidDocumentoOrigen != null && !uidDocumentoOrigen.isEmpty()) {
					if (!uidDocumentoOrigen.equalsIgnoreCase(UID_DEVOLUCION_FLEXPOINT)) {
						albaranOrigen = ServicioAlbaranesVentasImpl.get().consultar(conn, uidDocumentoOrigen, datosSesion.getConfigEmpresa());
					}
					else {
						String codTicketOrigen = XMLDocumentUtils.getTagValueAsString(datosDocumentoOrigen, "cod_ticket", true);
						albaran.setCodAlbaranOrigen(codTicketOrigen);
					}

				}
				// Si no viene definido el uidTicket,
				else {
					String codAlmOrigen = XMLDocumentUtils.getTagValueAsString(datosDocumentoOrigen, "serie", true);
					String codCajaOrigen = XMLDocumentUtils.getTagValueAsString(datosDocumentoOrigen, "caja", true);
					String idTicketOrigen = XMLDocumentUtils.getTagValueAsString(datosDocumentoOrigen, "numero_factura", true);
					String codTicket = XMLDocumentUtils.getTagValueAsString(datosDocumentoOrigen, "cod_ticket", true);
					try {
						TicketBean ticketOrigen = ServicioTicketsImpl.get().consultarTicket(sqlSession, codAlmOrigen, idTicketOrigen, codCajaOrigen, idTipoDocumentoOrigen, codTicket,
						        datosSesion.getConfigEmpresa());
						albaranOrigen = ServicioAlbaranesVentasImpl.get().consultar(conn, ticketOrigen.getUidTicket(), datosSesion.getConfigEmpresa());
					}
					catch (TicketNotFoundException e) {
						throw new TicketOrigenNotFoundException(
						        "No se ha encontrado el ticket origen [codAlm= " + codAlmOrigen + ", codCaja=" + codCajaOrigen + ", idTicket=" + idTicketOrigen + ", codTicket=" + codTicket + "]");
					}
				}

				if (albaranOrigen != null) {
					idclieAlbaran = albaranOrigen.getIdAlbaran();
					albaran.setCodAlbaranOrigen(albaranOrigen.getCodAlbaran());
					albaran.setIdTipoDocumentoOrigen(idTipoDocumentoOrigen);
					albaran.setUidTicketOrigen(albaranOrigen.getBean().getUidTicket());
				}
			}
			
		}
	}
	
	
	public TicketBean getTicket() {
		return this.ticket;
	}
	
	private void addMotivosDatos(Motivos motivo, Element motivoTag, Integer idLinea, String codArt, BigDecimal cantidad) throws XMLDocumentException, MotivoException {
		log.debug("addMotivosDatos()");

		String precioArticuloOriginal = XMLDocumentUtils.getTagValueAsString(motivoTag, XML_PRECIO_ARTICULO_ORIGINAL, true);
		String precioArticuloAplicado = XMLDocumentUtils.getTagValueAsString(motivoTag, XML_PRECIO_ARTICULO_APLICADO, true);

		MotivosDatos motivosDatos = new MotivosDatos();
		motivosDatos.setUidActividad(motivo.getUidActividad());
		motivosDatos.setUidTicket(albaran.getUidTicket());
		motivosDatos.setCodAlbaran(albaran.getCodAlbaran());
		motivosDatos.setCodAlbaranOrigen(albaran.getCodAlbaranOrigen());
		motivosDatos.setFecha(albaran.getFechaCompleta());
		motivosDatos.setLinea(idLinea);
		motivosDatos.setCodart(codArt);
		motivosDatos.setTipoMotivo(motivo.getCodigoTipo());
		motivosDatos.setCodigoMotivo(motivo.getCodigo());
		motivosDatos.setDescripcionMotivo(motivo.getDescripcion());
		motivosDatos.setComentarioMotivo(motivo.getComentario());
		motivosDatos.setCantidad(cantidad);

		if (precioArticuloOriginal != null && !precioArticuloOriginal.trim().equals("")) {
			motivosDatos.setPrecioOriginal(new BigDecimal(precioArticuloOriginal));
		}
		if (precioArticuloAplicado != null && !precioArticuloAplicado.trim().equals("")) {
			motivosDatos.setPrecioFinal(new BigDecimal(precioArticuloAplicado));
		}
		
		if (listaMotivosDatos == null) {
			listaMotivosDatos = new ArrayList<MotivosDatos>();
		}
		
		listaMotivosDatos.add(motivosDatos);
		albaran.putExtension("listaMotivosDatos", listaMotivosDatos);
	}
	
	private void getTipoImpresion(Element cabecera) {
		String tipoImpresion = null;
		try {
			tipoImpresion = XMLDocumentUtils.getTagValueAsString(cabecera, "tipo_impresion", true);

			if (StringUtils.isNotBlank(tipoImpresion)) {
				log.debug("getTipoImpresion() - Obteniendo tipo de impresión del ticket : " + ticket.getUidTicket());
				
				TicketTipoImpresion ticketTipoImpresion = new TicketTipoImpresion();
				ticketTipoImpresion.setEstadoBean(Estado.NUEVO);
				ticketTipoImpresion.setUidActividad(ticket.getUidActividad());
				ticketTipoImpresion.setUidTicket(ticket.getUidTicket());
				ticketTipoImpresion.setTipoImpresion(tipoImpresion.toUpperCase());
				ticketTipoImpresion.setFecha(ticket.getFecha());

				albaran.putExtension("ticketTipoImpresion", ticketTipoImpresion);
			}
		} catch (XMLDocumentException e) {
			log.error("getTipoImpresion() - Error obteniendo tipo de impresion del ticket " + ticket.getUidTicket() + " : " + e.getMessage());
		}
	}
	
	public void obtenerNumPresupuesto(Element cabecera) {
		try {
			int numPresupuesto = XMLDocumentUtils.getTagValueAsInt(cabecera, NUM_PRESUPUESTO, true);
			Element totales = (Element) XMLDocumentUtils.getNode(cabecera, "totales", false);
			double total = XMLDocumentUtils.getTagValueAsDouble(totales, "total", false);
			if (numPresupuesto>0) {
				log.debug("obtenerNumPresupuesto() - Obteniendo numPresupuesto de la cabecera del ticket");
					PresupuestoVenta newPresupuesto = new PresupuestoVenta();
					newPresupuesto.setIdPresupuesto(numPresupuesto);
					newPresupuesto.setImporte(BigDecimalUtil.redondear(new BigDecimal(total), 2));
					newPresupuesto.setUidActividad(datosSesion.getUidActividad());
					albaran.putExtension(NUM_PRESUPUESTO, newPresupuesto);
			}
		} catch (Exception e) {
			log.error("obtenerNumPresupuesto() - Ha ocurrido un error obteniendo el numero presupuesto del ticket: " + e.getMessage(), e);
		}
	}
	
	private void tratarPromocionesInforme(Element cabecera, TicketBean ticket, AlbaranVentaBean albaran) throws Exception {
		log.debug("tratarPromocionesInforme() - tratando promociones para ticket " + ticket.getUidTicket() + " para insercion en la tabla x_informe_promociones_tlb");

		List<InformePromociones> listaPromocionesInforme = new ArrayList<>();
		Element promocionesElement = XMLDocumentUtils.getElement(ticket.getXml().getDocumentElement(), "promociones", false);
		List<Element> promociones = XMLDocumentUtils.getChildElements(promocionesElement);

		for (Element promocion : promociones) {
			InformePromociones informePromocionCabecera = new InformePromociones();

			informePromocionCabecera.setUidTicket(XMLDocumentUtils.getTagValueAsString(cabecera, "uid_ticket", false));
			informePromocionCabecera.setUidActividad(XMLDocumentUtils.getTagValueAsString(cabecera, "uid_actividad", false));
			
			String fechaCompleta = XMLDocumentUtils.getTagValueAsString(cabecera, "fecha", false);
			Date soloFecha;
			SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			SimpleDateFormat formatoSalida = new SimpleDateFormat("yyyy-MM-dd");
			Date fechaConHora = formatoEntrada.parse(fechaCompleta);
			String fechaStr = formatoSalida.format(fechaConHora);
			soloFecha = formatoSalida.parse(fechaStr);

			informePromocionCabecera.setFecha(soloFecha);
			informePromocionCabecera.setIdPromocion(XMLDocumentUtils.getTagValueAsLong(promocion, "idpromocion", false));
			informePromocionCabecera.setDescripcionPromocion(XMLDocumentUtils.getTagValueAsString(promocion, "descripcionPromocion", false));

			Element tienda = XMLDocumentUtils.getElement(cabecera, "tienda", false);
			informePromocionCabecera.setCodalm(XMLDocumentUtils.getTagValueAsString(tienda, "codigo", false));
			informePromocionCabecera.setDesalm(XMLDocumentUtils.getTagValueAsString(tienda, "descripcion", false));

			Element datosFidelizado = XMLDocumentUtils.getElement(cabecera, "datos_fidelizacion", true);
			if (datosFidelizado != null) {
				String nombre = XMLDocumentUtils.getTagValueAsString(datosFidelizado, "nombre", false);
				String apellidos = XMLDocumentUtils.getTagValueAsString(datosFidelizado, "apellidos", false);
				String nombreCompleto = nombre + " " + apellidos;
				informePromocionCabecera.setNombreFidelizado(nombreCompleto);
				informePromocionCabecera.setIdFidelizado(XMLDocumentUtils.getTagValueAsLong(datosFidelizado, "idFidelizado", false));
			}

			BigDecimal importeAhorro = new BigDecimal(XMLDocumentUtils.getTagValueAsString(promocion, "importe_total_ahorro", false));
			BigDecimal modificacion = importeAhorro.negate();
			informePromocionCabecera.setValorDescuento(BigDecimalUtil.redondear(modificacion, 2));

			Element totales = XMLDocumentUtils.getElement(cabecera, "totales", false);
			BigDecimal valorCobrado = new BigDecimal(XMLDocumentUtils.getTagValueAsString(totales, "total", false));
			informePromocionCabecera.setValorCobrado(valorCobrado);

			CampAccMarketingBean campaniaEnlaceMarketing = ServicioCampAccEnlMarketingImpl.get().consultarAccionPorIdObjetoEnlace(String.valueOf(informePromocionCabecera.getIdPromocion()),
			        datosSesion);
			if (campaniaEnlaceMarketing != null) {
				try {
					CampAccMarketingBean accion = ServicioCampAccMarketingImpl.get().consultarAccion(campaniaEnlaceMarketing.getUidAccionMkt(), datosSesion);
					informePromocionCabecera.setCodCampania(accion.getCodaccionmkt());
				}
				catch (CampAccMarketingNotFoundException e) {
					log.warn("tratarPromocionesDescuentoManualCuponesDesdeApi() - Accion con uid " + campaniaEnlaceMarketing.getUidAccionMkt() + " No encontrada", e);
					throw new Exception();
				}
			}

			listaPromocionesInforme.add(informePromocionCabecera);
			log.debug("tratarPromocionesInforme() - InformePromociones agregado a la lista para ID de promoción: " + informePromocionCabecera.getIdPromocion());
		}

		albaran.putExtension("listaPromocionesInforme", listaPromocionesInforme);
		log.debug("tratarPromocionesInforme() - Lista de promociones agregada al extension del ticket.");
	}

}
