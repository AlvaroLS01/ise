package com.comerzzia.bimbaylola.pos.gui.ventas.tickets.articulos.cambiarVendedor;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.comerzzia.pos.core.gui.InitializeGuiException;
import com.comerzzia.pos.core.gui.controllers.WindowController;
import com.comerzzia.pos.gui.ventas.tickets.TicketManager;
import com.comerzzia.pos.gui.ventas.tickets.articulos.FacturacionArticulosController;
import com.comerzzia.pos.persistence.core.usuarios.UsuarioBean;
import com.comerzzia.pos.services.core.usuarios.UsuarioNotFoundException;
import com.comerzzia.pos.services.core.usuarios.UsuariosService;
import com.comerzzia.pos.services.core.usuarios.UsuariosServiceException;
import com.comerzzia.pos.services.ticket.lineas.LineaTicket;
import com.comerzzia.pos.util.i18n.I18N;

@Controller
public class ByLCambiarVendedorController extends WindowController implements Initializable {
	
	private static final Logger log = Logger.getLogger(ByLCambiarVendedorController.class.getName());
	
	public static final String CLAVE_PARAMETRO_ARTICULO = "Articulo";
	
	protected TicketManager ticketManager;
	
	@Autowired
    protected UsuariosService usuariosService;
	
	protected LineaTicket lineaOriginal;
	
	//Elementos de pantalla
	@FXML
	protected TextField tfVendedor;
	
	@FXML
    protected Button btAceptar;
	
	@FXML
	protected Label lbError;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
	}

	@Override
	public void initializeComponents() throws InitializeGuiException {
		
	}

	@Override
	public void initializeForm() throws InitializeGuiException {
		ticketManager = (TicketManager) getDatos().get(FacturacionArticulosController.TICKET_KEY);
		LineaTicket lineaSeleccionada = null;

        //Recuperamos la línea que nos llega como parámetro
    	lineaSeleccionada = (LineaTicket) this.getDatos().get(CLAVE_PARAMETRO_ARTICULO);
    	
    	this.lineaOriginal = lineaSeleccionada;
		
		tfVendedor.setText(lineaOriginal.getVendedor().getUsuario());
	}

	@Override
	public void initializeFocus() {
		lbError.setText("");
		tfVendedor.requestFocus();
	}
	
	@FXML
    public void accionAceptar() {
		log.debug("accionAceptar()");
		
		String textoUsuario = tfVendedor.getText();
		if(textoUsuario != null && !textoUsuario.isEmpty()) {
			try {
				UsuarioBean usuario = usuariosService.consultarUsuario(textoUsuario);
				lineaOriginal.setVendedor(usuario);
				getStage().close();
			} catch (UsuarioNotFoundException e) {
				lbError.setText(I18N.getTexto("No existe el vendedor introducido"));
			} catch (UsuariosServiceException e) {
				lbError.setText(I18N.getTexto("Ha ocurrido un error al consultar el vendedor"));
			}
		}else{
			lbError.setText(I18N.getTexto("Debe introducir un código de vendedor válido"));
		}
	}
	
	@FXML
    public void accionAceptarIntro(KeyEvent e){
		log.trace("accionAceptarIntro()");

	    if (e.getCode() == KeyCode.ENTER) {
	    	accionAceptar();
	    }
	}

}
