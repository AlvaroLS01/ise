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
<%@ taglib prefix="cmz" uri="http://comerzzia.com/taglib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:useBean id="variables" type="com.comerzzia.core.model.variables.VariableBean" scope="session" />
<jsp:useBean id="permisos" class="com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean" scope="request" />


<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Comerzzia</title>
    
    <cmz:head/>
    <link rel="stylesheet" type="text/css" media="screen,projection,print" href="comun/css/calendario.css"/>
    
    <script type="text/javascript" language="javascript" src="comun/js/comun.js"></script>
    <script type="text/javascript" language="javascript" src="comun/js/validacion.js"></script>
    <script type="text/javascript" language="javascript" src="comun/js/mantenimiento.js"></script>
    <script type="text/javascript" language="javascript" src="comun/js/ayudas.js"></script>
    
    <script type="text/javascript">
        function inicio() {
          document.getElementById("atcud").focus();
        }
        
        function checkForm() {
            if (!esValido("atcud", "TEXTO", true)) {
                return false;
            }            
            return true;
        }
        
    	function aceptar() {
    		if(checkForm()){
    			document.getElementById("accion").value = "salvar";
    			document.getElementById("frmDatos").submit();
    			_esperando();
    		}
    	}
            
     </script>
  </head>

  <body onload="inicio();" onkeydown="keyDown(event);>
    <cmz:main>
      <cmz:panelCMZ>
        <cmz:cabeceraPanelCMZ titulo="${permisos.accionMenu.titulo}" icono="${permisos.accionMenu.icono}">
          <cmz:acciones numAccionesVisibles="5">
            <c:choose>
              <c:when test="${!variables.enEdicion}">
                <cmz:accion icono="comun/images/iconos/i-volver.gif" onclick="volver();" titulo="Volver" descripcion="Volver"></cmz:accion>
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
          <form id="frmDatos" name="frmDatos" action="atcud" method="post">
            <input id="accion" name="accion" type="hidden" value="" />
            <input id="operacion" name="operacion" type="hidden" value="" />
            <input id="indice" name="indice" type="hidden" value=""/>
            <input id="idObjeto" name="idObjeto" type="hidden" value="${variables.idVariable}"/>
            <input id="estadoObjeto" name="estadoObjeto" type="hidden" value="${variables.estadoBean}"/>
            
            <cmz:panel>
              <cmz:cabeceraPanel titulo="Configuración de ATCUD"></cmz:cabeceraPanel>
              <cmz:cuerpoPanel>
                <table cellpadding="2px" cellspacing="2px" border="0px">
                  <tr>
                    <td><cmz:etiqueta titulo="ATCUD:"/></td>
                    <td>
                      <cmz:campoTexto id="atcud" valor="${variables.valor}" requerido="true" anchura="130px" longitudMaxima="6"
                                        editable="${variables.enEdicion}" soloLectura="${!variables.enEdicion}"/>
                    </td>
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