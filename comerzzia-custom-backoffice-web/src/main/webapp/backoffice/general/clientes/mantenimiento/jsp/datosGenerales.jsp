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

<c:set var="cliente" value="${formularioCliente.registroActivo}" />

<script type="text/javascript">
    function checkFormPestaña() {
    	if (!esValido("nombreComercial", "TEXTO", false)) {
            return false;
        }
        if (!esValido("personaContacto", "TEXTO", false)) {
            return false;
        }
        if (!esValido("domicilio", "TEXTO", false)) {
            return false;
        }
        if (!esValido("poblacion", "TEXTO", false)) {
            return false;
        }

        if (!esValido("provincia", "TEXTO", false)) {
            return false;
        }
		if (!esValido("codPais", "AYUDA", true, "desPais", "PAISES")) {
			return false;
		}
		if (!esValido("codLengua", "AYUDA", false, "desLengua", "LENGUAJES")){
			return false;
		}
        if (!esValido("cp", "TEXTO", false)) {
            return false;
        }
        if (!esValido("telefono1", "TEXTO", false)) {
            return false;
        }
        if (!esValido("telefono2", "TEXTO", false)) {
            return false;
        }
        if (!esValido("fax", "TEXTO", false)) {
            return false;
        }
        if (!esValido("email", "TEXTO", false)) {
            return false;
        }
        if (!validarDocumentoIdentificacion("cif", "codPais", "codTipoIden", false)) {
            return false;
        }
    	if (!esValido("idTratImp", "TABLA", true, "tratamiento impuestos")) {
            return false;
        }
		if (!esValido("codTar", "AYUDA", false, "desTar", "TARIFAS")) {
			return false;
		}
		if (!esValido("codSec", "AYUDA", false, "desSec", "SECCIONES")) {
			return false;
		}
        return true;
    }

    function limpiarCamposRelacionados(onchange) {
    	var actualizado = false;
        if(!onchange) {
        	buscar("PAISES", "codPais", "desPais", null, null, callbackLimpiarCamposRelacionados);
        }
        else{
        	actualizado = buscarXML("PAISES", "codPais", "desPais");
        }

        if(actualizado) {
    		borrarDatos("idTratImp", "desTratImp");
    		obtenerTiposIdentificacionPais();
        }
    }

    function callbackLimpiarCamposRelacionados(event){
    	borrarDatos("idTratImp", "desTratImp");
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
			  var nuevosDatos = "<option value=''>-- Seleccionar --</option>";
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
  <cmz:cabeceraPanel titulo="Contacto"></cmz:cabeceraPanel>
  <cmz:cuerpoPanel>
    <table cellpadding="2px" cellspacing="2px" border="0px">
      <tr>
        <td><cmz:etiqueta titulo="Nombre comercial"/>:</td>
        <td>
          <cmz:campoTexto id="nombreComercial" valor="${cliente.nombreComercial}" requerido="false" anchura="300px" longitudMaxima="45"
                            editable="${formularioCliente.editable}" soloLectura="${!formularioCliente.editable}"/>
        </td>
      </tr>
	
	  <tr>	        
        <td><cmz:etiqueta titulo="Persona contacto"/>:</td>
        <td><cmz:campoTexto id="personaContacto" valor="${cliente.personaContacto}" requerido="false" anchura="300px" longitudMaxima="45" 
                            editable="${formularioCliente.editable}" soloLectura="${!formularioCliente.editable}"/>
        </td>
      </tr>  

	  <tr>	        
        <td><cmz:etiqueta titulo="Domicilio"/>:</td>
        <td><cmz:campoTexto id="domicilio" valor="${cliente.domicilio}" requerido="false" anchura="320px" longitudMaxima="50" 
                            editable="${formularioCliente.editable}" soloLectura="${!formularioCliente.editable}"/>
        </td>
      </tr>  

	  <tr>	        
        <td><cmz:etiqueta titulo="Población"/>:</td>
        <td><cmz:campoTexto id="poblacion" valor="${cliente.poblacion}" requerido="false" anchura="320px" longitudMaxima="50" 
                            editable="${formularioCliente.editable}" soloLectura="${!formularioCliente.editable}"/>
        </td>
      </tr>

	  <tr>	        
        <td><cmz:etiqueta titulo="Provincia"/>:</td>
        <td><cmz:campoTexto id="provincia" valor="${cliente.provincia}" requerido="false" anchura="320px" longitudMaxima="50" 
                            editable="${formularioCliente.editable}" soloLectura="${!formularioCliente.editable}"/>
        </td>
      </tr>  

	  <tr>	  
        <td><cmz:etiqueta titulo="Código Postal"/>:</td>
        <td><cmz:campoTexto id="cp" valor="${cliente.cp}" requerido="false" anchura="60px" longitudMaxima="8" 
                            editable="${formularioCliente.editable}" soloLectura="${!formularioCliente.editable}"/>
        </td>
      </tr>
      
      <tr>	
        <td><cmz:etiqueta titulo="País"/>:</td>
	    <td>
          <cmz:ayuda requerido="true" soloLectura="${!formularioCliente.editable}" nombreAyuda="PAISES" onclick="limpiarCamposRelacionados(false);">
          	<cmz:codigoAyuda idCodigo="codPais" valorCodigo="${cliente.codPais}" anchuraCodigo="40px" longitudMaximaCodigo="4" onchange="limpiarCamposRelacionados(true);"></cmz:codigoAyuda>
          	<cmz:descripcionAyuda idDescripcion="desPais" valorDescripcion="${cliente.desPais}" anchuraDescripcion="180px"></cmz:descripcionAyuda>
          </cmz:ayuda>
	    </td>
	  </tr>
	  
	  <tr>	
        <td><cmz:etiqueta titulo="Idioma"/>:</td>
	    <td>
          <cmz:ayuda requerido="false" soloLectura="${!formularioCliente.editable}" nombreAyuda="LENGUAJES">
          	<cmz:codigoAyuda idCodigo="codLengua" valorCodigo="${cliente.codlengua}" anchuraCodigo="40px" longitudMaximaCodigo="4"></cmz:codigoAyuda>
          	<cmz:descripcionAyuda idDescripcion="desLengua" valorDescripcion="${cliente.deslengua}" anchuraDescripcion="180px"></cmz:descripcionAyuda>
          </cmz:ayuda>
	    </td>
	  </tr>

	  <tr>	        
        <td><cmz:etiqueta titulo="Teléfonos"/>:</td>
        <td>
        	<cmz:campoTexto id="telefono1" valor="${cliente.telefono1}" requerido="false" anchura="120px" longitudMaxima="15" 
                            editable="${formularioCliente.editable}" soloLectura="${!formularioCliente.editable}"/>
        	<cmz:campoTexto id="telefono2" valor="${cliente.telefono2}" requerido="false" anchura="120px" longitudMaxima="15" 
                            editable="${formularioCliente.editable}" soloLectura="${!formularioCliente.editable}"/>
        </td>
      </tr>  

	  <tr>	        
        <td><cmz:etiqueta titulo="Fax"/>:</td>
        <td><cmz:campoTexto id="fax" valor="${cliente.fax}" requerido="false" anchura="120px" longitudMaxima="15" 
                            editable="${formularioCliente.editable}" soloLectura="${!formularioCliente.editable}"/>
        </td>
      </tr>  

	  <tr>	        
        <td><cmz:etiqueta titulo="Email"/>:</td>
        <td><cmz:campoTexto id="email" valor="${cliente.email}" requerido="false" anchura="380px" longitudMaxima="60" 
                            editable="${formularioCliente.editable}" soloLectura="${!formularioCliente.editable}"/>
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
            <c:when test="${formularioCliente.editable}">
              <select id="codTipoIden" name="codTipoIden" class="campo" style="width: 120px" >
          		<c:choose>
            			<c:when test="${formularioCliente.editable}">
              			<option value="">-- <cmz:etiqueta titulo="Seleccionar"/> --</option>
            			</c:when>
          		</c:choose>
 			</select>
            </c:when>
            <c:otherwise>
            	<cmz:campoTexto id="codTipoIden" valor="${cliente.codTipoIden}" requerido="false" anchura="120px" longitudMaxima="10"
                          editable="${formularioCliente.editable}" soloLectura="${!formularioCliente.editable}"/>
            </c:otherwise>
          </c:choose>
		 <cmz:campoTexto id="cif" valor="${cliente.cif}" requerido="false" anchura="120px" longitudMaxima="20"
                            editable="${formularioCliente.editable}" soloLectura="${!formularioCliente.editable}"/>
		  <img src="comun/images/iconos/info.gif" align="absMiddle" border="0" valign="middle" style="vertical-align:top" 
		  		title='<cmz:etiqueta titulo="Para seleccionar el documento de identificación, debe seleccionar antes el país"/>'/> 
		  <c:if test="${formularioCliente.editable}">
	          <img src="comun/images/iconos/ok.gif" align="absMiddle" border="0" valign="middle" 
	          	onClick="validarDocumentoIdentificacion('cif', 'codPais', 'codTipoIden', false, true)" style="vertical-align:top;cursor: pointer;" 
	          	title='<cmz:etiqueta titulo="Validar Documento de identificación"/>' alt='<cmz:etiqueta titulo="Validar Documento de identificación"/>'/>
		  </c:if>       
        </td>
      </tr>
      <tr>
        <td><cmz:etiqueta titulo="Tratamiento impuestos"/>:</td>
        <td>
          <cmz:ayuda requerido="true" soloLectura="${!formularioCliente.editable}" nombreAyuda="TRATAMIENTO_IMP_PAIS" campoFiltro="codPaisEmpresa">
          	<cmz:codigoAyuda idCodigo="idTratImp" valorCodigo="${cliente.idTratImp}"></cmz:codigoAyuda>
          	<cmz:descripcionAyuda idDescripcion="desTratImp" valorDescripcion="${cliente.desTratImp}" anchuraDescripcion="320px"></cmz:descripcionAyuda>
          </cmz:ayuda>
        </td>
      </tr>
      
      <tr>
        <td><cmz:etiqueta titulo="Medio de pago"/>:</td>
        <td>
          <cmz:ayuda requerido="false" soloLectura="${!formularioCliente.editable}" nombreAyuda="MEDIOS_PAGO_VEN">
            <cmz:codigoAyuda idCodigo="idMedioPagoVencimiento" valorCodigo="${cliente.idMedioPagoVencimiento}"></cmz:codigoAyuda>
            <cmz:descripcionAyuda idDescripcion="desMedioPagoVencimiento" valorDescripcion="${cliente.desMedioPagoVencimiento}" anchuraDescripcion="320px"></cmz:descripcionAyuda>
          </cmz:ayuda>
        </td>
      </tr>
      
      <tr>
        <td><cmz:etiqueta titulo="Tarifa"/>:</td>
        <td>
          <cmz:ayuda requerido="false" soloLectura="${!formularioCliente.editable}" nombreAyuda="TARIFAS">
          	<cmz:codigoAyuda idCodigo="codTar" valorCodigo="${cliente.codTar}" anchuraCodigo="110px" longitudMaximaCodigo="11"></cmz:codigoAyuda>
          	<cmz:descripcionAyuda idDescripcion="desTar" valorDescripcion="${cliente.desTar}" anchuraDescripcion="200px"></cmz:descripcionAyuda>
          </cmz:ayuda>
        </td>
      </tr>
      
      <tr>
        <td><cmz:etiqueta titulo="Riesgo concedido"/>:</td>
        <td>
          <cmz:campoTexto id="riesgoConcecido" requerido="false" anchura="70" longitudMaxima="9"
            editable="${formularioCliente.editable}" soloLectura="${!formularioCliente.editable}">
             <cmz:formateaNumero valor="${cliente.riesgoConcedido}"></cmz:formateaNumero>               
          </cmz:campoTexto>
        </td>
      </tr>
      
      <tr>
	      <td><cmz:etiqueta titulo="Depósito"/>:</td>
	      <td>
		      <select id="deposito" name="deposito"
		      	<c:choose>
		      		<c:when test="${!formularioCliente.editable}">
		      			disabled class="soloLectura"
		      		</c:when>
		      		<c:otherwise>
		      			class="campo"
		      		</c:otherwise>
		      	</c:choose>
		      >
		      	<option value="N" <c:if test="${cliente.deposito == null || cliente.deposito == '' || cliente.deposito == 'N'}">selected="selected"</c:if>><cmz:etiqueta titulo="No"/></option>
		      	<option value="S" <c:if test="${cliente.deposito == 'S'}">selected="selected"</c:if>><cmz:etiqueta titulo="Sí"/></option>
		      </select>                    
	      </td>
      </tr>
      
    </table>  
  </cmz:cuerpoPanel>
</cmz:panel>  

<cmz:panel>
  <cmz:cabeceraPanel titulo="Organización"></cmz:cabeceraPanel>
  <cmz:cuerpoPanel>
    <table cellpadding="2px" cellspacing="2px" border="0px">
      <tr>
        <td><cmz:etiqueta titulo="Sector"/>:</td>
        <td>
          <cmz:ayuda requerido="false" soloLectura="${!formularioCliente.editable}" nombreAyuda="SECTORES">
          	<cmz:codigoAyuda idCodigo="codSec" valorCodigo="${cliente.codSec}" anchuraCodigo="40px" longitudMaximaCodigo="4"></cmz:codigoAyuda>
          	<cmz:descripcionAyuda idDescripcion="desSec" valorDescripcion="${cliente.desSec}" anchuraDescripcion="200px"></cmz:descripcionAyuda>
          </cmz:ayuda>
        </td>
      </tr>
    </table>  
  </cmz:cuerpoPanel>
</cmz:panel>
<c:choose>
	<c:when test="${formularioCliente.editable}">
		<script type="text/javascript">
		obtenerTiposIdentificacionPais("${cliente.codTipoIden}");
		</script>
	</c:when>
</c:choose>
