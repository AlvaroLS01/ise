package com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.axis.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class ObjectParseUtil {
	
	private static Logger log = Logger.getLogger(ObjectParseUtil.class);

	public static Map<String, Object> introspect(Object obj) {
		Map<String, Object> result = new HashMap<String, Object>();
		BeanInfo info;
		try {
			info = Introspector.getBeanInfo(obj.getClass());
			for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
				Method reader = pd.getReadMethod();
				if (reader != null) {
					Object invoke = reader.invoke(obj);
					if (invoke != null && !invoke.getClass().isArray()) {
						result.put(pd.getName(), invoke);
					}
				}
			}
		}
		catch (Exception e) {
			log.error("introspect() - Error seriealizando objeto a map: " + e.getMessage(), e);
		}

		return result;
	}

}
