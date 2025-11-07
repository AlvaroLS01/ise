package com.comerzzia.bimbaylola.pos.gui.pagos.email;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.comerzzia.bimbaylola.pos.gui.pagos.ByLPagosController;
import com.comerzzia.pos.core.gui.InitializeGuiException;
import com.comerzzia.pos.core.gui.controllers.WindowController;
import com.comerzzia.pos.services.core.sesion.Sesion;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

@Controller
public class ByLEmailController extends WindowController implements Initializable {

	protected Logger log = Logger.getLogger(getClass());

	@FXML
	protected Button btImprimir, btCorreo, btAmbos;

	@FXML
	protected Label lbSolicitaCliente;

	private final String ACCION_EMAIL = "ACCION_EMAIL";
	private final String IMPRIMIR = "IMPRIMIR";
	private final String CORREO = "CORREO";
	private final String AMBOS = "AMBOS";

	@Autowired
	protected Sesion sesion;

	@Override
	public void initialize(URL url, ResourceBundle rb) {

	}

	@Override
	public void initializeComponents() throws InitializeGuiException {

	}

	@Override
	public void initializeForm() throws InitializeGuiException {
		if (getDatos().get(ByLPagosController.POS_PERMITE_TICKET_ELECTRONICO) != null) {
			String permiteTicketE = (String) getDatos().get(ByLPagosController.POS_PERMITE_TICKET_ELECTRONICO);
			if (permiteTicketE.equals("1")) {
				btCorreo.setDisable(true);
			}
			else {
				btCorreo.setDisable(false);
			}
		}

	}

	@Override
	public void initializeFocus() {

	}

	@FXML
	public void accionImprimir() {
		this.getDatos().put(ACCION_EMAIL, IMPRIMIR);
		getStage().close();
	}

	@FXML
	public void accionCorreo() {
		this.getDatos().put(ACCION_EMAIL, CORREO);
		getStage().close();
	}

	@FXML
	public void accionAmbos() {
		this.getDatos().put(ACCION_EMAIL, AMBOS);
		getStage().close();
	}

}
