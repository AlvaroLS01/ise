/*
 -----------------------------------------------------------------------------
 INGENICO Technical Software Department
 -----------------------------------------------------------------------------
 Copyright (c) 2011 - 2015 INGENICO.
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

import java.awt.Font;

public interface GuiConstants
{
	public static final String WORK_DIR = ".";
	public static final String ICONS_DIR = "icons";

	public static final String GUI_SETTINGS_FILE = "jc3api-gui.properties";
	public static final String C3AGENT_SETTINGS_FILE = "jc3api-example.properties";
	public static final String LOG4J_SETTINGS_FILE = "jc3api-log4j.properties";

	public static final String ICON_C3AGENT = "icon-c3agent.png";
	public static final String ICON_INGENICO = "icon-ingenico.png";
	public static final String ICON_SETTINGS = "icon-settings.png";

	public static final String BOOLEAN_FALSE = Boolean.FALSE.toString();
	public static final String BOOLEAN_TRUE = Boolean.TRUE.toString();

	public static final String STRING_EMPTY = "";
	public static final String STRING_ZERO = "0";
	public static final String STRING_COMMA = ",";
	public static final String STRING_DOT = ".";
	
	public static final char CHAR_COMMA = STRING_COMMA.charAt(0);
	public static final char CHAR_DOT = STRING_DOT.charAt(0);
	public static final char CHAR_ZERO = STRING_ZERO.charAt(0);
	
	public static final String AMOUNT_INPUT_DEFAULT = "1";

	public static final String OPTIONPANE_OKBUTTON_TEXT = "OptionPane.okButtonText";
	public static final String OPTIONPANE_CANCELBUTTON_TEXT = "OptionPane.cancelButtonText";

	public static final String OPTIONPANE_MESSAGE_FONT = "OptionPane.messageFont";
	public static final String OPTIONPANE_BUTTON_FONT = "OptionPane.buttonFont";

	public static final String BUTTON_OK_TEXT = "OK";
	public static final String BUTTON_CANCEL_TEXT = "CANCEL";

	public static final Font FONT_COURIER_PLAIN_12 = new Font("Courier", Font.PLAIN, 12);
	public static final Font FONT_COURIER_PLAIN_16 = new Font("Courier", Font.PLAIN, 16);
	public static final Font FONT_COURIER_BOLD_18 = new Font("Courier", Font.BOLD, 18);
}
