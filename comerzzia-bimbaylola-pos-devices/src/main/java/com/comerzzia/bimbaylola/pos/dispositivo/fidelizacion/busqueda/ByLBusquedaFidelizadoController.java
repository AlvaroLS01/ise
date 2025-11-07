package com.comerzzia.bimbaylola.pos.dispositivo.fidelizacion.busqueda;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import com.comerzzia.ByL.backoffice.persistencia.fidelizacion.fidelizados.FidelizadosBean;
import com.comerzzia.ByL.backoffice.rest.client.fidelizados.ByLFidelizadosRest;
import com.comerzzia.model.fidelizacion.fidelizados.FidelizadoBean;
import com.comerzzia.pos.core.gui.BackgroundTask;
import com.comerzzia.pos.core.gui.InitializeGuiException;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.core.gui.validation.ValidationUI;
import com.comerzzia.pos.dispositivo.fidelizacion.busqueda.BusquedaFidelizadoController;
import com.comerzzia.pos.dispositivo.fidelizacion.busqueda.FormularioBusquedaFidelizadoBean;
import com.comerzzia.pos.services.core.sesion.Sesion;
import com.comerzzia.pos.services.core.variables.VariablesServices;
import com.comerzzia.pos.util.config.AppConfig;
import com.comerzzia.pos.util.i18n.I18N;
import com.comerzzia.rest.client.exceptions.RestException;
import com.comerzzia.rest.client.exceptions.RestHttpException;
import com.comerzzia.rest.client.fidelizados.ConsultarFidelizadoRequestRest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

@Component
@Primary
public class ByLBusquedaFidelizadoController extends BusquedaFidelizadoController {

	private static final Logger log = Logger.getLogger(ByLBusquedaFidelizadoController.class);

	@Autowired
	private VariablesServices variablesServices;
	@Autowired
	private Sesion sesion;

	@FXML
	protected Button btnFidelizadoGenerico;

	private static final String FIDELIZADO_GENERICO = "FIDELIZACION.FIDELIZADO_GENERICO";
	public static final String PARAMETRO_DATOS_BUSQUEDA = "DATOS_BUSQUEDA";

	@Override
	public void initializeForm() throws InitializeGuiException {
		super.initializeForm();

		frBuscaFidelizado.limpiarFormulario();
	}

	@FXML
	public void accionAceptar() {
		log.debug("ByLBusquedaFidelizadoController/accionAceptar() - Iniciamos la Búsqueda de los Clientes...");

		if (StringUtils.isBlank(tfNumero.getText()) && StringUtils.isBlank(tfApellidos.getText()) && StringUtils.isBlank(tfNombre.getText()) && StringUtils.isBlank(tfTelefono.getText())
		        && StringUtils.isBlank(tfEmail.getText()) && StringUtils.isBlank(tfDocumento.getText())) {
			VentanaDialogoComponent.crearVentanaConfirmacionUnBoton(I18N.getTexto("Es necesario rellenar al menos uno de los campos de búsqueda"), getStage());
		}
		// INICIO GAP #1583
		else if (StringUtils.isNotBlank(tfNombre.getText()) && StringUtils.isBlank(tfNumero.getText()) && StringUtils.isBlank(tfDocumento.getText()) && StringUtils.isBlank(tfApellidos.getText())
		        && StringUtils.isBlank(tfTelefono.getText()) && StringUtils.isBlank(tfEmail.getText())
		        || StringUtils.isNotBlank(tfApellidos.getText()) && StringUtils.isBlank(tfNumero.getText()) && StringUtils.isBlank(tfDocumento.getText()) && StringUtils.isBlank(tfNombre.getText())
		                && StringUtils.isBlank(tfTelefono.getText()) && StringUtils.isBlank(tfEmail.getText())) {
			VentanaDialogoComponent.crearVentanaConfirmacionUnBoton(I18N.getTexto("Debe rellenar un criterio más para ejecutar la búsqueda de cliente"), getStage());
		}
		 // FIN GAP #1583
		else {
			/* Si el Código del Cliente esta relleno no se necesita recoger más datos aunque esten rellenos */
			if (StringUtils.isNotBlank(tfNumero.getText())) {
				frBuscaFidelizado.setCodTarjRegalo(tfNumero.getText());
			}
			else {
				frBuscaFidelizado.setApellidos(tfApellidos.getText());
				frBuscaFidelizado.setNombre(tfNombre.getText());
				frBuscaFidelizado.setTelefono(tfTelefono.getText());
				frBuscaFidelizado.setEmail(tfEmail.getText());
				frBuscaFidelizado.setDocumento(tfDocumento.getText());
			}

			/* Validamos los datos del formulario */
			Set<ConstraintViolation<FormularioBusquedaFidelizadoBean>> constraintViolations = ValidationUI.getInstance().getValidator().validate(frBuscaFidelizado);
			if (constraintViolations.size() >= 1) {
				ConstraintViolation<FormularioBusquedaFidelizadoBean> next = constraintViolations.iterator().next();
				frBuscaFidelizado.setErrorStyle(next.getPropertyPath(), true);
				frBuscaFidelizado.setFocus(next.getPropertyPath());
				VentanaDialogoComponent.crearVentanaError(next.getMessage(), this.getStage());
			}
			else {
				ConsultarFidelizadoRequestRest req = new ConsultarFidelizadoRequestRest(variablesServices.getVariableAsString(VariablesServices.WEBSERVICES_APIKEY),
				        sesion.getAplicacion().getUidActividad());
				if (StringUtils.isNotBlank(frBuscaFidelizado.getCodTarjRegalo())) {
					req.setNumeroTarjeta(frBuscaFidelizado.getCodTarjRegalo());
				}
				else {
					req.setApellidos(frBuscaFidelizado.getApellidos());
					req.setNombre(frBuscaFidelizado.getNombre());
					req.setTelefono(frBuscaFidelizado.getTelefono());
					req.setEmail(frBuscaFidelizado.getEmail());
					req.setDocumento(frBuscaFidelizado.getDocumento());
				}
				
				/* === BYL-113 GAP-01 Filtrar fidelizados por país === */
				//Se usará idTienda para codPais
				req.setIdTienda(sesion.getAplicacion().getTienda().getCliente().getCodpais());
				/* === FIN BYL-113 GAP-01 === */
				

				/* Enviamos la Tarjeta a la siguiente pantalla y realizamos la búsqueda */
				String tarjeta = "";
				if (StringUtils.isNotBlank(frBuscaFidelizado.getCodTarjRegalo())) {
					tarjeta = frBuscaFidelizado.getCodTarjRegalo();
					getDatos().put(PARAMETRO_NUM_TARJETA, tarjeta);
					getStage().close();
				}
				else {
					new BusquedaFidelizadoTask(tarjeta, req).start();
				}
			}
		}
	}

	public class BusquedaFidelizadoTask extends BackgroundTask<List<FidelizadosBean>> {

		String tarjeta;
		ConsultarFidelizadoRequestRest fidelizadoRequest;

		public BusquedaFidelizadoTask(String tarjeta, ConsultarFidelizadoRequestRest fidelizadoRequest) {
			super();
			this.tarjeta = tarjeta;
			this.fidelizadoRequest = fidelizadoRequest;
		}

		@Override
		public List<FidelizadosBean> call() throws Exception {
			return ByLFidelizadosRest.getFidelizadosDatos(fidelizadoRequest);
		}

		@Override
		public void succeeded() {
			List<FidelizadosBean> fidelizados = getValue();
			FidelizadoBean fidelizado = null;

			if (fidelizados.size() == 0) {
				tarjeta = null;
				if (VentanaDialogoComponent.crearVentanaConfirmacion(I18N.getTexto("No se ha encontrado ningún fidelizado con los datos introducidos" + "\n¿Desea realizar el Alta del Cliente?"), getStage())) {
					accionAltaFidelizado();
				}
			}
			else {
				if (fidelizados.size() == 1) {
					fidelizado = fidelizados.get(0);
					getStage().close();
				}
				else if (fidelizados.size() > 1) {
					getDatos().put(PARAMETRO_FIDELIZADOS_SELECCION, fidelizados);
					accionSeleccionarFidelizado();
					fidelizado = (FidelizadoBean) getDatos().get(PARAMETRO_FIDELIZADO_SELECCIONADO);
				}
				if (fidelizado != null) {
					tarjeta = fidelizado.getNumeroTarjeta();
					if (StringUtils.isBlank(tarjeta)) {
						String mensajeConfirmacion = "El Fidelizado no dispone de una tarjeta";
						log.debug("ByLBusquedaFidelizadoController/accionAceptar() - " + mensajeConfirmacion);
						VentanaDialogoComponent.crearVentanaConfirmacionUnBoton(I18N.getTexto("El Fidelizado no dispone de una tarjeta"), getStage());
					}
					else if (!StringUtils.isBlank(tarjeta)) {
						getDatos().put(PARAMETRO_NUM_TARJETA, tarjeta);
						getStage().close();
					}
				}
				else {
					if (!StringUtils.isBlank(tarjeta)) {
						getDatos().put(PARAMETRO_NUM_TARJETA, tarjeta);
						getStage().close();
					}
				}
			}
			log.debug("ByLBusquedaFidelizadoController/accionAceptar() - Finalizada la Búsqueda de los Clientes");
			super.succeeded();
		}

		@Override
		public void failed() {
			super.failed();
			Exception e = (Exception) getException();
			if (e instanceof RestException || e instanceof RestHttpException) {
				log.error("ByLBusquedaFidelizadoController/accionAceptar() - " + e.getMessage(), e);
				VentanaDialogoComponent.crearVentanaError(getStage(), I18N.getTexto(e.getMessage()), e);
			}
			else {
				String mensajeError = "Ha ocurrido un error durante la petición a la central, consulte con el administrador";
				log.error("ByLBusquedaFidelizadoController/accionAceptar() - " + mensajeError + " - " + e.getMessage(), e);
				VentanaDialogoComponent.crearVentanaError(getStage(), I18N.getTexto("Ha ocurrido un error durante la petición a la central, consulte con el administrador"), e);
			}
		}
	}

	public void accionFidelizadoGenerico() {
		String tarjeta = variablesServices.getVariableAsString(FIDELIZADO_GENERICO);
		if (StringUtils.isNotBlank(tarjeta)) {
			getDatos().put(PARAMETRO_NUM_TARJETA, tarjeta);
			getStage().close();
		}
		else {
			String mensajeAviso = "No se ha configurado el fidelizado genérico en este TPV";
			log.debug("accionFidelizadoGenerico() - " + mensajeAviso);
			VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("No se ha configurado el fidelizado genérico en este TPV"), getStage());
		}
	}

	@FXML
	public void accionAltaFidelizado() {
		getDatos().put(PARAMETRO_ID_FIDELIZADO, null);
		getDatos().put(PARAMETRO_MODO, "INSERCION");
		/*
		 * Si hemos seleccionado la opción de Alta, deberemos comprobar si tiene datos en el formulario de búsqueda y en
		 * ese caso los pasamos a la otra pantalla
		 */
		getDatos().put(PARAMETRO_DATOS_BUSQUEDA, cargarDatosAlta());
		getApplication().getMainView().showActionView(AppConfig.accionFidelizado, datos);
		String sinPermiso = getDatos().get("SIN_PERMISO") == null ? "" : (String) getDatos().get("SIN_PERMISO");
		if(StringUtils.isBlank(sinPermiso)) {
			getStage().close();
		}
		getDatos().remove("SIN_PERMISO");
	}

	/**
	 * Realiza una carga de datos del formulario de búsqueda en un objeto para pasarlo al formulario de Alta en caso de
	 * pulsar el botón y tener datos escritos.
	 * 
	 * @return DatosBusquedaAltaFidelizado
	 */
	public DatosBusquedaAltaFidelizado cargarDatosAlta() {
		DatosBusquedaAltaFidelizado datosBusqueda = new DatosBusquedaAltaFidelizado();
		if (StringUtils.isNotBlank(tfNumero.getText())) {
			datosBusqueda.setCodigoCliente(tfNumero.getText());
		}
		if (StringUtils.isNotBlank(tfNombre.getText())) {
			datosBusqueda.setNombre(tfNombre.getText());
		}
		if (StringUtils.isNotBlank(tfApellidos.getText())) {
			datosBusqueda.setApellidos(tfApellidos.getText());
		}
		if (StringUtils.isNotBlank(tfDocumento.getText())) {
			datosBusqueda.setDocumentoFiscal(tfDocumento.getText());
		}
		if (StringUtils.isNotBlank(tfEmail.getText())) {
			datosBusqueda.setEmail(tfEmail.getText());
		}
		if (StringUtils.isNotBlank(tfTelefono.getText())) {
			datosBusqueda.setTelefono(tfTelefono.getText());
		}
		log.debug("cargarDatosAlta() - Datos del Cliente : " + datosBusqueda.toString());
		return datosBusqueda;
	}

	@Override
	public void initializeFocus() {
		tfTelefono.requestFocus();
	}
}
