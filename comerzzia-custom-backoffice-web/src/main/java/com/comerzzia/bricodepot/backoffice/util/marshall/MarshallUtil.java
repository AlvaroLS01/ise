package com.comerzzia.bricodepot.backoffice.util.marshall;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.log4j.Logger;

public class MarshallUtil {

	private static final Logger log = Logger.getLogger(MarshallUtil.class.getName());

	protected static volatile MarshallUtil instance;

	protected Map<Class<?>, JAXBContextCache> cache = new HashMap<Class<?>, MarshallUtil.JAXBContextCache>();

	public static MarshallUtil get() {
		// Doble check y atributo volatile para asegurarnos de que no se construyen más de una instancia en entornos
		// multi-hilo
		if (instance == null) {
			synchronized (MarshallUtil.class) {
				if (instance == null) {
					instance = new MarshallUtil();
				}
			}
		}
		return instance;
	}

	/** Permite la personalización de la instancia */
	public static void setInstance(MarshallUtil inst) {
		instance = inst;
	}

	public static byte[] crearXML(Object obj) throws MarshallUtilException {
		return get().marshal(obj);
	}

	public static byte[] crearXML(Object obj, List<Class<?>> clasesAux) throws MarshallUtilException {
		return get().marshal(obj, clasesAux);
	}

	public byte[] marshal(Object obj) throws MarshallUtilException {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			long time = System.currentTimeMillis();
			getMarshallContext(obj).getMarshaller().marshal(obj, out);
			log.trace(String.format("crearXML() - Tiempo en hacer marshal: %sms", (System.currentTimeMillis() - time)));
			return out.toByteArray();
		}
		catch (JAXBException e) {
			log.error("crearXML() - Error creando XML: " + e.getMessage(), e);
			throw new MarshallUtilException(e);
		}
	}

	public byte[] marshal(Object obj, List<Class<?>> clasesAux) throws MarshallUtilException {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			long time = System.currentTimeMillis();
			getMarshallContext(obj, clasesAux).getMarshaller().marshal(obj, out);
			log.trace(String.format("crearXML() - Tiempo en hacer marshal: %sms", (System.currentTimeMillis() - time)));
			return out.toByteArray();
		}
		catch (JAXBException e) {
			log.error("crearXML() - Error creando XML: " + e.getMessage(), e);
			throw new MarshallUtilException(e);
		}
	}

	public static Object leerXML(byte[] cadenaXML, Class<?>... clase) {
		return get().unmarshal(cadenaXML, clase);
	}

	public Object unmarshal(byte[] cadenaXML, Class<?>... clase) {
		Object res = null;
		try {
			String stringXML = new String(cadenaXML, StandardCharsets.UTF_8);
			res = getMarshallContext(clase).getUnmarshaller().unmarshal(new StringReader(stringXML));
		}
		catch (JAXBException e) {
			log.error("crearXML() - Error creando XML" + e.getMessage(), e);
		}
		return res;
	}

	public static Object leerXML(InputStream is, Class<?> clase) throws JAXBException {
		return get().unmarshal(is, clase);
	}

	public Object unmarshal(InputStream is, Class<?> clase) throws JAXBException {
		return getMarshallContext(clase).getUnmarshaller().unmarshal(is);
	}

	public static <T> List<T> leerXMLListaParcial(InputStream is, Class<T> itemClass, String itemTag, Integer maxElements) throws XMLStreamException, JAXBException {
		return get().unmarshalListaParcial(is, itemClass, itemTag, maxElements);
	}

	public <T> List<T> unmarshalListaParcial(InputStream is, Class<T> itemClass, String itemTag, Integer maxElements) throws XMLStreamException, JAXBException {
		LinkedList<T> list = new LinkedList<T>();

		final QName qName = new QName(itemTag);

		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(is);

		Unmarshaller unmarshaller = getMarshallContext(itemClass).getUnmarshaller();

		XMLEvent e = null;

		Integer count = 0;
		while (((e = xmlEventReader.peek()) != null) && count < maxElements) {
			// check the event is a Document start element
			if (e.isStartElement() && ((StartElement) e).getName().equals(qName)) {
				list.add(unmarshaller.unmarshal(xmlEventReader, itemClass).getValue());
				count++;
			}
			else {
				xmlEventReader.next();
			}
		}

		return list;
	}

	public static void setFastBoot() {
		System.setProperty("com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl.fastBoot", Boolean.TRUE.toString());
	}

	@SuppressWarnings("unchecked")
	protected JAXBContextCache getMarshallContext(Object obj) throws JAXBException {
		// No podemos cachear si nos llega una lista o array
		if (obj instanceof Class[]) {
			return initContextForArray((Class[]) obj);
		}
		else if (obj instanceof List) {
			return initContextForList((List<Class<?>>) obj);
		}

		Class<?> clazz = (Class<?>) ((obj instanceof Class) ? obj : obj.getClass());
		JAXBContextCache marshallContext = cache.get(clazz);
		if (marshallContext == null) {
			marshallContext = init(obj);
			cache.put(clazz, marshallContext);
		}
		return marshallContext;
	}

	@SuppressWarnings("unchecked")
	protected JAXBContextCache getMarshallContext(Object obj, List<Class<?>> clasesAux) throws JAXBException {
		// No podemos cachear si nos llega una lista o array
		if (obj instanceof Class[]) {
			return initContextForArray((Class[]) obj);
		}
		else if (obj instanceof List) {
			return initContextForList((List<Class<?>>) obj);
		}

		Class<?> clazz = (Class<?>) ((obj instanceof Class) ? obj : obj.getClass());
		JAXBContextCache marshallContext = cache.get(clazz);
		if (marshallContext == null) {
			marshallContext = init(obj, clasesAux);
			cache.put(clazz, marshallContext);
		}
		return marshallContext;
	}

	protected JAXBContextCache init(Object obj) throws JAXBException {
		LinkedList<Class<?>> clases = new LinkedList<>();

		if (obj instanceof Class) {
			clases.add((Class<?>) obj);
		}
		else {
			clases.add(obj.getClass());
			clases.addAll((getDeclaredTypes(obj)));
		}

		log.debug(String.format("init() - Iniciando nuevo contexto de JAXB para %s", clases.get(0).getName()));

		Class<?>[] classArray = clases.toArray(new Class[] {});
		return initContextForArray(classArray);
	}

	protected JAXBContextCache init(Object obj, List<Class<?>> clasesAux) throws JAXBException {
		LinkedList<Class<?>> clases = new LinkedList<>();

		if (obj instanceof Class) {
			clases.add((Class<?>) obj);
		}
		else {
			clases.add(obj.getClass());
			clases.addAll((getDeclaredTypes(obj)));
		}
		clases.addAll(clasesAux);

		log.debug(String.format("init() - Iniciando nuevo contexto de JAXB para %s", clases.get(0).getName()));

		Class<?>[] classArray = clases.toArray(new Class[] {});
		return initContextForArray(classArray);
	}

	protected JAXBContextCache initContextForList(List<Class<?>> clases) throws JAXBException {
		Class<?>[] classArray = clases.toArray(new Class[] {});
		return initContextForArray(classArray);
	}

	@SuppressWarnings("rawtypes")
	protected JAXBContextCache initContextForArray(Class[] classArray) throws JAXBException {
		long time = System.currentTimeMillis();
		final JAXBContext jaxbContext = JAXBContext.newInstance(classArray);
		log.debug(String.format("getMarshallContext() - Iniciado nuevo contexto de JAXB en %sms", (System.currentTimeMillis() - time)));

		ThreadLocal<Marshaller> marshallerThreadLocal = new ThreadLocal<Marshaller>(){

			@Override
			protected Marshaller initialValue() {
				try {
					Marshaller marshaller = jaxbContext.createMarshaller();
					marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
					marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

					return marshaller;
				}
				catch (JAXBException e) {
					throw new RuntimeException(e);
				}
			}
		};
		ThreadLocal<Unmarshaller> unmarshallerThreadLocal = new ThreadLocal<Unmarshaller>(){

			@Override
			protected Unmarshaller initialValue() {
				try {
					Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
					return unmarshaller;
				}
				catch (JAXBException e) {
					throw new RuntimeException(e);
				}
			}
		};

		return new JAXBContextCache(marshallerThreadLocal, unmarshallerThreadLocal);
	}

	protected Collection<Class<?>> getDeclaredTypes(Object obj) {
		Set<Class<?>> clases = new LinkedHashSet<>();
		Object object = obj;
		do {
			List<Field> declaredFields = getDeclaredFields(object.getClass());
			for (Field field : declaredFields) {
				try {
					field.setAccessible(true);
					if (field.getType().isAnnotation()) {
						continue;
					}
					if (field.isAnnotationPresent(XmlTransient.class)) {
						continue;
					}
					Object fieldValue = field.get(obj);
					if (fieldValue == null) {
						clases.add(field.getType());
						continue;
					}
					clases.add(fieldValue.getClass());
					if (fieldValue instanceof List) {
						@SuppressWarnings("rawtypes")
						List list = ((List) fieldValue);
						if (list.size() > 0) {
							clases.add(list.get(0).getClass());
						}
					}
				}
				catch (IllegalArgumentException e) {
					log.trace("getDeclaredClasses() - " + e.getClass().getName() + " - " + e.getLocalizedMessage());
				}
				catch (Exception e) {
					log.error("getDeclaredClasses() - " + e.getClass().getName() + " - " + e.getLocalizedMessage(), e);
				}
			}
		}
		while ((object = object.getClass().getSuperclass()) != Object.class);
		return clases;
	}

	protected List<Field> getDeclaredFields(Class<? extends Object> clazz) {
		List<Field> list = new ArrayList<Field>();
		list.addAll(Arrays.asList(clazz.getDeclaredFields()));
		Class<?> superClass = clazz.getSuperclass();
		do {
			list.addAll(Arrays.asList(superClass.getDeclaredFields()));
			superClass = superClass.getSuperclass();
		}
		while (superClass != null);

		return list;
	}

	protected static class JAXBContextCache {

		private ThreadLocal<Marshaller> tlMarshaller;
		private ThreadLocal<Unmarshaller> tlUnmarshaller;

		public JAXBContextCache(ThreadLocal<Marshaller> tlMarshaller, ThreadLocal<Unmarshaller> tlUnmarshaller) {
			super();
			this.tlMarshaller = tlMarshaller;
			this.tlUnmarshaller = tlUnmarshaller;
		}

		public Marshaller getMarshaller() {
			return tlMarshaller.get();
		}

		public Unmarshaller getUnmarshaller() {
			return tlUnmarshaller.get();
		}
	}

}
