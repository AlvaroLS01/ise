package com.comerzzia.bricodepot.backoffice.web.facturas.acciones;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.comerzzia.bricodepot.backoffice.persistence.facturas.ParametrosBuscarFacturasBean;
import com.comerzzia.bricodepot.backoffice.services.facturas.BricodepotFacturasService;
import com.comerzzia.core.model.tiposdocumentos.TipoDocumentoBean;
import com.comerzzia.core.servicios.permisos.PermisosEfectivosAccionBean;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.servicios.tipodocumento.ServicioTiposDocumentosImpl;
import com.comerzzia.core.servicios.tipodocumento.TipoDocumentoException;
import com.comerzzia.core.servicios.tipodocumento.TipoDocumentoNotFoundException;
import com.comerzzia.core.util.fechas.FechaException;
import com.comerzzia.core.util.fechas.Fechas;
import com.comerzzia.core.util.numeros.Numero;
import com.comerzzia.core.util.paginacion.PaginaResultados;
import com.comerzzia.web.base.Accion;
import com.comerzzia.web.base.Vista;
import com.comerzzia.web.base.WebKeys;

@SuppressWarnings("deprecation")
public class BuscarAccion extends Accion {

	protected static Logger log = Logger.getLogger(BuscarAccion.class);
	private static final Vista NEXT_PAGE = new Vista("backoffice/facturas/buscar/jsp/buscar.jsp", Vista.INTERNA);

	@Autowired
	protected BricodepotFacturasService servicioFacturas;

	@Override
	public String getNombre() {
		return "buscar";
	}

	@Override
	public Vista ejecutar(HttpServletRequest request) {
		try {
			HttpSession sesion = request.getSession();
			DatosSesionBean datosSesion = (DatosSesionBean) sesion.getAttribute(WebKeys.DATOS_SESION);

			// Comprobamos los permisos de la accion
			PermisosEfectivosAccionBean permisos = datosSesion.getPermisos(this.getAccionMenu());
			if (!permisos.isPuedeEjecutar()) {
				return SIN_PERMISO;
			}
			// Obtenemos los parametros de busqueda
			ParametrosBuscarFacturasBean param = (ParametrosBuscarFacturasBean) sesion.getAttribute("paramBuscarFacturas");

			String operacion = request.getParameter("operacion");

			if (operacion != null) {
				// Consultar
				if (operacion.equals("consultar")) {
					setParametrosBuscar(param, request, datosSesion);
				}
				// Ver página
				else if (operacion.equals("paginar")) {
					setPaginaBuscar(param, request);
				}
				// Ordenar
				else if (operacion.equals("ordenar")) {
					setOrdenBuscar(param, request);

				}

				// Si tenemos pagina, realizamos la busqueda
				if (param.getNumPagina() > 0) {
					PaginaResultados paginaResultados = servicioFacturas.consultarFacturas(param, datosSesion);
					request.setAttribute(WebKeys.PAGINA_RESULTADOS, paginaResultados);
				}
			}

			sesion.setAttribute("paramBuscarFacturas", param);

			return NEXT_PAGE;
		}
		catch (Exception e) {
			log.error("Ha ocurrido un error: " + e, e);
			setError(request, e);
			return ERROR_PAGE;
		}
	}

	protected void setParametrosBuscar(ParametrosBuscarFacturasBean param, HttpServletRequest request, DatosSesionBean datosSesion)
	        throws FechaException, TipoDocumentoNotFoundException, TipoDocumentoException {
		param.setUidActividad(request.getParameter("uidActividad"));
		param.setCodalm(request.getParameter("codalm"));
		param.setCodart(request.getParameter("codart"));
		param.setDesart(request.getParameter("desart"));
		param.setCodcaja(request.getParameter("codcaja"));
		param.setDescli(request.getParameter("descli"));
		param.setUsuario(request.getParameter("usuario"));
		param.setReferenciaCliente(request.getParameter("referenciaCliente"));
		param.setCif(request.getParameter("cif"));

		String codTipoDoc = request.getParameter("codTipoDoc");
		if (StringUtils.isNotBlank(codTipoDoc)) {
			TipoDocumentoBean tipoDoc = ServicioTiposDocumentosImpl.get().consultar(datosSesion, codTipoDoc, datosSesion.getEmpresa().getDatosEmpresa().getCodPais());
			param.setIdTipoDoc(tipoDoc.getIdTipoDocumento());
		}
		else {
			param.setIdTipoDoc(null);
		}

		String importeTotal = request.getParameter("importeTotal");
		if (StringUtils.isNotBlank(importeTotal)) {
			param.setImporteTotal(Numero.desformatea(importeTotal));
		}
		else {
			param.setImporteTotal(null);
		}

		if (!request.getParameter("fechaDesde").isEmpty()) {
			Date fechaDesde = Fechas.getFecha(request.getParameter("fechaDesde"));
			param.setFechaDesde(fechaDesde);
		}
		else {
			param.setFechaDesde(null);
		}

		if (!request.getParameter("fechaHasta").isEmpty()) {
			Date fechaHasta = Fechas.getFecha(request.getParameter("fechaHasta"));
			param.setFechaHasta(fechaHasta);
		}
		else {
			param.setFechaHasta(null);
		}

		param.setNumPagina(1);

		try {

			param.setTamañoPagina(Integer.parseInt(request.getParameter("tamanoPagina")));
		}
		catch (Exception ignore) {
		}
	}

	protected void setPaginaBuscar(ParametrosBuscarFacturasBean param, HttpServletRequest request) {
		try {
			// Obtenemos la pagina solicitada
			int pagina = Integer.parseInt(request.getParameter("pagina"));
			param.setNumPagina(pagina);
		}
		catch (Exception ignore) {

		}
	}

	protected void setOrdenBuscar(ParametrosBuscarFacturasBean param, HttpServletRequest request) {
		try {
			// Establecamos primera pagina
			param.setNumPagina(1);

			// Obtenemos la columna por la que ordenar
			int columna = Integer.parseInt(request.getParameter("columna"));

			// Establecemos la columna de orden
			switch (columna) {
				case 1:
					if (param.getOrden().equals("FECHA")) {
						param.setOrden("FECHA DESC");
					}
					else {
						param.setOrden("FECHA");
					}
					break;
				case 2:
					if (param.getOrden().equals("CODALM")) {
						param.setOrden("CODALM DESC");
					}
					else {
						param.setOrden("CODALM");
					}
					break;
				case 3:
					if (param.getOrden().equals("CODCAJA")) {
						param.setOrden("CODCAJA DESC");
					}
					else {
						param.setOrden("CODCAJA");
					}
					break;
				case 4:
					if (param.getOrden().equals("REFERENCIA_CLIENTE")) {
						param.setOrden("REFERENCIA_CLIENTE DESC");
					}
					else {
						param.setOrden("REFERENCIA_CLIENTE");
					}
					break;
				case 5:
					if (param.getOrden().equals("USUARIO")) {
						param.setOrden("USUARIO DESC");
					}
					else {
						param.setOrden("USUARIO");
					}
					break;
				case 6:
					if (param.getOrden().equals("DESCLI")) {
						param.setOrden("DESCLI DESC");
					}
					else {
						param.setOrden("DESCLI");
					}
					break;
				case 7:
					if (param.getOrden().equals("CIF")) {
						param.setOrden("CIF DESC");
					}
					else {
						param.setOrden("CIF");
					}
					break;
				case 8:
					if (param.getOrden().equals("TOTAL")) {
						param.setOrden("TOTAL DESC");
					}
					else {
						param.setOrden("TOTAL");
					}
					break;
				default:
					param.setOrden("FECHA DESC");

			}
		}
		catch (Exception e) {

		}
	}

}
