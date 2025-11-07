package com.comerzzia.bimbaylola.pos.gui.ventas.devoluciones.articulos;


import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.comerzzia.bimbaylola.pos.gui.ventas.tickets.ByLTicketManager;
import com.comerzzia.bimbaylola.pos.gui.ventas.tickets.ByLVentaProfesionalManager;
import com.comerzzia.bimbaylola.pos.services.articulos.articulosNoAptos.ArticuloNoAptoService;
import com.comerzzia.bimbaylola.pos.services.ticket.ByLLineaTicket;
import com.comerzzia.bimbaylola.pos.services.ticket.profesional.ByLLineaTicketProfesional;
import com.comerzzia.pos.core.gui.InitializeGuiException;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.core.gui.permisos.exception.SinPermisosException;
import com.comerzzia.pos.core.gui.tablas.celdas.CellFactoryBuilder;
import com.comerzzia.pos.gui.ventas.devoluciones.articulos.DevolucionArticulosController;
import com.comerzzia.pos.gui.ventas.devoluciones.articulos.LineaTicketAbonoGui;
import com.comerzzia.pos.gui.ventas.tickets.TicketManager;
import com.comerzzia.pos.gui.ventas.tickets.articulos.FacturacionArticulosController;
import com.comerzzia.pos.services.core.documentos.DocumentoException;
import com.comerzzia.pos.services.core.sesion.Sesion;
import com.comerzzia.pos.services.ticket.TicketVentaAbono;
import com.comerzzia.pos.services.ticket.TicketsServiceException;
import com.comerzzia.pos.services.ticket.cabecera.DatosDocumentoOrigenTicket;
import com.comerzzia.pos.services.ticket.copiaSeguridad.CopiaSeguridadTicketService;
import com.comerzzia.pos.services.ticket.lineas.LineaTicket;
import com.comerzzia.pos.services.ticket.lineas.LineaTicketAbstract;
import com.comerzzia.pos.util.bigdecimal.BigDecimalUtil;
import com.comerzzia.pos.util.i18n.I18N;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;


@Component
@Primary
public class ByLDevolucionArticulosController extends DevolucionArticulosController{
		
	private static final Logger log = Logger.getLogger(ByLDevolucionArticulosController.class.getName());
	
	@Autowired
    private Sesion sesion;
	
	@Autowired
	protected ArticuloNoAptoService noAptoService;
	
	@Autowired
	private CopiaSeguridadTicketService copiaSeguridadTicketService;
	
	public static final String PERMISO_DEVOLVER_NO_APTOS = "DEVOLVER NO APTOS";
	
	
    @SuppressWarnings("unchecked")
	@Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //tbArticulos.setPlaceholder(new Label(""));
        
        //Inicializamos la lista de lineas de articulos
        lineas = FXCollections.observableList(new ArrayList<LineaTicketAbonoGui>());
        
        tbArticulos.setItems(lineas);
        
        tcArticulo.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbArticulos", "tcArticulo", null,CellFactoryBuilder.ESTILO_ALINEACION_IZQ));
        tcDescripcion.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbArticulos", "tcDescripcion", null,CellFactoryBuilder.ESTILO_ALINEACION_IZQ));
        tcDesglose1.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbArticulos", "tcDesglose1", null,CellFactoryBuilder.ESTILO_ALINEACION_IZQ));
        tcDesglose2.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbArticulos", "tcDesglose2", null,CellFactoryBuilder.ESTILO_ALINEACION_IZQ));
        tcPVP.setCellFactory(CellFactoryBuilder.createCellRendererCeldaImporte("tbArticulos", "tcPVP", CellFactoryBuilder.ESTILO_ALINEACION_DCHA));
        tcCantidad.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbArticulos", "tcCantidad", 0,CellFactoryBuilder.ESTILO_ALINEACION_DCHA));
        tcCantDevuelta.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbArticulos", "tcCantDevuelta", 0, CellFactoryBuilder.ESTILO_ALINEACION_DCHA));
        tcCantADevolver.setCellFactory(CellFactoryBuilder.createCellRendererCelda("tbArticulos", "cantidadADevolver", 0, CellFactoryBuilder.ESTILO_ALINEACION_DCHA));
        
        // Definimos un factory para cada celda para aumentar el rendimiento
        tcArticulo.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LineaTicketAbonoGui, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<LineaTicketAbonoGui, String> cdf) {
                return cdf.getValue().getArtProperty();
            }
        });
        tcDescripcion.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LineaTicketAbonoGui, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<LineaTicketAbonoGui, String> cdf) {
                return cdf.getValue().getDescripcionProperty();
            }
        });
        tcCantidad.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LineaTicketAbonoGui, BigDecimal>, ObservableValue<BigDecimal>>() {
            @Override
            public ObservableValue<BigDecimal> call(TableColumn.CellDataFeatures<LineaTicketAbonoGui, BigDecimal> cdf) {
                return cdf.getValue().getCantidadProperty();
            }
        });
        tcDesglose1.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LineaTicketAbonoGui, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<LineaTicketAbonoGui, String> cdf) {
                return cdf.getValue().getDesglose1Property();
            }
        });
        tcDesglose2.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LineaTicketAbonoGui, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<LineaTicketAbonoGui, String> cdf) {
                return cdf.getValue().getDesglose2Property();
            }
        });
        tcPVP.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LineaTicketAbonoGui, BigDecimal>, ObservableValue<BigDecimal>>() {
            @Override
            public ObservableValue<BigDecimal> call(TableColumn.CellDataFeatures<LineaTicketAbonoGui, BigDecimal> cdf) {
                return cdf.getValue().getPvpConDtoProperty();
            }
        });

        tcCantDevuelta.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LineaTicketAbonoGui, BigDecimal>, ObservableValue<BigDecimal>>() {
            @Override
            public ObservableValue<BigDecimal> call(TableColumn.CellDataFeatures<LineaTicketAbonoGui, BigDecimal> cdf) {
                return cdf.getValue().getCantidadDevuelta();
            }
        });
        tcCantADevolver.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LineaTicketAbonoGui, BigDecimal>, ObservableValue<BigDecimal>>() {
            @Override
            public ObservableValue<BigDecimal> call(TableColumn.CellDataFeatures<LineaTicketAbonoGui, BigDecimal> cdf) {
                return cdf.getValue().getCantidadADevolver();
            }
        });
        
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public void initializeForm() throws InitializeGuiException {
        try {
            HashMap<String, Object> datos = getDatos();
            ticketManager = (TicketManager) datos.get(FacturacionArticulosController.TICKET_KEY);
            
            lineasProvisionales = new ArrayList();
            
            DatosDocumentoOrigenTicket datosOrigen = ticketManager.getTicket().getCabecera().getDatosDocOrigen();
            
            if(datosOrigen != null) {
	            tfCaja.setText(datosOrigen.getCaja());
	            tfCodDoc.setText(datosOrigen.getCodTipoDoc());
	            tfFecha.setText(datosOrigen.getFecha());
	            tfTicket.setText(datosOrigen.getNumFactura() == null ? "" : String.valueOf(datosOrigen.getNumFactura()));
	            tfDesDoc.setText(sesion.getAplicacion().getDocumentos().getDocumento(datosOrigen.getCodTipoDoc()).getDestipodocumento());
            }
            
            
            cambiosRealizados = false;           
            
            refrescarDatosPantalla();
           
        }
        catch (DocumentoException ex) {
            log.error("No se encontró el documento de la operación original.", ex);
        }
        
        // Borramos el ticket guardado como copia de seguridad para evitar introducir basura en la base de datos.
	    try {
	        copiaSeguridadTicketService.guardarBackupTicketActivo(new TicketVentaAbono());
        }
        catch (TicketsServiceException  e) {
        	log.error("initializeForm() - " + e.getMessage());
        	VentanaDialogoComponent.crearVentanaError(getStage(), e.getMessageI18N(), e);
        }

    }

	@Override
	public void accionTablaAgnadirUno() {
		if (comprobarNoAptos(false)) {
			log.debug("accionTablaAgnadirUno() - Acción ejecutada");
			if (tbArticulos.getItems() != null) {
				int indice = tbArticulos.getSelectionModel().getSelectedIndex();
				if (indice < tbArticulos.getItems().size()) {
					LineaTicketAbonoGui lineaTablaSelec = ((LineaTicketAbonoGui) tbArticulos.getSelectionModel().getSelectedItem());
					if (lineaTablaSelec != null) {
						LineaTicket lineaTicketSelec = ((LineaTicket) ticketManager.getTicketOrigen().getLinea(lineaTablaSelec.getIdLinea()));
						if (lineaTicketSelec instanceof ByLLineaTicket) {
							compruebaNotaInformativa((ByLLineaTicket) lineaTicketSelec);
						}
						else if(lineaTicketSelec instanceof ByLLineaTicketProfesional) {
							compruebaNotaInformativa((ByLLineaTicketProfesional)lineaTicketSelec);
						}
						if (lineaTicketSelec != null && BigDecimalUtil.isMayorOrIgualACero(
						        lineaTicketSelec.getCantidad().subtract(lineaTicketSelec.getCantidadDevuelta().add(lineaTicketSelec.getCantidadADevolver().add(BigDecimal.ONE))))) {
							actualizarLineasProvisionales(BigDecimal.ONE, lineaTicketSelec);
							lineaTicketSelec.setCantidadADevolver(lineaTicketSelec.getCantidadADevolver().add(BigDecimal.ONE));
							cambiosRealizados = true;
							refrescarDatosPantalla();
							tbArticulos.getSelectionModel().select(indice);
						}
					}
					else {
						VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("Debe seleccionar previamente la línea que desea modificar."), this.getStage());
					}
				}
			}
		}
	}

	/**
	 * Acción seleccionar toda la línea para devolver
	 */
	@Override
	public void accionTablaAgnadirLinea() {
		log.debug("accionTablaAgnadirLinea() - Acción ejecutada");
		if (tbArticulos.getItems() != null) {
			int indice = tbArticulos.getSelectionModel().getSelectedIndex();
			if (indice < tbArticulos.getItems().size()) {
				LineaTicketAbonoGui lineaTablaSelec = ((LineaTicketAbonoGui) tbArticulos.getSelectionModel().getSelectedItem());
				if (lineaTablaSelec != null) {

					LineaTicket lineaTicketSelec = ((LineaTicket) ticketManager.getTicketOrigen().getLinea(lineaTablaSelec.getIdLinea()));
					if(lineaTicketSelec instanceof ByLLineaTicket) {
						compruebaNotaInformativa((ByLLineaTicket)lineaTicketSelec);
					}
					else if(lineaTicketSelec instanceof ByLLineaTicketProfesional) {
						compruebaNotaInformativa((ByLLineaTicketProfesional)lineaTicketSelec);
					}
					BigDecimal cantASumar = lineaTicketSelec.getCantidad().subtract(lineaTicketSelec.getCantidadDevuelta());
					actualizarLineasProvisionales(cantASumar, lineaTicketSelec);

					lineaTicketSelec.setCantidadADevolver(lineaTicketSelec.getCantidadADevolver().add(lineaTicketSelec.getCantidadDisponibleDevolver()));

					cambiosRealizados = true;
					refrescarDatosPantalla();
					tbArticulos.getSelectionModel().select(indice);
				}
				else {
					VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("Debe seleccionar previamente la línea que desea modificar."), this.getStage());
				}
			}
		}
	}

	/**
     * Acción seleccionar todo el ticket para devolver
     */
	@SuppressWarnings("unchecked")
	@Override
	public void accionTablaSeleccionarTodo() {

		if (comprobarNoAptos(true)) {
			log.debug("accionTablaSeleccionarTodo() - Acción ejecutada");
			if (tbArticulos.getItems() != null) {
				for (LineaTicketAbstract linea : (List<LineaTicketAbstract>) ticketManager.getTicketOrigen().getLineas()) {
					if (linea instanceof ByLLineaTicket) {
						compruebaNotaInformativa((ByLLineaTicket) linea);
					}
					else if(linea instanceof ByLLineaTicketProfesional) {
						compruebaNotaInformativa((ByLLineaTicketProfesional) linea);
					}
					if (BigDecimalUtil.isMayorACero(linea.getCantidadDisponibleDevolver())) {
						BigDecimal cantASumar = linea.getCantidad().subtract(linea.getCantidadDevuelta());
						actualizarLineasProvisionales(cantASumar, linea);
						linea.setCantidadADevolver(linea.getCantidadADevolver().add(linea.getCantidadDisponibleDevolver()));
						cambiosRealizados = true;
					}
				}
				refrescarDatosPantalla();
			}
		}

	}

    /**
     * Comprueba que los articulos que se intentan añadir no sean no aptos,
     * y en caso de ser, se deberá comprobar los permisos.
     * @param todos : Indica si es añadir todos.
     * @return
     */
	@SuppressWarnings("unchecked")
	public Boolean comprobarNoAptos(Boolean todos){
		
		Boolean continuarDevolucion = true;
		
		List<String> listadoNoAptos = null;
		/* Rescatamos el listado de no aptos. */
		if(ticketManager instanceof ByLVentaProfesionalManager) {
			listadoNoAptos = ((ByLVentaProfesionalManager)ticketManager).getListadoNoAptos();
		}
		else {
			listadoNoAptos = ((ByLTicketManager)ticketManager).getListadoNoAptos();			
		}
		
		if(listadoNoAptos != null){
			if(!todos){
				LineaTicketAbonoGui lineaTablaSelec = 
						((LineaTicketAbonoGui)tbArticulos.getSelectionModel().getSelectedItem());
				if(lineaTablaSelec!=null){
		            LineaTicket lineaTicketSelec = 
		            		((LineaTicket)ticketManager.getTicketOrigen().getLinea(lineaTablaSelec.getIdLinea()));
					for(String apto : listadoNoAptos){
						if(lineaTicketSelec.getCodArticulo().equals(apto)){
							if(!comprobarPermisoNoAptos()){
								continuarDevolucion = false;
								VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("El artículo " +
										lineaTicketSelec.getCodArticulo() + " , " + lineaTicketSelec.getDesArticulo() 
										+ " no es apto para devolución. Avise al responsable de tienda."), getStage());
								break;
							}
						}

					}
				}
			}else{
				int contador = 0;
				for(LineaTicketAbstract linea : 
		   			(List<LineaTicketAbstract>)ticketManager.getTicketOrigen().getLineas()){
	   				for(String apto : listadoNoAptos){
	   					if(linea.getCodArticulo().equals(apto)){
	   						if(!comprobarPermisoNoAptos()){
	   							continuarDevolucion = false;
	   							if(contador == 0){
	   								VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("El artículo " +
	   										linea.getCodArticulo() + " , " + linea.getDesArticulo() 
	   										+ "no es apto para devolución. Avise al responsable de tienda."), getStage());
	   							}
	   							contador++;
	   						}
	   					}
	   				}
		   		}
			}
		}
		
		return continuarDevolucion;
	}
    
	/**
	 * Comprueba que se tiene el permiso de "Devolver No Aptos",
	 * para las operaciones que son 0 de precio final.
	 * @return permisos : Boolean que indica "true" si tiene permisos, 
	 * y "false" en caso de no tener permisos.
	 */
	public Boolean comprobarPermisoNoAptos(){
		
		boolean permisos = true;
		
		try{
			compruebaPermisos(PERMISO_DEVOLVER_NO_APTOS);
		}catch(SinPermisosException e){
			permisos = false;
		}
		
		return permisos;
		
	}
	
	private void compruebaNotaInformativa(ByLLineaTicket linea) {
		log.debug("compruebaNotaInformativa() - Comprobando si la linea tiene nota informativa");
		if (linea.getNotaInformativa() != null) {
			VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("EL ARTÍCULO SELECCIONADO SE HA VENDIDO CON +INFO. REVISE EL TICKET POR FAVOR. GRACIAS"), getStage());
		}
	}
	

    private void compruebaNotaInformativa(ByLLineaTicketProfesional linea) {
    	log.debug("compruebaNotaInformativa() - Comprobando si la linea tiene nota informativa");
		if (linea.getNotaInformativa() != null) {
			VentanaDialogoComponent.crearVentanaAviso(I18N.getTexto("EL ARTÍCULO SELECCIONADO SE HA VENDIDO CON +INFO. REVISE EL TICKET POR FAVOR. GRACIAS"), getStage());
		}
	}

}
