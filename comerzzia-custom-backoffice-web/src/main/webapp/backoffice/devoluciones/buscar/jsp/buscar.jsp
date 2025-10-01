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

<jsp:useBean id="paramBuscarDevoluciones"
	type="com.comerzzia.bricodepot.backoffice.persistence.devoluciones.validacion.ParametrosBuscarValidacionesDevolucionAlbaran"
	scope="session" />
<jsp:useBean id="paginaResultados"
	class="com.comerzzia.core.util.paginacion.PaginaResultados"
	scope="request" />
<jsp:useBean id="permisos"
	class="com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean"
	scope="request" />
	
<jsp:useBean id="permisosInforme" class="com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean" scope="request" />
<jsp:useBean id="trabajoInforme" type="com.comerzzia.core.model.informes.TrabajoInformeBean" scope="session" />

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
	            setFoco("referenciaCliente");
	
	    		pintaCalendario("fechaDesde", "imgFechaDesde");
				pintaCalendario("fechaHasta", "imgFechaHasta");
	        }
	
	        function limpiarFechas(){
	
	    		pintaCalendario("fechaDesde", "imgFechaDesde");
				pintaCalendario("fechaHasta", "imgFechaHasta");
			}
	
	        function checkForm() {
				/*if (!esValido("usuario", "TEXTO", false)) {
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
	            }*/
	            return true;
	        }
	
	        function imprimirTicket(uidTicket){
	        	document.getElementById("tipo").value="pdf";
				document.getElementById("idInforme").value = '${trabajoInforme.idInforme}';				
				document.getElementById("accion").value ='${trabajoInforme.informe}';
		    	document.getElementById("operacion").value = "imprimirRapido";	    	
		    	document.getElementById("uidTicket").value = uidTicket;	    	
		    	document.getElementById("frmDatos").target = "_blank";
	    		document.getElementById("frmDatos").submit();
	    		document.getElementById("frmDatos").target = "_self";
			}
	        
	        function imprimir() {
		    	if(checkForm()) {
		            document.getElementById("accion").value = "devoluciones.lstValidacion";
		            document.getElementById("operacion").value = "ejecutar";
					document.getElementById("frmDatos").submit();
		    	}
		    }
	        
	        function validar(idClieAlbaran, linea){
	        	document.getElementById("accion").value = "validar";
	            document.getElementById("operacion").value = "ejecutar";
	            document.getElementById("idClieAlbaran").value = idClieAlbaran;
	            document.getElementById("linea").value = linea;
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
					<cmz:acciones numAccionesVisibles="2">
						<cmz:accion titulo="Imprimir"
							icono="comun/images/iconos/i-impresora.gif" 
							descripcion="Imprimir" onclick="imprimir()" /> 
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
					<form id="frmDatos" name="frmDatos" action="devoluciones" method="post">
						<input id="accion" name="accion" type="hidden" value="" /> 
							<input id="operacion" name="operacion" type="hidden" value="" />
							<input id="columna" name="columna" type="hidden" value="" />
							<input id="pagina" name="pagina" type="hidden" value="" /> 
							<input id="idObjeto" name="idObjeto" type="hidden" value="" />
							<input id="idClieAlbaran" name="idClieAlbaran" type="hidden" value="" />
							<input id="linea" name="linea" type="hidden" value="" />

							<input type="hidden" id="informe" name="informe" value="" /> 
							<input type="hidden" id="idVersion" name="idVersion" value="" /> 
							<input type="hidden" id="version" name="version" value="" />
							<input type="hidden" id="idInforme" name="idInforme" value="" /> 
							<input type="hidden" id="tipo" name="tipo" value="" />
							<input type="hidden" id="referenciaClienteTicket" name="referenciaClienteTicket" value="" />
							<input type="hidden" id="uidTicket" name="uidTicket" value="" />
		
						<cmz:panel>
							<cmz:cabeceraPanel titulo="Devoluciones"></cmz:cabeceraPanel>
							<cmz:cuerpoPanel>
								<table cellpadding="0px" cellspacing="0px" border="0px">
									<tr>
										<td>
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
													<td><cmz:etiqueta titulo="Tienda"/>:</td>
													<td>
														<cmz:ayuda nombreAyuda="TIENDAS">
														<cmz:codigoAyuda idCodigo="codAlm" valorCodigo="${paramBuscarDevoluciones.codAlm}"
															anchuraCodigo="50px" longitudMaximaCodigo="4"></cmz:codigoAyuda>
														<cmz:descripcionAyuda idDescripcion="desAlm" valorDescripcion="${paramBuscarDevoluciones.desAlm}"
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
													<td><cmz:etiqueta titulo="Articulo"/>:</td>
													<td>
														<cmz:ayuda requerido="false" soloLectura="false" nombreAyuda="ARTICULOS">
								          					<cmz:codigoAyuda idCodigo="codArt" valorCodigo="${paramBuscarDevoluciones.codArt}" anchuraCodigo="100px" longitudMaximaCodigo="20"></cmz:codigoAyuda>
								          					<cmz:descripcionAyuda idDescripcion="desArt" valorDescripcion="${paramBuscarDevoluciones.desArt}" anchuraDescripcion="300px"></cmz:descripcionAyuda>
								          				</cmz:ayuda>
													</td>
												</tr>
												<tr>
													<td><cmz:etiqueta titulo="Numero de factura"/>:</td>
													<td>
														<cmz:campoTexto id="referenciaCliente" valor="${paramBuscarDevoluciones.referenciaCliente}" requerido="false" anchura="200px" longitudMaxima="20" />
													</td>
												</tr>
												<tr>
													<td><cmz:etiqueta titulo="Validado"/>:</td>
													<td>
							                          	<select id="validado" name="validado" class="campo">
							                          		<option value="" <c:if test="${paramBuscarDevoluciones.validado == ''}">selected="selected"</c:if>><cmz:etiqueta titulo="Todos"/></option>
							                          		<option value="S" <c:if test="${paramBuscarDevoluciones.validado == 'S'}">selected="selected"</c:if>><cmz:etiqueta titulo="Validados"/></option>
							                          		<option value="N" <c:if test="${paramBuscarDevoluciones.validado == 'N'}">selected="selected"</c:if>><cmz:etiqueta titulo="No validados"/></option>
							                          	</select>
							                          </td>
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
									<cmz:cabeceraPanelResultados numPorPagina="${paginaResultados.tamañoPagina}">
									</cmz:cabeceraPanelResultados>
									<cmz:cuerpoPanel>
										<cmz:listaPaginada>
											<cmz:cabeceraListaPaginada
												ordenActual="${paramBuscarEventoAuditoria.orden}">
												<cmz:itemCabeceraListaPaginada nombre="Fecha" columna="1"
													ordenColumna="fecha_albaran"></cmz:itemCabeceraListaPaginada>
												<cmz:itemCabeceraListaPaginada nombre="Supervisor" columna="2"
													ordenColumna="des_usuario_supervisor"></cmz:itemCabeceraListaPaginada>
												<cmz:itemCabeceraListaPaginada nombre="Artículo" columna="3"
													ordenColumna="des_art"></cmz:itemCabeceraListaPaginada>
												<cmz:itemCabeceraListaPaginada nombre="Uds." columna="4"
													ordenColumna="cantidad" estilo="text-align: right;"></cmz:itemCabeceraListaPaginada>
												<cmz:itemCabeceraListaPaginada nombre="Motivo" columna="5"
													ordenColumna="des_motivo"></cmz:itemCabeceraListaPaginada>
												<cmz:itemCabeceraListaPaginada nombre="Importe" columna="6"
													ordenColumna="importe" estilo="text-align: right;"></cmz:itemCabeceraListaPaginada>
												<cmz:itemCabeceraLista nombre="Método pago"></cmz:itemCabeceraLista>
												<cmz:itemCabeceraListaPaginada nombre="Tienda" columna="7"
													ordenColumna="des_alm"></cmz:itemCabeceraListaPaginada>
												<cmz:itemCabeceraLista nombre="Referencia devolución" columna="8"
													ordenColumna="referencia_cliente"></cmz:itemCabeceraLista>
												<cmz:itemCabeceraLista nombre="Referencia venta" columna="9"
													ordenColumna="cod_albaran_origen"></cmz:itemCabeceraLista>
												<cmz:itemCabeceraLista nombre="Ticket"></cmz:itemCabeceraLista>
												<cmz:itemCabeceraLista nombre="Validacion"></cmz:itemCabeceraLista>
												<cmz:itemCabeceraLista nombre="Validado"></cmz:itemCabeceraLista>
												<cmz:itemCabeceraLista nombre="Validador" columna="10"
													ordenColumna="des_usuario_validador"></cmz:itemCabeceraLista>
												<cmz:itemCabeceraLista nombre="Fecha validación" columna="11"
													ordenColumna="fecha_validacion"></cmz:itemCabeceraLista>
											</cmz:cabeceraListaPaginada>
	
											<cmz:contenidoListaPaginada variable="validacion"
												paginaResultados="${paginaResultados}">
												<cmz:itemContenidoListaPaginada>
													<fmt:formatDate type="both" dateStyle="short"
														timeStyle="short" value="${validacion.fechaAlbaran}" />
												</cmz:itemContenidoListaPaginada>
												<cmz:itemContenidoListaPaginada valor="${validacion.desUsuarioSupervisor}"></cmz:itemContenidoListaPaginada>
												<cmz:itemContenidoListaPaginada valor="${validacion.desArt}"></cmz:itemContenidoListaPaginada>
												<cmz:itemContenidoLista alineacion="right">
													<cmz:formateaNumero valor="${validacion.cantidad}"
														numDecimales="0" />
												</cmz:itemContenidoLista>
												<cmz:itemContenidoListaPaginada valor="${validacion.desMotivo}"></cmz:itemContenidoListaPaginada>
												<cmz:itemContenidoLista alineacion="right">
													<cmz:formateaNumero valor="${validacion.importe}"
														numDecimales="2" />
												</cmz:itemContenidoLista>
												<cmz:itemContenidoListaPaginada valor="${validacion.medioPago}"></cmz:itemContenidoListaPaginada>
												<cmz:itemContenidoListaPaginada valor="${validacion.desAlm}"></cmz:itemContenidoListaPaginada>
												<cmz:itemContenidoListaPaginada valor="${validacion.referenciaCliente}"></cmz:itemContenidoListaPaginada>
												<cmz:itemContenidoListaPaginada valor="${validacion.codAlbaranOrigen}"></cmz:itemContenidoListaPaginada>
												<cmz:acciones alineacion="center">
													<cmz:accion icono="comun/images/iconos/pdf.png"
 													onclick="imprimirTicket('${validacion.uidTicket}');"
													descripcion="Ticket"></cmz:accion>
												</cmz:acciones>
												<cmz:acciones alineacion="center">
													<cmz:accion icono="comun/images/iconos/aceptar.gif"
 													onclick="validar('${validacion.idClieAlbaran}', '${validacion.linea}');"
													descripcion="Validar" titulo="Validar"></cmz:accion>
												</cmz:acciones>
												<cmz:itemContenidoListaPaginada>
													<c:choose>
														<c:when test="${validacion.validado}">
															SÍ
														</c:when>
														<c:otherwise>
															NO
														</c:otherwise>
													</c:choose>
												</cmz:itemContenidoListaPaginada>
												<cmz:itemContenidoListaPaginada valor="${validacion.desUsuarioValidador}"></cmz:itemContenidoListaPaginada>
												<cmz:itemContenidoListaPaginada>
													<fmt:formatDate type="both" dateStyle="short"
														timeStyle="short" value="${validacion.fechaValidacion}" />
												</cmz:itemContenidoListaPaginada>
												
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
