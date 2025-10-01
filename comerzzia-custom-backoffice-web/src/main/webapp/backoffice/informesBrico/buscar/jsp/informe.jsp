<%--

    ComerZZia 3.0

    Copyright (c) 2008-2015 Comerzzia, S.L.  All Rights Reserved.

    THIS WORK IS  SUBJECT  TO  SPAIN  AND  INTERNATIONAL  COPYRIGHT  LAWS  AND
    TREATIES.   NO  PART  OF  THIS  WORK MAY BE  USED,  PRACTICED,  PERFORMED
    COPIED, DISTRIBUTED, REVISED, MODIFIED, TRANSLATED,  ABRIDGED, CONDENSED,
    EXPANDED,  COLLECTED,  COMPILED,  LINKED,  RECAST, TRANSFORMED OR ADAPTED
    WITHOUT THE PRIOR WRITTEN CONSENT OF COMERZZIA, S.L. ANY USE OR EXPLOITATION
    OF THIS WORK WITHOUT AUTHORIZATION COULD SUBJECT THE PERPETRATOR TO
    CRIMINAL AND CIVIL LIABILITY.

    CONSULT THE END USER LICENSE AGREEMENT FOR INFORMATION ON ADDITIONAL
    RESTRICTIONS.

--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="cmz" uri="http://comerzzia.com/taglib"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:useBean id="permisos"
	class="com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean"
	scope="request" />
<jsp:useBean id="trabajoInforme"
	type="com.comerzzia.core.model.informes.TrabajoInformeBean"
	scope="session" />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Comerzzia</title>

<cmz:head />
<link rel="stylesheet" type="text/css" media="screen,projection,print"
	href="comun/css/calendario.css" />

<script type="text/javascript" language="javascript"
	src="comun/js/comun.js"></script>
<script type="text/javascript" language="javascript"
	src="comun/js/ayudas.js"></script>
<script type="text/javascript" language="javascript"
	src="comun/js/validacion.js"></script>
<script type="text/javascript" language="javascript"
	src="comun/js/calendario.js"></script>

<script type="text/javascript">
	    function inicio() {
	        pintaCalendario("fechaDesde", "imgFechaDesde");
            pintaCalendario("fechaHasta", "imgFechaHasta");
            pintaCalendario("fechaEmisionUsoTarjeta", "imgFechaEmisionUsoTarjeta");
            pintaCalendario("fechaRecepcionEmisionTarjeta", "imgFechaRecepcionEmisionTarjeta");
	    }
	    
	    function checkForm() {
            if (!esValido("fechaDesde", "FECHA", false)) {
                return false;
            }
            
            if (!esValido("fechaHasta", "FECHA", false)) {
                return false;
            }
            
            if (!esValido("codTieDesde", "AYUDA", false, "desTieDesde", "TIENDAS")) {
                return false;
            }
            
            if (!esValido("codTieHasta", "AYUDA", false, "desTieHasta", "TIENDAS")) {
                return false;
            }
            
            
			if(!esValido("fechaEmisionUsoTarjeta", "FECHA", false)) {
				return false;
			}
	
			if(!esValido("fechaRecepcionEmisionTarjeta", "FECHA", false)) {
				return false;
			}
			
            if (!esValido("cajero", "TEXTO", false)) {
                return false;
            }

			if (!esValido("codFormaDePago", "AYUDA", false, "desformaDePago", "MEDIOS_PAGO_VENTAS")) {
                return false;
            }

			if (!esValido("codTipoTarjeta", "AYUDA", false, "destipoTarjeta", "TIPOS_TARJETA")) {
                return false;
            }

			if (!esValido("documento", "TEXTO", false)) {
                return false;
            }
            
			if (!esValido("facturaDesde", "TEXTO", false)) {
                return false;
            }
            
			if (!esValido("facturaHasta", "TEXTO", false)) {
                return false;
            }

			if (!esValido("codPromocion", "AYUDA", false, "desPromocion", "PROMOCIONES")) {
                return false;
            }

			if (!esValido("codColectivo", "AYUDA", false, "desColectivo", "COLECTIVOS")) {
                return false;
            }

			if (!esValido("codEtiqueta", "AYUDA", false, "desEtiqueta", "ETIQUETAS")) {
                return false;
            }

			if (!esValido("codCampania", "AYUDA", false, "desCampania", "CAMPANIAS_MKT")) {
                return false;
            }
		
			return true;
	    }
	
	    function imprimir(idVersion) {
	    	if(checkForm()) {
	            document.getElementById("accion").value = "informes.lstInformesBrico";
	            document.getElementById("operacion").value = "imprimir";
		    	document.getElementById("idVersion").value = idVersion;
	    		document.getElementById("frmDatos").target = "_blank";
	    		document.getElementById("frmDatos").submit();
	    		document.getElementById("frmDatos").target = "_self";
	    	}
	    }

    </script>
</head>

<body onload="inicio();">
	<cmz:main>
		<cmz:panelCMZ>
			<cmz:cabeceraPanelCMZ titulo="${permisos.accionMenu.titulo}"
				icono="${permisos.accionMenu.icono}">
				<cmz:acciones numAccionesVisibles="2">
					<cmz:accion titulo="Ver Permisos"
						icono="comun/images/iconos/i-key.gif"
						descripcion="Ver permisos efectivos del usuario"
						onclick="verPermisos(${permisos.accionMenu.idAccion})" />
					<c:if test="${permisos.puedeAdministrar}">
						<cmz:accion titulo="Administrar Permisos"
							icono="comun/images/iconos/i-admin_permisos.gif"
							descripcion="Administración de permisos"
							onclick="adminPermisos(${permisos.accionMenu.idAccion})" />
					</c:if>
				</cmz:acciones>
			</cmz:cabeceraPanelCMZ>

			<cmz:cuerpoPanelCMZ>
				<cmz:mensaje />
				<form id="frmDatos" name="frmDatos" action="informesBrico"
					method="POST" target="_self">
					<input id="accion" name="accion" type="hidden" value="" /> <input
						id="operacion" name="operacion" type="hidden" value="" /> <input
						id="idVersion" name="idVersion" type="hidden" value="0" />
					<cmz:panel>
						<cmz:cabeceraPanel titulo="Informes">
							<table cellpadding="2px" cellspacing="2px" border="0px">
								<tr>
									<td><cmz:etiqueta titulo="Informe GBS Global" />:</td>
									<td><cmz:etiqueta
											titulo="Parámetros: fecha desde/hasta, tienda desde/hasta y cajero" /></td>
								</tr>
								<tr>
									<td><cmz:etiqueta titulo="Informe GBS Formas de Pago" />:</td>
									<td><cmz:etiqueta
											titulo="Parámetros: fecha desde/hasta, cajero, fecha emisión/uso tarjeta, fecha recepción/emisión tarjeta, forma de pago y tipo de tarjeta" /></td>
								</tr>
								<tr>
									<td><cmz:etiqueta
											titulo="Informe Ventas Superiores a 2000 € en efectivo" />:</td>
									<td><cmz:etiqueta
											titulo="Parámetros: Periodo (se consulta el año entero indicado en el campo fecha desde), documento" /></td>
								</tr>
								<tr>
									<td><cmz:etiqueta titulo="Informe Promociones" />:</td>
									<td><cmz:etiqueta
											titulo="Parámetros: fecha desde/hasta, tienda desde/hasta, documento, promoción, colectivo y campaña" /></td>
								</tr>
							</table>
						</cmz:cabeceraPanel>
					</cmz:panel>
					<cmz:panel>
						<cmz:cabeceraPanel titulo="Parámetros"></cmz:cabeceraPanel>
						<cmz:cuerpoPanel>
							<table cellpadding="2px" cellspacing="2px" border="0px">
								<tr>
									<td><cmz:etiqueta titulo="Fecha desde" />:</td>
									<td><cmz:campoFecha id="fechaDesde"
											mostrarCheckbox="false">
											<fmt:formatDate pattern="dd/MM/yyyy" value="${fechaDesde}" />
										</cmz:campoFecha></td>
								</tr>
								<tr>
									<td><cmz:etiqueta titulo="Fecha hasta" />:</td>
									<td><cmz:campoFecha id="fechaHasta"
											mostrarCheckbox="false">
											<fmt:formatDate pattern="dd/MM/yyyy" value="${fechaHasta}" />
										</cmz:campoFecha></td>
								</tr>
								<tr>
									<td><cmz:etiqueta titulo="Tienda desde" />:</td>
									<td><cmz:ayuda nombreAyuda="TIENDAS">
											<cmz:codigoAyuda idCodigo="codTieDesde" anchuraCodigo="50px"
												longitudMaximaCodigo="4" />
											<cmz:descripcionAyuda idDescripcion="desTieDesde"
												anchuraDescripcion="400px" />
										</cmz:ayuda></td>
								</tr>
								<tr>
									<td><cmz:etiqueta titulo="Tienda hasta" />:</td>
									<td><cmz:ayuda nombreAyuda="TIENDAS">
											<cmz:codigoAyuda idCodigo="codTieHasta" anchuraCodigo="50px"
												longitudMaximaCodigo="4" />
											<cmz:descripcionAyuda idDescripcion="desTieHasta"
												anchuraDescripcion="400px" />
										</cmz:ayuda></td>
								</tr>

								<tr>
									<td><cmz:etiqueta titulo="Caja desde" />:</td>
									<td><cmz:campoTexto id="cajaDesde" anchura="50px"></cmz:campoTexto></td>
								</tr>
								<tr>
									<td><cmz:etiqueta titulo="Caja hasta" />:</td>
									<td><cmz:campoTexto id="cajaHasta" anchura="50px"></cmz:campoTexto></td>
								</tr>

								<tr>
									<td><cmz:etiqueta titulo="Fecha Recepción/Emisión Tarjeta" />:</td>
									<td><cmz:campoFecha id="fechaRecepcionEmisionTarjeta"
											mostrarCheckbox="false" /></td>
								</tr>
								<tr>
									<td><cmz:etiqueta titulo="Fecha emisión/uso de tarjeta" />:</td>
									<td><cmz:campoFecha id="fechaEmisionUsoTarjeta"
											mostrarCheckbox="false" /></td>
								</tr>
								<tr>
		                        	<td><cmz:etiqueta titulo="Cajero"/>:</td>
		                        	<td>
										<cmz:ayuda requerido="false" nombreAyuda="USUARIOS" >
											<cmz:codigoAyuda idCodigo="cajero" longitudMaximaCodigo="10"></cmz:codigoAyuda>	
											<cmz:descripcionAyuda idDescripcion="desCajero" anchuraDescripcion="150px" ></cmz:descripcionAyuda>
										</cmz:ayuda>
								  	</td>
		                        </tr>
								<tr>
									<td><cmz:etiqueta titulo="Forma de Pago" />:</td>
									<td><cmz:ayuda nombreAyuda="MEDIOS_PAGO_VENTAS">
											<cmz:codigoAyuda idCodigo="codFormaDePago"
												longitudMaximaCodigo="4" anchuraCodigo="50px" />
											<cmz:descripcionAyuda idDescripcion="desFormaDePago"
												anchuraDescripcion="200px" />
										</cmz:ayuda></td>
								</tr>
								<tr>
									<td><cmz:etiqueta titulo="Tipo Tarjeta" />:</td>
									<td><cmz:ayuda nombreAyuda="TIPOS_TARJETA">
											<cmz:codigoAyuda idCodigo="codTipoTarjeta"
												longitudMaximaCodigo="20" anchuraCodigo="130px" />
											<cmz:descripcionAyuda idDescripcion="desTipoTarjeta"
												anchuraDescripcion="200px" />
										</cmz:ayuda></td>
								</tr>
								<tr>
									<td><cmz:etiqueta titulo="Documento" />:</td>
									<td><cmz:campoTexto id="documento" anchura="80px"></cmz:campoTexto></td>
								</tr>
								<tr>
									<td><cmz:etiqueta titulo="Factura desde" />:</td>
									<td><cmz:campoTexto id="facturaDesde" anchura="150px"></cmz:campoTexto></td>
								</tr>
								<tr>
									<td><cmz:etiqueta titulo="Factura hasta" />:</td>
									<td><cmz:campoTexto id="facturaHasta" anchura="150px"></cmz:campoTexto></td>
								</tr>
								<tr>
									<td><cmz:etiqueta titulo="Promocion" />:</td>
									<td><cmz:ayuda nombreAyuda="PROMOCIONES">
											<cmz:codigoAyuda idCodigo="codPromocion"
												longitudMaximaCodigo="4" anchuraCodigo="50px" />
											<cmz:descripcionAyuda idDescripcion="desPromocion"
												anchuraDescripcion="200px" />
										</cmz:ayuda></td>
								</tr>
								<tr>
									<td><cmz:etiqueta titulo="Colectivo" />:</td>
									<td><cmz:ayuda nombreAyuda="COLECTIVOS">
											<cmz:codigoAyuda idCodigo="codColectivo"
												longitudMaximaCodigo="4" anchuraCodigo="50px" />
											<cmz:descripcionAyuda idDescripcion="desColectivo"
												anchuraDescripcion="200px" />
										</cmz:ayuda></td>
								</tr>
								<tr>
									<td><cmz:etiqueta titulo="Etiqueta" />:</td>
									<td><cmz:ayuda nombreAyuda="ETIQUETAS">
											<cmz:codigoAyuda idCodigo="codEtiqueta"
												longitudMaximaCodigo="4" anchuraCodigo="50px" />
											<cmz:descripcionAyuda idDescripcion="desEtiqueta"
												anchuraDescripcion="200px" />
										</cmz:ayuda></td>
								</tr>
								<tr>
									<td><cmz:etiqueta titulo="Campaña" />:</td>
									<td><cmz:ayuda nombreAyuda="CAMPANIAS_MKT">
											<cmz:codigoAyuda idCodigo="codCampania"
												longitudMaximaCodigo="4" anchuraCodigo="50px" />
											<cmz:descripcionAyuda idDescripcion="desCampania"
												anchuraDescripcion="200px" />
										</cmz:ayuda></td>
								</tr>
							</table>
						</cmz:cuerpoPanel>
					</cmz:panel>
					<c:import url="/core/informes/jsp/versiones.jsp"></c:import>
				</form>
			</cmz:cuerpoPanelCMZ>
		</cmz:panelCMZ>
	</cmz:main>
</body>
</html>
