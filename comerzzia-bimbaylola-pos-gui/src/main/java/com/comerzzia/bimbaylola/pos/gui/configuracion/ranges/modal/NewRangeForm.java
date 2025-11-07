package com.comerzzia.bimbaylola.pos.gui.configuracion.ranges.modal;

import com.comerzzia.pos.core.gui.validation.FormularioGui;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class NewRangeForm extends FormularioGui {

	@NotEmpty(message = "Debe rellenar el campo Id Contador")
	protected String counterId;

	@NotEmpty(message = "Debe rellenar el campo Divisor 1")
	protected String divider1;

	@NotEmpty(message = "Debe rellenar el campo Divisor 2")
	protected String divider2;

	@NotEmpty(message = "Debe rellenar el campo Divisor 3")
	protected String divider3;

	@NotEmpty(message = "Debe rellenar el campo Rango")
	protected String range;

	@Override
	public void limpiarFormulario() {
		counterId = "";
		divider1 = "";
		divider2 = "";
		divider3 = "";
		range = "";
	}

	public String getCounterId() {
		return counterId;
	}

	public String getDivider1() {
		return divider1;
	}

	public String getDivider2() {
		return divider2;
	}

	public String getDivider3() {
		return divider3;
	}

	public String getRange() {
		return range;
	}

	public void setCounterId(String counterId) {
		this.counterId = counterId;
	}

	public void setDivider1(String divider1) {
		this.divider1 = divider1;
	}

	public void setDivider2(String divider2) {
		this.divider2 = divider2;
	}

	public void setDivider3(String divider3) {
		this.divider3 = divider3;
	}

	public void setRange(String range) {
		this.range = range;
	}

}
