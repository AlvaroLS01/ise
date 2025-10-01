<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="cmz" uri="http://comerzzia.com/taglib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<jsp:useBean id="paramBuscarFacturas" type="com.comerzzia.bricodepot.backoffice.persistence.facturas.ParametrosBuscarFacturasBean" scope="session" />
<jsp:useBean id="datosSesion" type="com.comerzzia.core.servicios.sesion.DatosSesionBean" scope="session" />
<jsp:useBean id="permisos" class="com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean" scope="request" />


<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Comerzzia</title>
    <cmz:head/>
    
    <link rel="stylesheet" type="text/css" media="screen,projection,print" href="comun/css/calendario.css" />
    <script type="text/javascript" language="javascript" src="comun/js/comun.js"></script>
    <script type="text/javascript" language="javascript" src="comun/js/mantenimiento.js"></script>
    <script type="text/javascript" language="javascript" src="comun/js/ayudas.js"></script>
	<script type="text/javascript" language="javascript" src="comun/js/validacion.js"></script>
    <script type="text/javascript" language="javascript" src="comun/js/formulario.js"></script>
    <script type="text/javascript" language="javascript" src="comun/js/calendario.js"></script>
    
    <script type="text/javascript">
	    function inicio() {
	        document.getElementById("fechaDesde").focus();
	        pintaCalendario("fechaDesde", "imgFechaDesde");
            pintaCalendario("fechaHasta", "imgFechaHasta");
	    }
	
	    function buscarTiposDocumento(){
			if(document.getElementById("codPais").value != null && document.getElementById("codPais").value != ''){
				buscar("TIPOS_DOCUMENTO_PAIS", "codTipoDocumento", "desTipoDocumento", null, "codPais", null);
			}
		}
	    
	    function imprimir(uidTicket) {
	            document.getElementById("accion").value = "ventas.facturas.facturaA4";
	            document.getElementById("operacion").value = "imprimirRapido";
	            document.getElementById("uidTicket").value = uidTicket;
	            document.getElementById("reportVersion").value = "1";
	    		document.getElementById("frmDatos").target = "_blank";
	    		document.getElementById("frmDatos").submit();
	    		document.getElementById("frmDatos").target = "_self";
	    }
	    
	   	function checkForm() {
	   		if (!esValido("importeTotal", "NUMERICO", false)) {
				return false;
			}
			return true;
	 	}
	    
    </script>
  	</head>

  <body onload="inicio();">
    <cmz:main>
      <cmz:panelCMZ>
      	<cmz:cabeceraPanelCMZ titulo="${permisos.accionMenu.titulo}" icono="${permisos.accionMenu.icono}">
			<cmz:acciones numAccionesVisibles="3">
					<cmz:accion titulo="Ver Permisos" icono="comun/images/iconos/i-key.gif" descripcion="Ver permisos efectivos del usuario" onclick="verPermisos(${permisos.accionMenu.idAccion})" />
					<c:if test="${permisos.puedeAdministrar}">
						<cmz:accion titulo="Administrar Permisos" icono="comun/images/iconos/i-admin_permisos.gif" descripcion="Administración de permisos" onclick="adminPermisos(${permisos.accionMenu.idAccion})" />
					</c:if>
				</cmz:acciones>
			</cmz:cabeceraPanelCMZ>
        <cmz:cuerpoPanelCMZ>
          <cmz:mensaje/>
          <form id="frmDatos" name="frmDatos" action="facturas" method="post">
            <input id="accion" name="accion" type="hidden" value="" />
            <input id="operacion" name="operacion" type="hidden" value="" />
            <input id="columna" name="columna" type="hidden" value="" />
            <input id="pagina" name="pagina" type="hidden" value="" />
            <input id="codPais" name="codPais" type="hidden" value="${datosSesion.empresa.datosEmpresa.codPais}"/>
            <input type="hidden" id="informe" name="informe" value="" /> 
			<input type="hidden" id="idObjeto" name="idObjeto" value='${sessionScope.idObjeto.toString()}' /> 
			<input id="reportVersion" name="reportVersion" type="hidden" value="" />
			<input id="referenciaClientes" name="referenciaClientes" type="hidden" value="" />
			<input type="hidden" id="tipo" name="tipo" value="" />
			<input id="uidTicket" name="uidTicket" type="hidden" value="" />
            
            <cmz:panel>
              <cmz:cabeceraPanel titulo="Búsqueda de Facturas"></cmz:cabeceraPanel>
              <cmz:cuerpoPanel>
                <table cellpadding="0px" cellspacing="0px" border="0px">
                  <tr>
                    <td>
                      <table cellpadding="2px" cellspacing="2px" border="0px">
                        <tr>
                          <td><cmz:etiqueta titulo="Fecha desde"/>:</td>
                      	  <td><cmz:campoFecha id="fechaDesde" mostrarCheckbox="false">
                      	  		<fmt:formatDate pattern="dd/MM/yyyy" value="${paramBuscarFacturas.fechaDesde}"/>
                      	  	  </cmz:campoFecha>	
                      	  </td>
                      	  <td><cmz:botonConsultar onclick="consultar();"/></td>
                        </tr>

                        <tr>
                          <td><cmz:etiqueta titulo="Fecha hasta"/>:</td>
                       	  <td><cmz:campoFecha id="fechaHasta" mostrarCheckbox="false">
                      	  		<fmt:formatDate pattern="dd/MM/yyyy" value="${paramBuscarFacturas.fechaHasta}"/>
                      	  	  </cmz:campoFecha>	
                      	  </td>
                       
                        </tr>
                        
                        <tr>
       					  <td><cmz:etiqueta titulo="Almacén"/>:</td>
						  <td>
					          <cmz:ayuda nombreAyuda="ALMACENES">
					          	<cmz:codigoAyuda idCodigo="codalm" valorCodigo="${paramBuscarFacturas.codalm}" anchuraCodigo="40px" longitudMaximaCodigo="10"></cmz:codigoAyuda>
					          	<cmz:descripcionAyuda idDescripcion="des" valorDescripcion="${paramBuscarFacturas.desalm}" anchuraDescripcion="180px"></cmz:descripcionAyuda>
					          </cmz:ayuda>
						  </td>
	  					</tr>

	  					<tr>
       					  <td><cmz:etiqueta titulo="Tipo de Documento"/>:</td>
						  <td>
					          <cmz:ayuda nombreAyuda="TIPOS_DOCUMENTO_PAIS" campoFiltro="codPais">
					          	<cmz:codigoAyuda idCodigo="codTipoDoc" valorCodigo="${paramBuscarFacturas.codTipoDoc}" anchuraCodigo="40px" longitudMaximaCodigo="4"></cmz:codigoAyuda>
					          	<cmz:descripcionAyuda idDescripcion="desTipoDoc" valorDescripcion="${paramBuscarFacturas.desTipoDoc}" anchuraDescripcion="180px"></cmz:descripcionAyuda>
					          </cmz:ayuda>
						  </td>
	  					</tr>
	  					
	  				    <tr>
                          <td><cmz:etiqueta titulo="Código de Caja"/>:</td>
                          <td><cmz:campoTexto id="codcaja" valor="${paramBuscarFacturas.codcaja}" anchura="80px" longitudMaxima="20"/></td>
                        </tr>
                        
	  				    <tr>
                          <td><cmz:etiqueta titulo="Localizador"/>:</td>
                          <td><cmz:campoTexto id="referenciaCliente" valor="${paramBuscarFacturas.referenciaCliente}" anchura="180px" longitudMaxima="40"/></td>
                        </tr>
                        
	  					<tr>
       					  <td><cmz:etiqueta titulo="Usuario"/>:</td>
						  <td>
					          <cmz:ayuda nombreAyuda="USUARIOS">
					          	<cmz:descripcionAyuda idDescripcion="usuario" valorDescripcion="${paramBuscarFacturas.usuario}" anchuraDescripcion="180px"></cmz:descripcionAyuda>
					          </cmz:ayuda>
						  </td>
	  					</tr>
                        
	  					<tr>
       					  <td><cmz:etiqueta titulo="Cliente"/>:</td>
						  <td>
					          <cmz:ayuda nombreAyuda="CLIENTES">
					          	<cmz:codigoAyuda idCodigo="codcli" valorCodigo="${paramBuscarFacturas.codcli}" anchuraCodigo="40px" longitudMaximaCodigo="4"></cmz:codigoAyuda>
					          	<cmz:descripcionAyuda idDescripcion="descli" valorDescripcion="${paramBuscarFacturas.descli}" anchuraDescripcion="180px"></cmz:descripcionAyuda>
					          </cmz:ayuda>
						  </td>
	  					</tr>
	  					
	  					<tr>
                          <td><cmz:etiqueta titulo="DNI/CIF/NIF"/>:</td>
                          <td><cmz:campoTexto id="cif" valor="${paramBuscarFacturas.cif}" anchura="80px" longitudMaxima="20"/></td>
                        </tr>
                        
                        <tr>
       					  <td><cmz:etiqueta titulo="Artículo"/>:</td>
						  <td>
					          <cmz:ayuda nombreAyuda="ARTICULOS">
					          	<cmz:codigoAyuda idCodigo="codart" valorCodigo="${paramBuscarFacturas.codart}" anchuraCodigo="140px" longitudMaximaCodigo="30"></cmz:codigoAyuda>
					          	<cmz:descripcionAyuda idDescripcion="desart" valorDescripcion="${paramBuscarFacturas.desart}" anchuraDescripcion="180px"></cmz:descripcionAyuda>
					          </cmz:ayuda>
						  </td>
	  					</tr>
                        
                        <tr>
                          <td><cmz:etiqueta titulo="Importe"/>:</td>
                          <td>
                          <cmz:campoTexto id="importeTotal" anchura="80px" longitudMaxima="11">
                          	<cmz:formateaNumero valor="${paramBuscarFacturas.importeTotal}" numDecimales="4"/>
                          </cmz:campoTexto>
                          </td>
                        </tr>
                        
                      </table>
                    </td>
                  </tr>
                </table>
              </cmz:cuerpoPanel>
            </cmz:panel>
            
            <c:choose>
              <c:when test="${paginaResultados.pagina != null}">
                <cmz:panel>
                  <cmz:cabeceraPanelResultados numPorPagina="${paginaResultados.tamañoPagina}"></cmz:cabeceraPanelResultados>
                  <cmz:cuerpoPanel>
                    <cmz:listaPaginada>
                      <cmz:cabeceraListaPaginada ordenActual="${paramBuscarFacturas.orden}">
                        <cmz:itemCabeceraListaPaginada nombre="Fecha" columna="1" ordenColumna="FECHA"></cmz:itemCabeceraListaPaginada>
                        <cmz:itemCabeceraListaPaginada nombre="Cod. Almacén" columna="2" ordenColumna="CODALM"></cmz:itemCabeceraListaPaginada>
                        <cmz:itemCabeceraListaPaginada nombre="Cod. Caja" columna="3" ordenColumna="CODCAJA"></cmz:itemCabeceraListaPaginada>
                        <cmz:itemCabeceraListaPaginada nombre="Factura" columna="4" ordenColumna="REFERENCIA_CLIENTE"></cmz:itemCabeceraListaPaginada>
                        <cmz:itemCabeceraListaPaginada nombre="Cajero" columna="5" ordenColumna="USUARIO"></cmz:itemCabeceraListaPaginada>
                        <cmz:itemCabeceraListaPaginada nombre="Cliente" columna="6" ordenColumna="DESCLI"></cmz:itemCabeceraListaPaginada>
                        <cmz:itemCabeceraListaPaginada nombre="Documento" columna="7" ordenColumna="CIF"></cmz:itemCabeceraListaPaginada>
                        <cmz:itemCabeceraListaPaginada nombre="Importe" columna="8" ordenColumna="TOTAL"></cmz:itemCabeceraListaPaginada>
                        <cmz:itemCabeceraListaPaginada nombre="Accion" columna="9" ordenColumna=""></cmz:itemCabeceraListaPaginada>
                        
                      </cmz:cabeceraListaPaginada>
                      <cmz:contenidoListaPaginada variable="factura" paginaResultados="${paginaResultados}">
                        <cmz:itemContenidoListaPaginada  alineacion="left">
                      		<fmt:formatDate pattern="dd/MM/yyyy" value="${factura.fecha}"/>
                      	</cmz:itemContenidoListaPaginada>
                        <cmz:itemContenidoListaPaginada valor="${factura.codalm}"></cmz:itemContenidoListaPaginada>
                        <cmz:itemContenidoListaPaginada valor="${factura.codcaja}"></cmz:itemContenidoListaPaginada>
                        <cmz:itemContenidoListaPaginada valor="${factura.referenciaCliente}"></cmz:itemContenidoListaPaginada>  
                        <cmz:itemContenidoListaPaginada valor="${factura.usuario}"></cmz:itemContenidoListaPaginada>  
                        <cmz:itemContenidoListaPaginada valor="${factura.descli}"></cmz:itemContenidoListaPaginada>  
                        <cmz:itemContenidoListaPaginada valor="${factura.cif}"></cmz:itemContenidoListaPaginada>  
                        <cmz:itemContenidoListaPaginada valor="${factura.importeTotal}"></cmz:itemContenidoListaPaginada>  
                        <cmz:acciones alineacion="center" idAcciones="">
                          <cmz:accion id="referenciaCliente" icono="comun/images/iconos/pdf.png" onclick="imprimir('${factura.uidTicket}');" descripcion="Imprimir PDF"></cmz:accion>
                        </cmz:acciones>
                      </cmz:contenidoListaPaginada>
                    </cmz:listaPaginada>
                  </cmz:cuerpoPanel>
                </cmz:panel>
              </c:when>
            </c:choose>
          </form>
        </cmz:cuerpoPanelCMZ>
      </cmz:panelCMZ>
    </cmz:main>
  </body>
</html>