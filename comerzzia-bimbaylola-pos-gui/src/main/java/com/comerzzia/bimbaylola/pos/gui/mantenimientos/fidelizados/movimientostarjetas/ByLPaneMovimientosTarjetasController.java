package com.comerzzia.bimbaylola.pos.gui.mantenimientos.fidelizados.movimientostarjetas;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.comerzzia.ByL.backoffice.persistencia.fidelizacion.fidelizados.FidelizadosBean;
import com.comerzzia.pos.gui.mantenimientos.fidelizados.movimientostarjetas.PaneMovimientosTarjetasController;

@Component
@Primary
public class ByLPaneMovimientosTarjetasController extends PaneMovimientosTarjetasController{
	
	protected static final Logger log = Logger.getLogger(ByLPaneMovimientosTarjetasController.class);
	
	protected FidelizadosBean fidelizado;
		
	public void selected(){
		FidelizadosBean newFidelizado =	(FidelizadosBean) getTabParentController().getFidelizado();
		if(newFidelizado != null && (fidelizado == null ||
				!fidelizado.getIdFidelizado().equals(newFidelizado.getIdFidelizado()))){
			cargarTarjetas(newFidelizado);
		}	
		fidelizado = newFidelizado;
	}
		
	public void setFidelizado(FidelizadosBean fidelizado){
		this.fidelizado = fidelizado;
	}

}
