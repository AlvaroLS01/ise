package com.comerzzia.bricodepot.backoffice.web.fidelizacion.fidelizados.acciones;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.comerzzia.bricodepot.backoffice.services.fidelizacion.fidelizados.BricodepotServicioFidelizadosImpl;
import com.comerzzia.core.omnichannel.engine.model.fidelizacion.contactos.FidelizadoContactoBean;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.model.fidelizacion.fidelizados.FidelizadoBean;
import com.comerzzia.model.fidelizacion.fidelizados.contactos.TiposContactoFidelizadoBean;
import com.comerzzia.servicios.fidelizacion.fidelizados.FidelizadoNotFoundException;
import com.comerzzia.servicios.fidelizacion.fidelizados.ServicioFidelizadosImpl;
import com.comerzzia.web.base.WebKeys;
import com.comerzzia.web.fidelizacion.fidelizados.acciones.LeerFormularioAccion;
import com.comerzzia.web.fidelizacion.fidelizados.ui.FormularioFidelizadoBean;

import org.apache.log4j.Logger;

/*
 * BRICO-459
 */
public class BricodepotLeerFormularioAccion extends LeerFormularioAccion {

    protected static Logger log = Logger.getLogger(BricodepotLeerFormularioAccion.class);

    @Override
    protected void leerFormularioRegistro(FormularioFidelizadoBean formulario, HttpServletRequest request) throws Exception { // BRICO-459
        super.leerFormularioRegistro(formulario, request);

        HttpSession sesion = request.getSession();
        DatosSesionBean datosSesion = (DatosSesionBean) sesion.getAttribute(WebKeys.DATOS_SESION);

        if (formulario.getPestañaActiva() == 5 && formulario.getFormularioPestañaActiva().isModoVisualizacionFicha()) {
            log.debug("leerFormularioRegistro() - Leyendo formulario pestaña Contactos");
            evitaDuplicacionEmailCaseInsensitive(formulario, datosSesion, request);
        }

    }

    @SuppressWarnings("deprecation")
    private void evitaDuplicacionEmailCaseInsensitive(FormularioFidelizadoBean formulario, DatosSesionBean datosSesion, HttpServletRequest request) { // BRICO-459

        try {
            FidelizadoBean registroFidelizado = formulario.getRegistroActivo();
            if (registroFidelizado == null) {
                log.warn("evitaDuplicacionEmailCaseInsensitive() - El registro del fidelizado es nulo o vacío");
                return;
            }

            TiposContactoFidelizadoBean tipoContactoEmail = registroFidelizado.getTipoContacto("EMAIL");
            if (tipoContactoEmail == null) {
                log.warn("evitaDuplicacionEmailCaseInsensitive() - El tipo de contacto EMAIL es nulo o vacío para el registro del fidelizado");
                return;
            }

            String emailIntroducido = tipoContactoEmail.getValor();
            if (emailIntroducido == null) {
                log.warn("evitaDuplicacionEmailCaseInsensitive() - El valor del email introducido es nulo o vacío");
                return;
            }

            FidelizadoBean fidelizadoConsultado = ServicioFidelizadosImpl.get().consultar(registroFidelizado.getIdFidelizado(), datosSesion);
            if (fidelizadoConsultado == null) {
                log.warn("evitaDuplicacionEmailCaseInsensitive() - No se pudo consultar el fidelizado por el ID proporcionado");
                return;
            }

            TiposContactoFidelizadoBean tipoContactoEmailConsultado = fidelizadoConsultado.getTipoContacto("EMAIL");
            if (tipoContactoEmailConsultado == null) {
                log.warn("evitaDuplicacionEmailCaseInsensitive() - El tipo de contacto EMAIL es nulo o vacío para el fidelizado consultado");
                return;
            }

            String emailFidelizado = tipoContactoEmailConsultado.getValor();
            if (emailFidelizado == null) {
                log.warn("evitaDuplicacionEmailCaseInsensitive() - El valor del email del fidelizado consultado es nulo o vacío");
                return;
            }

            log.debug("evitaDuplicacionEmailCaseInsensitive() - Comprobando que el email introducido no existe para otro fidelizado.");
            log.debug("evitaDuplicacionEmailCaseInsensitive() - Email fidelizado actual: " + emailFidelizado + "; Email introducido: " + emailIntroducido);

            boolean mismoEmailFidelizadoActual = emailIntroducido.equalsIgnoreCase(emailFidelizado);
            if (mismoEmailFidelizadoActual) {
                return;
            }

            List<FidelizadoContactoBean> fidelizadosPorEmailCaseInSensitive = ((BricodepotServicioFidelizadosImpl) ServicioFidelizadosImpl.get()).buscaFidelizadosPorEmailCaseInSensitive(datosSesion, emailIntroducido);

            boolean existeFidelizadoMismoEmailCaseInsensitive = !fidelizadosPorEmailCaseInSensitive.isEmpty();
            if (existeFidelizadoMismoEmailCaseInsensitive) {

                String idFidelizadoMismoEmail = String.valueOf(fidelizadosPorEmailCaseInSensitive.get(0).getIdFidelizado());
                String msgError = datosSesion.getTranslation().getText("Ya existe fidelizado con el email:{0}. Id del fidelizado:{1}", emailIntroducido, idFidelizadoMismoEmail);
                log.error("evitaDuplicacionEmailCaseInsensitive() - " + msgError);
                setMensajeError(request, msgError);

                setEmailOriginal(registroFidelizado, emailFidelizado);
            }
        } catch (FidelizadoNotFoundException e) {
            log.error("evitaDuplicacionEmailCaseInsensitive() - Error buscando fidelizado: " + e.getMessage(), e);
        }

    }

    private void setEmailOriginal(FidelizadoBean registroFidelizado, String emailFidelizado) {
        log.debug("setEmailOriginal() - Restableciendo el email original del fidelizado para evitar duplicaciones");

        List<TiposContactoFidelizadoBean> tiposContacto = registroFidelizado.getTiposContacto();
        for (TiposContactoFidelizadoBean tipoContacto : tiposContacto) {
            if (tipoContacto.getCodTipoCon().equals("EMAIL")) {
                tipoContacto.setValor(emailFidelizado);
            }
        }
    }
}
