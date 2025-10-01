package com.comerzzia.bricodepot.backoffice.services.procesamiento.notificaciones;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.app.event.implement.IncludeRelativePath;
import org.apache.velocity.runtime.log.NullLogChute;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.comerzzia.aena.util.xml.MarshallUtil;
import com.comerzzia.bricodepot.backoffice.persistence.mensajes.BuzonMensaje;
import com.comerzzia.bricodepot.backoffice.persistence.mensajes.BuzonMensajeMapper;
import com.comerzzia.bricodepot.backoffice.persistence.mensajeticket.MensajeTicketKey;
import com.comerzzia.bricodepot.backoffice.persistence.mensajeticket.MensajeTicketMapper;
import com.comerzzia.bricodepot.backoffice.services.procesamiento.notificaciones.factura.GeneradorTicketEmailPDF;
import com.comerzzia.core.model.canales.parametros.ParametroCanalBean;
import com.comerzzia.core.model.canales.parametros.ParametroCanalExample;
import com.comerzzia.core.model.empresas.ConfigEmpresaBean;
import com.comerzzia.core.model.empresas.EmpresaBean;
import com.comerzzia.core.model.empresas.EmpresaKey;
import com.comerzzia.core.model.notificaciones.ContactoModel;
import com.comerzzia.core.model.notificaciones.DestinatarioModel;
import com.comerzzia.core.model.notificaciones.NotificacionModel;
import com.comerzzia.core.model.notificaciones.buzones.BuzonBean;
import com.comerzzia.core.model.notificaciones.canales.BuzonMsgCanalBean;
import com.comerzzia.core.model.notificaciones.parametros.ParametroBean;
import com.comerzzia.core.model.notificaciones.plantillas.PlantillaBean;
import com.comerzzia.core.model.notificaciones.plantillas.PlantillaKey;
import com.comerzzia.core.model.notificaciones.tipos.NotificacionTipoBean;
import com.comerzzia.core.model.ventas.tickets.TicketBean;
import com.comerzzia.core.persistencia.canal.parametros.ParametroCanalMapper;
import com.comerzzia.core.persistencia.empresas.EmpresaMapper;
import com.comerzzia.core.persistencia.notificaciones.buzones.BuzonMsgMapper;
import com.comerzzia.core.persistencia.notificaciones.canales.BuzonMsgCanalMapper;
import com.comerzzia.core.persistencia.notificaciones.parametros.ParametroMapper;
import com.comerzzia.core.persistencia.notificaciones.plantillas.PlantillaMapper;
import com.comerzzia.core.servicios.notificaciones.tipos.ServicioNotificacionesTipos;
import com.comerzzia.core.servicios.sesion.DatosSesionBean;
import com.comerzzia.core.servicios.sesion.IDatosSesion;
import com.comerzzia.core.servicios.variables.ServicioVariablesImpl;
import com.comerzzia.core.servicios.ventas.tickets.ServicioTicketsImpl;
import com.comerzzia.core.util.config.AppInfo;
import com.comerzzia.core.util.config.PlantillasInfo;
import com.comerzzia.core.util.db.Connection;
import com.comerzzia.model.general.almacenes.AlmacenBean;
import com.comerzzia.model.general.servicios.ServicioBean;
import com.comerzzia.omnichannel.documentos.facturas.converters.albaran.ticket.TicketVentaAbono;
import com.comerzzia.persistencia.general.almacenes.AlmacenesDao;
import com.comerzzia.servicios.general.tiendas.ServicioTiendasImpl;
import com.comerzzia.servicios.general.tiendas.Tienda;
import com.comerzzia.servicios.procesamiento.notificaciones.GeneradorMensajesNotificacionException;
import com.comerzzia.servicios.procesamiento.notificaciones.GeneradorMensajesNotificacionImpl;

@Service
@Primary
@Scope("prototype")
@SuppressWarnings("deprecation")
public class BricodepotGeneradorMensajesNotificacionImpl extends GeneradorMensajesNotificacionImpl {

	private static final long serialVersionUID = 8015933889894453516L;
	protected static final Logger log = Logger.getLogger(BricodepotGeneradorMensajesNotificacionImpl.class);
	protected final static String PARAMETRO_MENSAJE = "mensaje";
	private TicketBean ticketNotificacion;
	protected final static String PARAMETRO_ADJUNTO1 = "ADJUNTO1";
	protected final static String PARAMETRO_ADJUNTO2 = "ADJUNTO2";
	protected final static String PARAMETRO_NOMBREADJUNTO1 = "NOMBREADJUNTO1";
	protected final static String PARAMETRO_NOMBREADJUNTO2 = "NOMBREADJUNTO2";
	protected final static String VARIABLE_NOMBRE_PDF_TICKET = "EMAIL.NOMBREADJUNTO1";
	protected final static String VARIABLE_NOMBRE_PDF_CUPONES = "EMAIL.NOMBREADJUNTO2";
	protected final static String NOMBRE_PDF_TICKET_DEFECTO = "ticket.pdf";
	protected final static String COD_TIPO_SERV_ENV_DOMICILIO = "1000";
	protected final static String COD_TIPO_SERV_ENV_TIENDA = "1010";

	public TicketBean getTicketNotificacion() {
		return ticketNotificacion;
	}

	public void setTicketNotificacion(TicketBean ticketNotificacion) {
		this.ticketNotificacion = ticketNotificacion;
	}

	@Override
	public List<String> generarMensajes(Connection conn, NotificacionModel notificacion, IDatosSesion datosSesion) {
		log.debug("generarMensajes() - Usando la clase de BRICODEPOT para generar mensajes");

		List<String> listUidMensajes = new ArrayList<String>();
		log.debug(notificacion.getTipoNotificacion().toString());
		if (notificacion.getTipoNotificacion().equalsIgnoreCase(BricodepotProcesadorNotificaciones.TIPO_NOTIFICACION_ENVIO_TICKET)) {
			try {
				generarMensajesBricodepot(conn, notificacion, datosSesion, listUidMensajes);
			}
			catch (Exception e) {
				throw new GeneradorMensajesNotificacionException(e.getMessage(), e);
			}
		}
		log.debug("generarMensajes() - Lista de mensajes (UID): " + listUidMensajes);
		return listUidMensajes;
	}

	public List<String> generarMensajesBricodepot(Connection conn, NotificacionModel notificacion, IDatosSesion datosSesion, List<String> listUidMensajes) throws Exception {
		log.debug("generarMensajesBricodepot()");
		try {
			SqlSession sqlSession = datosSesion.getSqlSessionFactory().openSession(conn);

			log.debug("generarMensajesBricodepot() - PASO 1 : Obteniendo tipo de notificación...");
			NotificacionTipoBean notificacionTipo = ServicioNotificacionesTipos.get().consultar(datosSesion, notificacion.getTipoNotificacion(), notificacion.getCodlengua());

			BuzonMsgMapper buzonMapper = sqlSession.getMapper(BuzonMsgMapper.class);
			BuzonMsgCanalMapper canalMapper = sqlSession.getMapper(BuzonMsgCanalMapper.class);
			ParametroMapper parametroMapper = sqlSession.getMapper(ParametroMapper.class);
			PlantillaMapper plantillaMapper = sqlSession.getMapper(PlantillaMapper.class);
			EmpresaMapper empresaMapper = sqlSession.getMapper(EmpresaMapper.class);
			ParametroCanalMapper parametroCanalMapper = sqlSession.getMapper(ParametroCanalMapper.class);
			BuzonMensajeMapper buzonMensajeMapper = sqlSession.getMapper(BuzonMensajeMapper.class);
			MensajeTicketMapper mensajeTicketMapper = sqlSession.getMapper(MensajeTicketMapper.class);

			log.debug("generarMensajesBricodepot() - PASO 2 : Obteniendo empresa...");
			EmpresaBean empresa = null;
			if (notificacion.getCodemp() != null) {
				EmpresaKey empresaKey = new EmpresaKey();
				empresaKey.setUidActividad(datosSesion.getUidActividad());
				empresaKey.setCodEmp(notificacion.getCodemp());
				empresa = empresaMapper.selectFromViewByPrimaryKey(empresaKey);
				log.debug("UID Instancia de la empresa: " + empresa.getUidInstancia());
			}

			log.debug("generarMensajesBricodepot() - PASO 3 : Obteniendo rutas para las plantillas...");
			PlantillasInfo plantillasInfo = AppInfo.getPlantillasInfo();
			if (plantillasInfo == null) {
				throw new IllegalStateException("Se debe configurar la ruta base para las plantillas");
			}
			String rutaBase = plantillasInfo.getRutaBase();
			if (rutaBase == null) {
				throw new IllegalStateException("Se debe configurar la ruta base para las plantillas, la ruta es null");
			}

			log.debug("generarMensajesBricodepot() - PASO 4 : Procesando destinatarios...");
			for (DestinatarioModel destinatarioModel : notificacion.getDestinatarios()) {
				for (ContactoModel tipoContacto : destinatarioModel.getContactos()) {
					log.debug("generarMensajesBricodepot() - PASO 5 : Procesando contacto " + tipoContacto.getClaveContacto() + "/" + tipoContacto + tipoContacto.getValorContacto() + "...");

					String uidActividad = datosSesion.getUidActividad();
					String uidMensaje = UUID.randomUUID().toString();
					listUidMensajes.add(uidMensaje);

					// Creamos el objeto que relaciona el buzón con el mensaje
					log.debug("generarMensajesBricodepot() - PASO 6 : Instanciando buzón...");
					BuzonBean buzonBean = new BuzonBean();
					buzonBean.setUidActividad(uidActividad);
					buzonBean.setIdBuzon(notificacionTipo.getIdBuzon());
					buzonBean.setUidMensaje(uidMensaje);
					buzonBean.setFechaMensaje(new Date());

					// Cambiamos la plantilla del ticket en el caso de que sea PT o ES
					String idioma = "";
					if (tipoContacto.getIdPlantillaMensaje() == 175L) {
						if ("PT".equals(notificacion.getCodlengua())) {
							// tipoContacto.setIdPlantillaMensaje(177L);
							idioma = "PT";
						}
						else {
							idioma = "ES";
							ConfigEmpresaBean config = new ConfigEmpresaBean();
							config.setUidActividad(empresa.getUidActividad());
							config.setUidInstancia(empresa.getUidInstancia());
							config.setCodEmpresa(empresa.getCodEmp());
							AlmacenBean almacen = AlmacenesDao.consultar(conn, config, ticketNotificacion.getCodAlmacen());

							// Consultamos la tienda segun el codAlm de la notificación.
							Tienda tiendaObtenida = ServicioTiendasImpl.get().consultar(almacen.getCodAlm(), config);
							// Obtenemos el idoma del cliente de la tienda
							String idiomaTienda = tiendaObtenida.getCliente().getCodlengua();

							if (idiomaTienda != null && StringUtils.isNotBlank(idiomaTienda)) {
								idioma = idiomaTienda;
							}
						}
					}
					buzonBean.setIdPlantillaMensaje(tipoContacto.getIdPlantillaMensaje());

					// Creamos el objeto que relaciona el canal con el mensaje
					log.debug("generarMensajesBricodepot() - PASO 7 : Instanciando canal...");
					BuzonMsgCanalBean canalBean = new BuzonMsgCanalBean();
					canalBean.setUidActividad(uidActividad);
					canalBean.setUidMensaje(uidMensaje);
					canalBean.setIdCanal(tipoContacto.getIdCanal());
					canalBean.setProcesado("N");
					canalBean.setIntentosProcesado(new Short("0"));

					VelocityContext context = new VelocityContext();
					parametrosContext(context, empresa, rutaBase, destinatarioModel, datosSesion);

					// Creamos el contexto de Velocity y le añadimos los valores que necesita la plantilla
					log.debug("generarMensajesBricodepot() - PASO 8 : Creando el contexto de Velocity y añadiendo los valores que necesita la plantilla.");
					ParametroCanalExample example = new ParametroCanalExample();
					example.or().andUidActividadEqualTo(uidActividad).andIdCanalEqualTo(tipoContacto.getIdCanal());
					List<ParametroCanalBean> parametrosDelCanal = parametroCanalMapper.selectByExample(example);
					for (ParametroCanalBean parametroDelCanal : parametrosDelCanal) {
						context.put(parametroDelCanal.getParametro(), parametroDelCanal.getValor());
					}

					log.debug("generarMensajesBricodepot - Disponible en el contexto: ");
					for (Object key : context.getKeys()) {
						log.debug("generarMensajes - " + key.toString());
					}

					// Obtenemos la plantilla de velocity que se va a usar para formatear el mensaje
					PlantillaKey plantillaKey = new PlantillaKey();
					plantillaKey.setUidActividad(uidActividad);
					plantillaKey.setIdPlantillaMensaje(tipoContacto.getIdPlantillaMensaje());
					PlantillaBean plantillaBean = plantillaMapper.selectByPrimaryKey(plantillaKey);

					// EN CASO DE NO ENCONTRAR LA PLANTILLA POR PAIS, BUSCAR POR DEFECTO.
					byte[] mensaje = null;
					boolean reintentar = false;
					String plantillaInicial = plantillaBean.getPlantilla();

					insertarImagenesMensaje(context, rutaBase, idioma);

					try {
						plantillaBean.setPlantilla(seleccionarPlantilla(plantillaInicial, notificacion));
						String plantilla = plantillaBean.getPlantilla() + idioma.toUpperCase() + "/ticket_" + idioma.toLowerCase() + ".vm";

						mensaje = formatearMensaje(plantilla, rutaBase, context);
						if (mensaje == null) {
							String msgError = "Error al generar el mensaje del email, se realizará otro intento con plantilla por defecto (ES).";
							log.error("generarMensajes() - " + msgError);
							reintentar = true;
						}
					}
					catch (Exception e) {
						String msgError = "Error al generar el mensaje del email, se realizará otro intento con plantilla por defecto (ES) : " + e.getMessage();
						log.error("generarMensajes() - " + msgError, e);
						reintentar = true;
					}

					// En caso de no encontrar la plantilla definida, debemos intentar con la de ES, que siempre damos
					// por hecho que está.
					if (reintentar) {
						plantillaBean.setPlantilla(seleccionarPlantillaPorDefecto(plantillaInicial));
						mensaje = formatearMensaje(plantillaBean.getPlantilla(), rutaBase, context);
					}

					// String idiomaPlantilla = notificacion.getCodlengua().toUpperCase();
					log.debug("generarMensajesBricodepot() - Formateando mensaje.");
					// mensaje = formatearMensaje(idiomaPlantilla, plantillaBean.getPlantilla(), rutaBase, context);

					// Formateamos el asunto
					ServicioBean servicio = (ServicioBean) context.get("servicio");
					String asunto = notificacionTipo.getAsunto();
					buzonBean.setAsunto(formatearAsunto(asunto, rutaBase, context));

					buzonMapper.insert(buzonBean);
					canalMapper.insert(canalBean);

					// Creamos el parámetro que contiene el mensaje formateado
					ParametroBean param = new ParametroBean();
					param.setUidActividad(uidActividad);
					param.setUidMensaje(uidMensaje);
					param.setParametro(PARAMETRO_MENSAJE);
					param.setContenido(mensaje);
					parametroMapper.insert(param);

					param = new ParametroBean();
					param.setUidActividad(uidActividad);
					param.setUidMensaje(uidMensaje);
					param.setParametro(tipoContacto.getClaveContacto());
					param.setContenido(tipoContacto.getValorContacto().getBytes(StandardCharsets.UTF_8));
					parametroMapper.insert(param);

					/* Si se trata de una notificación en la que haya que adjuntar la factura como pdf */
					log.debug("generarMensajesBricodepot() - Se adjuntará PDF.");
					log.debug("generarMensajesBricodepot() - Contiene servicio: " + context.containsKey("servicio"));

					generarPDF(datosSesion, parametroMapper, buzonMensajeMapper, mensajeTicketMapper, servicio.getUidDocumentoOrigen(), uidMensaje, buzonBean, servicio.getUidServicio(),
					        servicio.getCodDocumentoOrigen(), idioma);

					for (Entry<String, Object> entry : destinatarioModel.getObjetos().entrySet()) {
						context.put(entry.getKey(), entry.getValue());
					}
				}

			}
		}
		catch (Exception e) {
			String mensajeError = "Error al generar el mensaje para enviar el ticket por email.";
			log.error("generarMensajesBricodepot() - " + mensajeError + " : " + e.getMessage(), e);
			throw new Exception(mensajeError, e);

		}
		return listUidMensajes;
	}

	private void generarPDF(IDatosSesion datosSesion, ParametroMapper parametroMapper, BuzonMensajeMapper buzonMensajeMapper, MensajeTicketMapper mensajeTicketMapper, String uidTicket,
	        String uidMensaje, BuzonBean buzonBean, String uidServicio, String codTicket, String idioma) throws Exception {

		ParametroBean param = new ParametroBean();
		GeneradorTicketEmailPDF generadorFactura = new GeneradorTicketEmailPDF();

		byte[] factura = null;

		try {

			factura = generadorFactura.generatePdf(datosSesion, uidTicket, false, idioma);

			param.setUidActividad(datosSesion.getUidActividad());
			param.setUidMensaje(uidMensaje);
			param.setParametro(PARAMETRO_ADJUNTO1);

			if (factura != null) {
				// Insertamos en BD el pdf con el ticket en base64
				Base64 codec = new Base64();
				param.setContenido(codec.encode(factura));
				parametroMapper.insert(param);

				log.debug("generarPDF() - Iniciando la inserción de la asociación de servicio con mensaje...");
				BuzonMensaje buzonMensaje = new BuzonMensaje();
				buzonMensaje.setUidActividad(datosSesion.getUidActividad());
				buzonMensaje.setUidServicio(uidServicio);
				buzonMensaje.setIdBuzon(buzonBean.getIdBuzon());
				buzonMensaje.setUidMensaje(uidMensaje);
				buzonMensaje.setUidTicketNotificacion(ticketNotificacion.getUidTicket());
				if (buzonMensajeMapper.updateByPrimaryKey(buzonMensaje) == 0) {
					buzonMensajeMapper.insert(buzonMensaje);
				}

				log.debug("generarPDF() - Iniciando la inserción de la asociación de ticket de venta con mensaje...");
				MensajeTicketKey mensajeTicket = new MensajeTicketKey();
				mensajeTicket.setUidActividad(datosSesion.getUidActividad());
				mensajeTicket.setUidTicket(uidTicket);
				mensajeTicket.setUidMensaje(uidMensaje);
				mensajeTicketMapper.insert(mensajeTicket);

				log.debug("generarPDF() - Finalizada la inserción de la asociación de servicion con mensaje.");
			}
		}
		catch (Exception e) {
			log.error("generarPDF() - Error al generar el pdf y adjuntarlo: " + e.getMessage(), e);
			throw new Exception(e.getMessage());
		}

		// Insertamos en BD el nombre del documento del pdf con el ticket
		param = new ParametroBean();
		param.setUidActividad(datosSesion.getUidActividad());
		param.setUidMensaje(uidMensaje);
		param.setParametro(PARAMETRO_NOMBREADJUNTO1);

		String nombrePDF = "";
		try {
			nombrePDF = ServicioVariablesImpl.get().consultarValor(datosSesion, VARIABLE_NOMBRE_PDF_TICKET);
		}
		catch (Exception e) {
		}

		if (StringUtils.isBlank(nombrePDF.trim())) {
			nombrePDF = NOMBRE_PDF_TICKET_DEFECTO;
		}

		String[] split = nombrePDF.split("\\.");
		nombrePDF = split[0] + "_" + codTicket + "." + split[1];
		if (factura == null || StringUtils.isBlank(nombrePDF)) {
			throw new Exception("Error: El adjunto (PDF) o el nombre del adjunto es nulo.");
		}
		log.debug("generarPDF() - Mostrando nombre del PDF " + nombrePDF);
		param.setContenido(nombrePDF.getBytes(StandardCharsets.UTF_8));
		parametroMapper.insert(param);
	}

	public Boolean hayCupones(IDatosSesion datosSesion, String uidTicket) throws Exception {
		try {
			TicketBean ticket = null;
			try {
				ticket = ServicioTicketsImpl.get().consultarTicketUid(uidTicket, datosSesion.getUidActividad());
			}
			catch (Exception e) {
				log.error("hayCupones() -  : " + e.getMessage(), e);
				throw new Exception("Error al buscar el ticket para enviar por email (" + uidTicket + ")", e);
			}

			if (ticket != null) {
				TicketVentaAbono ticketVenta = null;
				try {
					ticketVenta = (TicketVentaAbono) MarshallUtil.leerXML(ticket.getTicket(), TicketVentaAbono.class);
					if (ticketVenta == null) {
						String msgError = "Error al parsear el xml del ticket en un objeto 'TicketVentaAbono.class'.";
						throw new Exception(msgError);
					}
					return ticketVenta.getCuponesEmitidos().size() != 0;
				}
				catch (Exception e) {
					String msgError = e.getMessage();
					log.error("hayCupones() - " + msgError, e);
					throw new Exception(msgError, e);
				}
			}
		}
		catch (Exception e) {
			log.error("hayCupones() - Error comprobando si el ticket contiene cupones emitidos : " + e.getMessage(), e);
			throw new Exception(e.getMessage(), e);
		}

		return false;
	}

	protected String formatearAsunto(String asunto, String rutaBase, VelocityContext context) {
		StringWriter stringWriter = null;

		VelocityEngine engine = new VelocityEngine();
		engine.setProperty(Velocity.RESOURCE_LOADER, "string");
		engine.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM_CLASS, NullLogChute.class.getName());
		engine.addProperty("string.resource.loader.class", StringResourceLoader.class.getName());
		engine.addProperty("string.resource.loader.repository.static", "false");
		engine.addProperty("eventhandler.include.class", IncludeRelativePath.class.getName());

		engine.setProperty(VelocityEngine.INPUT_ENCODING, "UTF-8");
		engine.setProperty(VelocityEngine.OUTPUT_ENCODING, "UTF-8");
		engine.init();

		String templateName = "templateName";

		StringResourceRepository repo = (StringResourceRepository) engine.getApplicationAttribute(StringResourceLoader.REPOSITORY_NAME_DEFAULT);
		repo.putStringResource(templateName, asunto);

		Template template = engine.getTemplate(templateName, "UTF-8");
		template.setEncoding("UTF-8");
		stringWriter = new StringWriter();

		template.merge(context, stringWriter);

		String string = stringWriter.toString();

		if (log.isDebugEnabled()) {
			log.debug(string);
		}

		return string;
	}

	protected byte[] formatearMensaje(String idiomaPlantilla, String urlTemplate, String rutaBase, VelocityContext context) {
		StringWriter stringWriter = null;
		VelocityEngine engine = new VelocityEngine();
		Properties props = new Properties();
		props.put("file.resource.loader.path", rutaBase);
		engine.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM_CLASS, NullLogChute.class.getName());
		engine.setProperty(VelocityEngine.INPUT_ENCODING, "UTF-8");
		engine.setProperty(VelocityEngine.OUTPUT_ENCODING, "UTF-8");
		engine.addProperty("eventhandler.include.class", IncludeRelativePath.class.getName());
		engine.init(props);

		Template template = engine.getTemplate(urlTemplate, "UTF-8");
		template.setEncoding("UTF-8");
		stringWriter = new StringWriter();

		template.merge(context, stringWriter);

		String string = stringWriter.toString();

		if (log.isDebugEnabled()) {
			log.debug(string);
		}

		return string.getBytes(StandardCharsets.UTF_8);
	}

	// Modificaciones para seleccionar la plantilla según el país/idioma de la empresa
	private String seleccionarPlantilla(String rutaPlantillas, NotificacionModel notificacion) {

		String[] plantillaDiv = rutaPlantillas.split("/");

		rutaPlantillas = rutaPlantillas.replace(plantillaDiv[plantillaDiv.length - 1], plantillaDiv[plantillaDiv.length - 1]);
		return rutaPlantillas;
	}

	private String seleccionarPlantillaPorDefecto(String rutaPlantillas) {
		String[] plantillaDiv = rutaPlantillas.split("/");
		rutaPlantillas = rutaPlantillas.replace(plantillaDiv[plantillaDiv.length - 1], plantillaDiv[plantillaDiv.length - 1]);
		return rutaPlantillas;
	}

	@Override
	protected void parametrosContext(VelocityContext context, EmpresaBean empresa, String rutaPlantillas, DestinatarioModel destinatarioModel, IDatosSesion datosSesion) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		// Las notificaciones de canjeo de puntos no tienen el tag empresa
		if (empresa != null && empresa.getCodPais() != null) {
			Locale locale = getLocalePais(empresa.getCodPais());
			NumberFormat numberFormat = NumberFormat.getInstance(locale);
			numberFormat.setGroupingUsed(true);
			numberFormat.setMinimumFractionDigits(2);
			numberFormat.setMaximumFractionDigits(3);

			NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);
			Currency currency = Currency.getInstance(Currency.getInstance(locale).getCurrencyCode());
			numberFormat.setCurrency(currency);
			numberFormat.setMaximumFractionDigits(currency.getDefaultFractionDigits());
			context.put("numberFormat", numberFormat);
			context.put("currencyFormat", currencyFormat);
		}

		context.put("empresa", empresa);
		context.put("rutaPlantillas", rutaPlantillas);
		context.put("dateFormat", dateFormat);

		if (datosSesion instanceof DatosSesionBean) {
			context.put("desglose1Activo", ((DatosSesionBean) datosSesion).isDesglose1Activo());
			context.put("tituloDesglose1", ((DatosSesionBean) datosSesion).getTituloDesglose1());
			context.put("desglose2Activo", ((DatosSesionBean) datosSesion).isDesglose2Activo());
			context.put("tituloDesglose2", ((DatosSesionBean) datosSesion).getTituloDesglose2());
		}

		for (Entry<String, Object> entry : destinatarioModel.getObjetos().entrySet()) {
			context.put(entry.getKey(), entry.getValue());
		}
		for (Entry<String, String> parametro : destinatarioModel.getParametros().entrySet()) {
			context.put(parametro.getKey(), parametro.getValue());
		}

	}

	private void insertarImagenesMensaje(VelocityContext context, String rutaBase, String idioma) {

		// Creamos un mapa con todas las imagenes
		Map<String, String> imagenes = new HashMap<>();
		imagenes.put("cid:logo_brico_aniv.png", "logoBricoAnivURL");
		imagenes.put("cid:ticket_ca.jpg", "ticketCaURL");
		imagenes.put("cid:ticket_pt.jpg", "ticketPtURL");
		imagenes.put("cid:ticket_es.jpg", "ticketEsURL");
		imagenes.put("cid:logo_brico_footer.png", "logoBricoFooterURL");
		imagenes.put("cid:serv1_new.png", "serv1URL");
		imagenes.put("cid:serv2_new.png", "serv2URL");
		imagenes.put("cid:serv3_new.png", "serv3URL");
		imagenes.put("cid:serv4_new.png", "serv4URL");
		imagenes.put("cid:serv5_new.png", "serv5URL");
		imagenes.put("cid:serv6_new.png", "serv6URL");
		imagenes.put("cid:instagram.png", "instagramURL");
		imagenes.put("cid:facebook.png", "facebookURL");
		imagenes.put("cid:linkedin.png", "linkedinURL");
		imagenes.put("cid:youtube.png", "youtubeURL");
		imagenes.put("cid:logo_kingfisher.png", "logoKingFisherURL");
		imagenes.put("cid:puntos.jpg", "puntosURL");
		imagenes.put("cid:logo_brico.png", "logoBricoURL");
		// Añadimos todas las imagenes con sus rutas y la clave para agregarlo al context
		try {
			for (Map.Entry<String, String> entry : imagenes.entrySet()) {
				String imageName = entry.getKey();
				String imageVariableName = entry.getValue();
				addImageToContext(imageName, imageVariableName, context);
			}
		}
		catch (Exception e) {
		}

	}

	private void addImageToContext(String imageName, String imageVariableName, VelocityContext context) throws Exception {
		context.put(imageVariableName, imageName);
	}

	private Locale getLocalePais(String codPais) {
		Locale[] locales = Locale.getAvailableLocales();
		Locale locale = Locale.getDefault();

		for (Locale l : locales) {
			if (l.getCountry().equals(codPais)) {
				locale = l;
				break;
			}
		}

		return locale;
	}

}
