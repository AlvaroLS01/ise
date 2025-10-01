<%--
    ComerZZia 3.0

    Copyright (c) 2008-2015 Comerzzia, S.L.  All Rights Reserved.

    THIS WORK IS  SUBJECT  TO  SPAIN  AND  INTERNATIONAL  COPYRIGHT  LAWS  AND
    TREATIES.   NO  PART  OF  THIS  WORK MAY BE  USED,  PRACTICED,  PERFORMED,
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

<script type="text/javascript">
var idVariable = "";
var valor = "";

function aceptarVariables(){
    var i;
    var numChecks = document.frmDatos.valor.length;
    if(numChecks){
        for(i = 0; i < numChecks; i++){
            var lstValor = document.frmDatos.valor[i].value;
            var lstIdVariable = document.frmDatos.idVar[i].value;
            idVariable += lstIdVariable + '|';
            valor += lstValor + "|";
        }
        document.getElementById("idVariables").value = idVariable;
        document.getElementById("valores").value = valor;
    }
    document.getElementById("accion").value = "leerFormulario";
    document.getElementById("operacion").value = "aceptarRegistroPestaña";
    document.getElementById("frmDatos").submit();
    _esperando();
}
</script>

<cmz:panel>
  <cmz:cabeceraPanel titulo="Variables">
    <cmz:acciones numAccionesVisibles="2">
      <c:choose>
        <c:when test="${formularioTienda.formularioPestañaActiva.modoVisualizacionFicha}">
          <cmz:accion onclick="aceptarVariables();" titulo="Aceptar" icono="comun/images/iconos/aceptar.gif"></cmz:accion>
          <cmz:accion onclick="cancelarRegistroPestaña();" titulo="Cancelar" icono="comun/images/iconos/cancelar.gif"></cmz:accion>
        </c:when>
        <c:otherwise>
          <c:if test="${formularioTienda.editable}">
            <cmz:accion icono="comun/images/iconos/i-edit.gif" descripcion="Editar Variables" onclick="editarRegistroPestaña(${formularioTienda.formularioPestañaActiva.indiceRegistroActivo});" />
          </c:if>
        </c:otherwise>
      </c:choose>
    </cmz:acciones>
  </cmz:cabeceraPanel>
  <cmz:cuerpoPanel>
    <cmz:lista>
      <cmz:cabeceraLista>
        <cmz:itemCabeceraLista nombre=""></cmz:itemCabeceraLista>
        <cmz:itemCabeceraLista nombre="Variable"></cmz:itemCabeceraLista>
        <cmz:itemCabeceraLista nombre="Valor" estilo="text-align: left;"></cmz:itemCabeceraLista>
      </cmz:cabeceraLista>
      <cmz:contenidoLista variable="variable" lista="${formularioTienda.formularioPestañaActiva.registros}">
        <c:choose>
          <c:when test="${variable.idVariable == 'X_POS.TEXTO_INFORMATIVO_LEGAL_2' or 
                           variable.idVariable == 'X_POS.TEXTO_INFORMATIVO_LEGAL_3' or 
                           variable.idVariable == 'X_POS.TEXTO_INFORMATIVO_LEGAL_4'}">
            <div style="display:none;">
              <c:choose>
                <c:when test="${formularioTienda.formularioPestañaActiva.modoVisualizacionFicha}">
                  <input type="hidden" id="idVariable" name="idVar" value="${variable.idVariable}">
                  <cmz:campoAreaTexto valor="${variable.valor}" id="valor" longitudMaxima="1000" anchura="800px" altura="120px" soloLectura="false" editable="true"/>
                </c:when>
                <c:otherwise>
                  <input type="hidden" id="idVar" name="idVar" value="${variable.idVariable}">
                  <cmz:campoAreaTexto valor="${variable.valor}" id="valor" longitudMaxima="250" anchura="800px" altura="120px" soloLectura="true" editable="false"/>
                </c:otherwise>
              </c:choose>
            </div>
          </c:when>
          <c:otherwise>
            <c:choose>
              <c:when test="${formularioTienda.formularioPestañaActiva.modoVisualizacionFicha}">
                <input type="hidden" id="idVariable" name="idVar" value="${variable.idVariable}">
                <cmz:itemContenidoLista escape="false">
                  <img src="comun/images/iconos/info.gif" align="absMiddle" border="0" valign="middle" style="vertical-align:top;" alt='<cmz:etiqueta titulo="${variable.descripcion}"/>' title='<cmz:etiqueta titulo="${variable.descripcion}"/>'/>
                </cmz:itemContenidoLista>
                <cmz:itemContenidoLista valor="${variable.idVariable}" ></cmz:itemContenidoLista>
                <cmz:itemContenidoLista alineacion="left" escape="false">
                  <c:choose>
                    <c:when test="${variable.idVariable == 'X_POS.TEXTO_INFORMATIVO_LEGAL'}">
                      <c:set var="valorConcatenado" value="${variable.valor}" />
                      <c:forEach var="varAux" items="${formularioTienda.formularioPestañaActiva.registros}">
                        <c:if test="${varAux.idVariable == 'X_POS.TEXTO_INFORMATIVO_LEGAL_2' or 
                                         varAux.idVariable == 'X_POS.TEXTO_INFORMATIVO_LEGAL_3' or 
                                         varAux.idVariable == 'X_POS.TEXTO_INFORMATIVO_LEGAL_4'}">
                          <c:set var="valorConcatenado" value="${valorConcatenado}${varAux.valor}" />
                        </c:if>
                      </c:forEach>
                      <cmz:campoAreaTexto valor="${valorConcatenado}" id="valor" longitudMaxima="1000" anchura="800px" altura="120px" soloLectura="false" editable="true"/>
                    </c:when>
                    <c:otherwise>
                      <cmz:campoTexto valor="${variable.valor}" id="valor" longitudMaxima="250" anchura="800px" soloLectura="false" editable="true"/>
                    </c:otherwise>
                  </c:choose>
                </cmz:itemContenidoLista>
              </c:when>
              <c:otherwise>
                <c:if test="${variable.valor != null and variable.valor != ''}">
                  <input type="hidden" id="idVar" name="idVar" value="${variable.idVariable}">
                  <cmz:itemContenidoLista escape="false">
                    <img src="comun/images/iconos/info.gif" align="absMiddle" border="0" valign="middle" style="vertical-align:top;" alt='<cmz:etiqueta titulo="${variable.descripcion}"/>' title='<cmz:etiqueta titulo="${variable.descripcion}"/>'/>
                  </cmz:itemContenidoLista>
                  <cmz:itemContenidoLista valor="${variable.idVariable}"></cmz:itemContenidoLista>
                  <cmz:itemContenidoLista alineacion="left" escape="false">
                    <c:choose>
                      <c:when test="${variable.idVariable == 'X_POS.TEXTO_INFORMATIVO_LEGAL'}">
                        <c:set var="valorConcatenado" value="${variable.valor}" />
                        <c:forEach var="varAux" items="${formularioTienda.formularioPestañaActiva.registros}">
                          <c:if test="${varAux.idVariable == 'X_POS.TEXTO_INFORMATIVO_LEGAL_2' or 
                                           varAux.idVariable == 'X_POS.TEXTO_INFORMATIVO_LEGAL_3' or 
                                           varAux.idVariable == 'X_POS.TEXTO_INFORMATIVO_LEGAL_4'}">
                            <c:set var="valorConcatenado" value="${valorConcatenado} ${varAux.valor}" />
                          </c:if>
                        </c:forEach>
                        <cmz:campoAreaTexto valor="${valorConcatenado}" id="valor" longitudMaxima="250" anchura="800px" altura="120px" soloLectura="true" editable="false"/>
                      </c:when>
                      <c:otherwise>
                        <cmz:campoTexto valor="${variable.valor}" id="valor" longitudMaxima="250" anchura="800px" soloLectura="true" editable="false"/>
                      </c:otherwise>
                    </c:choose>
                  </cmz:itemContenidoLista>
                </c:if>
              </c:otherwise>
            </c:choose>
          </c:otherwise>
        </c:choose>
      </cmz:contenidoLista>
    </cmz:lista>
  </cmz:cuerpoPanel>
</cmz:panel>