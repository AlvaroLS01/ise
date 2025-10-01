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

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Comerzzia</title>
    
    <cmz:head/>
    
    <script type="text/javascript" language="javascript" src="comun/js/comun.js"></script>
    <script type="text/javascript" language="javascript" src="comun/js/mantenimiento.js"></script>

    <script type="text/javascript">
    	function inicio() {
	    	document.getElementById("trazaH").value = parent.document.getElementById("traza").value;
 	    }

    </script>

  </head>
  
   <body onkeydown="keyDown(event);" class="ventanaModal" onload="inicio();">
     <cmz:panelPrincipal>
        
        <cmz:cuerpoPanelPrincipal>
          <cmz:mensaje/>
          <form id="frmDatos" name="frmDatos" action=erroresInterfaces method="POST">
            <input type="hidden" name="accion" id="accion" value=""/>
            <cmz:panel>
              <cmz:cabeceraPanel titulo="Ver Traza" icono="comun/images/iconos/modo_tabla.gif" />
              <cmz:cuerpoPanel>
	            <table cellpadding="2px" cellspacing="2px" border="0px">
					<tr>
						<td>
				          <cmz:campoAreaTexto id="trazaH" anchura="800px" longitudMaxima="2000" altura="300px" soloLectura="true" editable="false" />
						</td>
					</tr>
	            </table>  
              </cmz:cuerpoPanel>
            </cmz:panel>
          </form>
        </cmz:cuerpoPanelPrincipal>
      </cmz:panelPrincipal>
   </body>
 </html>