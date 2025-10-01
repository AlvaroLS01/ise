<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="cmz" uri="http://comerzzia.com/taglib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<jsp:useBean id="datosMovimiento" type="com.comerzzia.bricodepot.backoffice.persistence.datosMovimiento.DatosMovimientoBean" scope="session" />
<jsp:useBean id="permisos" class="com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean" scope="request" />


<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Comerzzia</title>
    <cmz:head/>
    
    <link rel="stylesheet" type="text/css" media="screen,projection,print" href="comun/css/calendario.css" />
    <script type="text/javascript" language="javascript" src="comun/js/ayudas.js"></script>
    <script type="text/javascript" language="javascript" src="comun/js/comun.js"></script>
    <script type="text/javascript" language="javascript" src="comun/js/mantenimiento.js"></script>
    <script type="text/javascript" language="javascript" src="comun/js/validacion.js"></script>
    <script type="text/javascript" language="javascript" src="comun/js/calendario.js"></script>
    
    <script type="text/javascript">
	    function inicio() {
	        document.getElementById("fecha").focus();
	        pintaCalendario("fecha", "imgFecha");
	    }
	    
        function checkForm() {
        	if(!esValido("fecha", "FECHA", true)){
                return false;
            }
        	if (!esValido("codAlm", "AYUDA", true, "desAlm", "ALMACENES")) {
				return false;
			}
        	if(!esValido("comentarios", "TEXTO", true)){
                return false;
            }
        	if(!esValido("importe", "NUMERICO", true)){
                return false;
            }
        	if(!esValido("importeRepetido", "NUMERICO", true)){
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
        
		function limpiar(){
			jQuery("#codAlm").val("");
			jQuery("#desAlm").val("");
			jQuery("#fecha").val("");
			jQuery("#comentarios").val("");
			jQuery("#importe").val("");
			jQuery("#importeRepetido").val("");
			return true;
		}
       
    </script>
  	</head>

  <body onload="inicio();">
    <cmz:main>
      <cmz:panelCMZ>
      	<cmz:cabeceraPanelCMZ titulo="${permisos.accionMenu.titulo}" icono="${permisos.accionMenu.icono}">
			<cmz:acciones numAccionesVisibles="2">
			            <cmz:accion icono="comun/images/iconos/aceptar.gif" onclick="aceptar();" titulo="Aceptar"/>
                		<cmz:accion icono="comun/images/iconos/i-goma.gif" onclick="limpiar();" titulo="Limpiar"/>
					<c:if test="${permisos.puedeAdministrar}">
						<cmz:accion titulo="Administrar Permisos" icono="comun/images/iconos/i-admin_permisos.gif" descripcion="Administración de permisos" onclick="adminPermisos(${permisos.accionMenu.idAccion})" />
					</c:if>
						<cmz:accion titulo="Ver Permisos" icono="comun/images/iconos/i-key.gif" descripcion="Ver permisos efectivos del usuario" onclick="verPermisos(${permisos.accionMenu.idAccion})" />
				</cmz:acciones>
			</cmz:cabeceraPanelCMZ>
        <cmz:cuerpoPanelCMZ>
          <cmz:mensaje/>
          <form id="frmDatos" name="frmDatos" action="datosMovimiento" method="post">
				<input id="accion" name="accion" type="hidden" value="" />
				<input id="operacion" name="operacion" type="hidden" value="" />
				<input id="idObjeto" name="idObjeto" type="hidden" value="${datosMovimiento.fecha}" />
            
            <cmz:panel>
              <cmz:cabeceraPanel titulo="Insertar datos de movimiento 92"></cmz:cabeceraPanel>
              <cmz:cuerpoPanel>
                      <table cellpadding="2px" cellspacing="2px" border="0px">
                        <tr>
                          <td><cmz:etiqueta titulo="Fecha"/>:</td>
                          <td>
                          	<cmz:campoFecha id="fecha" requerido="true" mostrarCheckbox="false">
                          		<fmt:formatDate pattern="dd/MM/yyyy" value="${datosMovimiento.fecha}"/>
                          	</cmz:campoFecha>	
                      	  </td>
                        </tr>
                        
                        <tr>
       					  <td><cmz:etiqueta titulo="Almacén"/>:</td>
						  <td>
					          <cmz:ayuda nombreAyuda="ALMACENES" requerido="true">
					          	<cmz:codigoAyuda idCodigo="codAlm" valorCodigo="${datosMovimiento.codAlm}" anchuraCodigo="40px" longitudMaximaCodigo="4"></cmz:codigoAyuda>
					          	<cmz:descripcionAyuda idDescripcion="desAlm" valorDescripcion="" anchuraDescripcion="180px"></cmz:descripcionAyuda>
					          </cmz:ayuda>
						  </td>
	  					</tr>
	  					
	  					<tr>
	  						<td><cmz:etiqueta titulo="Comentarios"/>:</td>
							<td>
								<cmz:campoAreaTexto valor="${datosMovimiento.comentarios}" id="comentarios" requerido="true" altura="120px" anchura="100%" longitudMaxima="60" />
							</td>
						</tr>
						
						<tr>
							<td><cmz:etiqueta titulo="Importe:" /></td>
                    		<td>
                    			<cmz:campoTexto id="importe" valor="${datosMovimiento.importe}" requerido="true" anchura="190px" longitudMaxima="10"/>
            				</td>
            				<td><cmz:etiqueta titulo="Repetir importe:" /></td>
            				<td>
                    			<cmz:campoTexto id="importeRepetido" valor="${datosMovimiento.importeRepetido}" requerido="true" anchura="190px" longitudMaxima="10"/>
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