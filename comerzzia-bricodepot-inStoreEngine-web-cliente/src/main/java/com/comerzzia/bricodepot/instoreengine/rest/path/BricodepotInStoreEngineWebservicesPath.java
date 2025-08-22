package com.comerzzia.bricodepot.instoreengine.rest.path;

public class BricodepotInStoreEngineWebservicesPath {
	
	public static String servicio = null;
	
	public static final String servicioInsertarApunte = "bricocajas/insertarApunte";
	public static final String servicioConsultarCajasMovimientos = "bricocajas/consultarCajasMovimientos";
    public static final String servicioCajaTransferidaMaster = "bricocajas/{uidDiarioCaja}/transferidaMaster";

	 public static void initPath(String basePath){
	    	servicio = basePath;
	    }
	
}
