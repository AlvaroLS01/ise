package com.comerzzia.bimbaylola.pos.gui.pagos.datoscliente;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Path;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.comerzzia.bimbaylola.pos.services.clientes.ByLClientesService;
import com.comerzzia.core.util.base.Estado;
import com.comerzzia.core.util.tipoidentificacion.IValidadorDocumentoIdentificacion;
import com.comerzzia.pos.core.gui.InitializeGuiException;
import com.comerzzia.pos.core.gui.POSApplication;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.core.gui.controllers.WindowController;
import com.comerzzia.pos.core.gui.validation.ValidationUI;
import com.comerzzia.pos.gui.mantenimientos.clientes.FormularioBusquedaCentralBean;
import com.comerzzia.pos.gui.ventas.tickets.TicketManager;
import com.comerzzia.pos.gui.ventas.tickets.articulos.FacturacionArticulosController;
import com.comerzzia.pos.gui.ventas.tickets.clientes.ConsultaClienteController;
import com.comerzzia.pos.gui.ventas.tickets.clientes.ConsultaClienteView;
import com.comerzzia.pos.gui.ventas.tickets.factura.FormularioDatosFacturaBean;
import com.comerzzia.pos.gui.ventas.tickets.factura.paises.PaisesController;
import com.comerzzia.pos.gui.ventas.tickets.factura.paises.PaisesView;
import com.comerzzia.pos.persistence.clientes.ClienteBean;
import com.comerzzia.pos.persistence.fidelizacion.FidelizacionBean;
import com.comerzzia.pos.persistence.paises.PaisBean;
import com.comerzzia.pos.persistence.tiposIdent.TiposIdentBean;
import com.comerzzia.pos.services.clientes.ClienteConstraintViolationException;
import com.comerzzia.pos.services.clientes.ClientesServiceException;
import com.comerzzia.pos.services.core.paises.PaisNotFoundException;
import com.comerzzia.pos.services.core.paises.PaisService;
import com.comerzzia.pos.services.core.paises.PaisServiceException;
import com.comerzzia.pos.services.core.sesion.Sesion;
import com.comerzzia.pos.services.core.tiposIdent.TiposIdentNotFoundException;
import com.comerzzia.pos.services.core.tiposIdent.TiposIdentService;
import com.comerzzia.pos.services.core.tiposIdent.TiposIdentServiceException;
import com.comerzzia.pos.util.config.AppConfig;
import com.comerzzia.pos.util.config.SpringContext;
import com.comerzzia.pos.util.i18n.I18N;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;

@Component
public class ByLCambiarDatosClientePTController extends WindowController {

	protected final Logger log = Logger.getLogger(getClass());

	@FXML
	protected TextField tfCodCliente, tfDesCliente, tfRazonSocial, tfDomicilio, tfPoblacion, tfProvincia, tfLocalidad, tfCP, tfNumDocIdent, tfTelefono, tfCodPais, tfDesPais;
	@FXML
	protected TextField tfBanco, tfBancoDomicilio, tfBancoPoblacion, tfBancoCCC;

	@FXML
	protected TabPane tabPane;
	@FXML
	protected Tab tabBanco;
	@FXML
	protected Tab tabGeneral;

	@FXML
	protected Label lbTitulo, lbError;

	@FXML
	protected ComboBox<TiposIdentBean> cbTipoDocIdent;

	protected ClienteBean clienteTicket;

	protected TicketManager ticketManager;

	protected ObservableList<TiposIdentBean> tiposIdent;

	protected FormularioDatosFacturaBean formulario;
	protected FormularioBusquedaCentralBean frBusquedaCentral;

	@FXML
	protected Label lbRazonSocial;

	@FXML
	protected FlowPane panelCodCliente, panelRazonSocial;

	@FXML
	protected Button btBuscar, btBusquedaCentral;

	@Autowired
	private Sesion sesion;

	@Autowired
	private TiposIdentService tiposIdentService;

	@Autowired
	private PaisService paisService;
	
	@Autowired
	private ByLClientesService clientesService;

	protected boolean isClienteGenerico;

	protected String codPais;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		formulario = SpringContext.getBean(FormularioDatosFacturaBean.class);
		lbRazonSocial.setText(I18N.getTexto("Nombre"));
		formulario.setFormField("desCli", tfDesCliente);
		formulario.setFormField("razonSocial", tfRazonSocial);
		formulario.setFormField("domicilio", tfDomicilio);
		formulario.setFormField("poblacion", tfPoblacion);
		formulario.setFormField("provincia", tfProvincia);
		formulario.setFormField("localidad", tfLocalidad);
		formulario.setFormField("codigoPostal", tfCP);
		formulario.setFormField("numDocIdent", tfNumDocIdent);
		formulario.setFormField("telefono", tfTelefono);
		formulario.setFormField("pais", tfCodPais);

		formulario.setFormField("banco", tfBanco);
		formulario.setFormField("bancoDomicilio", tfBancoDomicilio);
		formulario.setFormField("bancoPoblacion", tfBancoPoblacion);
		formulario.setFormField("bancoCCC", tfBancoCCC);
	}

	@Override
	public void initializeComponents() throws InitializeGuiException {
		panelCodCliente.setVisible(false);
		panelCodCliente.setManaged(false);

		tfCodPais.focusedProperty().addListener(new ChangeListener<Boolean>(){

			@Override
			public void changed(ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) {
				String codPais = tfCodPais.getText();
				if (oldValue && !codPais.equals(ByLCambiarDatosClientePTController.this.codPais)) {

					if (!codPais.trim().isEmpty()) {
						PaisBean pais = obtenerPais(codPais);
						if (pais != null) {
							tfCodPais.setText(tfCodPais.getText().toUpperCase());
							tfDesPais.setText(pais.getDesPais());
						}
						else {
							tfDesPais.setText("");
						}
					}
					else {
						tfDesPais.setText("");
					}
					ByLCambiarDatosClientePTController.this.codPais = codPais.toUpperCase();
				}
			}
		});

		registrarAccionCerrarVentanaEscape();
	}

	@Override
	public void initializeForm() throws InitializeGuiException {
		formulario.clearErrorStyle();
		lbError.setText("");

		ticketManager = (TicketManager) getDatos().get(FacturacionArticulosController.TICKET_KEY);
		setTitulo();

		getCliente();

		isClienteGenerico = ObjectUtils.equals(clienteTicket.getCodCliente(), sesion.getAplicacion().getTienda().getCliente().getCodCliente())
		        && clienteTicket.getDesCliente().equals(sesion.getAplicacion().getTienda().getCliente().getDesCliente());

		String codPais = clienteTicket.getCodpais();
		if (codPais != null && !codPais.isEmpty()) {
			PaisBean paisCliente = obtenerPais(codPais);
			if (paisCliente != null) {
				tfCodPais.setText(paisCliente.getCodPais().toUpperCase());
				this.codPais = paisCliente.getCodPais().toUpperCase();
				clienteTicket.setPais(paisCliente.getDesPais());
			}
		}
		else {
			tfCodPais.setText(AppConfig.pais);
			this.codPais = AppConfig.pais.toUpperCase();
			PaisBean pais = obtenerPais(AppConfig.pais);
			tfDesPais.setText(pais.getDesPais());
		}

		tiposIdent = FXCollections.observableArrayList();
		loadTiposIdentificacion();
		cbTipoDocIdent.setItems(tiposIdent);

		// Escondemos el botÃ³n de bÃºsqueda en la central ya que solo se usarÃ¡ en la pantalla de datos de factura
		btBusquedaCentral.setVisible(false);

		refrescarDatosPantalla();
	}

	protected void loadTiposIdentificacion() {
		try {
			tiposIdent.clear();

			String codPais = sesion.getAplicacion().getTienda().getCliente().getCodpais();
			List<TiposIdentBean> tiposIdentificacion = tiposIdentService.consultarTiposIdent(null, true, codPais);
			if (tiposIdentificacion.isEmpty()) {
				// Añadimos elemento vacío
				tiposIdent.add(new TiposIdentBean());
			}
			else {
				tiposIdent.addAll(tiposIdentificacion);
			}
		}
		catch (TiposIdentNotFoundException ex) {
		}
		catch (TiposIdentServiceException ex) {
			log.error("Error consultando los tipos de identificación.", ex);
			VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("Error consultando los documentos de identificación de la tienda."), this.getStage());
		}
		catch (Exception ex) {
			log.error("Se produjo un error en el tratamiento de los tipos de identificacion", ex);
		}
		updateSelectedTipoIdentificacion();
	}

	@Override
	public void initializeFocus() {
		tfRazonSocial.requestFocus();
	}

	public void refrescarDatosPantalla() {
		limpiarCampos();

		tfProvincia.setText(clienteTicket.getProvincia());
		tfLocalidad.setText(clienteTicket.getLocalidad());
		tfCP.setText(clienteTicket.getCp());
		tfCodPais.setText(clienteTicket.getCodpais());
		codPais = clienteTicket.getCodpais() != null ? clienteTicket.getCodpais().toUpperCase() : "";
		tfDesPais.setText(clienteTicket.getPais());

		if (!isClienteGenerico) {

			tfPoblacion.setText(clienteTicket.getPoblacion());
			tfCodCliente.setText(clienteTicket.getCodCliente());
			tfDesCliente.setText(clienteTicket.getDesCliente());
			tfRazonSocial.setText(clienteTicket.getDesCliente());// (clienteTicket.getNombreComercial());
			tfDomicilio.setText(clienteTicket.getDomicilio());
			tfTelefono.setText(clienteTicket.getTelefono1());
			tfNumDocIdent.setText(clienteTicket.getCif());
			updateSelectedTipoIdentificacion();

			tfBanco.setText(clienteTicket.getBanco());
			tfBancoDomicilio.setText(clienteTicket.getBancoDomicilio());
			tfBancoPoblacion.setText(clienteTicket.getBancoPoblacion());
			tfBancoCCC.setText(clienteTicket.getCcc());
		}

		FidelizacionBean fidelizado = ticketManager.getTicket().getCabecera().getDatosFidelizado();
		if (fidelizado != null && isClienteGenerico) {
			tfRazonSocial.setText(fidelizado.getNombre() + " " + fidelizado.getApellido());
			tfRazonSocial.setEditable(false);
			tfProvincia.setText(fidelizado.getProvincia());
			tfDomicilio.setText(fidelizado.getDomicilio());
			tfLocalidad.setText(fidelizado.getLocalidad());
			tfCodPais.setText(fidelizado.getCodPais());
			codPais = fidelizado.getCodPais() != null ? fidelizado.getCodPais().toUpperCase() : "";
			tfDesPais.setText(fidelizado.getDesPais());
			tfPoblacion.setText(fidelizado.getPoblacion());
			tfCP.setText(fidelizado.getCp());
			TiposIdentBean tipoIdentSeleccionado = null;
			for (TiposIdentBean tipoIdent : tiposIdent) {
				if (tipoIdent != null && tipoIdent.getCodTipoIden() != null && tipoIdent.getCodTipoIden().equals(fidelizado.getCodTipoIden())) {
					tipoIdentSeleccionado = tipoIdent;
				}
			}
			if (tipoIdentSeleccionado != null) {
				cbTipoDocIdent.getSelectionModel().select(tipoIdentSeleccionado);
			}
			if (fidelizado.getCodTipoIden() != null && !fidelizado.getCodTipoIden().isEmpty()) {
				tipoIdentSeleccionado = null;
				for (TiposIdentBean tipoIdent : tiposIdent) {
					if (tipoIdent != null && tipoIdent.getCodTipoIden() != null && tipoIdent.getCodTipoIden().equals(fidelizado.getCodTipoIden())) {
						tipoIdentSeleccionado = tipoIdent;
					}
				}
				cbTipoDocIdent.getSelectionModel().select(tipoIdentSeleccionado);
			}
			tfNumDocIdent.setText(fidelizado.getDocumento());
		}
	}

	protected void updateSelectedTipoIdentificacion() {
		if (clienteTicket != null) {
			updateSelectedTipoIdentificacion(clienteTicket.getTipoIdentificacion());
		}
		else {
			cbTipoDocIdent.getSelectionModel().clearSelection();
		}
	}

	protected void updateSelectedTipoIdentificacion(String selectedTipoIdent) {
		cbTipoDocIdent.getSelectionModel().clearSelection();

		if (tiposIdent == null) {
			return;
		}

		TiposIdentBean tipoIdentSeleccionado = null;
		if (selectedTipoIdent != null) {
			for (TiposIdentBean tipoIdent : tiposIdent) {
				if (tipoIdent != null && tipoIdent.getCodTipoIden() != null && tipoIdent.getCodTipoIden().equals(selectedTipoIdent)) {
					tipoIdentSeleccionado = tipoIdent;
				}
			}
		}
		if (tipoIdentSeleccionado != null) {
			cbTipoDocIdent.getSelectionModel().select(tipoIdentSeleccionado);
		}
		else {
			cbTipoDocIdent.getSelectionModel().select(0);
		}
	}

	protected void limpiarCampos() {
		tfRazonSocial.setText("");
		tfProvincia.setText("");
		tfDomicilio.setText("");
		tfLocalidad.setText("");
		tfTelefono.setText("");
		tfNumDocIdent.setText("");
		tfCodPais.setText("");
		codPais = "";
		tfDesPais.setText("");
		tfPoblacion.setText("");
		tfCP.setText("");
		updateSelectedTipoIdentificacion();

		tfBanco.setText("");
		tfBancoDomicilio.setText("");
		tfBancoPoblacion.setText("");
		tfBancoCCC.setText("");
	}

	public void accionCancelar() {
		log.debug("cancelar() - Cancelar");

		POSApplication.getInstance().activarTimer();
		getStage().close();
	}

	public void accionAceptar() {
		if (validarFormulario()) {
			clienteTicket.setCodpais(tfCodPais.getText());
			clienteTicket.setCif(tfNumDocIdent.getText());
			clienteTicket.setCp(tfCP.getText());
			clienteTicket.setDomicilio(tfDomicilio.getText());
			clienteTicket.setPoblacion(tfPoblacion.getText());
			clienteTicket.setLocalidad(tfLocalidad.getText());
			clienteTicket.setTelefono1(tfTelefono.getText());
			clienteTicket.setPais(tfDesPais.getText());
			if (cbTipoDocIdent.getSelectionModel().getSelectedItem() != null) {
				clienteTicket.setTipoIdentificacion(cbTipoDocIdent.getSelectionModel().getSelectedItem().getCodTipoIden());
			}

			clienteTicket.setDesCliente(tfRazonSocial.getText());// .setNombreComercial(tfRazonSocial.getText());
			clienteTicket.setProvincia(tfProvincia.getText());

			clienteTicket.setBanco(tfBanco.getText());
			clienteTicket.setBancoDomicilio(tfBancoDomicilio.getText());
			clienteTicket.setBancoPoblacion(tfBancoPoblacion.getText());
			clienteTicket.setCcc(tfBancoCCC.getText());

			registrarCliente();
			
			getStage().close();
		}
	}

	@FXML
	public void accionBuscarPais() {
		getApplication().getMainView().showModalCentered(PaisesView.class, getDatos(), this.getStage());

		if (getDatos() != null && getDatos().containsKey(PaisesController.PARAMETRO_SALIDA_PAIS)) {
			PaisBean pais = (PaisBean) getDatos().get(PaisesController.PARAMETRO_SALIDA_PAIS);
			tfDesPais.setText(pais.getDesPais());
			tfCodPais.setText(pais.getCodPais());
			codPais = pais.getCodPais().toUpperCase();
		}
	}

	@FXML
	public void accionBuscarCliente() {
		try {
			log.trace("accionBuscarCliente()");

			HashMap<String, Object> parametrosBusquedaCliente = new HashMap<>();
			parametrosBusquedaCliente.put(ConsultaClienteController.MODO_MODAL, true);
			getApplication().getMainView().showModal(ConsultaClienteView.class, parametrosBusquedaCliente);
			if (parametrosBusquedaCliente.containsKey(ConsultaClienteController.PARAMETRO_SALIDA_CLIENTE)) {
				setClienteConsultado((ClienteBean) parametrosBusquedaCliente.get(ConsultaClienteController.PARAMETRO_SALIDA_CLIENTE));
			}
		}
		catch (Exception ex) {
			log.error(ex.getLocalizedMessage(), ex);
			VentanaDialogoComponent.crearVentanaError(getStage(), ex);
		}
	}

	protected void setClienteConsultado(ClienteBean clienteSeleccionado) {
		this.clienteTicket = clienteSeleccionado;
		limpiarCampos();
		// Establecemos los campos en pantalla:
		if (clienteSeleccionado.getDesCliente() != null) {
			tfRazonSocial.setText(clienteSeleccionado.getDesCliente());
		}
		else {
			tfRazonSocial.setText("");
		}
		if (clienteSeleccionado.getCif() != null) {
			tfNumDocIdent.setText(clienteSeleccionado.getCif());
		}
		else {
			tfNumDocIdent.setText("");
		}
		if (clienteSeleccionado.getCp() != null) {
			tfCP.setText(clienteSeleccionado.getCp());
		}
		else {
			tfCP.setText("");
		}
		if (clienteSeleccionado.getDomicilio() != null) {
			tfDomicilio.setText(clienteSeleccionado.getDomicilio());
		}
		else {
			tfDomicilio.setText("");
		}
		if (clienteSeleccionado.getPoblacion() != null) {
			tfPoblacion.setText(clienteSeleccionado.getPoblacion());
		}
		else {
			tfPoblacion.setText("");
		}
		if (clienteSeleccionado.getLocalidad() != null) {
			tfLocalidad.setText(clienteSeleccionado.getLocalidad());
		}
		else {
			tfLocalidad.setText("");
		}
		if (clienteSeleccionado.getProvincia() != null) {
			tfProvincia.setText(clienteSeleccionado.getProvincia());
		}
		else {
			tfProvincia.setText("");
		}
		if (clienteSeleccionado.getTelefono1() != null) {
			tfTelefono.setText(clienteSeleccionado.getTelefono1());
		}
		else {
			tfTelefono.setText("");
		}
		if (clienteSeleccionado.getCodpais() != null) {
			tfCodPais.setText(clienteSeleccionado.getCodpais());
			codPais = clienteSeleccionado.getCodpais().toUpperCase();
		}
		else {
			tfCodPais.setText("");
			codPais = "";
		}
		if (clienteSeleccionado.getPais() != null) {
			tfDesPais.setText(clienteSeleccionado.getPais());
		}
		else if (!tfCodPais.getText().trim().isEmpty()) {
			PaisBean pais = obtenerPais(tfCodPais.getText());
			if (pais != null) {
				tfCodPais.setText(tfCodPais.getText().toUpperCase());
				codPais = tfCodPais.getText().toUpperCase();
				tfDesPais.setText(pais.getDesPais());
			}
		}
		else {
			tfDesPais.setText("");
		}
		loadTiposIdentificacion();

		if (clienteSeleccionado.getBanco() != null) {
			tfBanco.setText(clienteSeleccionado.getBanco());
		}
		else {
			tfBanco.setText("");
		}
		if (clienteSeleccionado.getBancoDomicilio() != null) {
			tfBancoDomicilio.setText(clienteSeleccionado.getBancoDomicilio());
		}
		else {
			tfBancoDomicilio.setText("");
		}
		if (clienteSeleccionado.getBanco() != null) {
			tfBancoPoblacion.setText(clienteSeleccionado.getBanco());
		}
		else {
			tfBancoPoblacion.setText("");
		}
		if (clienteSeleccionado.getCcc() != null) {
			tfBancoCCC.setText(clienteSeleccionado.getCcc());
		}
		else {
			tfBancoCCC.setText("");
		}
	}

	protected PaisBean obtenerPais(String codPais) {
		PaisBean pais = null;

		try {
			pais = paisService.consultarCodPais(codPais);
		}
		catch (PaisNotFoundException ex) {
			log.error("No se encontrÃ³ el cÃ³digo del cliente.");
			VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("Error en la bÃºsqueda, paÃ­s no encontrado"), getStage());
			tfCodPais.setText("");
			this.codPais = "";
			tfDesPais.setText("");
		}
		catch (PaisServiceException ex) {
			log.error("Error buscando el cÃ³digo del cliente.", ex);
			VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("Error en la bÃºsqueda del paÃ­s del cliente."), getStage());
			tfCodPais.setText("");
			this.codPais = "";
			tfDesPais.setText("");
		}

		return pais;
	}

	protected void getCliente() {
		clienteTicket = ticketManager.getTicket().getCliente();
		if (clienteTicket == null) {
			clienteTicket = new ClienteBean();
			clienteTicket.setIdGrupoImpuestos(sesion.getAplicacion().getTienda().getCliente().getIdGrupoImpuestos());
		}
	}

	protected void setTitulo() {
		lbTitulo.setText(I18N.getTexto("Datos del Cliente"));
	}

	protected boolean validarFormulario() {
		log.debug("validarFormulario()");

		String sResultado = validarDocumento();
		if (sResultado != null) {
			boolean guardar = VentanaDialogoComponent.crearVentanaConfirmacion(sResultado, getStage());
			if (!guardar) {
				tfNumDocIdent.requestFocus();
				return false;
			}
		}

		completarCamposObligatorios();

		formulario.setRazonSocial(formulario.trimTextField(tfRazonSocial));
		formulario.setDomicilio(formulario.trimTextField(tfDomicilio));
		formulario.setPoblacion(formulario.trimTextField(tfPoblacion));
		formulario.setProvincia(formulario.trimTextField(tfProvincia));
		formulario.setLocalidad(formulario.trimTextField(tfLocalidad));
		formulario.setcPostal(formulario.trimTextField(tfCP));
		formulario.setNumDocIdent(formulario.trimTextField(tfNumDocIdent));
		formulario.setTelefono(formulario.trimTextField(tfTelefono));
		formulario.setPais(formulario.trimTextField(tfCodPais));

		formulario.setBanco(formulario.trimTextField(tfBanco));
		formulario.setBancoDomicilio(formulario.trimTextField(tfBancoDomicilio));
		formulario.setBancoPoblacion(formulario.trimTextField(tfBancoPoblacion));
		formulario.setBancoCCC(formulario.trimTextField(tfBancoCCC));

		boolean valido = true;

		// Limpiamos los errores que pudiese tener el formulario
		formulario.clearErrorStyle();
		// Limpiamos el posible error anterior
		lbError.setText("");

		final Set<ConstraintViolation<FormularioDatosFacturaBean>> constraintViolations = ValidationUI.getInstance().getValidator().validate(formulario);
		Iterator<ConstraintViolation<FormularioDatosFacturaBean>> iterator = constraintViolations.iterator();
		while (iterator.hasNext()) {
			final ConstraintViolation<FormularioDatosFacturaBean> next = iterator.next();
			Path path = next.getPropertyPath();
			formulario.setErrorStyle(path, true);
			if (valido) { // Ponemos el foco en el primero que da error
				if (path.toString().toLowerCase().contains("banco")) {
					tabPane.getSelectionModel().select(tabBanco);
				}
				else {
					tabPane.getSelectionModel().select(tabGeneral);
				}
				lbError.setText(next.getMessage());
				Platform.runLater(new Runnable(){

					@Override
					public void run() {
						formulario.setFocus(next.getPropertyPath());
					}
				});
			}
			valido = false;
		}

		return valido;
	}

	public void cargarClienteCentral() {
	}

	public String validarDocumento() {
		TiposIdentBean tipoIden = cbTipoDocIdent.getSelectionModel().getSelectedItem();
		String sResultado = null;
		if (tipoIden != null && StringUtils.isNotBlank(tipoIden.getCodTipoIden()) && StringUtils.isNotBlank(tipoIden.getClaseValidacion()) && StringUtils.isNotBlank(tfNumDocIdent.getText())) {
			String claseValidacion = tipoIden.getClaseValidacion();
			String cif = tfNumDocIdent.getText();

			try {
				IValidadorDocumentoIdentificacion validadorDocumentoIdentificacion = (IValidadorDocumentoIdentificacion) Class.forName(claseValidacion).newInstance();
				if (!validadorDocumentoIdentificacion.validarDocumentoIdentificacion(cif)) {
					sResultado = I18N.getTexto("El documento indicado no es válido, ¿desea guardar el cliente de todas formas?");
				}
			}
			catch (Exception e) {
				sResultado = I18N.getTexto("Ha habido un error al intentar validar el documento, ¿desea guardar el cliente de todas formas?");
				log.error("validarDocumento() - Ha habido un error al intentar validar el documento: " + e.getMessage());
			}
		}
		return sResultado;
	}

	private void completarCamposObligatorios() {
		String domicilio = tfDomicilio.getText();
		if (StringUtils.isBlank(domicilio.trim())) {
			tfDomicilio.setText("DESCONHECIDO");
		}

		String nombre = tfRazonSocial.getText();
		if (StringUtils.isBlank(nombre.trim())) {
			tfRazonSocial.setText("Consumidor final");
		}
	}
	
	private void registrarCliente() {
		log.debug("registrarCliente() - Persistiendo cliente del ticketManager...");
		ClienteBean cliente = new ClienteBean();
		try {
			/* COMPROBAMOS SI YA EXISTE UN CLIENTE CON LOS DATOS DEL TICKET MANAGER, SI NO EXISTE LO CREAMOS  */
			List<ClienteBean> clientes = clientesService.consultarClientesCIF(clienteTicket.getCif());
			cliente = clientes.isEmpty() ? null : clientes.get(0);
			
			if(cliente == null) {
				cliente = clienteTicket;
				cliente.setActivo(Boolean.TRUE);
				cliente.setEstadoBean(Estado.NUEVO);
				cliente.setCodCliente(cliente.getCif());
				clientesService.salvar(cliente);
			}
			ticketManager.getTicket().setCliente(cliente);
		}
		catch (ClientesServiceException e) {
			log.error("tratarClienteSAFT() - Error creando al cliente " + cliente.getCodCliente() + " " + e.getMessage());
		}
		catch (ClienteConstraintViolationException e) {
			log.error("tratarClienteSAFT() - Error creando al cliente " + cliente.getCodCliente() + " " + e.getMessage());
		}
	}

}
