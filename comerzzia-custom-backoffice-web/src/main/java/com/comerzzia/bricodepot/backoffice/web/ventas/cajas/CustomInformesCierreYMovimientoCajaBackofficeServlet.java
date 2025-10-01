package com.comerzzia.bricodepot.backoffice.web.ventas.cajas;



import javax.servlet.annotation.WebServlet;

import com.comerzzia.bricodepot.backoffice.web.ventas.cajas.acciones.BricoDepotImprimirAccion;
import com.comerzzia.web.backoffice.ventas.cajas.acciones.EjecutarAccion;
import com.comerzzia.web.base.ControladorServlet;

@WebServlet(value = "/informesCierreYMovimientoCajaBackoffice", description = "Servlet de informes de caja de tienda del backoffice", displayName = "InformesCierreYMovimientoCajaBackofficeServlet", name = "InformesCierreYMovimientoCajaBackofficeServlet")
public class CustomInformesCierreYMovimientoCajaBackofficeServlet extends ControladorServlet {

	private static final long serialVersionUID = 1071643186151558069L;

	@Override
	protected void loadAcciones() {
		this.añadirAccionDefault(new EjecutarAccion());
		this.añadirAccion(new BricoDepotImprimirAccion());
	}
}
