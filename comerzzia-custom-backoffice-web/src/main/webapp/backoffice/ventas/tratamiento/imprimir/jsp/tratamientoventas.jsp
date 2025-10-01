<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="cmz" uri="http://comerzzia.com/taglib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:useBean id="permisos" class="com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean" scope="request" />
<jsp:useBean id="trabajoInforme" type="com.comerzzia.core.model.informes.TrabajoInformeBean" scope="session" />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Comerzzia</title>
    
    <cmz:head/>
    <link rel="stylesheet" type="text/css" media="screen,projection,print" href="comun/css/calendario.css" />
    
    <script type="text/javascript" language="javascript" src="comun/js/comun.js"></script>
    <script type="text/javascript" language="javascript" src="comun/js/ayudas.js"></script>
    <script type="text/javascript" language="javascript" src="comun/js/validacion.js"></script>
    <script type="text/javascript" language="javascript" src="comun/js/calendario.js"></script>
    
    <script type="text/javascript">
	    function inicio() {
	        document.getElementById("fechaDesde").focus();
	        pintaCalendario("fechaDesde", "imgFechaDesde");
            pintaCalendario("fechaHasta", "imgFechaHasta");         
            
	    }
	
	    function checkForm() {
	    	if(!esValido("codAlm", "TEXTO", true)) {
				return false;
			}
	    	return true;
	    }
	
	    function imprimir(idVersion) {
	    	if(checkForm()) {
	            document.getElementById("accion").value = "ventas.estadisticas.lstSituacionVentas";
	            document.getElementById("operacion").value = "imprimir";
		    	document.getElementById("idVersion").value = idVersion;
	    		document.getElementById("frmDatos").target = "_blank";
	    		document.getElementById("frmDatos").submit();
	    		document.getElementById("frmDatos").target = "_self";
	    	}
	    }
    </script>
  </head>

  <body onload="inicio();" >
    <cmz:main>
      <cmz:panelCMZ>
        <cmz:cabeceraPanelCMZ titulo="${permisos.accionMenu.titulo}" icono="${permisos.accionMenu.icono}">
          <cmz:acciones numAccionesVisibles="2">       
            <cmz:accion titulo="Ver Permisos" icono="comun/images/iconos/i-key.gif" descripcion="Ver permisos efectivos del usuario" onclick="verPermisos(${permisos.accionMenu.idAccion})" />
            <c:if test="${permisos.puedeAdministrar}">
              <cmz:accion titulo="Administrar Permisos" icono="comun/images/iconos/i-admin_permisos.gif" descripcion="Administración de permisos" onclick="adminPermisos(${permisos.accionMenu.idAccion})" />
            </c:if>
          </cmz:acciones>
        </cmz:cabeceraPanelCMZ>
        
        <cmz:cuerpoPanelCMZ>
          <cmz:mensaje/>
          <form id="frmDatos" name="frmDatos" action="tratamientoVentas" method="POST" target="_self">
            <input id="accion" name="accion" type="hidden" value="" />
            <input id="operacion" name="operacion" type="hidden" value="" />
            <input id="idVersion" name="idVersion" type="hidden" value="0" />
            <cmz:panel>
              <cmz:cabeceraPanel titulo="Parámetros"></cmz:cabeceraPanel>
              <cmz:cuerpoPanel>
	            <table cellpadding="2px" cellspacing="2px" border="0px">
	              <tr>
	                <td><cmz:etiqueta titulo="Fecha desde"/>:</td>
	                <td>
	                	<cmz:campoFecha id="fechaDesde" mostrarCheckbox="false">
	                		<fmt:formatDate pattern="dd/MM/yyyy" value="${fechaDesde}"/>
	                	</cmz:campoFecha>
	                </td>
				  </tr>	                
	              <tr>
	                <td><cmz:etiqueta titulo="Fecha hasta"/>:</td>
	                <td>
	                	<cmz:campoFecha id="fechaHasta" mostrarCheckbox="false" >
	                		<fmt:formatDate pattern="dd/MM/yyyy" value="${fechaHasta}"/>
	                	</cmz:campoFecha>
	                </td>
				  </tr>
	                <td><cmz:etiqueta titulo="Almacén"/>:</td>
	                <td>
	                	<cmz:ayuda nombreAyuda="ALMACENES">
	                		<cmz:codigoAyuda idCodigo="codAlm" longitudMaximaCodigo="10" anchuraCodigo="100px"/>
	                		<cmz:descripcionAyuda idDescripcion="desAlm" anchuraDescripcion="320px"/>
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