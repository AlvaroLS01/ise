package com.comerzzia.bimbaylola.pos.gui.ventas.devoluciones;

import java.util.Set;

import javax.validation.ConstraintViolation;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.comerzzia.ByL.backoffice.rest.client.tickets.TicketDevolucionResponse;
import com.comerzzia.bimbaylola.pos.gui.componentes.dialogos.ByLVentanaDialogoComponent;
import com.comerzzia.bimbaylola.pos.gui.ventas.devoluciones.fechaorigen.RequestFechaOrigenController;
import com.comerzzia.bimbaylola.pos.gui.ventas.devoluciones.fechaorigen.RequestFechaOrigenView;
import com.comerzzia.bimbaylola.pos.gui.ventas.profesional.devoluciones.ByLIntroduccionArticulosProfesionalView;
import com.comerzzia.bimbaylola.pos.gui.ventas.tickets.ByLTicketManager;
import com.comerzzia.bimbaylola.pos.gui.ventas.tickets.ByLVentaProfesionalManager;
import com.comerzzia.bimbaylola.pos.services.cajas.ByLCajasService;
import com.comerzzia.bimbaylola.pos.services.ticket.cabecera.ByLCabeceraTicket;
import com.comerzzia.bimbaylola.pos.services.vertex.CabeceraVertex;
import com.comerzzia.core.util.documentos.LocalizadorDocumento;
import com.comerzzia.core.util.documentos.LocalizadorDocumentoParseException;
import com.comerzzia.pos.core.gui.BackgroundTask;
import com.comerzzia.pos.core.gui.InitializeGuiException;
import com.comerzzia.pos.core.gui.POSApplication;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.core.gui.permisos.exception.SinPermisosException;
import com.comerzzia.pos.core.gui.validation.ValidationUI;
import com.comerzzia.pos.gui.ventas.devoluciones.DevolucionesController;
import com.comerzzia.pos.gui.ventas.devoluciones.FormularioConsultaTicketBean;
import com.comerzzia.pos.gui.ventas.devoluciones.IntroduccionArticulosView;
import com.comerzzia.pos.gui.ventas.tickets.TicketManager;
import com.comerzzia.pos.gui.ventas.tickets.articulos.FacturacionArticulosController;
import com.comerzzia.pos.persistence.core.documentos.tipos.TipoDocumentoBean;
import com.comerzzia.pos.services.core.documentos.DocumentoException;
import com.comerzzia.pos.services.core.sesion.Sesion;
import com.comerzzia.pos.util.config.AppConfig;
import com.comerzzia.pos.util.config.SpringContext;
import com.comerzzia.pos.util.i18n.I18N;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

@Component
@Primary
public class ByLDevolucionesController extends DevolucionesController {

	public static final String MENU_POS_PROFESIONAL = "POS PROFESIONAL BYL";

	private static final Logger log = Logger.getLogger(ByLDevolucionesController.class.getName());

	private static String ERROR_CANAL = "ERROR_CANAL";
	private static String ERROR_FECHA = "ERROR_FECHA";

	public static final String PERMISO_DEVOLUCION_NO_REFERENCIADAS = "NO_REFERENCIADAS";

	public static final String DEVOLUCION_SIN_TICKET = "esDevolucionSinTicket";

	private static final String CODIGO_PAIS_CO = "CO";
	private static final String CODIGO_PAIS_ECUADOR ="EC";
	private static final String BOLETA_CO_EC = "BO";
	private static final String CODIGO_PAIS_PANAMA ="PA";
	private static final String BOLETA_PA = "BO";
	private static final String FACTURA_SIMPLIFICADA = "FS";

	@Autowired
	protected ByLCajasService cajasService;
	@Autowired
	protected Sesion sesion;

	@FXML
	protected Button btDevolucionSinTicket;

	/**
	 * Realiza una petición para traer un ticket para su devolución.
	 */
	public class RecuperarTicketDevolucionByL extends BackgroundTask<TicketDevolucionResponse> {

		private String codigo;
		private String codTienda, codCaja;
		private Long idTipoDoc;

		public RecuperarTicketDevolucionByL(String codigo, String codTienda, String codCaja, Long idTipoDoc) {
			this.codigo = codigo;
			this.codTienda = codTienda;
			this.codCaja = codCaja;
			this.idTipoDoc = idTipoDoc;
		}

		@Override
		protected TicketDevolucionResponse call() throws Exception {
			return ((ByLTicketManager) ticketManager).recuperarTicketDevolucionByL(codigo, codTienda, codCaja, idTipoDoc);
		}

		@Override
		protected void failed() {
			super.failed();
			if (getException() instanceof com.comerzzia.pos.util.exception.Exception) {
				VentanaDialogoComponent.crearVentanaError(getStage(), getCMZException().getMessage(), getCMZException());
			}
			else {
				VentanaDialogoComponent.crearVentanaError(getStage(), I18N.getTexto("Lo sentimos, ha ocurrido un error."), getException());
			}
		}

		@Override
		protected void succeeded() {
			TicketDevolucionResponse res = getValue();
			/* Para tratar el error de cuando no se encuentra ningún ticket. */
			if (res != null) {
				String mensajeConfirmacion = I18N.getTexto("¿Es un Ticket Regalo?");
				if (ByLVentanaDialogoComponent.crearVentanaSiNo(mensajeConfirmacion, getStage())) {
					log.debug("succeeded() - Se debe tratar como un Ticket Regalo");
					((ByLTicketManager) ticketManager).setEsTicketRegalo(Boolean.TRUE);
				}
				else {
					log.debug("succeeded() - No se debe tratar como un Ticket Regalo");
				}
				recuperarTicketDevolucionSucceededByL(res.getTicket() != null ? true : false, res);
			}
			else {
				TicketDevolucionResponse noEncontrado = new TicketDevolucionResponse();
				noEncontrado.setError("");
				recuperarTicketDevolucionSucceededByL(false, noEncontrado);
			}
			super.succeeded();
		}

	}

	/**
	 * Recupera un ticket con datos de BackOffice para tratarlo en POS.
	 * 
	 * @param encontrado
	 *            : Boolean que indica si el ticket se encontro en BackOffice
	 * @param respuesta
	 *            : Objeto del ticket.
	 */
	protected void recuperarTicketDevolucionSucceededByL(boolean encontrado, TicketDevolucionResponse respuesta) {
		/*
		 * Esto se realiza porque el error que tiene que ver con las fechas no tiene que parar la funcionalidad, solo
		 * lanzar un aviso.
		 */
		if (encontrado) {

			boolean esMismoTratamientoFiscal = ticketManager.comprobarTratamientoFiscalDev();
			/*
			 * Pasamos los articulos no aptos al ticketManager, para poder usarlos al añadir articulos del ticket origen
			 * luego.
			 */
			((ByLTicketManager) ticketManager).setListadoNoAptos(respuesta.getListadoArticulosNoDevolver());

			if (!esMismoTratamientoFiscal) {
				try {
					ticketManager.eliminarTicketCompleto();
				}
				catch (Exception e) {
					log.error("recuperarTicketDevolucionSucceeded() - Ha habido un error al eliminar los tickets: " + e.getMessage(), e);
				}
				lbMensajeError.setText(I18N.getTexto("El ticket fue realizando en una tienda con un tratamiento fiscal diferente" + " al de esta tienda. No se puede realizar esta devolución."));
			}
			else {
				boolean continuar = true;
				/* En caso de entrar en alguno de los errores de canal, no deberá mostrar el de fecha. */
				if (respuesta.getError().equals(ERROR_CANAL)) {
					lbMensajeError.setText(I18N.getTexto("No es posible la devolución entre estas tiendas"));
					continuar = false;
				}
				else if (respuesta.getError().equals(ERROR_FECHA)) {
					/* Si el error es de fecha, podrá continuar. Solo mostrará el error de modo informativo. */
					VentanaDialogoComponent.crearVentanaInfo(I18N.getTexto("Se ha superado el periodo máximo de devolución."), getStage());
				}

				if (continuar) {
					getDatos().put(DEVOLUCION_SIN_TICKET, Boolean.FALSE);
					abrirIntroduccionArticulos(false);
				}
			}
		}
		else {
			lbMensajeError.setText(I18N.getTexto("No se ha encontrado ningún ticket con esos datos"));
		}

	}

	/**
	 * Acción para aceptar iniciar una devolución.
	 */
	@FXML
	public void accionAceptar() {
		if (cajasService.comprobarCajaMaster()) {
			lbMensajeError.setText("");
			if (validarFormularioConsultaCliente()) {
				ticketManager = SpringContext.getBean(TicketManager.class);

				String codTienda = tfTienda.getText();
				String codCaja = tfCodCaja.getText();
				String codigo = tfOperacion.getText();
				String codDoc = tfCodDoc.getText();
				try {
					Long idTipoDocumento = documentos.getDocumento(codDoc).getIdTipoDocumento();
					new RecuperarTicketDevolucionByL(codigo, codTienda, codCaja, idTipoDocumento).start();

				}
				catch (DocumentoException e) {
					VentanaDialogoComponent.crearVentanaError(getStage(), String.format(I18N.getTexto("El documento %s no se ha encontrado"), codDoc), e);
				}
			}
		}
		else {
			cajaMasterCerrada();
		}
	}

	@FXML
	public void accionDevolucionSinTicket() {
		if (sesion.getAplicacion().getTienda().getCliente().getCodpais().equals(CODIGO_PAIS_ECUADOR) || sesion.getAplicacion().getTienda().getCliente().getCodpais().equals(CODIGO_PAIS_PANAMA)) {
			getApplication().getMainView().showModalCentered(RequestFechaOrigenView.class, getDatos(), this.getStage());
			if (getDatos().get(RequestFechaOrigenController.FECHA_ACCION) == null || !((Boolean) getDatos().get(RequestFechaOrigenController.FECHA_ACCION))) {
				return;
			}
		}
		abrirIntroduccionArticulos(true);
	}

	private void abrirIntroduccionArticulos(Boolean sinTicket) {
		try {
			if (AppConfig.menu.equals(MENU_POS_PROFESIONAL)) {
				ByLVentaProfesionalManager ticketManagerProfesional = SpringContext.getBean(ByLVentaProfesionalManager.class);
				if (!sinTicket) {
					ticketManagerProfesional.setDevolucionSinTicket(Boolean.FALSE);
					ticketManagerProfesional.setTicketOrigen(ticketManager.getTicketOrigen());

					try {
						ticketManagerProfesional.nuevoTicket();
						ticketManagerProfesional.getTicket().getCabecera().setDatosDocOrigen(ticketManager.getTicket().getCabecera().getDatosDocOrigen());
						ticketManagerProfesional.setDocumentoActivo(documentos.getDocumentoAbono(ticketManagerProfesional.getDocumentoActivo().getCodtipodocumento()));
						tratarDatosVertex(ticketManagerProfesional);
					}
					catch (Exception e) {
						log.error("abrirIntroduccionArticulos() - No se ha podido crear un nuevo ticket: " + e.getMessage(), e);
						VentanaDialogoComponent.crearVentanaError(getStage(), I18N.getTexto("No se ha podido iniciar la devolución. Contacte con el responsable."), e);
						return;
					}

					ticketManagerProfesional.setEsTicketRegalo(((ByLTicketManager) ticketManager).getEsTicketRegalo());
					ticketManagerProfesional.setListadoNoAptos(((ByLTicketManager) ticketManager).getListadoNoAptos());
					getDatos().put(FacturacionArticulosController.TICKET_KEY, ticketManagerProfesional);
					getView().changeSubView(ByLIntroduccionArticulosProfesionalView.class);
				}
				else {
					try {
						if (comprobarPermisoNoReferenciadas()) {
							if (cajasService.comprobarCajaMaster()) {
								try {
									((ByLVentaProfesionalManager) ticketManagerProfesional).setEsTicketRegalo(Boolean.FALSE);
									getDatos().put(DEVOLUCION_SIN_TICKET, Boolean.TRUE);
									getView().changeSubView(ByLIntroduccionArticulosProfesionalView.class);
								}
								catch (InitializeGuiException e) {
									if (e.isMostrarError()) {
										log.error("accionCambiarArticulo() - Error abriendo ventana", e);
										VentanaDialogoComponent.crearVentanaError(getStage(), I18N.getTexto("Error cargando pantalla. Para mas información consulte el log."), e);
									}
								}
							}
							else {
								cajaMasterCerrada();
							}
						}
					}
					catch (SinPermisosException e1) {
						VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("No tiene permisos para realizar la acción."), getStage());
					}
				}
			}
			else {
				if (!sinTicket) {
					getDatos().put(FacturacionArticulosController.TICKET_KEY, ticketManager);
					getDatos().put(DEVOLUCION_SIN_TICKET, Boolean.FALSE);
					getView().changeSubView(IntroduccionArticulosView.class);
				}
				else {
					try {
						if (comprobarPermisoNoReferenciadas()) {
							if (cajasService.comprobarCajaMaster()) {
								try {
									((ByLTicketManager) ticketManager).setEsTicketRegalo(Boolean.FALSE);
									getDatos().put(DEVOLUCION_SIN_TICKET, Boolean.TRUE);
									getView().changeSubView(IntroduccionArticulosView.class);
								}
								catch (InitializeGuiException e) {
									if (e.isMostrarError()) {
										log.error("accionCambiarArticulo() - Error abriendo ventana", e);
										VentanaDialogoComponent.crearVentanaError(getStage(), I18N.getTexto("Error cargando pantalla. Para mas información consulte el log."), e);
									}
								}
							}
							else {
								cajaMasterCerrada();
							}
						}
					}
					catch (SinPermisosException e1) {
						VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("No tiene permisos para realizar la acción."), getStage());
					}
				}
			}
		}
		catch (InitializeGuiException e) {
			if (e.isMostrarError()) {
				log.error("abrirIntroduccionArticulos() - Error abriendo ventana", e);
				VentanaDialogoComponent.crearVentanaError(getStage(), I18N.getTexto("Error cargando pantalla. Para mas información consulte el log."), e);
			}
		}
	}

	/**
	 * Envía un mensaje por pantalla, que indica que la caja no está abierta y te envía a la pantalla principal.
	 */
	private void cajaMasterCerrada() {
		VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("La caja a la que estaba conectado se ha cerrado."), this.getStage());
		/* Vuelve a la pantalla principal. */
		POSApplication.getInstance().getMainView().close();
	}

	protected boolean validarFormularioConsultaCliente() {
		boolean valido;

		// Limpiamos los errores que pudiese tener el formulario
		frConsultaTicket.clearErrorStyle();
		// Limpiamos el posible error anterior
		lbMensajeError.setText("");

		frConsultaTicket.setCodCaja(tfCodCaja.getText());
		frConsultaTicket.setCodOperacion(tfOperacion.getText());
		frConsultaTicket.setCodTienda(tfTienda.getText());
		frConsultaTicket.setCodDoc(tfCodDoc.getText());

		String codigo = frConsultaTicket.getCodOperacion();
		try {
			LocalizadorDocumento.parse(codigo);
		}
		catch (LocalizadorDocumentoParseException e) {
			// No es localizador válido
			// if(codigo.length() > 10){
			// //No es codDocumento válido
			// lbMensajeError.setText(I18N.getTexto("El código {0} no es un localizador o un código de documento
			// válido",
			// codigo));
			// valido = false;
			// return valido;
			// }
		}

		// Validamos el formulario de login
		Set<ConstraintViolation<FormularioConsultaTicketBean>> constraintViolations = ValidationUI.getInstance().getValidator().validate(frConsultaTicket);
		if (constraintViolations.size() >= 1) {
			ConstraintViolation<FormularioConsultaTicketBean> next = constraintViolations.iterator().next();
			frConsultaTicket.setErrorStyle(next.getPropertyPath(), true);
			frConsultaTicket.setFocus(next.getPropertyPath());
			lbMensajeError.setText(next.getMessage());
			valido = false;
		}
		else {
			valido = true;
		}

		return valido;
	}

	public Boolean comprobarPermisoNoReferenciadas() throws SinPermisosException {

		boolean permisos = true;

		try {
			compruebaPermisos(PERMISO_DEVOLUCION_NO_REFERENCIADAS);
		}
		catch (SinPermisosException e) {
			permisos = false;
			log.info("comprobarPermisoNoReferenciadas() - No tienes permisos para realizar la acción");
			throw new SinPermisosException();
		}

		return permisos;

	}

	@Override
	public void initializeForm() throws InitializeGuiException {
		super.initializeForm();
		preseleccionTipoDocumento();
	}

	private void tratarDatosVertex(ByLVentaProfesionalManager ticketManagerProfesional) {
		log.debug("tratarDatosVertex() - Se comprueba si la venta tiene impuestos vertex. En caso positivo se añadirá al ticket");
		if (ticketManager.getTicketOrigen() != null) {
			CabeceraVertex cabeceraVertexOrigen = ((ByLCabeceraTicket) ticketManager.getTicketOrigen().getCabecera()).getCabeceraVertex();
			((ByLCabeceraTicket) ticketManagerProfesional.getTicket().getCabecera()).setCabeceraVertex(cabeceraVertexOrigen);
		}
	}

	private void preseleccionTipoDocumento() {
		try {
			/*
			 * PRESELECCION DE TIPO DE DOCUMENTO EN COLOMBIA
			 */
			if (sesion.getAplicacion().getTienda().getCliente().getCodpais().equals(CODIGO_PAIS_CO)) {
				TipoDocumentoBean docPreseleccion = this.documentos.getDocumento(BOLETA_CO_EC);
				if (docPreseleccion != null) {
					tfCodDoc.setText(docPreseleccion.getCodtipodocumento());
					tfDesDoc.setText(docPreseleccion.getDestipodocumento());
					log.debug("initializeForm() - Estableciendo el documento " + BOLETA_CO_EC + " por defecto para Colombia");
				}
			}
			else if (sesion.getAplicacion().getTienda().getCliente().getCodpais().equals(CODIGO_PAIS_ECUADOR)) {
				TipoDocumentoBean docPreseleccion = this.documentos.getDocumento(BOLETA_CO_EC);
				if (docPreseleccion != null) {
					tfCodDoc.setText(docPreseleccion.getCodtipodocumento());
					tfDesDoc.setText(docPreseleccion.getDestipodocumento());
					log.debug("initializeForm() - Estableciendo el documento " + BOLETA_CO_EC + " por defecto para Ecuador");
				}
			}
			else if (sesion.getAplicacion().getTienda().getCliente().getCodpais().equals(CODIGO_PAIS_PANAMA)) {
				TipoDocumentoBean docPreseleccion = this.documentos.getDocumento(BOLETA_PA);
				if (docPreseleccion != null) {
					tfCodDoc.setText(docPreseleccion.getCodtipodocumento());
					tfDesDoc.setText(docPreseleccion.getDestipodocumento());
					log.debug("initializeForm() - Estableciendo el documento " + BOLETA_PA + " por defecto para Panamá");
				}
			}
			else {
				/*
				 * PRESELECCION PARA EL RESTO DE PAISES
				 */
				TipoDocumentoBean docPreseleccion = this.documentos.getDocumento(FACTURA_SIMPLIFICADA);
				if (docPreseleccion != null) {
					tfCodDoc.setText(docPreseleccion.getCodtipodocumento());
					tfDesDoc.setText(docPreseleccion.getDestipodocumento());
					log.debug("initializeForm() - Estableciendo el documento FS por defecto");
				}
			}
		}
		catch (DocumentoException e) {
			log.debug("initializeForm() - No se ha podido establecer el tipo de documento por defecto");
		}

	}

}