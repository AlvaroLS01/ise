package com.comerzzia.bimbaylola.pos.gui.mantenimientos.fidelizados.observaciones;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.comerzzia.ByL.backoffice.persistencia.fidelizacion.fidelizados.FidelizadosBean;
import com.comerzzia.pos.gui.mantenimientos.fidelizados.observaciones.PaneObservacionesController;

@Component
@Primary
public class ByLPaneObservacionesController extends PaneObservacionesController{
	
	protected static final Logger log = Logger.getLogger(ByLPaneObservacionesController.class);
		
	protected FidelizadosBean fidelizado;
	
	public void selected(){
		
		FidelizadosBean newFidelizado =	(FidelizadosBean) getTabParentController().getFidelizado();
		if(newFidelizado != null && (fidelizado == null ||
				!fidelizado.getIdFidelizado().equals(newFidelizado.getIdFidelizado()))){
			taObservaciones.setText(newFidelizado.getObservaciones());
		}	
		
		fidelizado = newFidelizado;
		
		if("CONSULTA".equals(getTabParentController().getModo())){
			editarCampos(false);
		}else{
			editarCampos(true);
		}		
		
	}

	public FidelizadosBean getFidelizado() {
		return fidelizado;
	}

	public void setFidelizado(FidelizadosBean fidelizado) {
		this.fidelizado = fidelizado;
	}

}
