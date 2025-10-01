<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="cmz" uri="http://comerzzia.com/taglib"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:useBean id="motivo"
	type="com.comerzzia.bricodepot.backoffice.persistence.motivos.Motivos"
	scope="session" />
<jsp:useBean id="permisos"
	class="com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean"
	scope="request" />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Comerzzia</title>

<cmz:head />

<script type="text/javascript" language="javascript" src="comun/js/comun.js"></script>
<script type="text/javascript" language="javascript" src="comun/js/mantenimiento.js"></script>
<script type="text/javascript" language="javascript" src="comun/js/validacion.js"></script>
<script type="text/javascript" language="javascript" src="comun/js/i18n.js"></script>
<script type="text/javascript" language="javascript" src="comun/js/jquery-ui-1.9.2.min.js"></script>
<script type="text/javascript" language="javascript" src="comun/js/formulario.js"></script>
<script type="text/javascript" language="javascript" src="comun/js/ayudas.js"></script>

<script type="text/javascript">
	function inicio() {
		document.getElementById("codigo").focus();
	}

	function checkForm() {
		if (!esValido("descripcion", "TEXTO", true)) {
			return false;
		}
		return true;
	}

	function aceptar() {
		if (checkForm()) {
			document.getElementById("accion").value = "salvar";
			document.getElementById("frmDatos").submit();
			_esperando();
		}
	}
</script>
</head>

<body onload="inicio();" onkeydown="keyDown(event);">
	<cmz:main>
		<cmz:panelCMZ>
			<cmz:cabeceraPanelCMZ titulo="${permisos.accionMenu.titulo}"
				icono="${permisos.accionMenu.icono}">
				<cmz:acciones numAccionesVisibles="2">
					<c:choose>
						<c:when test="${!motivo.enEdicion}">
							<cmz:accion icono="comun/images/iconos/i-volver.gif"
								onclick="volver();" titulo="Volver" descripcion="Volver"></cmz:accion>
								
							<c:if test="${permisos.puedeEditar}">
								<cmz:accion icono="comun/images/iconos/i-edit.gif"
									onclick="editar();" titulo="Editar" descripcion="Editar Motivo"></cmz:accion>
							</c:if>
							<c:if test="${permisos.puedeAñadir}">
								<cmz:accionNuevoRegistro onclick="alta();"
									descripcion="Añadir un nuevo Motivo" />
							</c:if>
							<c:if test="${permisos.puedeEliminar}">
								<cmz:accion icono="comun/images/iconos/i-cancel.gif"
									onclick="eliminar();" titulo="Eliminar"
									descripcion="Eliminar Motivo"></cmz:accion>
							</c:if>
						</c:when>
						<c:otherwise>
							<cmz:accionSalvar onclick="aceptar();" />
							<cmz:accionCancelar onclick="volver();" />
						</c:otherwise>
					</c:choose>
				</cmz:acciones>
			</cmz:cabeceraPanelCMZ>

			<cmz:cuerpoPanelCMZ>
				<cmz:mensaje />
				<form id="frmDatos" name="frmDatos" action="motivos" method="post">
					<input id="accion" name="accion" type="hidden" value="" />
					<input id="operacion" name="operacion" type="hidden" value="" />
					<input id="idObjeto" name="idObjeto" type="hidden" value="${motivo.codigo}" />

					<cmz:panel>
						<cmz:cabeceraPanel titulo="Datos del Motivo"></cmz:cabeceraPanel>
						<cmz:cuerpoPanel>
							<table cellpadding="2px" cellspacing="2px" border="0px">
							<c:if test="${motivo.estadoBean != 3}">
								<tr>
									<td><cmz:etiqueta titulo="Código" />:</td>
									<td><cmz:campoTexto id="codigo" valor="${motivo.codigo}"
											requerido="true" anchura="30px" longitudMaxima="15"
											editable="false"
											soloLectura="true" /></td>
								</tr>
								</c:if>
								<tr>
									<c:set var="idClase" value="X_MOTIVOS_TBL.DESCRIPCION" />
									<c:set var="desMotInter"
										value="${motivo.getTraduccionLenguaje(idClase, idiomaSeleccionado)}" />
									<c:choose>
										<c:when test="${motivo.estadoNuevo}">
											<c:set var="desMot" value="${motivo.descripcion}" />
										</c:when>
										<c:otherwise>
											<c:set var="desMot"
												value="${desMotInter != null ? desMotInter.valor : motivo.descripcion}" />
										</c:otherwise>
									</c:choose>

									<td><cmz:etiqueta titulo="Descripción" />:</td>
									<td><cmz:campoI18N id="descripcion" valor="${desMot}"
											requerido="true" anchura="400px" longitudMaxima="60"
											campoEditable="${motivo.estadoBean == 3}"
											soloLectura="${!motivo.enEdicion}"
											traduccionEditable="${motivo.enEdicion}" idClase="${idClase}"
											nombreObjetoSesion="motivo"
											traducido="${desMotInter != null}" /></td>
								</tr>
								<tr>
									<%-- 									<td><cmz:etiqueta titulo="Tipo de Motivo" />:</td> --%>
									<%-- 									<td><cmz:campoTexto id="tipo" valor="${tipo}" --%>
									<%-- 											requerido="true" anchura="400px" longitudMaxima="60" --%>
									<%-- 											soloLectura="${!motivo.enEdicion}" /></td> --%>

									<td><cmz:etiqueta titulo="Tipo de Motivo :" /></td>
									<td><select class="campo requerido" id="tipoMotivo"
										name="tipoMotivo"
										<c:if test="${!motivo.enEdicion && motivo.estadoBean != 3}">disabled="disabled"</c:if>>
											<option value="">
												<cmz:etiqueta titulo="Seleccione tipo motivo" /></option>
											<c:forEach items="${tiposMotivos}" var="tipoMotivo">
												<option value="${tipoMotivo.codigoTipo}"
													<c:if test="${tipoMotivo.codigoTipo == motivo.codigoTipo}">
													selected = "selected"
												</c:if>>
													<cmz:etiqueta titulo="${tipoMotivo.tipo}" />
												</option>
											</c:forEach>
									</select></td>
								</tr>

								<tr>	
									<td><cmz:etiqueta titulo="Comentario" />:</td>
									<td>
										<cmz:campoAreaTexto  id="comentario" valor="${motivo.comentario}"
											requerido="true" anchura="300px" altura="100px" longitudMaxima="250"
											editable="false"
											soloLectura="true" /></td>
								</tr>
							</table>
						</cmz:cuerpoPanel>
					</cmz:panel>
				</form>
			</cmz:cuerpoPanelCMZ>
		</cmz:panelCMZ>
	</cmz:main>
</body>
</html>
