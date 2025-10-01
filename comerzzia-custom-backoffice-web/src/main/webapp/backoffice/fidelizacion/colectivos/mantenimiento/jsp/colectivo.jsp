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
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="cmz" uri="http://comerzzia.com/taglib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="formularioColectivo" type="com.comerzzia.web.fidelizacion.colectivos.ui.FormularioColectivoBean" scope="session" />
<jsp:useBean id="permisos" class="com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean" scope="request" />

<c:set var="colectivo" value="${formularioColectivo.registroActivo}" />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Comerzzia</title>
    
    <cmz:head/>
    
    <link rel="stylesheet" type="text/css" media="screen,projection,print" href="comun/css/calendario.css" />
    
    <script type="text/javascript" language="javascript" src="comun/js/ayudas.js"></script>
    <script type="text/javascript" language="javascript" src="comun/js/calendario.js"></script>
    <script type="text/javascript" language="javascript" src="comun/js/comun.js"></script>
    <script type="text/javascript" language="javascript" src="comun/js/formulario.js"></script>
    <script type="text/javascript" language="javascript" src="comun/js/validacion.js"></script>
    <script type="text/javascript" language="javascript" src="comun/js/i18n.js"></script>
    
    <script type="text/javascript">
	    function inicio() {
	    	setFoco("descripcion");

            if(window.inicioRegistroPestaña) {
            	inicioRegistroPestaña();
            }
        }

	    function checkForm() {
            if (!esValido("codColectivo", "TEXTO", true)) {
                return false;
            }
            if (!esValido("desColectivo", "TEXTO", true)) {
                return false;
            }            
            return true;
        }
	    
	    function asignarEtiquetas(){
	    	document.getElementById("accion").value = "asignarEtiquetas";
		    document.getElementById("frmDatos").submit();
			_esperando();
	    }
	    
	    function duplicar(){
        	document.getElementById("accion").value = "leerFormulario";
        	document.getElementById("operacion").value = "duplicar";
        	document.getElementById("frmDatos").submit();
        	_esperando();    	    
        }
	    
	    function importar(){
	    	document.getElementById("accion").value = "importar";
		    document.getElementById("frmDatos").submit();
			_esperando();
	    }
    </script>
  </head>
  <body  onload="inicio();" onkeydown="keyDown(event);">
    <cmz:main>
      <cmz:panelCMZ>
        <cmz:cabeceraPanelCMZ titulo="${permisos.accionMenu.titulo}" icono="${permisos.accionMenu.icono}">
          <cmz:acciones numAccionesVisibles="3">
            <c:choose>
              <c:when test="${!formularioColectivo.editable}">
                <cmz:accion icono="comun/images/iconos/i-volver.gif" onclick="volver();" titulo="Volver" descripcion="Volver"></cmz:accion>
                <c:if test="${permisos.puedeEditar}">
                  <cmz:accion icono="comun/images/iconos/i-edit.gif" onclick="editar();" titulo="Editar" descripcion="Editar colectivo"></cmz:accion>
                </c:if>
                <cmz:accion onclick="importar()" titulo="Importar" icono="comun/images/iconos/i-importar-excell.gif" descripcion="Importar" />
                <c:if test="${permisos.puedeEditar}">
                  <cmz:accion icono="comun/images/menu/unidadesmedidaetiquetas.gif" onclick="asignarEtiquetas();" titulo="Asignar etiquetas" descripcion="Asignar etiquetas"></cmz:accion>
                </c:if>
                <c:if test="${permisos.puedeAñadir}">
                  <cmz:accion icono="comun/images/iconos/i-alta.gif" onclick="alta();" titulo="Añadir" descripcion="Añadir un nuevo colectivo"></cmz:accion>
                </c:if>
                <c:if test="${permisos.puedeEliminar}">
                  <cmz:accion icono="comun/images/iconos/i-cancel.gif" onclick="eliminar();" titulo="Eliminar" descripcion="Eliminar colectivo"></cmz:accion>
                </c:if>
                  <cmz:accion icono="comun/images/iconos/colectivos.gif" onclick="duplicar();" titulo="Duplicar" descripcion="Duplica el Colectivo"/>
              </c:when>
              <c:otherwise>
                <cmz:accion icono="comun/images/iconos/aceptar.gif" onclick="aceptar();" titulo="Aceptar"></cmz:accion>
                <cmz:accion icono="comun/images/iconos/cancelar.gif" onclick="cancelar();" titulo="Cancelar"></cmz:accion>
              </c:otherwise>
            </c:choose>
          </cmz:acciones>
        </cmz:cabeceraPanelCMZ>
        <cmz:cuerpoPanelCMZ>
          <cmz:mensaje/>
          <form id="frmDatos" name="frmDatos" action="colectivos" method="post">
          	<input id="accion" name="accion" type="hidden" value="" />
            <input id="operacion" name="operacion" type="hidden" value="" />
            <input id="indice" name="indice" type="hidden" value=""/>
            <input id="idObjeto" name="idObjeto" type="hidden" value="${colectivo.codColectivo}" />                        
            <input id="vacia" name="vacia" type="hidden" value="" />
            <cmz:panel>
				<cmz:cabeceraPanel titulo="Colectivo"></cmz:cabeceraPanel>
				<cmz:cuerpoPanel>
					<table cellpadding="2px" cellspacing="2px" border="0px">
						<tr>	        
							<td><cmz:etiqueta titulo="Código:"></cmz:etiqueta></td>
							<td>
								<cmz:campoTexto id="codColectivo" valor="${colectivo.codColectivo}" requerido="true" anchura="350px" longitudMaxima="40"  
										editable="${formularioColectivo.enInsercion}" soloLectura="${!formularioColectivo.enInsercion}"/>
							</td>
							<td>
								<input type="checkbox" value="" id="activo" name="activo"  <c:if test="${colectivo.activo}">checked="checked"</c:if> 
								<c:if test="${!formularioColectivo.editable}">disabled="disabled"</c:if>/>Activo
							</td>
						</tr>
						<tr>
						<c:set var="idClaseDesCol" value="D_COLECTIVOS_TBL.DES_COLECTIVO"/>
                   		<c:set var="desColInter" value= "${colectivo.getTraduccionLenguaje(idClaseDesCol, idiomaSeleccionado)}"/>
                    	<c:choose>
                    		<c:when test="${formularioColectivo.editable}">
                    			<c:set var="desCol" value="${colectivo.desColectivo}"/>
                    		</c:when>
                    		<c:otherwise>
                    			<c:set var="desCol" value="${desColInter != null ? desColInter.valor : colectivo.desColectivo}"/>
                    		</c:otherwise>
                    	</c:choose>
							<td><cmz:etiqueta titulo="Descripción:"></cmz:etiqueta></td>
							<%-- <td>
								<cmz:campoTexto id="desColectivo" valor="${colectivo.desColectivo}" requerido="true" anchura="800px" longitudMaxima="255"
										editable="${formularioColectivo.editable}" soloLectura="${!formularioColectivo.editable}"/>
							</td> --%>
                   		
                    	<td>
                    	<cmz:campoI18N id="desColectivo" valor="${desCol}" requerido="true" anchura="800px" longitudMaxima="255"
                           campoEditable="${formularioColectivo.editable}" soloLectura="${!formularioColectivo.editable}" traduccionEditable="${formularioColectivo.editable}"
                           idClase="${idClaseDesCol}" nombreObjetoSesion="formularioColectivo" traducido="${desColInter != null}"/>
                           </td>
						</tr>
						<tr>
							<td><cmz:etiqueta titulo="Tipo:"></cmz:etiqueta></td>
							<td>
								<cmz:ayuda nombreAyuda="TIPOS_COLECTIVOS" soloLectura="${!formularioColectivo.editable}">
									<cmz:codigoAyuda idCodigo="codtipcolectivo" longitudMaximaCodigo="40" anchuraCodigo="50px" valorCodigo="${colectivo.codtipcolectivo}"/>
									<cmz:descripcionAyuda idDescripcion="destipcolectivo" anchuraDescripcion="250px"  valorDescripcion="${colectivo.destipcolectivo}"/>
								</cmz:ayuda>
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" value="visualizacionAltas" id="visualizacionAltas" name="visualizacionAltas"  <c:if test="${colectivo.extensiones['visualizacionAltas']}">checked="checked"</c:if> 
								<c:if test="${!formularioColectivo.editable}">disabled="disabled"</c:if>/><cmz:etiqueta titulo="Visualización altas">:</cmz:etiqueta>
							</td>
							<td>
								<input type="checkbox" value="profesional" id="profesional" name="profesional"  <c:if test="${colectivo.extensiones['profesional']}">checked="checked"</c:if> 
								<c:if test="${!formularioColectivo.editable}">disabled="disabled"</c:if>/><cmz:etiqueta titulo="Profesional">:</cmz:etiqueta>
							</td>
						</tr>
					</table>
				</cmz:cuerpoPanel>
			</cmz:panel>
            
            <c:if test="${!formularioColectivo.enInsercion}">
            	<cmz:pestañas>
              		<cmz:cabeceraPestañas pestañaActiva="${formularioColectivo.pestañaActiva}">
                	<cmz:pestaña titulo="Fidelizados" accion="0" onclick="seleccionaPestaña(0);"></cmz:pestaña>  
                	<cmz:pestaña titulo="Campañas" accion="1" onclick="seleccionaPestaña(1);"></cmz:pestaña>       		
              	</cmz:cabeceraPestañas>
              	<cmz:cuerpoPestaña>
					<c:choose>
						<c:when test="${formularioColectivo.pestañaActiva == 1}">
							<c:import url="campsAccMarketing.jsp"></c:import>
						</c:when>   
						<c:otherwise>                                        
							<c:import url="fidelizados.jsp"></c:import>              
						</c:otherwise>
					</c:choose>
              	</cmz:cuerpoPestaña>
            	</cmz:pestañas>
            </c:if>
          </form>
        </cmz:cuerpoPanelCMZ>
      </cmz:panelCMZ>
    </cmz:main>
  </body>
</html>
