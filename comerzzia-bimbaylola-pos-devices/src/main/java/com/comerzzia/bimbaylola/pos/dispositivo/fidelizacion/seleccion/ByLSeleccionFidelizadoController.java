package com.comerzzia.bimbaylola.pos.dispositivo.fidelizacion.seleccion;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Controller;

import com.comerzzia.model.fidelizacion.fidelizados.FidelizadoBean;
import com.comerzzia.pos.core.gui.InitializeGuiException;
import com.comerzzia.pos.core.gui.tablas.celdas.CellFactoryBuilder;
import com.comerzzia.pos.dispositivo.fidelizacion.busqueda.BusquedaFidelizadoController;
import com.comerzzia.pos.dispositivo.fidelizacion.seleccion.SeleccionFidelizadoController;
import com.comerzzia.pos.util.i18n.I18N;

@Controller
@Primary
public class ByLSeleccionFidelizadoController extends SeleccionFidelizadoController{
	
    protected Logger log = Logger.getLogger(ByLSeleccionFidelizadoController.class);
    
	@FXML
    protected TableColumn<ByLSeleccionFidelizadoGui, String> tcEmail, tcTelefono;
	private ObservableList<ByLSeleccionFidelizadoGui> fidelizados;
    
    @SuppressWarnings("unchecked")
	@Override
    public void initialize(URL url, ResourceBundle rb){

    	tbFidelizados.setPlaceholder(new Label(""));
        
    	fidelizados = FXCollections.observableList(new ArrayList<ByLSeleccionFidelizadoGui>());
        
        tcCodFid.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tcFidelizados", "tcCodFid", null, CellFactoryBuilder.ESTILO_ALINEACION_IZQ));
        tcNombre.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tcFidelizados", "tcNombre", null, CellFactoryBuilder.ESTILO_ALINEACION_IZQ));
        tcApellidos.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tcFidelizados", "tcApellidos", null, CellFactoryBuilder.ESTILO_ALINEACION_IZQ));
        tcEmail.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tcFidelizados", "tcEmail", null, CellFactoryBuilder.ESTILO_ALINEACION_IZQ));
        tcTelefono.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tcFidelizados", "tcTelefono", null, CellFactoryBuilder.ESTILO_ALINEACION_IZQ));
        tcDireccion.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tcFidelizados", "tcDireccion", null, CellFactoryBuilder.ESTILO_ALINEACION_IZQ));
    
        tcCodFid.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ByLSeleccionFidelizadoGui, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ByLSeleccionFidelizadoGui, String> cdf){
                return cdf.getValue().getCodFid();
            }
        });
        tcNombre.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ByLSeleccionFidelizadoGui, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ByLSeleccionFidelizadoGui, String> cdf){
                return cdf.getValue().getNombre();
            }
        });
        tcApellidos.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ByLSeleccionFidelizadoGui, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ByLSeleccionFidelizadoGui, String> cdf){
                return cdf.getValue().getApellidos();
            }
        });
        tcEmail.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ByLSeleccionFidelizadoGui, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ByLSeleccionFidelizadoGui, String> cdf){
                return cdf.getValue().getEmail();
            }
        });
        tcTelefono.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ByLSeleccionFidelizadoGui, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ByLSeleccionFidelizadoGui, String> cdf){
            	return cdf.getValue().getTelefono();
            }
        });
        tcDireccion.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ByLSeleccionFidelizadoGui, String>, ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ByLSeleccionFidelizadoGui, String> cdf){
                return cdf.getValue().getDireccion();
            }
        });
    }

    @SuppressWarnings("unchecked")
	@Override
    public void initializeForm() throws InitializeGuiException{
    	try{
	    	HashMap<String,Object> parametros = getDatos();
	    	setTitulo();
	    	
	        fidelizados.clear();
	        
	        List<FidelizadoBean> fidelizadosEntrada = (List<FidelizadoBean>)
	        		parametros.get(BusquedaFidelizadoController.PARAMETRO_FIDELIZADOS_SELECCION);
	        
	        for(FidelizadoBean fid: fidelizadosEntrada){
        		fidelizados.add(new ByLSeleccionFidelizadoGui(fid));
	        }
	        tbFidelizados.setItems(fidelizados);
	        
    	}catch(Exception e){
    		log.error("initializeForm() - Error inicializando la pantalla: " + e.getMessage());
    	}
    }
    
    @Override
    public void setTitulo(){
    	lbTitulo.setText(I18N.getTexto("Selección de Cliente"));
    }
    
}
