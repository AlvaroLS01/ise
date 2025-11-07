package com.comerzzia.bimbaylola.pos.gui.mantenimientos.fidelizados.direcciones;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.comerzzia.ByL.backoffice.persistencia.fidelizacion.fidelizados.FidelizadosBean;
import com.comerzzia.bimbaylola.pos.gui.mantenimientos.fidelizados.ByLFidelizadoController;
import com.comerzzia.bimbaylola.pos.gui.mantenimientos.fidelizados.datosgenerales.ByLPaneDatosGeneralesController;
import com.comerzzia.core.util.base.Estado;
import com.comerzzia.model.fidelizacion.fidelizados.contactos.TiposContactoFidelizadoBean;
import com.comerzzia.model.fidelizacion.fidelizados.direcciones.DireccionFidelizadoBean;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.core.gui.controllers.TabController;
import com.comerzzia.pos.core.gui.tablas.celdas.CellFactoryBuilder;
import com.comerzzia.pos.gui.ventas.tickets.factura.paises.PaisesController;
import com.comerzzia.pos.gui.ventas.tickets.factura.paises.PaisesView;
import com.comerzzia.pos.persistence.paises.PaisBean;
import com.comerzzia.pos.services.core.paises.PaisNotFoundException;
import com.comerzzia.pos.services.core.paises.PaisService;
import com.comerzzia.pos.services.core.paises.PaisServiceException;
import com.comerzzia.pos.util.config.SpringContext;
import com.comerzzia.pos.util.i18n.I18N;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

@Component
public class PaneDireccionesController extends TabController<ByLFidelizadoController> {

	public static final String DESCRIPCION_DIRECCION_PRINCIPAL = "Principal";
	protected static final Logger log = Logger.getLogger(PaneDireccionesController.class);
	@FXML
	protected TextField tfDescripcion, tfDomicilio, tfProvincia, tfLocalidad, tfPoblacion, tfCodigoPostal, tfCodPais, tfDesPais;

	@FXML
	protected Button btBuscarPais;

	@FXML
	protected TableView<DireccionFidelizadoBean> tvDirecciones;
	@Autowired
	private PaisService paisService = SpringContext.getBean(PaisService.class);
	
	/*
	 * Tendremos 2 listas - direccionesInicial : La lista inicial con los datos de bbdd - direcciones: Será la lista con
	 * la que "juegaremos" a quitar/poner/editar, para mostrarse en pantalla cuando se haga alguna operación
	 */
	protected ObservableList<DireccionFidelizadoBean> direcciones;
	protected ObservableList<DireccionFidelizadoBean> direccionesInicial;

	@FXML
	protected TableColumn<DireccionFidelizadoBean, String> tcDescripcion, tcDomicilio, tcProvincia, tcLocalidad, tcPoblacion, tcCodigoPostal, tcPais;
	@FXML
	protected Button btInsertarDireccion, btEditarDireccion, btEliminarDireccion, btConfirmarCambios;
	@FXML
	protected Label lbModo;

	protected FidelizadosBean fidelizado;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		/* Configuramos las columnas de la tabla de Direcciones */
		tcDescripcion.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tvDirecciones", "tcDescripcion", null, CellFactoryBuilder.ESTILO_ALINEACION_IZQ));
		tcDomicilio.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tvDirecciones", "tcDomicilio", null, CellFactoryBuilder.ESTILO_ALINEACION_IZQ));
		tcLocalidad.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tvDirecciones", "tcLocalidad", null, CellFactoryBuilder.ESTILO_ALINEACION_IZQ));
		tcProvincia.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tvDirecciones", "tcProvincia", null, CellFactoryBuilder.ESTILO_ALINEACION_IZQ));
		tcPoblacion.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tvDirecciones", "tcPoblacion", null, CellFactoryBuilder.ESTILO_ALINEACION_IZQ));
		tcCodigoPostal.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tvDirecciones", "tcCodigoPostal", null, CellFactoryBuilder.ESTILO_ALINEACION_DCHA));
		tcPais.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tvDirecciones", "tcPais", null, CellFactoryBuilder.ESTILO_ALINEACION_IZQ));

		tcDescripcion.setCellValueFactory(new PropertyValueFactory<DireccionFidelizadoBean, String>("descripcionDireccion"));
		tcDescripcion.setSortable(false);
		tcDomicilio.setCellValueFactory(new PropertyValueFactory<DireccionFidelizadoBean, String>("domicilio"));
		tcDomicilio.setSortable(false);
		tcLocalidad.setCellValueFactory(new PropertyValueFactory<DireccionFidelizadoBean, String>("localidad"));
		tcLocalidad.setSortable(false);
		tcProvincia.setCellValueFactory(new PropertyValueFactory<DireccionFidelizadoBean, String>("provincia"));
		tcProvincia.setSortable(false);
		tcPoblacion.setCellValueFactory(new PropertyValueFactory<DireccionFidelizadoBean, String>("poblacion"));
		tcPoblacion.setSortable(false);
		tcCodigoPostal.setCellValueFactory(new PropertyValueFactory<DireccionFidelizadoBean, String>("cp"));
		tcCodigoPostal.setSortable(false);
		tcPais.setCellValueFactory(new PropertyValueFactory<DireccionFidelizadoBean, String>("codPais"));
		tcPais.setSortable(false);

		tfDesPais.setEditable(Boolean.FALSE);

		tfCodPais.focusedProperty().addListener(new ChangeListener<Boolean>(){

			@Override
			public void changed(ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) {
				if (StringUtils.isNotBlank(tfCodPais.getText())) {
					tfCodPais.setText(tfCodPais.getText().toUpperCase());
					if (!newValue) {
						cargaTiposDocumento();
					}
				}
			}
		});

		/* Añadimos el listado de datos a la tabla */
		direccionesInicial = FXCollections.observableList(new ArrayList<DireccionFidelizadoBean>());
		direcciones = FXCollections.observableList(new ArrayList<DireccionFidelizadoBean>());
		tvDirecciones.setItems(direcciones);
	}

	/**
	 * Se ejecuta para poner seleccionada esta pantalla en la selección de pestañas.
	 */
	public void selected(boolean cargarDatosEdicion) {

		tvDirecciones.rowFactoryProperty();
		tvDirecciones.getRowFactory();

		FidelizadosBean newFidelizado = (FidelizadosBean) getTabParentController().getFidelizado();
		fidelizado = newFidelizado;

		if (ByLFidelizadoController.MODO_EDICION.equals(getTabParentController().getModo())) {
			/* Para no perder los datos de la edición de la pantalla de datos generales */
			cargarEdicionFidelizado();
		}

		if (ByLFidelizadoController.MODO_CONSULTA.equals(getTabParentController().getModo())) {
			camposEditables(Boolean.TRUE);
		}

		/* Bloqueamos lo campos primero */
		bloquearFormulario(Boolean.FALSE);
	}

	/**
	 * Almacena los datos modificados en la anterior pantalla para que no se pierdan con el cambio entre pestañas.
	 * Cargamos todos los datos que son posibles modificar de la pantalla de datos generales
	 */
	private void cargarEdicionFidelizado() {
		ByLPaneDatosGeneralesController panelDatosGenerales = SpringContext.getBean(ByLPaneDatosGeneralesController.class);
		fidelizado.setNombre(panelDatosGenerales.getTfNombre().getText());
		fidelizado.setApellidos(panelDatosGenerales.getTfApellidos().getText());

		String email = panelDatosGenerales.getTfEmail().getText();
		String movil = panelDatosGenerales.getTfMovil().getText();
		boolean recibirNotificaciones = "S".equals(panelDatosGenerales.getFidelizado().getConsentimientosFirma().getConsentimientoRecibenoti()) ? Boolean.TRUE : Boolean.FALSE;
		TiposContactoFidelizadoBean oldMovilContacto = panelDatosGenerales.getFidelizado().getTipoContacto(ByLFidelizadoController.MOVIL);
		if (StringUtils.isNotBlank(movil)) {
			if (oldMovilContacto != null && oldMovilContacto.getValor() != null) {
				String oldMovil = oldMovilContacto.getValor();
				boolean oldRecibeNotif = oldMovilContacto.isRecibeNotificaciones();
				if (movil == null) {
					oldMovilContacto.setEstadoBean(Estado.BORRADO);
				}
				else if (!oldMovil.equals(movil) || oldRecibeNotif != recibirNotificaciones) {
					oldMovilContacto.setValor(movil);
					oldMovilContacto.setRecibeNotificaciones(recibirNotificaciones);
					oldMovilContacto.setPuedeRecibirNotificaciones(recibirNotificaciones);
					oldMovilContacto.setEstadoBean(Estado.MODIFICADO);
				}
			}
			else {
				if (movil != null) {
					TiposContactoFidelizadoBean contactoMovil = new TiposContactoFidelizadoBean();
					contactoMovil.setCodTipoCon(ByLFidelizadoController.MOVIL);
					contactoMovil.setRecibeNotificaciones(recibirNotificaciones);
					contactoMovil.setPuedeRecibirNotificaciones(recibirNotificaciones);
					contactoMovil.setValor(movil);
					contactoMovil.setEstadoBean(Estado.NUEVO);
					panelDatosGenerales.getFidelizado().getTiposContacto().add(contactoMovil);
				}
			}
		}
		if (StringUtils.isNotBlank(email)) {
			TiposContactoFidelizadoBean oldEmailContacto = fidelizado.getTipoContacto(ByLFidelizadoController.EMAIL);
			if (oldEmailContacto != null && oldEmailContacto.getValor() != null) {
				String oldEmail = oldEmailContacto.getValor();
				boolean oldRecibeNotif = oldEmailContacto.isRecibeNotificaciones();
				if (email == null) {
					oldEmailContacto.setEstadoBean(Estado.BORRADO);
				}
				else if (!oldEmail.equals(email) || oldRecibeNotif != recibirNotificaciones) {
					oldEmailContacto.setValor(email);
					oldEmailContacto.setRecibeNotificaciones(recibirNotificaciones);
					oldEmailContacto.setPuedeRecibirNotificaciones(recibirNotificaciones);
					oldEmailContacto.setEstadoBean(Estado.MODIFICADO);
				}
			}
			else {
				if (email != null) {
					TiposContactoFidelizadoBean contactoEmail = new TiposContactoFidelizadoBean();
					contactoEmail.setCodTipoCon(ByLFidelizadoController.EMAIL);
					contactoEmail.setRecibeNotificaciones(recibirNotificaciones);
					contactoEmail.setPuedeRecibirNotificaciones(recibirNotificaciones);
					contactoEmail.setValor(email);
					contactoEmail.setEstadoBean(Estado.NUEVO);
					panelDatosGenerales.getFidelizado().getTiposContacto().add(contactoEmail);
				}
			}
		}
		fidelizado.setCodPais(StringUtils.isNotBlank(panelDatosGenerales.getTfCodPais().getText()) ? panelDatosGenerales.getTfCodPais().getText() : null);
		fidelizado.setDesPais(StringUtils.isNotBlank(panelDatosGenerales.getTfDesPais().getText()) ? panelDatosGenerales.getTfDesPais().getText() : null);
		fidelizado.setDocumento(StringUtils.isNotBlank(panelDatosGenerales.getTfDocumento().getText()) ? panelDatosGenerales.getTfDocumento().getText() : null);
		fidelizado.setSexo(panelDatosGenerales.getCbSexo().getValue() != null ? panelDatosGenerales.getCbSexo().getValue().getCodigo() : null);
		fidelizado.setCodTipoIden(panelDatosGenerales.getCbTipoDocumento().getValue() != null ? panelDatosGenerales.getCbTipoDocumento().getValue().getCodigo() : null);
		fidelizado.setConsentimientosFirma(panelDatosGenerales.getFidelizado().getConsentimientosFirma());

		ByLFidelizadoController fidelizadosController = SpringContext.getBean(ByLFidelizadoController.class);
		fidelizadosController.setFidelizado(fidelizado);
	}

	/**
	 * Bloquea los campos del formulario para que no sean editables.
	 * 
	 * @param editables
	 *            : Indica si editar datos o no.
	 */
	public void bloquearFormulario(Boolean editable) {
		tfProvincia.setEditable(editable);
		tfPoblacion.setEditable(editable);
		tfLocalidad.setEditable(editable);
		tfDomicilio.setEditable(editable);
		tfCodigoPostal.setEditable(editable);
		tfCodPais.setEditable(editable);
		btBuscarPais.setDisable(!editable);

		// Sólo se podrá modificar el campo descripción cuando sea un alta de dirección
		if (lbModo.getText().equals(ByLFidelizadoController.MODO_INSERCION)) {
			tfDescripcion.setEditable(editable);
		}
		else {
			tfDescripcion.setEditable(false);
		}
	}

	public void limpiarFormulario() {
		tfProvincia.clear();
		tfPoblacion.clear();
		tfLocalidad.clear();
		tfDomicilio.clear();
		tfCodigoPostal.clear();
		tfDescripcion.clear();
		tfCodPais.clear();
		tfDesPais.clear();
		lbModo.setText("");
	}

	public void limpiarFormularioNoTabla() {
		tfProvincia.clear();
		tfPoblacion.clear();
		tfLocalidad.clear();
		tfDomicilio.clear();
		tfDescripcion.clear();
		tfCodigoPostal.clear();
		tfCodPais.clear();
		tfDesPais.clear();
		lbModo.setText("");
	}

	/**
	 * Realiza la carga de los datos de las direcciones.
	 */
	public void cargarDatosDirecciones(FidelizadosBean fidelizado) {
		Log.debug("cargarDatosDirecciones() - Iniciamos la carga de los datos de Direcciones del Cliente...");

		limpiarListadoDirecciones();

		if (fidelizado != null) {
			List<DireccionFidelizadoBean> listadoDirecciones = fidelizado.getDirecciones();

			if (!listadoDirecciones.isEmpty()) {
				for (DireccionFidelizadoBean direccionFidelizado : listadoDirecciones) {

					// Cargaremos la dirección principal en la posición 0
					// Primero comprobamos que exista una dirección principal (tabla F_FIDELIZADOS_TBL)
					if (fidelizado.getDomicilio() != null && !fidelizado.getDomicilio().isEmpty()) {

						// Tendremos que comparar todos los campos (la descripcion es parte de la PK pero no la tenemos
						// en la dirección principal) para asegurarnos de que las direcciones son las
						// mismas y así insertar la dirección principal en la posición 0

						if ((fidelizado.getUidInstancia().equals(direccionFidelizado.getUidInstancia())) && (fidelizado.getIdFidelizado() == direccionFidelizado.getIdFidelizado())
						        && (fidelizado.getDomicilio().equals(direccionFidelizado.getDomicilio())) && (fidelizado.getPoblacion().equals(direccionFidelizado.getPoblacion()))
						        && (fidelizado.getProvincia().equals(direccionFidelizado.getProvincia())) && (fidelizado.getLocalidad().equals(direccionFidelizado.getLocalidad()))
						        && (fidelizado.getCp().equals(direccionFidelizado.getCp())) && (fidelizado.getCodPais().equals(direccionFidelizado.getCodPais()))) {

							direccionesInicial.add(0, direccionFidelizado);
							direcciones.add(0, direccionFidelizado);

						}
						else {
							direccionesInicial.add(direccionFidelizado);
							direcciones.add(direccionFidelizado);
						}

					}
					else {
						direccionesInicial.add(direccionFidelizado);
						direcciones.add(direccionFidelizado);
					}
					
					Log.debug("cargarDatosDirecciones() - Dirección cargada : ");
					Log.debug("cargarDatosDirecciones() - Descripcion : " + direccionFidelizado.getDescripcionDireccion());
					Log.debug("cargarDatosDirecciones() - Domicilio : " + direccionFidelizado.getDomicilio());
					Log.debug("cargarDatosDirecciones() - Localidad : " + direccionFidelizado.getLocalidad());
					Log.debug("cargarDatosDirecciones() - Población : " + direccionFidelizado.getPoblacion());
					Log.debug("cargarDatosDirecciones() - Provincia : " + direccionFidelizado.getProvincia());
				}
			}
			else {
				if (fidelizado.getDomicilio() != null && !fidelizado.getDomicilio().isEmpty()) {
					DireccionFidelizadoBean domicilioFidelizado = new DireccionFidelizadoBean();
					domicilioFidelizado.setDomicilio(fidelizado.getDomicilio());
					domicilioFidelizado.setPoblacion(fidelizado.getPoblacion());
					domicilioFidelizado.setLocalidad(fidelizado.getLocalidad());
					domicilioFidelizado.setProvincia(fidelizado.getProvincia());
					domicilioFidelizado.setCp(fidelizado.getCp());
					domicilioFidelizado.setCodPais(fidelizado.getCodPais());
					domicilioFidelizado.setDesPais(fidelizado.getDesPais());
					domicilioFidelizado.setUidInstancia(fidelizado.getUidInstancia());
					domicilioFidelizado.setIdFidelizado(fidelizado.getIdFidelizado());
					domicilioFidelizado.setDescripcionDireccion(DESCRIPCION_DIRECCION_PRINCIPAL);

					direccionesInicial.add(0, domicilioFidelizado);
					direcciones.add(0, domicilioFidelizado);
				}
			}
		}
		else {
			String mensajeAviso = "No se han encontrado datos de direcciones del Cliente";
			Log.debug("cargarDatosDirecciones() - " + mensajeAviso);
		}
		Log.debug("cargarDatosDirecciones() - Finalizada la carga de los datos de Direcciones del Cliente");
	}

	public void camposEditables(boolean editable) {
		tfProvincia.setEditable(editable);
		tfPoblacion.setEditable(editable);
		tfLocalidad.setEditable(editable);
		tfDomicilio.setEditable(editable);
		tfCodigoPostal.setEditable(editable);
		tfCodPais.setEditable(editable);

		btBuscarPais.setDisable(!editable);

		btInsertarDireccion.setDisable(editable);
		btEditarDireccion.setDisable(editable);
		btEliminarDireccion.setDisable(editable);
	}

	public void activarBotoneraDirecciones(boolean activar) {
		btInsertarDireccion.setDisable(!activar);
		btEditarDireccion.setDisable(!activar);
		btEliminarDireccion.setDisable(!activar);

	}

	@FXML
	public void insertarDirecciones() {
		limpiarFormularioNoTabla();
		lbModo.setText(ByLFidelizadoController.MODO_INSERCION);

		ByLPaneDatosGeneralesController panelDatosGenerales = SpringContext.getBean(ByLPaneDatosGeneralesController.class);
		tfCodPais.setText(panelDatosGenerales.getTfCodPais().getText());
		tfDesPais.setText(panelDatosGenerales.getTfDesPais().getText());

		bloquearFormulario(Boolean.TRUE);
		activarConfirmarCambios(Boolean.FALSE);
		getTabParentController().habilitarBotonEditar(false);
	}

	@FXML
	public void editarDirecciones() {
		if (tvDirecciones.getSelectionModel().getSelectedItem() != null) {
			limpiarFormularioNoTabla();
			lbModo.setText(ByLFidelizadoController.MODO_EDICION);
			cargarDatosDireccion();
			activarConfirmarCambios(Boolean.FALSE);
			bloquearFormulario(Boolean.TRUE);
			getTabParentController().habilitarBotonEditar(false);
		}
		else {
			Log.debug("editarDirecciones() - Para editar una Dirección debe seleccionar una");
			VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("Para editar una Dirección debe seleccionar una"), getTabParentController().getStage());
		}
	}

	/**
	 * Carga los datos de la dirección en el formulario.
	 */
	public void cargarDatosDireccion() {
		DireccionFidelizadoBean direccionSeleccionada = tvDirecciones.getSelectionModel().getSelectedItem();
		tfDescripcion.setText(direccionSeleccionada.getDescripcionDireccion());
		tfDomicilio.setText(direccionSeleccionada.getDomicilio());
		tfProvincia.setText(direccionSeleccionada.getProvincia());
		tfPoblacion.setText(direccionSeleccionada.getPoblacion());
		/* Campos no Obligatorios */
		if (direccionSeleccionada.getLocalidad() != null && StringUtils.isNotBlank(direccionSeleccionada.getLocalidad())) {
			tfLocalidad.setText(direccionSeleccionada.getLocalidad());
		}
		else {
			tfLocalidad.setText("");
		}
		if (direccionSeleccionada.getCp() != null && StringUtils.isNotBlank(direccionSeleccionada.getCp())) {
			tfCodigoPostal.setText(direccionSeleccionada.getCp());
		}
		else {
			tfCodigoPostal.setText("");
		}

		if (direccionSeleccionada.getCodPais() != null && StringUtils.isNotBlank(direccionSeleccionada.getCodPais())) {
			tfCodPais.setText(direccionSeleccionada.getCodPais());
		}
		else {
			tfCodPais.setText("");
		}

		if (direccionSeleccionada.getDesPais() != null && StringUtils.isNotBlank(direccionSeleccionada.getDesPais())) {
			tfDesPais.setText(direccionSeleccionada.getDesPais());
		}
		else {
			tfDesPais.setText("");
		}
	}

	@FXML
	public void eliminarDirecciones() {
		if (tvDirecciones.getSelectionModel().getSelectedItem() != null) {
			if (VentanaDialogoComponent.crearVentanaConfirmacion(I18N.getTexto("¿Está seguro de eliminar la Dirección seleccionada?"), getTabParentController().getStage())) {
				direcciones.remove(tvDirecciones.getSelectionModel().getSelectedItem());

				tvDirecciones.requestFocus();
				tvDirecciones.getSelectionModel().select(null);
				tvDirecciones.getFocusModel().focus(null);
			}

		}
		else {
			Log.debug("eliminarDirecciones() - Para eliminar una Dirección debe seleccionar una");
			VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("Para eliminar una Dirección debe seleccionar una"), getTabParentController().getStage());
		}
	}

	@FXML
	public void confirmarCambios() {
		DireccionFidelizadoBean direccionInsertar = new DireccionFidelizadoBean();
		int index = 0;

		try {

			if (ByLFidelizadoController.MODO_EDICION.equals(lbModo.getText())) {
				index = tvDirecciones.getSelectionModel().getSelectedIndex();
				BeanUtils.copyProperties(direccionInsertar, tvDirecciones.getSelectionModel().getSelectedItem());
				direcciones.remove(tvDirecciones.getSelectionModel().getSelectedItem());
			}
			String mensajeError = "";

			if (StringUtils.isNotBlank(tfCodPais.getText())) {
				direccionInsertar.setCodPais(tfCodPais.getText());
			}

			if (StringUtils.isNotBlank(tfDesPais.getText())) {
				direccionInsertar.setDesPais(tfDesPais.getText());
			}

			/*
			 * Rellenamos solo los datos que necesitamos en esta pantalla, el resto los rellenaremos a la hora de
			 * insertarlos en BD.
			 */
			if (StringUtils.isNotBlank(tfDescripcion.getText())) {
				if (!existeDescripcion(tfDescripcion.getText())) {
					direccionInsertar.setDescripcionDireccion(tfDescripcion.getText());
				}
				else {
					Log.debug("confirmarCambios() - Ya existe una dirección con esa descripción");
					VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("Ya existe una dirección con esa descripción"), getTabParentController().getStage());
					tfDescripcion.requestFocus();
					return;
				}

			}
			else {
				mensajeError = "La Descripción es obligatoria para una Dirección";
				throw new ValidarDireccionesException(mensajeError);
			}

			if (StringUtils.isNotBlank(tfDomicilio.getText())) {
				direccionInsertar.setDomicilio(tfDomicilio.getText());
			}
			else {
				mensajeError = "El Domicilio es obligatorio para una Dirección";
				throw new ValidarDireccionesException(mensajeError);
			}
			if (StringUtils.isNotBlank(tfPoblacion.getText())) {
				direccionInsertar.setPoblacion(tfPoblacion.getText());
			}
			else {
				mensajeError = "La Poblacion es obligatoria para una Dirección";
				throw new ValidarDireccionesException(mensajeError);
			}
			if (StringUtils.isNotBlank(tfLocalidad.getText())) {
				direccionInsertar.setLocalidad(tfLocalidad.getText());
			}
			if (StringUtils.isNotBlank(tfProvincia.getText())) {
				// TODO Desarrollo de validación de Provincias.
				/* Si la validación es correcta pasamos a insertarlo */
				direccionInsertar.setProvincia(tfProvincia.getText());
			}
			else {
				mensajeError = "La Provincia es obligatoria para una Dirección";
				throw new ValidarDireccionesException(mensajeError);
			}
			if (StringUtils.isNotBlank(tfCodigoPostal.getText())) {
				if (StringUtils.isNumeric(tfCodigoPostal.getText())) {
					direccionInsertar.setCp(tfCodigoPostal.getText());
				}
				else {
					mensajeError = "El Código Postal solo puede contener números";
					throw new ValidarDireccionesException(mensajeError);
				}
			}
		}
		catch (ValidarDireccionesException e) {
			Log.debug("confirmarCambios() - Error de validación de datos : " + e.getMessage(), e);
			VentanaDialogoComponent.crearVentanaAviso(e.getMessage(), getTabParentController().getStage());
			return;
		}
		catch (IllegalAccessException | InvocationTargetException e) {
			Log.debug("confirmarCambios() - Error de validación de datos : " + e.getMessage(), e);
			VentanaDialogoComponent.crearVentanaAviso(e.getMessage(), getTabParentController().getStage());
			return;
		}

		// Si editamos un registro, para que se vuelva a insertar en la posición que estaba
		if (ByLFidelizadoController.MODO_EDICION.equals(lbModo.getText())) {
			direcciones.add(index, direccionInsertar);
		}
		else {
			// Lo insertamos en la ultima posición
			direcciones.add(direcciones.size(), direccionInsertar);
		}

		tvDirecciones.requestFocus();
		tvDirecciones.getSelectionModel().select(0);
		tvDirecciones.getFocusModel().focus(null);

		/* Realizamos acciones de limpieza y bloqueo del formulario */
		limpiarFormularioNoTabla();
		activarConfirmarCambios(Boolean.TRUE);
		bloquearFormulario(Boolean.FALSE);
		lbModo.setText("");

		refrescarPantalla();

		getTabParentController().habilitarBotonEditar(true);

	}

	protected boolean existeDescripcion(String descripcion) {
		boolean existe = false;

		if (direcciones != null && !direcciones.isEmpty()) {
			for (int i = 0; i < direcciones.size() && !existe; i++) {

				if (direcciones.get(i).getDescripcionDireccion().equals(descripcion)) {
					existe = true;
				}

			}
		}
		return existe;
	}

	@FXML
	public void cargaTiposDocumento() {
		log.debug("cargaTiposDocumento() - Iniciamos la carga de los Tipos de País...");
		String codPais = tfCodPais.getText();
		if (codPais != null) {
			try {
				/* Realizamos la búsqueda del País */
				PaisBean pais = paisService.consultarCodPais(codPais);
				tfDesPais.setText(pais.getDesPais());

				log.debug("cargaTiposDocumento() - Finalizada la carga de País");
			}
			catch (Exception e) {
				if (e instanceof PaisNotFoundException) {
					log.error("cargaTiposDocumento() - No se ha encontrado ningún resultado al realizar la búsqueda del País indicado - " + e.getMessage(), e);
					VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("No se ha encontrado ningún resultado al realizar la búsqueda del País indicado"), getTabParentController().getStage());
				}
				else if (e instanceof PaisServiceException) {
					log.error("cargaTiposDocumento() - Error al realizar la búsqueda del País indicado - " + e.getMessage(), e);
					VentanaDialogoComponent.crearVentanaError(getTabParentController().getStage(), I18N.getTexto("Error al realizar la búsqueda del País indicado"), e);
				}
				else {
					log.error("cargaTiposDocumento() - Se ha producido un error sin controlar - " + e.getMessage(), e);
					VentanaDialogoComponent.crearVentanaError(getTabParentController().getStage(), I18N.getTexto("Se ha producido un error sin controlar"), e);
				}
			}
		}
	}

	@FXML
	public void accionBuscarPais() {
		getTabParentController().getApplication().getMainView().showModalCentered(PaisesView.class, getTabParentController().getDatos(), getTabParentController().getStage());

		if (getTabParentController().getDatos() != null && getTabParentController().getDatos().containsKey(PaisesController.PARAMETRO_SALIDA_PAIS)) {
			PaisBean pais = (PaisBean) getTabParentController().getDatos().get(PaisesController.PARAMETRO_SALIDA_PAIS);
			tfDesPais.setText(pais.getDesPais());
			tfCodPais.setText(pais.getCodPais().toUpperCase());
			cargaTiposDocumento();
		}
	}

	private void refrescarPantalla() {
		tvDirecciones.getColumns().get(0).setVisible(false);
		tvDirecciones.getColumns().get(0).setVisible(true);
	}

	public void volverInicio() {
		tvDirecciones.getItems().clear();
		direcciones.clear();
		direcciones.addAll(direccionesInicial);
	}

	/**
	 * Activa o desactiva el botón de confirmar cambios.
	 * 
	 * @param activar
	 *            : Indica si activar o no
	 */
	public void activarConfirmarCambios(boolean desactivar) {
		btConfirmarCambios.setDisable(desactivar);
	}

	public TextField getTfCodigoPostal() {
		return tfCodigoPostal;
	}

	public TextField getTfProvincia() {
		return tfProvincia;
	}

	public TextField getTfPoblacion() {
		return tfPoblacion;
	}

	public TextField getTfLocalidad() {
		return tfLocalidad;
	}

	public TextField getTfDomicilio() {
		return tfDomicilio;
	}

	public TextField getTfDescripcion() {
		return tfDescripcion;
	}

	public TextField getTfCodPais() {
		return tfCodPais;
	}

	public TextField getTfDesPais() {
		return tfDesPais;
	}

	public ObservableList<DireccionFidelizadoBean> getDirecciones() {
		return direcciones;
	}

	public void limpiarListadoDirecciones() {
		direccionesInicial.clear();
		direcciones.clear();
		tvDirecciones.getItems().clear();
	}

	public FidelizadosBean getFidelizado() {
		return fidelizado;
	}

	public void setFidelizado(FidelizadosBean fidelizado) {
		this.fidelizado = fidelizado;
	}

}
