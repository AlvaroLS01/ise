package com.comerzzia.bimbaylola.pos.gui.ventas.tickets.articulos.agregarNotaInformativa.tablaNotas;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Controller;

import com.comerzzia.bimbaylola.pos.persistence.ticket.articulos.agregarnotainformativa.AvisoInformativoBean;
import com.comerzzia.bimbaylola.pos.services.ticket.articulos.agregarnotainformativa.ByLAgregarNotaInformativaService;
import com.comerzzia.pos.core.gui.InitializeGuiException;
import com.comerzzia.pos.core.gui.componentes.botonaccion.BotonBotoneraComponent;
import com.comerzzia.pos.core.gui.componentes.botonera.IContenedorBotonera;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.core.gui.controllers.WindowController;
import com.comerzzia.pos.core.gui.tablas.celdas.CellFactoryBuilder;
import com.comerzzia.pos.services.cajas.CajasServiceException;
import com.comerzzia.pos.util.i18n.I18N;

@Primary
@Controller
public class ByLTablaNotasController extends WindowController implements Initializable, IContenedorBotonera {
	
    
    protected Logger log = Logger.getLogger(getClass());
    
    @FXML
    protected Label lbTitulo;
    
    ObservableList<ByLTablaNotasGui> docs;

    @SuppressWarnings("rawtypes")
	@FXML
    protected TableView tbTipoDoc;
    
    @SuppressWarnings("rawtypes")
	@FXML
    protected TableColumn tcCodDoc, tcDescripcion;
    
    @FXML
    protected Button btAceptar, btCancelar;
    
    public static final String PARAMETRO_AVISO_INFORMATIVO = "PARAMETRO AVISO INFORMATIVO";
    
    @SuppressWarnings("unchecked")
	@Override
    public void initialize(URL url, ResourceBundle rb) {


        tbTipoDoc.setPlaceholder(new Label(""));
        
        docs = FXCollections.observableList(new ArrayList<ByLTablaNotasGui>());
        
        tcCodDoc.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbTipoDoc", "tcCodDoc", null, CellFactoryBuilder.ESTILO_ALINEACION_IZQ));
        tcDescripcion.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbTipoDoc", "tcDesDoc", null, CellFactoryBuilder.ESTILO_ALINEACION_IZQ));
    
        tcCodDoc.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ByLTablaNotasGui, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ByLTablaNotasGui, String> cdf) {
                return cdf.getValue().getCodDoc();
            }
        });
        tcDescripcion.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ByLTablaNotasGui, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ByLTablaNotasGui, String> cdf) {
                return cdf.getValue().getDesDoc();
            }
        });
    }
	
	public void setTitulo(){
		  lbTitulo.setText(I18N.getTexto("Tipo de Aviso"));
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public void initializeForm() throws InitializeGuiException {
		
		try {
	    	setTitulo();
	    	docs.clear();
	    	
			List<AvisoInformativoBean> avisos = ByLAgregarNotaInformativaService.get().consultarAvisosInformativos();
			for(AvisoInformativoBean aviso: avisos){
				try {
	        		docs.add(new ByLTablaNotasGui(aviso));
	        	}
	        	catch (Exception ex) {
	        		log.error("Error procesando los documentos de origen válidos para nota de crédito.", ex);
	        	}
			}			
	        tbTipoDoc.setItems(docs);
		} catch (Exception e) {
    		log.error("initializeForm() - Error inicializando la pantalla: " + e.getMessage());

		}
	}
	
    @FXML
    public void accionAceptar(){
        if(tbTipoDoc.getSelectionModel().getSelectedItem()!=null){
        	
            getDatos().put(PARAMETRO_AVISO_INFORMATIVO, ((ByLTablaNotasGui)tbTipoDoc.getSelectionModel().getSelectedItem()).getConcepto());
            getStage().close();
        }
        else{
            VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("Debe seleccionar el tipo de documento correspondiente."), this.getStage());
        }
    }

	@Override
	public void realizarAccion(BotonBotoneraComponent arg0)	throws CajasServiceException{}

	@Override
	public void initializeComponents() throws InitializeGuiException{}
	
    public void aceptarDocDobleClick(MouseEvent event) {
        log.trace("aceptarArticuloDobleClick() - Acción aceptar");
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            if (event.getClickCount() == 2) {
                accionAceptar();
            }
        }
    }

	@Override
	public void initializeFocus() {
		tbTipoDoc.requestFocus();
    	tbTipoDoc.scrollTo(0);
		if(tbTipoDoc.getSelectionModel().getSelectedIndex()<0){
			tbTipoDoc.getSelectionModel().select(0);
		}
	}	

}
