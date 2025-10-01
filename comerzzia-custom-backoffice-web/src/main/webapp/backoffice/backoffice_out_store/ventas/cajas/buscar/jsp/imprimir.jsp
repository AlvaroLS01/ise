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
            pintaCalendario("fechaDesde", "imgFechaDesde");
            pintaCalendario("fechaHasta", "imgFechaHasta");
            setFoco("fechaDesde", "fechaHasta")
	    }
	    function volver() {
	    	document.getElementById("accion").value = "verFormulario";
	        document.getElementById("frmDatos").submit();
	    	_esperando();
	    }
	
	    function checkForm() {
		    /* Validar tb los campos ayuda */
	        if (!esValido("fechaDesde", "FECHA", true)) {
	            return false;
	        }
	        if (!esValido("fechaHasta", "FECHA", true)) {
	            return false;
	        }
			return true;
	    }
	
	    function imprimir(idVersion) {
	    	if(checkForm()) {
	            document.getElementById("accion").value = "ventas.cajas.lstCierreYMovimientoCaja";
	            document.getElementById("operacion").value = "imprimir";
		    	document.getElementById("idVersion").value = idVersion;
	    		document.getElementById("frmDatos").target = "_blank";
	    		document.getElementById("frmDatos").submit();
	    		document.getElementById("frmDatos").target = "_self";
	    	}
	    }
	    
	    function checkDobleMarcado(){
	    	var fechaEmisionChecked = document.querySelector('.fechaEmision:checked').value;
	    	var fechaRecepcionChecked = document.querySelector('.fechaRecepcion:checked').value;
 			
	    	if(fechaEmisionChecked && fechaRecepcionChecked){
	    		alert(Gettext.gettext("fechaEmisionChecked && fechaRecepcionChecked."));
	    	}
	    	
	    }
    </script>
  </head>

  <body onload="inicio();" >
    <cmz:main>
      <cmz:panelCMZ>
        <cmz:cabeceraPanelCMZ titulo="${permisos.accionMenu.titulo}" icono="${permisos.accionMenu.icono}">
          <cmz:acciones numAccionesVisibles="1">
            <cmz:accion titulo="Ver Permisos" icono="comun/images/iconos/i-key.gif" descripcion="Ver permisos efectivos del usuario" onclick="verPermisos(${permisos.accionMenu.idAccion})" />
            <c:if test="${permisos.puedeAdministrar}">
              <cmz:accion titulo="Administrar Permisos" icono="comun/images/iconos/i-admin_permisos.gif" descripcion="Administración de permisos" onclick="adminPermisos(${permisos.accionMenu.idAccion})" />
            </c:if>
          </cmz:acciones>
        </cmz:cabeceraPanelCMZ>
        
        <cmz:cuerpoPanelCMZ>
          <cmz:mensaje/>
          <form id="frmDatos" name="frmDatos" action="informesCierreYMovimientoCajaBackoffice" method="POST" target="_self">
            <input id="accion" name="accion" type="hidden" value="" />
            <input id="operacion" name="operacion" type="hidden" value="" />
            <input id="idVersion" name="idVersion" type="hidden" value="0" />
            <cmz:panel>
          		<cmz:cabeceraPanel titulo="Parámetros"></cmz:cabeceraPanel>
              	<cmz:cuerpoPanel>
	            	<table cellpadding="2px" cellspacing="2px" border="0px"> 
	              		<tr>
              				<td colspan="4">
              				<table border="0" cellpadding="0" cellspacing="0">
								<tr>
               						<td><cmz:etiqueta titulo="Fecha desde"/>:</td>
               						<td width="10px"></td>
					                <td>
					                  	<cmz:campoFecha id="fechaDesde" requerido="true" clase="campo requerido">
		                                	<fmt:formatDate pattern="dd/MM/yyyy" value="${requestScope.fechaDesde}" />
		                              	</cmz:campoFecha>
					                </td>
					                <td width="30px"></td>
					                <td><cmz:etiqueta titulo="Fecha hasta"/>:</td>
					                <td width="10px"></td>
					                <td>
					                  	<cmz:campoFecha id="fechaHasta" requerido="true" clase="campo requerido">
		                                	<fmt:formatDate pattern="dd/MM/yyyy" value="${requestScope.fechaHasta}" />
		                              	</cmz:campoFecha>
					                </td>
							  	</tr>	
	              			</table>
	              			</td>
				  		</tr>
				  		<tr>
					  		<td><cmz:etiqueta titulo="Caja"/>:</td>
							<td>
								<cmz:campoTexto id="codCaja" valor="${requestScope.codCaja}" anchura="30px" longitudMaxima="4"></cmz:campoTexto>
							</td>
				  		</tr>
                   		<tr>
                        	<td><cmz:etiqueta titulo="Nº Anticipos"/>:</td>
                          	<td><cmz:campoTexto id="numAnticipo" valor="${requestScope.numAnticipo}" anchura="50px" longitudMaxima="40"/></td>
                          	<td><cmz:etiqueta titulo="*(Sólo válido para informe de anticipos)"/></td>
                        </tr>
                       	<tr>
                       		<td><cmz:etiqueta titulo="Estado"/>:</td>
                          	<td>
                          		<select id="estado" name="estado" class="campo">
                          			<option value="" <c:if test="${requestScope.estado == ''}">selected="selected"</c:if>><cmz:etiqueta titulo="Todos"/></option>
                          			<option value="DEVUELTO" <c:if test="${requestScope.estado == 'DEVUELTO'}">selected="selected"</c:if>><cmz:etiqueta titulo="Devuelto"/></option>
                          			<option value="DISPONIBLE" <c:if test="${requestScope.estado == 'DISPONIBLE'}">selected="selected"</c:if>><cmz:etiqueta titulo="Disponible"/></option>
                          			<option value="LIQUIDADO" <c:if test="${requestScope.estado == 'LIQUIDADO'}">selected="selected"</c:if>><cmz:etiqueta titulo="Liquidado"/></option>
	                          	</select>                          
                          	</td>
                          	<td><cmz:etiqueta titulo="*(Sólo válido para informe de anticipos)"/></td>
                        </tr>
                        <tr>
                        	<td><cmz:etiqueta titulo="Cajero"/>:</td>
                        	<td width="300px">
								<cmz:ayuda requerido="false" nombreAyuda="USUARIOS" >
									<cmz:codigoAyuda idCodigo="cajero" longitudMaximaCodigo="10"></cmz:codigoAyuda>	
									<cmz:descripcionAyuda idDescripcion="desCajero" anchuraDescripcion="250px" ></cmz:descripcionAyuda>
								</cmz:ayuda>
						  	</td>
                          	<td width="250px"><cmz:etiqueta titulo="*(Sólo válido para informe financiero de cajero)"/></td>
                        </tr>
				  		<tr>
			  				<td width="200px">
			  					<cmz:etiqueta titulo="Fecha Emisión/Fecha uso tarjeta"/>
			  					<input type="radio" id="fechaEmision" name="radioTipoFechaTarjeta" value="fechaEmision" <cmz:etiqueta titulo="Fecha Emisión/Fecha uso tarjeta"/></input>
			  				</td>
		    				
				  			<td>
				  				<cmz:etiqueta titulo="Fecha Recepción/Fecha emisión tarjeta"/>
		    					<input type="radio" id="fechaRecepcion" name="radioTipoFechaTarjeta" value="fechaRecepcion" <cmz:etiqueta titulo="Fecha Recepción/Fecha emisión tarjeta"/></input>
			  				</td>
			  				<td><cmz:etiqueta titulo="* (Solo válido para el informe Formas de pago)"/></td>
				  		</tr>
				  		<tr>
				  			<td width="100px"><cmz:etiqueta titulo="Forma de pago"/></td>
				  			<td>
				  				<select id="formaPago" name="formaPago" class="campo">
									<option value=""><cmz:etiqueta titulo="-- Todos --"/></option>
									<c:forEach items="${mediosPagoDisponibles}" var="medioPago">
											<option value="${medioPago.codMedioPago}">${medioPago.desMedioPago}</option>
									</c:forEach>
								</select>
				  			</td>
				  			<td><cmz:etiqueta titulo="* (Solo válido para el informe Formas de pago)"/></td>
				  		</tr>
				  		<tr>
				  			<td width="100px"><cmz:etiqueta titulo="Tipo de tarjeta"/></td>
				  			<td>
				  				<select id="tipoTarjeta" name="tipoTarjeta" class="campo">
									<option value=""><cmz:etiqueta titulo="-- Todos --"/></option>
									<c:forEach items="${tiposTarjetaDisponibles}" var="tipoTarjeta">
											<option value="${tipoTarjeta.codtipotarj}">${tipoTarjeta.destipotarj}</option>
									</c:forEach>
								</select>
				  			</td>
				  			<td width="10px"></td>
				  			<td><cmz:etiqueta titulo="* (Solo válido para el informe Formas de pago)"/></td>
				  		</tr>
	            	</table>  
              	</cmz:cuerpoPanel>
            </cmz:panel>
            <c:import url="/core/informes/jsp/versiones.jsp"></c:import>
          </form>
        </cmz:cuerpoPanelCMZ>
      </cmz:panelCMZ>
    </cmz:main>
  </body>
</html>