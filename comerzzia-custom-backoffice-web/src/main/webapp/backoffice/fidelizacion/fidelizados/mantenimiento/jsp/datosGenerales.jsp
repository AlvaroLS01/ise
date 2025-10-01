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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:useBean id="listaEstadosCiviles" type="java.util.List<com.comerzzia.core.model.config.ayudas.ElementoAyudaBean>" scope="session" />
<jsp:useBean id="listaTiposDocumento" type="java.util.List<com.comerzzia.core.model.config.ayudas.ElementoAyudaBean>" scope="session" />

<c:set var="fidelizado" value="${formularioFidelizado.registroActivo}" />

<script type="text/javascript">

	var lstTagsNuevo = [];
	var lstTagsEliminado = [];
	
	$tagit=jQuery.noConflict();
	$tagit(document).ready(function(){
		$tagit('#tagit').tagit({
	        readOnly: <c:out value="${!formularioFidelizado.editable}"/>,
			allowSpaces: true,
			availableTags:${requestScope.lstEtiquetasDisponibles},
			
			//Evento cuando el tag es añadido
			afterTagAdded: function(event, ui) {
				//Añadimos a la lstTagsNuevo el nuevo tags
		        lstTagsNuevo.push($tagit('#tagit').tagit('tagLabel', ui.tag));
				
				
				//Si añadimos de nuevo un tag eliminado sin guardar, debemos eliminarlo de la lista de eliminados
		        var indiceBorrado = lstTagsEliminado.indexOf($tagit('#tagit').tagit('tagLabel', ui.tag));
		        if(indiceBorrado != -1) { 
		        	lstTagsEliminado.splice(indiceBorrado, 1);
		        }
		    },
		    
	        //Evento cuando el tag es borrado
	        afterTagRemoved: function(event, ui) {
	        	//Añadimos a la lstTagsEliminado el tags eliminado
	            lstTagsEliminado.push($tagit('#tagit').tagit('tagLabel', ui.tag));
	            
	           	//Borramos de la lstTagsNuevo aquellos tags eliminados
	           	var indiceBorrado = lstTagsNuevo.indexOf($tagit('#tagit').tagit('tagLabel', ui.tag));
	           	lstTagsNuevo.splice(indiceBorrado, 1);
	        },
	    });
	});
	
    function checkFormPestaña() {
    	if (!esValido("codFidelizado", "TEXTO", false)) {
            return false;
        }
    	if (!esValido("nombre", "TEXTO", true)) {
            return false;
        }
    	if (!esValido("apellidos", "TEXTO", true)) {
            return false;
        }
        if (!esValido("fechaNacimiento", "FECHA", false)) {
            return false;
        }
        if (!esValido("codPais", "AYUDA", false, "desPais", "PAISES")) {
			return false;
		}
        if (!esValido("codLengua", "AYUDA", false, "desLengua", "LENGUAJES")){
			return false;
		}
        if (!validarDocumentoIdentificacion("documento", "codPais", "codTipoIden", false,false)) {
            return false;
        }
        if (!esValido("codAlm", "AYUDA", false, "desAlm", "TIENDAS")) {
			return false;
		}

    	document.getElementById("lstEtiquetasNuevas").value = arrayToString(lstTagsNuevo);
		document.getElementById("lstEtiquetasEliminadas").value = arrayToString(lstTagsEliminado);
		document.getElementById("lstEtiquetasDisponibles").value = arrayToString(${requestScope.lstEtiquetasDisponibles});

        return true;
    }

    function arrayToString(lst){
		var tagString = null; 
		for(var i = 0; i<lst.length; i+=1){ 
			if(i==0){
				tagString = lst[i];
				}
			else{
				tagString += '|;'+lst[i];
					}
			
			}
	
		return tagString;
			}

    function obtenerTiposIdentificacion(onchange) {
    	var actualizado = false;
        if(!onchange) {
        	buscar("PAISES", "codPais", "desPais", null, null, callbackObtenerTiposIdentificacion);
        }
        else{
        	actualizado = buscarXML("PAISES", "codPais", "desPais");
        }

        if(actualizado) {
    		obtenerTiposIdentificacionPais();
        }
    }

    function callbackObtenerTiposIdentificacion(event){
    	obtenerTiposIdentificacionPais();
    }

    function obtenerTiposIdentificacionPais(valor){
    	var url = "obtenerTiposIdentificacionPais.cmz" +"?codPais=" + escape(document.getElementById("codPais").value);

		response = xmlRequest(url);
		
		if (response != null) {
		  if (response.getElementsByTagName("ERROR").item(0)) {
		      alert(response.getElementsByTagName("ERROR").item(0).firstChild.data);

		      return false;
		  }
		  else {
			  var elementos = response.getElementsByTagName("tiposIdentificacion").item(0);
			  var nuevosDatos = "<option value=''> -- Seleccionar --</option>";
			  for (var j = 0; j < elementos.childNodes.length; j++) {
				  if(valor == elementos.childNodes.item(j).firstChild.data) {
					  selected = "selected=selected";
				  }
				  else{
					  selected = "";
			      }
				  nuevosDatos = nuevosDatos + "<option value='"+elementos.childNodes.item(j).firstChild.data+"' "+selected+">"+elementos.childNodes.item(j).firstChild.data+"</option>\n";
			  }
			  jQuery("#codTipoIden").html(nuevosDatos);
		      return true;
		  }
		}
		else {
		  alert(Gettext.gettext("No se ha podido conectar con el servidor"));
		  alert(response.getElementsByTagName("ERROR").item(0).firstChild.data);
		  
		  return false;
		}
	}
    
</script>

<cmz:panel>
	<input type="hidden" id="lstEtiquetasDisponibles" name="lstEtiquetasDisponibles" value=""/>
	<input type="hidden" id="lstEtiquetasNuevas" name="lstEtiquetasNuevas" value=""/>
    <input type="hidden" id="lstEtiquetasEliminadas" name="lstEtiquetasEliminadas" value=""/>	
    
  <cmz:cabeceraPanel titulo="Contacto"></cmz:cabeceraPanel>
  <cmz:cuerpoPanel>
    <table cellpadding="2px" cellspacing="2px" border="0px">
	      <tr>
	      	<td><cmz:etiqueta titulo="Cód. Fidelizado"/>:</td>
	      	<td>
	          <cmz:campoTexto id="codFidelizado" valor="${fidelizado.codFidelizado}" anchura="200px" longitudMaxima="30"
	                            editable="${formularioFidelizado.editable}" soloLectura="${!formularioFidelizado.editable}"/>
	        </td>
	        <c:if test="${!formularioFidelizado.enInsercion}">	        
		        <td><cmz:etiqueta titulo="Fecha alta"/>:</td>
		        <td>
		        	<cmz:campoFecha id="fechaAlta" requerido="false" editable="false" soloLectura="true" anchura="110px">
						<fmt:formatDate pattern="dd/MM/yyyy HH:mm:ss" value="${fidelizado.fechaAlta}"/>
			        </cmz:campoFecha>
		        </td>
	        </c:if>
	      </tr>
      <tr>
      	<td><cmz:etiqueta titulo="Nombre"/>:</td>
        <td>
          <cmz:campoTexto id="nombre" valor="${fidelizado.nombre}" requerido="true" anchura="320px" longitudMaxima="45"
                            editable="${formularioFidelizado.editable}" soloLectura="${!formularioFidelizado.editable}"/>
        </td>
        <c:if test="${!formularioFidelizado.enInsercion}">
	        <td><cmz:etiqueta titulo="Fecha baja"/>:</td>
		        <td>
		        	<cmz:campoFecha id="fechaBaja" requerido="false" editable="false" soloLectura="true" anchura="110px">
						<fmt:formatDate pattern="dd/MM/yyyy HH:mm:ss" value="${fidelizado.fechaBaja}"/>
			        </cmz:campoFecha>
		        </td>
        </c:if>
      </tr>
	
	  <tr>
	  	<td><cmz:etiqueta titulo="Apellidos"/>:</td>	        
        <td><cmz:campoTexto id="apellidos" valor="${fidelizado.apellidos}" requerido="true" anchura="320px" longitudMaxima="45" 
                            editable="${formularioFidelizado.editable}" soloLectura="${!formularioFidelizado.editable}"/>
        </td>
        <td>
          <input type="checkbox" value="" id="activo" name="activo" <c:if test="${fidelizado.activo}">checked="checked"</c:if> 
                 <c:if test="${!formularioFidelizado.editable}">disabled="disabled"</c:if>/>
          <cmz:etiqueta titulo="Activo"/>
        </td>
      </tr>
      
      <tr>
      	<td><cmz:etiqueta titulo="Fecha de Nacimiento"/>:</td>	
        <td>
          <cmz:campoFecha id="fechaNacimiento" requerido="false" editable="${formularioFidelizado.editable}" soloLectura="${!formularioFidelizado.editable}">
			<fmt:formatDate pattern="dd/MM/yyyy" value="${fidelizado.fechaNacimiento}"/>
          </cmz:campoFecha>
        </td>
      </tr>
      
      <c:choose>
		  <c:when test="${formularioFidelizado.editable}">
		  	<c:set var="claseCombo" value="campo" />
		  </c:when>
		  <c:otherwise>
		  	<c:set var="claseCombo" value="campo soloLectura" />
		  </c:otherwise>
	  </c:choose>
      
	  <tr>
	  	<td><cmz:etiqueta titulo="Sexo"/>:</td>	   	   
        <td>
          <select id="sexo" name="sexo" class="${claseCombo}" <c:if test="${!formularioFidelizado.editable}">disabled="disabled"</c:if>>
            <option value="">-- <cmz:etiqueta titulo="Seleccionar"/> --</option>
            <option value="M" <c:if test="${fidelizado.sexo == 'M'}">selected="selected"</c:if>><cmz:etiqueta titulo="Mujer"/></option>
            <option value="H" <c:if test="${fidelizado.sexo == 'H'}">selected="selected"</c:if>><cmz:etiqueta titulo="Hombre"/></option>
          </select>
        </td>
      </tr>
      
	  <tr>
	  	<td><cmz:etiqueta titulo="Estado Civil"/>:</td>        
        <td>
          <select id="estadoCivil" name="estadoCivil" class="${claseCombo}" <c:if test="${!formularioFidelizado.editable}">disabled="disabled"</c:if>>
            <option value="">-- <cmz:etiqueta titulo="Seleccionar"/> --</option>
            <c:forEach items="${listaEstadosCiviles}" var="estadoCivil">
              <option value="<c:out value="${estadoCivil.codigo}" />" <c:if test="${fidelizado.codEstCivil == estadoCivil.codigo}">selected="selected"</c:if>><c:out value="${estadoCivil.descripcion}" /></option>
            </c:forEach>
          </select>
        </td>
      </tr>
      
	  <tr>
	  	<td><cmz:etiqueta titulo="Domicilio"/>:</td>       
        <td><cmz:campoTexto id="domicilio" valor="${fidelizado.domicilio}" requerido="false" anchura="330px" longitudMaxima="50" 
                            editable="${formularioFidelizado.editable}" soloLectura="${!formularioFidelizado.editable}"/>
        </td>
      </tr>

	  <tr>
	  	<td><cmz:etiqueta titulo="Población"/>:</td>
        <td><cmz:campoTexto id="poblacion" valor="${fidelizado.poblacion}" requerido="false" anchura="330px" longitudMaxima="50" 
                            editable="${formularioFidelizado.editable}" soloLectura="${!formularioFidelizado.editable}"/>
        </td>
      </tr>
      
      <tr style="display:none;">
	  	<td><cmz:etiqueta titulo="Localidad"/>:</td>
        <td><cmz:campoTexto id="localidad" valor="${fidelizado.localidad}" requerido="false" anchura="330px" longitudMaxima="50" 
                            editable="${formularioFidelizado.editable}" soloLectura="${!formularioFidelizado.editable}"/>
        </td>
      </tr>
      
	  <tr>
	  	<td><cmz:etiqueta titulo="Provincia"/>:</td>        
        <td><cmz:campoTexto id="provincia" valor="${fidelizado.provincia}" requerido="false" anchura="330px" longitudMaxima="50" 
                            editable="${formularioFidelizado.editable}" soloLectura="${!formularioFidelizado.editable}"/>
        </td>
      </tr>
	      
	  <tr>
	  	<td><cmz:etiqueta titulo="Código Postal"/>:</td>
        <td><cmz:campoTexto id="cp" valor="${fidelizado.cp}" requerido="false" anchura="60px" longitudMaxima="20" 
                            editable="${formularioFidelizado.editable}" soloLectura="${!formularioFidelizado.editable}"/>
        </td>
      </tr>
      
      <tr>	
        <td><cmz:etiqueta titulo="País"/>:</td>
	    <td>
          <cmz:ayuda requerido="false" soloLectura="${!formularioFidelizado.editable}" nombreAyuda="PAISES" onclick="obtenerTiposIdentificacion(false);">
          	<cmz:codigoAyuda idCodigo="codPais" valorCodigo="${fidelizado.codPais}" anchuraCodigo="40px" longitudMaximaCodigo="4" onchange="obtenerTiposIdentificacion(true);"></cmz:codigoAyuda>
          	<cmz:descripcionAyuda idDescripcion="desPais" valorDescripcion="${fidelizado.desPais}" anchuraDescripcion="180px"></cmz:descripcionAyuda>
          </cmz:ayuda>
	    </td>
	  </tr>  
	  
	  <tr>	
        <td><cmz:etiqueta titulo="Idioma"/>:</td>
	    <td>
          <cmz:ayuda requerido="false" soloLectura="${!formularioFidelizado.editable}" nombreAyuda="LENGUAJES">
          	<cmz:codigoAyuda idCodigo="codLengua" valorCodigo="${fidelizado.codlengua}" anchuraCodigo="40px" longitudMaximaCodigo="4"></cmz:codigoAyuda>
          	<cmz:descripcionAyuda idDescripcion="desLengua" valorDescripcion="${fidelizado.deslengua}" anchuraDescripcion="180px"></cmz:descripcionAyuda>
          </cmz:ayuda>
	    </td>
	  </tr>    
     </table>
  </cmz:cuerpoPanel>
</cmz:panel>

<cmz:panel>
  <cmz:cabeceraPanel titulo="Datos Fiscales"></cmz:cabeceraPanel>
  <cmz:cuerpoPanel>
    <table cellpadding="2px" cellspacing="2px" border="0px">
	  <tr>
        <td><cmz:etiqueta titulo="Documento de identificación"/>:</td>
        <td>
        	<c:choose>
        		<c:when test="${formularioFidelizado.editable}">
	            	<select id="codTipoIden" name="codTipoIden" class="${claseCombo}" style="width: 120px" <c:if test="${!formularioFidelizado.editable}">disabled="disabled"</c:if>>
		         		<c:choose>
		           			<c:when test="${formularioFidelizado.editable}">
		             			<option value=""> -- <cmz:etiqueta titulo="Seleccionar"/> -- </option>
		           			</c:when>
		         		</c:choose>
					</select>
				</c:when>
				<c:otherwise>
	            	<cmz:campoTexto id="codTipoIden" valor="${fidelizado.codTipoIden}" requerido="false" anchura="120px" longitudMaxima="10"
	                          editable="${formularioFidelizado.editable}" soloLectura="${!formularioFidelizado.editable}"/>
	            </c:otherwise>
            </c:choose>
		 <cmz:campoTexto id="documento" valor="${fidelizado.documento}" requerido="false" anchura="120px" longitudMaxima="20"
                            editable="${formularioFidelizado.editable}" soloLectura="${!formularioFidelizado.editable}"/>
		 <img src="comun/images/iconos/info.gif" align="absMiddle" border="0" valign="middle" style="vertical-align:top" 
		 		title='<cmz:etiqueta titulo="Para seleccionar el documento de identificación, debe seleccionar antes el país"/>'/> 
		 <c:if test="${formularioFidelizado.editable}">
	          <img src="comun/images/iconos/ok.gif" align="absMiddle" border="0" valign="middle" 
	          	onClick="validarDocumentoIdentificacion('documento', 'codPais', 'codTipoIden', false, true)" style="vertical-align:top;cursor: pointer;" 
	          	title='<cmz:etiqueta titulo="Validar Documento de identificación"/>' alt='<cmz:etiqueta titulo="Validar Documento de identificación"/>'/>
		  </c:if>       
        </td>
      </tr>
    </table>
  </cmz:cuerpoPanel>
</cmz:panel>

<cmz:panel>
  <cmz:cabeceraPanel titulo="Tienda favorita"></cmz:cabeceraPanel>
  <cmz:cuerpoPanel>
    <table cellpadding="2px" cellspacing="2px" border="0px">
      <tr>	
        <td><cmz:etiqueta titulo="Tienda"/>:</td>
	    <td>
          <cmz:ayuda requerido="false" soloLectura="${!formularioFidelizado.editable}" nombreAyuda="TIENDAS">
          	<cmz:codigoAyuda idCodigo="codAlm" valorCodigo="${fidelizado.codAlm}" anchuraCodigo="40px" longitudMaximaCodigo="10"></cmz:codigoAyuda>
          	<cmz:descripcionAyuda idDescripcion="desAlm" valorDescripcion="${fidelizado.desAlm}" anchuraDescripcion="180px"></cmz:descripcionAyuda>
          </cmz:ayuda>
	    </td>
	  </tr> 
    </table>
  </cmz:cuerpoPanel>
</cmz:panel>

<cmz:panel>
  <cmz:cabeceraPanel titulo="Opciones"></cmz:cabeceraPanel>
  <cmz:cuerpoPanel>
    <table cellpadding="2px" cellspacing="2px" border="0px">
      <tr>	
        <td>
          <input type="checkbox" value="" id="paperLess" name="paperLess" <c:if test="${fidelizado.paperLess}">checked="checked"</c:if> 
                 <c:if test="${!formularioFidelizado.editable}">disabled="disabled"</c:if>/>
          <cmz:etiqueta titulo="Solo ticket digital"/>
        </td>
	  </tr> 
    </table>
  </cmz:cuerpoPanel>
</cmz:panel>

<cmz:panel>
	<cmz:cabeceraPanel titulo="Etiquetas">
	</cmz:cabeceraPanel>
	<cmz:cuerpoPanel>
		<table cellpadding="2px" cellspacing="2px" width="100%">
			<tr>
           		<td width="6%"><cmz:etiqueta titulo="Etiquetas"/>:</td>
           		<td>
               		<ul id="tagit">
	               		<c:forEach items="${formularioFidelizado.formularioPestañaActiva.registros}" var="etiquetaCategoria">
							<li>
								${etiquetaCategoria.etiqueta}
							</li>
						</c:forEach>
        			</ul>
            	</td> 
    		</tr>
		</table>
	</cmz:cuerpoPanel>
</cmz:panel>
<cmz:panel>
  <cmz:cabeceraPanel titulo="Observaciones"></cmz:cabeceraPanel>
  <cmz:cuerpoPanel>
	      <cmz:campoAreaTexto id="observaciones" valor="${fidelizado.observaciones}" requerido="false" anchura="100%" altura="100px" longitudMaxima="255"
	                          editable="${formularioFidelizado.editable}" soloLectura="${!formularioFidelizado.editable}"/>
  </cmz:cuerpoPanel>
</cmz:panel>  
<c:choose>
	<c:when test="${formularioFidelizado.editable}">
		<script type="text/javascript">
			obtenerTiposIdentificacionPais("${fidelizado.codTipoIden}");
		</script>
	</c:when>
</c:choose>