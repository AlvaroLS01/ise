package com.comerzzia.bimbaylola.pos.gui.ventas.articulos.edicion;

import static com.comerzzia.pos.gui.ventas.tickets.articulos.FacturacionArticulosController.PERMISO_DEVOLUCIONES;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Controller;

import com.comerzzia.bimbaylola.pos.services.ticket.ByLLineaTicket;
import com.comerzzia.bimbaylola.pos.services.ticket.profesional.ByLLineaTicketProfesional;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.core.gui.permisos.exception.SinPermisosException;
import com.comerzzia.pos.core.gui.validation.ValidationUI;
import com.comerzzia.pos.gui.ventas.tickets.TicketManager;
import com.comerzzia.pos.gui.ventas.tickets.articulos.FacturacionArticulosController;
import com.comerzzia.pos.gui.ventas.tickets.articulos.edicion.EdicionArticuloController;
import com.comerzzia.pos.gui.ventas.tickets.articulos.edicion.FormularioEdicionArticuloBean;
import com.comerzzia.pos.persistence.articulos.ArticuloBean;
import com.comerzzia.pos.services.core.sesion.Sesion;
import com.comerzzia.pos.services.core.variables.VariablesServices;
import com.comerzzia.pos.services.ticket.lineas.LineaTicket;
import com.comerzzia.pos.util.bigdecimal.BigDecimalUtil;
import com.comerzzia.pos.util.config.SpringContext;
import com.comerzzia.pos.util.format.FormatUtil;
import com.comerzzia.pos.util.i18n.I18N;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;

@Controller
@Primary
public class ByLEdicionArticuloController extends EdicionArticuloController {
	
	private static final Logger log = Logger.getLogger(ByLEdicionArticuloController.class.getName());
	
	private static final String PERMISO_MODIFICAR_CANTIDAD_LINEA = "MODIFICAR_CANTIDAD_LINEA";
	
	@Autowired
    private Sesion sesion;
	@Autowired
	private VariablesServices variablesServices;
	
	protected BigDecimal cantidadOriginal;
	
	protected boolean puedeModificarCantidad;
	
	@Override
    public void initialize(URL url, ResourceBundle rb) {
    	
    }
	
	@Override
	public void initializeFocus() {
    	frEdicionArticulo.clearErrorStyle();
    	lbError.setText("");
    	
    	if(tfCantidad.isEditable()) {
    		tfCantidad.requestFocus();
    	}
    	else {
    		tfPrecio.requestFocus();
    	}
    }
	
	@Override
    public void initializeForm() {
    	ticketManager = (TicketManager) getDatos().get(FacturacionArticulosController.TICKET_KEY);
    	getDatos().remove(CLAVE_CANCELADO);
        LineaTicket lineaSeleccionada = null;

        //Recuperamos el artículo a editar que nos llega como parámetro
    	lineaSeleccionada = (LineaTicket) this.getDatos().get(CLAVE_PARAMETRO_ARTICULO);
    	
    	aplicarPromociones = true;
    	if(this.getDatos().get(CLAVE_APLICAR_PROMOCIONES) != null) {
    		aplicarPromociones = (Boolean) this.getDatos().get(CLAVE_APLICAR_PROMOCIONES);
    	}
    	
    	bVentaProfesional = lineaSeleccionada instanceof ByLLineaTicketProfesional;
        
        linea = lineaSeleccionada.clone();
        
        setLinea(lineaSeleccionada);

         // Seteamos los componentes        
        if (!puedeEditarPrecio && !lineaSeleccionada.getArticulo().getGenerico()) {
            tfPrecio.setEditable(false);
        }
        else {
            tfPrecio.setEditable(true);
            frEdicionArticulo.setFormField("precioTotal", tfPrecio);
        }

        tfPrecioTotal.setEditable(false);
        if (!puedeEditarDescuentos) {
            log.debug("inicializarComponentes() - No se editan descuentos");
            lbDescuento.setVisible(false);
            tfDescuento.setVisible(false);
        }
        else {
            tfDescuento.setEditable(true);
            frEdicionArticulo.setFormField("descuento", tfDescuento);
        }

        if (!desglose1) {
            log.debug("inicializarComponentes() - No hay desglose 1");
            fpLineaDesglose1.setVisible(false);
            fpLineaDesglose2.setVisible(false);
        }
        else {
            lbDesglose1.setText(I18N.getTexto(textoDesglose1));
        }
        if (!desglose2) {
            log.debug("inicializarComponentes() - No hay desglose 2");
            fpLineaDesglose2.setVisible(false);
            fpLineaDesglose2.setVisible(false);
        }
        else {
            lbDesglose2.setText(I18N.getTexto(textoDesglose2));
        }
        if (linea.getGenerico()) {
        	tfDescripcion.getStyleClass().remove("solo-lectura");
        	tfDescripcion.setEditable(true);
        	tfDescripcion.setFocusTraversable(true);
        } else {
        	tfDescripcion.getStyleClass().add("solo-lectura");
        	tfDescripcion.setEditable(false);
        	tfDescripcion.setFocusTraversable(false);
        }
        
        frEdicionArticulo.setFormField("desArticulo", tfDescripcion);
        
        calculaImporte();
        
        cantidadOriginal = linea.getCantidad();
        
        btNumerosSerie.setDisable(false);
        // Mostramos u ocultamos el botón de números de serie
	    if(linea.getArticulo().getNumerosSerie() && !panelLotesSerie.getChildren().contains(btNumerosSerie)) {
	    		panelLotesSerie.getChildren().add(btNumerosSerie);
	    }
	    else if(!linea.getArticulo().getNumerosSerie() && panelLotesSerie.getChildren().contains(btNumerosSerie)) {
	    	panelLotesSerie.getChildren().remove(btNumerosSerie);
	    }
	    
	    try {
			compruebaPermisos(PERMISO_MODIFICAR_CANTIDAD_LINEA);
			tfCantidad.setEditable(true);
		}
		catch (SinPermisosException e) {
			log.debug("initializeForm() - El usuario no tiene permisos para modificar la cantidad de la linea");
			tfCantidad.setEditable(false);
		};
	    
    }
	
	@Override
	    public void initializeComponents() {
	        log.debug("inicializarComponentes()");
	         
	        // Inicializamos las variables
	        textoDesglose1 = variablesServices.getVariableAsString(VariablesServices.ARTICULO_DESGLOSE1_TITULO);
	        textoDesglose2 = variablesServices.getVariableAsString(VariablesServices.ARTICULO_DESGLOSE2_TITULO);
	        permiteVentaPrecioCero = variablesServices.getVariableAsBoolean(VariablesServices.TPV_PERMITIR_VENTA_PRECIO_CERO, true);
	        puedeEditarDescuentos = variablesServices.getVariableAsBoolean(VariablesServices.TICKETS_USA_DESCUENTO_EN_LINEA);
	        desglose1 = sesion.getAplicacion().isDesglose1Activo();
	        desglose2 = sesion.getAplicacion().isDesglose2Activo();
	        puedeEditarPrecio = variablesServices.getVariableAsBoolean(VariablesServices.TICKETS_PERMITE_CAMBIO_PRECIO);
	        
	        frEdicionArticulo = SpringContext.getBean(FormularioEdicionArticuloBean.class);
	               
	        tfPrecio.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> paramObservableValue, String oldValue, String newValue) {
					if(!oldValue.isEmpty() && !newValue.isEmpty() && !listenerChangedTextTotal){
						try{		
							BigDecimal precioTotal;
							listenerChangedTextDescuento = true;
							BigDecimal descuento = FormatUtil.getInstance().desformateaBigDecimal(tfDescuento.getText(), 2);
							linea.setDescuentoManual(descuento);
							listenerChangedTextDescuento = false;
							
							if(!bVentaProfesional){			
								precioTotal = FormatUtil.getInstance().desformateaBigDecimal(newValue, 2).abs();
								linea.setPrecioTotalSinDto(precioTotal);
							}
							else{
								precioTotal = FormatUtil.getInstance().desformateaBigDecimal(newValue, 2).abs();
								linea.setPrecioSinDto(precioTotal);
							}
							
							/*BigDecimal importe = BigDecimalUtil.redondear(precioTotal.subtract(precioTotal.multiply(descuento.divide(new BigDecimal(100)))), 2);
							importe = importe.multiply(FormatUtil.getInstance().desformateaBigDecimal(tfCantidad.getText(),3));
							
							tfImporte.setText(FormatUtil.getInstance().formateaImporte(importe));*/
							calculaImporte();
						}catch(NumberFormatException e){
						}catch(Exception e){
							log.warn("changed() - Error:" + e.getMessage(), e);
						}
					}
				}
			});
	        
	        tfDescuento.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> paramObservableValue, String oldValue, String newValue) {
					if(!oldValue.isEmpty() && !newValue.isEmpty() && !listenerChangedTextDescuento){
						try{						
							BigDecimal descuento = FormatUtil.getInstance().desformateaBigDecimal(newValue, 2);
							linea.setDescuentoManual(descuento);
							
							calculaImporte();
						}catch(NumberFormatException e){
						}catch(Exception e){
							log.warn("changed() - Error:" + e.getMessage(), e);
						}
					}
				}
			});
	        
	        tfCantidad.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> paramObservableValue, String oldValue, String newValue) {
					if(!oldValue.isEmpty() && !newValue.isEmpty() && !listenerChangedTextCantidad){
						try{										
							calculaImporte();
						}catch(NumberFormatException e){
						}catch(Exception e){
							log.warn("changed() - Error:" + e.getMessage(), e);
						}
					}
				}
			});
	        
	        frEdicionArticulo.setFormField("cantidad", tfCantidad);
	        frEdicionArticulo.setFormField("precioFinal", tfPrecio);
	        frEdicionArticulo.setFormField("precioFinalProfesional", tfPrecio);
	        
	        tfCantidad.focusedProperty().addListener(new ChangeListener<Boolean>() {
	            @Override
	            public void changed(ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) {
	                if(oldValue){
	                    BigDecimal cantidadNueva = FormatUtil.getInstance().desformateaBigDecimal(tfCantidad.getText(),3);
	                    cantidadNueva = comprobarCantidadUnitaria(cantidadNueva);
	                    if(cantidadNueva != null){
//	                        if(ticketManager.getDocumentoActivo().isSignoNegativo()){
//	                            cantidadNueva = cantidadNueva.abs().negate();
//	                        }
//	                        else if(ticketManager.getDocumentoActivo().isSignoPositivo()){
//	                            cantidadNueva = cantidadNueva.abs();
//	                        }
	                        tfCantidad.setText(FormatUtil.getInstance().formateaNumero(cantidadNueva, 3));
	                    }
	                }
	            }
	        });
	        
	        tfPrecio.focusedProperty().addListener(new ChangeListener<Boolean>() {
	            @Override
	            public void changed(ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) {
	                if(oldValue){
	                    if(!bVentaProfesional){	                    	
	                    	BigDecimal cantidadNueva = FormatUtil.getInstance().desformateaBigDecimal(tfPrecio.getText(), 2);
							tfPrecio.setText(FormatUtil.getInstance().formateaImporte(cantidadNueva));
	                    }
	                    else{
	                    	BigDecimal cantidadNueva = FormatUtil.getInstance().desformateaBigDecimal(tfPrecio.getText(), 2);
							tfPrecio.setText(FormatUtil.getInstance().formateaNumero(cantidadNueva, 2));
	                    }
	                }
	            }
	        });
	        
	        tfDescuento.focusedProperty().addListener(new ChangeListener<Boolean>() {
	            @Override
	            public void changed(ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) {
	                if(oldValue){
	                	BigDecimal cantidadNueva = FormatUtil.getInstance().desformateaBigDecimal(tfDescuento.getText(), 2);
	                	tfDescuento.setText(FormatUtil.getInstance().formateaNumero(cantidadNueva, 2));
	                }
	            }
	        });
	        
	        tfCantidad.textProperty().addListener(new ChangeListener<String>(){
	            @Override
	            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
	            	if(!oldValue.equals(newValue)) {
		            	btNumerosSerie.setDisable(true);
	            	}
	            }
	        });
	    
	        tfImporte.setEditable(false);
	        addTextLimiter(tfDescripcion, 45);
	        
	        //Se registra el evento para salir de la pantalla pulsando la tecla escape.
	        registrarAccionCerrarVentanaEscape();
	        
	    }
	
	@FXML
    public void accionAceptar() {
        log.debug("accionAceptar()");
        BigDecimal precio = null;
        
        try{
	        precio = FormatUtil.getInstance().desformateaBigDecimal(tfCantidad.getText(), 0);
	        tfCantidad.setText(FormatUtil.getInstance().formateaNumero(precio, 0));
        }
    	catch(Exception e){
    		//
    	}
        
        frEdicionArticulo.setCantidad(tfCantidad.getText());

        if(!bVentaProfesional){
        	try{
	        	precio = FormatUtil.getInstance().desformateaBigDecimal(tfPrecio.getText(), 2);
	        	tfPrecio.setText(FormatUtil.getInstance().formateaImporte(precio));
        	}
        	catch(Exception e){
        		//
        	}
        	
        	frEdicionArticulo.setPrecioFinalProfesional("0");
        	frEdicionArticulo.setPrecioFinal(tfPrecio.getText());
        }
        else{
        	try{
	        	precio = FormatUtil.getInstance().desformateaBigDecimal(tfPrecio.getText(), 2);
	        	tfPrecio.setText(FormatUtil.getInstance().formateaNumero(precio, 2));
        	}
        	catch(Exception e){
        		//
        	}
        	
        	frEdicionArticulo.setPrecioFinal("0");
        	frEdicionArticulo.setPrecioFinalProfesional(tfPrecio.getText());
        }
        
        frEdicionArticulo.setDescuento(tfDescuento.getText());
        frEdicionArticulo.setDesArticulo(tfDescripcion.getText());

        if (validarFormularioEdicionArticulo() && sonNumerosSerieValidos()) {
            BigDecimal nuevaCantidad = frEdicionArticulo.getCantidadAsBD();
            BigDecimal precioSinImpuestos = null;
            if(lineaOriginal instanceof ByLLineaTicketProfesional && precio != null) {
            	precioSinImpuestos = precio;
            }
            else {
            	precioSinImpuestos = sesion.getImpuestos().getPrecioSinImpuestos(linea.getCodImpuesto(), linea.getPrecioTotalSinDto(), ticketManager.getTicket().getIdTratImpuestos());            	
            }
            lineaOriginal.setPrecioSinDto(precioSinImpuestos);
            lineaOriginal.setPrecioTotalSinDto(linea.getPrecioTotalSinDto());
            lineaOriginal.setDescuentoManual(linea.getDescuentoManual());
            lineaOriginal.setCantidad(nuevaCantidad);
            lineaOriginal.setDesArticulo(frEdicionArticulo.getDesArticulo());
            lineaOriginal.setNumerosSerie(linea.getNumerosSerie());
            lineaOriginal.recalcularImporteFinal();
            if(aplicarPromociones) {
            	ticketManager.recalcularConPromociones();
            }
			if (lineaOriginal.tieneCambioPrecioManual()) {
				cambioPrecioManual();
			}
			
			if(!bVentaProfesional) {
				((ByLLineaTicket) lineaOriginal).setCambioPrecioManual(lineaOriginal.tieneCambioPrecioManual());
			}else {
				((ByLLineaTicketProfesional) lineaOriginal).setCambioPrecioManual(lineaOriginal.tieneCambioPrecioManual());
			}
        
            if(lineaOriginal.tieneDescuentoManual()){
            	cambioDescuentoManual();
            	
            }
            getStage().close();
        }
    }
	
	protected boolean sonNumerosSerieValidos() {
    	if(linea.getArticulo().getNumerosSerie()) {
    		BigDecimal nuevaCantidad = FormatUtil.getInstance().desformateaBigDecimal(tfCantidad.getText());
			if(!cantidadOriginal.equals(nuevaCantidad) && linea.getNumerosSerie() != null && linea.getNumerosSerie().size() > nuevaCantidad.intValue()) {
				// Comprobamos si se ha modificado la línea una vez insertados números de serie y se ha bajado la cantidad. En ese caso, se borráran 
				// los números de serie registrados y se volverá a mostrar la pantalla de números de serie
				if (VentanaDialogoComponent.crearVentanaConfirmacion(I18N.getTexto("Esta linea tiene asociados numeros de serie, la cantidad indicada es inferior a la anterior, los números de serie serán borrados, deberá indicarlos de nuevo, ¿está seguro?"), getStage())) {
					linea.getNumerosSerie().clear();
					editarNumerosSerie();
					cantidadOriginal = nuevaCantidad;
				}
				return false;
			}
		}
    	return true;
    }
	
    protected void accionRefrescaVista() {
        log.debug("accionRefrescaVista()");
        
        ArticuloBean articuloSeleccionado = lineaOriginal.getArticulo();
        
        tfCantidad.setText(FormatUtil.getInstance().formateaNumero(lineaOriginal.getCantidad(),0));
        tfDesglose1.setText(lineaOriginal.getDesglose1());
        tfDesglose2.setText(lineaOriginal.getDesglose2());
        tfDescripcion.setText(articuloSeleccionado.getDesArticulo());
        tfArticulo.setText(articuloSeleccionado.getCodArticulo());
        //tfPrecioTotal.setText(FormatUtil.getInstance().formateaImporte(lineaOriginal.getPrecioTotalTarifaOrigen()));
        listenerChangedTextDescuento = true;
        listenerChangedTextTotal = true;
        tfDescuento.setText(FormatUtil.getInstance().formateaNumero(lineaOriginal.getDescuentoManual(),2));
        if(!bVentaProfesional){
        	tfPrecio.setText(FormatUtil.getInstance().formateaImporte(lineaOriginal.getPrecioTotalSinDto()));
        }
        else{
        	tfPrecioTotal.setText(FormatUtil.getInstance().formateaNumero(lineaOriginal.getTarifa().getPrecioVenta(), 2));
            tfPrecio.setText(FormatUtil.getInstance().formateaNumero(lineaOriginal.getPrecioSinDto(), 2));
        }
        listenerChangedTextDescuento = false;
        listenerChangedTextTotal = false;
        
        calculaImporte();
    }

    
    /**
     * Valida los datos introducidos
     *
     * @return
     */
    @Override
    protected boolean validarFormularioEdicionArticulo() {   	
        // Limpiamos los errores que pudiese tener el formulario
        frEdicionArticulo.clearErrorStyle();
        //Limpiamos el posible error anterior
        lbError.setText("");

        // Validamos el formulario de login
        Set<ConstraintViolation<FormularioEdicionArticuloBean>> constraintViolations = ValidationUI.getInstance().getValidator().validate(frEdicionArticulo);
        if (constraintViolations.size() >= 1) {
            ConstraintViolation<FormularioEdicionArticuloBean> next = constraintViolations.iterator().next();
            frEdicionArticulo.setErrorStyle(next.getPropertyPath(), true);
            frEdicionArticulo.setFocus(next.getPropertyPath());
            lbError.setText(next.getMessage());
            return false;
        }

        if (BigDecimalUtil.isMenorACero(frEdicionArticulo.getCantidadAsBD()) && !ticketManager.isEsDevolucion()) {
            try {
                super.compruebaPermisos(PERMISO_DEVOLUCIONES);
            } catch (SinPermisosException e) {
                lbError.setText(I18N.getTexto("No tiene permisos para realizar una devolución"));
                return false;
            }
        }
        
        BigDecimal max = new BigDecimal(10000000);
		if(BigDecimalUtil.isMayorOrIgual(frEdicionArticulo.getCantidadAsBD(), max)){
			lbError.setText(I18N.getTexto(I18N.getTexto("La cantidad debe ser menor que {0}", FormatUtil.getInstance().formateaNumero(max))));
        	tfCantidad.requestFocus();
			return false;
		}
		if(BigDecimalUtil.isIgualACero(frEdicionArticulo.getCantidadAsBD())){
			lbError.setText(I18N.getTexto(I18N.getTexto("La cantidad debe ser distinta de cero.")));
        	tfCantidad.requestFocus();
			return false;
		}
		max = new BigDecimal(10000000000L);
		if(BigDecimalUtil.isMayorOrIgual(FormatUtil.getInstance().desformateaImporte(tfPrecio.getText()), max)){
			lbError.setText(I18N.getTexto(I18N.getTexto("El campo debe ser menor que {0}", FormatUtil.getInstance().formateaNumero(max))));
			tfPrecio.requestFocus();
			return false;
		}
		if(BigDecimalUtil.isMayorOrIgual(FormatUtil.getInstance().desformateaImporte(tfPrecioTotal.getText()), max)){
			lbError.setText(I18N.getTexto(I18N.getTexto("El campo debe ser menor que {0}", FormatUtil.getInstance().formateaNumero(max))));
        	tfPrecioTotal.requestFocus();
			return false;
		}
        
		max = new BigDecimal(10000000000L);
		if(BigDecimalUtil.isMayorOrIgual(FormatUtil.getInstance().desformateaImporte(tfPrecio.getText()), max)){
			lbError.setText(I18N.getTexto(I18N.getTexto("El campo debe ser menor que {0}", FormatUtil.getInstance().formateaNumero(max))));
			tfPrecio.requestFocus();
			return false;
		}
		if(BigDecimalUtil.isMayorOrIgual(FormatUtil.getInstance().desformateaImporte(tfPrecioTotal.getText()), max)){
			lbError.setText(I18N.getTexto(I18N.getTexto("El campo debe ser menor que {0}", FormatUtil.getInstance().formateaNumero(max))));
        	tfPrecioTotal.requestFocus();
			return false;
		}
		
        if(BigDecimalUtil.isMenorACero(frEdicionArticulo.getDescuentoAsBD()) || BigDecimalUtil.isMayor(frEdicionArticulo.getDescuentoAsBD(), new BigDecimal(100))){
        	lbError.setText(I18N.getTexto("El descuento debe ser un valor entre 0 y 100."));
        	tfDescuento.requestFocus();
        	return false;
        }
              
        //control de venta a precio cero
       	if(BigDecimalUtil.isIgualACero(FormatUtil.getInstance().desformateaBigDecimal(tfPrecio.getText(),2))){

        	if(permiteVentaPrecioCero){
        		boolean vender = VentanaDialogoComponent.crearVentanaConfirmacion(I18N.getTexto("¿Desea vender el artículo a precio 0?"), getStage());
				if(!vender){
					tfPrecio.requestFocus();
					tfPrecio.selectAll();
					return false;
				}
        	}else{
        		lbError.setText(I18N.getTexto("No está permitida la venta a precio 0."));
        		if(BigDecimalUtil.isIgual(FormatUtil.getInstance().desformateaBigDecimal(tfDescuento.getText()), new BigDecimal(100d))){
        			Platform.runLater(new Runnable() {
        				@Override
        				public void run() {
        					tfDescuento.requestFocus();
        					tfDescuento.selectAll();
        				}
        			});
        		}else{
        			Platform.runLater(new Runnable() {
        				@Override
        				public void run() {
        					tfPrecio.requestFocus();
        					tfPrecio.selectAll();
        				}
        			});
        		}
        		return false;
        	}
        }
        
        return true;
    }
    
    @Override
    protected void calculaImporte(){
    	BigDecimal precioTotal,descuento, cantidad, importe;
    	
    	cantidad  = FormatUtil.getInstance().desformateaBigDecimal(tfCantidad.getText(),2);
    	descuento = FormatUtil.getInstance().desformateaBigDecimal(tfDescuento.getText(),2);
    	if(!bVentaProfesional){	
    		precioTotal = FormatUtil.getInstance().desformateaBigDecimal(tfPrecio.getText(),2);
    		importe  = BigDecimalUtil.menosPorcentajeR(precioTotal, descuento);
    		tfPrecioTotal.setText(FormatUtil.getInstance().formateaImporte(importe));
    	}
    	else{
    		precioTotal = FormatUtil.getInstance().desformateaBigDecimal(tfPrecio.getText(),2);
    		importe = BigDecimalUtil.menosPorcentajeR4(precioTotal, descuento);
    		tfPrecioTotal.setText(FormatUtil.getInstance().formateaNumero(importe,2));
    	}
    			
    	importe = importe.multiply(cantidad);
		tfImporte.setText(FormatUtil.getInstance().formateaImporte(importe));
    }
    
}