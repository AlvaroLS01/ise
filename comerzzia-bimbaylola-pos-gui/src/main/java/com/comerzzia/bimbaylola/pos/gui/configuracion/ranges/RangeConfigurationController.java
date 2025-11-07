package com.comerzzia.bimbaylola.pos.gui.configuracion.ranges;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.comerzzia.bimbaylola.pos.gui.configuracion.ranges.modal.NewRangeView;
import com.comerzzia.bimbaylola.pos.services.core.config.configContadores.rangos.CounterRangeDto;
import com.comerzzia.bimbaylola.pos.services.core.config.configContadores.rangos.CounterRangeManager;
import com.comerzzia.pos.core.gui.BackgroundTask;
import com.comerzzia.pos.core.gui.InitializeGuiException;
import com.comerzzia.pos.core.gui.POSApplication;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.core.gui.controllers.Controller;
import com.comerzzia.pos.core.gui.tablas.celdas.CellFactoryBuilder;
import com.comerzzia.pos.util.i18n.I18N;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

@Component
public class RangeConfigurationController extends Controller {

	protected Logger log = Logger.getLogger(getClass());

	@Autowired
	protected CounterRangeManager counterRangeManager;

	@FXML
	protected Button btConfirmChanges, btAddNewRange;

	@FXML
	protected Label lbError, lbTitle;

	@FXML
	protected TableView<CounterRangeDto> tbRanges;

	@FXML
	protected TableColumn<CounterRangeDto, String> tcCounterId, tcDivider1, tcDivider2, tcDivider3;

	@FXML
	protected TableColumn<CounterRangeDto, HBox> tcActions, tcRange;

	protected ObservableList<CounterRangeDto> lines;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		log.debug("initialize()");
	}

	@Override
	public void initializeComponents() throws InitializeGuiException {
		log.debug("initializeComponents()");

		registraEventoTeclado(new EventHandler<KeyEvent>(){

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.ENTER)) {
					confirmChanges();
				}
			}

		}, KeyEvent.KEY_RELEASED);

	}

	@Override
	public void initializeForm() throws InitializeGuiException {
		log.debug("initializeForm()");

		lbError.setText("");
		clearTable();
		initTable();
		new LoadTableDataTask().start();
	}

	@Override
	public void initializeFocus() {
		// Does not need implementation
	}

	@SuppressWarnings("unchecked")
	protected void initTable() {
		log.debug("initTable() - initializing table");

		tbRanges.setPlaceholder(new Label(""));

		tcCounterId.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbRanges", "tcCounterId", null, CellFactoryBuilder.ESTILO_ALINEACION_IZQ));
		tcDivider1.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbRanges", "tcDivider1", null, CellFactoryBuilder.ESTILO_ALINEACION_IZQ));
		tcDivider2.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbRanges", "tcDivider2", null, CellFactoryBuilder.ESTILO_ALINEACION_IZQ));
		tcDivider3.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbRanges", "tcDivider3", null, CellFactoryBuilder.ESTILO_ALINEACION_IZQ));

		tcCounterId.setCellValueFactory(new PropertyValueFactory<CounterRangeDto, String>("counterId"));
		tcDivider1.setCellValueFactory(new PropertyValueFactory<CounterRangeDto, String>("divisor1"));
		tcDivider2.setCellValueFactory(new PropertyValueFactory<CounterRangeDto, String>("divisor2"));
		tcDivider3.setCellValueFactory(new PropertyValueFactory<CounterRangeDto, String>("divisor3"));

		tcActions.setCellValueFactory(actionsValueFactory());
		tcRange.setCellValueFactory(rangesValueFactory());
	}

	public Callback<TableColumn.CellDataFeatures<CounterRangeDto, HBox>, ObservableValue<HBox>> actionsValueFactory() {
		log.debug("actionsValueFactory() - Creating ValueFactory for Actions column");

		return new Callback<TableColumn.CellDataFeatures<CounterRangeDto, HBox>, ObservableValue<HBox>>(){

			@Override
			public ObservableValue<HBox> call(CellDataFeatures<CounterRangeDto, HBox> param) {
				CounterRangeDto counterRangeDto = param.getValue();

				return new ObservableValueBase<HBox>(){

					@Override
					public HBox getValue() {
						final HBox hboxDelete = createHboxDelete(counterRangeDto);
						return hboxDelete;
					}
				};
			}
		};
	}

	protected Callback<TableColumn.CellDataFeatures<CounterRangeDto, HBox>, ObservableValue<HBox>> rangesValueFactory() {
		log.debug("actionsValueFactory() - Creating ValueFactory for Actions column");

		return new Callback<TableColumn.CellDataFeatures<CounterRangeDto, HBox>, ObservableValue<HBox>>(){

			@Override
			public ObservableValue<HBox> call(CellDataFeatures<CounterRangeDto, HBox> param) {
				CounterRangeDto counterRangeDto = param.getValue();

				return new ObservableValueBase<HBox>(){

					@Override
					public HBox getValue() {
						final HBox hboxDelete = createHboxRange(counterRangeDto);
						return hboxDelete;
					}

				};
			}
		};
	}

	protected HBox createHboxDelete(CounterRangeDto counterRangeDto) {
		log.trace("createHboxDelete()");

		final HBox hboxDelete = new HBox();
		Button deleteButton = createDeleteButton(counterRangeDto);
		hboxDelete.getChildren().add(deleteButton);
		hboxDelete.setAlignment(Pos.BOTTOM_CENTER);
		hboxDelete.setSpacing(30);

		return hboxDelete;
	}

	protected Button createDeleteButton(CounterRangeDto counterRangeDto) {
		log.trace("createDeleteButton()");

		Button btDelete = new Button();
		Image imgDelete = POSApplication.getInstance().createImage("iconos/cancelar.png");
		ImageView imagViewDelete = new ImageView(imgDelete);
		btDelete.setGraphic(imagViewDelete);
		btDelete.setOnAction(deleteEventHandler());
		btDelete.setUserData(counterRangeDto);

		return btDelete;
	}

	protected HBox createHboxRange(CounterRangeDto counterRangeDto) {
		log.trace("createHboxRange()");

		final TextField tfRange = createTextFieldRange(counterRangeDto);

		final HBox hboxDelete = new HBox();
		hboxDelete.getChildren().add(tfRange);
		hboxDelete.setAlignment(Pos.BOTTOM_CENTER);
		hboxDelete.setSpacing(30);

		return hboxDelete;
	}

	protected TextField createTextFieldRange(CounterRangeDto counterRangeDto) {

		final TextField tfRange = new TextField();
		tfRange.setUserData(counterRangeDto);
		tfRange.setText(counterRangeDto.getRangeId());
		tfRange.textProperty().addListener(rangeChangeListener(tfRange));
		addSeleccionarTodoEnFoco(tfRange);

		return tfRange;
	}

	protected ChangeListener<String> rangeChangeListener(final TextField tfRange) {
		return new ChangeListener<String>(){

			public void changed(final ObservableValue<? extends String> observableValue, final String oldValue, final String newValue) {
				CounterRangeDto userData = (CounterRangeDto) tfRange.getUserData();
				userData.setRangeId(newValue);
			}
		};
	}

	protected EventHandler<ActionEvent> deleteEventHandler() {
		return new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				log.debug("deleteEventHandler() - Deleting range");

				try {
					CounterRangeDto counterRangeDto = (CounterRangeDto) ((Node) event.getSource()).getUserData();
					if (VentanaDialogoComponent.crearVentanaConfirmacion(I18N.getTexto("¿Desea borrar el rango fiscal seleccionado?"), getStage())) {
						counterRangeManager.deleteRange(counterRangeDto);
						new LoadTableDataTask().start();
					}
				}
				catch (Exception e) {
					log.error("deleteEventHandler() - Error deleting range");
					VentanaDialogoComponent.crearVentanaError(getStage(), I18N.getTexto("Error borrando rango"), e);
				}
			}

		};
	}

	protected void clearTable() {
		log.debug("clearTable()");

		tbRanges.getItems().clear();
		lines = FXCollections.observableList(new ArrayList<CounterRangeDto>());
	}

	@FXML
	protected void confirmChanges() {
		log.debug("confirmChanges()");

		if (!validateData()) {
			return;
		}

		if (VentanaDialogoComponent.crearVentanaConfirmacion(I18N.getTexto("¿Desea guardar cambios?"), getStage())) {
			saveRanges();
		}
	}

	protected boolean validateData() {
		log.debug("validateData()");

		lbError.setText("");
		for (CounterRangeDto dto : tbRanges.getItems()) {
			if (StringUtils.isBlank(dto.getRangeId())) {
				lbError.setText(I18N.getTexto("Ningún rango puede estar vacío"));
				tbRanges.getSelectionModel().select(dto);

				return false;
			}
		}

		return true;
	}

	protected void saveRanges() {
		log.debug("saveRanges() - Saving ranges");

		try {
			for (CounterRangeDto counterRangeDto : tbRanges.getItems()) {
				counterRangeManager.saveRange(counterRangeDto, counterRangeDto.getRangeId());
			}

			new LoadTableDataTask().start();
		}
		catch (Exception e) {
			log.error("saveRanges() - Error saving ranges");
			VentanaDialogoComponent.crearVentanaError(getStage(), I18N.getTexto("Error borrando rango"), e);
		}
	}

	@FXML
	protected void addNewRangeAction(ActionEvent event) {
		log.debug("addNewRangeAction - Adding new Range");

		HashMap<String, Object> datos = new HashMap<String, Object>();
		getApplication().getMainView().showModalCentered(NewRangeView.class, datos, getStage());
		new LoadTableDataTask().start();
	}

	protected void loadTableDataSucceeded(List<CounterRangeDto> ranges) {
		log.debug("loadTableDataSucceeded()");

		ObservableList<CounterRangeDto> observableList = FXCollections.observableList(new ArrayList<CounterRangeDto>(ranges));
		tbRanges.setItems(observableList);
	}

	protected void loadTableDataFailed(Throwable e) {

		String errorMsg = I18N.getTexto("Error cargando datos en la tabla: "+e.getMessage());
		VentanaDialogoComponent.crearVentanaError(getStage(), errorMsg, e);
	}

	public class LoadTableDataTask extends BackgroundTask<Boolean> {

		protected List<CounterRangeDto> ranges;

		@Override
		protected Boolean call() throws Exception {
			clearTable();
			ranges = counterRangeManager.findAllRanges();
			return true;
		}

		@Override
		protected void failed() {
			super.failed();
			Throwable e = getException();
			loadTableDataFailed(e);
		}

		@Override
		protected void succeeded() {
			super.succeeded();
			loadTableDataSucceeded(ranges);
		}
	}

	@SuppressWarnings("rawtypes")
	protected void addSeleccionarTodoEnFoco(final TextField campo) {
		campo.focusedProperty().addListener(new ChangeListener<Boolean>(){

			@Override
			public void changed(ObservableValue ov, Boolean t, Boolean t1) {
				Platform.runLater(new Runnable(){

					@Override
					public void run() {
						if (campo.isFocused() && !campo.getText().isEmpty()) {
							campo.selectAll();
						}
					}
				});
			}
		});
	}
	
	
	
	
	
}
