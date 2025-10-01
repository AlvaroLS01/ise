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

<jsp:useBean id="formularioTienda" type="com.comerzzia.web.general.tiendas.ui.FormularioTiendaBean" scope="session" />
<jsp:useBean id="permisos" class="com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean" scope="request" />
<jsp:useBean id="paramBuscarMediosPago" type="com.comerzzia.persistencia.general.mediospago.ParametrosBuscarMediosPagoBean" scope="session" />

<c:set var="tienda" value="${formularioTienda.registroActivo}" />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Comerzzia</title>
    
    <cmz:head/>
    <link rel="stylesheet" href="comun/css/jquery.tagit.css" type="text/css" media="all"/>
    <link rel="stylesheet" href="comun/css/tagit.ui-zendesk.css" type="text/css" media="all"/>
    
    <script type="text/javascript" language="javascript" src="comun/js/comun.js"></script>
    <script type="text/javascript" language="javascript" src="comun/js/formulario.js"></script>
    <script type="text/javascript" language="javascript" src="comun/js/validacion.js"></script>
    <script type="text/javascript" language="javascript" src="comun/js/ayudas.js"></script>
    
    <script type="text/javascript" language="javascript" src="comun/js/jquery-ui-1.9.2.min.js"></script>
    <script type="text/javascript" language="javascript" src="comun/js/tag-it.js"></script>
    
    <script type="text/javascript">
    	function inicio() {
    		setFoco("codAlm", "desAlm", "domicilio", "codCaja", "xmlConfiguracion");
    	}
        
        function checkForm() {
        	if(!esValido("codAlm", "TEXTO", true))
            	return false;
	        if(!esValido("desAlm", "TEXTO", true))
	            return false;
            
            return true;
        }
    </script>
  </head>

  <body  onload="inicio();" onkeydown="keyDown(event);">
    <cmz:main>
      <cmz:panelCMZ>
        <cmz:cabeceraPanelCMZ titulo="${permisos.accionMenu.titulo}" icono="${permisos.accionMenu.icono}">
          <cmz:acciones numAccionesVisibles="2">
            <c:choose>
              <c:when test="${!formularioTienda.editable}">
                <cmz:accion icono="comun/images/iconos/i-volver.gif" onclick="volver();" titulo="Volver" descripcion="Volver"></cmz:accion>
                <c:if test="${permisos.puedeEditar}">
                  <cmz:accion icono="comun/images/iconos/i-edit.gif" onclick="editar();" titulo="Editar" descripcion="Editar Tienda"></cmz:accion>
                </c:if>
                <c:if test="${permisos.puedeAñadir}">
                  <cmz:accionNuevoRegistro onclick="alta();" descripcion="Añadir una nueva Tienda"/>
                </c:if>
                <c:if test="${permisos.puedeEliminar}">
                  <cmz:accion icono="comun/images/iconos/i-cancel.gif" onclick="eliminar();" titulo="Eliminar" descripcion="Eliminar Tienda"></cmz:accion>
                </c:if>
              </c:when>
              <c:otherwise>
                <cmz:accionSalvar onclick="aceptar();"/>
                <cmz:accionCancelar onclick="cancelar();"/>
              </c:otherwise>
            </c:choose>
          </cmz:acciones>
        </cmz:cabeceraPanelCMZ>
        
        <cmz:cuerpoPanelCMZ>
          <cmz:mensaje/>
          <form id="frmDatos" name="frmDatos" action="tiendas" method="post">            
            <input id="accion" name="accion" type="hidden" value="" />
            <input id="operacion" name="operacion" type="hidden" value="" />
            <input id="indice" name="indice" type="hidden" value=""/>
            <input id="idObjeto" name="idObjeto" type="hidden" value="${tienda.codAlm}" />
            <input id="codTarifas" name="codTarifas" type="hidden" value=""/>
            <input id="desTarifas" name="desTarifas" type="hidden" value=""/>
            <input id="codTarifasPadre" name="codTarifasPadre" type="hidden" value=""/>
            <input id="idConfPasarela" name="idConfPasarela" type="hidden" value=""/>
             <input id="pagina" name="pagina" type="hidden" value="" />
             <input id="idVariables" name="idVariables" type="hidden" value=""/>
            <input id="valores" name="valores" type="hidden" value=""/>
            
            <cmz:panel>
              <cmz:cabeceraPanel titulo="Datos de la Tienda"></cmz:cabeceraPanel>
              <cmz:cuerpoPanel>
                <table cellpadding="2px" cellspacing="2px" border="0px">
                  <tr>
                    <td><cmz:etiqueta titulo="Código"/>:</td>
                    <td>
                      <cmz:campoTexto id="codAlm" valor="${tienda.codAlm}" requerido="true" anchura="90px" longitudMaxima="4"
                                        editable="${formularioTienda.enInsercion}" soloLectura="${!formularioTienda.enInsercion}"/>
                    </td>
                    
                    <td><cmz:etiqueta titulo="Descripción"/>:</td>
                    <td><cmz:campoTexto id="desAlm" valor="${tienda.desAlm}" requerido="true" anchura="300px" longitudMaxima="45" 
                                        editable="${formularioTienda.editable}" soloLectura="${!formularioTienda.editable}"/></td>
					
					<td>
                      <input type="checkbox" value="" id="activo" name="activo" <c:if test="${tienda.activo}">checked="checked"</c:if> 
                             <c:if test="${!formularioTienda.editable}">disabled</c:if>/>
                      <cmz:etiqueta titulo="Activo"/>
                    </td>                                        
                  </tr>
                </table>
              </cmz:cuerpoPanel>
            </cmz:panel>
            
            <cmz:pestañas>
            	<cmz:cabeceraPestañas pestañaActiva="${formularioTienda.pestañaActiva}">
            		<cmz:pestaña titulo="General" accion="0" onclick="seleccionaPestaña(0);"></cmz:pestaña>
            		<cmz:pestaña titulo="Tarifas" accion="1" onclick="seleccionaPestaña(1);"></cmz:pestaña>
            		<cmz:pestaña titulo="Configuración" accion="2" onclick="seleccionaPestaña(2);"></cmz:pestaña>
            		<cmz:pestaña titulo="Usuarios" accion="3" onclick="seleccionaPestaña(3);"></cmz:pestaña>
                	<cmz:pestaña titulo="Cajas" accion="4" onclick="seleccionaPestaña(4);"></cmz:pestaña>
                	<cmz:pestaña titulo="Servicios" accion="5" onclick="seleccionaPestaña(5);"></cmz:pestaña>
                	<cmz:pestaña titulo="Medios de Pago" accion="6" onclick="seleccionaPestaña(6);"></cmz:pestaña>
                	<cmz:pestaña titulo="Variables" accion="7" onclick="seleccionaPestaña(7);"></cmz:pestaña>
                	<cmz:pestaña titulo="Configuración ATCUD tienda online" accion="8" onclick="seleccionaPestaña(8);"></cmz:pestaña>
            	</cmz:cabeceraPestañas>
            	<cmz:cuerpoPestaña>
            		<c:choose>
                        <c:when test="${formularioTienda.pestañaActiva == 1}">
                        	<c:choose>
		                      <c:when test="${requestScope.modoFicha}">
		                          <c:import url="tarifasFicha.jsp"></c:import>
		                      </c:when>
		                      <c:otherwise>
			                      <c:import url="tarifasTabla.jsp"></c:import>
		                      </c:otherwise>
		                    </c:choose>
                        </c:when>
                        <c:when test="${formularioTienda.pestañaActiva == 2}">
                        	<c:choose>
		                      <c:when test="${requestScope.modoFicha}">
		                        <c:import url="configuracionSincronizacionFicha.jsp"></c:import>
		                      </c:when>
		                      <c:otherwise>
		                        <c:import url="configuracionSincronizacionTabla.jsp"></c:import>
		                      </c:otherwise>
		                	</c:choose>                       	
                        </c:when>
                        <c:when test="${formularioTienda.pestañaActiva == 3}">
                        	<c:choose>
		                      <c:when test="${requestScope.modoFicha}">
		                        <c:import url="usuariosFicha.jsp"></c:import>
		                      </c:when>
		                      <c:otherwise>
		                        <c:import url="usuariosTabla.jsp"></c:import>
		                      </c:otherwise>
		                	</c:choose>                       	
                        </c:when>
                        <c:when test="${formularioTienda.pestañaActiva == 4}">
                        	<c:choose>
		                      <c:when test="${requestScope.modoFicha}">
		                        <c:import url="cajasFicha.jsp"></c:import>
		                      </c:when>
		                      <c:when test="${requestScope.modoAltaCaja}">
		                        <c:import url="cajasAlta.jsp"></c:import>
		                      </c:when>
		                      <c:when test="${requestScope.modoEdicionCaja}">
		                        <c:import url="cajasEdicion.jsp"></c:import>
		                      </c:when>
		                      <c:otherwise>
		                        <c:import url="cajasTabla.jsp"></c:import>
		                      </c:otherwise>
		                	</c:choose>                       	
                        </c:when>
                        <c:when test="${formularioTienda.pestañaActiva == 5}">
                        	<c:choose>
		                      <c:when test="${requestScope.modoFicha}">
		                        <c:import url="serviciosFicha.jsp"></c:import>
		                      </c:when>                 
		                      <c:otherwise>
		                        <c:import url="serviciosTabla.jsp"></c:import>
		                      </c:otherwise>
		                	</c:choose>                       	
                        </c:when>
                        <c:when test="${formularioTienda.pestañaActiva == 6}">
                        	<c:choose>
		                      <c:when test="${requestScope.modoFicha}">
		                        <c:import url="mediosPagoFicha.jsp"></c:import>
		                      </c:when>                 
		                      <c:otherwise>
		                        <c:import url="mediosPagoTabla.jsp"></c:import>
		                      </c:otherwise>
		                	</c:choose>                       	
                        </c:when>
                        <c:when test="${formularioTienda.pestañaActiva == 7}">
                        	<c:import url="variables.jsp"></c:import>
                        </c:when>
                          <c:when test="${formularioTienda.pestañaActiva == 8}">
                          <c:choose>
		                      <c:when test="${requestScope.modoFicha}">
		                        <c:import url="atcudFicha.jsp"></c:import>
		                      </c:when>                 
		                      <c:otherwise>
		                        <c:import url="atcud.jsp"></c:import>
		                      </c:otherwise>
		                	</c:choose>            
                        </c:when>
                        <c:otherwise>
                          <c:import url="datosGenerales.jsp"></c:import>
                        </c:otherwise>
                     </c:choose>
            	</cmz:cuerpoPestaña>
            </cmz:pestañas>
            
          </form>
        </cmz:cuerpoPanelCMZ>
      </cmz:panelCMZ>
    </cmz:main>
  </body>
</html>