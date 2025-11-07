package com.comerzzia.bimbaylola.pos.dispositivo.impresora.fiscal.polonia;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.comerzzia.bimbaylola.pos.devices.impresoras.fiscal.IFiscalPrinter;
import com.comerzzia.bimbaylola.pos.services.impresorafiscal.InformacionFiscal;
import com.comerzzia.bimbaylola.pos.services.ticket.cabecera.ByLCabeceraTicket;
import com.comerzzia.bimbaylola.pos.util.textos.TextUtils;
import com.comerzzia.pos.core.dispositivos.configuracion.ConfiguracionDispositivo;
import com.comerzzia.pos.core.dispositivos.dispositivo.Dispositivo;
import com.comerzzia.pos.core.dispositivos.dispositivo.DispositivoException;
import com.comerzzia.pos.core.dispositivos.dispositivo.impresora.IPrinter;
import com.comerzzia.pos.persistence.core.documentos.propiedades.PropiedadDocumentoBean;
import com.comerzzia.pos.persistence.core.documentos.tipos.TipoDocumentoBean;
import com.comerzzia.pos.persistence.core.impuestos.porcentajes.PorcentajeImpuestoBean;
import com.comerzzia.pos.services.core.sesion.Sesion;
import com.comerzzia.pos.services.ticket.TicketVentaAbono;
import com.comerzzia.pos.services.ticket.lineas.LineaTicket;
import com.comerzzia.pos.services.ticket.pagos.PagoTicket;
import com.comerzzia.pos.util.bigdecimal.BigDecimalUtil;
import com.comerzzia.pos.util.config.SpringContext;
import com.comerzzia.pos.util.format.FormatUtil;
import com.comerzzia.pos.util.i18n.I18N;

import javafx.stage.Stage;
import jpos.FiscalPrinter;
import jpos.FiscalPrinterConst;
import jpos.JposConst;
import jpos.JposException;
import jpos.events.DirectIOEvent;
import jpos.events.DirectIOListener;
import jpos.events.ErrorEvent;
import jpos.events.ErrorListener;
import jpos.events.OutputCompleteEvent;
import jpos.events.OutputCompleteListener;
import jpos.events.StatusUpdateEvent;
import jpos.events.StatusUpdateListener;
import jpos.util.JposPropertiesConst;
import pl.com.upos.jpos.fp.UposFiscalPrinterConst;
import pl.com.upos.jpos.utils.JposInvoiceInfo;
import pl.com.upos.utils.StringHelper;

@Component
public class PoloniaFiscalPrinter extends Dispositivo implements IPrinter, IFiscalPrinter, ErrorListener, OutputCompleteListener, StatusUpdateListener, DirectIOListener {

	public static final String IMPORTE_MAXIMO_NIP = "IMPORTE_MAXIMO_NIP";
	
	private Logger log = Logger.getLogger(PoloniaFiscalPrinter.class);	
	
	private FiscalPrinter fiscalPrinter;

	private List<String> impresionNoFiscal;
	private String lineaImpresionNormal;

	private List<String> lineasPie;

	private String rtType;

	private Map<Integer, Integer> listaImpuestosEquivalencia;

	protected Sesion sesion;

	@Override
	public void conecta() throws DispositivoException {
		try {
			fiscalPrinter = new FiscalPrinter();

//			System.setProperty(JposPropertiesConst.JPOS_POPULATOR_FILE_PROP_NAME, "C:\\Vistas\\Clientes\\Bimba y Lola\\pos\\comerzzia-bimbaylola-pos-resources\\lib\\ext\\jpos.xml");

			log.debug("conecta() - Fiscal Printer opening");
			fiscalPrinter.open("UPOSFPPL");
			log.debug("conecta() - Fiscal Printer open OK");
			log.debug("conecta() - Fiscal Printer claiming");
			fiscalPrinter.claim(2000);
			log.debug("conecta() - Fiscal Printer claiming OK");
			log.debug("conecta() - Fiscal Printer enabling");
			fiscalPrinter.setDeviceEnabled(true);
			log.debug("conecta() - Fiscal Printer enabling OK");

			fiscalPrinter.addErrorListener(this);
			fiscalPrinter.addOutputCompleteListener(this);
			fiscalPrinter.addStatusUpdateListener(this);
			fiscalPrinter.addDirectIOListener(this);
			fiscalPrinter.setAsyncMode(false);
			fiscalPrinter.setPowerNotify(JposConst.JPOS_PN_ENABLED);

			// if(!fiscalPrinter.getDayOpened()) {
			// Map<Long, Map<String, PorcentajeImpuestoBean>> taxes =
			// SpringContext.getBean(SesionImpuestos.class).getPorcentajes();
			// loadTaxes(taxes);
			//
			// setPrinterData();
			//
			// SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyyhhmm");
			// String date = simpleDateFormat.format(new Date());
			// fiscalPrinter.setDate(date);
			// }
			//
			setFooterLine();

			// getStatus();
			//
			// log.debug("conecta() - Reiniciando impresora");
			// fiscalPrinter.resetPrinter();
			//
			// getStatus();

			cargarListaImpuestosEquivalencia();
		}
		catch (Throwable e) {
			log.error("conecta() - Ha habido un error al arrancar la impresora: " + e.getMessage(), e);
			throw new DispositivoException("Ha habido un error al arrancar la impresora: " + e.getMessage(), e);
		}
	}

	@Override
	public void desconecta() throws DispositivoException {
		try {
			log.debug("conecta() - Fiscal Printer disabeling");
			fiscalPrinter.setDeviceEnabled(false);
			log.debug("conecta() - 2FP - Fiscal Printer releasing");
			fiscalPrinter.release();
			log.debug("conecta() - Fiscal Printer closing");
			fiscalPrinter.close();
		}
		catch (Exception e) {
			log.error("conecta() - Ha habido un error al cerrar la impresora: " + e.getMessage(), e);
			throw new DispositivoException("Ha habido un error al cerrar la impresora: " + e.getMessage(), e);
		}
	}

	public void sendTicket(TicketVentaAbono ticket) throws Throwable {
		log.debug("sendTicket() - Inicio del proceso de venta desde la impresora fiscal de Polonia");

		if (ticket.getCabecera().getCodTipoDocumento().equals("TR")) {
			log.debug("sendTicket() - Se trata de una venta de tarjeta regalo. Se trata de un documento NO FISCAL");
			return;
		}

		try {
			if (fiscalPrinter.getFiscalReceiptStation() != 1) {
				fiscalPrinter.resetPrinter();
			}

			BigDecimal factorConversionCantidades = new BigDecimal(1000);
			BigDecimal factorConversionImportes = new BigDecimal(10000);

			if (!(ticket.getCabecera() instanceof ByLCabeceraTicket)) {
				throw new IllegalArgumentException(I18N.getTexto("No se puede proceder a la impresión fiscal. Contacte con el administrador."));
			}

			ByLCabeceraTicket cabecera = (ByLCabeceraTicket) ticket.getCabecera();

			/* Si es una FO o FT deberemos de pasarle a la impresora fiscal los datos del cliente de la factura */
			if (ticket.getCabecera().getCodTipoDocumento().equals("FO") || ticket.getCabecera().getCodTipoDocumento().equals("FT")) {
				JposInvoiceInfo invoiceInfo = new JposInvoiceInfo();

				invoiceInfo.setInvoiceNr(cabecera.getCodTicket());
				List<String> listaDesc = new ArrayList<String>();
				List<String> listaDomicilio = new ArrayList<String>();
				if (cabecera.getCliente().getDatosFactura() != null) {
					invoiceInfo.addClientInfo(cabecera.getCliente().getDatosFactura().getNombre() != null && cabecera.getCliente().getDatosFactura().getNombre().length() > 43
					        ? cabecera.getCliente().getDatosFactura().getNombre().substring(0, 43)
					        : cabecera.getCliente().getDatosFactura().getNombre());
					invoiceInfo.setClientNipNr(cabecera.getCliente().getDatosFactura().getCif());
					listaDesc.add("Dane o obrotach");
					listaDesc.add("Ludność: " + cabecera.getCliente().getDatosFactura().getPoblacion());
					listaDesc.add("Województwo: " + cabecera.getCliente().getDatosFactura().getProvincia());
					listaDesc.add("Adres: " + cabecera.getCliente().getDatosFactura().getLocalidad());
					listaDesc.add("CP: " + cabecera.getCliente().getDatosFactura().getCp());

					listaDomicilio.add("Siedziba podatkowa");
					if (cabecera.getCliente().getDatosFactura().getDomicilio() != null && cabecera.getCliente().getDatosFactura().getDomicilio().length() > 43) {
						String domicilio1 = cabecera.getCliente().getDatosFactura().getDomicilio().substring(0, 43);
						listaDomicilio.add(domicilio1);
						String domicilio2 = cabecera.getCliente().getDatosFactura().getDomicilio().substring(43, cabecera.getCliente().getDatosFactura().getDomicilio().length());
						listaDomicilio.add(domicilio2);
					}
					else {
						listaDomicilio.add(cabecera.getCliente().getDatosFactura().getDomicilio());
					}
					invoiceInfo.setFreeDescription(1, listaDomicilio);
					invoiceInfo.setFreeDescription(2, listaDesc);
				}
				else {
					invoiceInfo
					        .addClientInfo(cabecera.getCliente().getDesCliente() != null && cabecera.getCliente().getDesCliente().length() > 43 ? cabecera.getCliente().getDesCliente().substring(0, 43)
					                : cabecera.getCliente().getDesCliente());
					invoiceInfo.setClientNipNr(cabecera.getCliente().getCif());
					listaDesc.add("Dane o obrotach");
					listaDesc.add("Ludność: " + cabecera.getCliente().getPoblacion());
					listaDesc.add("Województwo: " + cabecera.getCliente().getProvincia());
					listaDesc.add("Adres: " + cabecera.getCliente().getLocalidad());
					listaDesc.add("CP: " + cabecera.getCliente().getCp());
				
					listaDomicilio.add("Siedziba podatkowa");
					if (cabecera.getCliente().getDatosFactura().getDomicilio() != null && cabecera.getCliente().getDatosFactura().getDomicilio().length() > 43) {
						String domicilio1 = cabecera.getCliente().getDatosFactura().getDomicilio().substring(0, 43);
						listaDomicilio.add(domicilio1);
						String domicilio2 = cabecera.getCliente().getDatosFactura().getDomicilio().substring(43, cabecera.getCliente().getDatosFactura().getDomicilio().length());
						listaDomicilio.add(domicilio2);
					}
					else {
						listaDomicilio.add(cabecera.getCliente().getDatosFactura().getDomicilio());
					}
					invoiceInfo.setFreeDescription(1, listaDomicilio);
					invoiceInfo.setFreeDescription(2, listaDesc);
				}

				fiscalPrinter.setFiscalReceiptType(FiscalPrinterConst.FPTR_RT_SIMPLE_INVOICE);
				fiscalPrinter.directIO(UposFiscalPrinterConst.FPTR_DIO_SET_INVOICE_DATA, null, invoiceInfo);
			}
			else {
				fiscalPrinter.setFiscalReceiptType(FiscalPrinterConst.FPTR_RT_SALES);
			}

			log.debug("sendTicket() - beginFiscalReceipt");
			fiscalPrinter.beginFiscalReceipt(false);

			for (LineaTicket linea : ticket.getLineas()) {
				String descripcion = linea.getDesArticulo();
				descripcion = StringUtils.substring(descripcion, 0, 38);

				String codArt = linea.getCodArticulo();
				String desglose1 = linea.getDesglose1();
				String desglose2 = linea.getDesglose2();

				descripcion = codArt + " " + descripcion + ", " + desglose1 + " " + desglose2;

				Long precio = linea.getImporteTotalConDto().multiply(factorConversionImportes).longValue();
				Integer cantidad = linea.getCantidad().abs().multiply(factorConversionCantidades).intValue();
				Long precioUnitario = linea.getPrecioTotalConDto().multiply(factorConversionImportes).longValue();

				Integer codImpuesto = new Integer(linea.getArticulo().getCodImpuesto());
				Integer codImpuestoPolonia = listaImpuestosEquivalencia.get(codImpuesto);

				String unidad = "";

				log.debug("sendTicket() - Se va a imprimir la línea: " + descripcion + "(" + precio + " X " + cantidad + ")");

				BigDecimal descuento = linea.getDescuento();
				BigDecimal importeDescuento = linea.getImporteTotalConDto().subtract(linea.getImporteTotalSinDto()).abs();
				Long importeDescuentoFormateado = importeDescuento.multiply(factorConversionImportes).longValue();

				log.debug("sendTicket() - RecItem: " + descripcion + ", " + precio + ", " + cantidad + ", " + codImpuestoPolonia + ", " + precioUnitario + ", " + unidad);
				fiscalPrinter.printRecItem(descripcion, precio, cantidad, codImpuestoPolonia, precioUnitario, unidad);

				if (fiscalPrinter.getCapOrderAdjustmentFirst() && BigDecimalUtil.isMayorACero(descuento)) {
					log.debug("sendTicket() - RecItemAdjustment: " + FiscalPrinterConst.FPTR_AT_AMOUNT_DISCOUNT + ", PODSUMY, " + importeDescuentoFormateado + ", " + codImpuestoPolonia);
					fiscalPrinter.printRecItemAdjustment(FiscalPrinterConst.FPTR_AT_AMOUNT_DISCOUNT, "PODSUMY", importeDescuentoFormateado, codImpuestoPolonia);
				}

			}

			long total = ticket.getTotales().getTotal().multiply(factorConversionImportes).longValue();
			for (PagoTicket pago : ticket.getPagos()) {
				long importePago = pago.getImporte().abs().multiply(factorConversionImportes).longValue();
				log.debug("sendTicket() - printRecTotal: " + total + ", " + importePago + ", " + pago.getDesMedioPago());
				fiscalPrinter.printRecTotal(total, importePago, pago.getDesMedioPago());
			}

			if (cabecera.getCodTipoDocumento().equals("FS") && cabecera.getDatosFidelizado() != null && StringUtils.isNotBlank(cabecera.getDatosFidelizado().getDocumento())) {
				sesion = SpringContext.getBean(Sesion.class);
				TipoDocumentoBean tipoDocumento = sesion.getAplicacion().getDocumentos().getDocumento(ticket.getCabecera().getTipoDocumento());
				PropiedadDocumentoBean propiedadClaseProcesamiento = tipoDocumento.getPropiedades().get(IMPORTE_MAXIMO_NIP);
				if (propiedadClaseProcesamiento != null && StringUtils.isNotBlank(propiedadClaseProcesamiento.getValor())) {
					BigDecimal minimoInformaNIP = new BigDecimal(propiedadClaseProcesamiento.getValor());

					if (cabecera.getTotales().getTotal().compareTo(minimoInformaNIP) < 0) {
						fiscalPrinter.printRecMessage("Nabywca: " + cabecera.getDatosFidelizado().getNombre() + " " + cabecera.getDatosFidelizado().getApellido());
						fiscalPrinter.printRecMessage("NIP Nabywcy: " + cabecera.getDatosFidelizado().getDocumento());
					}
				}
			}

			fiscalPrinter.printRecMessage("Kod biletu: " + ticket.getCabecera().getCodTicket());
			
			for (String line : lineasPie) {
				fiscalPrinter.printRecMessage(line);
			}

			printLocator(ticket);

			log.debug("sendTicket() - endFiscalReceipt");
			fiscalPrinter.endFiscalReceipt(false);

			InformacionFiscal informacionFiscal = new InformacionFiscal();
			((ByLCabeceraTicket) ticket.getCabecera()).setInformacionFiscal(informacionFiscal);
			readPrinterId(ticket);

			readReceiptNumber(ticket);
			readReceiptDate(ticket);
			readZReportNumber(ticket);
		}
		catch (Throwable e) {
			log.error("sendTicket() - Error: " + e.getMessage(), e);
			try {
				fiscalPrinter.resetPrinter();
			}
			catch (Throwable ex) {
				log.error("sendTicket() - Error reiniciando impresora: " + e.getMessage(), e);
			}
			throw e;
		}
	}

	private void readZReportNumber(TicketVentaAbono ticket) throws JposException {
		String[] strData = new String[1];
		int[] opt = new int[1];
		fiscalPrinter.getData(FiscalPrinterConst.FPTR_GD_Z_REPORT, opt, strData);
		String zRepNum = strData[0];
		int nextInt = Integer.parseInt(zRepNum) + 1;
		zRepNum = String.format("%04d", nextInt);
		((ByLCabeceraTicket) ticket.getCabecera()).getInformacionFiscal().setzRepNum(zRepNum);
		log.debug("readZReportNumber() - Z Report: " + zRepNum);
	}

	private void readReceiptDate(TicketVentaAbono ticket) throws JposException {
		String[] strData = new String[1];
		fiscalPrinter.getDate(strData);
		String strDate = strData[0].substring(0, 8);
		((ByLCabeceraTicket) ticket.getCabecera()).getInformacionFiscal().setFechaReciboFiscal(strDate);
		log.debug("readReceiptDate() - Date: " + strDate);
	}

	private void readReceiptNumber(TicketVentaAbono ticket) throws JposException {
		String[] strData = new String[1];
		int[] opt = new int[1];
		fiscalPrinter.getData(FiscalPrinterConst.FPTR_GD_FISCAL_REC, opt, strData);
		String recNum = strData[0];
		((ByLCabeceraTicket) ticket.getCabecera()).getInformacionFiscal().setNumReciboFiscal(recNum);
		log.debug("sendTicket() - Rec Num: " + recNum);
	}

	private void readPrinterId(TicketVentaAbono ticket) throws JposException {
		String[] strData = new String[1];
		int[] opt = new int[1];

		fiscalPrinter.getData(FiscalPrinterConst.FPTR_GD_PRINTER_ID, opt, strData);
		log.debug("readPrinterId() - Returned printerId: " + strData[0]);
		String printerIdModel = strData[0].substring(0, 2);
		String printerIdNumber = strData[0].substring(4, 10);
		String printerIdManufacturer = strData[0].substring(2, 4);
		// String printerId = printerIdManufacturer + rtType + printerIdModel + printerIdNumber;
		String printerId = printerIdManufacturer + printerIdModel + printerIdNumber;
		((ByLCabeceraTicket) ticket.getCabecera()).getInformacionFiscal().setPrinterId(printerId);
		log.debug("readPrinterId() - Printer Id: " + printerId);
	}

	// private boolean checkDocumentRefundable(TramasCabeceraTicket cabecera) throws JposException {
	// int[] icmd = {0};
	// StringBuffer sbcmd = new StringBuffer("");
	//
	// icmd[0] = 9205;
	//
	// sbcmd = new StringBuffer("1" + cabecera.getPrinterIdOrigen() + cabecera.getFechaReciboFiscalOrigen() +
	// cabecera.getNumReciboFiscalOrigen() + cabecera.getzRepNumOrigen());
	//
	// fiscalPrinter.directIO(0, icmd, sbcmd);
	//
	// log.debug("checkDocumentRefundable() - DirectIO - icmd[0] = " + icmd[0]);
	// log.debug("checkDocumentRefundable() - DirectIO - sbcmd = " + sbcmd);
	//
	// if (sbcmd.substring(0, 1).equals("0") || sbcmd.substring(0, 1).equals("1")) {
	// log.debug("checkDocumentRefundable() - Document refundable");
	// return true;
	// }
	// else {
	// log.debug("checkDocumentRefundable() - Document NOT refundable");
	// return false;
	// }
	// }

	// private boolean checkDocumentVoidable(TramasCabeceraTicket cabecera) throws JposException {
	// int[] icmd = {0};
	// icmd[0] = 9205;
	//
	// StringBuffer sbcmd = new StringBuffer("1" + cabecera.getPrinterIdOrigen() + cabecera.getFechaReciboFiscalOrigen()
	// + cabecera.getNumReciboFiscalOrigen() + cabecera.getzRepNumOrigen());
	//
	// log.debug("checkDocumentRefundable() - DirectIO - icmd[0] = " + icmd[0]);
	// log.debug("checkDocumentRefundable() - DirectIO - sbcmd = " + sbcmd);
	//
	// fiscalPrinter.directIO(0, icmd, sbcmd);
	//
	// log.debug("checkDocumentRefundable() - DirectIO - Result = " + sbcmd);
	//
	// if (sbcmd.substring(0, 1).equals("0") || sbcmd.substring(0, 1).equals("1")) {
	// log.debug("checkDocumentRefundable() - Document voidable");
	// return true;
	// }
	// else {
	// log.debug("checkDocumentRefundable() - Document NOT voidable");
	// return false;
	// }
	// }

	public boolean loadTaxes(Map<Long, Map<String, PorcentajeImpuestoBean>> taxes) {
		log.debug("loadTaxes() - Loading taxes in fiscal printer");

		Long idTratImpuestos = SpringContext.getBean(Sesion.class).getAplicacion().getTienda().getCliente().getIdTratImpuestos();

		try {
			if (fiscalPrinter.getCapHasVatTable()) {
				Map<String, PorcentajeImpuestoBean> map = taxes.get(idTratImpuestos);
				for (PorcentajeImpuestoBean taxItem : map.values()) {
					if (BigDecimalUtil.isMayorACero(taxItem.getPorcentaje())) {
						fiscalPrinter.setVatValue(new Integer(taxItem.getCodImpuesto()), FormatUtil.getInstance().formateaNumero(taxItem.getPorcentaje(), 0));
					}
				}

				// Mandamos los impuestos a la impresora
				fiscalPrinter.setVatTable();
			}

			checkVATTableEntry();

			return true;
		}
		catch (Throwable e) {
			log.error("loadTaxes() - Error: " + e.getMessage(), e);
			return false;
		}
	}

	public void checkVATTableEntry() {
		int[] icmd = { 0, 0, 0, 0 };
		StringBuffer sbcmd = new StringBuffer("");

		log.debug("checkVATTableEntry() - Checking VAT table");

		for (int i = 0; i < 11; i++) {
			String VATid = "";
			if (i < 10) {
				VATid = "0" + i;
			}
			else {
				VATid = "" + i;
			}

			try {
				icmd[0] = 4205;
				sbcmd = new StringBuffer(VATid);
				fiscalPrinter.directIO(0, icmd, sbcmd);
				if (sbcmd.length() == 6) {
					log.debug("checkVATTableEntry() - Aliquota IVA Gruppo " + i + " = " + sbcmd.substring(2, 4) + ",00%");
				}
			}
			catch (Throwable e) {
				log.error("checkVATTableEntry() - Error: " + e.getMessage(), e);
			}
		}
	}

	// private void setPrinterData() throws JposException {
	// setHeaderLines();
	// }
	//
	// private void setHeaderLines() throws JposException {
	// log.debug("setHeaderLines() - Setting header lines in Fiscal Printer");
	//
	// List<String> headerLines = new ArrayList<String>();
	//
	// InputStream in = this.getClass().getClassLoader().getResourceAsStream("fiscalprinter-header.txt");
	// Scanner sc = new Scanner(in);
	// try {
	// while (sc.hasNext()) {
	// headerLines.add(sc.nextLine());
	// }
	// }
	// finally {
	// sc.close();
	// }
	//
	// log.debug("setHeaderLines() - Header lines: " + headerLines);
	//
	// for (int i = 0; i < headerLines.size() - 1; i++) {
	// String line = headerLines.get(i);
	// boolean doubleWidth = line.startsWith("**");
	// line = line.replaceAll("\\*\\*", "");
	//
	// fiscalPrinter.setHeaderLine(i + 1, line, doubleWidth);
	// }
	// }

	private void setFooterLine() throws JposException {
		log.debug("setFooterLine() - Setting footer line in Fiscal Printer");

		lineasPie = new ArrayList<String>();

		InputStream in = this.getClass().getClassLoader().getResourceAsStream("fiscalprinter-footer.txt");
		Scanner sc = new Scanner(in);
		try {
			while (sc.hasNext()) {
				lineasPie.add(sc.nextLine());
			}
		}
		finally {
			sc.close();
		}

		log.debug("setFooterLines() - Foote lines: " + lineasPie);
	}

	private void printLocator(TicketVentaAbono ticket) throws JposException {
		/* CODE-128 B --> I */
		fiscalPrinter.directIO(UposFiscalPrinterConst.FPTR_DIO_PRINT_BARCODE, null, "I:D1:10:30:" + ticket.getCabecera().getLocalizador()); // PL
	}

	@Override
	public ConfiguracionDispositivo getConfiguracion() {
		return configuracion;
	}

	@Override
	public void setConfiguracion(ConfiguracionDispositivo config) throws DispositivoException {
		this.configuracion = config;
		if (config.getConfiguracionModelo() != null) {
			cargaConfiguracion(config);
		}
	}

	@Override
	public int getEstado() {
		return 0;
	}

	@Override
	public void setEstado(int estado) {
	}

	@Override
	public boolean isConfigurable() {
		return false;
	}

	@Override
	public void configurar(Stage stage) {
	}

	@Override
	public void inicializar() {
	}

	@Override
	public void cerrar() {
	}

	@Override
	public void empezarLinea(int size, int lineCols) {
		lineaImpresionNormal = "";
	}

	@Override
	public void terminarLinea() {
		impresionNoFiscal.add(lineaImpresionNormal);
	}

	@Override
	public void empezarDocumento(Map<String, Object> parametros) {
		impresionNoFiscal = new ArrayList<String>();
	}

	@Override
	public void terminarDocumento() {
		if (!impresionNoFiscal.isEmpty()) {
			try {

				if (fiscalPrinter.getFiscalReceiptStation() != 1) {
					fiscalPrinter.resetPrinter();
				}

				fiscalPrinter.beginNonFiscal();

				for (String linea : impresionNoFiscal) {
					log.debug("terminarDocumento() - " + linea);

					List<String> cadenaDividida = TextUtils.getInstance().divideLines(linea, 40, "\r");
					if (cadenaDividida != null && !cadenaDividida.isEmpty()) {
						for (String cadena : cadenaDividida) {
							fiscalPrinter.printNormal(2, cadena);
						}
					}
					else {
						fiscalPrinter.printNormal(2, linea);
					}
				}

				fiscalPrinter.endNonFiscal();
			}
			catch (JposException e) {
				log.error("terminarDocumento() - Ha habido un error al imprimir: " + e.getMessage(), e);

				try {
					log.debug("terminarDocumento() - Reiniciando impresora.");
					fiscalPrinter.resetPrinter();
				}
				catch (Throwable ex) {
					log.error("terminarDocumento() - Error al reiniciar la impresora: " + e.getMessage(), e);
				}
			}
		}
	}

	@Override
	public void imprimirTexto(String texto, Integer size, String align, Integer style, String fontName, int fontSize) {
		String textoFormateado = texto;

		if (align != null && size != null) {
			if ("center".equals(align)) {
				textoFormateado = StringUtils.center(texto, size);
			}
			else if ("right".equals(align)) {
				textoFormateado = StringUtils.leftPad(texto, size);
			}
			else if ("left".equals(align)) {
				textoFormateado = StringUtils.rightPad(texto, size);
			}
		}
		else {
			if (size == null) {
				log.error(I18N.getTexto("imprimirTexto() - Argumento inválido: Alineamiento [" + align + "] con tamaño nulo. Utilizamos alineación izquierda"));
			}
		}

		lineaImpresionNormal = lineaImpresionNormal + textoFormateado;
	}

	@Override
	public void imprimirCodigoBarras(String codigoBarras, String tipo, String alineacion, int tipoAlineacionTexto, int height) {
		// try {
		// if (tipo == null || tipo.equals("128")) {
		// fiscalPrinter.directIO(UposFiscalPrinterConst.FPTR_DIO_PRINT_BARCODE, new int[] { 0 }, "I:D1:10:30:" +
		// codigoBarras); // PL
		// }
		// }
		// catch (JposException ex) {
		// log.error("Error imprimiendo Código de Barras : " + codigoBarras, ex);
		// }
	}

	@Override
	public void abrirCajon() {
	}

	@Override
	public void cortarPapel() {
	}

	@Override
	public void imprimirLogo() {
	}

	@Override
	public void comandoEntradaPlantilla(String comandoEntradaTexto, int leftMargin) {
	}

	@Override
	public void comandoSalidaPlantilla(String comandoSalidaTexto) {
	}

	@Override
	public void comandoEntradaCodigoBarras(String comandoEntradaCodBar) {
	}

	@Override
	public void comandoSalidaCodBar(String comandoSalidaTexto) {
	}

	@Override
	public void comandoEntradaLinea(String comandoEntradaLinea) {
	}

	@Override
	public void comandoEntradaTexto(String comandoEntradaTexto) {
	}

	@Override
	public void comandoSalidaTexto(String comandoSalidaTexto) {
	}

	@Override
	public void comandoSalidaLinea(String comandoSalidaTexto) {
	}

	public void getStatus() throws JposException {
		int[] icmd = { 0 };
		StringBuffer sbcmd = new StringBuffer("");
		String rtMainStatus = "", rtSubStatus = "", sDailyOpen = "", sNoWorkingPeriod = "", fileToSend = "", oldFiletoSend = "", fileRejected = "", expiryDateCD = "", expiryDateCA = "",
		        trainingMode = "";
		boolean dailyOpen = false, noWorkingPeriod = false;

		// Check RT status
		try {
			log.debug("getStatus() - DirectIO (RT status)");
			icmd[0] = 1138;
			sbcmd = new StringBuffer("01");
			fiscalPrinter.directIO(0, icmd, sbcmd);
			log.debug("getStatus() - DirectIO - icmd[0] = " + icmd[0]);
			log.debug("getStatus() - DirectIO - sbcmd = " + sbcmd);
			if ((sbcmd.length()) >= 30) {
				log.debug("getStatus() - Printer is RT model");
				rtType = (sbcmd.toString()).substring(2, 3);
				rtMainStatus = (sbcmd.toString()).substring(3, 5);
				rtSubStatus = (sbcmd.toString()).substring(5, 7);
				sDailyOpen = (sbcmd.toString()).substring(7, 8);
				sNoWorkingPeriod = (sbcmd.toString()).substring(8, 9);
				fileToSend = (sbcmd.toString()).substring(9, 12);
				oldFiletoSend = (sbcmd.toString()).substring(12, 15);
				fileRejected = (sbcmd.toString()).substring(15, 18);
				expiryDateCD = (sbcmd.toString()).substring(21, 27);
				expiryDateCA = (sbcmd.toString()).substring(27, 33);
				trainingMode = (sbcmd.toString()).substring(33, 34);

				if (sDailyOpen.equals("1")) {
					dailyOpen = true;
				}

				if (sNoWorkingPeriod.equals("1")) {
					noWorkingPeriod = true;
				}

				log.debug("getStatus() - RT type : " + rtType);
				log.debug("getStatus() - RT Main status : " + rtMainStatus);
				log.debug("getStatus() - RT Sub status : " + rtSubStatus);
				log.debug("getStatus() - RT Daily open : " + dailyOpen);
				log.debug("getStatus() - RT No Working Period : " + noWorkingPeriod);
				log.debug("getStatus() - RT File to send n. : " + fileToSend);
				log.debug("getStatus() - RT Old File to send n. : " + oldFiletoSend);
				log.debug("getStatus() - RT File rejected n. : " + fileRejected);
				log.debug("getStatus() - RT Expiry Date CD : " + expiryDateCD);
				log.debug("getStatus() - RT Expiry Date CA : " + expiryDateCA);
				log.debug("getStatus() - RT Training mode: " + trainingMode);
			}
			else {
				log.debug("getStatus() - Printer is NOT RT model");
			}
		}
		catch (JposException je) {
			log.error("getStatus() - JposException - errorCode : " + je.getErrorCode() + " - errorCodeExtended : " + je.getErrorCodeExtended());
			throw je;
		}
	}

	@Override
	public void directIOOccurred(DirectIOEvent arg0) {
	}

	@Override
	public void statusUpdateOccurred(StatusUpdateEvent arg0) {
	}

	@Override
	public void outputCompleteOccurred(OutputCompleteEvent arg0) {
	}

	@Override
	public void errorOccurred(ErrorEvent arg0) {
	}

	public void informeDiarioFiscal() {
		try {
			// Z-Report
			//
			if (fiscalPrinter.getDayOpened()) {
				fiscalPrinter.printZReport();
			}
		}
		catch (JposException e) {
			log.error("informeDiarioFiscal() - Ha habido un error al realizar el informe diario fiscal: " + e.getMessage(), e);

			try {
				log.debug("informeDiarioFiscal() - Reiniciando impresora.");
				fiscalPrinter.resetPrinter();
			}
			catch (Throwable ex) {
				log.error("informeDiarioFiscal() - Error al reiniciar la impresora: " + e.getMessage(), e);
			}
		}
	}

	public void imprimirTicketRegalo(TicketVentaAbono ticket) throws Throwable {
		try {
			int[] lineLenInfo = new int[4];
			fiscalPrinter.directIO(UposFiscalPrinterConst.FPTR_DIO_GET_LINE_LEN_INFO, lineLenInfo, null);
			int nonFiscalLineLen = lineLenInfo[3];

			fiscalPrinter.beginNonFiscal();
			fiscalPrinter.printNormal(0, StringHelper.center("*** Bon podarunkowy ***", nonFiscalLineLen));
			for (LineaTicket linea : ticket.getLineas()) {
				if (fiscalPrinter.getCapNonFiscalMode()) {
					fiscalPrinter.printNormal(0, StringHelper.left("- " + linea.getDesArticulo(), nonFiscalLineLen));
				}
				// else {
				// fiscalPrinter.beginFixedOutput(FiscalPrinterConst.FPTR_S_RECEIPT, 0);
				// fiscalPrinter.printFixedOutput(0x10, 0x01, ""); // this line number is mandatory to begin the print
				// fiscalPrinter.printFixedOutput(0x10, 0x10, " ");
				// fiscalPrinter.printFixedOutput(0x10, 0x11, "- " + linea.getDesArticulo());
				// fiscalPrinter.printFixedOutput(0x10, 0x14, " ");
				// fiscalPrinter.endFixedOutput();
				// }
			}

			printLocator(ticket);
			fiscalPrinter.endNonFiscal();

		}
		catch (Throwable e) {
			log.error("sendTicket() - Error: " + e.getMessage(), e);
			try {
				fiscalPrinter.resetPrinter();
			}
			catch (Throwable ex) {
				log.error("sendTicket() - Error reiniciando impresora: " + e.getMessage(), e);
			}
			throw e;
		}
	}

	@Override
	protected void cargaConfiguracion(ConfiguracionDispositivo config) throws DispositivoException {
	}

	private void cargarListaImpuestosEquivalencia() {
		/*
		 * La impresora fiscal de Polonia tiene unos codigos de impuestos propios, los cuales empiezan desde el 0 A -> 0
		 * = Normal (23%) B -> 1 = Reducido (8%) C -> 2 = Super Reducido (5%) D -> 3 = 0% E -> 4 = Exento F -> 5 =
		 * Undefinied
		 */

		listaImpuestosEquivalencia = new HashMap<Integer, Integer>();

		listaImpuestosEquivalencia.put(1, 0);
		listaImpuestosEquivalencia.put(2, 1);
		listaImpuestosEquivalencia.put(3, 2);
		listaImpuestosEquivalencia.put(4, 4);
	}

	public Map<Integer, Integer> getListaImpuestosEquivalencia() {
		return listaImpuestosEquivalencia;
	}

	public void setListaImpuestosEquivalencia(Map<Integer, Integer> listaImpuestosEquivalencia) {
		this.listaImpuestosEquivalencia = listaImpuestosEquivalencia;
	}
}
