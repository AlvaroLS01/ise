package com.comerzzia.bimbaylola.pos.gui.componentes.dialogos;

import java.awt.Toolkit;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import org.apache.log4j.Logger;

import com.comerzzia.pos.core.gui.POSApplication;
import com.comerzzia.pos.core.gui.componentes.dialogos.VentanaDialogoComponent;
import com.comerzzia.pos.core.gui.componentes.keyboard.Keyboard;
import com.comerzzia.pos.util.config.AppConfig;
import com.comerzzia.pos.util.config.SpringContext;
import com.comerzzia.pos.util.i18n.I18N;

/**
 * Componente Ventana de diálogo
 *
 * @DES
 */
public class ByLVentanaDialogoComponent extends VentanaDialogoComponent {

    // <editor-fold desc="Declaración de variables">   
    private static final Logger log = Logger.getLogger(ByLVentanaDialogoComponent.class.getName());

    public static int MSG_MAX_CHARS = 500; 
    
    public static int TIPO_INFO = 1;
    public static int TIPO_AVISO = 2;
    public static int TIPO_ERROR = 3;
    public static int TIPO_ERROR_EXCEPCION = 4;
    public static int TIPO_CONFIRMACION = 5;
    
    public static boolean WAIT_USER_CLOSE = true;
    
    // escena
    protected Scene scene;
    protected Scene primaryScene; // escena principal.

    // atributos
    protected int tipoVentana;
    protected String imagen;
    protected double ancho, alto; // ancho y alto configurado de la pantalla
    protected Modality modalidad;
    protected Window ventanaPadre;
    protected String titulo;
    protected String mensaje;
    protected String trazaError;
    protected Throwable excepcion; // Si es un mensaje de error, recogemos la excepción

    // componentes de la ventana
    protected ImageView ivImagen;
    protected BorderPane panelInterno;
    protected VBox vBoxMensage;
    protected Label lbMessage;
    protected HBox panelBotones;
    protected Button btnOk;

    // componentes para el error
    protected TextArea taTraza;
    protected Button btTraza;
    
    protected boolean pulsadoAceptar = false;

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Clase interna constructora - Construye y establece los parámetros de la nueva ventana">   
    /**
     * Clase interna Constructora.
     */
    public static class Constructor {

        private static final String BOTON_ACEPTAR = "botonAceptar";

		protected static final String ESTILO_VENTANA = "UNDECORATED";// "UTILITY"; //DECORATED, UNDECORATED, TRANSPARENT, UTILITY
        
        protected static final int VENTANA_TRAZA_ANCHO = 900;
        protected static final int VENTANA_TRAZA_ALTO = 600;
        
        protected static final boolean CUADRO_DIALOGO_CABECERA_CUADRO = true; // Indica si el cuadro de diálogo tiene titulo de cuadro o no
        protected static final int CUADRO_DIALOGO_MIN_ANCHO_MENSAJE = 180;
        protected static final int ANCHO_BOTONES_DIALOGO = 90;
        protected static final int ALTO_BOTONES_DIALOGO = 30;

        protected ByLVentanaDialogoComponent ventana;        
        
        /**
         * Crea la ventan de dialogo en función de la configuración.
         *
         * @return
         */
        public Constructor crear() {
            ventana = new ByLVentanaDialogoComponent();
            ventana.setResizable(false);
            
            switch (ESTILO_VENTANA) {
                case "UNDECORATED":
                    ventana.initStyle(StageStyle.UNDECORATED);
                    break;
                case "TRANSPARENT":
                    ventana.initStyle(StageStyle.TRANSPARENT);
                    break;
                case "UTILITY":
                    ventana.initStyle(StageStyle.UTILITY);
                    break;
                default:
                    ventana.initStyle(StageStyle.DECORATED);
                    break;
            }

            // Configuración de la ventana
            ventana.centerOnScreen();
            ventana.initModality(Modality.WINDOW_MODAL);
            ventana.setIconified(false);

            ventana.panelInterno = new BorderPane();

            // Imagen de la ayuda
            ventana.ivImagen = new ImageView();
            ventana.panelInterno.setLeft(ventana.ivImagen);
            BorderPane.setMargin(ventana.ivImagen, new Insets(10)); // probar con css

            // message
            ventana.vBoxMensage = new VBox();
            ventana.vBoxMensage.setAlignment(Pos.CENTER_LEFT);

            ventana.lbMessage = new Label();
            ventana.lbMessage.setWrapText(true);
            ventana.lbMessage.setMinWidth(CUADRO_DIALOGO_MIN_ANCHO_MENSAJE);
             
            if(!Double.isNaN(POSApplication.getInstance().getStage().getHeight()) && 
            		Double.isNaN(POSApplication.getInstance().getStage().getWidth())){
	            ventana.lbMessage.setMaxWidth(POSApplication.getInstance().getStage().getWidth() * 0.8);
	            ventana.lbMessage.setMaxHeight(POSApplication.getInstance().getStage().getHeight() * 0.8);
            }

            ventana.vBoxMensage.getChildren().add(ventana.lbMessage);
            ventana.panelInterno.setCenter(ventana.vBoxMensage);
            BorderPane.setAlignment(ventana.vBoxMensage, Pos.CENTER);
            BorderPane.setMargin(ventana.vBoxMensage, new Insets(10, 10, 10, 2 * 10)); //probar con estilos

            // botones
            ventana.panelBotones = new HBox();
            ventana.panelBotones.setSpacing(10); // probar con estilos
            ventana.panelBotones.setAlignment(Pos.BOTTOM_CENTER);
            BorderPane.setMargin(ventana.panelBotones, new Insets(0, 0, 1.5 * 10, 0));
            ventana.panelInterno.setBottom(ventana.panelBotones);
            ventana.panelInterno.widthProperty().addListener(new ChangeListener<Number>() {

                @Override
                public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                    ventana.panelBotones.layout();
                }
            });

            ventana.scene = new Scene(ventana.panelInterno);
            ventana.setScene(ventana.scene);

            // Estilos de ventana
            ventana.panelInterno.setId("ventanaDialogo");
            ventana.scene.setFill(Color.TRANSPARENT);
            POSApplication.getInstance().addBaseCSS(ventana.scene);

            ventana.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>(){
            	@Override
            	public void handle(KeyEvent event){
            		if(event.getCode() == KeyCode.ESCAPE){
            			close();
            		}
            	}
            });
            
            ventana.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>(){ //ALT + F4 debe estar en KEY_PRESSED para que no haya conflicto con el SO
                @Override
                public void handle(KeyEvent event){
                    if((event.isAltDown() && event.getCode() == KeyCode.F4)){
                    	close();
                    }
                }
            });

            POSApplication.getInstance().addDialogo(ventana);
            
            return this;
        }

        public void close(){
        	if (ventana.primaryScene != null) {
                ventana.primaryScene.getRoot().setEffect(null); // Eliminamos el efecto del fondo de pantalla
            }
            ventana.close();
        }
        
        public Constructor setTitulo(String titulo) {
            String tituloLocalizado = I18N.getTexto(titulo);
            ventana.titulo = tituloLocalizado;
            return this;
        }

        /**
         * Establece el tipo de ventana, y con ello el tipo de icono de la
         * ventana
         *
         * @param tipoVentana
         * @return
         */
        public Constructor setTipoAviso(int tipoVentana) {
            ventana.tipoVentana = tipoVentana;
            Image imagen;

            switch (tipoVentana) {
                case 1:
                    //INFORMACION
                    imagen = POSApplication.getInstance().createImage("dialog/iconoInformacion.png");
                    break;
                case 2:
                    // AVISO
                    imagen = POSApplication.getInstance().createImage("dialog/iconoAviso.png");
                    break;
                case 3:
                    // ERROR
                    imagen = POSApplication.getInstance().createImage("dialog/iconoError.png");
                    break;
                case 4:
                    // ERROR
                    imagen = POSApplication.getInstance().createImage("dialog/iconoError.png");
                    break;
                case 5:
                    // CONFIRMACION
                    imagen = POSApplication.getInstance().createImage("dialog/iconoConfirmacion.png");
                    break;
                default:
                    //ERROR   
                    imagen = POSApplication.getInstance().createImage("dialog/iconoError.png");
            }
            ventana.ivImagen.setImage(imagen);
            return this;
        }

        public Constructor setMensaje(String mensaje) {
            // intentamos localizar el mensaje de error
            ventana.mensaje = mensaje;
            if(mensaje.length() > MSG_MAX_CHARS){
            	mensaje = mensaje.substring(0, MSG_MAX_CHARS)+"...";
            }
            ventana.lbMessage.setText(mensaje);
            ventana.lbMessage.setStyle("-fx-text-fill: red;");
            return this;
        }
        
        /**
         * Realiza el mismo efecto que setMensaje, pero no incluye estilo de texto.
         * @param mensaje : Mensaje para mostrar en la ventana.
         * @return
         */
        public Constructor setMensajeSiNo(String mensaje){
            ventana.mensaje = mensaje;
            if(mensaje.length() > MSG_MAX_CHARS){
            	mensaje = mensaje.substring(0, MSG_MAX_CHARS) + "...";
            }
            ventana.lbMessage.setText(mensaje);
            return this;
        }

        public Constructor setModalidad(Modality modalidad) {
            ventana.modalidad = modalidad;
            return this;
        }

        public Constructor setVentanaPadre(Window ventanaPadre) {
            ventana.ventanaPadre = ventanaPadre;
            return this;
        }

        /**
         * Añade un botón generico
         *
         * @param cadena cadena de localización de idiomas para el botón
         * @param actionHandler acción que se realiza al pulsar el botón (si no
         * se desea acción, será null)
         * @return
         */
        @SuppressWarnings("rawtypes")
        public Button addBoton(final String cadena, final EventHandler actionHandler) {

            final Button btGenerico = new Button(I18N.getTexto(cadena));
            // Configuramos el botón
            btGenerico.setMinWidth(ANCHO_BOTONES_DIALOGO);
            btGenerico.setMinHeight(ALTO_BOTONES_DIALOGO);

            // Asignamos la acción
            btGenerico.setOnAction(new EventHandler<ActionEvent>() {
                @SuppressWarnings("unchecked")
				@Override
                public void handle(ActionEvent t) {
                    // Establecemos que el botón fue pulsado
                	if(BOTON_ACEPTAR.equals(btGenerico.getUserData())){
                		ventana.pulsadoAceptar = true;
                	}
                	
                    if (ventana.primaryScene != null) {
                        ventana.primaryScene.getRoot().setEffect(null); // Eliminamos el efecto del fondo de pantalla
                    }
                    ventana.close();
                    if (actionHandler != null) {
                        actionHandler.handle(t);  // Si se definió un handler para la ventana, se lanza.
                    }
                    
                    POSApplication.getInstance().removeDialogo(ventana);
                }
            });

            ventana.panelBotones.getChildren().add(btGenerico);
            return btGenerico;
        }

        // Funciones de creación de botones comunes
        /**
         * Añade un botón de tipo Aceptar
         * @param actionHandler
         * @return 
         */
        @SuppressWarnings("rawtypes")
        public Constructor addBotonAceptar(final EventHandler actionHandler) {
        	String msg = I18N.getTexto("Aceptar");
        	if(ventana.tipoVentana == TIPO_AVISO ||
					  ventana.tipoVentana == TIPO_ERROR ||
					  ventana.tipoVentana == TIPO_ERROR_EXCEPCION){
        		msg = I18N.getTexto("Pulse para continuar");
        	}
            final Button boton = addBoton(msg, actionHandler);
            boton.addEventFilter(KeyEvent.ANY, new EventHandler<KeyEvent>(){
            	@Override
                public void handle(KeyEvent event){
                    if(event.getCode() == KeyCode.ENTER && (ventana.tipoVentana == TIPO_AVISO ||
                    											  ventana.tipoVentana == TIPO_ERROR ||
                    											  ventana.tipoVentana == TIPO_ERROR_EXCEPCION)){
                    	//Ventana de errores no se pueden cerrar con enter
                    	event.consume();
                    }
                }
            });
            boton.setUserData(BOTON_ACEPTAR);
            return this;
        }

        @SuppressWarnings("rawtypes")
		public Constructor addBotonSiAceptar(final EventHandler actionHandler){
        	String msg = I18N.getTexto("Si");
        	if(ventana.tipoVentana == TIPO_AVISO || ventana.tipoVentana == TIPO_ERROR 
        			|| ventana.tipoVentana == TIPO_ERROR_EXCEPCION){
        		msg = I18N.getTexto("Pulse para continuar");
        	}
            final Button boton = addBoton(msg, actionHandler);
            boton.addEventFilter(KeyEvent.ANY, new EventHandler<KeyEvent>(){
            	@Override
                public void handle(KeyEvent event){
                    if(event.getCode() == KeyCode.ENTER && (ventana.tipoVentana == TIPO_AVISO 
                    		|| ventana.tipoVentana == TIPO_ERROR || ventana.tipoVentana == TIPO_ERROR_EXCEPCION)){
                    	/* Ventana de errores no se pueden cerrar con enter */
                    	event.consume();
                    }
                }
            });
            boton.setUserData(BOTON_ACEPTAR);
            return this;
        }
        
        /**
         * Añade un botón de tipo Cancelar
         * @param actionHandler
         * @return 
         */
        public Constructor addBotonCancelar() {
            addBoton(I18N.getTexto("Cancelar"), null);
            return this;
        }

        /**
         * Añade un botón de tipo Cancelar
         * @param actionHandler
         * @return 
         */
        public Constructor addBotonNoCancelar() {
            addBoton(I18N.getTexto("No"), null);
            return this;
        }
        
        /**
         * Añade un botón de tipo Si
         * @param actionHandler
         * @return 
         */
        @SuppressWarnings("rawtypes")
		public Constructor addBotonSi(final EventHandler actionHandler) {
            addBoton(I18N.getTexto("Sí"), actionHandler);
            return this;
        }

        /**
         * Añade un botón de tipo No
         * @param actionHandler
         * @return 
         */
        @SuppressWarnings("rawtypes")
        public Constructor addBotonNo(final EventHandler actionHandler) {
            addBoton(I18N.getTexto("No"), actionHandler);
            return this;
        }

        /**
         * Añade un botón de tipo ver Traza o Detalle
         * @param e
         * @return 
         */
        public Constructor addBotonVerTraza(String msg, Throwable e) {
            if (msg.length() < MSG_MAX_CHARS && e == null){
                return this;
            }
            ventana.btTraza = new Button(I18N.getTexto("Ver Detalle"));
            // Configuramos el botón
            ventana.btTraza.setMinWidth(ANCHO_BOTONES_DIALOGO);
            ventana.btTraza.setMinHeight(ALTO_BOTONES_DIALOGO);

            // Asignamos la acción
            ventana.btTraza.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    if (!ventana.taTraza.isVisible()) {
                        log.debug("BotonVerTraza() - ver traza");
                        ventana.taTraza.setVisible(true);
                        ventana.btTraza.setText(I18N.getTexto("Ocultar Detalle"));
                        
                        // Guardamos los valores originales de ancho y alto de la ventana
                        ventana.alto = ventana.getHeight();
                        ventana.ancho = ventana.getWidth();
                        
                        // redimensionamos la ventana
                        ventana.setWidth(VENTANA_TRAZA_ANCHO);
                        ventana.setHeight(VENTANA_TRAZA_ALTO);                       
                        ventana.centerOnScreen();
                        
                        // redimensionamos el panel de la traza
                        ventana.taTraza.setMinWidth(VENTANA_TRAZA_ANCHO-100);
                        ventana.taTraza.setMaxWidth(VENTANA_TRAZA_ANCHO-100);
                        ventana.taTraza.setMinHeight(VENTANA_TRAZA_ALTO-100);
                        ventana.taTraza.setMaxHeight(VENTANA_TRAZA_ALTO-100);

                    } else {
                        log.debug("BotonVerTraza() - ocultar traza");
                        ventana.taTraza.setVisible(false);
                        ventana.btTraza.setText(I18N.getTexto("Ver Detalle"));
                        
                        // redimensionamos la ventana
                        ventana.setHeight(ventana.alto);
                        ventana.setWidth(ventana.ancho);
                        ventana.centerOnScreen();
                        
                        // redimensionamos el panel de la traza
                        ventana.taTraza.setMinWidth(0);
                        ventana.taTraza.setMaxWidth(0);
                        ventana.taTraza.setMinHeight(0);
                        ventana.taTraza.setMaxHeight(0);
                    }
                }
            });
            ventana.panelBotones.getChildren().add(ventana.btTraza);

            String textoError;
            if(e != null){
	            // intentamos localizar el mensaje de error            
	            ventana.excepcion = e;
	            final StringWriter salida = new StringWriter();
	            try (PrintWriter psalida = new PrintWriter(salida)) {
	                e.printStackTrace(psalida);
	                textoError = salida.toString();
	                ventana.trazaError = textoError;
	            }
            }
            // Añadimos el panel de traza
            ventana.taTraza = new TextArea();
            ventana.taTraza.setText(msg + (ventana.trazaError == null? "" : ("\n\n" + ventana.trazaError)));
            ventana.taTraza.setEditable(false);
            
            ventana.taTraza.setVisible(false); // inicialmente, la traza de la excepción estará oculta
            
            // redimensionamos el panel de la traza para que no ocupe
            ventana.taTraza.setMinWidth(0);
            ventana.taTraza.setMaxWidth(0);
            ventana.taTraza.setMinHeight(0);
            ventana.taTraza.setMaxHeight(0);

            ventana.vBoxMensage.getChildren().add(ventana.taTraza);
            
            return this;
        }

        /**
         * Genera el cuadro de diálogo
         * @param actionHandler
         * @return 
         */
        public VentanaDialogoComponent build() {
            if (ventana.panelBotones.getChildren().size() == 0) {
                log.error("build() - Error cargando pantalla - El panel ha de tener almenos un botón");
                throw new RuntimeException("El panel ha de tener almenos un botón");
            }
                     
            if (ventana.ventanaPadre != null) {
            	// Le ponemos initOwner para que la nueva ventana no
            	// aparezca en la barra de tareas
            	ventana.initOwner(ventana.ventanaPadre);
            	ventana.primaryScene = ventana.ventanaPadre.getScene();
            	if (AppConfig.efectoFondoVentanaModales && ventana.primaryScene != null) {
            		// Damos efecto al fondo
					ventana.primaryScene.getRoot().setEffect(new GaussianBlur(3));
				}
			}
            // Establece el foco
            ventana.panelBotones.getChildren().get(0).requestFocus();
            
            ventana.pulsadoAceptar = false;
            
            return ventana;
        }
    }

    @Override
	public void showAndWait() {
    	Keyboard keyboard = SpringContext.getBean(Keyboard.class);
    	keyboard.close();
		super.showAndWait();
	}

	/**
     * Crea una ventana de Información sin título
     * @param mensaje
     * @param ventanaPadre 
     */
     public static void crearVentanaInfo( String mensaje, Window ventanaPadre) {
         log.debug("crearVentanaInfo() ");
         crearVentanaInfo("", mensaje, ventanaPadre);
     }
     
    /**
     * Crea una ventana de información
     *
     * @param titulo titulo de la ventana
     * @param mensaje mensaje que se mostrará
     * @param ventanaPadre ventana padre
     */
    public static void crearVentanaInfo(String titulo, String mensaje, Window ventanaPadre) {
        log.debug("crearVentanaInfo() - " + titulo);
        crearVentana(titulo, mensaje, TIPO_INFO, false, ventanaPadre, null, false);
    }

    /**
     * Crea una ventana de error
     * @param ventanaPadre ventana padre
     * @param mensaje mensaje que se mostrará
     */
    public static void crearVentanaError(String mensaje, Window ventanaPadre) {
    	crearVentanaError(ventanaPadre, mensaje, null);
    }

    /**
     * Crea una ventana de error
     * @param ventanaPadre ventana padre
     * @param e Excepción de la que se mostrará su traza
     */
    public static void crearVentanaError(Window ventanaPadre, Throwable e) {
        crearVentanaError(ventanaPadre, I18N.getTexto("Lo sentimos, ha ocurrido un error."), e);
    }

    /**
     * Crea una ventana de error pasando una excepción
     * @param mensaje
     * @param ventanaPadre
     * @param e 
     */
    public static void crearVentanaError(Window ventanaPadre, String mensaje, Throwable e) {
    	if(AppConfig.modoDesarrollo && (e == null || e.getMessage() == null || e.getMessage().isEmpty() || (e.getCause() != null && e.getCause().getMessage() == null))){
    		log.debug("crearVentanaError() - Modo Desarrollo activo, mostramos la traza del hilo de ejecución para facilitar el debug");
    		Thread.dumpStack();
    	}
        log.debug("crearVentanaError() - Mensaje: " + mensaje + " // Exception: " + e);
        crearVentana(null, mensaje, TIPO_ERROR_EXCEPCION, false, ventanaPadre, e, true);
    }

    /**
     * Crea una ventana de aviso sin título
     * @param mensaje
     * @param ventanaPadre 
     */
    public static void crearVentanaAviso( String mensaje, Window ventanaPadre) {
        crearVentanaAviso("", mensaje, ventanaPadre);
    }
    
    /**
     * Crea una ventana de aviso
     *
     * @param titulo titulo de la ventana
     * @param mensaje mensaje que se mostrará
     * @param ventanaPadre ventana padre
     */
    public static void crearVentanaAviso(String titulo, String mensaje, Window ventanaPadre) {
        log.debug("crearVentanaAviso() - " + titulo);
        crearVentana(titulo, mensaje, TIPO_AVISO, false, ventanaPadre, null, true);
    }
    

    /**
     * Crea una ventana de confirmación sin título
     * @param mensaje
     * @param ventanaPadre
     * @return 
     */
    public static boolean crearVentanaConfirmacion(String mensaje, Window ventanaPadre) {        
        return crearVentanaConfirmacion("", mensaje, ventanaPadre);        
    }
    
    
    
    /**
     * Crea una ventana de confirmación
     *
     * @param titulo titulo de la ventana
     * @param mensaje mensaje que se mostrará
     * @param ventanaPadre ventana padre
     * @param handlerAceptar handler que lanzaremos si aceptamos la ventan
     * @param handlerCancelar handler que lanzaremos si cancelamos la ventana
     */
    public static boolean crearVentanaConfirmacion(String titulo, String mensaje, Window ventanaPadre) {
        log.debug("crearVentanaConfirmacion() - " + titulo);
       VentanaDialogoComponent ventana = crearVentana(titulo, mensaje, TIPO_CONFIRMACION, true, ventanaPadre, null, false);
       return ventana.isPulsadoAceptar();
    }
    
    public static boolean crearVentanaConfirmacionUnBoton(String mensaje, Window ventanaPadre){
    	 log.debug("crearVentanaConfirmacionUnBoton()" );
         VentanaDialogoComponent ventana = crearVentana(null, mensaje, TIPO_ERROR, false, ventanaPadre, null, false);
         return ventana.isPulsadoAceptar();
    }

    public static VentanaDialogoComponent crearVentana(String titulo, String mensaje, int tipo, boolean mostrarCancelar, Window ventanaPadre, Throwable e, boolean beep) {
    	return crearVentanaEnsureFXThread(titulo, mensaje, tipo, mostrarCancelar, ventanaPadre, e, beep);
    }
    
    protected static VentanaDialogoComponent crearVentanaEnsureFXThread(final String titulo, final String mensaje, final int tipo, final boolean mostrarCancelar, final Window ventanaPadre, final Throwable e, final boolean beep) {
    	if (Platform.isFxApplicationThread()) {
    		return crearVentanaInternal(titulo, mensaje, tipo, mostrarCancelar, ventanaPadre, e, beep);
    	} else {
    		FutureTask<VentanaDialogoComponent> futureTask = new FutureTask<>(new Callable<VentanaDialogoComponent>() {
    			@Override
    			public VentanaDialogoComponent call() throws Exception {
    				return crearVentanaInternal(titulo, mensaje, tipo, mostrarCancelar, ventanaPadre, e, beep);
    			}
    		});
    		Platform.runLater(futureTask);
    		try {
    			return futureTask.get(); //Bloquea hilo si es necesario
    		} catch (InterruptedException | ExecutionException ex) {
    			log.error("crearVentanaEnsureFXThread() - " + ex.getClass().getName() + " - " + ex.getLocalizedMessage(), ex);
    			throw new RuntimeException(ex);
    		} 
    	}
    }
    
	protected static VentanaDialogoComponent crearVentanaInternal(String titulo, String mensaje, int tipo, boolean mostrarCancelar, Window ventanaPadre, Throwable e, boolean beep) {
		if (mensaje == null) {
			mensaje = I18N.getTexto("Error inesperado. Para mas información consulte el log.");
		}
		if (beep) {
			Toolkit.getDefaultToolkit().beep();
		}
		Constructor constructor = new ByLVentanaDialogoComponent.Constructor();
		constructor.crear();
		constructor.setVentanaPadre(ventanaPadre);
		constructor.setTipoAviso(tipo);
		constructor.setTitulo(titulo);
		constructor.setMensaje(mensaje);
		constructor.addBotonAceptar(null);
		if (mostrarCancelar) {
			constructor.addBotonCancelar();
		}
		if (e != null) {
			constructor.addBotonVerTraza(mensaje, e);
		}
		VentanaDialogoComponent ventana = constructor.build();
		if (WAIT_USER_CLOSE) {
			ventana.showAndWait();
		} else {
			ventana.show();
		}
		return ventana;
	}
    
	public static boolean crearVentanaSiNo(String mensaje, Window ventanaPadre){
       VentanaDialogoComponent ventana = crearVentanaSiNoComponente(mensaje, ventanaPadre);
       return ventana.isPulsadoAceptar();
    }
	
	/**
	 * Crea una ventana de confirmación pero con los botones de Si y No.
	 * @param mensaje : Mensaje que se desea mostrar.
	 * @param ventanaPadre : Ventana padre donde mostrar la ventana.
	 * @return VentanaDialogoComponent
	 */
	public static VentanaDialogoComponent crearVentanaSiNoComponente(String mensaje, Window ventanaPadre){
		Constructor constructor = new ByLVentanaDialogoComponent.Constructor();
		constructor.crear();
		constructor.setVentanaPadre(ventanaPadre);
		constructor.setTipoAviso(TIPO_CONFIRMACION);
		constructor.setTitulo("");
		constructor.setMensajeSiNo(mensaje);
		constructor.addBotonSiAceptar(null);
		constructor.addBotonNoCancelar();

		VentanaDialogoComponent ventana = constructor.build();
		if(WAIT_USER_CLOSE){
			ventana.showAndWait();
		}else{
			ventana.show();
		}
		return ventana;
	}

    /**
     * @return the pulsadoAceptar
     */
    public boolean isPulsadoAceptar() {
    	return pulsadoAceptar;
    }
	
    public Scene getPrimaryScene() {
    	return primaryScene;
    }

}
