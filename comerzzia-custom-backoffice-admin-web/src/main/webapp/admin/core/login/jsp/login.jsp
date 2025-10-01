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
<jsp:useBean id="empresas" scope="request" type="java.util.List<com.comerzzia.core.model.empresas.EmpresaBean>"></jsp:useBean>
<jsp:useBean id="arrayEmpresas" scope="request" type="java.lang.String"></jsp:useBean>
<jsp:useBean id="anyoActual" scope="request" type="java.lang.Integer"></jsp:useBean>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Comerzzia - Login</title>
    
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
    
    <script type="text/javascript" language="javascript" src="core/login/js/login.js"></script>
    
    <script type="text/javascript" language="javascript">
        jQuery.noConflict();
        var arrayEmpresas = <c:out value="${arrayEmpresas}" escapeXml="false" />;
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
          <form id="frmLogin" name="frmLogin" method="post" action="login">
            <input type="hidden" id="accion" name="accion" value="login" />
            <input type="hidden" id="clave" name="clave" value="" />
            <input type="hidden" id="uidInstancia" name="uidInstancia" value="<c:out value="${param.uidInstancia}" />" />
            <input type="hidden" id="uidActividad" name="uidActividad" value="<c:out value="${param.uidActividad}" />" />
            <input type="hidden" id="codEmpresa" name="codEmpresa" value="<c:out value="${param.codEmpresa}" />" />
            
            <input type="hidden" id="uidInstanciaSeleccionada" name="uidInstanciaSeleccionada" value="" />
            <input type="hidden" id="uidActividadSeleccionada" name="uidActividadSeleccionada" value="" />
            <input type="hidden" id="codEmpresaSeleccionada" name="codEmpresaSeleccionada" value="" />
            <input type="hidden" id="empresaSeleccionada" name="empresaSeleccionada" value="" />
          
		    <div> 
		    <div class="lista-empresas">
				  
				  <div class="lista-empresas-c">
					<div id="logo-grande">
                       <c:forEach items="${empresas}" var="empresa" varStatus="status">
                          <div id="e_<c:out value="${status.index}" />" class="empresa-seleccionada">
                          	<p class="titulo-empresas"><c:out value="${empresa.desEmp}" /></p>						
                          	<div class="logo-imagen-wrap">	
                          	<div class="logo-nav-flechas" id="logo-prev" onclick="irAnterior();"><cmz:etiqueta titulo="Anterior"/></div>					
                          	  <div class="logo-imagen">
                                <div class="imgwrap">
                          	      <img title="<c:out value="${empresa.desEmp}" />" alt="<c:out value="${empresa.desEmp}" />" src="logoEmpresa?codemp=${empresa.codEmp}&uidact=${empresa.uidActividad}&uidins=${empresa.uidInstancia}" onError="this.src='core/login/images/logo-default.png'" />
                                </div>
                          	  </div>
                          	  <div class="logo-nav-flechas" id="logo-next" onclick="irSiguiente();"><cmz:etiqueta titulo="Siguiente"/></div>
                          	  <div class="limpia"></div>
                          	</div>
                          </div>
                        </c:forEach>
                        
                        <c:if test="${fn:length(empresas) > 1}">
                          <div id="logo-nav">
                           
                            <div class="logo-vertodas"><a href="#" onclick="verTodas();"><cmz:etiqueta titulo="Ver empresas"/></a></div>
                          </div>
                        </c:if>
					</div>
					<div id="logo-listado">
					    <p class="titulo-empresas"><cmz:etiqueta titulo="Seleccione empresa"/></p>
						<div class="logo-lista-wrap">
							<ul>
                              <c:forEach items="${empresas}" var="empresa" varStatus="status">
                                <c:choose>
                                  <c:when test="${status.count %2 == 0}">
                                    <c:set var="estiloFila" value="fila-par"/>
                                  </c:when>
                                  <c:otherwise>
                                    <c:set var="estiloFila" value="fila-impar"/>
                                  </c:otherwise>
                                </c:choose>
								<li>
                                  <a href="#" id="<c:out value="t_${status.index}" />" onclick="seleccionarEmpresa('<c:out value="e_${status.index}" />');">
                                    <div class="fila-lista <c:out value="${estiloFila}" />">
                                      <div class="logo-imagen-wrap thumb">
                                        <div class="logo-imagen">
                                          <div class="imgwrap">
                                            <img title="<c:out value="${empresa.desEmp}" />" alt="<c:out value="${empresa.desEmp}" />" src="logoEmpresa?codemp=${empresa.codEmp}&uidact=${empresa.uidActividad}&uidins=${empresa.uidInstancia}" onError="this.src='core/login/images/logo-default.png'" />
                                          </div>
                                        </div>
                                      </div>
                                      <p class="nombre-empresa"><c:out value="${empresa.desEmp}" /></p>
                                    </div>
                                  </a>
                                </li>
                              </c:forEach>
							</ul>
						</div>
					</div>
				  </div>
				
				</div>                  
			  <div class="input-login">
				<div class="linea-form" id="usuarioInput">
					<label for="login"><cmz:etiqueta titulo="Usuario"/>: </label>
					<input type="text" name="usuario" value="" id="usuario" class="login-input"/>
				</div>
				<div class="linea-form" id="passInput">
					<label for="password"><cmz:etiqueta titulo="Contraseña"/>: </label>
					<input type="password" name="password" value="" id="password" class="login-input"/>
				</div>
				<div class="linea-form" id="send">					
					<input type="button" id="frmDatos_0" value="<cmz:etiqueta titulo="Entrar"/>" class="login-submit" tabindex="3" onclick="aceptar();"/>
				</div>
				<div id="login-result">
				    <c:out value="${requestScope.mensajeError}" />
				</div>
				<%-- BRICO-128 --%>
				<c:if test="${not empty requestScope.mensaje}">
					<div class="linea-form" style="color:#ffffff;">${requestScope.mensaje}</div>
				</c:if>
				<%-- fin BRICO-128 --%>
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