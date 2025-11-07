package com.comerzzia.bimbaylola.pos.gui.mantenimientos.fidelizados.ultimasventas;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.comerzzia.ByL.backoffice.persistencia.fidelizacion.fidelizados.FidelizadosBean;
import com.comerzzia.model.fidelizacion.fidelizados.FidelizadoBean;
import com.comerzzia.model.ventas.albaranes.articulos.ArticuloAlbaranVentaBean;
import com.comerzzia.pos.core.gui.RestBackgroundTask;
import com.comerzzia.pos.gui.mantenimientos.fidelizados.ConsultarUltimasVentasFidelizadoTask;
import com.comerzzia.pos.gui.mantenimientos.fidelizados.VentaGui;
import com.comerzzia.pos.gui.mantenimientos.fidelizados.ultimasventas.PaneUltimasVentasController;
import com.comerzzia.pos.services.core.sesion.Sesion;
import com.comerzzia.pos.services.core.variables.VariablesServices;
import com.comerzzia.pos.util.config.SpringContext;
import com.comerzzia.rest.client.fidelizados.ConsultarFidelizadoRequestRest;

@Component
@Primary
public class ByLPaneUltimasVentasController extends PaneUltimasVentasController{
	
	protected static final Logger log = Logger.getLogger(ByLPaneUltimasVentasController.class);
		
	
	protected FidelizadosBean fidelizado;

	@Autowired
	private Sesion sesion;
	
	public void selected(){
		FidelizadosBean newFidelizado =	(FidelizadosBean) getTabParentController().getFidelizado();
		if(newFidelizado != null && (fidelizado == null ||
				!fidelizado.getIdFidelizado().equals(newFidelizado.getIdFidelizado()))){
			cargarVentas(newFidelizado);
		}	
		fidelizado = newFidelizado;		
	}

	public void setFidelizado(FidelizadosBean fidelizado){
		this.fidelizado = fidelizado;
	}

	@Override
	protected void cargarVentas(FidelizadoBean fidelizado) {
		String apiKey = variablesService.getVariableAsString(VariablesServices.WEBSERVICES_APIKEY);
		String uidActividad = sesion.getAplicacion().getUidActividad();
		ConsultarFidelizadoRequestRest consultaVentas = new ConsultarFidelizadoRequestRest(apiKey, uidActividad);
		consultaVentas.setIdFidelizado(String.valueOf(fidelizado.getIdFidelizado()));
		Date fechaHoy = new Date();
		Calendar calDesde = Calendar.getInstance();
		calDesde.set(Calendar.YEAR, calDesde.get(Calendar.YEAR)-2);
		Date fechaDesde = calDesde.getTime();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		consultaVentas.setFechaDesde(format.format(fechaDesde));
		consultaVentas.setFechaHasta(format.format(fechaHoy));
		ConsultarUltimasVentasFidelizadoTask consultarVentasTask = SpringContext.getBean(ConsultarUltimasVentasFidelizadoTask.class, 
				consultaVentas, 
					new RestBackgroundTask.FailedCallback<List<ArticuloAlbaranVentaBean>>() {
						@Override
						public void succeeded(List<ArticuloAlbaranVentaBean> result) {						
							ventasFidelizado.clear();
							for(ArticuloAlbaranVentaBean art : result){
								VentaGui artGui = new VentaGui(art);
								ventasFidelizado.add(artGui);
							}
							tableVentas.setItems(ventasFidelizado);
						}
						@Override
						public void failed(Throwable throwable) {
							getTabParentController().getApplication().getMainView().close();
						}
					}, getTabParentController().getStage());
		consultarVentasTask.start();
	}
	
}
