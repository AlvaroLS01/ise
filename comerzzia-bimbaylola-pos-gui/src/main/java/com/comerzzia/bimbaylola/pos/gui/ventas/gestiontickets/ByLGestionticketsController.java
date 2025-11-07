package com.comerzzia.bimbaylola.pos.gui.ventas.gestiontickets;

import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.comerzzia.ByL.backoffice.persistencia.ventas.albaranes.articulos.ByLTicketRestBean;
import com.comerzzia.ByL.backoffice.rest.client.tickets.ByLResponseTicketsCentral;
import com.comerzzia.ByL.backoffice.rest.client.tickets.ByLTicketsRest;
import com.comerzzia.bimbaylola.pos.gui.ventas.gestiontickets.ticketRegalo.ByLGestionTicketGui;
import com.comerzzia.bimbaylola.pos.persistence.tickets.ByLTicketBean;
import com.comerzzia.bimbaylola.pos.services.core.documentos.ByLDocumentos;
import com.comerzzia.bimbaylola.pos.services.ticket.ByLTicketsService;
import com.comerzzia.core.util.documentos.LocalizadorDocumento;
import com.comerzzia.pos.core.gui.BackgroundTask;
import com.comerzzia.pos.core.gui.InitializeGuiException;
import com.comerzzia.pos.core.gui.componentes.datepicker.DatePicker;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.core.gui.tablas.celdas.CellFactoryBuilder;
import com.comerzzia.pos.gui.ventas.gestiontickets.GestionTicketGui;
import com.comerzzia.pos.gui.ventas.gestiontickets.GestionticketsController;
import com.comerzzia.pos.gui.ventas.gestiontickets.detalle.DetalleGestionTicketView;
import com.comerzzia.pos.gui.ventas.gestiontickets.detalle.DetalleGestionticketsController;
import com.comerzzia.pos.persistence.tickets.TicketBean;
import com.comerzzia.pos.services.core.sesion.Sesion;
import com.comerzzia.pos.services.core.variables.VariablesServices;
import com.comerzzia.pos.services.ticket.TicketsService;
import com.comerzzia.pos.services.ticket.TicketsServiceException;
import com.comerzzia.pos.util.i18n.I18N;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

@Component
@Primary
public class ByLGestionticketsController extends GestionticketsController {

	private static final Logger log = Logger.getLogger(ByLGestionticketsController.class.getName());

	@Autowired
	private Sesion sesion;
	@Autowired
	private TicketsService ticketsService;
	@Autowired
	private ByLTicketsService bylTicketService;
	@Autowired
	private VariablesServices variablesServices;

	@FXML
	protected DatePicker tfFechaHasta;
	@FXML
	protected TextField tfVendedor, tfCodart, tfImporte;
	@FXML
	protected CheckBox cbBusquedaCentral;
	@FXML
	protected TableColumn tcVendedor, tcImporte, tcMedioPago;
	@FXML
	protected TableView<ByLGestionTicketGui> tbTickets;
	
	protected ObservableList<ByLGestionTicketGui> ticketPagos;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		super.initialize(url, rb);

		tcVendedor.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbTickets", "tcVendedor", null, CellFactoryBuilder.ESTILO_ALINEACION_IZQ));
		tcImporte.setCellFactory(CellFactoryBuilder.createCellRendererCeldaImporte("tbTickets", "tcImporte", CellFactoryBuilder.ESTILO_ALINEACION_DCHA));
		tcMedioPago.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbTickets", "tcMedioPago", null, CellFactoryBuilder.ESTILO_ALINEACION_IZQ));

		tcVendedor.setCellValueFactory(new PropertyValueFactory<ByLGestionTicketGui, String>("vendedor"));
		tcImporte.setCellValueFactory(new PropertyValueFactory<ByLGestionTicketGui, String>("total"));		
		tcMedioPago.setCellValueFactory(new PropertyValueFactory<ByLGestionTicketGui, String>("desMedPag"));

		cbBusquedaCentral.selectedProperty().addListener(new ChangeListener<Boolean>(){

			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (cbBusquedaCentral.isSelected()) {
					tfFechaHasta.setDisable(false);
					tfVendedor.setDisable(false);
					tfCodart.setDisable(false);
					tfImporte.setDisable(false);
				}
				else {
					tfFechaHasta.setDisable(true);
					tfVendedor.setDisable(true);
					tfCodart.setDisable(true);
					tfImporte.setDisable(true);
				}
			}
		});
	}

	@Override
	public void initializeForm() throws InitializeGuiException {
		super.initializeForm();
		cbBusquedaCentral.setSelected(false);
		tfFechaHasta.setValue(new Date());
		tfVendedor.setText("");
		tfCodart.setText("");
		tfImporte.setText("");

		tfFechaHasta.setDisable(true);
		tfVendedor.setDisable(true);
		tfCodart.setDisable(true);
		tfImporte.setDisable(true);
		
		
		// TODO Solución provisional hasta confirmar con cliente que las reservas sólo se visualizarán desde la pantalla
		// de reservas
		try {
			Long codReserva = sesion.getAplicacion().getDocumentos().getDocumento(ByLDocumentos.RESERVAS).getIdTipoDocumento();
			idTiposDocValidos.remove(codReserva);
		}
		catch (Exception ignore) {
		}
	}

	public void accionBuscar() {
		log.trace("accionBuscar()");

		ticketPagos.clear();
		tbTickets.setPlaceholder(new Label(""));

		boolean busquedaCentral = cbBusquedaCentral.isSelected();

		if (!busquedaCentral && tfLocalizador.getText().length() > 0) {
			log.debug("accionIntroTfLocalizador()");

			try {
				String localizador = tfLocalizador.getText();
				log.debug("accionIntroTfLocalizador() - Realizando búsqueda con localizador = " + localizador);
				LocalizadorDocumento localizadorDocumento = LocalizadorDocumento.parse(localizador);

				List<TicketBean> ticketsBean = ticketsService.consultarTicketLocalizador(localizadorDocumento, idTiposDocValidos);
				Collections.sort(ticketsBean);

				if (ticketsBean.isEmpty()) {
					tbTickets.setPlaceholder(lbSinResultados);
					tfLocalizador.requestFocus();
					tfLocalizador.selectAll();
				}
				else {
					for (TicketBean ticketBean : ticketsBean) {
						GestionTicketGui ticketGui = new GestionTicketGui(ticketBean);
						tickets.add(ticketGui);
					}
					tfLocalizador.setText("");
					tbTickets.requestFocus();
					tbTickets.getSelectionModel().select(0);
					tbTickets.getFocusModel().focus(0);
				}

			}
			catch (TicketsServiceException e1) {
				VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("No se pudo obtener el ticket con el localizador introducido."), this.getStage());
				log.error("Error en la búsqueda del ticket con el localizador.", e1);
				tfLocalizador.requestFocus();
				tfLocalizador.selectAll();
			}
			catch (Exception e1) {
				VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("Se produjo un error procesando el localizador. " + e1.getMessage()), this.getStage());
				log.error("Error procesando el localizador ", e1);
				tfLocalizador.requestFocus();
				tfLocalizador.selectAll();
			}

		}
		else {
			frGestionTicket.setCodCaja(tfCaja.getText());
			frGestionTicket.setCodTicket(tfTicket.getText().equals("") ? null : tfTicket.getText());
			frGestionTicket.setFecha(tfFecha.getTexto());
			frGestionTicket.setIdDoc(tfCodDoc.getText().equals("") ? null : tfCodDoc.getText());

			tfTicket.deselect();
			if (validarDatosFormulario()) {
				new ByLBuscarTask().start();
			}
		}
	}

	@Override
	public void refrescarDatosPantalla() {
		ticketPagos = FXCollections.observableList(new ArrayList<ByLGestionTicketGui>());
		tbTickets.setItems(ticketPagos);
	}

	protected class ByLBuscarTask extends BackgroundTask<List<ByLTicketBean>> {

		@Override
		protected List<ByLTicketBean> call() throws Exception {

			String codDoc = frGestionTicket.getIdDoc();
			Long idDoc = null;

			if (codDoc != null) {
				idDoc = sesion.getAplicacion().getDocumentos().getDocumento(codDoc).getIdTipoDocumento();
			}

			boolean busquedaCentral = cbBusquedaCentral.isSelected();
			if (busquedaCentral) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

				String fechaDesde = frGestionTicket.getFechaAsDate() != null ? sdf.format(frGestionTicket.getFechaAsDate()).replace("/", "%2F") : null;

				Date fechaHastaD = tfFechaHasta.getSelectedDate();
				String fechaTexto = sdf.format(fechaHastaD);

				String fechaHasta = null;
				if (StringUtils.isNotBlank(fechaTexto)) {
					try {
						sdf.parse(fechaTexto);
						fechaHasta = fechaTexto.replace("/", "%2F");
					}
					catch (Exception e) {
						VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("Introduzca una fecha hasta válida."), getStage());
						return new ArrayList<ByLTicketBean>();
					}
				}
				String vendedor = tfVendedor.getText();
				String codart = tfCodart.getText();
				String importeTexto = tfImporte.getText();
				String importe = null;
				if (StringUtils.isNotBlank(importeTexto)) {
					try {
						new BigDecimal(importeTexto);
						importe = importeTexto;
					}
					catch (Exception e) {
						VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("Introduzca un importe válido."), getStage());
						return new ArrayList<ByLTicketBean>();
					}
				}

				Long idTipoDocumento = null;
				if (StringUtils.isNotBlank(frGestionTicket.getIdDoc())) {
					idTipoDocumento = sesion.getAplicacion().getDocumentos().getDocumento(frGestionTicket.getIdDoc()).getIdTipoDocumento();
				}
				long consultaHistoricoCentral = System.currentTimeMillis();
				ByLResponseTicketsCentral listaTickets = ByLTicketsRest.consultarTicketsCentral(sesion.getAplicacion().getUidActividad(),
				        variablesServices.getVariableAsString(VariablesServices.WEBSERVICES_APIKEY), fechaDesde, fechaHasta, codart, vendedor, importe, sesion.getAplicacion().getCodAlmacen(),
				        frGestionTicket.getCodCaja(), frGestionTicket.getIdTicketAsLong(), idTipoDocumento);
				
				long consultaFinalHistoricoCentral = System.currentTimeMillis();
				log.debug("consultarTicketsParaHistoricoCentral() - En consultar el histórico a Central ha tardado: " + (consultaFinalHistoricoCentral - consultaHistoricoCentral) + " msg, se ha obtenido un total de "+listaTickets.getTickets().size()+ " resultados");
				return convertToTickets(listaTickets.getTickets());
			}
			else {
				return bylTicketService.consultarTicketsParaHistorico(frGestionTicket.getCodCaja(), frGestionTicket.getIdTicketAsLong(), frGestionTicket.getFechaAsDate(), idDoc, idTiposDocValidos);
			}
		}

		@Override
		protected void succeeded() {
			// Ordenamos la lista de tickets obtenida por la fecha de los mismos
			List<ByLTicketBean> ticketsBean = getValue();
			Collections.sort(ticketsBean);

			if (ticketsBean.isEmpty()) {
				tbTickets.setPlaceholder(lbSinResultados);
				tfTicket.requestFocus();
				tfTicket.selectAll();
			}
			else {
				for (ByLTicketBean ticketBean : ticketsBean) {
					ByLGestionTicketGui ticketGui = new ByLGestionTicketGui(ticketBean);
					ticketPagos.add(ticketGui);
				}
				// seleccionamos el primer registro del resultado
				tbTickets.requestFocus();
				tbTickets.getSelectionModel().select(0);
				tbTickets.getFocusModel().focus(0);
			}

			super.succeeded();
		}

		@Override
		protected void failed() {
			VentanaDialogoComponent.crearVentanaError(getStage(), getCMZException().getMessageI18N(), getCMZException());
			tfTicket.requestFocus();
			tfTicket.selectAll();
			super.failed();
		}
	}

	private List<ByLTicketBean> convertToTickets(List<ByLTicketRestBean> listaTickets) {
		List<ByLTicketBean> tickets = new ArrayList<ByLTicketBean>();
		if (listaTickets != null && !listaTickets.isEmpty()) {
			for (ByLTicketRestBean ticket : listaTickets) {
				ByLTicketBean t = new ByLTicketBean();
				t.setUidActividad(ticket.getUidActividad());
				t.setUidTicket(ticket.getUidTicket());
				t.setCodAlmacen(ticket.getCodAlmacen());
				t.setIdTicket(ticket.getIdTicket());
				t.setFecha(ticket.getFecha());
				t.setProcesado(ticket.getProcesado());
				t.setFechaProceso(ticket.getFechaProceso());
				t.setMensajeProceso(ticket.getMensajeProceso());
				t.setCodcaja(ticket.getCodcaja());
				t.setIdTipoDocumento(ticket.getIdTipoDocumento());
				t.setCodTicket(ticket.getCodTicket());
				t.setFirma(ticket.getFirma());
				t.setTicket(ticket.getTicket());
				t.setSerieTicket(ticket.getSerieTicket());
				t.setDesMedPag(ticket.getDesMedPago());
				t.setVendedor(ticket.getUsuario());
				t.setTotal(ticket.getTotal());
				t.setDesDoc(ticket.getDesTipoDoc());
				tickets.add(t);
			}
		}

		return tickets;
	}
	
	@FXML
	public void accionDobleClickTicket(MouseEvent event) {
		if (event.getButton().equals(MouseButton.PRIMARY)) {
			if (event.getClickCount() == 2) {
				mostrarTicket();
			}
		}
	}

	@Override
	protected void mostrarTicket() {
		if (tbTickets.getItems() != null && tbTickets.getItems() != null) {
			ByLGestionTicketGui lineaTicket = tbTickets.getSelectionModel().getSelectedItem();
			if (lineaTicket != null) {
				HashMap<String, Object> datosTicket = new HashMap<String, Object>();
				datosTicket.put(DetalleGestionticketsController.CLAVE_PARAMETRO_TICKETS, tbTickets.getItems());
				datosTicket.put(DetalleGestionticketsController.CLAVE_PARAMETRO_POSICION_TICKET, tbTickets.getSelectionModel().getSelectedIndex());
				if (lineaTicket.getTicketXML() != null) {
					datosTicket.put(DetalleGestionticketsController.CLAVE_PARAMETRO_TICKET_XML, lineaTicket.getTicketXML());
				}

				getApplication().getMainView().showModalCentered(DetalleGestionTicketView.class, datosTicket, this.getStage());
			}
		}
	}
}
