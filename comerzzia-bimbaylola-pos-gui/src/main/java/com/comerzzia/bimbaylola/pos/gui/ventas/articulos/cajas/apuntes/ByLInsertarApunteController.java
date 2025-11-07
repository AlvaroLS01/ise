package com.comerzzia.bimbaylola.pos.gui.ventas.articulos.cajas.apuntes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.comerzzia.bimbaylola.pos.dispositivo.impresora.epsontm30.EpsonTM30;
import com.comerzzia.bimbaylola.pos.services.cajas.ByLCajasService;
import com.comerzzia.bimbaylola.pos.services.core.sesion.ByLSesionCaja;
import com.comerzzia.bimbaylola.pos.services.epsontse.EpsonTSEService;
import com.comerzzia.pos.core.dispositivos.Dispositivos;
import com.comerzzia.pos.core.dispositivos.configuracion.ConfiguracionDispositivo;
import com.comerzzia.pos.core.gui.POSApplication;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.core.gui.validation.ValidationUI;
import com.comerzzia.pos.gui.ventas.cajas.apuntes.FormularioApunteBean;
import com.comerzzia.pos.gui.ventas.cajas.apuntes.InsertarApunteController;
import com.comerzzia.pos.persistence.cajas.movimientos.CajaMovimientoBean;
import com.comerzzia.pos.util.format.FormatUtil;
import com.comerzzia.pos.util.i18n.I18N;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

@Component
@Primary
public class ByLInsertarApunteController extends InsertarApunteController {

	private static final Logger log = Logger.getLogger(ByLInsertarApunteController.class.getName());
	
	@Autowired
	protected ByLCajasService cajasService;
	
	@Autowired
	protected EpsonTSEService epsonTSEService;

	@Autowired
	protected ByLSesionCaja sesionCaja;
	
	/**
	 * Acción que se realiza al intentar inserta un apunte.
	 * 
	 * @param event
	 *            : Objeto de tipo ActionEvent.
	 */
	@FXML
	public void accionAceptar(ActionEvent event) {
		log.debug("accionAceptar()");

		if (cajasService.comprobarCajaMaster()) {
			BigDecimal saldo = FormatUtil.getInstance().desformateaBigDecimal(tfImporte.getText());
			accionTSE(saldo);
			try {
				if ((this.concepto == null) || (!this.concepto.getCodConceptoMovimiento().equals(tfCodConcepto.getText()))) {
					if (!validaConcepto(tfCodConcepto.getText())) {
						return;
					}
				}

				frApunte.setDocumento(tfDocumento.getText());
				frApunte.setImporte(tfImporte.getText());
				frApunte.setConcepto(concepto);
				frApunte.setDescripcion(tfDescConcepto.getText());
				if (accionValidarFormulario()) {
					CajaMovimientoBean mov = sesionCaja.crearApunteManual(frApunte.getImporteAsBigDecimal(), frApunte.getConcepto().getCodConceptoMovimiento(), frApunte.getDocumento(),
					        tfDescConcepto.getText());

					// [BYL-299] - Mensaje movimientos de caja
//					if(mov != null) {
//						cajasService.crearDocumentoMovimientoCaja(mov);
//					}

					imprimirMovimiento(mov);
					getStage().close();
				}
				else {
					log.debug("datos del apunte invalidos");
				}
			}
			catch (Exception e) {
				log.error("accionAceptar() - Error inesperado - " + e.getCause(), e);
				VentanaDialogoComponent.crearVentanaError(getStage(), e);
			}
		}
		else {
			cajaMasterCerrada();
		}
	}

    private boolean accionValidarFormulario() {       
        boolean bResultado = true;
		
        //Inicializamos la etiqueta de error
        frApunte.clearErrorStyle();
		lbError.setText("");
		
        // Validamos el formulario 
        Set<ConstraintViolation<FormularioApunteBean>> constraintViolations = ValidationUI.getInstance().getValidator().validate(frApunte);
        Iterator<ConstraintViolation<FormularioApunteBean>> iterator = constraintViolations.iterator();
        while (iterator.hasNext()){
            ConstraintViolation<FormularioApunteBean> next = iterator.next();
            frApunte.setErrorStyle(next.getPropertyPath(), true);

            lbError.setText(next.getMessage());

            bResultado = false;
        }
        
        if (!bResultado){
        	estableceFoco();
        }
        
        return bResultado;
    }
	
    private void estableceFoco() {	
		if (tfImporte.getText().trim().equals("")){
			tfImporte.requestFocus();
		}
		else{
			if (tfCodConcepto.getText().trim().equals("")){
				tfCodConcepto.requestFocus();
			}
			else{
				if (tfDescConcepto.getText().trim().equals("")){
					tfDescConcepto.requestFocus();
				}
			}
		}
	}
    
	/**
	 * Envía un mensaje por pantalla, que indica que la caja no está abierta y te envía a la pantalla principal.
	 */
	private void cajaMasterCerrada() {
		VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("La caja a la que estaba conectado se ha cerrado."), this.getStage());
		/* Cierra el Popup de apuntes. */
		getStage().close();
		/* Vuelve a la pantalla principal. */
		POSApplication.getInstance().getMainView().close();
	}

	private void accionTSE(BigDecimal saldo) {
		saldo = saldo.setScale(2, BigDecimal.ROUND_HALF_UP);

		String tipoTransaccion = EpsonTSEService.TIPO_TRANSACCION_EINZAHLUNG;
		if (saldo.compareTo(new BigDecimal(0)) < 0) {
			tipoTransaccion = EpsonTSEService.TIPO_TRANSACCION_ENTNAHME;
		}

		try {
			if (Dispositivos.getInstance().getImpresora1() instanceof EpsonTM30) {
				if (Dispositivos.getInstance().getImpresora1().getConfiguracion().getNombreConexion().equals(EpsonTSEService.NOMBRE_CONEXION_TSE)) {
					if (!epsonTSEService.socketConectado()) {
						try {
							ConfiguracionDispositivo impresora = Dispositivos.getInstance().getImpresora1().getConfiguracion();
							String ip = impresora.getParametrosConfiguracion().get("IP");
							if (!StringUtils.isBlank(ip)) {
								epsonTSEService.procesoInicialTSE(ip);
							}
						}
						catch (Exception e) {
							log.error("accionAceptar() - Ha ocurrido un error al realizar el proceso inicial del TSE", e);
						}
					}

					String peticionStartTransaction = epsonTSEService.startTransaction(EpsonTSEService.PROCESSTYPE_SONSTINGERVORGANG, tipoTransaccion, saldo.abs(), null, null, true);
					epsonTSEService.enviarPeticion(peticionStartTransaction);
					String respuestaStartTransaction = epsonTSEService.lecturaSocket();

					List<String> listaCampos = new ArrayList<String>();
					listaCampos.add("transactionNumber");
					HashMap<String, String> mapaCampos = epsonTSEService.tratamientoRespuesta(respuestaStartTransaction, listaCampos);
					String transactionNumber = mapaCampos.get("transactionNumber");

					String peticionFinishTransaction = epsonTSEService.finishTransaction(EpsonTSEService.PROCESSTYPE_SONSTINGERVORGANG, tipoTransaccion, saldo.abs(), null, null,
					        Integer.parseInt(transactionNumber), false);
					epsonTSEService.enviarPeticion(peticionFinishTransaction);
					epsonTSEService.lecturaSocket();

					// String resultFinish = epsonTSEService.tratamientoRespuestaResult(respuestaFinishTransaction);
				}
			}
		}
		catch (Exception e) {
			log.error("tseAperturaCaja() - Error al realizar el proceso de apertura de caja -" + e.getMessage());
		}

	}

}
