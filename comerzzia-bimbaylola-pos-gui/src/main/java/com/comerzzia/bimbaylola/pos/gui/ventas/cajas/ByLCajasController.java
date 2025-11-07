package com.comerzzia.bimbaylola.pos.gui.ventas.cajas;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.comerzzia.bimbaylola.pos.gui.ventas.cajas.accionesimpresorafiscal.AccionesImpresoraFiscalView;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.core.gui.permisos.exception.SinPermisosException;
import com.comerzzia.pos.gui.ventas.cajas.CajasController;

@Component
@Primary
public class ByLCajasController extends CajasController {

	/* Permiso para controlar la inserción de apuntes. */
	public static final String PERMISO_INSERCION_APUNTES = "INSERCIÓN DE APUNTES";

	public void insertarApunte() {

		if (comprobarPermisoApuntes()) {
			super.insertarApunte();
		}

	}

	/**
	 * Realiza una comprobacion del permiso de "Inserción de apuntes".
	 * 
	 * @return permisos : Boolean que indica "true" si tiene permisos, y "false" en caso de no tener permisos.
	 */
	public Boolean comprobarPermisoApuntes() {

		boolean permisos = true;

		try {
			compruebaPermisos(PERMISO_INSERCION_APUNTES);
		}
		catch (SinPermisosException e) {
			permisos = false;
			VentanaDialogoComponent.crearVentanaAviso(e.getMessage(), getStage());
		}

		return permisos;

	}

	public void opImpresoraFiscal() {
		getApplication().getMainView().showModalCentered(AccionesImpresoraFiscalView.class, null, this.getStage());
	}

}
