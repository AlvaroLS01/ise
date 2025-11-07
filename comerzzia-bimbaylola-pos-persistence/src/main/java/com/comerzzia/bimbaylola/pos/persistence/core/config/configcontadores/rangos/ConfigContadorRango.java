package com.comerzzia.bimbaylola.pos.persistence.core.config.configcontadores.rangos;

import java.util.Date;

import com.comerzzia.core.util.fechas.Fechas;
import com.comerzzia.pos.persistence.core.config.configcontadores.parametros.ConfigContadorParametroBean;
import com.comerzzia.pos.util.format.FormatUtil;

public class ConfigContadorRango extends ConfigContadorRangoKey {
	
	/**
	 *
	 */
	private static final long serialVersionUID = -3588559576473617891L;
	public static final String VIGENCIA_CODEMP = "codEmp";
	public static final String VIGENCIA_CODALM = "codAlm";
	public static final String VIGENCIA_CODCAJA = "codCaja";
	
    private String rangoDescripcion;

    private Long rangoInicio;

    private Long rangoFin;

    private Long rangoAviso;

    private Short rangoAvisoIntervalo;

    private Long rangoUltimoAviso;

    private Date rangoFechaInicio;

    private Date rangoFechaFin;

    private Date rangoFechaAviso;

    private Short rangoFechaAvisoIntervalo;

    private Date rangoFechaUltimoAviso;

    private String codemp;

    private String codalm;

    private String codcaja;

    private String rangoCoNumResolucion;

    private Date rangoCoFechaResolucion;

    private String rangoCoClaveTecnica;

    private String rangoCoPref;

	// INICIO ATRIBUTOS
	// PERSONALIZADOS--------------------------------------------

	private String rangoInicioFormateado;

	private String rangoFinFormateado;

	// FIN ATRIBUTOS
	// PERSONALIZADOS-----------------------------------------------
    
    public String getRangoDescripcion() {
        return rangoDescripcion;
    }

    public void setRangoDescripcion(String rangoDescripcion) {
        this.rangoDescripcion = rangoDescripcion == null ? null : rangoDescripcion.trim();
    }

    public Long getRangoInicio() {
        return rangoInicio;
    }

    public void setRangoInicio(Long rangoInicio) {
        this.rangoInicio = rangoInicio;
    }

    public Long getRangoFin() {
        return rangoFin;
    }

    public void setRangoFin(Long rangoFin) {
        this.rangoFin = rangoFin;
    }

    public Long getRangoAviso() {
        return rangoAviso;
    }

    public void setRangoAviso(Long rangoAviso) {
        this.rangoAviso = rangoAviso;
    }

    public Short getRangoAvisoIntervalo() {
        return rangoAvisoIntervalo;
    }

    public void setRangoAvisoIntervalo(Short rangoAvisoIntervalo) {
        this.rangoAvisoIntervalo = rangoAvisoIntervalo;
    }

    public Long getRangoUltimoAviso() {
        return rangoUltimoAviso;
    }

    public void setRangoUltimoAviso(Long rangoUltimoAviso) {
        this.rangoUltimoAviso = rangoUltimoAviso;
    }

    public Date getRangoFechaInicio() {
        return rangoFechaInicio;
    }

    public void setRangoFechaInicio(Date rangoFechaInicio) {
        this.rangoFechaInicio = rangoFechaInicio;
    }

    public Date getRangoFechaFin() {
        return rangoFechaFin;
    }

    public void setRangoFechaFin(Date rangoFechaFin) {
        this.rangoFechaFin = rangoFechaFin;
    }

    public Date getRangoFechaAviso() {
        return rangoFechaAviso;
    }

    public void setRangoFechaAviso(Date rangoFechaAviso) {
        this.rangoFechaAviso = rangoFechaAviso;
    }

    public Short getRangoFechaAvisoIntervalo() {
        return rangoFechaAvisoIntervalo;
    }

    public void setRangoFechaAvisoIntervalo(Short rangoFechaAvisoIntervalo) {
        this.rangoFechaAvisoIntervalo = rangoFechaAvisoIntervalo;
    }

    public Date getRangoFechaUltimoAviso() {
        return rangoFechaUltimoAviso;
    }

    public void setRangoFechaUltimoAviso(Date rangoFechaUltimoAviso) {
        this.rangoFechaUltimoAviso = rangoFechaUltimoAviso;
    }

    public String getCodemp() {
        return codemp;
    }

    public void setCodemp(String codemp) {
        this.codemp = codemp == null ? null : codemp.trim();
    }

    public String getCodalm() {
        return codalm;
    }

    public void setCodalm(String codalm) {
        this.codalm = codalm == null ? null : codalm.trim();
    }

    public String getCodcaja() {
        return codcaja;
    }

    public void setCodcaja(String codcaja) {
        this.codcaja = codcaja == null ? null : codcaja.trim();
    }

    public String getRangoCoNumResolucion() {
        return rangoCoNumResolucion;
    }

    public void setRangoCoNumResolucion(String rangoCoNumResolucion) {
        this.rangoCoNumResolucion = rangoCoNumResolucion == null ? null : rangoCoNumResolucion.trim();
    }

    public Date getRangoCoFechaResolucion() {
        return rangoCoFechaResolucion;
    }

    public void setRangoCoFechaResolucion(Date rangoCoFechaResolucion) {
        this.rangoCoFechaResolucion = rangoCoFechaResolucion;
    }

    public String getRangoCoClaveTecnica() {
        return rangoCoClaveTecnica;
    }

    public void setRangoCoClaveTecnica(String rangoCoClaveTecnica) {
        this.rangoCoClaveTecnica = rangoCoClaveTecnica == null ? null : rangoCoClaveTecnica.trim();
    }

    public String getRangoCoPref() {
        return rangoCoPref;
    }

    public void setRangoCoPref(String rangoCoPref) {
        this.rangoCoPref = rangoCoPref == null ? null : rangoCoPref.trim();
    }
    
 // INICIO MÉTODOS PERSONALIZADOS--------------------------------------------
 	public Date getProximoAvisoFecha() {
 		Date res = null;
 		if (this.getRangoFechaAviso() != null) {
 			if (this.getRangoAvisoIntervalo() != null) {
 				res = getAvisoFechaRecursivo(0);
 			}
 			else {
 				res = this.getRangoFechaAviso();
 			}

 		}
 		return res;
 	}
 	
 	protected Date getAvisoFechaRecursivo(int iteracion) {
 	    Date res;
 	    // CAMBIADO: usar RANGO_AVISO_INTERVALO en vez de RANGO_FECHA_AVISO_INTERVALO
 	    Date proximoAviso = Fechas.sumaDias(this.getRangoFechaAviso(), this.getRangoAvisoIntervalo() * iteracion);
 	    
 	    if (Fechas.equalsDate(proximoAviso, new Date()) || new Date().before(proximoAviso)) {
 	        res = proximoAviso;
 	    } else {
 	        res = getAvisoFechaRecursivo(iteracion + 1);
 	    }

 	    return res;
 	}

 	public String getRangoFechaInicioAsLocale() {

 		Date fecha = getRangoFechaInicio();
 		if (fecha == null) {
 			fecha = new Date();
 		}
 		String fechaAsLocale = FormatUtil.getInstance().formateaFechaCorta(fecha);
 		return fechaAsLocale;
 	}

 	public String getRangoFechaFinAsLocale() {

 		Date fecha = getRangoFechaFin();
 		if (fecha == null) {
 			fecha = new Date();
 		}
 		String fechaAsLocale = FormatUtil.getInstance().formateaFechaCorta(fecha);
 		return fechaAsLocale;
 	}

 	public String getRangoInicioFormateado() {
 		return rangoInicioFormateado;
 	}

 	public void setRangoInicioFormateado(String rangoInicioFormateado) {
 		this.rangoInicioFormateado = rangoInicioFormateado;
 	}

 	public String getRangoFinFormateado() {
 		return rangoFinFormateado;
 	}

 	public void setRangoFinFormateado(String rangoFinFormateado) {
 		this.rangoFinFormateado = rangoFinFormateado;
 	}

 	public void formatearRangos(String divisor, ConfigContadorParametroBean parametroRango, String separador) {
 		rangoInicioFormateado = String.valueOf(rangoInicio);
 		rangoFinFormateado = String.valueOf(rangoFin);
 		if (parametroRango != null) {
 			if (parametroRango.getDireccionRelleno() != null && !parametroRango.getDireccionRelleno().isEmpty()) {
 				if ("I".equals(parametroRango.getDireccionRelleno().toUpperCase())) {
 					while (rangoInicioFormateado.length() < parametroRango.getLongitudMaxima()) {
 						rangoInicioFormateado = parametroRango.getCaracterRelleno() + rangoInicioFormateado;
 					}
 					while (rangoFinFormateado.length() < parametroRango.getLongitudMaxima()) {
 						rangoFinFormateado = parametroRango.getCaracterRelleno() + rangoFinFormateado;
 					}
 				}
 				else {
 					while (rangoInicioFormateado.length() < parametroRango.getLongitudMaxima()) {
 						rangoInicioFormateado = rangoInicioFormateado + parametroRango.getCaracterRelleno();
 					}
 					while (rangoFinFormateado.length() < parametroRango.getLongitudMaxima()) {
 						rangoFinFormateado = rangoFinFormateado + parametroRango.getCaracterRelleno();
 					}
 				}
 			}
 		}
 		if ("".equals(separador)) {
 			separador = "/";
 		}
 		rangoInicioFormateado = divisor + separador + rangoInicioFormateado;
 		rangoFinFormateado = divisor + separador + rangoFinFormateado;

 	}
 	
 	public String getRangoCoFechaResolucionAsLocale() {

 		Date fecha = getRangoCoFechaResolucion();
 		if (fecha == null) {
 			fecha = new Date();
 		}
 		String fechaAsLocale = FormatUtil.getInstance().formateaFechaCorta(fecha);
 		return fechaAsLocale;
 	}

 	// FIN MÉTODOS PERSONALIZADOS-----------------------------------------------
}