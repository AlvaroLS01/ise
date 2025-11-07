package com.comerzzia.bimbaylola.pos.gui.ventas.gestiontickets.ticketRegalo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.comerzzia.bimbaylola.pos.devices.impresoras.fiscal.IFiscalPrinter;
import com.comerzzia.bimbaylola.pos.dispositivo.impresora.fiscal.polonia.PoloniaFiscalPrinter;
import com.comerzzia.pos.core.dispositivos.Dispositivos;
import com.comerzzia.pos.core.dispositivos.dispositivo.impresora.IPrinter;
import com.comerzzia.pos.core.gui.InitializeGuiException;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.gui.ventas.gestiontickets.ticketRegalo.LineaTicketRegaloGui;
import com.comerzzia.pos.gui.ventas.gestiontickets.ticketRegalo.TicketRegaloController;
import com.comerzzia.pos.services.core.documentos.DocumentoException;
import com.comerzzia.pos.services.devices.DeviceException;
import com.comerzzia.pos.services.ticket.TicketVentaAbono;
import com.comerzzia.pos.services.ticket.lineas.LineaTicket;
import com.comerzzia.pos.servicios.impresion.ServicioImpresion;
import com.comerzzia.pos.util.i18n.I18N;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

@Component
@Primary
public class ByLTicketRegaloController extends TicketRegaloController {

	/* Componentes nuevos para el control impresión de tickets regalo. */
	@FXML
	protected CheckBox ckTodoUnTicket, ckUnoPorLinea;
	@FXML
	protected TextField txNumeroCopias;

	/* Variables que guardan las opciones seleccionadas. */
	private Boolean todo = true;
	private Boolean uno = false;
	private int numCopias = 1;

	public void initializeForm() throws InitializeGuiException {
		super.initializeForm();

		/* Valor por defecto, empieza chekeado. */
		ckTodoUnTicket.setSelected(true);

		/* Eventos para impedir tener los dos check seleccionados. */
		/* ======================================================= */
		ckTodoUnTicket.selectedProperty().addListener(new ChangeListener<Boolean>(){

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					todo = true;
					uno = false;
					ckUnoPorLinea.setSelected(false);
				}
				else {
					todo = false;
				}
			}
		});

		ckUnoPorLinea.selectedProperty().addListener(new ChangeListener<Boolean>(){

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					todo = false;
					uno = true;
					ckTodoUnTicket.setSelected(false);
				}
				else {
					uno = false;
				}
			}
		});
		/* ======================================================= */

		/* Iniciamos el texto con el número de copias por defecto. */
		txNumeroCopias.setText("1");

		/* Impedimos que se pueda escribir otra cosa que no sea números. */
		txNumeroCopias.textProperty().addListener(new ChangeListener<String>(){

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					txNumeroCopias.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});

	}

	/**
	 * Realiza comprobaciones previas de las opciones inferiores que puede realizar el usuario.
	 */
	private Boolean comprobacionesPrevias() {

		Boolean todoOk = true;
		/* Comprobamos que alguna de las opciones estén seleccionadas. */
		if (!todo && !uno) {
			todoOk = false;
			VentanaDialogoComponent.crearVentanaError(I18N.getTexto("Debe seleccionar una de las opciones del panel inferior."), this.getStage());
		}

		/* Comprobamos que haya algo escrito. */
		if (txNumeroCopias.getText().equals("")) {
			todoOk = false;
			VentanaDialogoComponent.crearVentanaError(I18N.getTexto("Debe escribir una cantidad."), this.getStage());
		}
		else {
			/* Comprobamos que el número de copias no sea 0. */
			if (!txNumeroCopias.getText().equals("0")) {
				numCopias = Integer.parseInt(txNumeroCopias.getText());
			}
			else {
				todoOk = false;
				VentanaDialogoComponent.crearVentanaError(I18N.getTexto("No puede imprimir 0 copias."), this.getStage());
			}
		}

		return todoOk;

	}

	/**
	 * Acción aceptar para imprimir los tickets regalo.
	 */
	@SuppressWarnings("unchecked")
	@FXML
	public void accionAceptar() {

		boolean hayLineasSelec = false;
		
		/*
		 * En caso de algunas de las condiciones previas no se cumplan, no hace nada.
		 */
		if (comprobacionesPrevias()) {

			try {

				for (LineaTicketRegaloGui lineaGui : lineas) {
					if (lineaGui.isLineaSelec()) {
						LineaTicket lineaticket = (LineaTicket) ticketRegalo.getLinea(lineaGui.getIdLinea());
						if (!lineaticket.isImprimirTicketRegalo()) {
							lineaticket.setImprimirTicketRegalo(true);
							lineaticket.setCantidad(BigDecimal.ZERO);
						}

						BigDecimal cantidad = lineaticket.getCantidad();
						lineaticket.setCantidad(cantidad.add(BigDecimal.ONE));
						hayLineasSelec = true;
					}
				}

				if (hayLineasSelec) {
					String formatoTicketRegalo = sesion.getAplicacion().getDocumentos().getDocumento(ticketRegalo.getCabecera().getCodTipoDocumento()).getFormatoImpresionTicketRegalo();
					if (formatoTicketRegalo != null) {

						ticketRegalo.getCabecera().setFormatoImpresion(formatoTicketRegalo);
						/* Comprobamos la opción que haya seleccionado el usuario. */
						if (todo && !uno) {

							HashMap<String, Object> mapaParametrosTicket = new HashMap<String, Object>();
							mapaParametrosTicket.put("ticket", ticketRegalo);
							mapaParametrosTicket.put("urlQR", variablesServices.getVariableAsString("TPV.URL_VISOR_DOCUMENTOS"));

							try {

								for (int i = 0; i < numCopias; i++) {
									IPrinter printer = Dispositivos.getInstance().getImpresora1();
									if (printer != null && printer instanceof PoloniaFiscalPrinter) {
										try {
											((IFiscalPrinter) printer).imprimirTicketRegalo((TicketVentaAbono) ticketRegalo);
										}
										catch (Throwable e) {
											log.error("registrarTicket() - Error mientras registraba en la impresora fiscal: " + e.getMessage(), e);
										}
									}
									else {
										ServicioImpresion.imprimir(ticketRegalo.getCabecera().getFormatoImpresionTicketRegalo(), mapaParametrosTicket);
									}
								}

							}
							catch (DeviceException e) {
								VentanaDialogoComponent.crearVentanaError(getStage(), I18N.getTexto("Lo sentimos, ha ocurrido un error al imprimir."), e);
							}

						}
						else if (uno && !todo) {

							try {
								/* Listado que incluye las lineas del ticket. */
								List<LineaTicket> listLineas = ticketRegalo.getLineas();

								for (LineaTicket lineaEliminar : listLineas) {
									/*
									 * Creamos una nueva lista de lineas de ticket, y le ponemos solo una, para poder
									 * imprimir de uno en uno los tickets por cada linea.
									 */
									List<LineaTicket> listaLineasNueva = new ArrayList<LineaTicket>();
									/*
									 * Comprobamos que dicha linea tiene el check para la impresión, en caso de que no,
									 * no lo insertamos en la lista.
									 */
									if (lineaEliminar.isImprimirTicketRegalo()) {
										listaLineasNueva.add(lineaEliminar);
									}

									/*
									 * Salvamos la cantidad de articulos de este tipo que se tiene, para después poder
									 * repetir la impresión tantas veces, luego se pone la cantidad a 1 para que en el
									 * ticket no aparezca en cada uno la cantidad original.
									 */
									int cantidadImpresion = 1;
									if (lineaEliminar.getCantidad().intValue() > 1) {
										cantidadImpresion = lineaEliminar.getCantidad().intValue();
										lineaEliminar.setCantidad(new BigDecimal(1));
									}

									ticketRegalo.setLineas(listaLineasNueva);

									for (int i = 0; i < numCopias; i++) {
										HashMap<String, Object> mapaParametrosTicket = new HashMap<String, Object>();
										mapaParametrosTicket.put("ticket", ticketRegalo);
										mapaParametrosTicket.put("urlQR", variablesServices.getVariableAsString("TPV.URL_VISOR_DOCUMENTOS"));
										for (int j = 1; j <= cantidadImpresion; j++) {
											/*
											 * Comprobamos que el ticket tiene lineas, para no imprimir tickets vacios.
											 */
											if (!ticketRegalo.getLineas().isEmpty()) {
												IPrinter printer = Dispositivos.getInstance().getImpresora1();
												if (printer != null && printer instanceof PoloniaFiscalPrinter) {
													try {
														((IFiscalPrinter) printer).imprimirTicketRegalo((TicketVentaAbono) ticketRegalo);
													}
													catch (Throwable e) {
														log.error("registrarTicket() - Error mientras registraba en la impresora fiscal: " + e.getMessage(), e);
													}
												}
												else {
													ServicioImpresion.imprimir(ticketRegalo.getCabecera().getFormatoImpresionTicketRegalo(), mapaParametrosTicket);
												}
											}
										}
									}

								}

							}
							catch (DeviceException e) {
								VentanaDialogoComponent.crearVentanaError(getStage(), I18N.getTexto("Lo sentimos, ha ocurrido un error al imprimir."), e);
							}

						}

					}
					else {
						VentanaDialogoComponent.crearVentanaError(I18N.getTexto("No existe un formato de ticket regalo para el tipo de documento del ticket original."), this.getStage());
					}
					getStage().close();
				}
				else {
					VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("No hay ninguna línea seleccionada."), getStage());
				}

			}
			catch (DocumentoException ex) {
				log.trace(I18N.getTexto("No se encontró el documento del ticket regalo."), ex);
			}

		}
	}

}
