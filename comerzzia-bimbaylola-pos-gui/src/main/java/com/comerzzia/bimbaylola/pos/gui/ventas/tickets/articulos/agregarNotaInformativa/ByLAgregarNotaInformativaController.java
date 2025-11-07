package com.comerzzia.bimbaylola.pos.gui.ventas.tickets.articulos.agregarNotaInformativa;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.comerzzia.bimbaylola.pos.gui.ventas.tickets.articulos.agregarNotaInformativa.tablaNotas.ByLTablaNotasController;
import com.comerzzia.bimbaylola.pos.gui.ventas.tickets.articulos.agregarNotaInformativa.tablaNotas.ByLTablaNotasView;
import com.comerzzia.bimbaylola.pos.persistence.ticket.articulos.agregarnotainformativa.AvisoInformativoBean;
import com.comerzzia.bimbaylola.pos.persistence.ticket.articulos.agregarnotainformativa.NotaInformativaBean;
import com.comerzzia.bimbaylola.pos.services.articulos.articulosNoAptos.ArticuloNoAptoException;
import com.comerzzia.bimbaylola.pos.services.ticket.ByLLineaTicket;
import com.comerzzia.bimbaylola.pos.services.ticket.articulos.agregarnotainformativa.ByLAgregarNotaInformativaService;
import com.comerzzia.bimbaylola.pos.services.ticket.profesional.ByLLineaTicketProfesional;
import com.comerzzia.pos.core.gui.InitializeGuiException;
import com.comerzzia.pos.core.gui.componentes.datepicker.DatePicker;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.core.gui.controllers.WindowController;
import com.comerzzia.pos.gui.ventas.tickets.TicketManager;
import com.comerzzia.pos.services.core.sesion.Sesion;
import com.comerzzia.pos.util.format.FormatUtil;
import com.comerzzia.pos.util.i18n.I18N;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

@Controller
public class ByLAgregarNotaInformativaController extends WindowController implements Initializable{
	
	//ByLCambiarVendedorController
	private static final Logger log = Logger.getLogger(ByLAgregarNotaInformativaController.class.getName());
	public static final String CLAVE_PARAMETRO_LINEA_TICKET ="CLAVE PARAMETRO LINEA TICKET";
    public static final String PARAMETRO_AVISO_INFORMATIVO = "PARAMETRO AVISO INFORMATIVO";
    public static final String AVISO_INFORMATIVO_BEAN = "AVISO INFORMATIVO BEAN";


    @FXML
    protected Label lbFecha, lbTexto;
	
	@FXML
	protected TextField tfCodNota, tfDescNota, tfTexto;
	
	@FXML
	protected DatePicker dpFecha;
	
    @FXML
    Label lbError;
    
    @Autowired
    private Sesion sesion;
    
    @Autowired
	protected TicketManager ticketManager;
	
	public AvisoInformativoBean aviso;
	public NotaInformativaBean nota;
	
	private ByLLineaTicket lineaTicket;
	private ByLLineaTicketProfesional lineaTicketProfesional;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {		
	}

	@Override
	public void initializeComponents() throws InitializeGuiException {	
		registrarAccionCerrarVentanaEscape();
		tfCodNota.focusedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) {
                if(oldValue){
                    procesarTipoNota();
                }
            }});
	}
	
	@Override
	public void initializeForm() throws InitializeGuiException {
		if (getDatos().get(CLAVE_PARAMETRO_LINEA_TICKET) instanceof ByLLineaTicketProfesional) {
			lineaTicketProfesional = (ByLLineaTicketProfesional) getDatos().get(CLAVE_PARAMETRO_LINEA_TICKET);
			nota = new NotaInformativaBean();
			aviso = null;
			if (lineaTicketProfesional.getNotaInformativa() != null) {
				nota = lineaTicketProfesional.getNotaInformativa();
				try {
					aviso = ByLAgregarNotaInformativaService.get().consultarAvisoInformativo(sesion.getAplicacion().getUidActividad(), sesion.getAplicacion().getTienda().getCliente().getCodpais(),
					        nota.getCodigo());
				}
				catch (ArticuloNoAptoException e) {
					log.error("Error obteniendo el tipo de aviso con código: " + nota.getCodigo(), e);
					VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("No existe ningún tipo de aviso con el código indicado."), getStage());
					tfCodNota.setText("");
					tfDescNota.setText("");
				}
			}
		}
		else {
			lineaTicket = (ByLLineaTicket) getDatos().get(CLAVE_PARAMETRO_LINEA_TICKET);
			nota = new NotaInformativaBean();
			aviso = null;
			if (lineaTicket.getNotaInformativa() != null) {
				nota = lineaTicket.getNotaInformativa();
				try {
					aviso = ByLAgregarNotaInformativaService.get().consultarAvisoInformativo(sesion.getAplicacion().getUidActividad(), sesion.getAplicacion().getTienda().getCliente().getCodpais(),
					        nota.getCodigo());
				}
				catch (ArticuloNoAptoException e) {
					log.error("Error obteniendo el tipo de aviso con código: " + nota.getCodigo(), e);
					VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("No existe ningún tipo de aviso con el código indicado."), getStage());
					tfCodNota.setText("");
					tfDescNota.setText("");
				}
			}
		}
    	
		refrescarDatosPantalla();
	}
	

	@Override
	public void initializeFocus() {
		tfCodNota.requestFocus();
		lbError.setText("");
	}
	

    public void refrescarDatosPantalla() {
    	if(aviso != null) {
	    	if(!aviso.getFecha().equals("S")){   			
	        	lbFecha.setVisible(false);
	        	dpFecha.setVisible(false);
	        }else{
	        	lbFecha.setVisible(true);
	        	dpFecha.setVisible(true);
//	        	try {
	        	dpFecha.setValue(nota.getFecha() == null ? null : FormatUtil.getInstance().desformateaFecha(nota.getFecha()));
//				} catch (ParseException e) {
//					dpFecha.setValue(null);
//				}
	        }
	        if(!aviso.getTexto().equals("S")){
	        	lbTexto.setVisible(false);
	        	tfTexto.setVisible(false);
	        }else{
	        	lbTexto.setVisible(true);
	        	tfTexto.setVisible(true);
	        	tfTexto.setText(nota.getTexto());
	        }
	    	tfCodNota.setText(nota.getCodigo());
	    	tfDescNota.setText(nota.getDescripcion());
	//    	dpFecha.setValue(aviso.getFecha());
    	} else {
    		lbTexto.setVisible(false);
        	tfTexto.setVisible(false);
        	lbFecha.setVisible(false);
        	dpFecha.setVisible(false);
        	tfCodNota.setText("");
	    	tfDescNota.setText("");
	    	dpFecha.setValue(null);
	    	tfTexto.setText("");
    	}
    }
    
    public void procesarTipoNota() {
    	String codNota = tfCodNota.getText();
    	
    	if(codNota != null && !codNota.trim().isEmpty()) {
    		try {
				aviso = ByLAgregarNotaInformativaService.get().consultarAvisoInformativo(sesion.getAplicacion().getUidActividad(), sesion.getAplicacion().getTienda().getCliente().getCodpais(), codNota);
				nota.setCodigo(aviso.getCodigo());
				nota.setDescripcion(aviso.getDescripcion());
				refrescarDatosPantalla();
			} catch (ArticuloNoAptoException e) {
				log.error("Error obteniendo el tipo de aviso con código: " + codNota, e);
                VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("No existe ningún tipo de aviso con el código indicado."), getStage());
                tfCodNota.setText("");
                tfDescNota.setText("");
			}
    	} else {
    		tfCodNota.setText("");
    		tfDescNota.setText("");
    	}
    }

	@FXML
    public void accionAceptar() {
	  try {
			log.debug("accionAceptarIntro()");
        	if (!validarCampos()){
        		return;
        	}
            nota.setCodigo(tfCodNota.getText());
            nota.setDescripcion(tfDescNota.getText());
            nota.setFecha(dpFecha.getValue());
            nota.setTexto(tfTexto.getText());     
            if(lineaTicket != null) {
            	lineaTicket.setNotaInformativa(nota);            	
            }
            else {
            	lineaTicketProfesional.setNotaInformativa(nota);
            }
            getStage().close();
        }
        catch (Exception e) {
            log.error("accionAceptar() - Error inesperado - " + e.getCause(), e);
            VentanaDialogoComponent.crearVentanaError(getStage(), e);
        }
		
	}
	
	
	protected boolean validarCampos(){
		String codigo = tfCodNota.getText() == null ? "" : tfCodNota.getText().trim();
        String fecha = dpFecha.getValue() == null ? "" : dpFecha.getValue().trim();
        String texto = tfTexto.getText() == null ? "" : tfTexto.getText().trim();
        
		lbError.setText("");
		
		if(codigo.isEmpty()){
			lbError.setText(I18N.getTexto("Debe seleccionar un código"));
			return false;
		}else if(aviso.getFecha().equals("S") && fecha.isEmpty()){
			lbError.setText(I18N.getTexto("Debe indicar una fecha"));
			return false;
		}else if(aviso.getTexto().equals("S") && texto.isEmpty()){
			lbError.setText(I18N.getTexto("Debe escribir un texto"));
			return false;
		}
		
		try{
			if(aviso.getFecha().equals("S") && !fecha.isEmpty()){
				FormatUtil.getInstance().desformateaFecha(fecha);
			}
		} catch(Exception e) {
			lbError.setText(I18N.getTexto("El formato de la fecha no es válido."));
			return false;
		}
		
		return true;
	}

	
    public void accionBuscarNotas(){ 
    	getApplication().getMainView().showModalCentered(ByLTablaNotasView.class, getDatos(), this.getStage());
      	
    	if(getDatos().containsKey(ByLTablaNotasController.PARAMETRO_AVISO_INFORMATIVO)){
    		aviso = (AvisoInformativoBean) getDatos().get(ByLTablaNotasController.PARAMETRO_AVISO_INFORMATIVO);
    		if(!aviso.getFecha().equals("S")){
	        	lbFecha.setVisible(false);
	        	dpFecha.setVisible(false);
	        }else{
	        	lbFecha.setVisible(true);
	        	dpFecha.setVisible(true);
	        }
	        if(!aviso.getTexto().equals("S")){
	        	lbTexto.setVisible(false);
	        	tfTexto.setVisible(false);
	        }else{
	        	tfTexto.setText("");
	        	lbTexto.setVisible(true);
	        	tfTexto.setVisible(true);
	        }
    		tfCodNota.setText(aviso.getCodigo());
    		tfDescNota.setText(aviso.getDescripcion());    		
    	}
    }
    
    
    public void registrarAccionCerrarVentanaEscape() {
        registraEventoTeclado(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.ESCAPE) {
                    accionCancelar();
                    t.consume();
                }
            }
        }, KeyEvent.KEY_RELEASED);
    }
    
    @FXML
    public void accionCancelar(){
    	getStage().close();
    }
}
