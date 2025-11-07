package com.comerzzia.bimbaylola.pos.gui.ventas.apartados.datosCliente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.comerzzia.bimbaylola.pos.services.clientes.ByLClientesService;
import com.comerzzia.bimbaylola.pos.services.clientes.ClienteValidatedException;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.gui.ventas.apartados.detalle.datosCliente.CambiarDatosClienteApartadoController;
import com.comerzzia.pos.util.i18n.I18N;

@Component
@Primary
public class ByLCambiarDatosClienteApartadoController extends CambiarDatosClienteApartadoController{

	@Autowired
	private ByLClientesService clienteService;
	
	public void accionAceptar(){

		try{
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
