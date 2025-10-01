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
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:useBean id="appInfo" scope="request" class="com.comerzzia.core.util.config.AppInfo"></jsp:useBean>
<jsp:useBean id="version" scope="page" class="com.comerzzia.core.util.version.Version"></jsp:useBean>
<jsp:useBean id="anyoActual" scope="request" type="java.lang.Integer"></jsp:useBean>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Comerzzia - Cambio de clave</title>
    
    <link rel="stylesheet" type="text/css" media="screen,projection,print" href="core/login/css/login.css" />
    <link rel="shortcut icon" href="comun/images/iconos/favicon.ico"/>
<!--[if lt IE 8]>
<link rel="stylesheet" type="text/css" media="screen,projection,print" href="core/login/css/login-ie8.css" />
<![endif]-->
<!--[if IE 7]>
<link rel="stylesheet" type="text/css" media="screen,projection,print" href="core/login/css/login-ie7.css" />
<![endif]-->
    
    <script type="text/javascript" language="javascript" src="comun/js/comun.js"></script>
	<script type="text/javascript" language="javascript" src="comun/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" language="javascript" src="comun/js/md5.js"></script>
    
    <script type='text/javascript' language="javascript" src='comun/js/prototype.js'></script>
    <script type='text/javascript' language="javascript" src='comun/js/gettext.js'></script>
    
    <script type="text/javascript" language="javascript" src="comun/js/validacion.js"></script>
    
    <script type="text/javascript" language="javascript">
	    function checkFormContraseña() {
	    	if(!esValido("antigua", "TEXTO", true)){
	            return false;
	        }
			
			if(!esValido("password", "TEXTO", true)){
	            return false;
	        }
			
			if(!esValido("confirma", "TEXTO", true)){
	            return false;
	        }
	    	
	    	var password = document.getElementById("password").value;
	        var confirma = document.getElementById("confirma").value;
	        if(password != confirma){
	            window.alert(Gettext.gettext("Las contraseñas no son iguales"));
	            return false;
	        }
	        return true;
	    }
	    
	    function aceptarContraseña() {
        	if(checkFormContraseña()) {
        		if (document.getElementById("antigua").value.length == 0){
		    		document.getElementById("claveAnt").value = "";
		    	}
                else {
		    		document.getElementById("claveAnt").value = hex_md5(document.getElementById("antigua").value);
		    	}
          
        		if (document.getElementById("password").value.length == 0){
		    		document.getElementById("clave").value = "";
		    	}
                else {
		    		document.getElementById("clave").value = hex_md5(document.getElementById("password").value);
		    	}
                
                document.getElementById("accion").value = "cambiarClave";  
                document.getElementById("frmDatos").submit();	
          	    _esperando();
        	}
        }
	    
	    function cancelarContraseña() {
        	document.getElementById("accion").value = "logout";
			document.getElementById("frmDatos").submit();
			_esperando();
        }
	    
	    function inicio(){
	    	setFoco("antigua");
	    }
    </script>  
  </head>
  <body onload="inicio();" onkeydown="keyDown(event);">
    <div id="login-main">
    	<c:set var="entorno" value="<%=  appInfo.getEntorno() %>"/>
	    <c:if test="${not empty entorno}">
	    	<span class="sale-box no-print">
				<span class="sale-label">${entorno}</span>
			</span>
	    </c:if>
    
      <div id="login-box">        
        <div id="login-title" style="background-image:url('<%= appInfo.getUrlLogoLogin() %>')">
          <div id="login-version">
             <c:out value="${version.versionLogin}"></c:out>
          </div>
        </div>  
        <div id="login-form">
          <form id="frmDatos" name="frmDatos" method="post" action="">
            <input type="hidden" id="accion" name="accion" value="cambiarClave" />
            <input type="hidden" id="uidInstanciaSeleccionada" name="uidInstanciaSeleccionada" value="<c:out value="${param.uidInstanciaSeleccionada}" />" />
            <input type="hidden" id="uidActividad" name="uidActividad" value="<c:out value="${param.uidActividad}" />" />
            
            <input type="hidden" id="usuario" name="usuario" value="<c:out value="${param.usuario}" />" />
            
            <input id="clave" name="clave" type="hidden" value="" />
            <input id="claveAnt" name="claveAnt" type="hidden" value="" />
            
		    <div>
			  <div class="input-login">
				<div class="linea-form" id="">
					<label for="antigua"><cmz:etiqueta titulo="Antigua contraseña"/>: </label>
					<input type="password" name="antigua" value="" id="antigua" class="login-input"/>
				</div>
				<div class="linea-form" id="">
					<label for="password"><cmz:etiqueta titulo="Nueva contraseña"/>: </label>
					<input type="password" name="password" value="" id="password" class="login-input"/>
				</div>
				<div class="linea-form" id="">
					<label for="confirma"><cmz:etiqueta titulo="Repetir contraseña"/>: </label>
					<input type="password" name="confirma" value="" id="confirma" class="login-input"/>
				</div>
				<div class="linea-form" id="send">					
					<input type="button" <%--id="frmDatos_0"--%> value="<cmz:etiqueta titulo="Entrar"/>" class="login-submit" tabindex="3" onclick="aceptarContraseña();"/>
					<input type="button" value="<cmz:etiqueta titulo="Volver"/>" class="login-submit" tabindex="4" onclick="cancelarContraseña();"/>
				</div>
				<div id="login-result">
				    <c:out value="${requestScope.mensajeError}" />				     
				  </div>
				
			  </div>
			  
		    </div>
          </form>
        </div>     
      </div>	
	</div>
	<div id="footer">
		<p><%= appInfo.getComerzziaVersion() + ' ' + appInfo.getVersionRevision() %>. &copy; 2008-${anyoActual}&nbsp;<cmz:etiqueta titulo="Comerzzia S.L. Todos los derechos reservados"/>.</p>
	</div>
  </body>
</html>