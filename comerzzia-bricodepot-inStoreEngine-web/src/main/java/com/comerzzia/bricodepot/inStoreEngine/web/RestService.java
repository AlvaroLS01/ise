package com.comerzzia.bricodepot.inStoreEngine.web;

import java.util.Set;

import javax.ws.rs.ApplicationPath;

import com.comerzzia.bricodepot.inStoreEngine.rest.BricodepotServicioRestCajas;

@ApplicationPath("/ws")
public class RestService extends com.comerzzia.instoreengine.rest.RestService {

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = super.getClasses();
		classes.add(BricodepotServicioRestCajas.class);
		return classes;
	}

}
