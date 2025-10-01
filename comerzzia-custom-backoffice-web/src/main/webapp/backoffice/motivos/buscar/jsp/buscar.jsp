<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="cmz" uri="http://comerzzia.com/taglib"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:useBean id="paramBuscarMotivos"
	type="com.comerzzia.bricodepot.backoffice.services.motivos.ParametrosBuscarMotivosBean"
	scope="session" />
<jsp:useBean id="paginaResultados"
	class="com.comerzzia.core.util.paginacion.PaginaResultados"
	scope="request" />
<jsp:useBean id="permisos"
	class="com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean"
	scope="request" />


<%@page
	import="com.comerzzia.bricodepot.backoffice.services.motivos.ParametrosBuscarMotivosBean"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Comerzzia</title>

<cmz:head />

<script type="text/javascript" language="javascript" src="comun/js/comun.js"></script>
<script type="text/javascript" language="javascript" src="comun/js/mantenimiento.js"></script>

<script type="text/javascript">
        function inicio() {
            document.getElementById("codigo").focus();
        }
        </script>


</head>

<body onload="inicio();">
	<cmz:main>
		<cmz:panelCMZ>
			<cmz:cabeceraPanelCMZ titulo="${permisos.accionMenu.titulo}"
				icono="${permisos.accionMenu.icono}">
				<cmz:acciones numAccionesVisibles="3">
					<c:if test="${permisos.puedeAñadir}">
						<cmz:accionNuevoRegistro onclick="alta();"
							descripcion="Alta de un nuevo motivo" />
					</c:if>
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
				<form id="frmDatos" name="frmDatos" action="motivos"
					method="POST">
					<input id="accion" name="accion" type="hidden" value="" /> <input
						id="operacion" name="operacion" type="hidden" value="" /> <input
						id="columna" name="columna" type="hidden" value="" /> <input
						id="pagina" name="pagina" type="hidden" value="" /> <input
						id="idObjeto" name="idObjeto" type="hidden" value="" />


					<cmz:panel>
						<cmz:cabeceraPanel titulo="Búsqueda de Motivos"></cmz:cabeceraPanel>
						<cmz:cuerpoPanel>
							<table cellpadding="0px" cellspacing="0px" border="0px">
								<tr>
									<td>
										<table cellpadding="2px" cellspacing="2px" border="0px">
											<tr>
												<td><cmz:etiqueta titulo="Código" />:</td>
												<td><cmz:campoTexto id="codigo"
														valor="${paramBuscarMotivos.codigo}"
														anchura="40px" longitudMaxima="4" /></td>
												<td><cmz:botonConsultar onclick="consultar();" /></td>
											</tr>

											<tr>
												<td><cmz:etiqueta titulo="Descripción" />:</td>
												<td><cmz:campoTexto id="descripcion"
														valor="${paramBuscarMotivos.descripcion}"
														anchura="150px" longitudMaxima="40" /></td>
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
											ordenActual="${paramBuscarMotivos.orden}">
											<cmz:itemCabeceraListaPaginada nombre="Código" columna="1"
												ordenColumna="CODIGO"></cmz:itemCabeceraListaPaginada>
											<cmz:itemCabeceraListaPaginada nombre="Descripción"
												columna="2" ordenColumna="DESCRIPCION"></cmz:itemCabeceraListaPaginada>
											<cmz:itemCabeceraListaPaginada nombre="Comentario"
												columna="3"></cmz:itemCabeceraListaPaginada>
											<cmz:itemCabeceraListaPaginada nombre="Acciones"
												estilo="text-align: center;"></cmz:itemCabeceraListaPaginada>
										</cmz:cabeceraListaPaginada>

										<cmz:contenidoListaPaginada variable="motivo"
											paginaResultados="${paginaResultados}">
											<cmz:itemContenidoListaPaginada valor="${motivo.codigo}"
												onclick="ver('${motivo.codigo}');"></cmz:itemContenidoListaPaginada>
												
										<c:set var="idClase" value="X_MOTIVOS_TBL.DESCRIPCION" />
										<c:set var="desMotInter" value="${motivo.getTraduccionLenguaje(idClase, idiomaSeleccionado)}" />
											<cmz:itemContenidoListaPaginada
												valor="${desMotInter != null ? desMotInter.valor : motivo.descripcion}"></cmz:itemContenidoListaPaginada>
											<cmz:itemContenidoListaPaginada
												valor="${motivo.comentario}"></cmz:itemContenidoListaPaginada>
											<cmz:acciones alineacion="center">
												<cmz:accion icono="comun/images/iconos/i-busca.gif"
													onclick="ver('${motivo.codigo}');"
													descripcion="Ver Motivo"></cmz:accion>
												<c:if test="${permisos.puedeEditar}">
													<cmz:accion icono="comun/images/iconos/i-edit.gif"
														onclick="editar('${motivo.codigo}');"
														descripcion="Editar Motivo"></cmz:accion>
												</c:if>
												<c:if test="${permisos.puedeEliminar}">
													<cmz:accion icono="comun/images/iconos/i-cancel.gif"
														onclick="eliminar('${motivo.codigo}');"
														descripcion="Eliminar Motivo"></cmz:accion>
												</c:if>
											</cmz:acciones>
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
