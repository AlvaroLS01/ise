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

<jsp:useBean id="paramBuscarEventoAuditoria"
	type="com.comerzzia.bricodepot.backoffice.persistence.tickets.auditoria.ParametrosBuscarEventoAuditoria"
	scope="session" />
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
	src="comun/js/calendario.js"></script>
<script type="text/javascript" language="javascript"
	src="comun/js/comun.js"></script>
<script type="text/javascript" language="javascript"
	src="comun/js/ayudas.js"></script>
<script type="text/javascript" language="javascript"
	src="comun/js/validacion.js"></script>

<script type="text/javascript">
		function inicio() {
    		setFoco("usuario");
    		pintaCalendario("fechaDesde", "imgFechaDesde");
			pintaCalendario("fechaHasta", "imgFechaHasta");
		}

		function limpiarFechas(){
			pintaCalendario("fechaDesde", "imgFechaDesde");
			pintaCalendario("fechaHasta", "imgFechaHasta");  	        	        	
		}
	    
	    function volver() {
	    	document.getElementById("accion").value = "buscar";
	        document.getElementById("frmDatos").submit();
	    	_esperando();
	    }
	
	    function checkForm() {
			if (!esValido("usuario", "TEXTO", false)) {
				return false;
			}
			if (!esValido("supervisor", "TEXTO", false)) {
				return false;
			}
			if(!esValido("fechaDesde", "FECHA", false)) {
                return false;
            }
            if(!esValido("fechaHasta", "FECHA", false)) {
                return false;
            }
            return true;
        }
	
	    function imprimir(idVersion) {
	    	if(checkForm()) {
	            document.getElementById("accion").value = "auditoria";
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
				<form id="frmDatos" name="frmDatos" action="auditoria" method="post">
					<input id="accion" name="accion" type="hidden" value="" /> <input
						id="operacion" name="operacion" type="hidden" value="" /> <input
						id="columna" name="columna" type="hidden" value="" /> <input
						id="pagina" name="pagina" type="hidden" value="" /> <input
						id="idObjeto" name="idObjeto" type="hidden" value="" />

					<cmz:panel>
						<cmz:cabeceraPanel titulo="Auditoria"></cmz:cabeceraPanel>
						<cmz:cuerpoPanel>
							<table cellpadding="0px" cellspacing="0px" border="0px">
								<tr>
									<td>
										<table cellpadding="2px" cellspacing="2px" border="0px">
											<tr>
												<td><cmz:etiqueta titulo="Usuario" />:</td>
												<td><cmz:campoTexto id="usuario"
														valor="${paramBuscarEventoAuditoria.usuario}"
														anchura="130px" longitudMaxima="20" /></td>
											</tr>
											<tr>
												<td><cmz:etiqueta titulo="Usuario supervisor" />:</td>
												<td><cmz:campoTexto id="supervisor"
														valor="${paramBuscarEventoAuditoria.usuarioSupervisor}"
														anchura="130px" longitudMaxima="20" /></td>
											</tr>
											<tr>
												<td><cmz:etiqueta titulo="Fecha desde" /></td>
												<td><cmz:campoFecha id="fechaDesde" editable="true"
														mostrarCheckbox="false" anchura="120px">
														<fmt:formatDate pattern="dd/MM/yyyy"
															value="${paramBuscarEventoAuditoria.fechaDesde}" />
													</cmz:campoFecha></td>
											</tr>
											<tr>
												<td><cmz:etiqueta titulo="Fecha hasta" /></td>
												<td><cmz:campoFecha id="fechaHasta" editable="true"
														mostrarCheckbox="false" anchura="120px">
														<fmt:formatDate pattern="dd/MM/yyyy"
															value="${paramBuscarEventoAuditoria.fechaHasta}" />
													</cmz:campoFecha></td>
											</tr>
											<tr>
												<td><cmz:botonConsultar onclick="consultar();" /></td>
											</tr>
										</table>
									</td>
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