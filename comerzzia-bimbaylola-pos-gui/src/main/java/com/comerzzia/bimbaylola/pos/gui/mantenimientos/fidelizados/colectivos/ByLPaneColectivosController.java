package com.comerzzia.bimbaylola.pos.gui.mantenimientos.fidelizados.colectivos;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.comerzzia.ByL.backoffice.persistencia.fidelizacion.fidelizados.FidelizadosBean;
import com.comerzzia.model.fidelizacion.fidelizados.FidelizadoBean;
import com.comerzzia.pos.gui.mantenimientos.fidelizados.colectivos.PaneColectivosController;

@Component
@Primary
public class ByLPaneColectivosController extends PaneColectivosController{
	
	protected static final Logger log = Logger.getLogger(ByLPaneColectivosController.class);
	
	protected FidelizadoBean fidelizado;

	public void selected(){
		FidelizadosBean newFidelizado =	(FidelizadosBean) getTabParentController().getFidelizado();
		if(newFidelizado != null && (fidelizado == null ||
				!fidelizado.getIdFidelizado().equals(newFidelizado.getIdFidelizado()))){
			cargarColectivos(newFidelizado);
		}
		fidelizado = newFidelizado;
	}
	
	public void setFidelizado(FidelizadosBean fidelizado){
		this.fidelizado = fidelizado;
	}

}
