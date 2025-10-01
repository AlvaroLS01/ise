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

<jsp:useBean id="exception" class="java.lang.Exception" scope="request" />

<c:choose>
  <c:when test="${!empty requestScope.nextPage}">
    <c:set var="nextPage" value="${requestScope.nextPage}" />
  </c:when>
  <c:otherwise>
    <c:set var="nextPage" value="inicio.screen" />
  </c:otherwise>
</c:choose>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Comerzzia</title>
    
    <cmz:head/>
    
    <script language="javascript">
        function mostrarException() {
            if (document.getElementById("divException").style.display == "none") {
	            document.getElementById("divException").style.display = "";
	        }
	        else {
	            document.getElementById("divException").style.display = "none";
	        }
        }
        
        function aceptar() {
        	document.frmDatos.action = "<c:out value="${nextPage}"/>";
			document.frmDatos.submit();
        }
        
        function cerrar() {
            window.close();
        }
        
        function keyDown() {
            if (event.keyCode == 13) {
                aceptar();
            }
        }
    </script>
    
  </head>

  <body onkeydown="keyDown();">
    <cmz:main>
      <cmz:panelCMZ>
        <cmz:cabeceraPanelCMZ titulo="Error" icono="comun/images/iconos/error_pequeno.png"></cmz:cabeceraPanelCMZ>
          <cmz:cuerpoPanelCMZ alineacion="center">
          	<form id="frmDatos" name="frmDatos" method="post" action="">
	          	<table width="50%" align="center">
	          		<tr>
	          			<td>
	          				<cmz:panel>
				            	<cmz:cuerpoPanel alineacion="center">
				            		<table align="center" width="500px;">
				            			<tr>
				            				<td onclick="JavaScript:mostrarException()"><img src='<c:url value="comun/images/iconos/error.png"/>'></td>
				            				<td>
												<table border="0" align="center" cellspacing="2"
				                                  cellpadding="2">
				                                  <tr>
				                                    <td align="center"><!-- tabla del mensaje de error -->
				                                    	<table cellpadding="4" cellspacing="2" border="0" width="100%">
															<c:choose>
																<c:when test="${requestScope.mensaje == null}">
																	<tr>
																		<td align="center" class="mensaje"
																			style="font-size: 12px;"><cmz:etiqueta titulo="Se ha producido un error en la descarga del informe"/>.</td>
																	</tr>
																	<tr>
																		<td align="center" class="mensaje"
																			style="font-size: 12px;"><cmz:etiqueta titulo="Disculpe las molestias"/>.
																		</td>
																	</tr>
																</c:when>
																<c:otherwise>
																	<tr>
																		<td align="center" class="mensaje"
																			style="font-size: 12px;"><c:out
																			value="${requestScope.mensaje}" /></td>
																	</tr>
																</c:otherwise>
															</c:choose>
														</table>
				                                    </td>
				                                  </tr>
				        
				                                  
				                                <!-- botones -->
				                            
				                                  <tr>
				                                    <td align="center">
				                                    <table cellpadding="7" cellspacing="2" border="0">
				                                      <tr>
				                                        <td align="center">
				                                          <table cellspacing="0">
														    <tr>
														      <td>
														        <input type="button"  value="Aceptar" class="boton" onclick="JavaScript:aceptar()" style="width: 80px">
														      </td>
														    </tr>
														  </table>
				                                        </td>
				                                      </tr>
				                                    </table>
				                                    </td>
				                                  </tr>
				                                  
				                              </table>
				                             </td>
				                            </tr>
				                            
				                            <!-- Excepcion -->
				                            <tr>
				                              <td colspan="2">
				                                <div id="divException" style="display:none">
                                                  <c:out value="Excepción: ${exception.localizedMessage}"></c:out>
                                                  <br><br>
                                                  <c:out value="Traza:"></c:out><br>
                                                  <c:forEach items="${exception.stackTrace}" var="traza">
                                                    <c:out value="${traza}"></c:out>
                                                    <br>
                                                  </c:forEach>
				                              </div>
				                              </td>
				                            </tr>
				                            <!-- Fin Excepcion -->
				                          </table>
				            	</cmz:cuerpoPanel>
				            </cmz:panel>
	          			</td>
	          		</tr>
	          	</table>
          	</form>
          </cmz:cuerpoPanelCMZ>
      </cmz:panelCMZ>
    </cmz:main>
  </body>
</html>