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
	        document.getElementById("fechaDesde").focus();
	        pintaCalendario("fechaDesde", "imgFechaDesde");
            pintaCalendario("fechaHasta", "imgFechaHasta");
	    }
	
	    function checkForm() {
			return true;
	    }
	
	    function imprimir(idVersion) {
	    	if(checkForm()) {
	            document.getElementById("accion").value = "devoluciones.lstMotivos";
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
					<cmz:accion titulo="Ver Permisos" icono="comun/images/iconos/i-key.gif" descripcion="Ver permisos efectivos del usuario" onclick="verPermisos(${permisos.accionMenu.idAccion})" />
					<c:if test="${permisos.puedeAdministrar}">
						<cmz:accion titulo="Administrar Permisos" icono="comun/images/iconos/i-admin_permisos.gif" descripcion="Administración de permisos" onclick="adminPermisos(${permisos.accionMenu.idAccion})" />
					</c:if>
				</cmz:acciones>
			</cmz:cabeceraPanelCMZ>

			<cmz:cuerpoPanelCMZ>
				<cmz:mensaje />
				<form id="frmDatos" name="frmDatos" action="estadisticasMotivos"
					method="POST" target="_self">
					<input id="accion" name="accion" type="hidden" value="" /> 
					<input id="operacion" name="operacion" type="hidden" value="" /> 
					<input id="idVersion" name="idVersion" type="hidden" value="0" />
					<cmz:panel>
						<cmz:cabeceraPanel titulo="Parámetros"></cmz:cabeceraPanel>
						<cmz:cuerpoPanel>
							<table cellpadding="2px" cellspacing="2px" border="0px">
								<tr>
									<td><cmz:etiqueta titulo="Artículo desde" />:</td>
									<td><cmz:ayuda nombreAyuda="ARTICULOS">
											<cmz:codigoAyuda idCodigo="codArtDesde"	longitudMaximaCodigo="20" anchuraCodigo="130px" />
											<cmz:descripcionAyuda idDescripcion="desArtDesde" anchuraDescripcion="400px" />
										</cmz:ayuda>
									</td>
								</tr>
								<tr>
									<td><cmz:etiqueta titulo="Artículo hasta" />:</td>
									<td><cmz:ayuda nombreAyuda="ARTICULOS">
											<cmz:codigoAyuda idCodigo="codArtHasta"	longitudMaximaCodigo="20" anchuraCodigo="130px" />
											<cmz:descripcionAyuda idDescripcion="desArtHasta" anchuraDescripcion="400px" />
										</cmz:ayuda>
									</td>
								</tr>
								<tr>
									<td><cmz:etiqueta titulo="Fecha desde" />:</td>
									<td><cmz:campoFecha id="fechaDesde" mostrarCheckbox="false">
											<fmt:formatDate pattern="dd/MM/yyyy" value="${fechaDesde}" />
										</cmz:campoFecha>
									</td>
								</tr>
								<tr>
									<td><cmz:etiqueta titulo="Fecha hasta" />:</td>
									<td><cmz:campoFecha id="fechaHasta" mostrarCheckbox="false">
											<fmt:formatDate pattern="dd/MM/yyyy" value="${fechaHasta}" />
										</cmz:campoFecha>
									</td>
								</tr>
								<tr>
									<td><cmz:etiqueta titulo="Familia desde" />:</td>
									<td><cmz:ayuda nombreAyuda="FAMILIAS">
											<cmz:codigoAyuda idCodigo="codFamDesde" longitudMaximaCodigo="20" anchuraCodigo="130px" />
											<cmz:descripcionAyuda idDescripcion="desFamDesde" anchuraDescripcion="400px" />
										</cmz:ayuda>
									</td>
								</tr>
								<tr>
									<td><cmz:etiqueta titulo="Familia hasta" />:</td>
									<td><cmz:ayuda nombreAyuda="FAMILIAS">
											<cmz:codigoAyuda idCodigo="codFamHasta" longitudMaximaCodigo="20" anchuraCodigo="130px" />
											<cmz:descripcionAyuda idDescripcion="desFamHasta" anchuraDescripcion="400px" />
										</cmz:ayuda>
									</td>
								</tr>
								<tr>
									<td><cmz:etiqueta titulo="Motivo" />:</td>
									<td><cmz:ayuda nombreAyuda="MOTIVOS">
											<cmz:codigoAyuda idCodigo="codMot" longitudMaximaCodigo="11" anchuraCodigo="100px" />
											<cmz:descripcionAyuda idDescripcion="desMot" anchuraDescripcion="320px" />
										</cmz:ayuda>
									</td>
								</tr>
								<tr>
									<td><cmz:etiqueta titulo="Usuario" />:</td>
									<td><cmz:ayuda nombreAyuda="USUARIOS">
											<cmz:codigoAyuda idCodigo="idUsuario" longitudMaximaCodigo="11" anchuraCodigo="100px" />
											<cmz:descripcionAyuda idDescripcion="desUsuario" anchuraDescripcion="320px" />
										</cmz:ayuda>
									</td>
								</tr>

								<tr>
									<td><cmz:etiqueta titulo="Forma de pago" />:</td>
									<td><cmz:ayuda nombreAyuda="MEDIOS_PAGO_VENTAS">
											<cmz:codigoAyuda idCodigo="codmedpag" longitudMaximaCodigo="11" anchuraCodigo="100px" />
											<cmz:descripcionAyuda idDescripcion="desmedpago" anchuraDescripcion="320px" />
										</cmz:ayuda>
									</td>
								</tr>
								<tr>
									<td><cmz:etiqueta titulo="Ticket" />:</td>
									<td><cmz:campoTexto id="codticket"></cmz:campoTexto></td>
								</tr>
								<tr>
		                        	<td><cmz:etiqueta titulo="Almacén desde"/>:</td>
		                          	<td>
			                            <cmz:ayuda requerido="false" soloLectura="false" nombreAyuda="ALMACENES">
		          					  		<cmz:codigoAyuda idCodigo="codAlmacenDesde" anchuraCodigo="130px" longitudMaximaCodigo="4"></cmz:codigoAyuda>
		          					  		<cmz:descripcionAyuda idDescripcion="desAlmacenDesde" anchuraDescripcion="400px"></cmz:descripcionAyuda>
		          				    	</cmz:ayuda>
		                          </td>
		                        </tr>
		                        <tr>
		                        	<td><cmz:etiqueta titulo="Almacén hasta"/>:</td>
		                          	<td>
			                            <cmz:ayuda requerido="false" soloLectura="false" nombreAyuda="ALMACENES">
		          					  		<cmz:codigoAyuda idCodigo="codAlmacenHasta" anchuraCodigo="130px" longitudMaximaCodigo="4"></cmz:codigoAyuda>
		          					  		<cmz:descripcionAyuda idDescripcion="desAlmacenHasta" anchuraDescripcion="400px"></cmz:descripcionAyuda>
		          				    	</cmz:ayuda>
		                          </td>
		                        </tr>
							</table>
						</cmz:cuerpoPanel>
					</cmz:panel>
					<c:import url="/backoffice/devoluciones/estadisticas/versiones.jsp"></c:import>
				</form>
			</cmz:cuerpoPanelCMZ>
		</cmz:panelCMZ>
	</cmz:main>
</body>
</html>