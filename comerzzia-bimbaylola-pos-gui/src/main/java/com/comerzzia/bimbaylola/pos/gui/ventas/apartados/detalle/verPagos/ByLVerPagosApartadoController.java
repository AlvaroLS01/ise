package com.comerzzia.bimbaylola.pos.gui.ventas.apartados.detalle.verPagos;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.comerzzia.pos.core.gui.InitializeGuiException;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.core.gui.tablas.celdas.CellFactoryBuilder;
import com.comerzzia.pos.gui.ventas.apartados.ApartadosManager;
import com.comerzzia.pos.gui.ventas.apartados.detalle.verPagos.VerPagosApartadoController;
import com.comerzzia.pos.gui.ventas.apartados.detalle.verPagos.VerPagosApartadoGui;
import com.comerzzia.pos.persistence.apartados.pagos.ApartadosPagoBean;
import com.comerzzia.pos.persistence.cajas.movimientos.CajaMovimientoBean;
import com.comerzzia.pos.services.cajas.CajasService;
import com.comerzzia.pos.services.cajas.CajasServiceException;
import com.comerzzia.pos.services.core.sesion.Sesion;
import com.comerzzia.pos.services.devices.DeviceException;
import com.comerzzia.pos.services.mediospagos.MediosPagosService;
import com.comerzzia.pos.servicios.impresion.ServicioImpresion;
import com.comerzzia.pos.util.bigdecimal.BigDecimalUtil;
import com.comerzzia.pos.util.format.FormatUtil;
import com.comerzzia.pos.util.i18n.I18N;

@Primary
@Component
public class ByLVerPagosApartadoController extends VerPagosApartadoController{

	ObservableList<VerPagosApartadoGui> pagos;

	ApartadosManager apartadosManager;

	@Autowired
	private Sesion sesion;
	@Autowired
	private MediosPagosService mediosPagosService;
	@Autowired
	private CajasService cajasService;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL url, ResourceBundle rb){
		tbPagos.setPlaceholder(new Label(""));
		pagos = FXCollections.observableList(new ArrayList<VerPagosApartadoGui>());

		tcFecha.setCellFactory(CellFactoryBuilder.createCellRendererCeldaFechaHora("tbPagos", "tcCodDoc", null, CellFactoryBuilder.ESTILO_ALINEACION_IZQ));
		tcImporte.setCellFactory(CellFactoryBuilder.createCellRendererCeldaImporte("tbPagos", "tcDesDoc", CellFactoryBuilder.ESTILO_ALINEACION_DCHA));
		tcMedioPago.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbPagos", "tcMedioPago", 2, CellFactoryBuilder.ESTILO_ALINEACION_IZQ));

		tcFecha.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<VerPagosApartadoGui, Date>, ObservableValue<Date>>(){

			@Override
			public ObservableValue<Date> call(TableColumn.CellDataFeatures<VerPagosApartadoGui, Date> cdf){
				return cdf.getValue().getFechaPagoProperty();
			}
		});
		tcImporte.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<VerPagosApartadoGui, BigDecimal>, ObservableValue<BigDecimal>>(){

			@Override
			public ObservableValue<BigDecimal> call(TableColumn.CellDataFeatures<VerPagosApartadoGui, BigDecimal> cdf){
				return cdf.getValue().getImporteProperty();
			}
		});
		tcMedioPago.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<VerPagosApartadoGui, String>, ObservableValue<String>>(){

			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<VerPagosApartadoGui, String> cdf){
				return cdf.getValue().getMedioPagoProperty();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initializeForm() throws InitializeGuiException{
		List<ApartadosPagoBean> pagosApartado = (List<ApartadosPagoBean>) getDatos().get(PARAMETRO_PAGOS);
		apartadosManager = (ApartadosManager) getDatos().get(PARAMETRO_APARTADO_MGR);

		pagos.clear();
		try{
			CajaMovimientoBean movimientoBean;
			for(ApartadosPagoBean pago : pagosApartado){
				movimientoBean = cajasService.consultarMovimientoApartado(pago.getUidDiarioCaja(), pago.getLinea(), sesion.getAplicacion().getUidActividad());
				movimientoBean.setDesMedioPago(mediosPagosService.getMedioPago(movimientoBean.getCodMedioPago()).getDesMedioPago());

				BigDecimal total = movimientoBean.getCargo().subtract(movimientoBean.getAbono());
				movimientoBean.setCargo(total);

				pagos.add(new ByLVerPagosApartadoGui(movimientoBean));
			}
		}
		catch(CajasServiceException e){
			VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("Error consultando los pagos del apartado"), this.getStage());
		}
		tbPagos.setItems(pagos);
	}

	public void accionAceptar(){
		log.trace("accionAceptar()");

		tbPagos.requestFocus();

		Map<String, Object> mapaParametros = new HashMap<String, Object>();
		mapaParametros.put("apartado", apartadosManager.getTicketApartado().getCabecera());
		
		BigDecimal total = BigDecimal.ZERO;
		if(BigDecimalUtil.isIgualACero(tbPagos.getSelectionModel().getSelectedItem().getMovimiento().getAbono())){
			total = tbPagos.getSelectionModel().getSelectedItem().getMovimiento().getCargo();
		}
		else{
			total = tbPagos.getSelectionModel().getSelectedItem().getMovimiento().getAbono().negate();
		}
		mapaParametros.put("importe", FormatUtil.getInstance().formateaImporte(total));
		mapaParametros.put("fecha", FormatUtil.getInstance().formateaFechaHora(new Date()));
		mapaParametros.put("empresa", sesion.getAplicacion().getEmpresa());
		mapaParametros.put("cajero", sesion.getSesionUsuario().getUsuario().getDesusuario());
		mapaParametros.put("pago", tbPagos.getSelectionModel().getSelectedItem().getMovimiento());

		try{
			ServicioImpresion.imprimir("informe_apartado", mapaParametros);
		}
		catch(DeviceException e){
			log.error("Error en la reimpresión del pago", e);
			VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("Se produjo un error en la impresión."), getStage());
		}
	}

}
