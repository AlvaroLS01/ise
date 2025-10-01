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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:useBean id="permisos" class="com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean" scope="request" />
<jsp:useBean id="versionBean" class="com.comerzzia.core.model.informes.VersionInformeBean" scope="session" />

<c:set var="idInforme" value="${trabajoInforme.idInforme}" />
<c:set var='edicion' value="${edicion}" />
<c:set var='url' value="'${trabajoInforme.informe}'" />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Comerzzia</title>

<cmz:head/>

<script type="text/javascript" language="javascript" src="comun/js/comun.js"></script>
<script type="text/javascript" language="javascript" src="comun/js/formulario.js"></script>
<script type="text/javascript" language="javascript" src="comun/js/validacion.js"></script>

<script type="text/javascript">
        function inicio() {            
        	setFoco("version");
        }
        
        function checkForm(){					
			if(!esValido("version","TEXTO",true)){		   		
	   	 		return false;
	   	 	} 
			if(!esValido("descripcion","TEXTO",false)){		   		
	   	 		return false;
	   	 	} 		
			if(!esValido("formatoSalida","TEXTO",false)){		   		
	   	 		return false;
	   	 	} 	
			if(!esValido("titulo","TEXTO",true)){		   		
	   	 		return false;
	   	 	} 							
	   	 	return true;
	   	 }  

	   	 function checkPorDefecto(){			   	   	
		   		alert(Gettext.gettext("Si marca esta casilla sustituirá la versión por defecto de este informe"));   	 		   	   
		}

	   //Acepta los cambios de una nueva version
	   	function aceptarNuevaVersion(url) {
	   		if(__checkForm()) {
	   			document.getElementById("accion").value = url;
	   			document.getElementById("operacion").value = "tratarFichero";
	   		    document.getElementById("frmVersion").submit();
	   			_esperando();
	   		}
	   	}

	  //Cancela los cambios de una nueva version
	   	function cancelarNuevaVersion(url) {
	   		document.getElementById("accion").value = url;
	   		document.getElementById("operacion").value = "ejecutar";	
	   	    document.getElementById("frmVersion").submit();
	   		_esperando();
	   	}      
       </script>

</head>
<body onload="inicio();">
	<cmz:main>
		<cmz:panelCMZ>
			<cmz:cabeceraPanelCMZ titulo="Nueva version"
				icono="${permisos.accionMenu.icono}">
				<cmz:acciones numAccionesVisibles="2">
					<cmz:accionSalvar onclick="aceptarNuevaVersion(${url});" />
					<cmz:accionCancelar onclick="cancelarNuevaVersion(${url});" />
				</cmz:acciones>
			</cmz:cabeceraPanelCMZ>
			<cmz:cuerpoPanelCMZ>
				<cmz:mensaje />
				<form id="frmVersion" name="frmVersion" action="" method="POST" enctype="multipart/form-data">
					<input id="accion" name="accion" type="hidden" value="<c:out value="${url}"/>" /> 
					<input id="operacion" name="operacion" type="hidden" value="" /> 				
					<input type='hidden' id="idVersion" name="idVersion" value="<c:out value="${versionBean.idVersion}"/>" /> 
					<input type='hidden' id="idInforme" name="idInforme" value="<c:out value="${idInforme}"/>" />									
					<cmz:panel>
						<cmz:cabeceraPanel titulo="Parámetros"></cmz:cabeceraPanel>
						<cmz:cuerpoPanel>
							<table cellpadding="2px" cellspacing="2px" border="0px">
								<tr>									
									<td><cmz:etiqueta titulo="Versión"/></td>
									<td><cmz:campoTexto id="version" valor="${versionBean.version}" requerido="${!versionBean.enEdicion}"	anchura="200px" longitudMaxima="30" soloLectura="${versionBean.enEdicion}" />
									</td>
								</tr>
								<tr>
									<td><cmz:etiqueta titulo="Título"/></td>
									<td><cmz:campoTexto id="titulo"	valor="${versionBean.titulo}" requerido="true" anchura="300px" longitudMaxima="50" editable="true" /></td>
								<tr>
									<td><cmz:etiqueta titulo="Descripción"/></td>
									<td><cmz:campoTexto id="descripcion" valor="${versionBean.descripcion}" requerido="false" anchura="300px" longitudMaxima="250" editable="true" /></td>
								</tr>
								<tr>
									<td><cmz:etiqueta titulo="Formato salida"/></td>
									<td width="150px">
										<select class='campo' id="formatoSalida" name="formatoSalida" requerido="false" editable="true">
											<option value="WEB" <c:if test="${versionBean.formatoSalida == 'WEB'}">selected="selected"</c:if>><cmz:etiqueta titulo="WEB"/></option>
										</select>
									</td>
								</tr>
								<tr>
									<td><cmz:etiqueta titulo="Por defecto"/></td>
									  <td><input type="checkbox" value="on" id="porDefecto" name="porDefecto" <c:if test="${versionBean.porDefecto == true}">checked="checked"</c:if> 
                            		 <c:if test="${versionBean.porDefecto == true}">disabled</c:if> onClick="checkPorDefecto()"/>                     
                   					
                   					  <c:if test="${versionBean.porDefecto == true}">
                   					 <cmz:etiqueta titulo="Para quitar este valor debe marcar la casilla 'Por defecto' en otra versión"/>.
                   					 </c:if>
                   					  </td>
								</tr>
							</table>
						</cmz:cuerpoPanel>
					</cmz:panel>
					<cmz:panel>
						<cmz:cabeceraPanel titulo="Informe"></cmz:cabeceraPanel>
						<cmz:cuerpoPanel>
							<table cellpadding="2px" cellspacing="2px" border="0px">
								<tr>
									<td align="center"><cmz:etiqueta titulo="Informe"/>: <input type="file" id="fichero" size='50' name="fichero"></td>
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