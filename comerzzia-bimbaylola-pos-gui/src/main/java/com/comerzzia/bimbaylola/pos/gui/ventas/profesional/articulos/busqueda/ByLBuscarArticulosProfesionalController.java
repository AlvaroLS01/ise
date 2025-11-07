package com.comerzzia.bimbaylola.pos.gui.ventas.profesional.articulos.busqueda;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.comerzzia.bimbaylola.pos.gui.ventas.devoluciones.ByLDevolucionesController;
import com.comerzzia.bimbaylola.pos.persistence.articulos.buscar.ByLArticuloBuscarBean;
import com.comerzzia.pos.core.gui.BackgroundTask;
import com.comerzzia.pos.core.gui.InitializeGuiException;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.core.gui.tablas.celdas.CellFactoryBuilder;
import com.comerzzia.pos.gui.ventas.profesional.articulos.busqueda.BuscarArticulosProfesionalController;
import com.comerzzia.pos.gui.ventas.tickets.articulos.busquedas.FormularioBusquedaArtBean;
import com.comerzzia.pos.gui.ventas.tickets.articulos.busquedas.LineaResultadoBusqGui;
import com.comerzzia.pos.persistence.articulos.buscar.ArticuloBuscarBean;
import com.comerzzia.pos.persistence.articulos.buscar.ArticulosParamBuscar;
import com.comerzzia.pos.persistence.clientes.ClienteBean;
import com.comerzzia.pos.services.articulos.ArticulosService;
import com.comerzzia.pos.services.core.sesion.Sesion;
import com.comerzzia.pos.services.core.variables.VariablesServices;
import com.comerzzia.pos.services.ticket.lineas.LineaTicket;
import com.comerzzia.pos.util.config.AppConfig;
import com.comerzzia.pos.util.config.SpringContext;
import com.comerzzia.pos.util.format.FormatUtil;
import com.comerzzia.pos.util.i18n.I18N;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

@Component
@Primary
public class ByLBuscarArticulosProfesionalController extends BuscarArticulosProfesionalController {

	private static final Logger log = Logger.getLogger(ByLBuscarArticulosProfesionalController.class.getName());
	public static final String COD_USA="US";
	
    @Autowired
    private Sesion sesion;

	@FXML
	protected TableColumn<LineaResultadoBusqGui, String> tcArticulosEAN;
	@FXML
	protected Label lblPrecioIva;
	
   
	private Map<String, String> mapaEAN = new HashMap<String, String>();
	
    @Autowired
    private ArticulosService articulosService;
    @Autowired
    private VariablesServices variablesServices;

    @SuppressWarnings("unchecked")
	@Override
	public void initialize(URL url, ResourceBundle rb) {

		log.debug("initialize() - Inicializando ventana...");

		/* Mensaje sin contenido para tabla. los establecemos a vacío. */
		tbArticulos.setPlaceholder(new Label(""));
		tbArticulos.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<LineaResultadoBusqGui>(){

			@Override
			public void changed(ObservableValue<? extends LineaResultadoBusqGui> observable, LineaResultadoBusqGui oldValue, LineaResultadoBusqGui newValue) {
				if (newValue != null && AppConfig.rutaImagenes != null) {
					imagenArticulo.mostrarImagen(newValue.getCodArticulo());
				}
				lineaSeleccionadaChanged();
			}
		});

		frBusquedaArt = SpringContext.getBean(FormularioBusquedaArtBean.class);
		/* Se asignan los campos del formulario a los componentes a validar. */
		frBusquedaArt.setFormField("codArticulo", tfCodigoArt);
		frBusquedaArt.setFormField("descripcion", tfDescripcion);

		/* Centrado con estilo a la derecha */
		tcArticulosArticulo.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbArticulos", "tcArticulosArticulo", null, CellFactoryBuilder.ESTILO_ALINEACION_IZQ));
		tcArticulosDescripcion.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbArticulos", "tcArticulosDescripcion", null, CellFactoryBuilder.ESTILO_ALINEACION_IZQ));

		lineas = FXCollections.observableList(new ArrayList<LineaResultadoBusqGui>());

		/* Asignamos las lineas a la tabla. */
		tbArticulos.setItems(lineas);

		/* Definimos un factory para cada celda para aumentar el rendimiento. */
		/* ================= EAN ================= */
		tcArticulosEAN.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LineaResultadoBusqGui, String>, ObservableValue<String>>(){

			@Override
			public ObservableValue<String> call(CellDataFeatures<LineaResultadoBusqGui, String> cdf) {
				String keyDesgloses = cdf.getValue().getCodArticulo() + " " + cdf.getValue().getDesglose1() + " " + cdf.getValue().getDesglose2();
				return new SimpleStringProperty(mapaEAN.get(keyDesgloses));
			}
		});
		/* ================= ARTICULO ================= */
		tcArticulosArticulo.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LineaResultadoBusqGui, String>, ObservableValue<String>>(){

			@Override
			public ObservableValue<String> call(CellDataFeatures<LineaResultadoBusqGui, String> cdf) {
				return cdf.getValue().getArtProperty();
			}
		});
		/* ================= DESGLOSE 1 ================= */
		tcArticulosDesglose1.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LineaResultadoBusqGui, String>, ObservableValue<String>>(){

			@Override
			public ObservableValue<String> call(CellDataFeatures<LineaResultadoBusqGui, String> cdf) {
				return cdf.getValue().getDesglose1Property();
			}
		});
		/* ================= DESGLOSE 2 ================= */
		tcArticulosDesglose2.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LineaResultadoBusqGui, String>, ObservableValue<String>>(){

			@Override
			public ObservableValue<String> call(CellDataFeatures<LineaResultadoBusqGui, String> cdf) {
				return cdf.getValue().getDesglose2Property();
			}
		});
		/* ================= DESCRIPCIÓN ================= */
		tcArticulosDescripcion.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LineaResultadoBusqGui, String>, ObservableValue<String>>(){

			@Override
			public ObservableValue<String> call(CellDataFeatures<LineaResultadoBusqGui, String> cdf) {
				return cdf.getValue().getDescripcionProperty();
			}
		});

		log.debug("inicializarComponentes() - Configuración de la tabla");

		/* Si hay desglose 1, establecemos el texto. */
		if (sesion.getAplicacion().isDesglose1Activo()) {
			tcArticulosDesglose1.setText(I18N.getTexto(variablesServices.getVariableAsString(VariablesServices.ARTICULO_DESGLOSE1_TITULO)));
			lbDesglose1.setText(I18N.getTexto(variablesServices.getVariableAsString(VariablesServices.ARTICULO_DESGLOSE1_TITULO)));
			tfDetalleDesglose1.setVisible(true);
			/* Sino ahi desgloses, compactamos la línea. */
		}
		else {
			tcArticulosDesglose1.setMinWidth(0);
			tcArticulosDesglose1.setMaxWidth(0);
			tcArticulosDesglose1.setPrefWidth(0);
			lbDesglose1.setText("");
			tfDetalleDesglose1.setVisible(false);
		}
		/* Si hay desglose 2, establecemos el texto. */
		if (sesion.getAplicacion().isDesglose2Activo()) {
			tcArticulosDesglose2.setText(I18N.getTexto(variablesServices.getVariableAsString(VariablesServices.ARTICULO_DESGLOSE2_TITULO)));
			lbDesglose2.setText(I18N.getTexto(variablesServices.getVariableAsString(VariablesServices.ARTICULO_DESGLOSE2_TITULO)));
			tfDetalleDesglose2.setVisible(true);
			/* Sino ahi desgloses, compactamos la línea. */
		}
		else {
			tcArticulosDesglose2.setMinWidth(0);
			tcArticulosDesglose2.setMaxWidth(0);
			tcArticulosDesglose2.setPrefWidth(0);
			lbDesglose2.setText("");
			tfDetalleDesglose2.setVisible(false);
		}
		
		String codPais = sesion.getAplicacion().getTienda().getCliente().getCodpais();
		/* Para el menu profesional o para Usa se ocultaran campo iva y recargo */
		if (AppConfig.menu.equals(ByLDevolucionesController.MENU_POS_PROFESIONAL) && codPais.equals(COD_USA) ) {
			tfPrecioIva.setVisible(false);
			lblPrecioIva.setVisible(false);
		}

	}
    
	@Override
	public void initializeForm() throws InitializeGuiException {
		log.debug("initializeForm() - Inicializando formulario...");
		lineas = FXCollections.observableList(new ArrayList<LineaResultadoBusqGui>());
		tbArticulos.setItems(lineas);

		tfCodigoArt.clear();
		tfDescripcion.clear();
		tfDetalleDescripcion.clear();
		tfDetalleCodArticulo.clear();
		tfDetalleDesglose1.clear();
		tfDetalleDesglose2.clear();
		tfDetallePrecio.clear();
		tfPrecioIva.clear();

		limpiarPanelPromociones();

		codTarifaBusqueda = (String) getDatos().get(PARAMETRO_ENTRADA_CODTARIFA);
		if (codTarifaBusqueda == null) {
			codTarifaBusqueda = sesion.getAplicacion().getTienda().getCliente().getCodtar();
		}
		clienteBusqueda = (ClienteBean) getDatos().get(PARAMETRO_ENTRADA_CLIENTE);
		if (clienteBusqueda == null) {
			clienteBusqueda = sesion.getAplicacion().getTienda().getCliente();
		}
		esStock = (Boolean) getDatos().get(PARAMETRO_ES_STOCK);
		if (esStock == null) {
			esStock = Boolean.TRUE;
		}

		if (imagenArticulo != null) {
			imagenArticulo.setImage(null);
		}
		if (esStock) {
			hbVentas.setVisible(Boolean.FALSE);
			hbVentas.setManaged(Boolean.FALSE);
			hbStocks.setVisible(Boolean.TRUE);
			hbStocks.setManaged(Boolean.TRUE);
			if (sesion.getAplicacion().isDesglose1Activo()) { // Si hay desglose 1, establecemos el texto
				btStocksPorDesglose1Stocks.setText(I18N.getTexto("Stocks por ") + I18N.getTexto(variablesServices.getVariableAsString(VariablesServices.ARTICULO_DESGLOSE1_TITULO).toLowerCase()));
			}
			if (sesion.getAplicacion().isDesglose2Activo()) { // Si hay desglose 1, establecemos el texto
				btStocksPorDesglose2Stocks.setText(I18N.getTexto("Stocks por ") + I18N.getTexto(variablesServices.getVariableAsString(VariablesServices.ARTICULO_DESGLOSE2_TITULO).toLowerCase()));
			}
		}
		else {
			hbVentas.setVisible(Boolean.TRUE);
			hbVentas.setManaged(Boolean.TRUE);
			hbStocks.setVisible(Boolean.FALSE);
			hbStocks.setManaged(Boolean.FALSE);
			if (sesion.getAplicacion().isDesglose1Activo()) { // Si hay desglose 1, establecemos el texto
				btStocksPorDesglose1Ventas.setText(I18N.getTexto("Stocks por ") + I18N.getTexto(variablesServices.getVariableAsString(VariablesServices.ARTICULO_DESGLOSE1_TITULO).toLowerCase()));
			}
			if (sesion.getAplicacion().isDesglose2Activo()) { // Si hay desglose 1, establecemos el texto
				btStocksPorDesglose2Ventas.setText(I18N.getTexto("Stocks por ") + I18N.getTexto(variablesServices.getVariableAsString(VariablesServices.ARTICULO_DESGLOSE2_TITULO).toLowerCase()));
			}
		}
		modal = (Boolean) getDatos().get(PARAM_MODAL);
		if (modal == null) {
			modal = Boolean.FALSE;
		}
		if (esStock && modal) {
			mostrarBoton(btCancelar);
		}
		else {
			ocultarBoton(btCancelar);
		}
	}
  
    @FXML
    public void accionBuscar(){
    	
        log.trace("accionBuscar()");

        /* Vaciamos el resultado de la busqueda anterior. */
        lineas.clear();
        mapaEAN.clear(); 
        
        /* Limpiamos los detalles del artículo de la posible selección anterior. */
        tfDetalleCodArticulo.setText("");
        tfDetalleDescripcion.setText("");
        tfDetallePrecio.setText("");
        tfDetalleDesglose1.setText("");
        tfDetalleDesglose2.setText("");
        
        frBusquedaArt.setCodArticulo(tfCodigoArt.getText());
        frBusquedaArt.setDescripcion(tfDescripcion.getText());
        
        if(validarFormularioBusqueda()){
        	
            ArticulosParamBuscar parametrosArticulo = new ArticulosParamBuscar();
            parametrosArticulo.setCodigo(tfCodigoArt.getText().toUpperCase());
            parametrosArticulo.setDescripcion(tfDescripcion.getText());
            parametrosArticulo.setCliente(clienteBusqueda);
            parametrosArticulo.setCodTarifa(codTarifaBusqueda);
            parametrosArticulo.setActivo(true);
            
            BuscarArticulosTask buscarArticulosTask = 
            		new BuscarArticulosTask(parametrosArticulo);
            buscarArticulosTask.start();
            
        }
    }
    
    protected class BuscarArticulosTask extends BackgroundTask<List<ArticuloBuscarBean>>{
    	
        private final ArticulosParamBuscar parametrosArticulo;
        
        public BuscarArticulosTask(ArticulosParamBuscar parametrosArticulo){
            this.parametrosArticulo = parametrosArticulo;
        }
        
        @Override
        protected List<ArticuloBuscarBean> call() throws Exception{
            return articulosService.buscarArticulos(parametrosArticulo);
        }

		@Override
        protected void succeeded(){
        	
            List<ArticuloBuscarBean> articulosF = getValue();
            List<LineaResultadoBusqGui> lineasRes = new ArrayList<LineaResultadoBusqGui>();
             
            if(articulosF.isEmpty()){
                tbArticulos.setPlaceholder(
                		new Label(I18N.getTexto("No se han encontrado resultados")));
            }
            
            for(ArticuloBuscarBean articulo: articulosF){
            	/* Creamos la linea que vamos a añadir. */
            	LineaResultadoBusqGui linea = new LineaResultadoBusqGui(articulo);
				
				/* Añadimos al mapa el Código de Articulo - Código de Barras. */
				String keyDesgloses = articulo.getCodArticulo() + " " + articulo.getValorDesglose1() + " " + articulo.getValorDesglose2();
				String codigoBarras = null;
				if(articulo instanceof ByLArticuloBuscarBean) {
					codigoBarras = ((ByLArticuloBuscarBean)articulo).getCodigoBarras();
				}
				mapaEAN.put(keyDesgloses, codigoBarras);
				
				lineasRes.add(linea);
            }

            lineas.addAll(lineasRes);
            
            tbArticulos.getSelectionModel().select(0);
            tbArticulos.getFocusModel().focus(0);
            
            super.succeeded();
            
        }
        
        @Override
        protected void failed(){
        	super.failed();
            log.error("accionBuscar() - Error consultando artículos");
            VentanaDialogoComponent.crearVentanaError(getCMZException().getMessageI18N(), getStage());
            
        }
        
    }
    
    @Override
	protected void lineaSeleccionadaChanged() {
		LineaResultadoBusqGui lineaSeleccionada = tbArticulos.getSelectionModel().getSelectedItem();
		limpiarPanelPromociones();
		if (lineaSeleccionada != null) {
			tfDetalleCodArticulo.setText(lineaSeleccionada.getCodArticulo());
			tfDetalleDescripcion.setText(lineaSeleccionada.getDescripcion());
			try {
				ticketManager.inicializarTicket();
				ticketManager.getTicket().setCliente(clienteBusqueda);
				LineaTicket linea = ticketManager.nuevaLineaArticulo(lineaSeleccionada.getCodArticulo(), lineaSeleccionada.getDesglose1(), lineaSeleccionada.getDesglose2(), BigDecimal.ONE, null);
				tfDetallePrecio.setText(FormatUtil.getInstance().formateaNumero(linea.getPrecioConDto(), 2));
				tfPrecioIva.setText(FormatUtil.getInstance().formateaImporte(linea.getPrecioTotalConDto()));
			}
			catch (Exception e) {
				log.error("lineaSeleccionadaChanged() - No se ha podido encontrar la tarifa para este artículo: " + e.getMessage());
				tfDetallePrecio.setText("-");
				tfPrecioIva.setText("-");
			}
			tfDetalleDesglose1.setText(lineaSeleccionada.getDesglose1());
			tfDetalleDesglose2.setText(lineaSeleccionada.getDesglose2());

			evaluarPromocion(lineaSeleccionada.getCodArticulo(), lineaSeleccionada.getDesglose1(), lineaSeleccionada.getDesglose2());
			cargarBotonesStock(lineaSeleccionada);
		}
		else {
			ocultarBotonesStock();
		}
	}
	
}
