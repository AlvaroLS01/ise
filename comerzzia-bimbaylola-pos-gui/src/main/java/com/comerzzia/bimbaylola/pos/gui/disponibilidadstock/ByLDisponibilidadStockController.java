package com.comerzzia.bimbaylola.pos.gui.disponibilidadstock;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.comerzzia.bimbaylola.pos.persistence.articulos.stock.RespuestaTokenDTO;
import com.comerzzia.bimbaylola.pos.persistence.articulos.stock.SolicitarStockDTO;
import com.comerzzia.bimbaylola.pos.persistence.articulos.stock.SolicitarTokenDTO;
import com.comerzzia.bimbaylola.pos.services.articulos.stock.StockService;
import com.comerzzia.core.omnichannel.engine.model.almacenes.articulos.AlmacenArticuloBean;
import com.comerzzia.core.omnichannel.engine.model.tiendas.TiendaBean;
import com.comerzzia.pos.core.gui.BackgroundTask;
import com.comerzzia.pos.core.gui.POSApplication;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.core.gui.disponibilidadstock.ContactoTiendaController;
import com.comerzzia.pos.core.gui.disponibilidadstock.ContactoTiendaView;
import com.comerzzia.pos.core.gui.disponibilidadstock.DisponibilidadStockController;
import com.comerzzia.pos.core.gui.disponibilidadstock.StockGui;
import com.comerzzia.pos.core.gui.tablas.celdas.CellFactoryBuilder;
import com.comerzzia.pos.persistence.articulos.codBarras.ArticuloCodBarraBean;
import com.comerzzia.pos.services.articulos.ArticuloNotFoundException;
import com.comerzzia.pos.services.articulos.ArticulosService;
import com.comerzzia.pos.services.articulos.ArticulosServiceException;
import com.comerzzia.pos.services.core.sesion.Sesion;
import com.comerzzia.pos.services.core.tiendas.Tienda;
import com.comerzzia.pos.services.core.tiendas.TiendasService;
import com.comerzzia.pos.services.core.variables.VariablesServices;
import com.comerzzia.pos.services.ticket.lineas.ConsultaCodigoArticuloResponse;
import com.comerzzia.pos.services.ticket.lineas.LineasTicketServices;
import com.comerzzia.pos.util.config.AppConfig;
import com.comerzzia.pos.util.config.SpringContext;
import com.comerzzia.pos.util.i18n.I18N;
import com.comerzzia.rest.client.articulos.DisponibilidadArticuloBean;
import com.comerzzia.rest.client.articulos.ListaDisponibilidadArticuloBean;
import com.comerzzia.rest.client.exceptions.RestException;
import com.comerzzia.rest.client.exceptions.RestTimeoutException;

@Component
@Primary
public class ByLDisponibilidadStockController extends DisponibilidadStockController{

	@Autowired
	private Sesion sesion;
	
    @Autowired
    private ArticulosService articulosService;
	
	@Autowired
	private VariablesServices variablesServices;
	
	@Autowired
	private TiendasService tiendasServices;
	
	@Autowired
	private LineasTicketServices lineasTicketServices;
	
	@Autowired
	private StockService stockServices;
	
	@FXML
	protected VBox vBoxInferior;
	
	private static final String GRANT_TYPE = "NAVISION.GRANT_TYPE";
	private static final String CLIENT_ID = "NAVISION.CLIENT_ID";
	private static final String CLIENT_SECRET = "NAVISION.CLIENT_SECRET";
	private static final String SCOPE_STOCKS = "NAVISION.SCOPE_STOCKS";
	
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		log.debug("initialize() - Inicializando pantalla...");

		// Mensaje sin contenido para tabla. los establecemos a vacio
		tbStockTiendaLogada.setPlaceholder(new Label(I18N.getTexto("No hay stock en la tienda")));

		stockTiendaLogada = FXCollections.observableList(new ArrayList<StockGui>());
		
		tcDesglose1.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbStockTiendaLogada", "tcDesglose1", null, CellFactoryBuilder.ESTILO_ALINEACION_CEN));
		tcDesglose2.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbStockTiendaLogada", "tcDesglose2", null, CellFactoryBuilder.ESTILO_ALINEACION_CEN));
		tcStock.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbStockTiendaLogada", "tcStock", 0, CellFactoryBuilder.ESTILO_ALINEACION_DCHA));
		tcStockA.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbStockTiendaLogada", "tcStockA", 0, CellFactoryBuilder.ESTILO_ALINEACION_DCHA));
		tcStockB.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbStockTiendaLogada", "tcStockB", 0, CellFactoryBuilder.ESTILO_ALINEACION_DCHA));
		tcStockC.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbStockTiendaLogada", "tcStockC", 0, CellFactoryBuilder.ESTILO_ALINEACION_DCHA));
		tcStockD.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbStockTiendaLogada", "tcStockD", 0, CellFactoryBuilder.ESTILO_ALINEACION_DCHA));
		tcStockLogistico.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbStockTiendaLogada", "tcStockLogistico", 0, CellFactoryBuilder.ESTILO_ALINEACION_DCHA));
		
		tcDesglose1.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<StockGui, String>, ObservableValue<String>>(){

			@Override
			public ObservableValue<String> call(CellDataFeatures<StockGui, String> cdf) {
				return cdf.getValue().desglose1Property();
			}
		});
		tcDesglose2.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<StockGui, String>, ObservableValue<String>>(){

			@Override
			public ObservableValue<String> call(CellDataFeatures<StockGui, String> cdf) {
				return cdf.getValue().desglose2Property();
			}
		});
		tcStock.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<StockGui, String>, ObservableValue<String>>(){

			@Override
			public ObservableValue<String> call(CellDataFeatures<StockGui, String> cdf) {
				return cdf.getValue().stockProperty();
			}
		});
		tcStockA.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<StockGui, String>, ObservableValue<String>>(){

			@Override
			public ObservableValue<String> call(CellDataFeatures<StockGui, String> cdf) {
				return cdf.getValue().stockAProperty();
			}
		});
		tcStockB.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<StockGui, String>, ObservableValue<String>>(){

			@Override
			public ObservableValue<String> call(CellDataFeatures<StockGui, String> cdf) {
				return cdf.getValue().stockBProperty();
			}
		});
		tcStockC.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<StockGui, String>, ObservableValue<String>>(){

			@Override
			public ObservableValue<String> call(CellDataFeatures<StockGui, String> cdf) {
				return cdf.getValue().stockCProperty();
			}
		});
		tcStockD.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<StockGui, String>, ObservableValue<String>>(){

			@Override
			public ObservableValue<String> call(CellDataFeatures<StockGui, String> cdf) {
				return cdf.getValue().stockDProperty();
			}
		});
		tcStockLogistico.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<StockGui, String>, ObservableValue<String>>(){

			@Override
			public ObservableValue<String> call(CellDataFeatures<StockGui, String> cdf) {
				return cdf.getValue().stockLogisticoProperty();
			}
		});

		// Mensaje sin contenido para tabla. los establecemos a vacio
		tbStockResto.setPlaceholder(new Label(I18N.getTexto("No hay stock en el resto de tiendas")));

		stockResto = FXCollections.observableList(new ArrayList<StockGui>());

		tcCodigoResto.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbStockResto", "tcCodigoResto", null, CellFactoryBuilder.ESTILO_ALINEACION_IZQ));
		tcDescripcionResto.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbStockResto", "tcDescripcionResto", null, CellFactoryBuilder.ESTILO_ALINEACION_IZQ));
		tcProvinciaResto.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbStockResto", "tcProvinciaResto", null, CellFactoryBuilder.ESTILO_ALINEACION_IZQ));
		tcDesglose1Resto.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbStockResto", "tcDesglose1Resto", null, CellFactoryBuilder.ESTILO_ALINEACION_CEN));
		tcDesglose2Resto.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbStockResto", "tcDesglose2Resto", null, CellFactoryBuilder.ESTILO_ALINEACION_CEN));
		tcStockResto.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbStockResto", "tcStockResto", 0, CellFactoryBuilder.ESTILO_ALINEACION_DCHA));
		tcContactoResto.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<StockGui, Button>, ObservableValue<Button>>(){

			@Override
			public ObservableValue<Button> call(CellDataFeatures<StockGui, Button> p) {
				StockGui stockGui = p.getValue();

				final Button btnContacto = new Button();

				btnContacto.setPrefSize(90, 30);
				btnContacto.setMaxSize(90, 30);
				btnContacto.setMinSize(90, 30);
				btnContacto.getStyleClass().add("btInfo");

				btnContacto.setUserData(stockGui);
				btnContacto.setAlignment(Pos.BOTTOM_CENTER);

				btnContacto.setOnAction(new EventHandler<ActionEvent>(){

					@Override
					public void handle(ActionEvent evt) {
						StockGui stockGui = (StockGui) ((Node) evt.getSource()).getUserData();
						TiendaBean tienda = stockGui.getDisponibilidad().getTienda();
						if (tienda != null) {
							HashMap<String, Object> datos = new HashMap<String, Object>();
							datos.put(ContactoTiendaController.STOCK, stockGui);
							POSApplication.getInstance().getMainView().showModalCentered(ContactoTiendaView.class, datos, getStage());
						}
					}
				});

				return new ObservableValueBase<Button>(){

					@Override
					public Button getValue() {
						return btnContacto;
					}
				};
			}
		});

		// Definimos un factory para cada celda para aumentar el rendimiento
		tcCodigoResto.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<StockGui, String>, ObservableValue<String>>(){

			@Override
			public ObservableValue<String> call(CellDataFeatures<StockGui, String> cdf) {
				return cdf.getValue().codAlmProperty();
			}
		});
		tcDescripcionResto.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<StockGui, String>, ObservableValue<String>>(){

			@Override
			public ObservableValue<String> call(CellDataFeatures<StockGui, String> cdf) {
				return cdf.getValue().desAlmProperty();
			}
		});
		tcProvinciaResto.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<StockGui, String>, ObservableValue<String>>(){

			@Override
			public ObservableValue<String> call(CellDataFeatures<StockGui, String> cdf) {
				return cdf.getValue().provinciaProperty();
			}
		});
		tcDesglose1Resto.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<StockGui, String>, ObservableValue<String>>(){

			@Override
			public ObservableValue<String> call(CellDataFeatures<StockGui, String> cdf) {
				return cdf.getValue().desglose1Property();
			}
		});
		tcDesglose2Resto.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<StockGui, String>, ObservableValue<String>>(){

			@Override
			public ObservableValue<String> call(CellDataFeatures<StockGui, String> cdf) {
				return cdf.getValue().desglose2Property();
			}
		});
		tcStockResto.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<StockGui, String>, ObservableValue<String>>(){

			@Override
			public ObservableValue<String> call(CellDataFeatures<StockGui, String> cdf) {
				return cdf.getValue().stockProperty();
			}
		});
	}
	
	@Override
    public void initializeComponents(){  
		super.initializeComponents();
		/* Ocultamos la columna de stock logístico. */
		tcStockLogistico.setVisible(false);
		/* Ocultamos la tabla inferior de la pantalla. */
		vBoxInferior.setVisible(false);
	}
	
	protected void buscarDisponibilidad(){
		
		String codart = tfCodigoIntro.getText();		
 		String desglose1 = cbDesglose1.getSelectionModel().getSelectedItem();
		String desglose2 = cbDesglose2.getSelectionModel().getSelectedItem();
		
		/* Rescatamos el código de barras del articulo. */
		String codBar = cargarCodBarArticulo(codart);
		
		ConsultaCodigoArticuloResponse codigoArticuloResponse = null;
		try{
			codigoArticuloResponse = lineasTicketServices.consultarLineaArticulo(codart, desglose1, desglose2, true);
			if(codigoArticuloResponse != null){
				codart = codigoArticuloResponse.getArticulo().getCodArticulo();
			}
		}catch(Exception e){
			if(AppConfig.modoDesarrollo){
				log.error("buscarDisponibilidad() - Ha habido un error al buscar el código de barras:" + e.getMessage(), e);
			}
		}

		try {
			if(codart != null && !codart.isEmpty()){
				String codalm = sesion.getAplicacion().getCodAlmacen();
				
				if(codart != null && !codart.isEmpty()){
					ConsultarStockTask consultarStockTask = SpringContext.getBean(ConsultarStockTask.class, this, codBar, codalm);
					consultarStockTask.start();
				}
			}else{
				VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("Debe introducir el código de artículo o código de barras"), getStage());
				tfCodigoIntro.requestFocus();
			}
		} catch(Exception e) {
			VentanaDialogoComponent.crearVentanaError(I18N.getTexto("Se ha producido un error al consultar el stock: " + e.getMessage()), getStage());
		}
	}
	
	@Component
	@Scope("prototype")
	public class ConsultarStockTask extends BackgroundTask<ListaDisponibilidadArticuloBean> {

		protected final String codbar;
		protected final String codalm;

		public ConsultarStockTask(String codbar, String codalm) {
			super(true);
			this.codbar = codbar;
			this.codalm = codalm;
		}

		@Override
		protected ListaDisponibilidadArticuloBean call() throws Exception {
			try {
				/* Consultamos el stock para el articulo seleccionado. */
				String uidActividad = sesion.getAplicacion().getUidActividad();
				String codart = tfCodigoIntro.getText();
				String desglose1 = cbDesglose1.getSelectionModel().getSelectedItem();
				String desglose2 = cbDesglose2.getSelectionModel().getSelectedItem();
				Tienda tienda = null;
				try {
					tienda = tiendasServices.consultarTienda(uidActividad, codalm);
				}
				catch (Exception e) {
				}
				ListaDisponibilidadArticuloBean listadoDisponibilidad = new ListaDisponibilidadArticuloBean();
				listadoDisponibilidad.setLstDisponibilidadResto(new ArrayList<DisponibilidadArticuloBean>());
				
				List<DisponibilidadArticuloBean> listaTienda = new ArrayList<DisponibilidadArticuloBean>();
				
				SolicitarStockDTO[] stockSolicitado = solicitarStock(codbar, codalm);
				if(stockSolicitado != null){
					for(int i = 0; i < stockSolicitado.length; i++){
						ArticuloCodBarraBean articulo = null;
						try{
							articulo = articulosService.consultarCodigoBarras(stockSolicitado[i].getEan());
						}catch(ArticuloNotFoundException | ArticulosServiceException e){
							log.error("buscarDisponibilidad() - No se ha encontrado el código de barras"
									+ " del articulo con EAN : " + stockSolicitado[i].getEan(), e);
							continue;
						}
	
						DisponibilidadArticuloBean disponibilidad = new DisponibilidadArticuloBean();
						/* Creamos el objeto que contendra el stock del articulo. */
						AlmacenArticuloBean stockArticulo = new AlmacenArticuloBean();
						stockArticulo.setCodAlmacen(tienda.getCodAlmacen());
						stockArticulo.setDesAlmacen(tienda.getDesAlmacen());
						stockArticulo.setProvincia(tienda.getProvincia());
						stockArticulo.setDesglose1(articulo.getDesglose1());
						stockArticulo.setDesglose2(articulo.getDesglose2());
						stockArticulo.setStock((double)stockSolicitado[i].getTotalStock());
						stockArticulo.setStockA((double) stockSolicitado[i].getShowcaseStock());
						stockArticulo.setStockB((double)stockSolicitado[i].getReservationStock());
						stockArticulo.setStockC((double)stockSolicitado[i].getOnTheWayStock());
						disponibilidad.setStock(stockArticulo);
						/* Introducimos una tienda porque se necesita luego el código postal. */
						TiendaBean tiendaStock = new TiendaBean();
						tiendaStock.setCp(tienda.getCp());
						disponibilidad.setTienda(tiendaStock);
						
						/* Stock por color */
						if(!desglose1.isEmpty() && desglose2.isEmpty()){
							if(desglose1.equals(stockArticulo.getDesglose1())){
								listaTienda.add(disponibilidad);
							}
						/* Stock por talla */
						}else if(!desglose2.isEmpty() && desglose1.isEmpty()){
							if(desglose2.equals(stockArticulo.getDesglose2())){
								listaTienda.add(disponibilidad);
							}
						/* Stock por articulo */
						}else if(codart.equals(articulo.getCodArticulo()) && desglose1.isEmpty() && desglose2.isEmpty()){
							listaTienda.add(disponibilidad);
						/* Stock por modelo */
						}else{
							if(desglose1.equals(articulo.getDesglose1()) && desglose2.equals(articulo.getDesglose2())){
								listaTienda.add(disponibilidad);
							}
						}
						
					}
					
					if(!listaTienda.isEmpty()){
						listadoDisponibilidad.setLstDisponibilidadTienda(listaTienda);
					}
				}else{
					failed();
				}
					
				return listadoDisponibilidad;
			}catch(RestTimeoutException e) {
				VentanaDialogoComponent.crearVentanaError(I18N.getTexto("No se ha podido establecer conexión con el servidor."), getStage());
				throw new Exception();
			}catch(Exception e) {
				VentanaDialogoComponent.crearVentanaError(I18N.getTexto("Se ha producido un error al consultar el stock: " + getMessage()), getStage());
				throw new Exception();
			}
		}

		@Override
		protected void succeeded() {
			try {
				buscarDisponibilidadSucceeded(getValue());
				super.succeeded();
			}
			catch (java.lang.Exception e) {
				log.error("succeeded() - " + e.getClass().getName() + " - " + e.getLocalizedMessage(), e);
			}
		}

		@Override
		protected void failed() {
			try {
				super.failed();
			}
			catch (java.lang.Exception e) {
				log.error("failed() - " + e.getClass().getName() + " - " + e.getLocalizedMessage(), e);
			}
		}

		@Override
		protected void done() {
			super.done();
		}

	}
    
    protected void buscarDisponibilidadSucceeded(ListaDisponibilidadArticuloBean listaDisponibilidad){
    	try{
	    	List<StockGui> stockTienda = new ArrayList<StockGui>();
	    	if(listaDisponibilidad != null){
	    		byte[] foto = null;
	    		if(!listaDisponibilidad.getLstDisponibilidadTienda().isEmpty()){
	    			DisponibilidadArticuloBean disponibilidadArticulo = listaDisponibilidad.getLstDisponibilidadTienda().get(0);
	    			if(disponibilidadArticulo != null){
	    				if(disponibilidadArticulo.getStock().getFoto() != null){
	    					foto = disponibilidadArticulo.getStock().getFoto();
	    				}
	    			}
	    			
	    		}
	    		if(!listaDisponibilidad.getLstDisponibilidadResto().isEmpty()){
	    			DisponibilidadArticuloBean disponibilidadArticulo = listaDisponibilidad.getLstDisponibilidadResto().get(0);
	    			if(disponibilidadArticulo != null){
	    				if(foto != null && disponibilidadArticulo.getStock().getFoto() != null){
	    					foto = disponibilidadArticulo.getStock().getFoto();
	    				}
	    			}
	    			
	    		}
	    		if(foto != null){
	    			ByteArrayInputStream bis = new ByteArrayInputStream(foto);
	        		Image image = new Image(bis, 90.0, 90.0, true, true);
	        		imgArticulo.setImage(image);
	    		}else{
	    			imgArticulo.setImage(null);
	    		}
	    		
	    		for(DisponibilidadArticuloBean disponibilidad : listaDisponibilidad.getLstDisponibilidadTienda()){
	    			/* Añadimos esta parte para mostrar un 0 en la parte de stock logístico. */
	    			StockGui stock = new ByLStockGui(disponibilidad);
	    			stock.setStockLogistico(0.0);
	    			stockTienda.add(stock);
	    		}
	    		
	    		stockTiendaLogada = FXCollections.observableArrayList(stockTienda);
	            tbStockTiendaLogada.setItems(stockTiendaLogada);
	            
	            List<StockGui> stockResto = new ArrayList<StockGui>();
	    		for(DisponibilidadArticuloBean disponibilidad : listaDisponibilidad.getLstDisponibilidadResto()){
	    			stockResto.add(new StockGui(disponibilidad));
	    		}
	    		
	    		this.stockResto = FXCollections.observableArrayList(stockResto);
	            tbStockResto.setItems(this.stockResto);
	            
	    	}else{
	    		log.error("buscarDisponibilidadSucceeded() - La respuesta no se ha podido parsear correctamente.");
	    	}
    	}catch(Exception e){
    		log.error("buscarDisponibilidadSucceeded() - Ha habido un error al pintar en la pantalla: " + e.getMessage(), e);
    	}
    }
    
    /**
     * Carga el código de barras de un articulo a partir del código del articulo.
     * @param codArticulo : Código del articulo ha buscar.
     * @return articuloCodBar.getCodigoBarras() : Código de barras del articulo buscado.
     */
    private String cargarCodBarArticulo(String codArticulo){
		ArticuloCodBarraBean articuloCodBar = null;
		try{
			if(codArticulo != null && !codArticulo.isEmpty()){
				articuloCodBar = articulosService.consultarCodigosBarrasArticulo(codArticulo).get(0);
			}
		}catch(ArticuloNotFoundException | ArticulosServiceException e){
			log.error("cargarCodBarArticulo() - No se han encontrado datos del articulo : " + codArticulo, e);
		}
		return articuloCodBar.getCodigoBarras();
    }
    
    /**
     * Consulta el stock de un articulo a través de su EAN.
     * @param ean : Contiene el EAN del articulo.
     * @return stockSolicitado : Array que contiene los stocks para el articulo indicado.
     * @throws RestException 
     */
    private SolicitarStockDTO[] solicitarStock(String ean, String codTienda) throws RestException{
    	RespuestaTokenDTO respuestaToken = null;
    	SolicitarStockDTO[] stockSolicitado = null;
    	try{
    		variablesServices = SpringContext.getBean(VariablesServices.class);
    		String grantType = variablesServices.getVariableAsString(GRANT_TYPE);
    		String clientID = variablesServices.getVariableAsString(CLIENT_ID);
    		String clientSecret = variablesServices.getVariableAsString(CLIENT_SECRET);
    		String scopeStocks = variablesServices.getVariableAsString(SCOPE_STOCKS);
    		/* Primero solicitamos el token de acceso. */
    		respuestaToken = stockServices.solicitarTokenArticulo(new SolicitarTokenDTO(grantType, clientID, clientSecret, scopeStocks, null));
    		/* Con el token de acceso y el ean ya podemos consultar el stock del articulo. */
    		stockSolicitado = stockServices.solicitarStockArticulo(ean, codTienda, respuestaToken.getAccess_token());
		}catch(RestTimeoutException e) {
			log.error("solicitarStock()- Error al solicitar el stock del articulo con EAN : " + ean + " - " + e.getMessage(), e);
			throw new RestTimeoutException(I18N.getTexto("No se ha podido conectar con el servidor."), e);
		}catch(RestException e){
			log.error("solicitarStock()- Error al solicitar el stock del articulo con EAN : " + ean, e);
			throw new RestException(e);
		}
    	return stockSolicitado;
    }
    
}
