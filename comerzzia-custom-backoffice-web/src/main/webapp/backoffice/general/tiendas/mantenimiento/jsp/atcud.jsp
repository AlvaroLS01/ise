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
    function checkFormRegistroPestaña() {
    	var numChecks = document.frmDatos.cajas.length;
    		if(numChecks){
    		    for(var i = 0; i < numChecks; i++){
        			if(document.frmDatos.cajas[i].checked){
        				return true;
        			}
    			}
    		}
    		else {
    			if(document.frmDatos.cajas.checked) {
                    return true;
    		}
    	}
    
    		alert(Gettext.gettext("Debes elegir al menos una caja"));
    	return false;
    }

</script>

<cmz:panel>
  <cmz:cabeceraPanel titulo="Configuracion de Rangos Fiscales">
    <cmz:acciones numAccionesVisibles="1">
     <c:if test="${formularioTienda.editable}">
			<cmz:accionNuevaLinea onclick="nuevoRegistroPestaña();" descripcion="Añadir nuevo rango fiscal"/>
	</c:if>
    </cmz:acciones>
  </cmz:cabeceraPanel>
    
  <cmz:cuerpoPanel>
    <cmz:lista>
      <cmz:cabeceraLista>
        <cmz:itemCabeceraLista nombre="ID CONTADOR" columna="1"></cmz:itemCabeceraLista>
        <cmz:itemCabeceraLista nombre="DIVISOR 1" columna="2"></cmz:itemCabeceraLista>
        <cmz:itemCabeceraLista nombre="DIVISOR 2" columna="3"></cmz:itemCabeceraLista>
        <cmz:itemCabeceraLista nombre="DIVISOR 3" columna="4"></cmz:itemCabeceraLista>
        <cmz:itemCabeceraLista nombre="RANGO" columna="1"></cmz:itemCabeceraLista>
           <cmz:itemCabeceraLista nombre="Acciones" estilo="text-align: center;"></cmz:itemCabeceraLista>
      </cmz:cabeceraLista>
      <cmz:contenidoLista variable="rango" lista="${requestScope.lstRangos}">
		<cmz:itemContenidoLista valor="${rango.idContador}"></cmz:itemContenidoLista>
		<cmz:itemContenidoLista valor="${rango.mascaraDivisor1}"></cmz:itemContenidoLista>
		<cmz:itemContenidoLista valor="${rango.mascaraDivisor2}"></cmz:itemContenidoLista>
		<cmz:itemContenidoLista valor="${rango.mascaraDivisor3}"></cmz:itemContenidoLista>
		<cmz:itemContenidoLista valor="${rango.rango}"></cmz:itemContenidoLista>
		<cmz:acciones alineacion="center">
		<c:if test="${formularioTienda.editable}">
				<cmz:accion onclick="eliminarRegistroPestaña(${indice})" icono="comun/images/iconos/i-cancel.gif" descripcion="Eliminar Registro Fiscal"></cmz:accion>
		</c:if>
		</cmz:acciones>
	  </cmz:contenidoLista>
    </cmz:lista>
  </cmz:cuerpoPanel>
</cmz:panel>