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
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<jsp:useBean id="formularioVariables"
	type="com.comerzzia.web.core.variables.ui.FormularioVariablesBean"
	scope="session" />
<jsp:useBean id="permisos"
	class="com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean"
	scope="request" />
<jsp:useBean id="variables"
	class="com.comerzzia.core.servicios.variables.Variables"
	scope="request" />

<c:set var="variables" value="${formularioVariables.variables}"></c:set>
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
	src="comun/js/formulario.js"></script>
<script type="text/javascript" language="javascript"
	src="comun/js/validacion.js"></script>
<script type="text/javascript" language="javascript"
	src="comun/js/ayudas.js"></script>

<script type="text/javascript">
        function inicio() {
        	setFoco("desglose1");
        }

        function checkForm(){
        	if (document.getElementById('desglose1') && !esValido("desglose1", "TEXTO", false)) {
                return false;
            }
        	if (document.getElementById('desglose2') && !esValido("desglose2", "TEXTO", false)) {
                return false;
            }
        	if (document.getElementById('r1Desde') && !esValido("r1Desde", "ENTERO", true)) {
                return false;
            }
			if (document.getElementById('r1Hasta') && !esValido("r1Hasta", "ENTERO", true)) {
                return false;
            }
			if (document.getElementById('r1Valor') && !esValido("r1Valor", "ENTERO", true)) {
                return false;
            }
			if (document.getElementById('r2Desde') && !esValido("r2Desde", "ENTERO", true)) {
                return false;
            }
			if (document.getElementById('r2Hasta') && !esValido("r2Hasta", "ENTERO", true)) {
                return false;
            }
			if (document.getElementById('r2Valor') && !esValido("r2Valor", "ENTERO", true)) {
                return false;
            }
			if (document.getElementById('r3Desde') && !esValido("r3Desde", "ENTERO", true)) {
                return false;
            }
			if (document.getElementById('r3Hasta') && !esValido("r3Hasta", "ENTERO", true)) {
                return false;
            }
			if (document.getElementById('r3Valor') && !esValido("r3Valor", "ENTERO", true)) {
                return false;
            }
        	if (!checkLongitudRedondeo()) {
            	return false;
            }
        	if (!checkRedondeo()) {
            	return false;
            }
        	if (document.getElementById('numReintentoCerrarCajaDescuadres') && !esValido("numReintentoCerrarCajaDescuadres", "ENTERO", false)) {
                return false;
            }
        	if (document.getElementById('importeAvisoRetirada') && !esValido("importeAvisoRetirada", "NUMERICO", true, 2)) {
                return false;
            }
        	if (document.getElementById('importeBloqueoRetirada') && !esValido("importeBloqueoRetirada", "NUMERICO", true, 2)) {
                return false;
            }
        	
        	return true;
        }

        function checkLongitudRedondeo(){
        	var l1Desde = document.getElementById("r1Desde").value.length;
			var l1Hasta = document.getElementById("r1Hasta").value.length;
			var l1Valor = document.getElementById("r1Valor").value.length;
			var l2Desde = document.getElementById("r2Desde").value.length;
			var l2Hasta = document.getElementById("r2Hasta").value.length;
			var l2Valor = document.getElementById("r2Valor").value.length;
			var l3Desde = document.getElementById("r3Desde").value.length;
			var l3Hasta = document.getElementById("r3Hasta").value.length;
			var l3Valor = document.getElementById("r3Valor").value.length;

			if(l1Desde != l1Hasta || l1Desde != l1Valor
					|| l1Desde != l2Desde || l1Desde != l2Hasta || l1Desde != l2Valor
					|| l1Desde != l3Desde || l1Desde != l3Hasta || l1Desde != l3Valor){

				alert(Gettext.gettext("Todos los valores del redondeo deben tener una longitud de %d dígitos.", l1Desde));
				return false;
			}
			
			return true;
        }

        function checkRedondeo(){
        	var r1Desde = document.getElementById("r1Desde").value;
			var r1Hasta = document.getElementById("r1Hasta").value;
			var r1Valor = document.getElementById("r1Valor").value;
			var r2Desde = document.getElementById("r2Desde").value;
			var r2Hasta = document.getElementById("r2Hasta").value;
			var r2Valor = document.getElementById("r2Valor").value;
			var r3Desde = document.getElementById("r3Desde").value;
			var r3Hasta = document.getElementById("r3Hasta").value;
			var r3Valor = document.getElementById("r3Valor").value;

			if(r1Desde == 0 && (r1Hasta != 0 || r1Valor != 0 || r2Desde != 0
					|| r2Hasta != 0 || r2Valor != 0 || r3Desde != 0
					|| r3Hasta != 0 || r3Valor != 0)){
				
				alert(Gettext.gettext("Todos los valores de redondeo deben ser igual a 0."));
				return false;
			}

			if(r1Desde >= r1Hasta){
				alert(Gettext.gettext("El campo 'Hasta' del primer nivel de redondeo debe ser mayor que el campo 'Desde' del primer nivel de redondeo."));
				return false;
			}
			if(r1Hasta >= r2Desde){
				alert(Gettext.gettext("El campo 'Desde' del segundo nivel de redondeo debe ser mayor que el campo 'Hasta' del primer nivel de redondeo'."));
				return false;
			}
			if(r2Desde >= r2Hasta){
				alert(Gettext.gettext("El campo 'Hasta' del segundo nivel de redondeo debe ser mayor que el campo 'Desde' del segundo nivel de redondeo."));
				return false;
			}
			if(r2Hasta >= r3Desde){
				alert(Gettext.gettext("El campo 'Desde' del tercer nivel de redondeo debe ser mayor que el campo 'Hasta' del segundo nivel de redondeo'."));
				return false;
			}
			if(r3Desde >= r3Hasta){
				alert(Gettext.gettext("El campo 'Hasta' del tercer nivel de redondeo debe ser mayor que el campo 'Desde' del tercer nivel de redondeo."));
				return false;
			}

			return true;
        }

        function editar() {
        	document.getElementById("accion").value = "leerFormulario";
        	document.getElementById("accion").value = "editar";
        	document.getElementById("frmDatos").submit();
        }
        
        function generarApiKey(){
        	var apiKey = "";
            var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            for( var i=0; i < 30; i++ ){
            	apiKey += possible.charAt(Math.floor(Math.random() * possible.length));
            }
            document.getElementById("wsRest").value = apiKey;
       	}

    </script>
</head>

<body onload="inicio();">
	<cmz:main>
		<cmz:panelCMZ>
			<cmz:cabeceraPanelCMZ titulo="${permisos.accionMenu.titulo}"
				icono="${permisos.accionMenu.icono}">
				<cmz:acciones numAccionesVisibles="2">
					<c:choose>
						<c:when test="${!formularioVariables.editable}">
							<c:if test="${permisos.puedeEditar}">
								<cmz:accion icono="comun/images/iconos/i-edit.gif"
									onclick="editar();" titulo="Editar"
									descripcion="Editar Variables"></cmz:accion>
							</c:if>
							<cmz:accion titulo="Ver Permisos"
								icono="comun/images/iconos/i-key.gif"
								descripcion="Ver permisos efectivos del usuarios"
								onclick="verPermisos(${permisos.accionMenu.idAccion});" />
							<c:if test="${permisos.puedeAdministrar}">
								<cmz:accion titulo="Administrar Permisos"
									icono="comun/images/iconos/i-admin_permisos.gif"
									descripcion="Administración de permisos"
									onclick="adminPermisos(${permisos.accionMenu.idAccion});" />
							</c:if>
						</c:when>
						<c:otherwise>
							<cmz:accionSalvar onclick="aceptar();" />
							<cmz:accionCancelar onclick="cancelar();" />
						</c:otherwise>
					</c:choose>
				</cmz:acciones>
			</cmz:cabeceraPanelCMZ>
			<cmz:cuerpoPanelCMZ>
				<cmz:mensaje />
				<form id="frmDatos" name="frmDatos" action="variables" method="post">
					<input id="accion" name="accion" type="hidden" value="" /> <input
						id="operacion" name="operacion" type="hidden" value="" />

					<cmz:panel>
						<cmz:cabeceraPanel titulo="Artículos"></cmz:cabeceraPanel>
						<cmz:cuerpoPanel>
							<table cellpadding="2px" cellspacing="2px" border="0px">
								<c:if test="${variables['ARTICULOS.DESGLOSE1_TITULO'] != null}">
									<tr>
										<c:set var="desglose1"
											value="${variables['ARTICULOS.DESGLOSE1_TITULO']}"></c:set>
										<td><cmz:etiqueta titulo="${desglose1.descripcion}" />:</td>
										<td><cmz:campoTexto id="desglose1"
												valor="${desglose1.valor == null ? desglose1.valorDefecto : desglose1.valor}"
												anchura="100px" longitudMaxima="20"
												editable="${formularioVariables.editable}"
												soloLectura="${!formularioVariables.editable}" /></td>
									</tr>
								</c:if>

								<c:if test="${variables['ARTICULOS.DESGLOSE2_TITULO'] != null}">
									<tr>
										<c:set var="desglose2"
											value="${variables['ARTICULOS.DESGLOSE2_TITULO']}"></c:set>
										<td><cmz:etiqueta titulo="${desglose2.descripcion}" />:</td>
										<td><cmz:campoTexto id="desglose2"
												valor="${desglose2.valor == null ? desglose2.valorDefecto : desglose2.valor}"
												anchura="100px" longitudMaxima="20"
												editable="${formularioVariables.editable}"
												soloLectura="${!formularioVariables.editable}" /></td>
									</tr>
								</c:if>

								<c:if
									test="${variables['ARTICULOS.FORMATO_CODBAR_AUTOMATICO'] != null}">
									<tr>
										<c:set var="codigoBarrasFormato"
											value="${variables['ARTICULOS.FORMATO_CODBAR_AUTOMATICO']}"></c:set>
										<td><cmz:etiqueta
												titulo="${codigoBarrasFormato.descripcion}" />:</td>
										<td><c:set var="formatoValor"
												value="${codigoBarrasFormato.valor == null ? codigoBarrasFormato.valorDefecto : codigoBarrasFormato.valor}"></c:set>
											<input type="radio" id="formatoValor" name="formatoValor"
											value="EAN13"
											<c:if test="${formatoValor == 'EAN13'}">checked="checked"</c:if>
											<c:if test="${!formularioVariables.editable}">disabled="disabled"</c:if>>&nbsp;EAN13
											<input type="radio" id="formatoValor" name="formatoValor"
											value="EAN8"
											<c:if test="${formatoValor == 'EAN8'}">checked="checked"</c:if>
											<c:if test="${!formularioVariables.editable}">disabled="disabled"</c:if>>&nbsp;EAN8
										</td>
									</tr>
								</c:if>
							</table>
						</cmz:cuerpoPanel>
					</cmz:panel>
					<cmz:panel>
						<cmz:cabeceraPanel titulo="Tarifas"></cmz:cabeceraPanel>
						<cmz:cuerpoPanel>
							<table cellpadding="2px" cellspacing="2px" border="0px">
								<tr>
									<c:set var="redondeo1"
										value="${variables['TARIFAS.REDONDEO_NIVEL1']}"></c:set>

									<c:if test="${redondeo1.valor == null}">
										<c:set var="r1Desde"
											value="${fn:split(redondeo1.valorDefecto, '-')[0]}"></c:set>
										<c:set var="r1Hasta"
											value="${fn:split(redondeo1.valorDefecto, '-')[1]}"></c:set>
										<c:set var="r1Valor"
											value="${fn:split(redondeo1.valorDefecto, '-')[2]}"></c:set>
									</c:if>
									<c:if test="${redondeo1.valor != null}">
										<c:set var="r1Desde"
											value="${fn:split(redondeo1.valor, '-')[0]}"></c:set>
										<c:set var="r1Hasta"
											value="${fn:split(redondeo1.valor, '-')[1]}"></c:set>
										<c:set var="r1Valor"
											value="${fn:split(redondeo1.valor, '-')[2]}"></c:set>
									</c:if>
									<td><cmz:etiqueta titulo="${redondeo1.descripcion}" />:</td>
									<td><cmz:campoTexto id="r1Desde" valor="${r1Desde}"
											anchura="30px" longitudMaxima="2" requerido="true"
											editable="${formularioVariables.editable}"
											soloLectura="${!formularioVariables.editable}" /></td>
									<td><cmz:campoTexto id="r1Hasta" valor="${r1Hasta}"
											anchura="30px" longitudMaxima="2" requerido="true"
											editable="${formularioVariables.editable}"
											soloLectura="${!formularioVariables.editable}" /></td>
									<td><cmz:campoTexto id="r1Valor" valor="${r1Valor}"
											anchura="30px" longitudMaxima="2" requerido="true"
											editable="${formularioVariables.editable}"
											soloLectura="${!formularioVariables.editable}" /></td>
								</tr>
								<tr>
									<c:set var="redondeo2"
										value="${variables['TARIFAS.REDONDEO_NIVEL2']}"></c:set>

									<c:if test="${redondeo2.valor == null}">
										<c:set var="r2Desde"
											value="${fn:split(redondeo2.valorDefecto, '-')[0]}"></c:set>
										<c:set var="r2Hasta"
											value="${fn:split(redondeo2.valorDefecto, '-')[1]}"></c:set>
										<c:set var="r2Valor"
											value="${fn:split(redondeo2.valorDefecto, '-')[2]}"></c:set>
									</c:if>
									<c:if test="${redondeo2.valor != null}">
										<c:set var="r2Desde"
											value="${fn:split(redondeo2.valor, '-')[0]}"></c:set>
										<c:set var="r2Hasta"
											value="${fn:split(redondeo2.valor, '-')[1]}"></c:set>
										<c:set var="r2Valor"
											value="${fn:split(redondeo2.valor, '-')[2]}"></c:set>
									</c:if>
									<td><cmz:etiqueta titulo="${redondeo2.descripcion}" />:</td>
									<td><cmz:campoTexto id="r2Desde" valor="${r2Desde}"
											anchura="30px" longitudMaxima="2" requerido="true"
											editable="${formularioVariables.editable}"
											soloLectura="${!formularioVariables.editable}" /></td>
									<td><cmz:campoTexto id="r2Hasta" valor="${r2Hasta}"
											anchura="30px" longitudMaxima="2" requerido="true"
											editable="${formularioVariables.editable}"
											soloLectura="${!formularioVariables.editable}" /></td>
									<td><cmz:campoTexto id="r2Valor" valor="${r2Valor}"
											anchura="30px" longitudMaxima="2" requerido="true"
											editable="${formularioVariables.editable}"
											soloLectura="${!formularioVariables.editable}" /></td>
								</tr>
								<tr>
									<c:set var="redondeo3"
										value="${variables['TARIFAS.REDONDEO_NIVEL3']}"></c:set>

									<c:if test="${redondeo3.valor == null}">
										<c:set var="r3Desde"
											value="${fn:split(redondeo3.valorDefecto, '-')[0]}"></c:set>
										<c:set var="r3Hasta"
											value="${fn:split(redondeo3.valorDefecto, '-')[1]}"></c:set>
										<c:set var="r3Valor"
											value="${fn:split(redondeo3.valorDefecto, '-')[2]}"></c:set>
									</c:if>
									<c:if test="${redondeo3.valor != null}">
										<c:set var="r3Desde"
											value="${fn:split(redondeo3.valor, '-')[0]}"></c:set>
										<c:set var="r3Hasta"
											value="${fn:split(redondeo3.valor, '-')[1]}"></c:set>
										<c:set var="r3Valor"
											value="${fn:split(redondeo3.valor, '-')[2]}"></c:set>
									</c:if>
									<td><cmz:etiqueta titulo="${redondeo3.descripcion}" />:</td>
									<td><cmz:campoTexto id="r3Desde" valor="${r3Desde}"
											anchura="30px" longitudMaxima="2" requerido="true"
											editable="${formularioVariables.editable}"
											soloLectura="${!formularioVariables.editable}" /></td>
									<td><cmz:campoTexto id="r3Hasta" valor="${r3Hasta}"
											anchura="30px" longitudMaxima="2" requerido="true"
											editable="${formularioVariables.editable}"
											soloLectura="${!formularioVariables.editable}" /></td>
									<td><cmz:campoTexto id="r3Valor" valor="${r3Valor}"
											anchura="30px" longitudMaxima="2" requerido="true"
											editable="${formularioVariables.editable}"
											soloLectura="${!formularioVariables.editable}" /></td>
								</tr>
							</table>
						</cmz:cuerpoPanel>
					</cmz:panel>
					<cmz:panel>
						<cmz:cabeceraPanel titulo="TPV"></cmz:cabeceraPanel>
						<cmz:cuerpoPanel>
							<table cellpadding="2px" cellspacing="2px" border="0px">

								<c:if
									test="${variables['TICKETS.USA_DESCUENTO_EN_LINEA'] != null}">
									<tr>
										<td colspan="2"><c:set var="ticketsUsa"
												value="${variables['TICKETS.USA_DESCUENTO_EN_LINEA']}"></c:set>
											<c:set var="ticketsUsaValor"
												value="${ticketsUsa.valor == null ? ticketsUsa.valorDefecto : ticketsUsa.valor}"></c:set>
											<input type="checkbox" value="" id="ticketsUsa"
											name="ticketsUsa"
											<c:if test="${ticketsUsaValor == 'S'}">checked="checked"</c:if>
											<c:if test="${!formularioVariables.editable}">disabled</c:if> />
											<cmz:etiqueta titulo="${ticketsUsa.descripcion}"></cmz:etiqueta>
										</td>
									</tr>
								</c:if>

								<c:if
									test="${variables['TPV.PERMITIR_VENTA_PRECIO_CERO'] != null}">
									<tr>
										<td colspan="2"><c:set var="ticketsVenta0"
												value="${variables['TPV.PERMITIR_VENTA_PRECIO_CERO']}"></c:set>
											<c:set var="ticketsVentaPrecio0"
												value="${ticketsVenta0.valor == null ? ticketsVenta0.valorDefecto : ticketsVenta0.valor}"></c:set>
											<input type="checkbox" value="" id="ticketsVenta0"
											name="ticketsVenta0"
											<c:if test="${ticketsVentaPrecio0 == 'S'}">checked="checked"</c:if>
											<c:if test="${!formularioVariables.editable}">disabled</c:if> />
											<cmz:etiqueta titulo="${ticketsVenta0.descripcion}"></cmz:etiqueta>
										</td>
									</tr>
								</c:if>

								<c:if
									test="${variables['TICKETS.PERMITE_CAMBIO_PRECIO'] != null}">
									<tr>
										<td colspan="2"><c:set var="ticketsPermiteCambioPrecio"
												value="${variables['TICKETS.PERMITE_CAMBIO_PRECIO']}"></c:set>
											<c:set var="ticketsPermiteCambioPrecioValor"
												value="${ticketsPermiteCambioPrecio.valor == null ? ticketsPermiteCambioPrecio.valorDefecto : ticketsPermiteCambioPrecio.valor}"></c:set>
											<input type="checkbox" value=""
											id="ticketsPermiteCambioPrecio"
											name="ticketsPermiteCambioPrecio"
											<c:if test="${ticketsPermiteCambioPrecioValor == 'S'}">checked="checked"</c:if>
											<c:if test="${!formularioVariables.editable}">disabled</c:if> />
											<cmz:etiqueta
												titulo="${ticketsPermiteCambioPrecio.descripcion}"></cmz:etiqueta>
										</td>
									</tr>
								</c:if>

								<c:if
									test="${variables['CAJA.CIERRE_CAJA_DIARIO_OBLIGATORIO'] != null}">
									<tr>
										<td colspan="2"><c:set var="cajaCierreDiarioObligatorio"
												value="${variables['CAJA.CIERRE_CAJA_DIARIO_OBLIGATORIO']}"></c:set>
											<c:set var="cajaCierreDiarioObligatorioValor"
												value="${cajaCierreDiarioObligatorio.valor == null ? cajaCierreDiarioObligatorio.valorDefecto : cajaCierreDiarioObligatorio.valor}"></c:set>
											<input type="checkbox" value=""
											id="cajaCierreDiarioObligatorio"
											name="cajaCierreDiarioObligatorio"
											<c:if test="${cajaCierreDiarioObligatorioValor == 'S'}">checked="checked"</c:if>
											<c:if test="${!formularioVariables.editable}">disabled</c:if> />
											<cmz:etiqueta
												titulo="${cajaCierreDiarioObligatorio.descripcion}"></cmz:etiqueta>
										</td>
									</tr>
								</c:if>

								<c:if test="${variables['CAJA.APERTURA_AUTOMATICA'] != null}">
									<tr>
										<td colspan="2"><c:set var="cajaAperturaAutomatica"
												value="${variables['CAJA.APERTURA_AUTOMATICA']}"></c:set> <c:set
												var="cajaAperturaAutomaticaValor"
												value="${cajaAperturaAutomatica.valor == null ? cajaAperturaAutomatica.valorDefecto : cajaAperturaAutomatica.valor}"></c:set>
											<input type="checkbox" value="" id="cajaAperturaAutomatica"
											name="cajaAperturaAutomatica"
											<c:if test="${cajaAperturaAutomaticaValor == 'S'}">checked="checked"</c:if>
											<c:if test="${!formularioVariables.editable}">disabled</c:if> />
											<cmz:etiqueta titulo="${cajaAperturaAutomatica.descripcion}"></cmz:etiqueta>
										</td>
									</tr>
								</c:if>

								<c:if
									test="${variables['TPV.COLUMNA_VENDEDOR_VISIBLE'] != null}">
									<tr>
										<td colspan="2"><c:set var="tpvColumnaVendedorVisible"
												value="${variables['TPV.COLUMNA_VENDEDOR_VISIBLE']}"></c:set>
											<c:set var="tpvColumnaVendedorVisibleValor"
												value="${tpvColumnaVendedorVisible.valor == null ? tpvColumnaVendedorVisible.valorDefecto : tpvColumnaVendedorVisible.valor}"></c:set>
											<input type="checkbox" value=""
											id="tpvColumnaVendedorVisible"
											name="tpvColumnaVendedorVisible"
											<c:if test="${tpvColumnaVendedorVisibleValor == 'S'}">checked="checked"</c:if>
											<c:if test="${!formularioVariables.editable}">disabled</c:if> />
											<cmz:etiqueta
												titulo="${tpvColumnaVendedorVisible.descripcion}"></cmz:etiqueta>
										</td>
									</tr>
								</c:if>

								<c:if
									test="${variables['PROMOCIONES.APLICACION_PREVIA_DE_PROMOCION_PRECIO'] != null}">
									<tr>
										<td colspan="2"><c:set
												var="tpvPromoPrecioAplicacionPrevia"
												value="${variables['PROMOCIONES.APLICACION_PREVIA_DE_PROMOCION_PRECIO']}"></c:set>
											<c:set var="tpvPromoPrecioAplicacionPreviaValor"
												value="${tpvPromoPrecioAplicacionPrevia.valor == null ? tpvPromoPrecioAplicacionPrevia.valorDefecto : tpvPromoPrecioAplicacionPrevia.valor}"></c:set>
											<input type="checkbox" value=""
											id="tpvPromoPrecioAplicacionPrevia"
											name="tpvPromoPrecioAplicacionPrevia"
											<c:if test="${tpvPromoPrecioAplicacionPreviaValor == 'S'}">checked="checked"</c:if>
											<c:if test="${!formularioVariables.editable}">disabled</c:if> />
											<cmz:etiqueta
												titulo="${tpvPromoPrecioAplicacionPrevia.descripcion}"></cmz:etiqueta>
										</td>
									</tr>
								</c:if>

								<c:if
									test="${variables['TPV.TRATAR_PROMOCIONES_MENOS_INGRESO'] != null}">
									<tr>
										<td colspan="2"><c:set var="ticketsPromoMenosIngresos"
												value="${variables['TPV.TRATAR_PROMOCIONES_MENOS_INGRESO']}"></c:set>
											<c:set var="ticketsPromoMenosIngresosValor"
												value="${ticketsPromoMenosIngresos.valor == null ? ticketsPromoMenosIngresos.valorDefecto : ticketsPromoMenosIngresos.valor}"></c:set>
											<input type="checkbox" value=""
											id="ticketsPromoMenosIngresos"
											name="ticketsPromoMenosIngresos"
											<c:if test="${ticketsPromoMenosIngresosValor == 'S'}">checked="checked"</c:if>
											<c:if test="${!formularioVariables.editable}">disabled</c:if> />
											<cmz:etiqueta
												titulo="${ticketsPromoMenosIngresos.descripcion}"></cmz:etiqueta>
										</td>
									</tr>
								</c:if>

								<c:if test="${variables['ARTICULOS.CONTROL_SURTIDO'] != null}">
									<tr>
										<td colspan="2"><c:set var="controlSurtido"
												value="${variables['ARTICULOS.CONTROL_SURTIDO']}"></c:set> <c:set
												var="controlSurtidoV"
												value="${controlSurtido.valor == null ? controlSurtido.valorDefecto : controlSurtido.valor}"></c:set>
											<input type="checkbox" value="" id="controlSurtido"
											name="controlSurtido"
											<c:if test="${controlSurtidoV == 'S'}">checked="checked"</c:if>
											<c:if test="${!formularioVariables.editable}">disabled</c:if> />
											<cmz:etiqueta titulo="${controlSurtido.descripcion}"></cmz:etiqueta>
											<img src="comun/images/iconos/info.gif" align="absMiddle"
											border="0" valign="middle" style="vertical-align: top;"
											alt='<cmz:etiqueta titulo="-Si no se marca, se envía el surtido completo a la tienda.
-Si se marca, se envía a cada tienda sólo los artículos que estén asociados a dicha tienda en Artículos->Almacenes"/>'
											title='<cmz:etiqueta titulo="-Si no se marca, se envía el surtido completo a la tienda.
-Si se marca, se envía a cada tienda sólo los artículos que estén asociados a dicha tienda en Artículos->Almacenes"/>' />

										</td>
									</tr>
								</c:if>

								<c:if test="${variables['CAJA.REINTENTOS_CIERRE'] != null}">
									<tr>
										<td colspan="2"><c:set
												var="numReintentoCerrarCajaDescuadres"
												value="${variables['CAJA.REINTENTOS_CIERRE']}"></c:set> <c:set
												var="numReintentoCerrarCajaDescuadresV"
												value="${numReintentoCerrarCajaDescuadres.valor == null ? numReintentoCerrarCajaDescuadres.valorDefecto : numReintentoCerrarCajaDescuadres.valor}"></c:set>
											<cmz:etiqueta
												titulo="${numReintentoCerrarCajaDescuadres.descripcion}"></cmz:etiqueta>
											<cmz:campoTexto id="numReintentoCerrarCajaDescuadres"
												valor="${numReintentoCerrarCajaDescuadresV}" anchura="30px"
												longitudMaxima="2" requerido="false"
												editable="${formularioVariables.editable}"
												soloLectura="${!formularioVariables.editable}" /></td>
									</tr>
								</c:if>

								<c:if
									test="${variables['CAJA.IMPORTE_MAXIMO_DESCUADRE'] != null}">
									<tr>
										<td colspan="2"><c:set var="numImporteMaximoDescuadre"
												value="${variables['CAJA.IMPORTE_MAXIMO_DESCUADRE']}"></c:set>
											<c:set var="numImporteMaximoDescuadreV"
												value="${numImporteMaximoDescuadre.valor == null ? numImporteMaximoDescuadre.valorDefecto : numImporteMaximoDescuadre.valor}"></c:set>
											<cmz:etiqueta
												titulo="${numImporteMaximoDescuadre.descripcion}"></cmz:etiqueta>
											<cmz:campoTexto id="numImporteMaximoDescuadre"
												valor="${numImporteMaximoDescuadreV}" anchura="30px"
												longitudMaxima="8" requerido="false"
												editable="${formularioVariables.editable}"
												soloLectura="${!formularioVariables.editable}" /></td>
									</tr>
								</c:if>

								<c:if
									test="${variables['CONTADORES.CARACTER_SEPARADOR'] != null}">
									<tr>
										<td colspan="2"><c:set var="caracterSeparador"
												value="${variables['CONTADORES.CARACTER_SEPARADOR']}"></c:set>
											<c:set var="caracterSeparadorV"
												value="${caracterSeparador.valor == null ? caracterSeparador.valorDefecto : caracterSeparador.valor}"></c:set>
											<cmz:etiqueta titulo="${caracterSeparador.descripcion}"></cmz:etiqueta>
											<cmz:campoTexto id="caracterSeparador"
												valor="${caracterSeparadorV}" anchura="20px"
												longitudMaxima="1" requerido="false"
												editable="${formularioVariables.editable}"
												soloLectura="${!formularioVariables.editable}" /></td>
									</tr>
								</c:if>

								<c:if test="${variables['CAJA.IMPORTE_AVISO_RETIRADA'] != null}">
									<tr>
										<td colspan="2"><c:set var="importeAvisoRetirada"
												value="${variables['CAJA.IMPORTE_AVISO_RETIRADA']}"></c:set>
											<c:set var="importeAvisoRetiradaV"
												value="${importeAvisoRetirada.valor == null ? importeAvisoRetirada.valorDefecto : importeAvisoRetirada.valor}"></c:set>
											<cmz:etiqueta titulo="${importeAvisoRetirada.descripcion}"></cmz:etiqueta>
											<cmz:campoTexto id="importeAvisoRetirada" anchura="50px"
												requerido="true" editable="${formularioVariables.editable}"
												soloLectura="${!formularioVariables.editable}">
												<cmz:formateaNumero valor="${importeAvisoRetiradaV}"
													numDecimales="2"></cmz:formateaNumero>
											</cmz:campoTexto></td>
									</tr>
								</c:if>

								<c:if
									test="${variables['CAJA.IMPORTE_BLOQUEO_RETIRADA'] != null}">
									<tr>
										<td colspan="2"><c:set var="importeBloqueoRetirada"
												value="${variables['CAJA.IMPORTE_BLOQUEO_RETIRADA']}"></c:set>
											<c:set var="importeBloqueoRetiradaV"
												value="${importeBloqueoRetirada.valor == null ? importeBloqueoRetirada.valorDefecto : importeBloqueoRetirada.valor}"></c:set>
											<cmz:etiqueta titulo="${importeBloqueoRetirada.descripcion}"></cmz:etiqueta>
											<cmz:campoTexto id="importeBloqueoRetirada" anchura="50px"
												requerido="true" editable="${formularioVariables.editable}"
												soloLectura="${!formularioVariables.editable}">
												<cmz:formateaNumero valor="${importeBloqueoRetiradaV}"
													numDecimales="2"></cmz:formateaNumero>
											</cmz:campoTexto></td>
									</tr>
								</c:if>

					<c:if
						test="${variables['TPV.MAX_DIAS_PERMITIDOS_DEVOLUCION'] != null}">
						<tr>
							<td colspan="2">
							<c:set var="numMaximoDiasDevolucion" value="${variables['TPV.MAX_DIAS_PERMITIDOS_DEVOLUCION']}"></c:set>
							<c:set var="numMaximoDiasDevolucionV"	value="${numMaximoDiasDevolucion.valor == null ? numMaximoDiasDevolucion.valorDefecto : numMaximoDiasDevolucion.valor}"></c:set>
							<cmz:etiqueta
								titulo="${numMaximoDiasDevolucion.descripcion}"></cmz:etiqueta>
							<cmz:campoTexto id="numMaximoDiasDevolucion"
								valor="${numMaximoDiasDevolucionV}" anchura="30px"
									longitudMaxima="8" requerido="false"
									editable="${formularioVariables.editable}"
									soloLectura="${!formularioVariables.editable}" /></td>
						</tr>
					</c:if>



							</table>
						</cmz:cuerpoPanel>
					</cmz:panel>

					<cmz:panel>
						<cmz:cabeceraPanel titulo="Stock"></cmz:cabeceraPanel>
						<cmz:cuerpoPanel>
							<table cellpadding="2px" cellspacing="2px" border="0px">
								<c:if test="${variables['STOCK.TITULO_A'] != null}">
									<tr>
										<c:set var="stockTituloA"
											value="${variables['STOCK.TITULO_A']}"></c:set>
										<td><cmz:etiqueta titulo="${stockTituloA.descripcion}" />:</td>
										<td><cmz:campoTexto id="stockTituloA"
												valor="${stockTituloA.valor == null ? stockTituloA.valorDefecto : stockTituloA.valor}"
												anchura="100px" longitudMaxima="20"
												editable="${formularioVariables.editable}"
												soloLectura="${!formularioVariables.editable}" /></td>
									</tr>
								</c:if>

								<c:if test="${variables['STOCK.TITULO_B'] != null}">
									<tr>
										<c:set var="stockTituloB"
											value="${variables['STOCK.TITULO_B']}"></c:set>
										<td><cmz:etiqueta titulo="${stockTituloB.descripcion}" />:</td>
										<td><cmz:campoTexto id="stockTituloB"
												valor="${stockTituloB.valor == null ? stockTituloB.valorDefecto : stockTituloB.valor}"
												anchura="100px" longitudMaxima="20"
												editable="${formularioVariables.editable}"
												soloLectura="${!formularioVariables.editable}" /></td>
									</tr>
								</c:if>

								<c:if test="${variables['STOCK.TITULO_C'] != null}">
									<tr>
										<c:set var="stockTituloC"
											value="${variables['STOCK.TITULO_C']}"></c:set>
										<td><cmz:etiqueta titulo="${stockTituloC.descripcion}" />:</td>
										<td><cmz:campoTexto id="stockTituloC"
												valor="${stockTituloC.valor == null ? stockTituloC.valorDefecto : stockTituloC.valor}"
												anchura="100px" longitudMaxima="20"
												editable="${formularioVariables.editable}"
												soloLectura="${!formularioVariables.editable}" /></td>
									</tr>
								</c:if>


								<c:if test="${variables['STOCK.TITULO_D'] != null}">
									<tr>
										<c:set var="stockTituloD"
											value="${variables['STOCK.TITULO_D']}"></c:set>
										<td><cmz:etiqueta titulo="${stockTituloD.descripcion}" />:</td>
										<td><cmz:campoTexto id="stockTituloD"
												valor="${stockTituloD.valor == null ? stockTituloD.valorDefecto : stockTituloD.valor}"
												anchura="100px" longitudMaxima="20"
												editable="${formularioVariables.editable}"
												soloLectura="${!formularioVariables.editable}" /></td>
									</tr>
								</c:if>
							</table>
						</cmz:cuerpoPanel>
					</cmz:panel>

					<c:if test="${variables['WEBSERVICES.APIKEY'] != null}">
						<cmz:panel>
							<cmz:cabeceraPanel titulo="Servicios Rest"></cmz:cabeceraPanel>
							<cmz:cuerpoPanel>
								<table cellpadding="2px" cellspacing="2px" border="0px">
									<tr>
										<c:set var="wsRest" value="${variables['WEBSERVICES.APIKEY']}"></c:set>
										<td><cmz:etiqueta titulo="${wsRest.descripcion}" />:</td>
										<td><cmz:campoTexto id="wsRest"
												valor="${wsRest.valor == null ? wsRest.valorDefecto : wsRest.valor}"
												anchura="400px" longitudMaxima="255"
												editable="${formularioVariables.editable}"
												soloLectura="${!formularioVariables.editable}" /></td>
										<c:if test="${formularioVariables.editable}">
											<td><cmz:boton id="btnGenerateKey" valor="Generar Key"
													onclick="generarApiKey();"></cmz:boton></td>
										</c:if>
									</tr>
								</table>
							</cmz:cuerpoPanel>
						</cmz:panel>
					</c:if>
				</form>
			</cmz:cuerpoPanelCMZ>
		</cmz:panelCMZ>
	</cmz:main>
</body>
</html>
