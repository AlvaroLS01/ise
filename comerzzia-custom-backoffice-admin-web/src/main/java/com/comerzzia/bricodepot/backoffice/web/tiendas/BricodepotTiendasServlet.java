package com.comerzzia.bricodepot.backoffice.web.tiendas;

import javax.servlet.annotation.WebServlet;

import com.comerzzia.bricodepot.backoffice.web.tiendas.acciones.BricodepotEjecutarAccion;
import com.comerzzia.bricodepot.backoffice.web.tiendas.acciones.BricodepotLeerFormularioAccion;
import com.comerzzia.bricodepot.backoffice.web.tiendas.acciones.BricodepotSalvarAccion;
import com.comerzzia.bricodepot.backoffice.web.tiendas.acciones.BricodepotVerFormularioAccion;
import com.comerzzia.web.base.ControladorServlet;
import com.comerzzia.web.general.tiendas.acciones.AltaAccion;
import com.comerzzia.web.general.tiendas.acciones.AsistenteAltaAccion;
import com.comerzzia.web.general.tiendas.acciones.BuscarAccion;
import com.comerzzia.web.general.tiendas.acciones.BuscarCodigoPostalAccion;
import com.comerzzia.web.general.tiendas.acciones.BuscarMedioPagoAccion;
import com.comerzzia.web.general.tiendas.acciones.BuscarServicioAccion;
import com.comerzzia.web.general.tiendas.acciones.EditarAccion;
import com.comerzzia.web.general.tiendas.acciones.EliminarAccion;
import com.comerzzia.web.general.tiendas.acciones.ImprimirAccion;
import com.comerzzia.web.general.tiendas.acciones.VerAccion;

@WebServlet(value = "/tiendas", description = "Servlet de Tiendas", displayName = "TiendasServlet", name = "TiendasServlet")
public class BricodepotTiendasServlet extends ControladorServlet {

	private static final long serialVersionUID = -1219187497550705272L;

	@Override
	protected void loadAcciones() {
		this.añadirAccionDefault(new BricodepotEjecutarAccion());
		this.añadirAccion(new AltaAccion());
		this.añadirAccion(new BuscarAccion());
		this.añadirAccion(new BuscarServicioAccion());
		this.añadirAccion(new EditarAccion());
		this.añadirAccion(new EliminarAccion());
		this.añadirAccion(new BricodepotSalvarAccion());
		this.añadirAccion(new VerAccion());
		this.añadirAccion(new BricodepotLeerFormularioAccion());
		this.añadirAccion(new BricodepotVerFormularioAccion());
		this.añadirAccion(new ImprimirAccion());
		this.añadirAccion(new AsistenteAltaAccion());
		this.añadirAccion(new BuscarMedioPagoAccion());
		this.añadirAccion(new BuscarCodigoPostalAccion());

	}

}
