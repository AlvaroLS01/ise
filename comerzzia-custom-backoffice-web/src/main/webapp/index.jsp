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

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:choose>
  <c:when test="${empty param.idSesion}">
    <c:set var="url" value="login" />
  </c:when>
  <c:otherwise>
    <c:set var="url" value="login?accion=loginDiferido" />
  </c:otherwise>
</c:choose>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<body onload="document.getElementById('frmIndex').submit();">
  <form id="frmIndex" name="frmIndex" method="post" action="<c:out value="${url}" />">
    <input type="hidden" id="uidInstancia" name="uidInstancia" value="<c:out value="${param.uidInstancia}" />">
    <input type="hidden" id="uidActividad" name="uidActividad" value="<c:out value="${param.uidActividad}" />">
    <input type="hidden" id="codEmpresa" name="codEmpresa" value="<c:out value="${param.codEmpresa}" />">
    <input type="hidden" id="idSesion" name="idSesion" value="<c:out value="${param.idSesion}" />">
  </form>
</body>
</html>
