package com.comerzzia.bricodepot.backoffice.persistence.general.clientes;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "bricodepotClienteBean")
public class BricodepotClienteBean {

	private int estadoBean;
	private String codCliente;
	private String desCliente;
	private String nombreComercial;
	private String domicilio;
	private String poblacion;
	private String provincia;
	private String cp;
	private String telefono1;
	private String telefono2;
	private String fax;
	private String codpais;
	private String personaContacto;
	private String email;
	private String cif;
	private Long idTratImpuestos;
	private Long IdMedioPagoVencimiento;
	private String codtar;
	private String codsec;
	private String banco;
	private String bancoDomicilio;
	private String bancoPoblacion;
	private String ccc;
	private String observaciones;
	private Boolean activo;
	private Date fechaAlta;
	private Date fechaBaja;
	private String codcliFactura;
	private Double riesgoConcedido;
	private String localidad;
	private String pais;
	private String tipoIdentificacion;
	private String deposito;
	private String codlengua;
	/* Personalizado */
	private int resultado; // BRICO-253 resultado de update/delete, para simular comportamiento de servicio POS
	/* Fin personalizado */

	private String codTipoIden;
	private String desTipoIden;
	private String codTratImp;
	private String desTratImp;
	private String codMedioPago;
	private String desMedioPagoVencimiento;
	private String desTar;
	private String desSec;
	private String codAlmacenTienda;
	private String desAlmacenTienda;
	private Long idFidelizado;
	private String deslengua;

	public BricodepotClienteBean() {

	}

	public int getEstadoBean() {
		return estadoBean;
	}

	public void setEstadoBean(int estadoBean) {
		this.estadoBean = estadoBean;
	}

	public String getCodCliente() {
		return codCliente;
	}

	public void setCodCliente(String codCliente) {
		this.codCliente = codCliente;
	}

	public String getDesCliente() {
		return desCliente;
	}

	public void setDesCliente(String desCliente) {
		this.desCliente = desCliente;
	}

	public String getNombreComercial() {
		return nombreComercial;
	}

	public void setNombreComercial(String nombreComercial) {
		this.nombreComercial = nombreComercial;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public String getPoblacion() {
		return poblacion;
	}

	public void setPoblacion(String poblacion) {
		this.poblacion = poblacion;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getCp() {
		return cp;
	}

	public void setCp(String cp) {
		this.cp = cp;
	}

	public String getTelefono1() {
		return telefono1;
	}

	public void setTelefono1(String telefono1) {
		this.telefono1 = telefono1;
	}

	public String getTelefono2() {
		return telefono2;
	}

	public void setTelefono2(String telefono2) {
		this.telefono2 = telefono2;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getCodpais() {
		return codpais;
	}

	public void setCodpais(String codpais) {
		this.codpais = codpais;
	}

	public String getPersonaContacto() {
		return personaContacto;
	}

	public void setPersonaContacto(String personaContacto) {
		this.personaContacto = personaContacto;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCif() {
		return cif;
	}

	public void setCif(String cif) {
		this.cif = cif;
	}

	public Long getIdTratImpuestos() {
		return idTratImpuestos;
	}

	public void setIdTratImpuestos(Long idTratImpuestos) {
		this.idTratImpuestos = idTratImpuestos;
	}

	public Long getIdMedioPagoVencimiento() {
		return IdMedioPagoVencimiento;
	}

	public void setIdMedioPagoVencimiento(Long idMedioPagoVencimiento) {
		IdMedioPagoVencimiento = idMedioPagoVencimiento;
	}

	public String getCodtar() {
		return codtar;
	}

	public void setCodtar(String codtar) {
		this.codtar = codtar;
	}

	public String getCodsec() {
		return codsec;
	}

	public void setCodsec(String codsec) {
		this.codsec = codsec;
	}

	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public String getBancoDomicilio() {
		return bancoDomicilio;
	}

	public void setBancoDomicilio(String bancoDomicilio) {
		this.bancoDomicilio = bancoDomicilio;
	}

	public String getBancoPoblacion() {
		return bancoPoblacion;
	}

	public void setBancoPoblacion(String bancoPoblacion) {
		this.bancoPoblacion = bancoPoblacion;
	}

	public String getCcc() {
		return ccc;
	}

	public void setCcc(String ccc) {
		this.ccc = ccc;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public Date getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public Date getFechaBaja() {
		return fechaBaja;
	}

	public void setFechaBaja(Date fechaBaja) {
		this.fechaBaja = fechaBaja;
	}

	public String getCodcliFactura() {
		return codcliFactura;
	}

	public void setCodcliFactura(String codcliFactura) {
		this.codcliFactura = codcliFactura;
	}

	public Double getRiesgoConcedido() {
		return riesgoConcedido;
	}

	public void setRiesgoConcedido(Double riesgoConcedido) {
		this.riesgoConcedido = riesgoConcedido;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}

	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}

	public String getDeposito() {
		return deposito;
	}

	public void setDeposito(String deposito) {
		this.deposito = deposito;
	}

	public String getCodlengua() {
		return codlengua;
	}

	public void setCodlengua(String codlengua) {
		this.codlengua = codlengua;
	}

	public int getResultado() {
		return resultado;
	}

	public void setResultado(int resultado) {
		this.resultado = resultado;
	}

	public String getCodTipoIden() {
		return codTipoIden;
	}

	public void setCodTipoIden(String codTipoIden) {
		this.codTipoIden = codTipoIden;
	}

	public String getDesTipoIden() {
		return desTipoIden;
	}

	public void setDesTipoIden(String desTipoIden) {
		this.desTipoIden = desTipoIden;
	}

	public String getCodTratImp() {
		return codTratImp;
	}

	public void setCodTratImp(String codTratImp) {
		this.codTratImp = codTratImp;
	}

	public String getDesTratImp() {
		return desTratImp;
	}

	public void setDesTratImp(String desTratImp) {
		this.desTratImp = desTratImp;
	}

	public String getCodMedioPago() {
		return codMedioPago;
	}

	public void setCodMedioPago(String codMedioPago) {
		this.codMedioPago = codMedioPago;
	}

	public String getDesMedioPagoVencimiento() {
		return desMedioPagoVencimiento;
	}

	public void setDesMedioPagoVencimiento(String desMedioPagoVencimiento) {
		this.desMedioPagoVencimiento = desMedioPagoVencimiento;
	}

	public String getDesTar() {
		return desTar;
	}

	public void setDesTar(String desTar) {
		this.desTar = desTar;
	}

	public String getDesSec() {
		return desSec;
	}

	public void setDesSec(String desSec) {
		this.desSec = desSec;
	}

	public String getCodAlmacenTienda() {
		return codAlmacenTienda;
	}

	public void setCodAlmacenTienda(String codAlmacenTienda) {
		this.codAlmacenTienda = codAlmacenTienda;
	}

	public String getDesAlmacenTienda() {
		return desAlmacenTienda;
	}

	public void setDesAlmacenTienda(String desAlmacenTienda) {
		this.desAlmacenTienda = desAlmacenTienda;
	}

	public Long getIdFidelizado() {
		return idFidelizado;
	}

	public void setIdFidelizado(Long idFidelizado) {
		this.idFidelizado = idFidelizado;
	}

	public String getDeslengua() {
		return deslengua;
	}

	public void setDeslengua(String deslengua) {
		this.deslengua = deslengua;
	}

}
