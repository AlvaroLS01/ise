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

/**
 * List of currencies supported by the GUI
 */
public enum GuiCurrency
{
	EURO(
		"978", "EUR", GuiConstants.STRING_COMMA, 2
	),
	GREAT_BRITAIN_POUND(
		"826", "GBP", GuiConstants.STRING_DOT, 2
	),
	US_DOLLAR(
		"840", "USD", GuiConstants.STRING_DOT, 2
	);

	private String currencyCodeNum_;
	private String currencyCodeStr_;
	private String currencySeparator_;
	private int currencyNumDecimals_;
	
	private GuiCurrency(String currencyCodeNum, String currencyCodeStr, String currencySeparator, int currencyNumDecimals) 
	{
		currencyCodeNum_ = currencyCodeNum;
		currencyCodeStr_ = currencyCodeStr;
		currencySeparator_ = currencySeparator;
		currencyNumDecimals_ = currencyNumDecimals;
	}

	public String getName() 
	{
		switch (this) {
			case EURO:
				return "Euro";
			case GREAT_BRITAIN_POUND:
				return "Great Britain Pound";
			case US_DOLLAR:
				return "US Dollar";
		}
		return "?";
	}

	public String toString()
	{
		return currencyCodeStr_ + " (" + getName() + ")";
	}

	public String getCodeNum() 
	{
		return currencyCodeNum_;
	}

	public String getCodeStr() 
	{
		return currencyCodeStr_;
	}

	public String getSeparator() 
	{
		return currencySeparator_;
	}

	public boolean isSeparatorComma() 
	{
		return getSeparator().equals(GuiConstants.STRING_COMMA);
	}

	public boolean isSeparatorDot() 
	{
		return getSeparator().equals(GuiConstants.STRING_DOT);
	}

	public int getNumDecimals() 
	{
		return currencyNumDecimals_;
	}

	public String buildDefaultAmount()
	{
		StringBuilder defaultAmount = new StringBuilder(GuiConstants.AMOUNT_INPUT_DEFAULT);
		if (getNumDecimals() > 0) {
			defaultAmount.append(getSeparator());
			for (int i = 0; i < getNumDecimals(); i++) {
				defaultAmount.append(GuiConstants.STRING_ZERO);
			}
		}
		return defaultAmount.toString();
	}

	public static GuiCurrency findCurrencyCodeNum(String codeNum)
	{
		for (GuiCurrency gc : GuiCurrency.values()) {
			if (gc.getCodeNum().equals(codeNum)) {
				return gc;
			}
		}
		return null;
	}	

	public static GuiCurrency findCurrencyCodeStr(String codeStr)
	{
		for (GuiCurrency gc : GuiCurrency.values()) {
			if (gc.getCodeStr().equals(codeStr)) {
				return gc;
			}
		}
		return null;
	}	
}
