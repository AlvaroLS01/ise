package com.comerzzia.bimbaylola.pos.gui.mantenimientos.fidelizados;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import com.comerzzia.ByL.backoffice.persistencia.fidelizacion.fidelizados.FidelizadosBean;
import com.comerzzia.ByL.backoffice.persistencia.fidelizacion.firma.FidelizadoFirmaBean;
import com.comerzzia.ByL.backoffice.rest.client.fidelizados.ByLFidelizadoRequestRest;
import com.comerzzia.ByL.backoffice.rest.client.fidelizados.ByLFidelizadosRest;
import com.comerzzia.bimbaylola.pos.dispositivo.fidelizacion.busqueda.ByLBusquedaFidelizadoController;
import com.comerzzia.bimbaylola.pos.dispositivo.fidelizacion.busqueda.DatosBusquedaAltaFidelizado;
import com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.axis.AxisManager;
import com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.axis.bean.AxisConsentimientoBean;
import com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.axis.exception.AxisResponseException;
import com.comerzzia.bimbaylola.pos.gui.ByLBackgroundTask;
import com.comerzzia.bimbaylola.pos.gui.componentes.dialogos.ByLVentanaDialogoComponent;
import com.comerzzia.bimbaylola.pos.gui.componentes.ventanaCarga.ByLVentanaEspera;
import com.comerzzia.bimbaylola.pos.gui.mantenimientos.fidelizados.datosgenerales.ByLPaneDatosGeneralesController;
import com.comerzzia.bimbaylola.pos.gui.mantenimientos.fidelizados.datosgenerales.ValidarDatosGeneralesException;
import com.comerzzia.bimbaylola.pos.gui.mantenimientos.fidelizados.direcciones.PaneDireccionesController;
import com.comerzzia.bimbaylola.pos.gui.mantenimientos.fidelizados.resumen.ByLPaneResumenFidelizadoController;
import com.comerzzia.bimbaylola.pos.gui.ventas.profesional.venta.ByLVentaProfesionalController;
import com.comerzzia.bimbaylola.pos.persistence.fidelizacion.ByLFidelizacionBean;
import com.comerzzia.bimbaylola.pos.services.clientes.ByLClientesService;
import com.comerzzia.bimbaylola.pos.services.clientes.ClienteValidatedException;
import com.comerzzia.bimbaylola.pos.services.dispositivofirma.ByLDispositivosFirma;
import com.comerzzia.bimbaylola.pos.services.dispositivofirma.DispositivoFirmaException;
import com.comerzzia.bimbaylola.pos.services.dispositivofirma.IFirma;
import com.comerzzia.core.util.base.Estado;
import com.comerzzia.model.fidelizacion.fidelizados.FidelizadoBean;
import com.comerzzia.model.fidelizacion.fidelizados.contactos.TiposContactoFidelizadoBean;
import com.comerzzia.model.fidelizacion.fidelizados.direcciones.DireccionFidelizadoBean;
import com.comerzzia.model.fidelizacion.tarjetas.TarjetaBean;
import com.comerzzia.model.general.tiendas.TiendaBean;
import com.comerzzia.pos.core.dispositivos.Dispositivos;
import com.comerzzia.pos.core.dispositivos.dispositivo.DispositivoException;
import com.comerzzia.pos.core.dispositivos.dispositivo.fidelizacion.ConsultaTarjetaFidelizadoException;
import com.comerzzia.pos.core.gui.BackgroundTask;
import com.comerzzia.pos.core.gui.InitializeGuiException;
import com.comerzzia.pos.core.gui.POSApplication;
import com.comerzzia.pos.core.gui.RestBackgroundTask;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.core.gui.permisos.exception.SinPermisosException;
import com.comerzzia.pos.core.gui.view.View;
import com.comerzzia.pos.dispositivo.comun.tarjeta.CodigoTarjetaController;
import com.comerzzia.pos.gui.mantenimientos.fidelizados.ConsultarTodasTiendasTask;
import com.comerzzia.pos.gui.mantenimientos.fidelizados.FidelizadoController;
import com.comerzzia.pos.gui.mantenimientos.fidelizados.tiendas.TiendaGui;
import com.comerzzia.pos.gui.ventas.profesional.venta.VentaProfesionalView;
import com.comerzzia.pos.gui.ventas.tickets.articulos.FacturacionArticulosController;
import com.comerzzia.pos.gui.ventas.tickets.articulos.FacturacionArticulosView;
import com.comerzzia.pos.persistence.paises.PaisBean;
import com.comerzzia.pos.services.core.paises.PaisNotFoundException;
import com.comerzzia.pos.services.core.paises.PaisService;
import com.comerzzia.pos.services.core.paises.PaisServiceException;
import com.comerzzia.pos.services.core.sesion.Sesion;
import com.comerzzia.pos.services.core.variables.VariablesServices;
import com.comerzzia.pos.services.devices.DeviceException;
import com.comerzzia.pos.servicios.impresion.ImpresionJasper;
import com.comerzzia.pos.servicios.impresion.ServicioImpresion;
import com.comerzzia.pos.util.config.SpringContext;
import com.comerzzia.pos.util.i18n.I18N;
import com.comerzzia.rest.ClientBuilder;
import com.comerzzia.rest.client.exceptions.RestException;
import com.comerzzia.rest.client.exceptions.RestHttpException;
import com.comerzzia.rest.client.general.tiendas.ConsultarTiendaRequestRest;
import com.google.gson.Gson;
import com.ingenico.fr.jc3api.JC3ApiC3Rspn;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.glassfish.jersey.client.ClientProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;

@Component
@Primary
public class ByLFidelizadoController<V> extends FidelizadoController {

	public static final String MOVIL = "MOVIL";
	public static final String MOVIL_NOTIF = "MOVIL_NOTIF";
	public static final String EMAIL = "EMAIL";
	public static final String EMAIL_NOTIF = "EMAIL_NOTIF";
	public static final String MODO_CONSULTA = "CONSULTA";
	public static final String MODO_EDICION = "EDICION";
	public static final String MODO_INSERCION = "INSERCION";

	public static final String FIDELIZACION_URL_REGISTRO_LOCAL = "FIDELIZACION.URL_REGISTRO_LOCAL";

	public static final String servicio_insertFidelizadoLocal = "/insertFidelizadoLocal";
	public static final String servicio_updateIdFidelizadoLocal = "/updateIDFidelizadoLocal";
	public static final String servicio_updateFidelizadoLocal = "/updateFidelizadoLocal";
	
	public static final String USA="US";
	public static final String PUERTO_RICO="PR";

	private static final Logger log = Logger.getLogger(ByLFidelizadoController.class);

	@Autowired
	private Sesion sesion;

	@Autowired
	private ByLClientesService clienteService;
	@Autowired
	private PaisService paisService = SpringContext.getBean(PaisService.class);
	@Autowired
	protected VariablesServices variablesServices;

	protected FidelizadosBean fidelizado;
	protected Map<String, String> consentimiento = new HashMap<String, String>();
	protected byte[] firma;
	protected String modo;

	/* Almacena las direcciones temporalmente en las inseciones y ediciones */
	protected List<DireccionFidelizadoBean> direcciones;

	@FXML
	protected ByLPaneDatosGeneralesController paneDatosGeneralesController;
	@FXML
	protected PaneDireccionesController paneDireccionController;
	@FXML
	protected AnchorPane paneDirecciones;
	@FXML
	protected Tab tabDirecciones;
	@FXML
	protected Button btConsentimientoFirma;
	@FXML
	protected Button btImprimirDocFidelizado;

	public FidelizadosBean getFidelizado() {
		return fidelizado;
	}

	public void setFidelizado(FidelizadosBean fidelizado) {
		this.fidelizado = fidelizado;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		paneDatosGeneralesController = SpringContext.getBean(ByLPaneDatosGeneralesController.class);
		paneDireccionController = SpringContext.getBean(PaneDireccionesController.class);
		paneResumenFidelizado = SpringContext.getBean(ByLPaneResumenFidelizadoController.class);

		lblCampoObligatorios.setAlignment(Pos.BASELINE_LEFT);
		lbError.setAlignment(Pos.BASELINE_CENTER);
	}

	@Override
	public void initializeComponents() throws InitializeGuiException {
		tpDatosFidelizado.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {

			@Override
			public void changed(ObservableValue<? extends Tab> arg0, Tab arg1, Tab tab) {
				if (tab.getContent().equals(paneDatosGenerales)) {
					paneDatosGeneralesController.selected();
				} else if (tab.getContent().equals(paneResumenFidelizado)) {
					paneResumenFidelizadoController.selected();
				} else if (tab.getContent().equals(paneDirecciones)) {
					paneDireccionController.selected(Boolean.TRUE);
				}
			}
		});
		consultarFiltroTiendas();		
	}

	@Override
	public void initializeForm() throws InitializeGuiException {
		modo = (String) getDatos().get(CodigoTarjetaController.PARAMETRO_MODO);

		paneDireccionController.limpiarListadoDirecciones();
		paneDireccionController.limpiarFormulario();

		if (MODO_INSERCION.equals(modo)) {
			try {
				super.compruebaPermisos(PERMISO_ALTA);
			} catch (SinPermisosException ex) {
				log.error("initializeForm() - " + ex.getMessage(), ex);
				getDatos().put("SIN_PERMISO", "S");
				throw new InitializeGuiException(ex.getMessage(), ex);
			}
			paneDatosGeneralesController.limpiarFormulario();
			paneDatosGeneralesController.selected();
			paneDireccionController.limpiarFormulario();
			paneDireccionController.activarBotoneraDirecciones(Boolean.TRUE);
			setModo(modo);
			lblCampoObligatorios.setVisible(true);
			tabResumen.setDisable(true);
			tabDirecciones.setDisable(false);
			numeroTarjetaFidelizado = null;
			tpDatosFidelizado.getSelectionModel().select(tabDatosGenerales);
			btCancelar.setVisible(false);
			btCancelar.setManaged(false);
			btCerrar.setVisible(true);
			btCerrar.setManaged(true);
			btAceptar.setVisible(true);
			btAceptar.setManaged(true);
			btAceptar.setDisable(false);
			btEditar.setVisible(false);
			btEditar.setManaged(false);
			/* Comprobamos si vienen datos desde la búsqueda, y en ese caso los cargamos */
			cargarDatosBusqueda();
			/* Imprimir solo visible para modo Consulta */
			activarImprimir(Boolean.FALSE);
			/* Limpiamos las direcciones anteriores */
			if (direcciones != null && !direcciones.isEmpty()) {
				direcciones.clear();
			}
		} else if (MODO_CONSULTA.equals(modo)) {
			setModo(MODO_CONSULTA);
			lblCampoObligatorios.setVisible(false);
			tabResumen.setDisable(false);
			tabDirecciones.setDisable(false);
			btCancelar.setVisible(true);
			btCancelar.setManaged(true);
			btCerrar.setVisible(false);
			btCerrar.setManaged(false);
			btAceptar.setManaged(false);
			btAceptar.setVisible(false);
			btAceptar.setDisable(false);
			btEditar.setVisible(true);
			btEditar.setManaged(true);
			Long idFidelizado = (Long) getDatos().get(CodigoTarjetaController.PARAMETRO_ID_FIDELIZADO);
			String numeroTarjeta = (String) getDatos().get(FacturacionArticulosController.PARAMETRO_NUMERO_TARJETA);
			numeroTarjetaFidelizado = numeroTarjeta;
			cargarFidelizado(idFidelizado);
			/* Imprimir solo visible para modo Consulta */
			activarImprimir(Boolean.TRUE);
			
		}
		else if(MODO_EDICION.equals(modo)) {	
			cargarFidelizado(Long.parseLong(getDatos().get(CodigoTarjetaController.PARAMETRO_ID_FIDELIZADO).toString()));
		}
		
		/* Desactivamos el botón de confirmar cambios */
		paneDireccionController.activarConfirmarCambios(Boolean.TRUE);
		/* Controlamos cuando mostrar el botón de consentimiento y firma */
		if (MODO_INSERCION.equals(modo) || MODO_EDICION.equals(modo)) {
			btConsentimientoFirma.setVisible(Boolean.TRUE);
			btConsentimientoFirma.setManaged(Boolean.TRUE);
		} else {
			btConsentimientoFirma.setVisible(Boolean.FALSE);
			btConsentimientoFirma.setManaged(Boolean.FALSE);
		}
		
	}

	/**
	 * Activa o desactiva el botón de imprimir.
	 * 
	 * @param activar : Indica si se activa el botón o no.
	 */
	public void activarImprimir(boolean activar) {
		btImprimir.setVisible(activar);
		btImprimir.setManaged(activar);

		btImprimirDocFidelizado.setVisible(activar);
	}
	
	@FXML
	public void accionEditar() {
		/*No se podra editar un fidelizado que tenga algun colectivo asociado*/
		if (fidelizado.getColectivos() == null || fidelizado.getColectivos().size() < 1) {
			log.debug("accionEditar()-El fidelizado con codigo " + fidelizado.getCodFidelizado() + " no tiene colectivos asociados");
			
			btAceptar.setVisible(true);
			btAceptar.setManaged(true);
			btCancelar.setVisible(true);
			btCancelar.setManaged(true);
			btEditar.setVisible(false);
			btEditar.setManaged(false);
			btCerrar.setVisible(false);
			btCerrar.setManaged(false);
			tabResumen.setDisable(true);
			lblCampoObligatorios.setVisible(true);
			btConsentimientoFirma.setVisible(Boolean.TRUE);
			btConsentimientoFirma.setManaged(Boolean.TRUE);
			setModo(MODO_EDICION);

			tpDatosFidelizado.getSelectionModel().select(tabDatosGenerales);
			paneDatosGeneralesController.selected();
			paneDireccionController.selected(Boolean.FALSE);

			/* Imprimir solo visible para modo Consulta */
			activarImprimir(Boolean.FALSE);
			/* Activamos la botonera de direcciones */
			paneDireccionController.activarBotoneraDirecciones(Boolean.TRUE);
		}
		else {
			log.debug("accionEditar()-El fidelizado con codigo " + fidelizado.getCodFidelizado() + " tiene colectivos asociados y no se puede editar");
			VentanaDialogoComponent.crearVentanaInfo("No se puede editar un Fidelizado con colectivo asociado", getStage());
		}
	}

	public void habilitarBotonEditar(boolean habilitado) {
		btAceptar.setDisable(!habilitado);
	}

	/**
	 * Acción de aceptar el formulario, realizamos comprobaciones previas para
	 * comprobar que el documento está correctamente.
	 */
	@FXML
	public void accionAceptar() {
		try {
			if (paneDatosGeneralesController.getCbTipoDocumento().getSelectionModel().getSelectedItem() != null) {
				if (clienteService.validarDocumento(
						paneDatosGeneralesController.getCbTipoDocumento().getSelectionModel().getSelectedItem()
								.getClaseValidacion(),
						paneDatosGeneralesController.getCbTipoDocumento().getSelectionModel().getSelectedItem()
								.getCodigo(),
						paneDatosGeneralesController.getTfCodPais().getText(),
						paneDatosGeneralesController.getTfDocumento().getText())) {
					realizarAccionAceptar();
				}
			} else {
				realizarAccionAceptar();
			}
		} catch (ClienteValidatedException e) {
			log.error("ByLFidelizadoController/accionAceptar() - " + e.getMessage(), e);
			VentanaDialogoComponent.crearVentanaAviso(e.getMessage(), getStage());
		}

	}

	public void realizarAccionAceptar() {
		if (MODO_INSERCION.equals(getModo())) {
			crearFidelizado();
		} else if (MODO_EDICION.equals(getModo())) {
			editarFidelizado();
		}
	}

	/**
	 * Realiza el alta de un fidelizado.
	 */
	public void crearFidelizado() {
		log.debug("crearFidelizado() - Iniciamos la creación del Fidelizado...");

		String apiKey = variablesService.getVariableAsString(VariablesServices.WEBSERVICES_APIKEY);
		String uidActividad = sesion.getAplicacion().getUidActividad();

		FidelizadosBean fidelizadoBean = (FidelizadosBean) getDatosFidelizado(MODO_INSERCION);
		if (fidelizadoBean != null) {
			ByLFidelizadoRequestRest insertFidelizado = new ByLFidelizadoRequestRest(apiKey, uidActividad,
					fidelizadoBean, sesion.getAplicacion().getEmpresa().getCodEmpresa(),
					sesion.getAplicacion().getCodAlmacen());
			insertFidelizado.setTipoNotificacion("NUEVO_USUARIO_FIDELIZADO");

			new CrearFidelizadoTask(insertFidelizado).start();
		}
	}

	/**
	 * Clase Task para crear un Fidelizado.
	 * 
	 * @param fidelizadoRequest : Request con los datos del Fidelizado.
	 */
	public class CrearFidelizadoTask extends BackgroundTask<FidelizadosBean> {

		ByLFidelizadoRequestRest fidelizadoRequest;

		public CrearFidelizadoTask(ByLFidelizadoRequestRest fidelizadoRequest) {
			super();
			this.fidelizadoRequest = fidelizadoRequest;
		}

		@Override
		protected FidelizadosBean call() throws Exception {

			String urlFidelizadoLocal = variablesService.getVariableAsString(FIDELIZACION_URL_REGISTRO_LOCAL);
			if (StringUtils.isNotBlank(urlFidelizadoLocal)) {
				insertFidelizadoLocal(fidelizadoRequest, urlFidelizadoLocal);
			}
			return ByLFidelizadosRest.insertFidelizado(fidelizadoRequest);
		}

		@Override
		public void succeeded() {
			fidelizado = getValue();

			tabResumen.setDisable(false);
			paneResumenFidelizadoController.limpiarFormulario();
			paneResumenFidelizadoController.selected();
			tpDatosFidelizado.getSelectionModel().select(tabResumen);
			btAceptar.setVisible(false);
			btAceptar.setManaged(false);
			btCancelar.setVisible(false);
			btCancelar.setManaged(false);
			btEditar.setVisible(true);
			btEditar.setManaged(true);
			btImprimir.setVisible(true);
			btImprimir.setManaged(true);
			btConsentimientoFirma.setVisible(false);
			btConsentimientoFirma.setManaged(false);
			btCerrar.setVisible(true);
			btCerrar.setManaged(true);
			lblCampoObligatorios.setVisible(false);

			/* Imprimir solo visible para modo Consulta */
			activarImprimir(Boolean.TRUE);

			setModo(MODO_CONSULTA);
			String mensajeInfo = "Fidelizado creado correctamente";
			log.info("CrearFidelizadoTask/succeeded() - " + mensajeInfo);
			VentanaDialogoComponent.crearVentanaInfo(I18N.getTexto("Fidelizado creado correctamente"), getStage());

			ByLFidelizacionBean fidelizadoBean = asignarFidelizadoAVenta(fidelizado);

			String urlFidelizadoLocal = variablesService.getVariableAsString(FIDELIZACION_URL_REGISTRO_LOCAL);
			if (StringUtils.isNotBlank(urlFidelizadoLocal)) {
				fidelizadoRequest.setIdFidelizado(fidelizadoBean.getIdFidelizado());
				try {
					updateIDFidelizadoLocal(fidelizadoRequest, urlFidelizadoLocal);
				} catch (RestException | RestHttpException e) {
					log.error("CrearFidelizadoTask/failed() - Ha fallado la actualización del idFidelizado en local"
							+ e.getMessage(), e);
				}
			}
			
			POSApplication.getInstance().getMainView().close();
		}

		@Override
		public void failed() {
			super.failed();
			Exception e = (Exception) getException();
			log.error("CrearFidelizadoTask/failed() - " + e.getMessage(), e);
			VentanaDialogoComponent.crearVentanaError(getStage(), I18N.getTexto(e.getMessage()), e);
		}
	}

	/**
	 * Realiza un update a un fidelizado.
	 */
	public void editarFidelizado() {
		String apiKey = variablesService.getVariableAsString(VariablesServices.WEBSERVICES_APIKEY);
		String uidActividad = sesion.getAplicacion().getUidActividad();
		fidelizado = (FidelizadosBean) getDatosFidelizado(MODO_EDICION);

		if (fidelizado != null) {
			log.debug("editarFidelizado() - Iniciamos la edición del Fidelizado " + fidelizado.getIdFidelizado());

			ByLFidelizadoRequestRest updateFidelizado = new ByLFidelizadoRequestRest(apiKey, uidActividad, fidelizado);
			if (fidelizado.getTiposContacto() != null && !fidelizado.getTiposContacto().isEmpty()) {
				Map<String, Integer> mapaEstadosTiposContacto = new HashMap<String, Integer>();
				for (TiposContactoFidelizadoBean tipoContacto : fidelizado.getTiposContacto()) {
					if (tipoContacto.getEstadoBean() != Estado.SIN_MODIFICAR) {
						mapaEstadosTiposContacto.put(tipoContacto.getCodTipoCon(), tipoContacto.getEstadoBean());
					}
				}
				updateFidelizado.setMapaEstadosTiposContacto(mapaEstadosTiposContacto);
			}
			if (fidelizado.getCodAlm() != null) {
				updateFidelizado.setCodAlmFav(fidelizado.getCodAlm());
			}

			new EditarFidelizadoTask(updateFidelizado).start();
		}
	}

	/**
	 * Clase Task para editar un Fidelizado.
	 * 
	 * @param fidelizadoRequest : Request con los datos del Fidelizado.
	 */
	public class EditarFidelizadoTask extends BackgroundTask<FidelizadosBean> {

		ByLFidelizadoRequestRest fidelizadoRequest;

		public EditarFidelizadoTask(ByLFidelizadoRequestRest fidelizadoRequest) {
			super();
			this.fidelizadoRequest = fidelizadoRequest;
		}

		@Override
		protected FidelizadosBean call() throws Exception {

			String urlFidelizadoLocal = variablesService.getVariableAsString(FIDELIZACION_URL_REGISTRO_LOCAL);
			if (StringUtils.isNotBlank(urlFidelizadoLocal)) {
				updateFidelizadoLocal(fidelizadoRequest, urlFidelizadoLocal);
			}

			return ByLFidelizadosRest.updateFidelizado(fidelizadoRequest);
		}

		@Override
		public void succeeded() {
			fidelizado = getValue();

			setModo(MODO_CONSULTA);
			tabResumen.setDisable(false);
			paneResumenFidelizadoController.setFidelizado(null);
			paneResumenFidelizadoController.limpiarFormulario();
			paneResumenFidelizadoController.selected();
			tpDatosFidelizado.getSelectionModel().select(tabResumen);
			btAceptar.setVisible(false);
			btAceptar.setManaged(false);
			btCancelar.setVisible(false);
			btCancelar.setManaged(false);
			btEditar.setVisible(true);
			btEditar.setManaged(true);
			btCerrar.setVisible(true);
			btCerrar.setManaged(true);
			btConsentimientoFirma.setVisible(false);
			btConsentimientoFirma.setManaged(false);
			lblCampoObligatorios.setVisible(false);
			/* Imprimir solo visible para modo Consulta */
			activarImprimir(Boolean.TRUE);

			asignarFidelizadoAVenta(fidelizado);

			paneDireccionController.cargarDatosDirecciones(fidelizado);

			String mensajeInfo = "Fidelizado editado correctamente";
			log.debug("EditarFidelizadoTask/succeeded() - " + mensajeInfo);
			VentanaDialogoComponent.crearVentanaInfo(I18N.getTexto("Fidelizado editado correctamente"), getStage());
			super.succeeded();
		}

		@Override
		public void failed() {
			super.failed();
			Exception e = (Exception) getException();
			log.error("EditarFidelizadoTask/failed() - " + e.getMessage(), e);
			VentanaDialogoComponent.crearVentanaError(getStage(), I18N.getTexto(e.getMessage()), e);
		}
	}

	/**
	 * Carga desde BackOffice los datos de un Fidelizado
	 * 
	 * @param idFidelizado : ID del Fidelizado que se desea cargar.
	 */
	public void cargarFidelizado(Long idFidelizado) {
		log.debug("cargarFidelizado() - Iniciamos la carga de los datos del Fidelizado " + idFidelizado);

		String apiKey = variablesService.getVariableAsString(VariablesServices.WEBSERVICES_APIKEY);
		String uidActividad = sesion.getAplicacion().getUidActividad();
		ByLFidelizadoRequestRest consulta = new ByLFidelizadoRequestRest(apiKey, uidActividad, idFidelizado);
		
		new ConsultarFidelizadoPorIdTask(consulta).start();
	}

	/**
	 * Clase Task para la Consulta de un Fidelizado por su ID.
	 * 
	 * @param fidelizadoRequest : Request con los datos del Fidelizado.
	 */
	public class ConsultarFidelizadoPorIdTask extends BackgroundTask<FidelizadosBean> {

		ByLFidelizadoRequestRest fidelizadoRequest;

		public ConsultarFidelizadoPorIdTask(ByLFidelizadoRequestRest fidelizadoRequest) {
			super();
			this.fidelizadoRequest = fidelizadoRequest;
		}

		@Override
		public FidelizadosBean call() throws Exception {
			return ByLFidelizadosRest.getFidelizadoPorId(fidelizadoRequest);
		}

		@Override
		public void succeeded() {
			fidelizado = getValue();

			tpDatosFidelizado.getSelectionModel().select(0);
			/* Limpiamos los datos del anterior Fidelizado */
			paneDatosGeneralesController.limpiarFormulario();
			paneDireccionController.limpiarFormulario();
			paneResumenFidelizadoController.setFidelizado(null);
			paneResumenFidelizadoController.limpiarFormulario();
			paneResumenFidelizadoController.selected();
			btCancelar.setVisible(false);
			btCancelar.setManaged(false);
			btCerrar.setVisible(true);
			btCerrar.setManaged(true);
			btAceptar.setVisible(false);
			btAceptar.setManaged(false);
			btEditar.setVisible(true);
			btEditar.setManaged(true);
			btImprimir.setVisible(true);
			btImprimir.setManaged(true);

			paneDireccionController.cargarDatosDirecciones(fidelizado);

			/* Coloreamos los botones según vengan los datos del Cliente */
			paneDatosGeneralesController.coloresFirmaConsentimiento(consentimiento, firma);
			log.debug("ConsultarFidelizadoPorIdTask/succeeded() - Finalizada la carga de los datos del Fidelizado");
			
			if(MODO_EDICION.equals(modo)) {
				accionEditar();				
			}
			
			super.succeeded();
		}

		@Override
		public void failed() {
			super.failed();
			Exception e = (Exception) getException();
			log.error("ConsultarFidelizadoPorIdTask/failed() - " + e.getMessage(), e);
			VentanaDialogoComponent.crearVentanaError(I18N.getTexto(e.getMessage()), getStage());
		}
	}

	/**
	 * Carga los datos de pantalla de un fidelizado.
	 * 
	 * @param modo : Indica la acción que se esta realizando en este momento.
	 */
	public FidelizadosBean getDatosFidelizado(String modo) {
		/* Limpiamos los posibles errores previos */
		lbError.setText("");
		FidelizadosBean fidelizado = new FidelizadosBean();
		try {
			fidelizado.setUidInstancia(sesion.getAplicacion().getUidInstancia());
			if (MODO_EDICION.equals(modo)) {
				fidelizado.setFechaAlta(this.fidelizado.getFechaAlta());
			} else if (MODO_INSERCION.equals(modo)) {
				fidelizado.setFechaAlta(new Date());
			}

			/* =============== DATOS GENERALES =============== */
			String mensajeValidar = "";
			if (StringUtils.isNotBlank(paneDatosGeneralesController.getTfCodigo().getText())) {
				fidelizado.setCodFidelizado(paneDatosGeneralesController.getTfCodigo().getText());
			}
			String codPaisFidelizado = paneDatosGeneralesController.getTfCodPais().getText();
			if (StringUtils.isNotBlank(codPaisFidelizado)) {
				fidelizado.setCodPais(codPaisFidelizado);
			} else {
				mensajeValidar = "El Código del Pais es un campo obligatorio";
				throw new ValidarDatosGeneralesException(mensajeValidar);
			}
			if (StringUtils.isNotBlank(paneDatosGeneralesController.getTfDesPais().getText())) {
				fidelizado.setDesPais(paneDatosGeneralesController.getTfDesPais().getText());
			} else {
				mensajeValidar = "La Descripción del Pais es un campo obligatorio";
				throw new ValidarDatosGeneralesException(mensajeValidar);
			}
			if (paneDatosGeneralesController.getCbTipoDocumento().getValue() != null) {
				fidelizado.setCodTipoIden(paneDatosGeneralesController.getCbTipoDocumento().getValue().getCodigo());
			}
			if (StringUtils.isNotBlank(paneDatosGeneralesController.getTfDocumento().getText())) {
				fidelizado.setDocumento(paneDatosGeneralesController.getTfDocumento().getText());
			}
			if (StringUtils.isNotBlank(paneDatosGeneralesController.getTfNombre().getText())) {
				fidelizado.setNombre(paneDatosGeneralesController.getTfNombre().getText());
			} else {
				mensajeValidar = "El Nombre es un campo obligatorio";
				throw new ValidarDatosGeneralesException(mensajeValidar);
			}
			if (StringUtils.isNotBlank(paneDatosGeneralesController.getTfApellidos().getText())) {
				fidelizado.setApellidos(paneDatosGeneralesController.getTfApellidos().getText());
			} else {
				mensajeValidar = "El Apellido es un campo obligatorio";
				throw new ValidarDatosGeneralesException(mensajeValidar);
			}
			/* =============== SEXO =============== */
			if (paneDatosGeneralesController.getCbSexo().getValue() != null) {
				String sexo = paneDatosGeneralesController.getCbSexo().getValue().getCodigo();
				fidelizado.setSexo(sexo);
			}
			/* =============== PAIS =============== */
			try {
				if (StringUtils.isNotBlank(codPaisFidelizado)
						&& StringUtils.isNotBlank(paneDatosGeneralesController.getTfDesPais().getText())) {
					
					PaisBean pais = paisService.consultarCodPais(codPaisFidelizado);
					String codPaisCliente = sesion.getAplicacion().getTienda().getCliente().getCodpais();
					
					/* No se puede dar de alta en el resto de paises fidelizados de USA */
					if (codPaisFidelizado.equals(USA) && !codPaisCliente.equals(USA)) {
						mensajeValidar = "No se puede dar de alta un fidelizado de USA";
						throw new ValidarDatosGeneralesException(mensajeValidar);
					}
					/* No se puede dar de alta en el resto de paises fidelizados de Puerto Rico */
					if (codPaisFidelizado.equals(PUERTO_RICO) && !codPaisCliente.equals(PUERTO_RICO)) {
						mensajeValidar = "No se puede dar de alta un fidelizado de Puerto Rico";
						throw new ValidarDatosGeneralesException(mensajeValidar);
					}
					fidelizado.setCodPais(pais.getCodPais());
					fidelizado.setDesPais(pais.getDesPais());
				} else {
					mensajeValidar = "El Pais es un campo obligatorio";
					throw new ValidarDatosGeneralesException(mensajeValidar);
				}
			} catch (ValidarDatosGeneralesException e) {
				throw new ValidarDatosGeneralesException(e.getMessage());
			} catch (PaisNotFoundException e1) {
				String mensajeError = "No se ha encontrado el Pais con el código indicado "
						+ codPaisFidelizado;
				log.error("getDatosFidelizado() - " + mensajeError, e1);
				throw new ValidarDatosGeneralesException(mensajeValidar, e1);
			} catch (PaisServiceException e2) {
				String mensajeError = "Se ha producido un error al realizar la búsqueda del Pais con el código "
						+ codPaisFidelizado;
				log.debug("getDatosFidelizado() - " + mensajeError, e2);
				throw new ValidarDatosGeneralesException(mensajeValidar, e2);
			}

			/* =============== DIRECCIONES =============== */
			if (direcciones != null) {
				direcciones.clear();
			} else {
				direcciones = new ArrayList<DireccionFidelizadoBean>();
			}
			direcciones.addAll(paneDireccionController.getDirecciones());
			if (direcciones != null && !direcciones.isEmpty()) {
				/* Añadimos la primera dirección como la principal */
				fidelizado.setProvincia(direcciones.get(0).getProvincia());
				fidelizado.setLocalidad(direcciones.get(0).getLocalidad());
				fidelizado.setPoblacion(direcciones.get(0).getPoblacion());
				fidelizado.setDomicilio(direcciones.get(0).getDomicilio());
				fidelizado.setCp(direcciones.get(0).getCp());

				fidelizado.setDirecciones(direcciones);
			} else {
				fidelizado.setProvincia("");
				fidelizado.setLocalidad("");
				fidelizado.setPoblacion("");
				fidelizado.setDomicilio("");
				fidelizado.setCp("");
			}

			/* =============== CONSENTIMIENTO Y FIRMA =============== */
			/* Realizamos las comprobaciones de que se ha realizado la Firma */
			if (firma == null) {
				mensajeValidar = "La Firma es un dato obligatorio";
				throw new ValidarDatosGeneralesException(mensajeValidar);
			}
			/* Generamos el objeto de Consentimiento/Firma y lo añadimos al Fidelizado */
			FidelizadoFirmaBean consentimientoFirma = new FidelizadoFirmaBean();
			consentimientoFirma.setFirma(firma);
			consentimientoFirma.setFecha(new Date());
			if (consentimiento.get("1").equals("S")) {
				consentimientoFirma.setConsentimientoRecibenoti("S");
			} else {
				consentimientoFirma.setConsentimientoRecibenoti("N");
			}
			if (consentimiento.get("2").equals("S")) {
				consentimientoFirma.setConsentimientoUsodatos("S");
			} else {
				consentimientoFirma.setConsentimientoUsodatos("N");
			}
			fidelizado.setConsentimientosFirma(consentimientoFirma);

			/* =============== EMAIL Y MOVIL =============== */
			String email = "", movil = "";
			if (StringUtils.isNotBlank(paneDatosGeneralesController.getTfEmail().getText())) {
				email = paneDatosGeneralesController.getTfEmail().getText();
				/* Realizamos una validación de formato del Email */
				Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
						+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
				Matcher mather = pattern.matcher(email);
				if (!mather.find()) {
					mensajeValidar = "El formato del Email no es correcto";
					throw new ValidarDatosGeneralesException(mensajeValidar);
				}
			}
			if (StringUtils.isNotBlank(paneDatosGeneralesController.getTfMovil().getText())) {
				String contenidoMovil = paneDatosGeneralesController.getTfMovil().getText();
				contenidoMovil = contenidoMovil.substring(1, contenidoMovil.length());
				if (StringUtils.isNumeric(contenidoMovil)) {
					movil = paneDatosGeneralesController.getTfMovil().getText();
				} else {
					mensajeValidar = "El Movil solo puede contener números";
					throw new ValidarDatosGeneralesException(mensajeValidar);
				}
			}
			if (StringUtils.isBlank(email) && StringUtils.isBlank(movil)) {
				mensajeValidar = "Indicar el Movil o el Email es obligatorio";
				throw new ValidarDatosGeneralesException(mensajeValidar);
			} else {
				boolean recibirNotificaciones = "S"
						.equals(fidelizado.getConsentimientosFirma().getConsentimientoRecibenoti()) ? Boolean.TRUE
								: Boolean.FALSE;
				if (MODO_INSERCION.equals(modo)) {
					List<TiposContactoFidelizadoBean> tiposContacto = new ArrayList<TiposContactoFidelizadoBean>();
					if (StringUtils.isNotBlank(email)) {
						TiposContactoFidelizadoBean contactoEmail = new TiposContactoFidelizadoBean();
						contactoEmail.setCodTipoCon(EMAIL);
						contactoEmail.setRecibeNotificaciones(recibirNotificaciones);
						contactoEmail.setPuedeRecibirNotificaciones(recibirNotificaciones);
						contactoEmail.setValor(email);
						tiposContacto.add(contactoEmail);
					}
					if (StringUtils.isNotBlank(movil)) {
						TiposContactoFidelizadoBean contactoMovil = new TiposContactoFidelizadoBean();
						contactoMovil.setCodTipoCon(MOVIL);
						contactoMovil.setRecibeNotificaciones(recibirNotificaciones);
						contactoMovil.setPuedeRecibirNotificaciones(recibirNotificaciones);
						contactoMovil.setValor(movil);
						tiposContacto.add(contactoMovil);
					}

					fidelizado.setTiposContacto(tiposContacto);
					fidelizado.setTarjetas(new ArrayList<TarjetaBean>());
				} else if (MODO_EDICION.equals(modo)) {
					fidelizado.setIdFidelizado(this.fidelizado.getIdFidelizado());
					fidelizado.setTiposContacto(this.fidelizado.getTiposContacto());
					TiposContactoFidelizadoBean oldMovilContacto = fidelizado.getTipoContacto(MOVIL);
					if (oldMovilContacto != null && oldMovilContacto.getValor() != null) {
						String oldMovil = oldMovilContacto.getValor();
						boolean oldRecibeNotif = oldMovilContacto.isRecibeNotificaciones();
						if (StringUtils.isBlank(movil)) {
							oldMovilContacto.setEstadoBean(Estado.BORRADO);
						} else if (!oldMovil.equals(movil) || oldRecibeNotif != recibirNotificaciones) {
							oldMovilContacto.setValor(movil);
							oldMovilContacto.setRecibeNotificaciones(recibirNotificaciones);
							oldMovilContacto.setPuedeRecibirNotificaciones(recibirNotificaciones);
							oldMovilContacto.setEstadoBean(Estado.MODIFICADO);
						}
					} else {
						if (StringUtils.isNotBlank(movil)) {
							TiposContactoFidelizadoBean contactoMovil = new TiposContactoFidelizadoBean();
							contactoMovil.setCodTipoCon(MOVIL);
							contactoMovil.setRecibeNotificaciones(recibirNotificaciones);
							contactoMovil.setPuedeRecibirNotificaciones(recibirNotificaciones);
							contactoMovil.setValor(movil);
							contactoMovil.setEstadoBean(Estado.NUEVO);
							fidelizado.getTiposContacto().add(contactoMovil);
						}
					}
					TiposContactoFidelizadoBean oldEmailContacto = fidelizado.getTipoContacto(EMAIL);
					if (oldEmailContacto != null && oldEmailContacto.getValor() != null) {
						String oldEmail = oldEmailContacto.getValor();
						boolean oldRecibeNotif = oldEmailContacto.isRecibeNotificaciones();
						if (StringUtils.isBlank(email)) {
							oldEmailContacto.setEstadoBean(Estado.BORRADO);
						} else if (!oldEmail.equals(email) || oldRecibeNotif != recibirNotificaciones) {
							oldEmailContacto.setValor(email);
							oldEmailContacto.setRecibeNotificaciones(recibirNotificaciones);
							oldEmailContacto.setPuedeRecibirNotificaciones(recibirNotificaciones);
							oldEmailContacto.setEstadoBean(Estado.MODIFICADO);
						}
					} else {
						if (StringUtils.isNotBlank(email)) {
							TiposContactoFidelizadoBean contactoEmail = new TiposContactoFidelizadoBean();
							contactoEmail.setCodTipoCon(EMAIL);
							contactoEmail.setRecibeNotificaciones(recibirNotificaciones);
							contactoEmail.setPuedeRecibirNotificaciones(recibirNotificaciones);
							contactoEmail.setValor(email);
							contactoEmail.setEstadoBean(Estado.NUEVO);
							fidelizado.getTiposContacto().add(contactoEmail);
						}
					}
				}
			}
		} catch (ValidarDatosGeneralesException e) {
			log.error("getDatosFidelizado() - " + e.getMessage());
			VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto(e.getMessage()), getStage());
			return null;
		}
		return fidelizado;
	}

	@FXML
	public void accionCancelar() {
		if (ByLVentanaDialogoComponent.crearVentanaConfirmacion(I18N.getTexto("¿Desea cancelar el alta/actualización de los datos del cliente?"), getStage())) {

			btAceptar.setVisible(false);
			btAceptar.setManaged(false);
			btAceptar.setDisable(false);
			btCancelar.setVisible(false);
			btCancelar.setManaged(false);
			btEditar.setVisible(true);
			btEditar.setManaged(true);
			btCerrar.setVisible(true);
			btCerrar.setManaged(true);
			lblCampoObligatorios.setVisible(false);
			setModo(MODO_CONSULTA);
			tabResumen.setDisable(false);
			paneDireccionController.setFidelizado(null);

			paneDireccionController.volverInicio();
			paneDireccionController.activarConfirmarCambios(true);
			paneDireccionController.limpiarFormulario();

			/* Imprimir solo visible para modo Consulta */
			activarImprimir(Boolean.TRUE);

			btConsentimientoFirma.setVisible(Boolean.FALSE);
			btConsentimientoFirma.setManaged(Boolean.FALSE);

			paneDatosGeneralesController.setFidelizado(null);
			tpDatosFidelizado.getSelectionModel().select(tabResumen);
			lbError.setText("");
		}
	}

	/**
	 * Realiza la carga de los datos procedentes del formulario de búsqueda, solo en
	 * una inserción.
	 */
	public void cargarDatosBusqueda() {
		if (getDatos().containsKey(ByLBusquedaFidelizadoController.PARAMETRO_DATOS_BUSQUEDA)) {
			DatosBusquedaAltaFidelizado datosBusqueda = (DatosBusquedaAltaFidelizado) getDatos()
					.get(ByLBusquedaFidelizadoController.PARAMETRO_DATOS_BUSQUEDA);
			/* Comprobamos todos los campos y los vamos añadiendo al formulario */
			if (StringUtils.isNotBlank(datosBusqueda.getCodigoCliente())) {
				paneDatosGeneralesController.getTfCodigo().setText(datosBusqueda.getCodigoCliente());
				log.debug("cargarDatosBusqueda() - Código Cliente cargado : " + datosBusqueda.getCodigoCliente());
			}
			if (StringUtils.isNotBlank(datosBusqueda.getDocumentoFiscal())) {
				paneDatosGeneralesController.getTfDocumento().setText(datosBusqueda.getDocumentoFiscal());
				log.debug("cargarDatosBusqueda() - Documento Fiscal cargado : " + datosBusqueda.getDocumentoFiscal());
			}
			if (StringUtils.isNotBlank(datosBusqueda.getNombre())) {
				paneDatosGeneralesController.getTfNombre().setText(datosBusqueda.getNombre());
				log.debug("cargarDatosBusqueda() - Nombre cargado : " + datosBusqueda.getNombre());
			}
			if (StringUtils.isNotBlank(datosBusqueda.getApellidos())) {
				paneDatosGeneralesController.getTfApellidos().setText(datosBusqueda.getApellidos());
				log.debug("cargarDatosBusqueda() - Apellidos cargados : " + datosBusqueda.getApellidos());
			}
			if (StringUtils.isNotBlank(datosBusqueda.getEmail())) {
				paneDatosGeneralesController.getTfEmail().setText(datosBusqueda.getEmail());
				log.debug("cargarDatosBusqueda() - Email cargado : " + datosBusqueda.getEmail());
			}
			if (StringUtils.isNotBlank(datosBusqueda.getTelefono())) {
				paneDatosGeneralesController.getTfMovil().setText(datosBusqueda.getTelefono());
				log.debug("cargarDatosBusqueda() - Teléfono cargado : " + datosBusqueda.getTelefono());
			}
		}
	}

	@FXML
	public void accionConsentimientoFirma() {
		/* CREAMOS EL DIÁLOGO DE CARGA PERSONALIZADO, ESTO DEBERÍA HACERSE UNA SOLA VEZ EN EL POSAPPLICATION */
		ByLVentanaEspera.crearVentanaCargando(getStage());
		new ConsentimientoFirmaTask().start(I18N.getTexto("En espera de que el cliente pulse los consentimientos y la firma en el dispositivo"));
	}

	public class ConsentimientoFirmaTask extends ByLBackgroundTask<Void> {

		public ConsentimientoFirmaTask() {
			super();
		}

		@Override
		public Void call() throws Exception {
			if (consentimiento == null && firma == null) {
				/* Realizamos las comprobaciones de Firma y Consentimiento */
				if (MODO_INSERCION.equals(modo)) {
					realizarConsentimientoFirma();
				} else if (MODO_EDICION.equals(modo)) {
					/* Controlamos si repetir las condiciones o la firma */
					if (VentanaDialogoComponent.crearVentanaConfirmacion(
							I18N.getTexto("¿Desea repetir los Consentimientos y la Firma?"), getStage())) {
						realizarConsentimientoFirma();
					}
				}
			} else {
				/*
				 * Si ya estaban rellenas de antes se debería preguntar si realizarla o no de
				 * nuevo, en caso de error al guardar
				 */
				if (VentanaDialogoComponent.crearVentanaConfirmacion(
						I18N.getTexto("¿Desea repetir los Consentimientos y la Firma?"), getStage())) {
					realizarConsentimientoFirma();
				}
			}
			paneDatosGeneralesController.coloresFirmaConsentimiento(consentimiento, firma);
			return null;
		}

		@Override
		public void succeeded() {
			super.succeeded();
		}

		@Override
		public void failed() {
			super.failed();
			Exception e = (Exception) getException();
			log.error("ConsentimientoFirmaTask/failed() - " + e.getMessage(), e);
			VentanaDialogoComponent.crearVentanaError(getStage(), I18N.getTexto(e.getMessage()), e);
		}
	}

	public void realizarConsentimientoFirma() {
		// TODO
		Map<String, Object> resultado = new HashMap<String, Object>();
		IFirma dispositivoFirma = ByLDispositivosFirma.getInstance().getDispositivoFirmaActual();
		// dispositivoFirma =
		// ByLDispositivos.getInstance().getDispositivoIFirma(dispositivoFirma.getManejador());

		try {
			resultado = dispositivoFirma.firmar();

			consentimiento = new HashMap<String, String>();
			Boolean consentimientosRealizados = Boolean.TRUE, firmaRealizada = Boolean.TRUE;

			if (dispositivoFirma instanceof AxisManager) {
				for (Map.Entry<String, Object> entry : resultado.entrySet()) {
					if (AxisManager.RESPUESTA_CONSENTIMIENTO.equals(entry.getKey())) {
						if (((JC3ApiC3Rspn) entry.getValue()).getJson().contains("System Cancel")) {
							consentimiento.put("1", "N");
							consentimiento.put("2", "N");
							consentimientosRealizados = Boolean.FALSE;
						} else {
							Gson gson = new Gson();
							AxisConsentimientoBean consentimientosAxis = gson.fromJson(
									((JC3ApiC3Rspn) entry.getValue()).getJson(), AxisConsentimientoBean.class);
							if (consentimientosAxis.getOperationResult().getMainCondition()) {
								consentimiento.put("1", "S");
							} else {
								consentimiento.put("1", "N");
							}
							if (consentimientosAxis.getOperationResult().getSecondaryCondition()) {
								consentimiento.put("2", "S");
							} else {
								consentimiento.put("2", "N");
							}
						}
					}
					if (AxisManager.IMAGEN_FIRMA.equals(entry.getKey())) {
						if (entry.getValue() != null) {
							firma = ((byte[]) entry.getValue());
						} else {
							firma = null;
							firmaRealizada = Boolean.FALSE;
						}
					}
				}
			} else {
				for (Map.Entry<String, Object> entry : resultado.entrySet()) {
					if (entry.getKey().equals(IFirma.RESPUESTA_CONSENTIMIENTO_NOTIFICACIONES)) {
						if ((Boolean) entry.getValue()) {
							consentimiento.put("1", "S");
						} else {
							consentimiento.put("1", "N");
						}
					}
					if (entry.getKey().equals(IFirma.RESPUESTA_CONSENTIMIENTO_USO_DATOS)) {
						if ((Boolean) entry.getValue()) {
							consentimiento.put("2", "S");
						} else {
							consentimiento.put("2", "N");
						}
					}

					if (entry.getKey().equals(IFirma.IMAGEN_FIRMA)) {
						if (entry.getValue() != null) {
							firma = ((byte[]) entry.getValue());
						}
					}
				}
			}

			/* Realizamos comprobaciones para informar al usuario de las acciones */
			String mensajeAviso = "";
			if (!consentimientosRealizados && !firmaRealizada) {
				mensajeAviso = I18N.getTexto("El cliente ha cancelado la aceptación de los Consentimientos"
						+ "\nEl cliente ha cancelado la Firma");
			} else if (!consentimientosRealizados) {
				mensajeAviso = I18N.getTexto("El cliente ha cancelado la aceptación de los Consentimientos");
			} else if (!firmaRealizada) {
				mensajeAviso = I18N.getTexto("El cliente ha cancelado la Firma");
			} else {
				mensajeAviso = I18N.getTexto("Se ha realizado el Consentimiento y Firma correctamente");
			}
			log.debug("realizarConsentimientoFirma() - " + mensajeAviso);
			VentanaDialogoComponent.crearVentanaInfo(mensajeAviso, getStage());

		} catch (Exception e) {
			String mensajeError = "";
			if (e instanceof IOException || e instanceof DispositivoException || e instanceof AxisResponseException
					|| e instanceof DispositivoFirmaException) {
				mensajeError = e.getMessage();
			} else {
				mensajeError = I18N.getTexto("Error no controlado a la hora de realizar el Consentimiento y Firma");
			}
			log.debug("realizarConsentimientoFirma() - " + mensajeError, e);
			VentanaDialogoComponent.crearVentanaError(getStage(), mensajeError, e);
		}
	}

	@Override
	public void onClose() {
		fidelizado = null;

		if (POSApplication.getInstance().getMainView().isUserClosing()) {
			paneDatosGeneralesController.setFidelizado(null);
			paneResumenFidelizadoController.setFidelizado(null);
			paneDireccionController.setFidelizado(null);
		}
		super.onClose();
	};

	public void reiniciarFidelizadoPestañas() {
		paneDatosGeneralesController.setFidelizado(null);
		paneResumenFidelizadoController.setFidelizado(null);
		paneDireccionController.setFidelizado(null);
	}

	@FXML
	public void accionImprimir() {
		log.debug("accionImprimir() Iniciamos el proceso para imprimir los datos del Fidelizado...");
		List<FidelizadoBean> fidelizados = new ArrayList<FidelizadoBean>();
		HashMap<String, Object> parametros = new HashMap<String, Object>();

		/*
		 * Se edita temporalmente el domicilio y documento del Fidelizado para la
		 * impresión por si fuera necesario ocultarlo
		 */
		String domicilioCopia = fidelizado.getDomicilio();
		String documentoCopia = fidelizado.getDocumento();
		fidelizado.setDomicilio(imprimeDatoSensible(fidelizado.getDomicilio()));
		fidelizado.setDocumento(imprimeDatoSensible(fidelizado.getDocumento()));

		fidelizados.add(fidelizado);
		parametros.put(ImpresionJasper.LISTA, fidelizados);
		if (fidelizado != null) {
			if (fidelizado.getTipoContacto(MOVIL) != null) {
				parametros.put(MOVIL, imprimeDatoSensible(fidelizado.getTipoContacto(MOVIL).getValor()));
				parametros.put(MOVIL_NOTIF,
						"S".equals(fidelizado.getTipoContacto(MOVIL).getRecibeNotificaciones()) ? "Sí" : "No");
			} else {
				parametros.put(MOVIL, "");
				parametros.put(MOVIL_NOTIF, "-");
			}
			if (fidelizado.getTipoContacto(EMAIL) != null) {
				parametros.put(EMAIL, imprimeDatoSensible(fidelizado.getTipoContacto(EMAIL).getValor()));
				parametros.put(EMAIL_NOTIF,
						"S".equals(fidelizado.getTipoContacto(EMAIL).getRecibeNotificaciones()) ? "Sí" : "No");
			} else {
				parametros.put(EMAIL, "");
				parametros.put(EMAIL_NOTIF, "-");
			}

		} else {
			/* Para que no aparezca 'null' en 'Permite notificaciones' */
			parametros.put(MOVIL_NOTIF, "");
			parametros.put(EMAIL_NOTIF, "");
		}
		parametros.put("DESEMP", sesion.getAplicacion().getEmpresa().getDesEmpresa());
		parametros.put("DOMICILIO", sesion.getAplicacion().getEmpresa().getDomicilio());
		parametros.put("CP", sesion.getAplicacion().getEmpresa().getCp());
		parametros.put("PROVINCIA", sesion.getAplicacion().getEmpresa().getProvincia());

		if (sesion.getAplicacion().getEmpresa().getLogotipo() != null) {
			ByteArrayInputStream bis = new ByteArrayInputStream(sesion.getAplicacion().getEmpresa().getLogotipo());
			parametros.put("LOGO", bis);
		}

		try {
			ServicioImpresion.imprimir("jasper/fidelizados/formulariofidelizado", parametros);
		} catch (DeviceException e) {
			String mensajeError = "Se ha producido un error al imprimir el informe del Fidelizado";
			log.error("accionImprimir() - " + mensajeError + " - " + e.getMessage(), e);
		} finally {
			fidelizado.setDomicilio(domicilioCopia);
			fidelizado.setDocumento(documentoCopia);
		}
	}

	public ByLFidelizacionBean tratarFidelizadoVenta(String numTarjeta, FidelizadosBean fidelizado) throws ConsultaTarjetaFidelizadoException {
		ByLFidelizacionBean fidelizadoVenta = (ByLFidelizacionBean) Dispositivos.getInstance().getFidelizacion().consultarTarjetaFidelizado(numTarjeta, sesion.getAplicacion().getUidActividad());

		/* Guardamos los datos de los consentimientos y las firmas */
		if (fidelizado.getConsentimientosFirma() != null) {
			if (StringUtils.isNotBlank(fidelizado.getConsentimientosFirma().getConsentimientoUsodatos())) {
				fidelizadoVenta.setConsentimientoUsodatos(fidelizado.getConsentimientosFirma().getConsentimientoUsodatos());
			}
			else {
				fidelizadoVenta.setConsentimientoUsodatos("N");
			}
			if (StringUtils.isNotBlank(fidelizado.getConsentimientosFirma().getConsentimientoRecibenoti())) {
				fidelizadoVenta.setConsentimientoRecibenoti(fidelizado.getConsentimientosFirma().getConsentimientoRecibenoti());
			}
			else {
				fidelizadoVenta.setConsentimientoRecibenoti("N");
			}
		}
		fidelizadoVenta.setFirma(firma);

		return fidelizadoVenta;

	}
	/**
	 * Realiza la asignación de los datos del Fidelizado al Ticket de venta.
	 * 
	 * @param fidelizado
	 */
	protected ByLFidelizacionBean asignarFidelizadoAVenta(FidelizadosBean fidelizado) {
		ByLFidelizacionBean fidelizadoVenta = null;
		for (View view : getApplication().getMainView().getSubViews()) {
			if ((view instanceof FacturacionArticulosView && view.getController() instanceof FacturacionArticulosController)
			        || (view instanceof VentaProfesionalView && view.getController() instanceof ByLVentaProfesionalController)) {
				String numTarjeta = fidelizado.getNumeroTarjeta();

				if (StringUtils.isNotBlank(numTarjeta)) {
					if (view instanceof FacturacionArticulosView && view.getController() instanceof FacturacionArticulosController) {

						FacturacionArticulosController facturacionArticulosController = (FacturacionArticulosController) view.getController();
						try {
							fidelizadoVenta = tratarFidelizadoVenta(numTarjeta, fidelizado);
							facturacionArticulosController.ticketManager.getTicket().getCabecera().setDatosFidelizado(fidelizadoVenta);
						}
						catch (Exception e) {
							log.error("crearFidelizado() - Ha habido un error al consultar el fidelizado recién creado: " + e.getMessage(), e);
							facturacionArticulosController.ticketManager.getTicket().getCabecera().setDatosFidelizado(numTarjeta);
						}
						facturacionArticulosController.refrescarDatosPantalla();
					}
					else if (view instanceof VentaProfesionalView && view.getController() instanceof ByLVentaProfesionalController) {
						ByLVentaProfesionalController bylVentaProfesionalController = (ByLVentaProfesionalController) view.getController();
						try {
							fidelizadoVenta = tratarFidelizadoVenta(numTarjeta, fidelizado);
							bylVentaProfesionalController.ticketManager.getTicket().getCabecera().setDatosFidelizado(fidelizadoVenta);
						}
						catch (Exception e) {
							log.error("crearFidelizado() - Ha habido un error al consultar el fidelizado recién creado: " + e.getMessage(), e);
							bylVentaProfesionalController.ticketManager.getTicket().getCabecera().setDatosFidelizado(numTarjeta);
						}
						bylVentaProfesionalController.refrescarDatosPantalla();
					}

				}
			}
		}

		return fidelizadoVenta;
	}

	@FXML
	public void imprimeTicketFidelizado() {
		((ByLPaneResumenFidelizadoController) paneResumenFidelizadoController).accionImprimirLinea();
	}

	@Override
	public void comprobarPermisosUI() {
		try {
			super.compruebaPermisos(PERMISO_EDITAR);
			btEditar.setDisable(false);
		} catch (SinPermisosException ex) {
			btEditar.setDisable(true);
		}
		try {
			super.compruebaPermisos(PERMISO_IMPRIMIR);
			btImprimir.setDisable(false);
		} catch (SinPermisosException ex) {
			btImprimir.setDisable(true);
		}
		try {
			super.compruebaPermisos(PERMISO_DATOSSENSIBLES);
			verDatosSensibles = true;
		} catch (SinPermisosException ex) {
			verDatosSensibles = false;
		}
	}

	public String getModo() {
		return modo;
	}

	public void setModo(String modo) {
		this.modo = modo;
	}

	public Map<String, String> getConsentimiento() {
		return consentimiento;
	}

	public void setConsentimiento(Map<String, String> consentimiento) {
		this.consentimiento = consentimiento;
	}

	public byte[] getFirma() {
		return firma;
	}

	public void setFirma(byte[] firma) {
		this.firma = firma;
	}

	public List<DireccionFidelizadoBean> getDirecciones() {
		return direcciones;
	}

	public void setDirecciones(List<DireccionFidelizadoBean> direcciones) {
		this.direcciones = direcciones;
	}

	private Response insertFidelizadoLocal(ByLFidelizadoRequestRest fidelizado, String url)
			throws RestException, RestHttpException {
		log.debug(
				"ByLFidelizadoController/insertFidelizadoLocal() - Inicio de la inserción de un Fidelizado en local...");

		GenericType<Response> genericType = new GenericType<Response>() {
		};
		try {
			WebTarget target = ClientBuilder.getClient().target(url).property(ClientProperties.CONNECT_TIMEOUT, 30000)
					.property(ClientProperties.READ_TIMEOUT, 30000).path(servicio_insertFidelizadoLocal);
			log.debug(
					"ByLFidelizadoController/insertFidelizadoLocal() - URL de servicio rest en la que se realiza la petición: "
							+ target.getUri());

			JAXBElement<ByLFidelizadoRequestRest> jaxbElement = new JAXBElement<ByLFidelizadoRequestRest>(
					new QName("FidelizadoRequestRest"), ByLFidelizadoRequestRest.class, fidelizado);

			Response response = target.request().header("Accept", MediaType.APPLICATION_XML)
					.post(Entity.entity(jaxbElement, MediaType.APPLICATION_XML), genericType);

			if (response.getStatus() != 200) {
				throw new Exception("No se ha podido realizar el insert de fidelizado en local");
			}

			log.debug("ByLFidelizadoController/insertFidelizadoLocal() - Finalizada la inserción de un Fidelizado");
			return response;
		} catch (BadRequestException e) {
			throw RestHttpException.establecerException(e);
		} catch (WebApplicationException e) {
			String mensajeError = "Se ha producido un error HTTP al insertar un Fidelizado";
			log.error("ByLFidelizadoController/insertFidelizadoLocal() - " + mensajeError + " - " + e.getMessage(), e);
			throw new RestHttpException(e.getResponse().getStatus(), mensajeError, e);
		} catch (Exception e) {
			String mensajeError = "Se ha producido un error realizando la petición de inserción de un Fidelizado";
			log.error("ByLFidelizadoController/insertFidelizadoLocal() - " + mensajeError + " - " + e.getMessage(), e);
			throw new RestException(mensajeError, e);
		}

	}

	private Response updateIDFidelizadoLocal(ByLFidelizadoRequestRest fidelizado, String url)
			throws RestException, RestHttpException {
		log.debug(
				"ByLFidelizadoController/updateIDFidelizadoLocal() - Inicio de la inserción de un Fidelizado en local...");

		GenericType<Response> genericType = new GenericType<Response>() {
		};
		try {
			WebTarget target = ClientBuilder.getClient().target(url).property(ClientProperties.CONNECT_TIMEOUT, 30000)
					.property(ClientProperties.READ_TIMEOUT, 30000).path(servicio_updateIdFidelizadoLocal);
			log.debug(
					"ByLFidelizadoController/envioFidelizadoLocal() - URL de servicio rest en la que se realiza la petición: "
							+ target.getUri());

			JAXBElement<ByLFidelizadoRequestRest> jaxbElement = new JAXBElement<ByLFidelizadoRequestRest>(
					new QName("FidelizadoRequestRest"), ByLFidelizadoRequestRest.class, fidelizado);

			Response response = target.request().header("Accept", MediaType.APPLICATION_XML)
					.put(Entity.entity(jaxbElement, MediaType.APPLICATION_XML), genericType);

			if (response.getStatus() != 200) {
				throw new Exception("No se ha podido realizar la actualización del fidelizado en local");
			}

			log.debug("ByLFidelizadoController/updateIDFidelizadoLocal() - Finalizada la inserción de un Fidelizado");
			return response;
		} catch (BadRequestException e) {
			throw RestHttpException.establecerException(e);
		} catch (WebApplicationException e) {
			String mensajeError = "Se ha producido un error HTTP al insertar un Fidelizado";
			log.error("ByLFidelizadoController/updateIDFidelizadoLocal() - " + mensajeError + " - " + e.getMessage(),
					e);
			throw new RestHttpException(e.getResponse().getStatus(), mensajeError, e);
		} catch (Exception e) {
			String mensajeError = "Se ha producido un error realizando la petición de inserción de un Fidelizado";
			log.error("ByLFidelizadoController/updateIDFidelizadoLocal() - " + mensajeError + " - " + e.getMessage(),
					e);
			throw new RestException(mensajeError, e);
		}

	}

	private Response updateFidelizadoLocal(ByLFidelizadoRequestRest fidelizado, String url)
			throws RestException, RestHttpException {
		log.debug(
				"ByLFidelizadoController/updateFidelizadoLocal() - Inicio de la inserción de un Fidelizado en local...");

		GenericType<Response> genericType = new GenericType<Response>() {
		};
		try {
			WebTarget target = ClientBuilder.getClient().target(url).property(ClientProperties.CONNECT_TIMEOUT, 30000)
					.property(ClientProperties.READ_TIMEOUT, 30000).path(servicio_updateFidelizadoLocal);
			log.debug(
					"ByLFidelizadoController/envioFidelizadoLocal() - URL de servicio rest en la que se realiza la petición: "
							+ target.getUri());

			JAXBElement<ByLFidelizadoRequestRest> jaxbElement = new JAXBElement<ByLFidelizadoRequestRest>(
					new QName("FidelizadoRequestRest"), ByLFidelizadoRequestRest.class, fidelizado);

			Response response = target.request().header("Accept", MediaType.APPLICATION_XML)
					.put(Entity.entity(jaxbElement, MediaType.APPLICATION_XML), genericType);

			if (response.getStatus() != 200) {
				throw new Exception("No se ha podido realizar la actualización del fidelizado en local");
			}

			log.debug("ByLFidelizadoController/updateIDFidelizadoLocal() - Finalizada la inserción de un Fidelizado");
			return response;
		} catch (BadRequestException e) {
			throw RestHttpException.establecerException(e);
		} catch (WebApplicationException e) {
			String mensajeError = "Se ha producido un error HTTP al insertar un Fidelizado";
			log.error("ByLFidelizadoController/updateIDFidelizadoLocal() - " + mensajeError + " - " + e.getMessage(),
					e);
			throw new RestHttpException(e.getResponse().getStatus(), mensajeError, e);
		} catch (Exception e) {
			String mensajeError = "Se ha producido un error realizando la petición de inserción de un Fidelizado";
			log.error("ByLFidelizadoController/updateIDFidelizadoLocal() - " + mensajeError + " - " + e.getMessage(),
					e);
			throw new RestException(mensajeError, e);
		}
	}

	@FXML
	public void accionCerrar() {
		if(MODO_INSERCION.equals(modo)) {
			if (ByLVentanaDialogoComponent.crearVentanaConfirmacion(I18N.getTexto("¿Desea cancelar el alta/actualización de los datos del cliente?"), getStage())) {
				POSApplication.getInstance().getMainView().close();
			}
		}else {
			POSApplication.getInstance().getMainView().close();			
		}
	}
	
	private void consultarFiltroTiendas() {
		String apiKey = variablesService.getVariableAsString(VariablesServices.WEBSERVICES_APIKEY);
		String uidActividad = sesion.getAplicacion().getUidActividad();
	
		ConsultarTiendaRequestRest consultaTiendas = new ConsultarTiendaRequestRest();
		consultaTiendas.setApiKey(apiKey);
		consultaTiendas.setUidActividad(uidActividad);
		consultaTiendas.setCodEmp(sesion.getAplicacion().getEmpresa().getCodEmpresa());
	
		ConsultarTodasTiendasTask consultarTiendasTask = SpringContext.getBean(ConsultarTodasTiendasTask.class, consultaTiendas, new RestBackgroundTask.FailedCallback<List<TiendaBean>>(){
	
			@Override
			public void succeeded(List<TiendaBean> result) {
				List<TiendaGui> tiendas = new ArrayList<TiendaGui>();
				for (TiendaBean tienda : result) {
					TiendaGui tiendaGui = new TiendaGui(tienda);
					tiendas.add(tiendaGui);
				}
				todasTiendas = tiendas;
			}
	
			@Override
			public void failed(Throwable throwable) {
				getApplication().getMainView().close();
			}
		}, getStage());
		consultarTiendasTask.start();
	}
}
