<%--

    ComerZZia 4.8

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
<jsp:useBean id="paramBuscarDevoluciones"
	type="com.comerzzia.bricodepot.backoffice.persistence.devoluciones.validacion.ParametrosBuscarValidacionesDevolucionAlbaran"
	scope="session" />
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
<jsp:useBean id="datosSesion"
	type="com.comerzzia.core.servicios.sesion.DatosSesionBean"
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
	    }
	    function volver() {
	    	document.getElementById("accion").value = "buscar";
	        document.getElementById("frmDatos").submit();
	    	_esperando();
	    }
	
	    function checkForm() {
	        if (!esValido("fechaDesde", "FECHA", true)) {
	            return false;
	        }
	        if (!esValido("fechaHasta", "FECHA", true)) {
	            return false;
	        }
			return true;
	    }
	
	    function imprimir(idVersion) {
	    	if(checkForm()) {
	            document.getElementById("accion").value = "devoluciones.lstValidacion";
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
			<c:set var="titulo"
				value="<%=datosSesion.getTranslation()._(permisos.getAccionMenu().getTitulo())%>"></c:set>
			<cmz:cabeceraPanelCMZ
				titulo="${titulo} - ${datosSesion.atributos['CODALMACEN_SELECCIONADO']} ${datosSesion.atributos['DESALMACEN_SELECCIONADO']}"
				icono="${permisos.accionMenu.icono}">
				<cmz:acciones numAccionesVisibles="1">
					<cmz:accion icono="comun/images/iconos/i-volver.gif"
						onclick="volver();" titulo="Volver" descripcion="Volver"></cmz:accion>
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
				<form id="frmDatos" name="frmDatos" action="devoluciones"
					method="POST" target="_self">
					<input id="accion" name="accion" type="hidden" value="" /> <input
						id="operacion" name="operacion" type="hidden" value="" /> <input
						id="idVersion" name="idVersion" type="hidden" value="0" />
					<cmz:panel>
						<cmz:cabeceraPanel titulo="Parámetros"></cmz:cabeceraPanel>
						<cmz:cuerpoPanel>
							<table cellpadding="2px" cellspacing="2px" border="0px">

								<tr>
									<td><cmz:etiqueta titulo="Fecha desde" />:</td>
									<td><cmz:campoFecha id="fechaDesde" editable="true"
											mostrarCheckbox="false" anchura="80px">
											<fmt:formatDate pattern="dd/MM/yyyy"
												value="${paramBuscarDevoluciones.fechaDesde}" />
										</cmz:campoFecha></td>
								</tr>
								<tr>
									<td><cmz:etiqueta titulo="Fecha hasta" />:</td>
									<td><cmz:campoFecha id="fechaHasta" editable="true"
											mostrarCheckbox="false" anchura="80px">
											<fmt:formatDate pattern="dd/MM/yyyy"
												value="${paramBuscarDevoluciones.fechaHasta}" />
										</cmz:campoFecha></td>
								</tr>
								<tr>
									<td><cmz:etiqueta titulo="Tienda" />:</td>
									<td><cmz:ayuda nombreAyuda="TIENDAS">
											<cmz:codigoAyuda idCodigo="codAlm"
												valorCodigo="${paramBuscarDevoluciones.codAlm}"
												anchuraCodigo="50px" longitudMaximaCodigo="4"></cmz:codigoAyuda>
											<cmz:descripcionAyuda idDescripcion="desAlm"
												valorDescripcion="${paramBuscarDevoluciones.desAlm}"
												anchuraDescripcion="180px"></cmz:descripcionAyuda>
										</cmz:ayuda></td>
								</tr>
								 <tr>
									<td><cmz:etiqueta titulo="Supervisor"/>:</td>
									<td>
										<cmz:ayuda nombreAyuda="USUARIOS">
										<cmz:codigoAyuda idCodigo="idUsuarioSupervisor" anchuraCodigo="100px" valorCodigo="${paramBuscarDevoluciones.idUsuarioSupervisor}"></cmz:codigoAyuda>
										<cmz:descripcionAyuda idDescripcion="desUsuarioSupervisor" anchuraDescripcion="300px" valorDescripcion="${paramBuscarDevoluciones.desUsuarioSupervisor}" ></cmz:descripcionAyuda>
										</cmz:ayuda>
									</td>
								</tr>
								
								<tr>
									<td><cmz:etiqueta titulo="Articulo" />:</td>
									<td><cmz:ayuda requerido="false" soloLectura="false"
											nombreAyuda="ARTICULOS">
											<cmz:codigoAyuda idCodigo="codArt"
												valorCodigo="${paramBuscarDevoluciones.codArt}"
												anchuraCodigo="100px" longitudMaximaCodigo="20"></cmz:codigoAyuda>
											<cmz:descripcionAyuda idDescripcion="desArt"
												valorDescripcion="${paramBuscarDevoluciones.desArt}"
												anchuraDescripcion="300px"></cmz:descripcionAyuda>
										</cmz:ayuda></td>
								</tr>
								<tr>
									<td><cmz:etiqueta titulo="Numero de factura" />:</td>
									<td><cmz:campoTexto id="referenciaCliente"
											valor="${paramBuscarDevoluciones.referenciaCliente}"
											requerido="false" anchura="200px" longitudMaxima="20" /></td>
								</tr>
								<tr>
									<td><cmz:etiqueta titulo="Validado" />:</td>
									<td><select id="validado" name="validado" class="campo">
											<option value=""
												<c:if test="${paramBuscarDevoluciones.validado == ''}">selected="selected"</c:if>><cmz:etiqueta
													titulo="Todos" /></option>
											<option value="S"
												<c:if test="${paramBuscarDevoluciones.validado == 'S'}">selected="selected"</c:if>><cmz:etiqueta
													titulo="Validados" /></option>
											<option value="N"
												<c:if test="${paramBuscarDevoluciones.validado == 'N'}">selected="selected"</c:if>><cmz:etiqueta
													titulo="No validados" /></option>
									</select></td>
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