/*
 -----------------------------------------------------------------------------
 INGENICO Technical Software Department
 -----------------------------------------------------------------------------
 Copyright (c) 2011 - 2014 INGENICO.
 28-32 boulevard de Grenelle 75015 Paris, France.
 All rights reserved.
 This source program is the property of the INGENICO Company mentioned above
 and may not be copied in any form or by any means, whether in part or in whole,
 except under license expressly granted by such INGENICO company.
 All copies of this source program, whether in part or in whole, and
 whether modified or not, must display this and all other
 embedded copyright and ownership notices in full.
*/
package com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.axis.utils;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Logger for GUI - configuration file is "jc3api-log4j.properties"
 */
public class GuiLogger
{
	private static Logger logger_ = null;

	public static Logger getLogger()
    {
        if (logger_ == null) {
       		PropertyConfigurator.configure(GuiConstants.LOG4J_SETTINGS_FILE);
        	logger_ = Logger.getLogger(GuiMain.class);
        }
        return logger_;
    }
	
	public static void logInfo(String msg)
	{
		Logger logger = getLogger();
		if (logger != null) {
			logger.info(msg);
		}
	}

	public static void logError(String msg)
	{
		Logger logger = getLogger();
		if (logger != null) {
			logger.error(msg);
		}
	}

	public static void logException(String msg, Throwable t)
	{
		Logger logger = getLogger();
		if (logger != null) {
			logger.error(msg, t);
		}
	}
}
