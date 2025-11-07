package com.comerzzia.bimbaylola.pos.persistence.core.config.configcontadores;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.comerzzia.bimbaylola.pos.persistence.core.config.configcontadores.rangos.ConfigContadorRango;
import com.comerzzia.pos.persistence.core.config.configcontadores.ConfigContadorBean;

@Component
@Primary
public class ByLConfigContadorBean extends ConfigContadorBean{

	private boolean rangosCargados = false;
	
	private List<ConfigContadorRango> rangos = new ArrayList<ConfigContadorRango>();
	
	public boolean isRangosCargados() {
		return rangosCargados;
	}

	public void setRangosCargados(boolean rangosCargados) {
		this.rangosCargados = rangosCargados;
	}

	public List<ConfigContadorRango> getRangos() {
		return rangos;
	}

	public void setRangos(List<ConfigContadorRango> rangos) {
		this.rangos = rangos;
	}
	

	public ConfigContadorRango getRango(String idRango){
		ConfigContadorRango res = null;
		for (ConfigContadorRango rango: this.getRangos()) {
			if(rango.getIdRango().equals(idRango)){
				res = rango;
				break;
			}
		}
		return res;
	}
	
	public List<ConfigContadorRango> getRangoFechasActivas(){
		List<ConfigContadorRango> res = new ArrayList<ConfigContadorRango>();
		Date date = new Date();
		for (ConfigContadorRango rango : this.getRangos()) {
			if(rango.getRangoFechaInicio().before(date) && rango.getRangoFechaFin().after(date)){
				res.add(rango);
			}
		}
		
		return res;
	}
}
