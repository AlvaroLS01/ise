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


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<jsp:useBean id="paramBuscarErroresInterfaces" type="com.comerzzia.bricodepot.backoffice.persistence.erroresinterfaces.ParametrosBuscarErroresInterfacesBean" scope="session" />
<jsp:useBean id="datosSesion" type="com.comerzzia.core.servicios.sesion.DatosSesionBean" scope="session" />

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Comerzzia</title>
    
    <cmz:head/>
    <link rel="stylesheet" type="text/css" media="screen,projection,print" href="comun/css/calendario.css" />
    <script type="text/javascript" language="javascript" src="comun/js/comun.js"></script>
    <script type="text/javascript" language="javascript" src="comun/js/mantenimiento.js"></script>
    <script type="text/javascript" language="javascript" src="comun/js/calendario.js"></script>
    <script type="text/javascript" language="javascript" src="comun/js/validacion.js"></script>
    
    <script type="text/javascript">
   		var uidsErrores = "";
   		var idsClases = "";
   		var idsObjetos = "";
        function inicio() {
            /* setFoco("fecha"); */
        }
        
        function verTraza(trazaEvaluada){
       	 var url = "ver.traza.error.screen";
       	 document.getElementById("traza").value = trazaEvaluada;
        	abrirVentanaModal(url, "350px", "850px", callbackVerTraza);
       }
        
        function callbackVerTraza(){}
        
        function avisoTraza(){
        	alert(Gettext.gettext("No existe traza de error."));
        }

        function marcarTodo() {
      		var numChecks = document.frmDatos.errores.length;
     		if(numChecks){
    			for(i = 0; i < numChecks; i++){
       				document.frmDatos.errores[i].checked = true;
       			}

      		}else{
                document.frmDatos.errores.checked = true;
    		}	
        }

    	function desmarcarTodo() {
      		var numChecks = document.frmDatos.errores.length;
     		if(numChecks){
    			for(i = 0; i < numChecks; i++){
       				document.frmDatos.errores[i].checked = false;
       			}

      		}else{
                document.frmDatos.errores.checked = false;
    		}	
        }

    	function invertir() {
      		var numChecks = document.frmDatos.errores.length;
     		if(numChecks){
    			for(i = 0; i < numChecks; i++){
    				if (document.frmDatos.errores[i].checked) {
    	   				document.frmDatos.errores[i].checked = false;
    				} else {
    					document.frmDatos.errores[i].checked = true;
    				}
       			}

      		}else{
      	  		if (document.frmDatos.errores.checked) {
                	document.frmDatos.errores.checked = false;
      	  		} else {
      	  			document.frmDatos.errores.checked = true;
      	  		}
    		}	
        }

        function seleccionarErrores(){
        	var i;
      		var numChecks = document.frmDatos.errores.length;
      		if(numChecks){
    			for(i = 0; i < numChecks; i++){
       				if(document.frmDatos.errores[i].checked){
       					var err = document.frmDatos.errores[i].value;
       					var lst = new Array();
       		            lst = err.split("|");
       					uidsErrores += lst[0] + ',';
       					idsClases   += lst[1] + ',';
       					idsObjetos  += lst[2] + ',';
    				}
       			}
       			document.getElementById("uidsErrores").value = uidsErrores.substring(0, uidsErrores.length - 1);
       			document.getElementById("idsClases").value = idsClases.substring(0, idsClases.length - 1);
       			document.getElementById("idsObjetos").value = idsObjetos.substring(0, idsObjetos.length - 1);

      		}else{
                if (document.getElementById("errores").checked){
                	var err = document.frmDatos.errores.value;
                	var lst = new Array();
    	            lst = err.split("|");
                	uidsErrores += lst[0];
                	idsClases   += lst[1];
   					idsObjetos  += lst[2];
                }

       			document.getElementById("uidsErrores").value = uidsErrores;
       			document.getElementById("idsClases").value = idsClases;
       			document.getElementById("idsObjetos").value = idsObjetos;
      		}
        }

        function eliminarErroresSeleccionados(){
            if(confirm(Gettext.gettext("¿Está seguro de que quiere eliminar los errores seleccionados?"))){
            	seleccionarErrores();
            	document.getElementById("accion").value = "eliminar";
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
          	<cmz:accion titulo="Eliminar seleccionados" icono="comun/images/iconos/cancelar.gif" descripcion="Eliminar los errores seleccionados" onclick="eliminarErroresSeleccionados()" />
            <cmz:accion titulo="Ver Permisos" icono="comun/images/iconos/i-key.gif" descripcion="Ver permisos efectivos del usuario" onclick="verPermisos(${permisos.accionMenu.idAccion})" />
            <c:if test="${permisos.puedeAdministrar}">
              <cmz:accion titulo="Administrar Permisos" icono="comun/images/iconos/i-admin_permisos.gif" descripcion="Administración de permisos" onclick="adminPermisos(${permisos.accionMenu.idAccion})" />
            </c:if>
          </cmz:acciones>
        </cmz:cabeceraPanelCMZ>       
        <cmz:cuerpoPanelCMZ>
          <cmz:mensaje/>
          <form id="frmDatos" name="frmDatos" action="erroresInterfaces" method="post">
            <input id="accion" name="accion" type="hidden" value="" />
            <input id="operacion" name="operacion" type="hidden" value="" />
            <input id="columna" name="columna" type="hidden" value="" />
            <input id="pagina" name="pagina" type="hidden" value="" />
            <input id="idObjeto" name="idObjeto" type="hidden" value="" />
            <input id="traza" name="traza" type="hidden" value="" />
            <input id="uidsErrores" name="uidsErrores" type="hidden" value=""/>
            <input id="idsClases" name="idsClases" type="hidden" value=""/>
            <input id="idsObjetos" name="idsObjetos" type="hidden" value=""/>
            <cmz:panel>
              <cmz:cabeceraPanel titulo="Búsqueda de errores en interfaces"></cmz:cabeceraPanel>
              <cmz:cuerpoPanel>
                <table cellpadding="0px" cellspacing="0px" border="0px">
                  <tr>
                    <td>
                      <table cellpadding="2px" cellspacing="2px" border="0px">
                        <tr>
                        	<td>
                        		<cmz:etiqueta titulo="Entidad: "/>
                        	</td>
                        	<td>
                        		<select class="campo" id="idClase" name="idClase">
                        			<option value="" <c:if test="${paramBuscarErroresInterfaces.idClase eq ''}">selected="selected"</c:if>><cmz:etiqueta titulo="Todos"/></option>
		                    		<c:forEach items="${listaClases}" var="idClase">
		                    			<option value="${idClase}" <c:if test="${idClase eq paramBuscarErroresInterfaces.idClase}">selected="selected"</c:if>>
		                    				<cmz:etiqueta titulo="${idClase}"/>
		                    			</option>
		                    		</c:forEach>
                          		</select>
                        	</td>
                        	<!-- Separador del botón -->
                         	<td width="150px"></td>
                        	<td>
                      			<cmz:botonConsultar onclick="consultar();"/>
                    	  	</td>
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
                  		<cmz:acciones numAccionesVisibles="3">
                  		<cmz:accion onclick="marcarTodo();" titulo="Marcar Todo" icono="comun/images/iconos/checkbox.png"></cmz:accion>
					    <cmz:accion onclick="desmarcarTodo();" titulo="Desmarcar Todo" icono="comun/images/iconos/checkboxVacio.png"></cmz:accion>
					    <cmz:accion onclick="invertir();" titulo="Invertir" icono="comun/images/iconos/invertir.png"></cmz:accion>
                  	</cmz:acciones>   
                     <cmz:listaPaginada>
                      <cmz:cabeceraListaPaginada ordenActual="${paramBuscarErroresInterfaces.orden}">
	                      <cmz:itemCabeceraListaPaginada nombre="&nbsp;"></cmz:itemCabeceraListaPaginada>
	                      <cmz:itemCabeceraListaPaginada nombre="Entidad" columna="1" ordenColumna="ID_CLASE"></cmz:itemCabeceraListaPaginada>
	                      <cmz:itemCabeceraListaPaginada nombre="Objeto" columna="2" ordenColumna="ID_OBJETO"></cmz:itemCabeceraListaPaginada>
	                      <cmz:itemCabeceraListaPaginada estilo="text-align: center;" nombre="Fecha Inicio" columna="3" ordenColumna="FECHA_INICIO"></cmz:itemCabeceraListaPaginada>
	                      <cmz:itemCabeceraListaPaginada nombre="Último Error" columna="4" ordenColumna="ULTIMO_ERROR"></cmz:itemCabeceraListaPaginada>
	                      <cmz:itemCabeceraListaPaginada nombre="Último Documento" columna="5" ordenColumna="ULTIMO_DOCUMENTO"></cmz:itemCabeceraListaPaginada>
	                      <cmz:itemCabeceraListaPaginada nombre="Tiendas Implicadas" columna="6" ordenColumna="TIENDAS_IMPLICADAS"></cmz:itemCabeceraListaPaginada>
	                      <cmz:itemCabeceraListaPaginada nombre="Último Mensaje" columna="7" ordenColumna="ULTIMO_MENSAJE"></cmz:itemCabeceraListaPaginada>
	                      <cmz:itemCabeceraListaPaginada nombre="Traza" estilo="text-align: center;"></cmz:itemCabeceraListaPaginada>
                      </cmz:cabeceraListaPaginada>
                      
                      <cmz:contenidoListaPaginada variable="error" paginaResultados="${paginaResultados}">
                      	<cmz:itemContenidoListaPaginada alineacion="center" escape="false">
                          <input type="checkbox" id="errores" name="errores" value="${error.uidError}|${error.idClase}|${error.idObjeto}"/>
                        </cmz:itemContenidoListaPaginada>
                      	<cmz:itemContenidoListaPaginada valor="${error.idClase}"></cmz:itemContenidoListaPaginada>
                      	<cmz:itemContenidoListaPaginada valor="${error.idObjeto}"></cmz:itemContenidoListaPaginada>
                      	<cmz:itemContenidoListaPaginada  alineacion="center">
                      		<fmt:formatDate pattern="dd/MM/yyyy HH:mm:ss" value="${error.fechaInicio}"/>
                      	</cmz:itemContenidoListaPaginada>
                      	<cmz:itemContenidoListaPaginada alineacion="center">
                      		<fmt:formatDate pattern="dd/MM/yyyy HH:mm:ss" value="${error.ultimoError}"/>
                      	</cmz:itemContenidoListaPaginada>
                      	<cmz:itemContenidoListaPaginada valor="${error.ultimoDocumento}"></cmz:itemContenidoListaPaginada>
                      	<cmz:itemContenidoListaPaginada valor="${error.tiendasImplicadas}"></cmz:itemContenidoListaPaginada>
                      	<cmz:itemContenidoListaPaginada valor="${error.ultimoMensaje}"></cmz:itemContenidoListaPaginada>
                       <cmz:acciones alineacion="center">
                       	<c:choose>
                       		<c:when test="${error.ultimoMensajeTraza != null}">
                       			<c:set var="traza1"><c:out value="${error.ultimoMensajeTrazaEscape}" escapeXml="true"/></c:set>
                  				<cmz:accion icono="comun/images/iconos/modo_tabla.gif" descripcion="Ver traza de error" onclick="verTraza('${traza1}');" />
                       		</c:when>
                       		<c:otherwise>
                       			<cmz:accion icono="comun/images/iconos/modo_tabla.gif" descripcion="Ver traza de error" onclick="avisoTraza();" />
                       		</c:otherwise>
                       	</c:choose>
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