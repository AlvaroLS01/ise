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
<jsp:useBean id="permisosInforme" class="com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean" scope="request" />
<jsp:useBean id="trabajoInforme" type="com.comerzzia.core.model.informes.TrabajoInformeBean" scope="session" />

<script type="text/javascript">   		
    
	    function editarVersionInforme(idVersion){
	    	document.getElementById("accion").value ='${trabajoInforme.informe }';	
	    	document.getElementById("operacion").value = "editar";	    	
	    	document.getElementById("idVersion").value = idVersion;
	    	document.getElementById("idInforme").value = '${trabajoInforme.idInforme}';	
			document.getElementById("frmDatos").submit();
			_esperando();	
	    }

	    function borrarVersionInforme(idVersion){		   
	    	if (confirm(Gettext.gettext("¿Desea borrar el informe seleccionado?"))) {		    	
		    	document.getElementById("accion").value ='${trabajoInforme.informe}';	
		    	document.getElementById("operacion").value = "eliminar";
		    	document.getElementById("idVersion").value = idVersion;	
		    	document.getElementById("idInforme").value = '${trabajoInforme.idInforme}';	
				document.getElementById("frmDatos").submit();							
	    	}		    	
	    }

	    function nuevaVersionInforme(){				   		    
	    	document.getElementById("accion").value ='${trabajoInforme.informe}';	
	    	document.getElementById("operacion").value = "nuevaVersion";		
			document.getElementById("frmDatos").submit();
			_esperando();	
		}   

		function descargarJrxml(version){	
			// Necesitamos saber el action actual, ya que habrá que restaurarlo 
			// tras modificarlo para descargar el jrxml.				
			var valor = window.location;
			var limit = valor.toString().lastIndexOf("/");
			limit+=1;
			var action = valor.toString().substring(limit);		
			document.getElementById("tipo").value="Jrxml";							
			document.getElementById("frmDatos").action = "descargaInformes?version=" + version;			
			document.getElementById("accion").value ='${trabajoInforme.informe}';
			document.getElementById("idInforme").value = '${trabajoInforme.idInforme}';				
			document.getElementById("frmDatos").submit();			
			document.getElementById("frmDatos").action = action;									
		}	
		
		function descargarPdf(idVersion){
			if (window.checkForm) {					
				if(checkForm()){
					pdf(idVersion);				
				}
			}
			else{
				pdf(idVersion);
			}														
		}

		function descargarExcel(idVersion){	
			if (window.checkForm) {					
				if(checkForm()){
					excel(idVersion);				
				}
			}
			else{
				excel(idVersion);
			}													
		}	

		function descargarRtf(idVersion){	
			if (window.checkForm) {					
				if(checkForm()){
					rtf(idVersion);				
				}
			}
			else{
				rtf(idVersion);
			}										
		}	
			

		function pdf(idVersion){
			document.getElementById("tipo").value="pdf";			
			document.getElementById("idVersion").value = idVersion;				
			document.getElementById("idInforme").value = '${trabajoInforme.idInforme}';				
			document.getElementById("accion").value ='${trabajoInforme.informe}';						
	    	document.getElementById("operacion").value = "imprimir";	    	
	    	document.getElementById("frmDatos").target = "_blank";
    		document.getElementById("frmDatos").submit();
    		document.getElementById("frmDatos").target = "_self";
			
		}

		function excel(idVersion){
			document.getElementById("tipo").value="excel";			
			document.getElementById("idVersion").value = idVersion;				
			document.getElementById("idInforme").value = '${trabajoInforme.idInforme}';				
			document.getElementById("accion").value ='${trabajoInforme.informe}';						
	    	document.getElementById("operacion").value = "imprimir";	    	
			document.getElementById("frmDatos").submit();	
		}

		function rtf(idVersion){
			document.getElementById("tipo").value="rtf";			
			document.getElementById("idVersion").value = idVersion;				
			document.getElementById("idInforme").value = '${trabajoInforme.idInforme}';				
			document.getElementById("accion").value ='${trabajoInforme.informe}';						
	    	document.getElementById("operacion").value = "imprimir";	    	
			document.getElementById("frmDatos").submit();
		}
		
    </script>

<cmz:panel>
	<form id="frmDatos" name="frmDatos" action="versiones" method="POST">
		<input type="hidden" id="informe" name="informe" value="" /> 
		<input type="hidden" id="idObjeto" name="idObjeto" value='${sessionScope.idObjeto.toString()}' /> 
		<input type="hidden" id="idVersion" name="idVersion" value="" /> 
		<input type="hidden" id="version" name="version" value="" />
		<input type="hidden" id="idInforme" name="idInforme" value="" /> 
		<input type="hidden" id="operacion" name="operacion" value="" />
		<input type="hidden" id="tipo" name="tipo" value="" />
		<cmz:cabeceraPanel titulo="Versiones">
			<cmz:acciones numAccionesVisibles="1" idAcciones="masAccionesVersiones">
			<c:if test="${permisos.puedeAdministrar}">
				<cmz:accionNuevoRegistro onclick="nuevaVersionInforme();" descripcion="Añadir una nueva Accion" />
			</c:if>
			<cmz:accion titulo="Ver Permisos" icono="comun/images/iconos/i-key.gif" descripcion="Ver permisos efectivos del usuario" onclick="verPermisos(${permisosInforme.accionMenu.idAccion})" />
            <c:if test="${permisos.puedeAdministrar}">
              <cmz:accion titulo="Administrar Permisos" icono="comun/images/iconos/i-admin_permisos.gif" descripcion="Administración de permisos" onclick="adminPermisos(${permisosInforme.accionMenu.idAccion})" />
            </c:if>
			</cmz:acciones>
		</cmz:cabeceraPanel>
		<cmz:cuerpoPanel>


			<c:forEach items="${listaVersionesInforme}" var="version">
				<cmz:panel>
					<cmz:cabeceraPanel titulo="${version.titulo}">
						<cmz:acciones numAccionesVisibles="4" idAcciones="${version.version}">
							<cmz:accion titulo="PDF" icono="comun/images/iconos/pdf.png" descripcion="Descargar pdf" onclick="descargarPdf(${version.idVersion});" />
							<cmz:accion titulo="RTF" icono="comun/images/iconos/rtf.gif" descripcion="Descargar rtf" onclick="descargarRtf(${version.idVersion});" />
							<cmz:accion titulo="Excel" icono="comun/images/iconos/excel.png" descripcion="Descargar excel" onclick="descargarExcel(${version.idVersion});" />
							<c:if test="${permisos.puedeAdministrar}">
								<cmz:accion titulo="Editar"	icono="comun/images/iconos/i-edit.gif" descripcion="Editar Operacion" onclick="editarVersionInforme(${version.idVersion});" />
								<cmz:accion titulo="Eliminar" icono="comun/images/iconos/i-cancel.gif" descripcion="Eliminar Operacion"	onclick="borrarVersionInforme(${version.idVersion});" />
								<cmz:accion titulo="Descargar" icono="comun/images/iconos/instalador.png" descripcion="Descargar .jrxml" onclick="descargarJrxml('${version.version}');" />
							</c:if>
						</cmz:acciones>
					</cmz:cabeceraPanel>
					<cmz:cuerpoPanel>
						<table cellpadding="0px" cellspacing="0px" border="0px">
							<tr>
								<td><cmz:etiqueta titulo="${version.descripcion}" /></td>
							</tr>
						</table>
					</cmz:cuerpoPanel>
				</cmz:panel>
			</c:forEach>
		</cmz:cuerpoPanel>
	</form>
</cmz:panel>
