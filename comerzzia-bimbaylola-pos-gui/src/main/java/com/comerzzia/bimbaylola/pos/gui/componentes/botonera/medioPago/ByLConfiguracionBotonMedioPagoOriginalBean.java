package com.comerzzia.bimbaylola.pos.gui.componentes.botonera.medioPago;

import com.comerzzia.pos.core.gui.componentes.botonera.ConfiguracionBotonBean;
import com.comerzzia.pos.services.ticket.pagos.PagoTicket;

public class ByLConfiguracionBotonMedioPagoOriginalBean extends ConfiguracionBotonBean {
	
	private PagoTicket pago;
    
    public ByLConfiguracionBotonMedioPagoOriginalBean(String rutaImagen, String texto, String textoAccesoRapido, String nombreAccion, String tipoAccion, PagoTicket pago){
        super(rutaImagen, texto, textoAccesoRapido, nombreAccion, tipoAccion);
        
        this.pago = pago;
    }

	public PagoTicket getPago() {
		return pago;
	}

	public void setPago(PagoTicket pago) {
		this.pago = pago;
	}

}
