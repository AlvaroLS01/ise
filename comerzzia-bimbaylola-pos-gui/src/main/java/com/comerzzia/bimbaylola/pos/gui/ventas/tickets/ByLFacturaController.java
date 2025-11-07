package com.comerzzia.bimbaylola.pos.gui.ventas.tickets;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.comerzzia.bimbaylola.pos.services.clientes.ByLClientesService;
import com.comerzzia.bimbaylola.pos.services.clientes.ClienteValidatedException;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.gui.ventas.tickets.TicketManager;
import com.comerzzia.pos.gui.ventas.tickets.articulos.FacturacionArticulosController;
import com.comerzzia.pos.gui.ventas.tickets.factura.FacturaController;
import com.comerzzia.pos.persistence.clientes.ClienteBean;
import com.comerzzia.pos.persistence.fidelizacion.FidelizacionBean;
import com.comerzzia.pos.persistence.paises.PaisBean;
import com.comerzzia.pos.persistence.tickets.datosfactura.DatosFactura;
import com.comerzzia.pos.services.core.documentos.DocumentoException;
import com.comerzzia.pos.services.core.documentos.Documentos;
import com.comerzzia.pos.services.core.sesion.Sesion;
import com.comerzzia.pos.services.ticket.TicketVenta;
import com.comerzzia.pos.util.config.AppConfig;
import com.comerzzia.pos.util.i18n.I18N;

@Component
@Primary
public class ByLFacturaController extends FacturaController{

	@Autowired
	private Sesion sesion;
	
	@Autowired
	private ByLClientesService clienteService;
	
	@SuppressWarnings("rawtypes")
	@Override
    public void initializeForm() {
    	limpiarCampos();
    	
    	log.debug("initializeForm()");
    	ticketManager = (TicketManager) getDatos().get(FacturacionArticulosController.TICKET_KEY);
        datosFactura = ((TicketVenta)ticketManager.getTicket()).getCliente().getDatosFactura();
        if(datosFactura == null) {
        	inicializarDatosFidelizado();
        }
        
        frDatosFactura.clearErrorStyle();
        
        clienteTicket = new ClienteBean();
        clienteTicket.setIdGrupoImpuestos(sesion.getAplicacion().getTienda().getCliente().getIdGrupoImpuestos());

        if (datosFactura !=null){
        	clienteTicket.setCif(datosFactura.getCif());
        	clienteTicket.setCp(datosFactura.getCp());
        	clienteTicket.setDomicilio(datosFactura.getDomicilio());
        	clienteTicket.setProvincia(datosFactura.getProvincia());
        	clienteTicket.setTelefono1(datosFactura.getTelefono());
        	clienteTicket.setDesCliente(datosFactura.getNombre());
        	clienteTicket.setPoblacion(datosFactura.getPoblacion());
        	clienteTicket.setCodpais(datosFactura.getPais());
        	clienteTicket.setTipoIdentificacion(datosFactura.getTipoIdentificacion());
        	clienteTicket.setLocalidad(datosFactura.getLocalidad());
        }
        else{
        	clienteTicket = ticketManager.getTicket().getCliente().clone();
        }
        
        isClienteGenerico = clienteTicket.getCodCliente() != null && clienteTicket.getCodCliente().equals(sesion.getAplicacion().getTienda().getCliente().getCodCliente()) && clienteTicket.getDesCliente().equals(sesion.getAplicacion().getTienda().getCliente().getDesCliente());

		String codPais = clienteTicket.getCodpais();
		if(codPais!=null && !codPais.isEmpty()){
			PaisBean paisCliente = obtenerPais(codPais);
			if(paisCliente!=null){
				tfCodPais.setText(paisCliente.getCodPais());
				this.codPais = paisCliente.getCodPais().toUpperCase();
				clienteTicket.setPais(paisCliente.getDesPais());
			}
		}
		else{
			tfCodPais.setText(AppConfig.pais);
			this.codPais = AppConfig.pais.toUpperCase();
			PaisBean pais = obtenerPais(AppConfig.pais);
			tfDesPais.setText(pais.getDesPais());
		}

		this.tiposIdent = FXCollections.observableArrayList();
		loadTiposIdentificacion();
		cbTipoDocIdent.setItems(tiposIdent);

        refrescarDatosPantalla();
    }
	
	/**
	 * Acción de aceptar el formulario, realizamos comprobaciones 
	 * previas para comprobar que el documento está correctamente.
	 */
	@FXML
    public void accionAceptar(ActionEvent event){
        
		log.debug("aceptar() - Acción aceptar");

		try {
			if(cbTipoDocIdent.getSelectionModel().getSelectedItem() != null){
				if(clienteService.validarDocumento(cbTipoDocIdent.getSelectionModel()
						.getSelectedItem().getClaseValidacion(), cbTipoDocIdent.getSelectionModel()
						.getSelectedItem().getCodTipoIden(), tfCodPais.getText(), tfNumDocIdent.getText())){
					establecerClienteFactura((Event) event);
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
	 * Acción de aceptar el formulario pulsando intro, realizamos comprobaciones 
	 * previas para comprobar que el documento está correctamente.
	 */
	@FXML
    public void accionAceptarIntro(KeyEvent event){
    	
		log.debug("accionAceptarIntro()");

		try {
			if(cbTipoDocIdent.getSelectionModel().getSelectedItem() != null){
				if(clienteService.validarDocumento(cbTipoDocIdent.getSelectionModel()
						.getSelectedItem().getClaseValidacion(), cbTipoDocIdent.getSelectionModel()
						.getSelectedItem().getCodTipoIden(), tfCodPais.getText(), tfNumDocIdent.getText())){
					if(event.getCode() == KeyCode.ENTER){
			            establecerClienteFactura((Event) event);
			        }
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
	 * Establece para una factura los datos de un cliente que
	 * hayamos creado nuevo o buscado.
	 */
	protected void establecerClienteFactura(Event event){
    	
		log.debug("establecerClienteFactura()");
    	
        frDatosFactura.setNumDocIdent(frDatosFactura.trimTextField(tfNumDocIdent));
        frDatosFactura.setDomicilio(frDatosFactura.trimTextField(tfDomicilio));
        frDatosFactura.setProvincia(frDatosFactura.trimTextField(tfProvincia));
        frDatosFactura.setLocalidad(frDatosFactura.trimTextField(tfLocalidad));
        frDatosFactura.setPoblacion(frDatosFactura.trimTextField(tfPoblacion));
        frDatosFactura.setRazonSocial(frDatosFactura.trimTextField(tfRazonSocial));
        frDatosFactura.setcPostal(frDatosFactura.trimTextField(tfCP));
        frDatosFactura.setTelefono(frDatosFactura.trimTextField(tfTelefono));
        frDatosFactura.setPais(frDatosFactura.trimTextField(tfCodPais));
        frDatosFactura.setBanco(frDatosFactura.trimTextField(tfBanco));
        frDatosFactura.setBancoDomicilio(frDatosFactura.trimTextField(tfBancoDomicilio));
        frDatosFactura.setBancoPoblacion(frDatosFactura.trimTextField(tfBancoPoblacion));
        frDatosFactura.setBancoCCC(frDatosFactura.trimTextField(tfBancoCCC));

        if(validarFormularioDatosFactura()){
            
            DatosFactura datosFactura = new DatosFactura();
            datosFactura.setCif(tfNumDocIdent.getText());
            datosFactura.setCp(tfCP.getText());
            datosFactura.setDomicilio(tfDomicilio.getText());
            datosFactura.setProvincia(tfProvincia.getText());
            datosFactura.setTelefono(tfTelefono.getText());
            datosFactura.setNombre(tfRazonSocial.getText());
            datosFactura.setPoblacion(tfPoblacion.getText());
            datosFactura.setLocalidad(tfLocalidad.getText());
            datosFactura.setPais(tfCodPais.getText());
            datosFactura.setBanco(tfBanco.getText());
            datosFactura.setBancoDomicilio(tfBancoDomicilio.getText());
            datosFactura.setBancoPoblacion(tfBancoPoblacion.getText());
            datosFactura.setCcc(tfBancoCCC.getText());

            if(cbTipoDocIdent.getSelectionModel().getSelectedItem() != null){
            	datosFactura.setTipoIdentificacion(cbTipoDocIdent.getSelectionModel()
            			.getSelectedItem().getCodTipoIden());
            }
            
			((TicketVenta<?, ?, ?>) ticketManager.getTicket()).setDatosFacturacion(datosFactura);

			try {

				if (!ticketManager.getDocumentoActivo().getCodtipodocumento().equals(Documentos.FACTURA_COMPLETA)) {
					ticketManager.setDocumentoActivo(sesion.getAplicacion().getDocumentos().getDocumento(ticketManager.getDocumentoActivo().getTipoDocumentoFacturaDirecta()));
				}
				getStage().close();
                
            }catch(DocumentoException ex){
                log.error("No se pudo establecer el tipo documento factura",ex);
                VentanaDialogoComponent.crearVentanaAviso(
                		I18N.getTexto("Error al establecer el tipo de documento factura."), 
                		this.getStage());
            }
        }
        
    }
	
	@SuppressWarnings("rawtypes")
	private void inicializarDatosFidelizado() {
		FidelizacionBean fidelizado = ((TicketVenta)ticketManager.getTicket()).getCabecera().getDatosFidelizado();
		
		if(fidelizado != null) {
			datosFactura = new DatosFactura();
			datosFactura.setCif(fidelizado.getDocumento());
	        datosFactura.setCp(fidelizado.getCp());
	        datosFactura.setDomicilio(fidelizado.getDomicilio());
	        datosFactura.setProvincia(fidelizado.getProvincia());
	        datosFactura.setTelefono(null);
	        datosFactura.setNombre(fidelizado.getNombre());
	        datosFactura.setPoblacion(fidelizado.getPoblacion());
	        datosFactura.setLocalidad(fidelizado.getLocalidad());
	        datosFactura.setPais(fidelizado.getCodPais());
	        datosFactura.setTipoIdentificacion(fidelizado.getCodTipoIden());
		}
	}
	
}
