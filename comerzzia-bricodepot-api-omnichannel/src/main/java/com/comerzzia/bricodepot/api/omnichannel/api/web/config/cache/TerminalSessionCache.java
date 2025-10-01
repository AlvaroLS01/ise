package com.comerzzia.bricodepot.api.omnichannel.api.web.config.cache;

import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;

import com.comerzzia.core.basketcalculator.model.usuarios.UsuarioBean;
import com.comerzzia.core.basketcalculator.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.basketcalculator.util.i18n.Translation;
import com.comerzzia.core.servicios.sesion.IDatosSesion;
import com.comerzzia.pos.services.core.sesion.Sesion;

public class TerminalSessionCache {
   private static Logger log = Logger.getLogger(TerminalSessionCache.class);
   
   @Autowired
   CacheManager cacheManager;
   
   @Autowired
   protected ModelMapper modelMapper;
   
   @Cacheable(cacheNames="terminalSessions", key = "new org.springframework.cache.interceptor.SimpleKey(#datosSesion.user)")
   public Sesion getTerminalSession(IDatosSesion datosSesion) throws Exception {        
      log.info("getTerminalSession() - Creating new session user'" + datosSesion.getUser() + "'...");
      return new Sesion(initDatosSesion((com.comerzzia.core.servicios.sesion.DatosSesionBean) datosSesion));
   }
      
   @Scheduled(fixedRate = 300000)
   public void clearSessionCache() {
	   log.debug("clearSessionCache() - Clean session cache");
	   cacheManager.getCache("terminalSessions").clear();
   }
   
   protected DatosSesionBean initDatosSesion(com.comerzzia.core.servicios.sesion.DatosSesionBean datosSesion) {
	   DatosSesionBean sessionData = new DatosSesionBean();
	   sessionData.setAtributos(datosSesion.getAtributos());
	   sessionData.setCodEmpresa(datosSesion.getCodEmpresa());
	   sessionData.setDesglose1Activo(datosSesion.isDesglose1Activo());
	   sessionData.setDesglose2Activo(datosSesion.isDesglose2Activo());
	   sessionData.setLocale(datosSesion.getLocale());
	   sessionData.setTituloDesglose1(datosSesion.getTituloDesglose1());
	   sessionData.setTituloDesglose2(datosSesion.getTituloDesglose2());
	   Translation translation = new Translation(datosSesion.getLocale());
	   sessionData.setTranslation(translation);
	   sessionData.setUidActividad(datosSesion.getUidActividad());
	   sessionData.setUidInstancia(datosSesion.getUidInstancia());
	   sessionData.setUser(datosSesion.getUser());
	   sessionData.setUserId(datosSesion.getUserId());
	   sessionData.setUsuario(modelMapper.map(datosSesion.getUsuario(), UsuarioBean.class));
	   return sessionData;
   }
   
}
