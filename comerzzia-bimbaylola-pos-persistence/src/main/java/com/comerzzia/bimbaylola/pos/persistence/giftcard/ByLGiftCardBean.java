package com.comerzzia.bimbaylola.pos.persistence.giftcard;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.comerzzia.pos.persistence.giftcard.GiftCardBean;

@Component
@Scope("prototype")
@Primary
public class ByLGiftCardBean extends GiftCardBean {
	
	private boolean tarjetaAbono;
	private String error;
	private Integer estado;
	
	public ByLGiftCardBean() {
		super();
	}

	public boolean isTarjetaAbono() {
		return tarjetaAbono;
	}

	public void setTarjetaAbono(boolean tarjetaAbono) {
		this.tarjetaAbono = tarjetaAbono;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

}
