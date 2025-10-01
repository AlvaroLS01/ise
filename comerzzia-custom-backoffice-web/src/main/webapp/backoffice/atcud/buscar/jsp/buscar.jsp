<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="cmz" uri="http://comerzzia.com/taglib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<jsp:useBean id="variables" type="com.comerzzia.core.model.variables.VariableBean" scope="session" />
<jsp:useBean id="datosSesion" type="com.comerzzia.core.servicios.sesion.DatosSesionBean" scope="session" />
<jsp:useBean id="permisos" class="com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean" scope="request" />


<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Comerzzia</title>
    <cmz:head/>
    
    <link rel="stylesheet" type="text/css" media="screen,projection,print" href="comun/css/calendario.css" />
    <script type="text/javascript" language="javascript" src="comun/js/comun.js"></script>
    <script type="text/javascript" language="javascript" src="comun/js/mantenimiento.js"></script>
    <script type="text/javascript" language="javascript" src="comun/js/ayudas.js"></script>
	<script type="text/javascript" language="javascript" src="comun/js/validacion.js"></script>
    <script type="text/javascript" language="javascript" src="comun/js/formulario.js"></script>
    <script type="text/javascript" language="javascript" src="comun/js/calendario.js"></script>
    
    <script type="text/javascript">
	    function inicio() {
	        document.getElementById("atcud").focus();
	    }
	    
	    	function editar(idVariable){
				document.getElementById("accion").value = "editar";
		        document.getElementById("operacion").value = "ejecutar";
		        document.getElementById("idObjeto").value = idVariable;
				document.getElementById("frmDatos").submit();
				_esperando();
			}

	    }
	    
    </script>
  	</head>

  <body onload="inicio();">
    <cmz:main>
      <cmz:panelCMZ>
      	<cmz:cabeceraPanelCMZ titulo="${permisos.accionMenu.titulo}" icono="${permisos.accionMenu.icono}">
			<cmz:acciones numAccionesVisibles="3">
					<c:if test="${permisos.puedeEditar}">
	                  <cmz:accion icono="comun/images/iconos/i-edit.gif" onclick="editar('${variables.idVariable}');" titulo="Editar" descripcion="Editar ATCUD"></cmz:accion>
	                </c:if>
					<cmz:accion titulo="Ver Permisos" icono="comun/images/iconos/i-key.gif" descripcion="Ver permisos efectivos del usuario" onclick="verPermisos(${permisos.accionMenu.idAccion})" />
					<c:if test="${permisos.puedeAdministrar}">
						<cmz:accion titulo="Administrar Permisos" icono="comun/images/iconos/i-admin_permisos.gif" descripcion="Administración de permisos" onclick="adminPermisos(${permisos.accionMenu.idAccion})" />
					</c:if>
				</cmz:acciones>
			</cmz:cabeceraPanelCMZ>
        <cmz:cuerpoPanelCMZ>
          <cmz:mensaje/>
            <form id="frmDatos" name="frmDatos" action="atcud" method="post">
	            <input id="accion" name="accion" type="hidden" value="" />
	            <input id="operacion" name="operacion" type="hidden" value="" />
	            <input id="columna" name="columna" type="hidden" value="" />
	            <input id="pagina" name="pagina" type="hidden" value="" />
	            <input id="idObjeto" name="idObjeto" type="hidden" value="" />
	            <cmz:panel>
	              <cmz:cabeceraPanel titulo="Configuración de ATCUD"></cmz:cabeceraPanel>
	              <cmz:cuerpoPanel>
	                <table cellpadding="0px" cellspacing="0px" border="0px">
	                  <tr>
	                    <td>
	                      <table cellpadding="2px" cellspacing="2px" border="0px">                        
		  				    <tr>
	                          <td><cmz:etiqueta titulo="ATCUD:"/></td>
	                          <td><cmz:campoTexto id="atcud" valor="${variables.valor}" anchura="80px" longitudMaxima="20" editable="false" soloLectura="true"/></td>
	                        </tr>
	                      </table>
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