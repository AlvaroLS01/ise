package com.comerzzia.bimbaylola.pos.gui;

import org.apache.log4j.Logger;

import com.comerzzia.bimbaylola.pos.gui.componentes.ventanaCarga.ByLVentanaEspera;
import com.comerzzia.pos.core.gui.BackgroundTaskException;

import javafx.concurrent.Task;

public abstract class ByLBackgroundTask<V> extends Task<V>{
	private static Logger log = Logger.getLogger(ByLBackgroundTask.class);
	
    protected boolean mostrarVentanaCargando = true;
    protected Thread currentThread;
    
    public ByLBackgroundTask(){
    }
    
    public ByLBackgroundTask(boolean mostrarVentanaCargando){
    	this();
		this.mostrarVentanaCargando = mostrarVentanaCargando;
    }

    public void start(String mensaje) {
    	ByLVentanaEspera.setMensaje(mensaje);
    	currentThread = new Thread(this); //TODO Deberian reutilizarse de un ThreadPool (Executors.newCachedThreadPool().submit(this))
    	currentThread.start();
    }

    /**
     * Igual que Task.getException() pero con casting a com.comerzzia.pos.util.exception.Exception. Si
     * getException() no es de tipo com.comerzzia.pos.util.exception.Exception, se devolverá null
     * @return Task.getException() con casting a com.comerzzia.pos.util.exception.Exception o una nueva BackgroundTaskException que engloba a Task.getException().
     */
    public com.comerzzia.pos.util.exception.Exception getCMZException(){
        final Throwable throwable = getException();
        if(throwable instanceof com.comerzzia.pos.util.exception.Exception){
            return (com.comerzzia.pos.util.exception.Exception) throwable;
        }
        
        return new BackgroundTaskException(throwable.getMessage(), throwable);
    }
    
    @Override
    protected void failed() {
        super.failed();
        cerrarVentanaCargando();
    }

    @Override
    protected void cancelled() {
        super.cancelled();
        cerrarVentanaCargando();
    }

    @Override
    protected void succeeded() {
        super.succeeded();
        cerrarVentanaCargando();
    }

    @Override
    protected void running() {
        super.running();
        mostrarVentanaCargando();
    }
    
    protected void mostrarVentanaCargando(){
    	if(mostrarVentanaCargando){
            ByLVentanaEspera.mostrar();
    	}
    }
    
    protected void cerrarVentanaCargando(){
    	log.trace("cerrarVentanaCargando()");
    	if(mostrarVentanaCargando){
            ByLVentanaEspera.cerrar();
    	}
    }
    
}
