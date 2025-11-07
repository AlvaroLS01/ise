package com.comerzzia.bimbaylola.pos.gui.mantenimientos.fidelizados.datosgenerales;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.comerzzia.ByL.backoffice.persistencia.fidelizacion.fidelizados.FidelizadosBean;
import com.comerzzia.bimbaylola.pos.gui.mantenimientos.fidelizados.ByLFidelizadoController;
import com.comerzzia.bimbaylola.pos.services.clientes.ByLClientesService;
import com.comerzzia.bimbaylola.pos.services.clientes.ClienteValidatedException;
import com.comerzzia.bimbaylola.pos.services.pais.x.XPaisesService;
import com.comerzzia.bimbaylola.pos.services.pais.x.exception.XPaisException;
import com.comerzzia.bimbaylola.pos.services.pais.x.exception.XPaisNotFoundException;
import com.comerzzia.core.util.base.Estado;
import com.comerzzia.model.fidelizacion.fidelizados.contactos.TiposContactoFidelizadoBean;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.gui.mantenimientos.fidelizados.EstadoCivilGui;
import com.comerzzia.pos.gui.mantenimientos.fidelizados.SexoGui;
import com.comerzzia.pos.gui.mantenimientos.fidelizados.TipoIdentGui;
import com.comerzzia.pos.gui.mantenimientos.fidelizados.datosgenerales.PaneDatosGeneralesController;
import com.comerzzia.pos.persistence.paises.PaisBean;
import com.comerzzia.pos.persistence.tiposIdent.TiposIdentBean;
import com.comerzzia.pos.services.core.paises.PaisNotFoundException;
import com.comerzzia.pos.services.core.paises.PaisService;
import com.comerzzia.pos.services.core.paises.PaisServiceException;
import com.comerzzia.pos.services.core.sesion.Sesion;
import com.comerzzia.pos.services.core.tiposIdent.TiposIdentNotFoundException;
import com.comerzzia.pos.services.core.tiposIdent.TiposIdentService;
import com.comerzzia.pos.services.core.tiposIdent.TiposIdentServiceException;
import com.comerzzia.pos.util.config.SpringContext;
import com.comerzzia.pos.util.i18n.I18N;

@Component
@Primary
public class ByLPaneDatosGeneralesController extends PaneDatosGeneralesController{

	protected static final Logger log = Logger.getLogger(ByLPaneDatosGeneralesController.class);

	public static final String CONSENTIMIENTO_COLOR_VERDE = "01DF01";
	public static final String CONSENTIMIENTO_COLOR_ROJO = "FF0000";
	public static final String CONSENTIMIENTO_COLOR_AMARILLO = "FFFF00";
	public static final String CONSENTIMIENTO_COLOR_NARANJA = "FF8000";
	
	public static final String USA="US";
	public static final String PUERTO_RICO="PR";
	
	protected FidelizadosBean fidelizado;
	
	@Autowired
	private ByLClientesService clienteService;
	@Autowired
	private Sesion sesion;
		
	@Autowired
	private PaisService paisService = SpringContext.getBean(PaisService.class);
	@Autowired
	private TiposIdentService tiposIdentService = SpringContext.getBean(TiposIdentService.class);
	@Autowired
	private XPaisesService xPaisesService = SpringContext.getBean(XPaisesService.class);;
	
	@FXML
	protected VBox vbFirma, vbNotificaciones, vbConsentimiento;
	@FXML
	protected TextField tfPrefijo;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1){
		ObservableList<SexoGui> sexos = FXCollections.observableArrayList();
		SexoGui hombre = new SexoGui("H", "HOMBRE");
		SexoGui mujer = new SexoGui("M", "MUJER");
		sexos.add(mujer);
		sexos.add(hombre);
		cbSexo.setItems(sexos);
		tiposDocumento = FXCollections.observableArrayList();
		cbTipoDocumento.setItems(tiposDocumento);
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		dpFechaAlta.setDateFormat(df);
		
		/* Bloqueamos el campo de Código */
		tfCodigo.setDisable(Boolean.TRUE);
		tfPrefijo.setDisable(Boolean.TRUE);
		sesion.getAplicacion().getTienda().getCliente().getCodpais();
		tfCodPais.setText(sesion.getAplicacion().getTienda().getCliente().getCodpais());
		
		tfCodPais.focusedProperty().addListener(new ChangeListener<Boolean>(){
				@Override
				public void changed(ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) {
					if (StringUtils.isNotBlank(tfCodPais.getText())) {
						tfCodPais.setText(tfCodPais.getText().toUpperCase());
						if (!newValue && (codPais == null || !codPais.equals(tfCodPais.getText()))) {
							codPais = tfCodPais.getText();
							cargaTiposDocumento();
						}
					}
				}
			});
	}
	
	@FXML
	public void cargaTiposDocumento(){
		log.debug("cargaTiposDocumento() - Iniciamos la carga de los Tipos de Documento, Prefijo, y País...");
		List<TiposIdentBean> tiposIdent = new ArrayList<TiposIdentBean>();
		String codPais = tfCodPais.getText();
		if(codPais != null){			
			try{
				/* Realizamos la búsqueda del País */
				PaisBean pais = paisService.consultarCodPais(codPais);
				tfDesPais.setText(pais.getDesPais());
				/* Realizamos la búsqueda de los Tipos de Documento por el País */
				tiposIdent = tiposIdentService.consultarTiposIdent(null, true, codPais);
				List<TipoIdentGui> tiposGui = new ArrayList<TipoIdentGui>();
				for(TiposIdentBean tipoIdent : tiposIdent){
					TipoIdentGui tipoIdentGui = new TipoIdentGui(tipoIdent);
					tiposGui.add(tipoIdentGui);
				}
				tiposDocumento = FXCollections.observableArrayList(tiposGui);
				cbTipoDocumento.setItems(tiposDocumento);
				
				/* Realizamos la carga del Prefijo según el País */
				cargarPrefijo(pais.getCodPais(), pais.getUidInstancia());
				
				log.debug("cargaTiposDocumento() - Finalizada la carga de los Tipos de Documento, Prefijo, y País");
			}catch(Exception e){
				String mensajeError = "";
				if(e instanceof TiposIdentNotFoundException){
					mensajeError = "No se ha encontrado los Tipos de Documentos para el País indicado";
					log.error("cargaTiposDocumento() - " + mensajeError + " - " + e.getMessage(), e);
					tiposDocumento = FXCollections.observableArrayList(new ArrayList<TipoIdentGui>());
					cbTipoDocumento.setItems(tiposDocumento);
					VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto(mensajeError), getTabParentController().getStage());
				}else if(e instanceof TiposIdentServiceException){
					mensajeError = "Error al realizar la búsqueda de los Tipos de Documentos para el País indicado";
					log.error("cargaTiposDocumento() - " + mensajeError + " - " + e.getMessage(), e);
					VentanaDialogoComponent.crearVentanaError(getTabParentController().getStage(), I18N.getTexto(mensajeError), e);
				}else if(e instanceof PaisNotFoundException){
					mensajeError = "No se ha encontrado ningún resultado al realizar la búsqueda del País indicado";
					log.error("cargaTiposDocumento() - " + mensajeError + " - " + e.getMessage(), e);
					VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto(mensajeError), getTabParentController().getStage());
				}else if(e instanceof PaisServiceException){
					mensajeError = "Error al realizar la búsqueda del País indicado";
					log.error("cargaTiposDocumento() - " + mensajeError + " - " + e.getMessage(), e);
					VentanaDialogoComponent.crearVentanaError(getTabParentController().getStage(), I18N.getTexto(mensajeError), e);
				}else{
					mensajeError = "Se ha producido un error sin controlar";
					log.error("cargaTiposDocumento() - " + mensajeError + " - " + e.getMessage(), e);
					VentanaDialogoComponent.crearVentanaError(getTabParentController().getStage(), I18N.getTexto(mensajeError), e);
				}
			}
		}
	}
	
	/**
	 * Limpia los datos del formulario de cada uno de sus componentes.
	 */
	public void limpiarFormulario(){
		super.limpiarFormulario();
		ByLFidelizadoController controllerFidelizado = SpringContext.getBean(ByLFidelizadoController.class);
		controllerFidelizado.setConsentimiento(null);
		controllerFidelizado.setFirma(null);
		coloresFirmaConsentimiento(null, null);
	}
	
	/**
	 * Pone los campos bloqueados o editables según desde donde
	 * se realiza el acceso a la pantalla.
	 */
	public void camposEditables(boolean editable){
		super.camposEditables(editable);
	}
	
	/**
	 * Rellena el objeto de tipo fidelizado.
	 */
	public void selected(){
		FidelizadosBean newFidelizado =	(FidelizadosBean) getTabParentController().getFidelizado();
		
		fidelizado = newFidelizado;
		String codPaisTxt = tfCodPais.getText();
		if(codPaisTxt == null || codPaisTxt.isEmpty()){
			String newCodPais = fidelizado != null ? fidelizado.getCodPais() 
					: sesion.getAplicacion().getTienda().getCliente().getCodpais();
			if(codPais == null || !codPais.equals(newCodPais)){
				codPais = newCodPais;
				PaisBean pais = obtenerPais(codPais);
				tfCodPais.setText(pais.getCodPais());
				tfDesPais.setText(pais.getDesPais());
				List<TiposIdentBean> tiposIden = cargarTiposIdent(codPais);
				List<TipoIdentGui> tiposDoc = new ArrayList<TipoIdentGui>();
				for(TiposIdentBean tipoIden : tiposIden){
					TipoIdentGui tipoGui = new TipoIdentGui(tipoIden);
					tiposDoc.add(tipoGui);
				}
				tiposDocumento = FXCollections.observableArrayList(tiposDoc);
				cbTipoDocumento.setItems(tiposDocumento);
			}
		}
		
		/* Si la tienda esta en Usa o Puerto Rico solo se podra dar de alta fidelizados del Pais */
		String codPaisTienda = sesion.getAplicacion().getTienda().getCliente().getCodpais();
		if (codPaisTienda.equals(USA) || codPaisTienda.equals(PUERTO_RICO)) {
			tfCodPais.setDisable(true);
			tfDesPais.setDisable(true);
			btBuscarPais.setVisible(false);
		}
		/* Cargamos el Prefijo */
		if(fidelizado != null){
			if(fidelizado.getTiposContacto() != null && !fidelizado.getTiposContacto().isEmpty()){
				boolean tieneMovil = Boolean.FALSE;
				for(TiposContactoFidelizadoBean contactos : fidelizado.getTiposContacto()){
					if(ByLFidelizadoController.MOVIL.equals(contactos.getCodTipoCon())){
						tieneMovil = Boolean.TRUE;
					}
				}
				if(!tieneMovil){
					if(StringUtils.isNotBlank(codPaisTxt)){
						cargarPrefijo(codPaisTxt, sesion.getAplicacion().getUidInstancia());
					}else{
						cargarPrefijo(sesion.getAplicacion().getTienda().getCliente().getCodpais(), sesion.getAplicacion().getUidInstancia());
					}
				}
			}else{
				if(StringUtils.isNotBlank(codPaisTxt)){
					cargarPrefijo(codPaisTxt, sesion.getAplicacion().getUidInstancia());
				}else{
					cargarPrefijo(sesion.getAplicacion().getTienda().getCliente().getCodpais(), sesion.getAplicacion().getUidInstancia());
				}
			}
		}
		else{
			if(StringUtils.isNotBlank(codPaisTxt)){
				cargarPrefijo(codPaisTxt, sesion.getAplicacion().getUidInstancia());
			}else{
				cargarPrefijo(sesion.getAplicacion().getTienda().getCliente().getCodpais(), sesion.getAplicacion().getUidInstancia());
			}
		}
		
		if(ByLFidelizadoController.MODO_CONSULTA.equals(getTabParentController().getModo())){
			camposEditables(false);
		}else{
			camposEditables(true);
		}
		
		if(fidelizado != null){
			cargarDatosGenerales();
		}
	}
	
	/**
	 * Carga el formulario de pantalla con los datos del fidelizado.
	 */
	protected void cargarDatosGenerales(){
		log.debug("cargarDatosGenerales() - Iniciamos la carga de los datos del Panel de Datos Generales...");	
		
		tfCodigo.setText(StringUtils.isNotBlank(fidelizado.getCodFidelizado()) ? fidelizado.getCodFidelizado() : "");
		tfNumeroTarjeta.setText(StringUtils.isNotBlank(fidelizado.getNumeroTarjeta()) ? fidelizado.getNumeroTarjeta() : "");
		tfNombre.setText(StringUtils.isNotBlank(fidelizado.getNombre()) ? fidelizado.getNombre() : "");
		tfApellidos.setText(StringUtils.isNotBlank(fidelizado.getApellidos()) ? fidelizado.getApellidos() : "");
		tfDocumento.setText(StringUtils.isNotBlank(fidelizado.getDocumento()) ? fidelizado.getDocumento() : "");
		if(StringUtils.isNotBlank(fidelizado.getCodEstCivil()) && estadosCiviles != null){
			seleccionaEstadoCivil(fidelizado.getCodEstCivil());
		}
		if(StringUtils.isNotBlank(fidelizado.getCodTipoIden()) && tiposDocumento != null){
			seleccionaTipoIdent(fidelizado.getCodTipoIden());
		}
		if(StringUtils.isNotBlank(fidelizado.getSexo()) && cbSexo != null){
			seleccionaSexo(fidelizado.getSexo());
		}
		
		dpFechaNacimiento.setValue(fidelizado.getFechaNacimiento() != null ? fidelizado.getFechaNacimiento() : null);
		
		if(fidelizado.getTipoContacto(ByLFidelizadoController.EMAIL) != null){
			TiposContactoFidelizadoBean email = fidelizado.getTipoContacto("EMAIL");
			if(email != null && email.getEstadoBean() != Estado.BORRADO){
				chNotifEmail.setSelected(email.isRecibeNotificaciones());
				tfEmail.setText(email.getValor());
			}else{
				chNotifEmail.setSelected(false);
				tfEmail.setText("");
			}
		}
		if(fidelizado.getTipoContacto(ByLFidelizadoController.MOVIL) != null){
			TiposContactoFidelizadoBean movil = fidelizado.getTipoContacto("MOVIL");
			if(movil != null && movil.getEstadoBean() != Estado.BORRADO){
				chNotifMovil.setSelected(movil.isRecibeNotificaciones());
				tfMovil.setText(movil.getValor());
			}else{
				chNotifMovil.setSelected(false);
				tfMovil.setText("");
			}
		}
		
		tfCodPostal.setText(StringUtils.isNotBlank(fidelizado.getCp()) ? fidelizado.getCp() : "");
		tfProvincia.setText(StringUtils.isNotBlank(fidelizado.getProvincia()) ? fidelizado.getProvincia() : "");
		tfPoblacion.setText(StringUtils.isNotBlank(fidelizado.getPoblacion()) ? fidelizado.getPoblacion() : "");
		tfLocalidad.setText(StringUtils.isNotBlank(fidelizado.getLocalidad()) ? fidelizado.getLocalidad() : "");
		tfDomicilio.setText(StringUtils.isNotBlank(fidelizado.getDomicilio()) ? fidelizado.getDomicilio() : "");
		
		if(tiendaFavorita != null){
			tfCodTienda.setText(StringUtils.isNotBlank(tiendaFavorita.getCodAlm()) ? tiendaFavorita.getCodAlm() : "");
			tfDesTienda.setText(StringUtils.isNotBlank(tiendaFavorita.getDesAlm()) ? tiendaFavorita.getDesAlm() : "");
		}
		
		tfCodPais.setText(fidelizado.getCodPais());
		tfDesPais.setText(fidelizado.getDesPais());
		/* Cargamos el Prefijo */
		cargarPrefijo(fidelizado.getCodPais(), fidelizado.getUidInstancia());
		
		/* Cargamos el Consentimiento y la Firma */
		if(fidelizado.getConsentimientosFirma() != null){
			ByLFidelizadoController controladorFidelizado = SpringContext.getBean(ByLFidelizadoController.class);
			Map<String, String> consentimientos = new HashMap<String, String>();
			if("S".equals(fidelizado.getConsentimientosFirma().getConsentimientoRecibenoti())){
				consentimientos.put("1", "S");
			}else{
				consentimientos.put("1", "N");
			}
			if("S".equals(fidelizado.getConsentimientosFirma().getConsentimientoUsodatos())){
				consentimientos.put("2", "S");
			}else{
				consentimientos.put("2", "N");
			}
			controladorFidelizado.setConsentimiento(consentimientos);
			if(fidelizado.getConsentimientosFirma().getFirma() != null){
				controladorFidelizado.setFirma(fidelizado.getConsentimientosFirma().getFirma());
			}
			coloresFirmaConsentimiento(consentimientos, fidelizado.getConsentimientosFirma().getFirma());
		}
		
		log.debug("cargarDatosGenerales() - Finalizada la carga de los datos del Panel de Datos Generales");	
	}
	
	/**
	 * Realiza la carga del Prefijo según el País indicado.
	 */
	public void cargarPrefijo(String codPais, String uidInstancia){
		Integer prefijo = 0;
		/* Realizamos la consulta del Prefijo por el código del País */
		if(StringUtils.isNotBlank(codPais) && StringUtils.isNotBlank(uidInstancia)){
			try{
				prefijo = xPaisesService.consultarPrefijoPais(codPais, uidInstancia);
			}catch(Exception e){
				String mensajeError = "";
				if(e instanceof XPaisException || e instanceof XPaisNotFoundException){
					mensajeError = e.getMessage();
					log.warn("cargarDatosGenerales() - " + mensajeError + " - " + e.getMessage(), e);
				}else{
					mensajeError = "Se ha producido un error no controlado al consultar el Prefijo del País indicado";
					log.error("cargarDatosGenerales() - " + mensajeError + " - " + e.getMessage(), e);
				}
			}
		}
		if(prefijo != null){
			tfPrefijo.setText("+" + prefijo);
		}
	}
	
	protected void seleccionaSexo(String codSexo){
		if(codSexo != null){
			List<SexoGui> sexos = cbSexo.getItems();
			boolean seleccionado = false;
			for(SexoGui sexo : sexos){
				if(sexo.getCodigo().equals(codSexo)){
					cbSexo.getSelectionModel().select(sexo);
					seleccionado = true;
					break;
				}
			}
			if(!seleccionado){
				cbSexo.setValue(null);
			}
		}
	}
	
	protected void seleccionaEstadoCivil(String codEstadoCivil){
		boolean seleccionado = false;
		if(codEstadoCivil != null){
			for (EstadoCivilGui estadoCivil : estadosCiviles){
				if (estadoCivil.getCodEstadoCivil().equals(codEstadoCivil)){
					cbEstadoCivil.getSelectionModel().select(estadoCivil);
					seleccionado = true;
					break;
				}
			}
			if(!seleccionado){
				cbEstadoCivil.setValue(null);
			}
		}
	}
	
	protected void seleccionaTipoIdent(String codTipoIdent){
		if(codTipoIdent != null){
			boolean seleccionado = false;
			for(TipoIdentGui tipoIdent : tiposDocumento){
				if(tipoIdent.getCodigo().equals(codTipoIdent)){
					cbTipoDocumento.getSelectionModel().select(tipoIdent);
					seleccionado = true;
					break;
				}
			}
			
			if(!seleccionado){
				cbTipoDocumento.setValue(null);
			}
		}
	}
	
	/**
	 * Acción que se realiza al pulsar sobre el botón de validar 
	 * el documento del formulario. Realiza varias comprobaciones 
	 * llamando al método de validar documento.
	 */
	@FXML
	public void accionValidarDocumento(){
		
		try {
			if(cbTipoDocumento.getSelectionModel().getSelectedItem() != null){
				if(clienteService.validarDocumento(cbTipoDocumento.getSelectionModel()
						.getSelectedItem().getClaseValidacion(),cbTipoDocumento.getSelectionModel()
						.getSelectedItem().getCodigo(), tfCodPais.getText(), tfDocumento.getText())){
					VentanaDialogoComponent.crearVentanaInfo("Documento válido"
							, getTabParentController().getStage());
				}
			}else{
				/* Sino tenemos nada seleccionado, nos envia un mensaje de aviso, 
				 * es necesario el tipo para poder comprobar. */
				VentanaDialogoComponent.crearVentanaAviso(
						I18N.getTexto("Debe seleccionar Tipo documento"),
						getTabParentController().getStage());
			}
		}catch(ClienteValidatedException e){
			VentanaDialogoComponent.crearVentanaAviso(e.getMessage()
					, getTabParentController().getStage());
		}
		
	}

	/**
	 * Realiza comprobaciones para saber que color poner en los botones de firma y consentimiento.
	 */
	public void coloresFirmaConsentimiento(Map<String, String> consentimiento, byte[] firma){
		/* Comprobamos que tiene hecho los dos consentimientos */
		if(consentimiento != null && !consentimiento.isEmpty()){
			Boolean consentimientoRecibirNoti = consentimiento.get("1") != null ? (consentimiento.get("1").equals("S") ? Boolean.TRUE : Boolean.FALSE) : Boolean.FALSE;
			Boolean consentimientoUsoDatos = consentimiento.get("2") != null ? (consentimiento.get("2").equals("S") ? Boolean.TRUE : Boolean.FALSE) : Boolean.FALSE;
			/* Si es correcto color verde, sino rojo */
			if(consentimientoUsoDatos){
				vbConsentimiento.setStyle("-fx-background-color: #" + CONSENTIMIENTO_COLOR_VERDE + ";");
			}else{
				vbConsentimiento.setStyle("-fx-background-color: #" + CONSENTIMIENTO_COLOR_ROJO + ";");
			}
			if(consentimientoRecibirNoti){
				vbNotificaciones.setStyle("-fx-background-color: #" + CONSENTIMIENTO_COLOR_VERDE + ";");
			}else{
				vbNotificaciones.setStyle("-fx-background-color: #" + CONSENTIMIENTO_COLOR_ROJO + ";");
			}
		}else{
			/* Si el objeto es nulo, ponemos el rojo */
			vbNotificaciones.setStyle("-fx-background-color: #" + CONSENTIMIENTO_COLOR_ROJO + ";");
			vbConsentimiento.setStyle("-fx-background-color: #" + CONSENTIMIENTO_COLOR_ROJO + ";");
		}
		
		if(firma != null){
			/* Si todo es correcto debemos ponerle el color verde */
			vbFirma.setStyle("-fx-background-color: #" + CONSENTIMIENTO_COLOR_VERDE + ";");
		}else{
			/* Si nada es correcto, ponemos el rojo */
			vbFirma.setStyle("-fx-background-color: #" + CONSENTIMIENTO_COLOR_ROJO + ";");
		}
	}

	public FidelizadosBean getFidelizado(){
		return fidelizado;
	}
	
}
