package com.comerzzia.bricodepot.backoffice.services.fidelizacion.fidelizados;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.comerzzia.bricodepot.backoffice.persistence.fidelizacion.fidelizados.XFidelizadoMapper;
import com.comerzzia.bricodepot.backoffice.services.fidelizacion.colectivos.BricodepotServicioColectivosImpl;
import com.comerzzia.bricodepot.backoffice.services.variables.CustomVariables;
import com.comerzzia.bricodepot.backoffice.services.ventas.tickets.BricodepotServicioTicketsImpl;
import com.comerzzia.core.model.etiquetas.categorias.EtiquetaBean;
import com.comerzzia.core.omnichannel.engine.model.fidelizacion.contactos.FidelizadoContactoBean;
import com.comerzzia.core.omnichannel.engine.model.fidelizacion.contactos.FidelizadoContactoExample;
import com.comerzzia.core.omnichannel.engine.persistence.fidelizacion.contactos.FidelizadoContactoMapper;
import com.comerzzia.core.servicios.contadores.ContadorException;
import com.comerzzia.core.servicios.etiquetas.EtiquetasConstraintViolationException;
import com.comerzzia.core.servicios.etiquetas.EtiquetasException;
import com.comerzzia.core.servicios.etiquetas.ServicioEtiquetasImpl;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.servicios.sesion.IDatosSesion;
import com.comerzzia.core.servicios.variables.ServicioVariablesImpl;
import com.comerzzia.core.servicios.variables.VariableException;
import com.comerzzia.core.servicios.variables.VariableNotFoundException;
import com.comerzzia.core.servicios.variables.Variables;
import com.comerzzia.core.util.base.Estado;
import com.comerzzia.core.util.criptografia.CriptoException;
import com.comerzzia.core.util.criptografia.CriptoUtil;
import com.comerzzia.core.util.db.Connection;
import com.comerzzia.core.util.i18n.Translation;
import com.comerzzia.core.util.mybatis.session.SqlSession;
import com.comerzzia.core.util.numeros.Numero;
import com.comerzzia.core.util.paginacion.PaginaResultados;
import com.comerzzia.model.fidelizacion.colectivos.ColectivoBean;
import com.comerzzia.model.fidelizacion.cuentas.CuentaBean;
import com.comerzzia.model.fidelizacion.fidelizados.FidelizadoBean;
import com.comerzzia.model.fidelizacion.fidelizados.FidelizadoExample;
import com.comerzzia.model.fidelizacion.fidelizados.FidelizadoExample.Criteria;
import com.comerzzia.model.fidelizacion.fidelizados.FidelizadoKey;
import com.comerzzia.model.fidelizacion.fidelizados.acceso.AccesoFidelizadoBean;
import com.comerzzia.model.fidelizacion.fidelizados.colectivos.ColectivosFidelizadoBean;
import com.comerzzia.model.fidelizacion.fidelizados.contactos.TiposContactoFidelizadoBean;
import com.comerzzia.model.fidelizacion.tarjetas.TarjetaBean;
import com.comerzzia.model.fidelizacion.tarjetas.tipos.TipoTarjetaBean;
import com.comerzzia.persistencia.fidelizacion.fidelizados.FidelizadoMapper;
import com.comerzzia.persistencia.fidelizacion.fidelizados.ParametrosBuscarFidelizadosBean;
import com.comerzzia.servicios.fidelizacion.colectivos.ColectivoNotFoundException;
import com.comerzzia.servicios.fidelizacion.cuentas.Cuenta;
import com.comerzzia.servicios.fidelizacion.cuentas.ServicioCuentasImpl;
import com.comerzzia.servicios.fidelizacion.fidelizados.FidelizadoConstraintViolationException;
import com.comerzzia.servicios.fidelizacion.fidelizados.FidelizadoException;
import com.comerzzia.servicios.fidelizacion.fidelizados.FidelizadoNotFoundException;
import com.comerzzia.servicios.fidelizacion.fidelizados.ServicioFidelizadosImpl;
import com.comerzzia.servicios.fidelizacion.fidelizados.acceso.AccesoFidelizadoNotFoundException;
import com.comerzzia.servicios.fidelizacion.fidelizados.acceso.ServicioAccesoFidelizadoImpl;
import com.comerzzia.servicios.fidelizacion.fidelizados.colectivos.ServicioColectivosFidelizadosImpl;
import com.comerzzia.servicios.fidelizacion.fidelizados.contactos.ServicioTiposContactoFidelizadoImpl;
import com.comerzzia.servicios.fidelizacion.fidelizados.direcciones.ServicioDireccionesFidelizadoImpl;
import com.comerzzia.servicios.fidelizacion.fidelizados.enlaces.ServicioEnlacesImpl;
import com.comerzzia.servicios.fidelizacion.fidelizados.listasdeseos.ListaDeseosNotFoundException;
import com.comerzzia.servicios.fidelizacion.fidelizados.versioning.FidelizadoVersion;
import com.comerzzia.servicios.fidelizacion.tarjetas.ServicioTarjetasImpl;
import com.comerzzia.servicios.fidelizacion.tarjetas.TarjetaNotFoundException;
import com.comerzzia.servicios.fidelizacion.tarjetas.tipos.ServicioTiposTarjetasImpl;
import com.comerzzia.servicios.fidelizacion.tarjetas.tipos.TipoTarjetaNotFoundException;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

@Service
@Primary
public class BricodepotServicioFidelizadosImpl extends ServicioFidelizadosImpl {

	public static final String COLECTIVO_PROFESIONAL = "profesional";

	protected static Logger log = Logger.getLogger(BricodepotServicioFidelizadosImpl.class);

	Translation translation = new Translation(Locale.getDefault());

	public static final String REGEX_MAGENTO_EMAIL = "^((([a-z]|\\d|[!#\\$%&\\u0027\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+(\\.([a-z]|\\d|[!#\\$%&\\u0027\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+)*)|((\\u0022)((((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(([\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x7f]|\\x21|[\\x23-\\x5b]|[\\x5d-\\x7e]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(\\\\([\\x01-\\x09\\x0b\\x0c\\x0d-\\x7f]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF]))))*(((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(\\u0022)))@((([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.)*(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.?$";

	@Override
	public void crear(FidelizadoBean fidelizado, DatosSesionBean datosSesion, Connection conn) throws ContadorException, EtiquetasException, EtiquetasConstraintViolationException, VariableException,
	        VariableNotFoundException, TipoTarjetaNotFoundException, CriptoException, FidelizadoNotFoundException, ListaDeseosNotFoundException {

		log.debug("crear() - Inicio del proceso de creacion de fidelizado");
		/*
		 * En el caso que el fidelizado tenga de nombre "Solicito ticket por mail" siginifica que lo estamos creando por
		 * haber solicitado el envio del ticket por mail
		 */
		if (StringUtils.isNotBlank(fidelizado.getNombre()) && fidelizado.getNombre().equals(BricodepotServicioTicketsImpl.SOLICITO_TICKET_POR_MAIL)) {
			log.debug("crear() - Creacion de fidelizado por envio de ticket por correo");
		}
		else {
			evitaDuplicacionFidelizadoDocumentoEmail(fidelizado, datosSesion);
		}

		org.apache.ibatis.session.SqlSession sqlSession = datosSesion.getSqlSessionFactory().openSession(conn);
		crearFidelizado(fidelizado, datosSesion, conn);

		ServicioDireccionesFidelizadoImpl.get().salvarDirecciones(conn, datosSesion, fidelizado);
		salvarTarjetas(conn, datosSesion, fidelizado);
		salvarPersonasRelacionadas(conn, datosSesion, fidelizado);
		salvarColectivos(conn, datosSesion, fidelizado);
		ServicioTiposContactoFidelizadoImpl.get().salvar(conn, datosSesion, fidelizado);
		salvarEnlace(conn, datosSesion, fidelizado);
		salvarEtiquetasCategorias(conn, datosSesion, fidelizado);
		salvarTiendaFavorita(conn, datosSesion, fidelizado);
		salvarListaDeseos(conn, datosSesion, fidelizado);
		String altaAutoUsuario = ServicioVariablesImpl.get().consultarValor(datosSesion, sqlSession, Variables.FIDELIZACION_ALTA_AUTOMATICA_USUARIO);
		if ("S".equals(altaAutoUsuario) && fidelizado.getTipoContacto("EMAIL") != null) {
			String email = fidelizado.getTipoContacto("EMAIL").getValor();
			try {
				ServicioAccesoFidelizadoImpl.get().consultarPorEmail(sqlSession, datosSesion, email);
			}
			catch (AccesoFidelizadoNotFoundException e) {
				AccesoFidelizadoBean acc = new AccesoFidelizadoBean();
				String clave = crearClaveAleatoria(6);
				acc.setClave(CriptoUtil.cifrar(CriptoUtil.ALGORITMO_MD5, "czz.2018".getBytes()));
				acc.setEmail(email);
				acc.setIdFidelizado(fidelizado.getIdFidelizado());
				acc.setUsuario(email.replace("@", "").replace("_", ""));
				ServicioAccesoFidelizadoImpl.get().crear(acc, datosSesion, conn);
				// LUSTRUM-137086: Se evita la generación del correo NUEVA_ALTA_FIDELIZADO que
				// no se debe enviar, para evitar que bloquee la cola de correo
				// ServicioAccesoFidelizadoImpl.get().sendNotificacionNuevoUsuario(acc, datosSesion, sqlSession, true);
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public FidelizadoBean crearFidelizadoRest(FidelizadoBean fidelizado, DatosSesionBean datosSesion, Connection conn)
	        throws ContadorException, CriptoException, VariableException, VariableNotFoundException, TipoTarjetaNotFoundException, EtiquetasException, EtiquetasConstraintViolationException {

		evitaDuplicacionFidelizadoDocumentoEmail(fidelizado, datosSesion); // BRICO-266

		org.apache.ibatis.session.SqlSession sqlSession = datosSesion.getSqlSessionFactory().openSession(conn);

		crearFidelizado(fidelizado, datosSesion, conn);

		// Creamos los contactos del fidelizado
		if (fidelizado.getTiposContacto() != null) {
			for (TiposContactoFidelizadoBean tipoContacto : fidelizado.getTiposContacto()) {
				tipoContacto.setIdFidelizado(fidelizado.getIdFidelizado());
				ServicioTiposContactoFidelizadoImpl.get().crear(tipoContacto, datosSesion, conn);
			}
		}

		// Creamos el enlace del fidelizado
		if (fidelizado.getEnlace() != null) {
			fidelizado.getEnlace().setIdFidelizado(fidelizado.getIdFidelizado());
			ServicioEnlacesImpl.get().crear(conn, fidelizado.getEnlace(), datosSesion);
		}

		String altaAutoUsuario = ServicioVariablesImpl.get().consultarValor(datosSesion, sqlSession, Variables.FIDELIZACION_ALTA_AUTOMATICA_USUARIO);
		// Creamos el acceso del fidelizado.
		if (fidelizado.getAcceso() != null) {
			fidelizado.getAcceso().setIdFidelizado(fidelizado.getIdFidelizado());
		}
		else if ("S".equals(altaAutoUsuario) && fidelizado.getTipoContacto("EMAIL") != null) {
			String email = fidelizado.getTipoContacto("EMAIL").getValor();
			try {
				ServicioAccesoFidelizadoImpl.get().consultarPorEmail(sqlSession, datosSesion, email);
			}
			catch (AccesoFidelizadoNotFoundException e) {
				AccesoFidelizadoBean acc = new AccesoFidelizadoBean();
//				String clave = crearClaveAleatoria(6);
				acc.setClave(CriptoUtil.cifrar(CriptoUtil.ALGORITMO_MD5, "czz.2018".getBytes()));
				acc.setEmail(email);
				acc.setIdFidelizado(fidelizado.getIdFidelizado());
				acc.setUsuario(email.replace("@", "").replace("_", ""));
				fidelizado.setAcceso(acc);
			}

		}
		if (fidelizado.getAcceso() != null) {
			ServicioAccesoFidelizadoImpl.get().crear(fidelizado.getAcceso(), datosSesion, conn);
		}
		// Creamos los colectivos del fidelizado
		for (ColectivosFidelizadoBean colectivo : fidelizado.getColectivos()) {
			colectivo.setIdFidelizado(fidelizado.getIdFidelizado());
			ServicioColectivosFidelizadosImpl.get().crear(conn, colectivo, datosSesion);

			/* BRICO-146 */
			ColectivoBean customColectivo;
			try {
				customColectivo = BricodepotServicioColectivosImpl.get().consultar(colectivo.getCodColectivo(), datosSesion);
			}
			catch (ColectivoNotFoundException e) {
				String msg = "No se ha encontrado colectivo con codColectivo:[" + colectivo.getCodColectivo() + "]: " + e.getMessage();
				log.error("crearFidelizadoRest() - " + msg, e);
				throw new VariableException(msg, e);
			}
			if (customColectivo.getExtension(COLECTIVO_PROFESIONAL) != null && (Boolean) customColectivo.getExtension(COLECTIVO_PROFESIONAL)) {

				log.debug("crearFidelizadoRest() - Se añade colectivo profesional al fidelizado, ya que codColectivo:[" + colectivo.getCodColectivo() + "] tiene marcado \"profesional\"");
				String codColectivoProfesional = ServicioVariablesImpl.get().consultarValor(datosSesion, sqlSession, CustomVariables.FIDELIZACION_COD_COLECTIVO_PROFESIONAL);
				ColectivoBean colectivoProfesional;
				try {
					colectivoProfesional = BricodepotServicioColectivosImpl.get().consultar(codColectivoProfesional, datosSesion);
				}
				catch (ColectivoNotFoundException e) {
					log.error("crearFidelizadoRest() - No existe el colectivo profesional con codColectivo:[" + codColectivoProfesional
					        + "], no se le asignará colectivo profesional al fidelizado con id:[" + fidelizado.getIdFidelizado() + "]");
					continue;
				}
				ColectivosFidelizadoBean colectivoFidelizadoProfesional = new ColectivosFidelizadoBean();
				colectivoFidelizadoProfesional.setIdFidelizado(fidelizado.getIdFidelizado());
				colectivoFidelizadoProfesional.setCodColectivo(colectivoProfesional.getCodColectivo());

				ServicioColectivosFidelizadosImpl.get().crear(conn, colectivoFidelizadoProfesional, datosSesion);

			}
			/* fin BRICO-146 */
		}
		// Creamos las etiquetas del fidelizado
		for (EtiquetaBean etiqueta : fidelizado.getEtiquetasCategorias()) {
			etiqueta.setIdClaseEtiquetaEnlazada("F_FIDELIZADOS_TBL.ID_FIDELIZADO");
			etiqueta.setIdObjetoEtiquetaEnlazada(fidelizado.getIdFidelizado().toString());
			etiqueta.setUidEtiquetaEnlazada(etiqueta.getUidEtiqueta());
			ServicioEtiquetasImpl.get().crearEtiquetaEnlace(etiqueta, datosSesion, conn);
		}
		// Salvamos la tarjeta del fidelizado
		String codtipotarj = ServicioVariablesImpl.get().consultarValor(datosSesion, sqlSession, Variables.FIDELIZACION_TIPO_TARJETA_ALTA_AUTOMATICA);
		if (fidelizado.getTarjetas() != null && !fidelizado.getTarjetas().isEmpty()) {
			for (TarjetaBean tarjeta : fidelizado.getTarjetas()) {
				try {
					TarjetaBean tarjetaFidelizado = ServicioTarjetasImpl.get().consultarTarjetaPorNumero(conn, tarjeta.getNumeroTarjeta(), datosSesion);
					tarjetaFidelizado.setIdFidelizado(fidelizado.getIdFidelizado());
					ServicioTarjetasImpl.get().modificar(conn, tarjetaFidelizado, datosSesion);
				}
				catch (TarjetaNotFoundException e) {
					tarjeta.setIdFidelizado(fidelizado.getIdFidelizado());
					tarjeta.setFechaEmision(new Date());
					List<TipoTarjetaBean> tiposTarjeta = ServicioTiposTarjetasImpl.get().consultarTiposVinculablesYSinPago(sqlSession, datosSesion);
					String codtipoTarj = null;
					for (TipoTarjetaBean tipoTarjeta : tiposTarjeta) {
						String prefijo = tipoTarjeta.getPrefijo();
						if (prefijo != null && tarjeta.getNumeroTarjeta().startsWith(prefijo)) {
							codtipoTarj = tipoTarjeta.getCodtipotarj();
							break;
						}
					}
					if (codtipoTarj == null && tiposTarjeta != null && !tiposTarjeta.isEmpty()) {
						codtipoTarj = tiposTarjeta.get(0).getCodtipotarj();
					}

					tarjeta.setCodTipoTarj(codtipoTarj);
					ServicioTarjetasImpl.get().crear(conn, tarjeta, datosSesion);
				}
			}
		}
		else if (codtipotarj != null) {
			TipoTarjetaBean tipoTarjeta = ServicioTiposTarjetasImpl.get().consultar(sqlSession, codtipotarj, datosSesion);
			Cuenta cuenta = ServicioCuentasImpl.get().crear((DatosSesionBean) datosSesion, conn, 0.0, 0.0);
			TarjetaBean tarjeta = new TarjetaBean();
			String numeroTarjeta = getNumeroTarjeta(conn, tipoTarjeta.getLongitudTotal(), tipoTarjeta.getPrefijo(), datosSesion);
			while (numeroTarjeta == null) {
				numeroTarjeta = getNumeroTarjeta(conn, tipoTarjeta.getLongitudTotal(), tipoTarjeta.getPrefijo(), datosSesion);
			}
			tarjeta.setNumeroTarjeta(numeroTarjeta);
			tarjeta.setFechaEmision(new Date());
			tarjeta.setCodTipoTarj(codtipotarj);
			tarjeta.setIdFidelizado(fidelizado.getIdFidelizado());
			// Le asignamos la cuenta a la tarjeta y creamos la misma
			tarjeta.setIdCuentaTarjeta(cuenta.getIdCuentaTarjeta());
			tarjeta.setFechaBaja(null);
			tarjeta.setFechaAsignacionFidelizado(new Date());
			ServicioTarjetasImpl.get().crear(conn, tarjeta, (DatosSesionBean) datosSesion);
		}

		FidelizadoVersion fidelizadoVersion = new FidelizadoVersion(conn, fidelizado.getIdFidelizado());
		fidVersionControlService.checkFidelizadosVersion(datosSesion, fidelizadoVersion);

		return fidelizado;
	}
	
	private void evitaDuplicacionFidelizadoDocumentoEmail(FidelizadoBean fidelizado, DatosSesionBean datosSesion) { // BRICO-266
		log.debug("evitaDuplicacionFidelizadoDocumentoEmail()");
		
		comprobarDni(fidelizado, datosSesion);
		comprobarEmail(fidelizado, datosSesion);
	}

	@SuppressWarnings("deprecation")
	private void comprobarDni(FidelizadoBean fidelizado, DatosSesionBean datosSesion) throws FidelizadoConstraintViolationException {
		String documento = fidelizado.getDocumento();
		log.debug("comprobarDni() - Comprobando documento: " + documento);
		
		if (StringUtils.isBlank(documento)) {
			String msg = "Documento vacío o nulo";
			log.error("comprobarDni() - " + msg);
			throw new FidelizadoConstraintViolationException(msg);
		}

		org.apache.ibatis.session.SqlSession sqlSession = datosSesion.getSqlSessionFactory().openSession();

		try {
			FidelizadoMapper fidelizadoMapper = sqlSession.getMapper(FidelizadoMapper.class);
			FidelizadoExample fidelizadoExample = new FidelizadoExample();
			FidelizadoExample.Criteria criteria = fidelizadoExample.or().andUidInstanciaEqualTo(datosSesion.getUidInstancia()).andDocumentoEqualTo(documento);
			if (fidelizado.getIdFidelizado() != null) {
				criteria.andIdFidelizadoNotEqualTo(fidelizado.getIdFidelizado());
			}

			List<FidelizadoBean> fidelizados = fidelizadoMapper.selectFromViewByExample(fidelizadoExample);
			if (!fidelizados.isEmpty()) {
				FidelizadoBean fidelizadoPorDocumento = fidelizados.get(0);
				String mensaje = translation.getText("Ya existe un fidelizado con el documento {0}. Id del fidelizado:{1}", documento, String.valueOf(fidelizadoPorDocumento.getIdFidelizado()));
				log.error("comprobarDni() - " + mensaje);
				throw new FidelizadoConstraintViolationException(mensaje);
			}
		}
		catch (FidelizadoConstraintViolationException e) {
			throw e;
		}
		catch (Exception e) {
			String msg = "Ha ocurrido un error comprobando si existe más de un fidelizado para el mismo documento: " + e.getMessage();
			log.error("comprobarDni() " + msg, e);
			throw new FidelizadoConstraintViolationException(msg, e);
		}
		finally {
			sqlSession.close();
		}

	}

	private void comprobarEmail(FidelizadoBean fidelizado, DatosSesionBean datosSesion) throws FidelizadoConstraintViolationException {
		String email = null;
		if (fidelizado.getTipoContacto("EMAIL") != null) {
			email = fidelizado.getTipoContacto("EMAIL").getValor();
		}
		
		log.debug("comprobarEmail() - Comprobando email: " + email);

		if (StringUtils.isBlank(email)) {
			String msg = "Email vacío o nulo";
			log.debug("comprobarEmail() - " + msg);
			throw new FidelizadoConstraintViolationException(msg);
		}

		if (!Pattern.compile(REGEX_MAGENTO_EMAIL, Pattern.CASE_INSENSITIVE).matcher(email).matches()) {
			String msg = "El formato del mail es incorrecto";
			log.debug("comprobarEmail() - " + msg);
			throw new FidelizadoConstraintViolationException(msg);
		}
		
		List<FidelizadoContactoBean> fidelizadosPorEmailCaseInSensitive = buscaFidelizadosPorEmailCaseInSensitive(datosSesion, email);
		if (!fidelizadosPorEmailCaseInSensitive.isEmpty()) {
			String mensaje = translation.getText("Ya existe un fidelizado con el email {0}", email);
			log.error("comprobarDni() - " + mensaje);
			throw new FidelizadoConstraintViolationException(mensaje);
		}
	}

	@SuppressWarnings("deprecation")
	public List<FidelizadoContactoBean> buscaFidelizadosPorEmailCaseInSensitive(DatosSesionBean datosSesion, String email) {
		log.debug("buscaFidelizadosPorEmailCaseInSensitive() - Buscando fidelizados por email: " + email + " sin tener en cuenta mayúsculas y minúsculas");

		SqlSession sqlSession = new SqlSession();

		try {
			sqlSession.openSession(datosSesion.getSqlSessionFactory().openSession());

			FidelizadoContactoMapper fidelizadoContactoMapper = sqlSession.getMapper(FidelizadoContactoMapper.class);

			// obtiene todos los fidelizados por email case insensitive
			FidelizadoContactoExample fidelizadoContactoExample = new FidelizadoContactoExample();
			fidelizadoContactoExample.or().andUidInstanciaEqualTo(datosSesion.getUidInstancia()).andCodTipoConEqualTo("EMAIL").andValorLikeInsensitive(email);
			List<FidelizadoContactoBean> fidelizadosPorEmailCaseInsensitive = fidelizadoContactoMapper.selectByExample(fidelizadoContactoExample);
			return fidelizadosPorEmailCaseInsensitive;
			
		}
		catch (Exception e) {
			log.error("buscaFidelizadosPorEmailCaseInSensitive() - Error buscando fidelizados por email: " + email + " sin tener en cuenta mayúsculas y minúsculas:" + e.getMessage(), e);
			throw new FidelizadoException();
		}
		finally {
			sqlSession.close();
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void salvarColectivos(Connection conn, DatosSesionBean datosSesion, FidelizadoBean fidelizado) {
		log.debug("salvarColectivos() - Inicio del guardado de colectivos");
		for (ColectivosFidelizadoBean colectivo : fidelizado.getColectivos()) {
			colectivo.setIdFidelizado(fidelizado.getIdFidelizado());
			ServicioColectivosFidelizadosImpl.get().salvar(conn, colectivo, datosSesion);

			ColectivoBean customColectivo;
			try {
				customColectivo = BricodepotServicioColectivosImpl.get().consultar(colectivo.getCodColectivo(), datosSesion);

				if (colectivo.getEstadoBean() == Estado.NUEVO && customColectivo.getExtension(COLECTIVO_PROFESIONAL) != null && (Boolean) customColectivo.getExtension(COLECTIVO_PROFESIONAL)) {
					log.debug("salvarColectivos() - Se añade colectivo profesional al fidelizado, ya que codColectivo:[" + colectivo.getCodColectivo() + "] tiene marcado \"profesional\"");
					String codColectivoProfesional = ServicioVariablesImpl.get().consultarValor(conn, datosSesion, CustomVariables.FIDELIZACION_COD_COLECTIVO_PROFESIONAL);
					ColectivoBean colectivoProfesional;
					try {
						colectivoProfesional = BricodepotServicioColectivosImpl.get().consultar(codColectivoProfesional, datosSesion);
					}
					catch (ColectivoNotFoundException e) {
						log.error("salvarColectivos() - No existe el colectivo profesional con codColectivo:[" + codColectivoProfesional
						        + "], no se le asignará colectivo profesional al fidelizado con id:[" + fidelizado.getIdFidelizado() + "]");
						continue;
					}
					ColectivosFidelizadoBean colectivoFidelizadoProfesional = new ColectivosFidelizadoBean();
					colectivoFidelizadoProfesional.setIdFidelizado(fidelizado.getIdFidelizado());
					colectivoFidelizadoProfesional.setCodColectivo(colectivoProfesional.getCodColectivo());

					ServicioColectivosFidelizadosImpl.get().crear(conn, colectivoFidelizadoProfesional, datosSesion);
				}
			}
			catch (Exception e) {
				log.error("salvarColectivos() - No se ha encontrado colectivo con codColectivo:[" + colectivo.getCodColectivo() + "]: " + e.getMessage(), e);
			}
		}

	}
	
	
	@SuppressWarnings("deprecation")
	@Override
	public List<FidelizadoBean> consultarFidelizadosDatosPorFiltro(DatosSesionBean datosSesion, FidelizadoBean fidelizado) {
		SqlSession sqlSession = new SqlSession();
		List<FidelizadoBean> lstFidelizados = new ArrayList<FidelizadoBean>();
		try {
			log.debug("consultarFidelizadosDatosPorFiltro() - Consultando de fidelizados");
			sqlSession.openSession(datosSesion.getSqlSessionFactory().openSession());
			XFidelizadoMapper mapper = sqlSession.getMapper(XFidelizadoMapper.class);
			FidelizadoExample example = new FidelizadoExample();
			Criteria criteria = example.or().andUidInstanciaEqualToFidelizados(datosSesion.getUidInstancia());

			if (fidelizado.getNumeroTarjeta() != null && !fidelizado.getNumeroTarjeta().isEmpty()) {
				criteria.andNumeroTarjetaEqualTo(fidelizado.getNumeroTarjeta());
			}
			if (fidelizado.getDocumento() != null && !fidelizado.getDocumento().isEmpty()) {
				criteria.andUpperDocumentoEqualTo(fidelizado.getDocumento());
			}
			if (fidelizado.getNombre() != null && !fidelizado.getNombre().isEmpty()) {
				criteria.andUpperNombreLike("%" + fidelizado.getNombre() + "%");
			}
			if (fidelizado.getApellidos() != null && !fidelizado.getApellidos().isEmpty()) {
				criteria.andUpperApellidosLike("%" + fidelizado.getApellidos() + "%");
			}
			if (fidelizado.getActivoFiltro() != null) {
				criteria.andActivoEqualToFidelizados(fidelizado.getActivoFiltro());
			}
			else {
				criteria.andActivoEqualToFidelizados(true);
			}
			String emailValue = null;
			String phoneValue = null;
			TiposContactoFidelizadoBean email = fidelizado.getTipoContacto("EMAIL");
			if (email != null && StringUtils.isNotBlank(email.getValor())) {
				emailValue = email.getValor().toUpperCase();
			}
			TiposContactoFidelizadoBean phone = fidelizado.getTipoContacto("TELEFONO1");
			if (phone != null && StringUtils.isNotBlank(phone.getValor())) {
				phoneValue = phone.getValor();
			}
			criteria.andCardIsActive();
			lstFidelizados = mapper.selectByExampleFidelizadoDatos(datosSesion.getUidActividad(), example, fidelizado.getCodColectivos(), fidelizado.getEtiquetas(), emailValue, phoneValue);

			return lstFidelizados;

		}
		catch (Exception e) {
			log.error("consultarFidelizadosDatosPorFiltro() - " + e.getMessage(), e);
		}
		finally {
			sqlSession.close();
		}
		return lstFidelizados;
	}
	
	@Override
	public void salvar(FidelizadoBean fidelizado, DatosSesionBean datosSesion)
	        throws VariableException, VariableNotFoundException, TipoTarjetaNotFoundException, CriptoException, FidelizadoNotFoundException, ListaDeseosNotFoundException {

		/* Creamos el fidelizado SIEMPRE con el documento en MAYUSCULAS */
		if(fidelizado.getDocumento() != null) {
			fidelizado.setDocumento(fidelizado.getDocumento().toUpperCase());
		}
		
		/* Creamos el fidelizado SIEMPRE con el EMAIL en MAYUSCULAS */
		if (fidelizado.getTipoContacto("EMAIL") != null && fidelizado.getTipoContacto("EMAIL").getValor() != null) {
			fidelizado.getTipoContacto("EMAIL").setValor(fidelizado.getTipoContacto("EMAIL").getValor().toUpperCase());
		}
		
		super.salvar(fidelizado, datosSesion);
	}
	
	@Override
	public PaginaResultados consultar(org.apache.ibatis.session.SqlSession sqlSession, ParametrosBuscarFidelizadosBean param, DatosSesionBean datosSesion) { // BRICO-459

		// INICIO STANDARD
		log.debug("consultar() - Consultando usuarios");

		FidelizadoMapper mapper = sqlSession.getMapper(FidelizadoMapper.class);
		FidelizadoExample example = new FidelizadoExample();
		Criteria criteria = example.or();

		// Si se filtra por número de tarjeta, no tener en cuenta el resto de
		// parámetros porque el número de tarjeta es único
		if (param.getNumeroTarjeta() != null && !param.getNumeroTarjeta().isEmpty()) {
			// NUMERO_TARJETA
			criteria.andUidInstanciaEqualToFidelizados(datosSesion.getConfigEmpresa().getUidInstancia());
			criteria.andNumeroTarjetaEqualTo(param.getNumeroTarjeta());
		}
		/*
		 * If the query is being filtered by user email the rest of the params are not evaluated
		 * because the user email is unique
		 */
		else if(StringUtils.isNotBlank(param.getUserEmail())) {
			//USER EMAIL
			criteria.andUidInstanciaEqualToFidelizados(datosSesion.getUidInstancia());
			criteria.andUserEmailEqualTo(param.getUserEmail());
		}
		else {
			if(StringUtils.isNotBlank(param.getContact())) {				
				criteria.andUidInstanciaEqualToFidelizados(datosSesion.getConfigEmpresa().getUidInstancia());
			}else {				
				criteria.andUidInstanciaEqualTo(datosSesion.getConfigEmpresa().getUidInstancia());
			}
			
			// NOMBRE
			if (StringUtils.isNotBlank(param.getNombre())) {
				criteria.andNombreLikeInsensitive("%" + param.getNombre() + "%");
			}

			// APELLIDOS
			if (StringUtils.isNotBlank(param.getApellidos())) {
				criteria.andApellidosLikeInsensitive("%" + param.getApellidos() + "%");
			}

			// DOCUMENTO
			if (StringUtils.isNotBlank(param.getDocumento())) {
				criteria.andDocumentoLikeInsensitive(param.getDocumento());
			}

			// CODFIDELIZADO
			if (StringUtils.isNotBlank(param.getCodFidelizado())) {
				criteria.andCodFidelizadoLikeInsensitive(param.getCodFidelizado() + "%");
			}
			
			//Contact
			if(StringUtils.isNotBlank(param.getContact())) {
				criteria.andContactEqualTo(param.getContact());
				if(StringUtils.isNotBlank(param.getContactType())) {
					criteria.andContactTypeEqualTo(param.getContactType());
				}
			}
			// ACTIVO
			if (StringUtils.isNotBlank(param.getActivo())) {
				Boolean activo = Boolean.FALSE;
				if("S".equals(param.getActivo())){
					activo = Boolean.TRUE;
				}
				if(StringUtils.isNotBlank(param.getContact())){
					criteria.andActivoEqualToFidelizados(activo);
				}else{
					criteria.andActivoEqualTo(activo);
				}

			}
			
			
		}

		example.setOrderByClause(param.getOrden());
		if (param.getNumPagina() == 0) {
			param.setNumPagina(1);
		}

		List<FidelizadoBean> resultados = new ArrayList<FidelizadoBean>(param.getTamañoPagina());
		PaginaResultados paginaResultados = new PaginaResultados(param, resultados);

		List<FidelizadoBean> fidelizados = new ArrayList<FidelizadoBean>();
		if (StringUtils.isNotBlank(param.getNumeroTarjeta()) ) {
			fidelizados = mapper.selectFromViewByExampleTarjeta(example);
			processResults(resultados, paginaResultados, fidelizados);
			paginaResultados.setTotalResultados(fidelizados.size());
		}else if (StringUtils.isNotBlank(param.getUserEmail()) ) {
			fidelizados = mapper.selectByUserEmail(example);
			processResults(resultados, paginaResultados, fidelizados);
			paginaResultados.setTotalResultados(fidelizados.size());
		}else if (StringUtils.isNotBlank(param.getContact()) ) {
			example.setDistinct(true);
			fidelizados = mapper.selectByExampleContact(example);
			// BRICO-459 ------------------------
			addFidelizadosPorEmailCaseInsensitive(param, datosSesion, mapper, fidelizados);
			// BRICO-459 ------------------------
			processResults(resultados, paginaResultados, fidelizados);
			paginaResultados.setTotalResultados(fidelizados.size());
		}else {
			int offset = (paginaResultados.getInicio() - 1);
			int limit = paginaResultados.getTamañoPagina();

			RowBounds rowBounds = new RowBounds(offset, limit);
			fidelizados = mapper.selectFromViewByExampleWithRowbounds(example, rowBounds);
			resultados.addAll(fidelizados);
			paginaResultados.setTotalResultados(mapper.countByExample(example));
		}

		return paginaResultados;
		// FIN STANDARD

	}

	private void addFidelizadosPorEmailCaseInsensitive(ParametrosBuscarFidelizadosBean param, DatosSesionBean datosSesion, FidelizadoMapper mapper, List<FidelizadoBean> fidelizados) { // BRICO-459

		if (param.getContactType() == null || !param.getContactType().equals("EMAIL") || param.getContact() == null) {
			return;
		}

		log.debug("addFidelizadosPorEmailCaseInsensitive() - Añadiendo a la búsqueda de fidelizados aquellos con email: " + param.getContact() + " sin tener en cuenta mayúsculas y minúsculas");

		List<FidelizadoContactoBean> fidelizadosPorEmailCaseInSensitive = buscaFidelizadosPorEmailCaseInSensitive(datosSesion, param.getContact());

		for (FidelizadoContactoBean fidelizadoContactoBean : fidelizadosPorEmailCaseInSensitive) {
			boolean existe = false;
			for (FidelizadoBean fidelizadoBean : fidelizados) {
				if (fidelizadoBean.getIdFidelizado().equals(fidelizadoContactoBean.getIdFidelizado())) {
					existe = true;
				}
			}
			
			if (!existe) {
				FidelizadoKey fidelizadoKey = new FidelizadoKey();
				fidelizadoKey.setActivo(true);
				fidelizadoKey.setIdFidelizado(fidelizadoContactoBean.getIdFidelizado());
				fidelizadoKey.setUidInstancia(fidelizadoContactoBean.getUidInstancia());
				FidelizadoBean fidelizado = mapper.selectByPrimaryKey(fidelizadoKey);

				fidelizados.add(fidelizado);
			}
		}
	}
	
	@Override
	protected void inicializarFidelizado(FidelizadoBean fidelizado, Sheet sheet, int i, Translation translation, List<String> mensajesProcesamientoFidelizado, IDatosSesion datosSesion) { // BRICO-469
		super.inicializarFidelizado(fidelizado, sheet, i, translation, mensajesProcesamientoFidelizado, datosSesion);
		
		// Crea fidelizado con documento en mayúsculas
		if(!fidelizado.getDocumento().isEmpty()) {
			fidelizado.setDocumento(fidelizado.getDocumento().toUpperCase());
		}
		
		// Crea fidelizado con email en mayúsculas
		if(fidelizado.getTipoContacto("EMAIL") != null && !fidelizado.getTipoContacto("EMAIL").getValor().isEmpty()) {
			fidelizado.getTipoContacto("EMAIL").setValor(fidelizado.getTipoContacto("EMAIL").getValor().toUpperCase());
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public Map<FidelizadoBean, List<String>> importarFidelizados(byte[] fichero, Translation translation, boolean editarCoincidencias, IDatosSesion datosSesion) throws BiffException, IOException {
		Map<FidelizadoBean, List<String>> resumen = new HashMap<FidelizadoBean, List<String>>();

		org.apache.ibatis.session.SqlSession sqlSession = datosSesion.getSqlSessionFactory().openSession();
		Connection conn = new Connection(sqlSession.getConnection());
		try {
			FidelizadoMapper fidelizadoMapper = sqlSession.getMapper(FidelizadoMapper.class);
			FidelizadoContactoMapper fidelizadoContactoMapper = sqlSession.getMapper(FidelizadoContactoMapper.class);

			InputStream input = new ByteArrayInputStream(fichero);
			WorkbookSettings settings = new WorkbookSettings();
			settings.setEncoding("Cp1252");
			Workbook work = Workbook.getWorkbook(input, settings);
			Sheet sheet = work.getSheet(0);
			int numRows = sheet.getRows();
			for (int i = 1; i < numRows; i++) {
				if (!isEmptyRow(sheet, i)) {
					List<String> mensajesProcesamientoFidelizado = new ArrayList<String>();

					Long idFidelizado = Numero.desformateaLong(sheet.getCell(0, i).getContents(), null);
					String documento = sheet.getCell(10, i).getContents();
					String email = sheet.getCell(37, i).getContents();

					FidelizadoBean fidelizado = null;
					FidelizadoBean fidCod = null;
					// Si se ha establecido identificador en la plantilla,
					// primero se busca un fidelizado cuyo identificador
					// coincida
					if (idFidelizado != null) {
						FidelizadoKey key = new FidelizadoKey();
						key.setIdFidelizado(idFidelizado);
						key.setUidInstancia(datosSesion.getUidInstancia());
						fidelizado = fidelizadoMapper.selectFromViewByPrimaryKey(key);
						fidCod= fidelizado;

						// En caso de no encontrarse fidelizado con el
						// identificador, se usa para buscarlo por código
						if (fidelizado == null) {
							FidelizadoExample fidelizadoExample = new FidelizadoExample();
							fidelizadoExample.or().andCodFidelizadoEqualTo(idFidelizado.toString()).andUidInstanciaEqualTo(datosSesion.getUidInstancia());
							List<FidelizadoBean> fidelizados = fidelizadoMapper.selectFromViewByExample(fidelizadoExample);

							if (!fidelizados.isEmpty()) {
								fidelizado = fidelizados.get(0);
								fidCod= fidelizado;
								mensajesProcesamientoFidelizado.add(translation._("Ya existe un fidelizado con el código {0}", idFidelizado.toString()));									
							}
						}
						else {
							mensajesProcesamientoFidelizado.add(translation._("Ya existe un fidelizado con el identificador {0}", idFidelizado));
						}
					}
					// Si no se encuentra ni por identificador ni por código, se
					// busca por documento.
					if (fidelizado == null && (documento != null && !documento.isEmpty())) {
						FidelizadoExample fidelizadoExample = new FidelizadoExample();
						fidelizadoExample.or().andDocumentoLikeInsensitive(documento).andUidInstanciaEqualTo(datosSesion.getUidInstancia());
						List<FidelizadoBean> fidelizados = fidelizadoMapper.selectFromViewByExample(fidelizadoExample);

						if (!fidelizados.isEmpty()) {
							fidelizado = fidelizados.get(0);
							fidCod = null;
							mensajesProcesamientoFidelizado.add(translation._("Ya existe un fidelizado con el documento {0}", documento));
						}
					}
					// Si aún no se encuentra el fidelizado, buscamos si existe
					// alguno que tenga como EMAIL de contacto el establecido en
					// la plantilla
					if (fidelizado == null) {
						FidelizadoContactoExample fidelizadoContactoExample = new FidelizadoContactoExample();
						fidelizadoContactoExample.or().andUidInstanciaEqualTo(datosSesion.getUidInstancia()).andCodTipoConEqualTo("EMAIL").andValorLikeInsensitive(email);
						List<FidelizadoContactoBean> listFidelizadoContacto = fidelizadoContactoMapper.selectByExample(fidelizadoContactoExample);
						if (!listFidelizadoContacto.isEmpty()) {
							FidelizadoKey key = new FidelizadoKey();
							key.setIdFidelizado(listFidelizadoContacto.get(0).getIdFidelizado());
							key.setUidInstancia(datosSesion.getUidInstancia());
							fidelizado = fidelizadoMapper.selectFromViewByPrimaryKey(key);
							if (fidelizado != null) {
								fidCod = null;
								mensajesProcesamientoFidelizado.add(translation._("Ya existe un fidelizado con el email {0}", email));
							}
						}
					}

					if (fidelizado == null) {
						fidelizado = new FidelizadoBean();
						if (!validarDatosObligatorios(fidelizado, sheet, i, translation, mensajesProcesamientoFidelizado)) {
							resumen.put(fidelizado, mensajesProcesamientoFidelizado);
							continue;
						}
						inicializarFidelizado(fidelizado, sheet, i, translation, mensajesProcesamientoFidelizado, datosSesion);
						try {
							ServicioFidelizadosImpl.get().crear(fidelizado, (DatosSesionBean) datosSesion, conn);
							FidelizadoVersion fidelizadoVersion = new FidelizadoVersion(conn, fidelizado);
							fidVersionControlService.checkFidelizadosVersion(datosSesion, fidelizadoVersion);
							mensajesProcesamientoFidelizado.add(0, translation._("Creado"));
							sqlSession.commit();
						}
						catch (Exception e) {
							mensajesProcesamientoFidelizado.clear();
							mensajesProcesamientoFidelizado.add(0, translation._("Error al crear: {0}", e.getMessage()));
							sqlSession.rollback();
						}
					}
					/*
					 * Si existe el fidelizado pero se ha marcado el parámetro para editar los fidelizados encontrados por
					 * idFidelizado o codFidelizado
					 */
					else if(fidCod!=null && editarCoincidencias){
						fidCod.setTiposContacto(ServicioTiposContactoFidelizadoImpl.get().consultar(fidCod.getIdFidelizado(), (DatosSesionBean) datosSesion));
						fidCod.setEtiquetasCategorias(ServicioEtiquetasImpl.get().consultarEtiquetasCompletasPorClaseYObjeto("F_FIDELIZADOS_TBL.ID_FIDELIZADO", fidCod.getIdFidelizado().toString(), datosSesion));
						fidCod.setEtiquetasCategoriasCargados(true);	
						inicializarFidelizado(fidCod, sheet, i, translation, mensajesProcesamientoFidelizado, editarCoincidencias,datosSesion);
						try{
							ServicioFidelizadosImpl.get().salvar(fidelizado, (DatosSesionBean) datosSesion);
							mensajesProcesamientoFidelizado.add(translation._("El fidelizado se ha actualizado correctamente"));
							sqlSession.commit();
						}
						catch(Exception e){
							mensajesProcesamientoFidelizado.clear();
							mensajesProcesamientoFidelizado.add(0, translation._("Error al crear: {0}", e.getMessage()));
							sqlSession.rollback();
						}
					}
					else {
						try {
							// Tratamiento de las tarjetas del fidelizado
							// existente
							fidelizado.setEstadoBean(Estado.MODIFICADO);
							fidelizado.setTarjetas(new ArrayList<TarjetaBean>());
							CuentaBean cuentaVacia =  new CuentaBean();
							cuentaVacia.setTarjetas(ServicioTarjetasImpl.get().consultarTarjetasClienteSinCuenta(fidelizado.getIdFidelizado(), (DatosSesionBean) datosSesion));
							List<CuentaBean> cuentas = new ArrayList<CuentaBean>();
							cuentas.add(cuentaVacia);
							fidelizado.setCuentas(cuentas);
							tratamientoTarjetas(fidelizado, sheet, i, translation, mensajesProcesamientoFidelizado, datosSesion);
							if (!fidelizado.getTarjetas().isEmpty()) {
								ServicioFidelizadosImpl.get().modificar(conn, fidelizado, (DatosSesionBean) datosSesion);
								FidelizadoVersion fidelizadoVersion = new FidelizadoVersion(conn, fidelizado);
								fidVersionControlService.checkFidelizadosVersion(datosSesion, fidelizadoVersion);
							}
							sqlSession.commit();
						}
						catch (Exception e) {
							mensajesProcesamientoFidelizado.clear();
							mensajesProcesamientoFidelizado.add(0, translation._("Error al actualizar el fidelizado {0}: {1}", fidelizado.getIdFidelizado(), e.getMessage()));
							sqlSession.rollback();
						}
					}

					resumen.put(fidelizado, mensajesProcesamientoFidelizado);
				}
				sqlSession.commit();
			}

		}
		catch (BiffException e) {
			sqlSession.rollback();
			log.error("Ha ocurrido un error al cargar el fichero ", e);
			throw e;
		}
		catch (IOException e) {
			sqlSession.rollback();
			log.error("Ha ocurrido un error al cargar el fichero ", e);
			throw e;
		}
		catch (Exception e) {
			sqlSession.rollback();
			log.error("Ha ocurrido un error al importar el fichero ", e);
		}
		finally {
			sqlSession.close();
		}
		return resumen;
	}
}
