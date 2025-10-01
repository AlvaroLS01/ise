package com.comerzzia.bricodepot.backoffice.persistence.movimientos.retirada;

import java.util.Objects;

public class DetMovRetiradaKey {
    private String uidActividad;

    private String uidDiarioCaja;

    private Integer linea;

    public String getUidActividad() {
        return uidActividad;
    }

    public void setUidActividad(String uidActividad) {
        this.uidActividad = uidActividad == null ? null : uidActividad.trim();
    }

    public String getUidDiarioCaja() {
        return uidDiarioCaja;
    }

    public void setUidDiarioCaja(String uidDiarioCaja) {
        this.uidDiarioCaja = uidDiarioCaja == null ? null : uidDiarioCaja.trim();
    }

    public Integer getLinea() {
        return linea;
    }

    public void setLinea(Integer linea) {
        this.linea = linea;
    }

	@Override
	public int hashCode() {
		return Objects.hash(linea, uidActividad, uidDiarioCaja);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DetMovRetiradaKey other = (DetMovRetiradaKey) obj;
		return Objects.equals(linea, other.linea) && Objects.equals(uidActividad, other.uidActividad)
				&& Objects.equals(uidDiarioCaja, other.uidDiarioCaja);
	}
    
    
}