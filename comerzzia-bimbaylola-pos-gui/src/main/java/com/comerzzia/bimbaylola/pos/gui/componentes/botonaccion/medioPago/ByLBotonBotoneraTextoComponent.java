/**
 * ComerZZia 3.0
 *
 * Copyright (c) 2008-2015 Comerzzia, S.L.  All Rights Reserved.
 *
 * THIS WORK IS  SUBJECT  TO  SPAIN  AND  INTERNATIONAL  COPYRIGHT  LAWS  AND
 * TREATIES.   NO  PART  OF  THIS  WORK MAY BE  USED,  PRACTICED,  PERFORMED
 * COPIED, DISTRIBUTED, REVISED, MODIFIED, TRANSLATED,  ABRIDGED, CONDENSED,
 * EXPANDED,  COLLECTED,  COMPILED,  LINKED,  RECAST, TRANSFORMED OR ADAPTED
 * WITHOUT THE PRIOR WRITTEN CONSENT OF COMERZZIA, S.L. ANY USE OR EXPLOITATION
 * OF THIS WORK WITHOUT AUTHORIZATION COULD SUBJECT THE PERPETRATOR TO
 * CRIMINAL AND CIVIL LIABILITY.
 *
 * CONSULT THE END USER LICENSE AGREEMENT FOR INFORMATION ON ADDITIONAL
 * RESTRICTIONS.
 */

package com.comerzzia.bimbaylola.pos.gui.componentes.botonaccion.medioPago;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import org.apache.log4j.Logger;

import com.comerzzia.bimbaylola.pos.gui.componentes.botonera.medioPago.ByLConfiguracionBotonMedioPagoOriginalBean;
import com.comerzzia.pos.core.gui.componentes.botonaccion.BotonBotoneraComponent;
import com.comerzzia.pos.core.gui.componentes.botonera.ConfiguracionBotonBean;
import com.comerzzia.pos.services.ticket.pagos.PagoTicket;


public class ByLBotonBotoneraTextoComponent extends BotonBotoneraComponent {

    private static final Logger log = Logger.getLogger(ByLBotonBotoneraTextoComponent.class.getName());
    // Componentes internos de botón
    HBox linea1, linea2, linea3;
    
    
    
    ByLConfiguracionBotonMedioPagoOriginalBean configMedioPago;
    
    /**
     * Constructor de botón de tipo medio de pago
     */
    public ByLBotonBotoneraTextoComponent() {
        super();
        log.debug("ByLBotonBotoneraTextoComponent() - Creando componentes internos del elemento Boton de Botonera");

        //establecemos un estilo para el botón
        btAccion.getStyleClass().add("boton-mediopago-botonera");
                
        panelBoton.setMinHeight(40.0);
        panelBoton.setMaxHeight(40.0);
        panelBoton.setMinWidth(80.0);

        btAccion.setMinHeight(40.0);
        btAccion.setMaxHeight(40.0);
        btAccion.setMinWidth(80.0);

        btAccion.setPickOnBounds(true);

        AnchorPane.setTopAnchor(this, 0.0d);
        AnchorPane.setBottomAnchor(this, 0.0d);
        AnchorPane.setLeftAnchor(this, 0.0d);
        AnchorPane.setRightAnchor(this, 0.0d);
        AnchorPane.setTopAnchor(panelBoton, 0.0d);
        AnchorPane.setBottomAnchor(panelBoton, 0.0d);
        AnchorPane.setLeftAnchor(panelBoton, 0.0d);
        AnchorPane.setRightAnchor(panelBoton, 0.0d);
        AnchorPane.setTopAnchor(panelInterno, 0.0d);
        AnchorPane.setBottomAnchor(panelInterno, 0.0d);
        AnchorPane.setLeftAnchor(panelInterno, 0.0d);
        AnchorPane.setRightAnchor(panelInterno, 0.0d);
    }

    /**
     * Inicialización personalizada de componente botón de tipo medio de pago
     * @param ancho
     * @param alto 
     */
    @Override
    protected void inicializaComponentesPersonalizados(Double ancho, Double alto) {
        log.debug("inicializaComponentesPersonalizados() - creando elementos personalizados del botón");
        
        // Ponemos el texto al botón
        btAccion.setText(configuracion.getTexto());        
    }
    
    public void setConfiguracionBoton(ConfiguracionBotonBean configuracion) {
        log.debug("setConfiguracionBoton() establecemos la configuración al botón");
        this.configMedioPago = (ByLConfiguracionBotonMedioPagoOriginalBean)configuracion;
        super.setConfiguracionBoton(configuracion);
    }
    
    public PagoTicket getPago(){
        return configMedioPago.getPago();
    }
}
