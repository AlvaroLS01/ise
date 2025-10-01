package com.comerzzia.bricodepot.backoffice.persistence.datosMovimiento;

import java.util.Date;

// [BRICOD-27]
public class DatosMovimientoBean {

	private Date fecha; // fecha
	private String codAlm;
	private String comentarios;
	private String importe; // importe
	private String importeRepetido; // en la pantalla se obliga a introducir 2 veces el importe para no equivocarse

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getCodAlm() {
		return codAlm;
	}

	public void setCodAlm(String codAlm) {
		this.codAlm = codAlm;
	}

	public String getComentarios() {
		return comentarios;
	}

	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}

	public String getImporte() {
		return importe;
	}

	public void setImporte(String importe) {
		this.importe = importe;
	}

	public String getImporteRepetido() {
		return importeRepetido;
	}

	public void setImporteRepetido(String importeRepetido) {
		this.importeRepetido = importeRepetido;
	}

}
