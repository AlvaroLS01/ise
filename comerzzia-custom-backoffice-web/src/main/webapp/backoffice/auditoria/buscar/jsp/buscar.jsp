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
<jsp:useBean id="paginaResultados"
	class="com.comerzzia.core.util.paginacion.PaginaResultados"
	scope="request" />
<jsp:useBean id="permisos"
	class="com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean"
	scope="request" />

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
	src="comun/js/formulario.js"></script>
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

        function imprimir(){
            document.getElementById("accion").value = "auditoria.lstEventosAuditoria";
            document.getElementById("operacion").value = "ejecutar";
			document.getElementById("frmDatos").submit();
			_esperando();
		}
    </script>
</head>

<body onload="inicio();">
	<cmz:main>
		<cmz:panelCMZ>
			<cmz:cabeceraPanelCMZ titulo="${permisos.accionMenu.titulo}"
				icono="${permisos.accionMenu.icono}">
				<cmz:acciones numAccionesVisibles="3">
					<cmz:accion titulo="Imprimir"
						icono="comun/images/iconos/i-impresora.gif"
						descripcion="Imprimir listado artículos" onclick="imprimir()" />
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

					<c:choose>
						<c:when test="${paginaResultados.pagina != null}">
							<cmz:panel>
								<cmz:cabeceraPanelResultados
									numPorPagina="${paginaResultados.tamañoPagina}"></cmz:cabeceraPanelResultados>
								<cmz:cuerpoPanel>
									<cmz:listaPaginada>
										<cmz:cabeceraListaPaginada
											ordenActual="${paramBuscarEventoAuditoria.orden}">
											<cmz:itemCabeceraListaPaginada nombre="Tienda" columna="1"
												ordenColumna="CODALM"></cmz:itemCabeceraListaPaginada>
											<cmz:itemCabeceraListaPaginada nombre="Fecha" columna="2"
												ordenColumna="FECHA"></cmz:itemCabeceraListaPaginada>
											<cmz:itemCabeceraListaPaginada nombre="Descripción"
												columna="3" ordenColumna="TIPO_EVENTO"></cmz:itemCabeceraListaPaginada>
											<cmz:itemCabeceraListaPaginada nombre="Usuario" columna="4"
												ordenColumna="DES_USUARIO"></cmz:itemCabeceraListaPaginada>
											<cmz:itemCabeceraListaPaginada nombre="Supervisor"
												columna="5" ordenColumna="DES_USUARIO_SUPERVISOR"></cmz:itemCabeceraListaPaginada>
											<cmz:itemCabeceraListaPaginada nombre="Ticket" columna="6"
												ordenColumna="UID_TICKET_VENTA"></cmz:itemCabeceraListaPaginada>
											<cmz:itemCabeceraLista nombre="" columna="7"
												ordenColumna="CODART"></cmz:itemCabeceraLista>
											<cmz:itemCabeceraListaPaginada nombre="Articulo" columna="8"
												ordenColumna="DESART"></cmz:itemCabeceraListaPaginada>
											<cmz:itemCabeceraLista nombre="Uds." columna="9"
												estilo="text-align: center;"></cmz:itemCabeceraLista>
											<cmz:itemCabeceraLista nombre="€/Ud." columna="10"
												estilo="text-align: right;"></cmz:itemCabeceraLista>
											<cmz:itemCabeceraLista nombre="Nuevo €/Ud." columna="11"
												estilo="text-align: right;"></cmz:itemCabeceraLista>
<%-- 											<cmz:itemCabeceraListaPaginada nombre="Acciones" --%>
<%-- 												estilo="text-align: center;"></cmz:itemCabeceraListaPaginada> --%>
										</cmz:cabeceraListaPaginada>

										<cmz:contenidoListaPaginada variable="evento"
											paginaResultados="${paginaResultados}">
											<cmz:itemContenidoListaPaginada
												valor="${evento.almacen.desAlm}"></cmz:itemContenidoListaPaginada>
											<cmz:itemContenidoListaPaginada>
												<fmt:formatDate type="both" dateStyle="short"
													timeStyle="short" value="${evento.fecha}" />
											</cmz:itemContenidoListaPaginada>
											<cmz:itemContenidoListaPaginada valor="${evento.tipoEvento}"></cmz:itemContenidoListaPaginada>
											<cmz:itemContenidoListaPaginada
												valor="${evento.nombreUsuario}"></cmz:itemContenidoListaPaginada>
											<cmz:itemContenidoListaPaginada
												valor="${evento.nombreUsuarioSupervisor}"></cmz:itemContenidoListaPaginada>
											<cmz:itemContenidoListaPaginada
												valor="${evento.ticketVenta!=null?evento.ticketVenta.codTicket:evento.uidTicketVenta}"></cmz:itemContenidoListaPaginada>
											<cmz:itemContenidoLista valor="${evento.codigoArticulo}"></cmz:itemContenidoLista>
											<cmz:itemContenidoListaPaginada
												valor="${evento.descripcionArticulo}"></cmz:itemContenidoListaPaginada>
											<cmz:itemContenidoLista alineacion="center">
												<cmz:formateaNumero valor="${evento.cantidadArticulo}"
													numDecimales="0" />
											</cmz:itemContenidoLista>
											<cmz:itemContenidoLista alineacion="right">
												<cmz:formateaNumero valor="${evento.precioOriginal}"
													numDecimales="2" />
											</cmz:itemContenidoLista>
											<cmz:itemContenidoLista alineacion="right">
												<cmz:formateaNumero valor="${evento.precioAplicado}"
													numDecimales="2" />
											</cmz:itemContenidoLista>

<%-- 											<cmz:acciones alineacion="center"> --%>
<%-- 												<cmz:accion icono="comun/images/iconos/i-busca.gif" --%>
<%-- 													onclick="ver('${evento.idTicketAuditoria}');" --%>
<%-- 													descripcion="Ver detalles"></cmz:accion> --%>

<%-- 											</cmz:acciones> --%>
										</cmz:contenidoListaPaginada>
									</cmz:listaPaginada>
								</cmz:cuerpoPanel>
							</cmz:panel>
						</c:when>
					</c:choose>
				</form>
			</cmz:cuerpoPanelCMZ>
		</cmz:panelCMZ>
	</cmz:main>
</body>
</html>