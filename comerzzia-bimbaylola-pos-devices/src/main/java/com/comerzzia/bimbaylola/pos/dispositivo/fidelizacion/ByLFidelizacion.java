package com.comerzzia.bimbaylola.pos.dispositivo.fidelizacion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.stage.Stage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.comerzzia.ByL.backoffice.rest.client.fidelizados.ByLFidelizadosRest;
import com.comerzzia.ByL.backoffice.rest.client.fidelizados.ByLResponseGetFidelizadoRest;
import com.comerzzia.bimbaylola.pos.persistence.fidelizacion.ByLFidelizacionBean;
import com.comerzzia.pos.core.dispositivos.dispositivo.DispositivoCallback;
import com.comerzzia.pos.core.dispositivos.dispositivo.fidelizacion.ConsultaTarjetaFidelizadoException;
import com.comerzzia.pos.core.dispositivos.dispositivo.fidelizacion.IFidelizacion;
import com.comerzzia.pos.core.gui.BackgroundTask;
import com.comerzzia.pos.core.gui.POSApplication;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.dispositivo.fidelizacion.Fidelizacion;
import com.comerzzia.pos.dispositivo.fidelizacion.busqueda.BusquedaFidelizadoController;
import com.comerzzia.pos.dispositivo.fidelizacion.busqueda.BusquedaFidelizadoView;
import com.comerzzia.pos.persistence.fidelizacion.FidelizacionBean;
import com.comerzzia.pos.services.core.sesion.Sesion;
import com.comerzzia.pos.services.core.variables.VariablesServices;
import com.comerzzia.pos.util.config.SpringContext;
import com.comerzzia.pos.util.i18n.I18N;
import com.comerzzia.rest.client.exceptions.RestException;
import com.comerzzia.rest.client.exceptions.RestHttpException;
import com.comerzzia.rest.client.fidelizados.ConsultarFidelizadoRequestRest;
import com.comerzzia.rest.client.fidelizados.ResponseGetFidelizadoColectivoRest;
import com.comerzzia.rest.client.fidelizados.ResponseGetFidelizadoContactosRest;
import com.comerzzia.rest.client.fidelizados.ResponseGetFidelizadoEtiquetasRest;

public class ByLFidelizacion extends Fidelizacion implements IFidelizacion{

	private static final Logger log = Logger.getLogger(ByLFidelizacion.class.getName());

	@Autowired
	private VariablesServices variablesServices;
	protected ByLFidelizacionTask currentTask;

	private static final String FIDELIZADO_GENERICO = "FIDELIZACION.FIDELIZADO_GENERICO";

	/** Consulta la tarjeta al rest a partir del número de tarjeta.
	 * @param numTarjRegalo
	 * @param uidActividad
	 * @return FidelizacionBean
	 * @throws RestHttpException
	 * @throws RestException 
	 */
	public FidelizacionBean consultarTarjetaFidelizado(String numTarjRegalo, String uidActividad) throws ConsultaTarjetaFidelizadoException{
		try{
			ByLFidelizacionBean tarjetaFidelizacion = null;
			ConsultarFidelizadoRequestRest paramConsulta = new ConsultarFidelizadoRequestRest(wsApiKey, uidActividad);

			paramConsulta.setNumeroTarjeta(numTarjRegalo);

			ByLResponseGetFidelizadoRest result = ByLFidelizadosRest.getFidelizado(paramConsulta);
			tarjetaFidelizacion = new ByLFidelizacionBean();
			tarjetaFidelizacion.setNumTarjetaFidelizado(result.getNumeroTarjeta());
			tarjetaFidelizacion.setBaja(result.getBaja().equals("S"));
			tarjetaFidelizacion.setActiva(result.getActiva().equals("S"));
			tarjetaFidelizacion.setSaldo(BigDecimal.valueOf(result.getSaldo()));
			tarjetaFidelizacion.setSaldoProvisional(BigDecimal.valueOf(result.getSaldoProvisional()));
			if(result.getTiposContactos() != null && !result.getTiposContactos().isEmpty()){
				for(ResponseGetFidelizadoContactosRest contacto : result.getTiposContactos()){
					if(contacto.getDescripcion().equals("E-mail")){
						tarjetaFidelizacion.setEmail(contacto.getValor());
					}
					else if(contacto.getDescripcion().equals("Móvil")){
						tarjetaFidelizacion.setTelefono(contacto.getValor());
					}
				}
			}

			List<String> codColectivos = new ArrayList<>();
			if(result.getColectivos() != null && !result.getColectivos().isEmpty()){
				for(ResponseGetFidelizadoColectivoRest responseGetFidelizadoColectivoRest : result.getColectivos()){
					codColectivos.add(responseGetFidelizadoColectivoRest.getCodigo());
				}
			}
			tarjetaFidelizacion.setCodColectivos(codColectivos);
			List<String> uidEtiquetas = new ArrayList<String>();
			if(result.getEtiquetas() != null && !result.getEtiquetas().isEmpty()){
				for(ResponseGetFidelizadoEtiquetasRest responseGetFidelizadoEtiquetaRest : result.getEtiquetas()){
					uidEtiquetas.add(responseGetFidelizadoEtiquetaRest.getUidEtiqueta());
				}
			}
			tarjetaFidelizacion.setUidEtiquetas(uidEtiquetas);
			tarjetaFidelizacion.setNombre(result.getNombre());
			tarjetaFidelizacion.setApellido(result.getApellidos());
			tarjetaFidelizacion.setCodTipoIden(result.getCodTipoIden());
			tarjetaFidelizacion.setDocumento(result.getDocumento());
			tarjetaFidelizacion.setCodPais(result.getCodPais());
			tarjetaFidelizacion.setDesPais(result.getDesPais());
			/* Rescatamos también el ID del Fidelizado */
			tarjetaFidelizacion.setIdFidelizado(result.getIdFidelizado());

			/* Cargamos la primera dirección que venga en el listado */
			if(result.getDirecciones() != null && !result.getDirecciones().isEmpty()){
				tarjetaFidelizacion.setCp(result.getDirecciones().get(0).getCp());
				tarjetaFidelizacion.setDomicilio(result.getDirecciones().get(0).getDomicilio());
				tarjetaFidelizacion.setLocalidad(result.getDirecciones().get(0).getLocalidad());
				tarjetaFidelizacion.setPoblacion(result.getDirecciones().get(0).getPoblacion());
				tarjetaFidelizacion.setProvincia(result.getDirecciones().get(0).getProvincia());
			}
			else{
				tarjetaFidelizacion.setCp(result.getCp());
				tarjetaFidelizacion.setDomicilio(result.getDomicilio());
				tarjetaFidelizacion.setLocalidad(result.getLocalidad());
				tarjetaFidelizacion.setPoblacion(result.getPoblacion());
				tarjetaFidelizacion.setProvincia(result.getProvincia());
			}
			
			/* Cargamos el consentimiento y firma */
			if(result.getConsentimiento() != null){
				tarjetaFidelizacion.setConsentimientoUsodatos(result.getConsentimiento().getConsentimientoUsodatos());
				tarjetaFidelizacion.setConsentimientoRecibenoti(result.getConsentimiento().getConsentimientoRecibenoti());
				tarjetaFidelizacion.setFirma(result.getConsentimiento().getFirma());
			}
			
			return tarjetaFidelizacion;
		}
		catch(RestException | RestHttpException e){
			throw new ConsultaTarjetaFidelizadoException(e);
		}
	}

	/** Consulta la tarjeta al rest a partir del QRCode.
	 * @param numTarjRegalo
	 * @param uidActividad
	 * @return FidelizacionBean
	 * @throws RestHttpException
	 * @throws RestException 
	 */
	public FidelizacionBean consultarTarjetaFidelizadoQRCode(String numTarjRegalo, String uidActividad) throws ConsultaTarjetaFidelizadoException{
		try{
			ByLFidelizacionBean tarjetaFidelizacion = null;
			ConsultarFidelizadoRequestRest paramConsulta = new ConsultarFidelizadoRequestRest(wsApiKey, uidActividad);

			paramConsulta.setNumeroTarjeta(numTarjRegalo);
			Sesion sesion = SpringContext.getBean(Sesion.class);
			/* === BYL-113 GAP-01 Filtrar fidelizados por país === */
			//Se usará idTienda para codPais
			paramConsulta.setIdTienda(sesion.getAplicacion().getTienda().getCliente().getCodpais());
			/* === FIN BYL-113 GAP-01 === */

			ByLResponseGetFidelizadoRest result = ByLFidelizadosRest.getFidelizadoQRCode(paramConsulta);
			tarjetaFidelizacion = new ByLFidelizacionBean();
			tarjetaFidelizacion.setNumTarjetaFidelizado(result.getNumeroTarjeta());
			tarjetaFidelizacion.setBaja(result.getBaja().equals("S"));
			tarjetaFidelizacion.setActiva(result.getActiva().equals("S"));
			tarjetaFidelizacion.setSaldo(BigDecimal.valueOf(result.getSaldo()));
			tarjetaFidelizacion.setSaldoProvisional(BigDecimal.valueOf(result.getSaldoProvisional()));
			if(result.getTiposContactos() != null && !result.getTiposContactos().isEmpty()){
				for(ResponseGetFidelizadoContactosRest contacto : result.getTiposContactos()){
					if(contacto.getDescripcion().equals("E-mail")){
						tarjetaFidelizacion.setEmail(contacto.getValor());
					}
					else if(contacto.getDescripcion().equals("Móvil")){
						tarjetaFidelizacion.setTelefono(contacto.getValor());
					}
				}
			}

			List<String> codColectivos = new ArrayList<>();
			if(result.getColectivos() != null && !result.getColectivos().isEmpty()){
				for(ResponseGetFidelizadoColectivoRest responseGetFidelizadoColectivoRest : result.getColectivos()){
					codColectivos.add(responseGetFidelizadoColectivoRest.getCodigo());
				}
			}
			tarjetaFidelizacion.setCodColectivos(codColectivos);
			List<String> uidEtiquetas = new ArrayList<String>();
			if(result.getEtiquetas() != null && !result.getEtiquetas().isEmpty()){
				for(ResponseGetFidelizadoEtiquetasRest responseGetFidelizadoEtiquetaRest : result.getEtiquetas()){
					uidEtiquetas.add(responseGetFidelizadoEtiquetaRest.getUidEtiqueta());
				}
			}
			tarjetaFidelizacion.setUidEtiquetas(uidEtiquetas);
			tarjetaFidelizacion.setNombre(result.getNombre());
			tarjetaFidelizacion.setApellido(result.getApellidos());
			tarjetaFidelizacion.setCodTipoIden(result.getCodTipoIden());
			tarjetaFidelizacion.setDocumento(result.getDocumento());
			tarjetaFidelizacion.setCodPais(result.getCodPais());
			tarjetaFidelizacion.setDesPais(result.getDesPais());
			/* Rescatamos también el ID del Fidelizado */
			tarjetaFidelizacion.setIdFidelizado(result.getIdFidelizado());

			/* Cargamos la primera dirección que venga en el listado */
			if(result.getDirecciones() != null && !result.getDirecciones().isEmpty()){
				tarjetaFidelizacion.setCp(result.getDirecciones().get(0).getCp());
				tarjetaFidelizacion.setDomicilio(result.getDirecciones().get(0).getDomicilio());
				tarjetaFidelizacion.setLocalidad(result.getDirecciones().get(0).getLocalidad());
				tarjetaFidelizacion.setPoblacion(result.getDirecciones().get(0).getPoblacion());
				tarjetaFidelizacion.setProvincia(result.getDirecciones().get(0).getProvincia());
			}
			else{
				tarjetaFidelizacion.setCp(result.getCp());
				tarjetaFidelizacion.setDomicilio(result.getDomicilio());
				tarjetaFidelizacion.setLocalidad(result.getLocalidad());
				tarjetaFidelizacion.setPoblacion(result.getPoblacion());
				tarjetaFidelizacion.setProvincia(result.getProvincia());
			}
			
			/* Cargamos el consentimiento y firma */
			if(result.getConsentimiento() != null){
				tarjetaFidelizacion.setConsentimientoUsodatos(result.getConsentimiento().getConsentimientoUsodatos());
				tarjetaFidelizacion.setConsentimientoRecibenoti(result.getConsentimiento().getConsentimientoRecibenoti());
				tarjetaFidelizacion.setFirma(result.getConsentimiento().getFirma());
			}
			
			return tarjetaFidelizacion;
		}
		catch(RestException | RestHttpException e){
			throw new ConsultaTarjetaFidelizadoException(e);
		}
	}
	
	@Override
	public void pedirTarjetaFidelizado(Stage stage, DispositivoCallback<FidelizacionBean> callback){
		String numTarjeta = null;
		boolean isPrefijo = true;
		do{
			if(!isPrefijo){
				VentanaDialogoComponent.crearVentanaConfirmacionUnBoton(I18N.getTexto("El número de tarjeta no es válido"), stage);
			}
			numTarjeta = leerNumeroTarjeta(stage);
		}
		while(numTarjeta != null && !numTarjeta.isEmpty() && !(isPrefijo = isPrefijoTarjeta(numTarjeta)));

		variablesServices = SpringContext.getBean(VariablesServices.class);
		String variableFidelizado = variablesServices.getVariableAsString(FIDELIZADO_GENERICO);
		if(numTarjeta != null && variableFidelizado.equals(numTarjeta)){
			FidelizacionBean fidelizado = new ByLFidelizacionBean();
			fidelizado.setNumTarjetaFidelizado(numTarjeta);
			callback.onSuccess(fidelizado);
			return;
		}

		if(numTarjeta != null){
			currentTask = new ByLFidelizacionTask(numTarjeta, callback, stage);
			currentTask.start();
		}
	}

	protected String leerNumeroTarjeta(Stage stage){
		HashMap<String, Object> parametros = new HashMap<>();
		parametros.put(BusquedaFidelizadoController.PARAMETRO_IN_TEXTOCABECERA, I18N.getTexto("Búsqueda de clientes"));
		parametros.put(BusquedaFidelizadoController.PARAMETRO_TIPO_TARJETA, "FIDELIZADO");

		POSApplication posApplication = POSApplication.getInstance();
		posApplication.getMainView().showModalCentered(BusquedaFidelizadoView.class, parametros, stage);
		
		String numTarjeta = (String) parametros.get(BusquedaFidelizadoController.PARAMETRO_NUM_TARJETA);
		return numTarjeta;
	}

	public class ByLFidelizacionTask extends BackgroundTask<FidelizacionBean>{

		private final String numTarjeta;
		private DispositivoCallback<FidelizacionBean> callback;
		private Stage stage;

		public ByLFidelizacionTask(String numTarjeta, DispositivoCallback<FidelizacionBean> callback, Stage stage){
			super(false);
			this.numTarjeta = numTarjeta;
			this.callback = callback;
			this.stage = stage;
		}

		@Override
		protected FidelizacionBean call() throws Exception{
			Sesion sesion = SpringContext.getBean(Sesion.class);
			FidelizacionBean fidelizado = consultarTarjetaFidelizadoQRCode(numTarjeta, sesion.getAplicacion().getUidActividad());

			if(currentThread.isInterrupted()){
				return null;
			}
			return fidelizado;
		}

		@Override
		protected void succeeded(){
			super.succeeded();
			FidelizacionBean tarjeta = getValue();
			if(tarjeta != null){
				callback.onSuccess(tarjeta);
			}
		}

		@Override
		protected void failed(){
			super.failed();
			if(currentThread.isInterrupted()){
				return;
			}
			Throwable e = (Throwable) getException();
			String mensajeError = "Se ha producido un error al realizar la petición Rest - " + e.getMessage();
			log.error(I18N.getTexto(mensajeError), e);
			
			/* Si no se cumple la condición anterior, damos la llamada por correcta */
			FidelizacionBean fidelizacionBean = new ByLFidelizacionBean();
			
			if(e instanceof RestHttpException || e.getCause() instanceof RestHttpException){
				if(!(e instanceof RestHttpException)){
					e = e.getCause();
				}
				if(((RestHttpException) e).getHttpStatus() < 500){
					if(e.getMessage().contains("se propone el Alta")){
						fidelizacionBean.setIdFidelizado(0L);
						fidelizacionBean.setNumTarjetaFidelizado(numTarjeta);
						callback.onSuccess(fidelizacionBean);
						return;
					}
				}
			}
			/* Si hemos alcanzando el servidor pero ha dado un error, marcamos como erróneo */
			VentanaDialogoComponent.crearVentanaError(stage, e.getMessage(), e);
			callback.onFailure(e);
		}

		public void ignorar(){
			currentThread.interrupt();
		}
	}

	@Override
	public boolean isPrefijoTarjeta(String numeroTarjeta){
		return true;
	}

}
