package com.comerzzia.bimbaylola.pos.gui.mantenimientos.fidelizados.resumen;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.comerzzia.ByL.backoffice.persistencia.fidelizacion.fidelizados.FidelizadosBean;
import com.comerzzia.ByL.backoffice.rest.client.fidelizados.ByLFidelizadosRest;
import com.comerzzia.bimbaylola.pos.gui.mantenimientos.fidelizados.ByLFidelizadoController;
import com.comerzzia.bimbaylola.pos.gui.mantenimientos.fidelizados.ByLVentaGui;
import com.comerzzia.bimbaylola.pos.gui.ventas.tickets.ByLTicketManager;
import com.comerzzia.bimbaylola.pos.persistence.ticket.articulos.agregarnotainformativa.AvisoInformativoBean;
import com.comerzzia.bimbaylola.pos.services.articulos.articulosNoAptos.ArticuloNoAptoException;
import com.comerzzia.bimbaylola.pos.services.ticket.ByLLineaTicket;
import com.comerzzia.bimbaylola.pos.services.ticket.ByLTicketVentaAbono;
import com.comerzzia.bimbaylola.pos.services.ticket.articulos.agregarnotainformativa.ByLAgregarNotaInformativaService;
import com.comerzzia.model.fidelizacion.fidelizados.FidelizadoBean;
import com.comerzzia.model.fidelizacion.fidelizados.colectivos.ColectivosFidelizadoBean;
import com.comerzzia.model.fidelizacion.fidelizados.contactos.TiposContactoFidelizadoBean;
import com.comerzzia.model.fidelizacion.tarjetas.TarjetaBean;
import com.comerzzia.model.ventas.albaranes.articulos.ArticuloAlbaranVentaBean;
import com.comerzzia.pos.core.gui.BackgroundTask;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.core.gui.tablas.celdas.CellFactoryBuilder;
import com.comerzzia.pos.gui.mantenimientos.fidelizados.ColectivoGui;
import com.comerzzia.pos.gui.mantenimientos.fidelizados.resumen.PaneResumenFidelizadoController;
import com.comerzzia.pos.gui.ventas.tickets.TicketManager;
import com.comerzzia.pos.services.core.documentos.DocumentoException;
import com.comerzzia.pos.services.core.documentos.Documentos;
import com.comerzzia.pos.services.core.sesion.Sesion;
import com.comerzzia.pos.services.core.variables.VariablesServices;
import com.comerzzia.pos.services.devices.DeviceException;
import com.comerzzia.pos.servicios.impresion.ServicioImpresion;
import com.comerzzia.pos.util.config.SpringContext;
import com.comerzzia.pos.util.format.FormatUtil;
import com.comerzzia.pos.util.i18n.I18N;
import com.comerzzia.rest.client.colectivos.ColectivosRequestRest;
import com.comerzzia.rest.client.fidelizados.ConsultarFidelizadoRequestRest;
import com.comerzzia.rest.client.fidelizados.FidelizadosRest;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

@Component
@Primary
public class ByLPaneResumenFidelizadoController extends PaneResumenFidelizadoController{
	
	protected static final Logger log = Logger.getLogger(ByLPaneResumenFidelizadoController.class);
	private static final String URL_VISOR_DOCUMENTOS = "TPV.URL_VISOR_DOCUMENTOS";
	public static final String PLANTILLA_NOTA_INFORMATIVA = "nota_informativa";
	
	@Autowired
	private Sesion sesion;
	@Autowired
	private Documentos documentos;
	@Autowired
	private VariablesServices variablesServices;
	
	@FXML
	protected TableColumn<ByLVentaGui, String> tcTicketResumen;

	
	protected FidelizadosBean fidelizado;

	
	public void selected(){
		limpiarFormulario();
		
		FidelizadosBean newFidelizado =	(FidelizadosBean) getTabParentController().getFidelizado();
		fidelizado = newFidelizado;
		/* Cargamos los datos principales de la pantalla */
		cargarColectivosTarjetas(fidelizado);
		cargarResumen(fidelizado);
		
		tableVentas.getSelectionModel().select(0);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void editarCampos(){
		super.editarCampos();
		tcTicketResumen.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tableVentas", "tcTicketResumen",
				null, CellFactoryBuilder.ESTILO_ALINEACION_IZQ));
		tcTicketResumen.setCellValueFactory(new PropertyValueFactory<ByLVentaGui, String>("codTicket"));
	}
	
	protected void cargarResumen(FidelizadoBean fidelizado){
		log.debug("cargarResumen() Iniciamos la carga de datos del Panel de Resumen...");		
		tfCodigo.setText(fidelizado.getCodFidelizado() != null ? fidelizado.getCodFidelizado() : "");
		tfNombre.setText(fidelizado.getNombreCompleto());
		tfTipoDocumento.setText(fidelizado.getCodTipoIden());
		dpFechaNacimiento.setValue(fidelizado.getFechaNacimiento() != null ? fidelizado.getFechaNacimiento() : new Date());		
		TiposContactoFidelizadoBean email = fidelizado.getTipoContacto(ByLFidelizadoController.EMAIL);
		TiposContactoFidelizadoBean movil = fidelizado.getTipoContacto(ByLFidelizadoController.MOVIL);
		if(!isPuedeVerDatosSensibles()){
			if(fidelizado.getDocumento() != null){
				ocultaDatoSensible(tfDocumento, fidelizado.getDocumento());
			}
			if(email != null && email.getValor() != null){
				ocultaDatoSensible(tfEmail, email.getValor());
			}
			if(movil != null && movil.getValor() != null){
				ocultaDatoSensible(tfMovil, movil.getValor());
			}
			
		}else{
			tfDocumento.setText(fidelizado.getDocumento());
			if(email != null && !email.isEstadoBorrado()){
				tfEmail.setText(email.getValor());
			}else{
				tfEmail.setText("");
			}
			
			if(movil != null && !movil.isEstadoBorrado()){
				tfMovil.setText(movil.getValor());
			}else{
				tfMovil.setText("");
			}
		}
		if(!fidelizado.getColectivos().isEmpty()){
			colectivos = FXCollections.observableArrayList();
			for(ColectivosFidelizadoBean colectivo : fidelizado.getColectivos()){
				ColectivoGui colectivoGui = new ColectivoGui(colectivo);
				colectivos.add(colectivoGui);
			}
			tableColectivos.setItems(colectivos);
		}
		else{
			if(colectivos != null){
				colectivos.clear();
				tableColectivos.setItems(colectivos);
			}
		}
		tableVentas.getSelectionModel().select(0); //TODO
		log.debug("cargarResumen() Finalizada la carga de datos del Panel de Resumen");	
	}

	protected void cargarColectivosTarjetas(final FidelizadoBean fidelizadoConsulta){
		String apiKey = variablesService.getVariableAsString(VariablesServices.WEBSERVICES_APIKEY);
		String uidActividad = sesion.getAplicacion().getUidActividad();
		ConsultarFidelizadoRequestRest consulta = new ConsultarFidelizadoRequestRest(apiKey, uidActividad);
		consulta.setIdFidelizado(String.valueOf(fidelizadoConsulta.getIdFidelizado()));
		
		Date fechaHoy = new Date();
		Calendar calDesde = Calendar.getInstance();
		calDesde.set(Calendar.YEAR, calDesde.get(Calendar.YEAR)-2);
		Date fechaDesde = calDesde.getTime();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		consulta.setFechaDesde(format.format(fechaDesde));
		consulta.setFechaHasta(format.format(fechaHoy));
		
		ColectivosRequestRest consultaColectivos = new ColectivosRequestRest(apiKey, uidActividad);
		consultaColectivos.setPrivado("S");
		
		/* Realizamos la llamada para traer las últimas compras realizadas por este Fidelizado */
		new ConsultarDatosResumenFidelizadoTask(consulta, consultaColectivos, fidelizadoConsulta).start();
	}
	
	/**
	 * Clase Task para crear un Fidelizado.
	 * @param fidelizadoRequest : Request con los datos del Fidelizado.
	 */
	public class ConsultarDatosResumenFidelizadoTask extends BackgroundTask<Object>{
		
		FidelizadoBean fidelizadoConsulta;
		ColectivosRequestRest consultaColectivos;
		ConsultarFidelizadoRequestRest consultarFidelizado;

		public ConsultarDatosResumenFidelizadoTask(ConsultarFidelizadoRequestRest consultarFidelizado,
				ColectivosRequestRest consultaColectivos, FidelizadoBean fidelizadoConsulta){
			super();
			this.fidelizadoConsulta = fidelizadoConsulta;
			this.consultaColectivos = consultaColectivos;
			this.consultarFidelizado = consultarFidelizado;
		}
		@Override
		protected Object call() throws Exception{
			/* ================== HISTÓRICO DE VENTAS ================== */
			List<ArticuloAlbaranVentaBean> result = ByLFidelizadosRest.getComprasFidelizado(consultarFidelizado).getCompras();
			ventas = FXCollections.observableArrayList();
			for(ArticuloAlbaranVentaBean art : result){
				ByLVentaGui artGui = new ByLVentaGui(art);
				ventas.add(artGui);
			}
			tableVentas.setItems(ventas);

			/* ================== TARJETAS ================== */
			List<TarjetaBean> tarjetas = FidelizadosRest.getTarjetasFidelizado(consultarFidelizado);
			Set<Long> cuentas = new HashSet<Long>();
			List<TarjetaBean> tarjetasFidelizado = new ArrayList<TarjetaBean>();
			Double saldoAcumulado = Double.valueOf(0);
			if(result != null){
				for(TarjetaBean tarjeta : tarjetas){
					if(!tarjeta.isPermitePago()){
						/* Comprobamos que las tarjetas sean de cuentas independientes */
						if(!cuentas.contains(tarjeta.getIdCuentaTarjeta())){
							saldoAcumulado = saldoAcumulado + tarjeta.getSaldo();
						}
						cuentas.add(tarjeta.getIdCuentaTarjeta());
					}
					if(tarjeta.isPermiteVincular() && !tarjeta.isPermitePago() && tarjeta.isActivo()){
						tarjetasFidelizado.add(tarjeta);
					}
				}
				tfSaldo.setText(FormatUtil.getInstance().formateaNumero(new BigDecimal(saldoAcumulado), 2));
				if(StringUtils.isNotBlank(getTabParentController().getNumeroTarjetaFidelizado())){
					tfNumeroTarjeta.setText(getTabParentController().getNumeroTarjetaFidelizado());
				}
				else{
					tfNumeroTarjeta.setText("");
				}

			}
			return null;
		}
		@Override
		public void succeeded(){
			cargarResumen(fidelizadoConsulta);
			super.succeeded();
		}
		@Override
		public void failed(){
			super.failed();
			Exception e = (Exception) getException();
			log.error("ConsultarUltimasVentasFidelizadoTask/failed() - " + e.getMessage(), e);
			VentanaDialogoComponent.crearVentanaInfo(I18N.getTexto(e.getMessage()), getStage());
		}
	}
	
	public void accionImprimirLinea() {
		log.debug("accionImprimirLinea() - Inicio de recuperación de ticket a través de la linea de venta seleccionada");
		ByLVentaGui lineaSeleccionada = (ByLVentaGui) tableVentas.getSelectionModel().getSelectedItem();
		if (lineaSeleccionada != null) {
			String idTicket = null;
			try {
				String codTicket = lineaSeleccionada.getCodTicket();
				String infoTicket = codTicket.split("/")[0];
				idTicket = codTicket.split("/")[1];
				String codTienda = lineaSeleccionada.getCodTienda();
				String codCaja = infoTicket.substring(infoTicket.length() - 6, infoTicket.length() - 4);

				log.debug("accionImprimirLinea() - codTicket [" + codTicket + "]");
				log.debug("accionImprimirLinea() - idTicket [" + idTicket + "]");
				log.debug("accionImprimirLinea() - codTienda [" + codTienda + "]");
				log.debug("accionImprimirLinea() - codCaja [" + codCaja + "]");

				Long idTipoDoc = documentos.getDocumento(infoTicket.substring(0, 2)).getIdTipoDocumento();
				TicketManager ticketManager = SpringContext.getBean(TicketManager.class);
				ByLTicketVentaAbono ticketRecuperado = ((ByLTicketManager) ticketManager).recuperarTicketFidelizadoByL(idTicket, codTienda, codCaja, idTipoDoc);
				if(ticketRecuperado != null) {
					imprimirTicketFidelizado(ticketRecuperado);	
				} else {
					VentanaDialogoComponent.crearVentanaError(String.format(I18N.getTexto("El ticket %s no se ha encontrado"), codTicket), getStage());
				}
				
			}
			catch (DocumentoException e) {
				VentanaDialogoComponent.crearVentanaError(getStage(), String.format(I18N.getTexto("El documento %s no se ha encontrado"), idTicket.substring(0, 3)), e);
			}
			catch (ArticuloNoAptoException e) {
				log.error("accionImprimirLinea() - " + e.getClass().getName() + " - " + e.getLocalizedMessage(), e);
			}
			catch(Exception e) {
				VentanaDialogoComponent.crearVentanaError(getStage(), String.format(I18N.getTexto("Ha ocurrido un error al procesar el ticket")), e);
			}
		}
	}

	private void imprimirTicketFidelizado(ByLTicketVentaAbono ticket) {
		log.debug("imprimirTicketFidelizado() - Inicio de impresión de ticket");
		String plantilla = ticket.getCabecera().getFormatoImpresion();
		Map<String, Object> mapaParametrosManager = new HashMap<String, Object>();
		mapaParametrosManager.put("ticket", ticket);
		mapaParametrosManager.put("urlQR", variablesServices.getVariableAsString(URL_VISOR_DOCUMENTOS));
		mapaParametrosManager.put("esCopia", true);

		try {
			if (ticket.getCabecera().getCodTipoDocumento().equals(sesion.getAplicacion().getDocumentos().getDocumento(Documentos.FACTURA_COMPLETA).getCodtipodocumento())) {
				mapaParametrosManager.put("empresa", sesion.getAplicacion().getEmpresa());
			}
			ServicioImpresion.imprimir(plantilla, mapaParametrosManager);

			for (int cont = 0; cont < ticket.getLineas().size(); cont++) {
				ByLLineaTicket linea = (ByLLineaTicket) ticket.getLineas().get(cont);
				if (linea.getNotaInformativa() != null) {
					AvisoInformativoBean aviso = ByLAgregarNotaInformativaService.get().consultarAvisoInformativo(sesion.getAplicacion().getUidActividad(),
					        sesion.getAplicacion().getTienda().getCliente().getCodpais(), linea.getNotaInformativa().getCodigo());
					if (aviso.getDocuIndepe().equals("S")) {
						Long numeroCopias = 1L;
						try {
							numeroCopias = aviso.getCopias();
						}
						catch (Exception e) {
							log.debug("imprimirTicketFidelizado() - " + e.getMessage());
						}

						mapaParametrosManager.put("linea", linea);
						for (int i = 0; i < numeroCopias; i++) {
							ServicioImpresion.imprimir(PLANTILLA_NOTA_INFORMATIVA, mapaParametrosManager);
						}
					}
				}
			}
		}
		catch (DocumentoException e) {
			log.error("imprimirTicketFidelizado() - Documento no encontrado");
		}
		catch (DeviceException e) {
			log.error("imprimirTicketFidelizado() - Error con la impresora");
		}
		catch (ArticuloNoAptoException e1) {
			log.error("imprimirTicketFidelizado() - Articulo no encontrado");
		}
	}

}