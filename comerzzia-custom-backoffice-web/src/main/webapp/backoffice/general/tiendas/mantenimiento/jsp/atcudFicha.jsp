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

<c:set var="tienda" value="${formularioTienda.registroActivo}" />

<script type="text/javascript">
	function inicio() {
	    document.getElementById("idContador").focus();
	}

	function aceptarRegistro(){
		if (checkFormRegistroPestaña()){
			document.getElementById("idContadorHidden").value = document.getElementById("idContador").value
			document.getElementById("divisor1Hidden").value = document.getElementById("divisor1").value
			document.getElementById("divisor2Hidden").value = document.getElementById("divisor2").value
			document.getElementById("divisor3Hidden").value = document.getElementById("divisor3").value
			document.getElementById("rangoHidden").value = document.getElementById("rango").value
			document.getElementById("accion").value = "leerFormulario";
        	document.getElementById("operacion").value = "aceptarAtcud";
        	document.getElementById("frmDatos").submit();
        	_esperando();
		}


	}
	function checkFormRegistroPestaña() {
		if(!esValido("idContador", "TEXTO", true)){
            return false;
        }
		if(!esValido("divisor1", "TEXTO", true)){
            return false;
        }
		if(!esValido("divisor2", "TEXTO", true)){
            return false;
        }
		if(!esValido("divisor3", "TEXTO", true)){
            return false;
        }
		if(!esValido("rango", "TEXTO", true)){
            return false;
        }
        
        return true;
    }
</script>

<cmz:panel>
  <cmz:cabeceraPanel titulo="Alta de Caja">
  	<input type="hidden" id="idContadorHidden" name="idContadorHidden" value="">
    <input type="hidden" id="divisor1Hidden" name="divisor1Hidden" value="">
    <input type="hidden" id="divisor2Hidden" name="divisor2Hidden" value="">
    <input type="hidden" id="divisor3Hidden" name="divisor3Hidden" value="">
    <input type="hidden" id="rangoHidden" name="rangoHidden" value="">
    <cmz:acciones numAccionesVisibles="2">
      <cmz:accionSalvarLinea onclick="aceptarRegistro();"/>
      <cmz:accion onclick="cancelarRegistroPestaña();" titulo="Cancelar" icono="comun/images/iconos/cancelar.gif"></cmz:accion>
    </cmz:acciones>
  </cmz:cabeceraPanel>
    
  <cmz:cuerpoPanel>
  	<cmz:mensaje/>
    <input id="estadoObjeto" name="estadoObjeto" type="hidden" value="alta" />
   	<table cellpadding="2px" cellspacing="2px" border="0px">
	    <tr>
	    	<td><cmz:etiqueta titulo="Id Contador"/>:</td>
	    	<td>
	    	<cmz:campoTexto id="idContador" requerido="true" anchura="120px" longitudMaxima="20"
	    		editable="true" soloLectura="false"/>
	    	</td>
	    </tr>
	    
	   <tr>
	    	<td><cmz:etiqueta titulo="Divisor 1"/>:</td>
	    	<td>
	    	<cmz:campoTexto id="divisor1" requerido="true" anchura="120px" longitudMaxima="255"
	    		editable="true" soloLectura="false"/>
	    	</td>
	    </tr>
	    
	    <tr>
	    	<td><cmz:etiqueta titulo="Divisor 2"/>:</td>
	    	<td>
	    	<cmz:campoTexto id="divisor2" requerido="true" anchura="120px" longitudMaxima="255"
	    		editable="true" soloLectura="false"/>
	    	</td>
	    </tr>
	    
	    <tr>
	    	<td><cmz:etiqueta titulo="Divisor 3"/>:</td>
	    	<td>
	    	<cmz:campoTexto id="divisor3" requerido="true" anchura="120px" longitudMaxima="255"
	    		editable="true" soloLectura="false"/>
	    	</td>
	    </tr>
	    
	     <tr>
	    	<td><cmz:etiqueta titulo="Rango"/>:</td>
	    	<td>
	    	<cmz:campoTexto id="rango" requerido="true" anchura="120px" longitudMaxima="8"
	    		editable="true" soloLectura="false"/>
	    	</td>
	    </tr>
	    
	</table>
  </cmz:cuerpoPanel>
</cmz:panel>