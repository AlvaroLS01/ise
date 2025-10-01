package com.comerzzia.bricodepot.backoffice.web.general.importacionmasiva.acciones;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.comerzzia.bricodepot.backoffice.services.general.importacionmasiva.CustomServicioImportacionMasivaImpl;
import com.comerzzia.core.servicios.contadores.ContadorException;
import com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.util.numeros.Numero;
import com.comerzzia.web.base.Accion;
import com.comerzzia.web.base.Vista;
import com.comerzzia.web.base.WebKeys;

import javazoom.upload.MultipartFormDataRequest;
import javazoom.upload.UploadFile;
import jxl.read.biff.BiffException;

public class ImportarAccion extends Accion {

	protected static Logger log = Logger.getLogger(ImportarAccion.class);

	private static final Vista NEXT_PAGE = new Vista("backoffice/general/importacionmasiva/jsp/resumen.jsp", Vista.INTERNA);

	@Override
	public String getNombre() {
		return "importar";
	}

	@SuppressWarnings("deprecation")
	@Override
	public Vista ejecutar(HttpServletRequest request) {
		MultipartFormDataRequest mrequest = (MultipartFormDataRequest) request.getAttribute(WebKeys.MULTIPART_REQUEST);
		DatosSesionBean datosSesion = (DatosSesionBean) request.getSession().getAttribute(WebKeys.DATOS_SESION);
		try {

			// Comprobamos los permisos de la acción
			PermisosEfectivosAccionBean permisos = datosSesion.getPermisos(this.getAccionMenu());
			if (!permisos.isPuedeEjecutar()) {
				return SIN_PERMISO;
			}
			if (mrequest != null) {

				String tipoImportacion = mrequest.getParameter("tipoImportacion");
				Long idAccion = Numero.desformateaLong(tipoImportacion, null);

				UploadFile fichero = (UploadFile) mrequest.getFiles().get("fichero");
				if (fichero.getData() != null) {
					List<String> mensajes = null;
					if (idAccion == 1010L) {
						mensajes = CustomServicioImportacionMasivaImpl.get().importarCodigosAbono(idAccion, fichero.getData(), datosSesion);
					}
					else if(idAccion == 70000L) {
						mensajes = CustomServicioImportacionMasivaImpl.get().importarAnticipos(idAccion, fichero.getData(), datosSesion);
					}
					else {
						//Descomentar cuando se solucione en el  standard los modificadores de acceso de los servicios inyectados en ServicioImportacionMasivaImp
						//mensajes = ServicioImportacionMasivaImpl.get().importarEtiquetas(idAccion, fichero.getData(), datosSesion);
						
						mensajes = CustomServicioImportacionMasivaImpl.get().importarEtiquetas(idAccion, fichero.getData(), datosSesion);
					}
					request.setAttribute("mensajes", mensajes);

					return NEXT_PAGE;
				}
			}

			return getControlador().getAccion("ejecutar").ejecutar(request);
		}
		catch (BiffException e) {
			log.error("Se ha producido un error al cargar el fichero", e);
			setMensajeError(request, datosSesion.getTranslation()._("Se ha producido un error al cargar el fichero"));
			return getControlador().getAccion("ejecutar").ejecutar(request);
		}
		catch (IOException e) {
			log.error("Se ha producido un error al cargar el fichero", e);
			setMensajeError(request, datosSesion.getTranslation()._("Se ha producido un error al cargar el fichero"));
			return getControlador().getAccion("ejecutar").ejecutar(request);
		}
		catch (ContadorException e) {
			log.error("Se ha producido un error al actualizar las versiones de los artículos", e);
			setMensajeError(request, datosSesion.getTranslation()._("Se ha producido un error al actualizar las versiones de los artículos"));
			return getControlador().getAccion("ejecutar").ejecutar(request);
		}
		catch (Exception e) {
			log.error("Se ha producido un error ejecutando la acción Ejecutar del servlet " + getControlador().getServletName() + ": " + e.getMessage(), e);
			setError(request, e);
		}
		return ERROR_PAGE;

	}
}
