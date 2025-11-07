package com.comerzzia.bimbaylola.pos.persistence.reservas;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

import org.springframework.stereotype.Component;

@Component
@XmlAccessorType(XmlAccessType.FIELD)
public class ReservasPagoGiftCardKey{

	@XmlTransient
	private String uidActividad;
	@XmlTransient
	private Long idClieAlbaran;
	@XmlTransient
	private Integer linea;

	public String getUidActividad(){
		return uidActividad;
	}

	public void setUidActividad(String uidActividad){
		this.uidActividad = uidActividad == null ? null : uidActividad.trim();
	}

	public Long getIdClieAlbaran(){
		return idClieAlbaran;
	}

	public void setIdClieAlbaran(Long idClieAlbaran){
		this.idClieAlbaran = idClieAlbaran;
	}

	public Integer getLinea(){
		return linea;
	}

	public void setLinea(Integer linea){
		this.linea = linea;
	}
	
}