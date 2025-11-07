package com.comerzzia.bimbaylola.pos.gui.mantenimientos.clientes;

import javafx.fxml.FXML;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.comerzzia.bimbaylola.pos.services.clientes.ByLClientesService;
import com.comerzzia.bimbaylola.pos.services.clientes.ClienteValidatedException;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.gui.mantenimientos.clientes.MantenimientoClienteController;
import com.comerzzia.pos.services.core.sesion.Sesion;
import com.comerzzia.pos.util.config.SpringContext;
import com.comerzzia.pos.util.i18n.I18N;

@Component
@Primary
public class ByLMantenimientoClienteController extends MantenimientoClienteController{

	@Autowired
	private Sesion sesion = SpringContext.getBean(Sesion.class);
	@Autowired
	private ByLClientesService clienteService;
	
	Boolean documentoValidado = false;
	
	/**
	 * Acción que se realiza al pulsar sobre el botón de validar 
	 * el documento del formulario. Realiza varias comprobaciones 
	 * llamando al método de validar documento.
	 */
	@FXML
	public void accionValidarDocumento(){
		
		try {
			if(cbTipoDocIdent.getSelectionModel().getSelectedItem() != null){
				if(clienteService.validarDocumento(cbTipoDocIdent.getSelectionModel()
						.getSelectedItem().getClaseValidacion(), cbTipoDocIdent.getSelectionModel()
						.getSelectedItem().getCodTipoIden(), tfCodPais.getText(), tfNumDocIdent.getText())){
					VentanaDialogoComponent.crearVentanaInfo("Documento válido", getStage());
				}
			}else{
				/* Sino tenemos nada seleccionado, nos envia un mensaje de aviso, 
				 * es necesario el tipo para poder comprobar. */
				VentanaDialogoComponent.crearVentanaAviso(
						I18N.getTexto("Debe seleccionar Tipo documento"), getStage());
			}
		}catch(ClienteValidatedException e){
			VentanaDialogoComponent.crearVentanaAviso(e.getMessage()
					, getStage());
		}
		
	}
	
	/**
	 * Acción de aceptar el formulario, realizamos comprobaciones 
	 * previas para comprobar que el documento está correctamente.
	 */
	@FXML
	public void accionAceptar(){
			
		try {
			if(cbTipoDocIdent.getSelectionModel().getSelectedItem() != null){
				if(clienteService.validarDocumento(cbTipoDocIdent.getSelectionModel()
						.getSelectedItem().getClaseValidacion(), cbTipoDocIdent.getSelectionModel()
						.getSelectedItem().getCodTipoIden(), tfCodPais.getText(), tfNumDocIdent.getText())){
					super.accionAceptar();
				}
			}else{
				/* Sino tenemos nada seleccionado, nos envia un mensaje de aviso, 
				 * es necesario el tipo para poder comprobar. */
				VentanaDialogoComponent.crearVentanaAviso(
						I18N.getTexto("Debe seleccionar Tipo documento"), getStage());
			}
		}catch(ClienteValidatedException e){
			VentanaDialogoComponent.crearVentanaAviso(e.getMessage()
					, getStage());
		}
		
	}
	
	
	
	
}
