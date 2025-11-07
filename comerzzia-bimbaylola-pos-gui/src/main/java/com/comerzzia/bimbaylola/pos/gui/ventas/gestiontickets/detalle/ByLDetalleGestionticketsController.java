package com.comerzzia.bimbaylola.pos.gui.ventas.gestiontickets.detalle;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.comerzzia.ByL.backoffice.persistencia.taxfree.TaxFreeVoucherBean;
import com.comerzzia.ByL.backoffice.rest.client.tickets.ByLTicketsRest;
import com.comerzzia.ByL.backoffice.rest.client.tickets.TaxFreeResponse;
import com.comerzzia.bimbaylola.pos.dispositivo.impresora.spark130f.Spark130F;
import com.comerzzia.bimbaylola.pos.gui.ventas.devoluciones.ByLDevolucionesController;
import com.comerzzia.bimbaylola.pos.gui.ventas.gestiontickets.ticketRegalo.ByLGestionTicketGui;
import com.comerzzia.bimbaylola.pos.persistence.ticket.articulos.agregarnotainformativa.AvisoInformativoBean;
import com.comerzzia.bimbaylola.pos.persistence.ticket.articulos.agregarnotainformativa.NotaInformativaBean;
import com.comerzzia.bimbaylola.pos.services.articulos.articulosNoAptos.ArticuloNoAptoException;
import com.comerzzia.bimbaylola.pos.services.core.ByLServicioContadores;
import com.comerzzia.bimbaylola.pos.services.spark130f.Spark130FConstants;
import com.comerzzia.bimbaylola.pos.services.spark130f.Spark130FService;
import com.comerzzia.bimbaylola.pos.services.spark130f.exception.Spark130FException;
import com.comerzzia.bimbaylola.pos.services.taxFree.TaxFreeLeerXML;
import com.comerzzia.bimbaylola.pos.services.taxFree.TaxFreeProcesador;
import com.comerzzia.bimbaylola.pos.services.taxFree.TaxFreeXMLVoucher;
import com.comerzzia.bimbaylola.pos.services.ticket.ByLLineaTicket;
import com.comerzzia.bimbaylola.pos.services.ticket.ByLTicketVentaAbono;
import com.comerzzia.bimbaylola.pos.services.ticket.articulos.agregarnotainformativa.ByLAgregarNotaInformativaService;
import com.comerzzia.bimbaylola.pos.services.ticket.cabecera.ByLCabeceraTicket;
import com.comerzzia.bimbaylola.pos.services.ticket.profesional.ByLLineaTicketProfesional;
import com.comerzzia.core.util.fechas.Fecha;
import com.comerzzia.core.util.mybatis.session.SqlSession;
import com.comerzzia.pos.core.dispositivos.Dispositivos;
import com.comerzzia.pos.core.dispositivos.dispositivo.impresora.IPrinter;
import com.comerzzia.pos.core.gui.BackgroundTask;
import com.comerzzia.pos.core.gui.InitializeGuiException;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.gui.ventas.gestiontickets.detalle.DetalleGestionticketsController;
import com.comerzzia.pos.gui.ventas.tickets.TicketManager;
import com.comerzzia.pos.persistence.core.contadores.ContadorBean;
import com.comerzzia.pos.persistence.core.documentos.tipos.TipoDocumentoBean;
import com.comerzzia.pos.persistence.mybatis.SpringTransactionSqlSession;
import com.comerzzia.pos.persistence.tickets.TicketBean;
import com.comerzzia.pos.services.core.contadores.ServicioContadores;
import com.comerzzia.pos.services.core.documentos.DocumentoException;
import com.comerzzia.pos.services.core.documentos.Documentos;
import com.comerzzia.pos.services.core.variables.VariablesServices;
import com.comerzzia.pos.services.devices.DeviceException;
import com.comerzzia.pos.services.ticket.Ticket;
import com.comerzzia.pos.services.ticket.TicketsService;
import com.comerzzia.pos.services.ticket.cabecera.CabeceraTicket;
import com.comerzzia.pos.services.ticket.cabecera.TotalesTicket;
import com.comerzzia.pos.services.ticket.lineas.LineaTicket;
import com.comerzzia.pos.services.ticket.profesional.TotalesTicketProfesional;
import com.comerzzia.pos.servicios.impresion.ServicioImpresion;
import com.comerzzia.pos.util.config.AppConfig;
import com.comerzzia.pos.util.config.SpringContext;
import com.comerzzia.pos.util.i18n.I18N;
import com.comerzzia.pos.util.xml.MarshallUtil;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

@Component
@Primary
public class ByLDetalleGestionticketsController extends DetalleGestionticketsController {

	private static final Logger log = Logger.getLogger(ByLDetalleGestionticketsController.class.getName());

	public static final String PLANTILLA_NOTA_INFORMATIVA = "nota_informativa";
	
	public static final String MENU_POS = "POS_BYL";
	
	@Autowired
	private ByLServicioContadores byLServicioContadores;
	@Autowired
	protected ServicioContadores servicioContadores;
	@Autowired
	private VariablesServices variablesServices;
	@Autowired
	protected TicketsService ticketService;
	@Autowired
	protected Spark130FService spark130FService;	
	@Autowired
	private Documentos documentos;
	
	@FXML
	protected Button btReprint;

	protected TicketManager ticketManager;

	@SuppressWarnings("rawtypes")
	@FXML
	public void accionTaxFree() {
		SqlSession sqlSession = SpringContext.getBean(SpringTransactionSqlSession.class);

		String rutaCarpeta = variablesService.getVariableAsString(TaxFreeLeerXML.TAXFREE_RUTA_CARPETA);
		String rutaEjecutable = rutaCarpeta + "\\" + TaxFreeLeerXML.NOMBRE_EJECUTABLE;
		String rutaFicheroXML = rutaCarpeta + "\\" + TaxFreeLeerXML.NOMBRE_CARPETA_FICHEROS + "\\";
		String nombreFichero = rutaFicheroXML + ticketOperacion.getUidTicket() + ".xml";

		TaxFreeLeerXML.leerXML((ByLTicketVentaAbono) ticketOperacion, nombreFichero);
		TaxFreeProcesador p = new TaxFreeProcesador(rutaCarpeta, rutaEjecutable);
		String respuesta = p.run("-FileName:" + ticketOperacion.getUidTicket());

		// Una vez realizado el proceso, borraremos el .xml
		File file = new File(nombreFichero);
		file.delete();

		if (respuesta != null) {
			// Si todo ha ido OK, el programa nos dejará el voucher/xml con informfación (num formulario/num pasaporte/
			// etc)

			// Primero leeremos el xml
			try {

				TaxFreeXMLVoucher voucher = leerXMLVoucher(respuesta);
				byte[] xmlVoucher = MarshallUtil.crearXML(voucher);

				// Creamos el documento TAXFREE
				// Primero obtenemos el contador

				Map<String, String> parametrosContador = new HashMap<>();

				parametrosContador.put("CODEMP", ((Ticket) ticketOperacion).getEmpresa().getCodEmpresa());
				parametrosContador.put("CODALM", ((Ticket) ticketOperacion).getTienda().getAlmacenBean().getCodAlmacen());
				parametrosContador.put("CODSERIE", ((Ticket) ticketOperacion).getTienda().getAlmacenBean().getCodAlmacen());
				parametrosContador.put("CODCAJA", ((Ticket) ticketOperacion).getCodCaja());
				parametrosContador.put("CODTIPODOCUMENTO", "TF"); // Codigo Tipo Documento TAXFREE
				parametrosContador.put("PERIODO", ((new Fecha()).getAño().toString()));

				TipoDocumentoBean documentoActivo = sesion.getAplicacion().getDocumentos().getDocumento("TF");
				ContadorBean ticketContador = byLServicioContadores.obtenerContador(documentoActivo.getIdContador(), parametrosContador, ((Ticket) ticketOperacion).getUidActividad());

				TicketBean ticketBean = new TicketBean();
				ticketBean.setCodAlmacen(((Ticket) ticketOperacion).getCabecera().getTienda().getAlmacenBean().getCodAlmacen());
				ticketBean.setCodcaja(((Ticket) ticketOperacion).getCodCaja());
				ticketBean.setFecha(new Date());
				ticketBean.setIdTicket(ticketContador.getValor());
				ticketBean.setUidTicket(UUID.randomUUID().toString());
				ticketBean.setIdTipoDocumento(documentoActivo.getIdTipoDocumento()); // Id Tipo Documento TAXFREE
				ticketBean.setCodTicket(ticketContador.getValorFormateado());
				ticketBean.setSerieTicket(ticketContador.getConfigContador().getValorDivisor3());

				ticketBean.setTicket(xmlVoucher);
				ticketBean.setFirma("*");

				ticketService.insertarTicket(sqlSession, ticketBean, false);

				VentanaDialogoComponent.crearVentanaInfo(I18N.getTexto("Se ha generado correctamente el taxfree"), this.getStage());
			}
			catch (Exception e) {
				String msg = "Se ha producido un error al generar el taxfree con voucher " + respuesta + " : " + e.getMessage();
				log.error("accionTaxFree() - " + msg, e);
				VentanaDialogoComponent.crearVentanaError(I18N.getTexto("Se ha generado el Voucher pero no se ha podido guardar en BBDD"), this.getStage());
			}

		}
		else {
			// No se ha podido realizar la generación de taxfree
			VentanaDialogoComponent.crearVentanaError(I18N.getTexto("No se ha podido realizar la generación de taxFree"), this.getStage());
		}
	}

	private TaxFreeXMLVoucher leerXMLVoucher(String nombreFichero) {
		TaxFreeXMLVoucher voucher = new TaxFreeXMLVoucher();

		voucher.setNumeroFormulario(nombreFichero.replace("-", ""));

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String fecha = sdf.format(new Date());
		voucher.setFecha(fecha);

		voucher.setUidActividad(((ByLTicketVentaAbono) ticketOperacion).getUidActividad());
		voucher.setUidTicket(((ByLTicketVentaAbono) ticketOperacion).getUidTicket());

		return voucher;
	}

	@Override
	public void refrescarDatosPantalla() throws InitializeGuiException {
		new RefrescarDatosPantallaTask().start();
	}

	@FXML
	private void accionTaxFreeReprint() {
		String rutaCarpeta = variablesService.getVariableAsString(TaxFreeLeerXML.TAXFREE_RUTA_CARPETA);
		String rutaEjecutable = rutaCarpeta + "\\" + TaxFreeLeerXML.NOMBRE_EJECUTABLE;

		TaxFreeProcesador p = new TaxFreeProcesador(rutaCarpeta, rutaEjecutable);
		String respuesta = p.run("-Reprint:" + ((ByLTicketVentaAbono) ticketOperacion).getTaxFree().getNumeroFormulario());

		log.debug("accionTaxFreeReprint() - Respuesta de la llamada de reimpresion de voucher: " + respuesta);
	}

	private TaxFreeVoucherBean taxFreeResponseToBean(TaxFreeResponse taxFreeResponse) {
		TaxFreeVoucherBean bean = new TaxFreeVoucherBean();

		bean.setFecha(taxFreeResponse.getFecha());
		bean.setNumeroFormulario(taxFreeResponse.getNumeroFormulario());
		bean.setUidActividad(taxFreeResponse.getUidActividad());
		bean.setUidTicket(taxFreeResponse.getUidTicket());

		return bean;
	}

	@SuppressWarnings("unchecked")
	@Override
	@FXML
	protected void accionImprimirCopia(ActionEvent event) {
		if (isImpresoraFiscalRusia()) {
			String fiscalDocument = ((ByLCabeceraTicket) ticketOperacion.getCabecera()).getSpark130F().getFd();
			try {
				List<String> listAtributos = new ArrayList<>();
				listAtributos.add("RC");
				HashMap<String, String> mapaCampos = spark130FService.getCamposRespuesta(spark130FService.realizarLlamada(spark130FService.printDocumentByFD(fiscalDocument)), listAtributos);
				String returnCode = mapaCampos.get("RC");

				if (returnCode.equals(Spark130FConstants.NO_ERROR)) {
					log.debug("accionImprimirCopia() - Impresión por impresora fiscal Spark130F realizada con éxito");
				}
				else {
					Map<String, String> mapaErrores = Spark130FConstants.setErrors();

					String errorDesc = mapaErrores.get(returnCode);
					throw new Spark130FException(errorDesc);
				}

			}
			catch (Spark130FException e) {
				log.error("accionImprimirCopia() - Se ha producido un error al realizar la impresión fiscal: " + e.getMessage(), e);
			}
		}
		else {
			super.accionImprimirCopia(event);
		}

		try {
			if (AppConfig.menu.equals(MENU_POS)){
				List<ByLLineaTicket> lineas = (List<ByLLineaTicket>) ticketOperacion.getLineas();
				procesarLineasTicket(lineas);
			}
			else {
				List<ByLLineaTicketProfesional> lineas = (List<ByLLineaTicketProfesional>) ticketOperacion.getLineas();
				procesarLineasTicketProfesional(lineas);
			}
			
		}
		catch (Exception e) {
			log.debug("accionImprimirCopia() - Ha ocurrido un error al imprimir el ticket" + e.getMessage(), e);

		}

	}

	protected class RefrescarDatosPantallaTask extends BackgroundTask<Boolean> {

		@SuppressWarnings("rawtypes")
		@Override
		protected Boolean call() throws Exception {
			try {
				log.debug("refrescarDatosPantallaTask()");
				log.debug("Obtenemos el XML del ticket que queremos visualizar");

				camposPorDefecto();

				ticket = tickets.get(posicionActual);
				byte[] ticketXML = ticket.getTicketXML();

				/*
				 * En el caso de que se haya consultado en central, traera el xml del ticket. La consulta que se realiza
				 * en local no devuelve el xml, por lo que se mantiene la segunda consulta que realiza estandar
				 */
				if (ticketXML == null) {
					TicketBean ticketConsultado = ticketsService.consultarTicket(ticket.getUidTicket(), sesion.getAplicacion().getUidActividad());
					ticketXML = ticketConsultado.getTicket();
				}

				TipoDocumentoBean documento = documentos.getDocumento(Long.parseLong(((ByLGestionTicketGui) ticket).getTipoDocumento()));
				if (documento.getFormatoImpresion().equals(TipoDocumentoBean.PROPIEDAD_FORMATO_IMPRESION_NO_CONFIGURADO)) {
					if (getStage() != null && getStage().isShowing()) {
						VentanaDialogoComponent.crearVentanaInfo(I18N.getTexto("No es posible visualizar este tipo de documento"), getStage());
					}
					else {
						Platform.runLater(new Runnable(){

							@Override
							public void run() {
								VentanaDialogoComponent.crearVentanaInfo(I18N.getTexto("No es posible visualizar este tipo de documento"), getStage());
							}
						});
					}
					taTicket.setText("");
					return false;
				}

				ticketOperacion = (ByLTicketVentaAbono) MarshallUtil.leerXML(ticketXML, getTicketClasses(documento).toArray(new Class[] {}));

				if (ticketOperacion != null) {
					ticketOperacion.getCabecera().setDocumento(sesion.getAplicacion().getDocumentos().getDocumento(ticketOperacion.getCabecera().getTipoDocumento()));
					if (sesion.getAplicacion().getDocumentos().getDocumento(ticketOperacion.getCabecera().getTipoDocumento()).getPermiteTicketRegalo()) {
						btnTicketRegalo.setDisable(false);
					}
					else {
						btnTicketRegalo.setDisable(true);
					}
					try {
						Map<String, Object> mapaParametros = new HashMap<String, Object>();
						mapaParametros.put("ticket", ticketOperacion);
						mapaParametros.put("urlQR", variablesService.getVariableAsString("TPV.URL_VISOR_DOCUMENTOS"));
						mapaParametros.put("esGestion", true);
						// Hay que obtener el resultado de mostrar en pantalla el ticket y mostrarlo en taTicket
						String previsualizacion = ServicioImpresion.imprimirPantalla(ticketOperacion.getCabecera().getFormatoImpresion(), mapaParametros);
						taTicket.setText(previsualizacion);
					}
					catch (Exception e) {
						VentanaDialogoComponent.crearVentanaError(getStage(), I18N.getTexto("Lo sentimos, ha ocurrido un error al imprimir."), e);
						throw new InitializeGuiException(false);
					}
				}
				else {
					log.error("refrescarDatosPantalla()- Error leyendo ticket otriginal");
					VentanaDialogoComponent.crearVentanaError(I18N.getTexto("Error leyendo información de ticket."), getStage());
					throw new InitializeGuiException(false);
				}

				String uidActividad = ((ByLTicketVentaAbono) ticketOperacion).getUidActividad();
				String uidTicket = ((ByLTicketVentaAbono) ticketOperacion).getUidTicket();
				String apiKey = variablesServices.getVariableAsString(VariablesServices.WEBSERVICES_APIKEY);

				TaxFreeVoucherBean taxFreeVoucher = null;
				try {
					TaxFreeResponse taxFreeResponse = ByLTicketsRest.getTaxFree(uidActividad, uidTicket, apiKey);
					if (taxFreeResponse != null) {
						taxFreeVoucher = taxFreeResponseToBean(taxFreeResponse);
						((ByLTicketVentaAbono) ticketOperacion).setTaxFree(taxFreeVoucher);
					}
					else {
						log.warn("refrescarDatosPantallaTask() - getTaxFree() : No se han encontrado datos de TaxFree para el ticket " + ticketOperacion.getCabecera().getCodTicket());
					}
				}
				catch (Exception e) {
					log.warn("refrescarDatosPantallaTask() - Error al obtener taxfree del ticket " + ticketOperacion.getCabecera().getCodTicket() + " - " + e.getLocalizedMessage(), e);
				}

				if (taxFreeVoucher != null) {
					btReprint.setVisible(true);
				}
				else {
					btReprint.setVisible(false);
				}

			}
			catch (DocumentoException e) {
				log.error("Error recuperando el tipo de documento del ticket.", e);
			}
			return true;
		}

		@Override
		protected void succeeded() {
			super.succeeded();
		}

		@Override
		protected void failed() {
			super.failed();

		}
	}
	
	private void camposPorDefecto() {
		btReprint.setVisible(false);
		btnTicketRegalo.setDisable(true);
		
		taTicket.clear();
	}
	
	private boolean isImpresoraFiscalRusia() {
		IPrinter printer = Dispositivos.getInstance().getImpresora1();
		if (printer != null && printer instanceof Spark130F) {
			return true;
		}
		else {
			return false;
		}
	}
	
	@Override
	public List<Class<?>> getTicketClasses(TipoDocumentoBean tipoDocumento) {
		List<Class<?>> classes = new LinkedList<>();
		
		// Obtenemos la clase root
		Class<?> clazz = SpringContext.getBean(getTicketClass(tipoDocumento)).getClass();
		
		// Generamos lista de clases "ancestras" de la principal
		Class<?> superClass = clazz.getSuperclass();
		while (!superClass.equals(Object.class)) {
			classes.add(superClass);
			superClass = superClass.getSuperclass();
		}
		// Las ordenamos descendentemente
		Collections.reverse(classes);
		
		//Añadimos la clase principal y otras necesarias
		classes.add(clazz);
		classes.add(SpringContext.getBean(LineaTicket.class).getClass());
		classes.add(SpringContext.getBean(CabeceraTicket.class).getClass());
		classes.add(SpringContext.getBean(TotalesTicket.class).getClass());
		classes.add(SpringContext.getBean(TotalesTicketProfesional.class).getClass());
		classes.add(ByLLineaTicketProfesional.class);

		return classes;
	}
	
	private void procesarLineasTicket(List<ByLLineaTicket> lineas) throws ArticuloNoAptoException, DeviceException {
	    for (ByLLineaTicket linea : lineas) {
	        procesarLineaTicket(linea);
	    }
	}

	private void procesarLineasTicketProfesional(List<ByLLineaTicketProfesional> lineas) throws ArticuloNoAptoException, DeviceException {
	    for (ByLLineaTicketProfesional linea : lineas) {
	        procesarLineaTicket(linea);
	    }
	}
	
	private void procesarLineaTicket(Object linea) throws ArticuloNoAptoException, DeviceException {
		NotaInformativaBean notaInformativa = null;
		
		 if (linea instanceof ByLLineaTicket && ((ByLLineaTicket) linea).getNotaInformativa() != null) {
			 notaInformativa = ((ByLLineaTicket) linea).getNotaInformativa();
		 }
		 else if (linea instanceof ByLLineaTicketProfesional&& ((ByLLineaTicketProfesional) linea).getNotaInformativa() != null) {
			 notaInformativa = ((ByLLineaTicketProfesional) linea).getNotaInformativa();
		 }
		 
		if (notaInformativa != null) {
			AvisoInformativoBean aviso = ByLAgregarNotaInformativaService.get().consultarAvisoInformativo(sesion.getAplicacion().getUidActividad(),
			        sesion.getAplicacion().getTienda().getCliente().getCodpais(), notaInformativa.getCodigo());
			if (aviso.getDocuIndepe().equals("S")) {
				Long numeroCopias = 1L;
				try {
					numeroCopias = aviso.getCopias();
				}
				catch (Exception e) {
					log.debug("accionImprimirCopia() - " + e.getMessage());
				}

				Map<String, Object> mapaParametrosManager = new HashMap<>();
				mapaParametrosManager.put("ticket", ticketOperacion);
				mapaParametrosManager.put("linea", linea);
				for (int i = 0; i < numeroCopias; i++) {
					log.debug("accionImprimirCopia() - imprimiendo copia número " + i);
					ServicioImpresion.imprimir(PLANTILLA_NOTA_INFORMATIVA, mapaParametrosManager);
				}
			}
		}
	}
}
