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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.ingenico.fr.jc3api.JC3ApiConstants;
import com.ingenico.fr.jc3api.JC3ApiParams;
import com.ingenico.fr.jc3api.JC3ApiUtils;

/**
 * Class to handle to GUI settings
 */
public class GuiSettings
{
	protected static final String SETTINGS_GUI_C3AGENT_TYPE = "gui.c3agent.type";
	protected static final String SETTINGS_GUI_C3AGENT_TYPE_DEFAULT = GuiC3AgentType.NET.name();

	protected static final String SETTINGS_GUI_TPVNUMBER = "gui.tpvnumber";
	protected static final String SETTINGS_GUI_TPVNUMBER_DEFAULT = "00008710";

	protected static final String SETTINGS_GUI_CASHIERNUMBER = "gui.cashiernumber";
	protected static final String SETTINGS_GUI_CASHIERNUMBER_DEFAULT = "99999999";

	protected static final String SETTINGS_GUI_FOLDERNUMBER = "gui.foldernumber";
	protected static final String SETTINGS_GUI_FOLDERNUMBER_DEFAULT = "000000010000";

	protected static final String SETTINGS_GUI_AMOUNT = "gui.amount";

	protected static final String SETTINGS_GUI_CURRENCY = "gui.currency";
	protected static final String SETTINGS_GUI_CURRENCY_DEFAULT = GuiCurrency.EURO.getCodeStr();

	protected static final String SETTINGS_GUI_OPERATION = "gui.operation";
	protected static final String SETTINGS_GUI_OPERATION_DEFAULT = GuiOperation.INIT.name();

	private static GuiSettings settings_ = null;

	private GuiSettings()
	{
	}
	
	private static GuiSettings getSettings()
    {
        if (settings_ == null) {
        	settings_ = new GuiSettings();
        }
        return settings_;
    }

	// class used to sort properties when storing them to file
	@SuppressWarnings("serial")
	static class SortedProperties extends Properties
	{
	    @Override
	    public synchronized Enumeration<Object> keys()
	    {
	    	Set<Object> keySet = super.keySet();
	    	TreeSet<Object> sortedSet = new TreeSet<Object>(keySet);

	    	return Collections.enumeration(sortedSet);
	    }
	}

	protected Properties properties_;

	private Properties loadSettings()
	{
		if (properties_ != null) {
			return properties_;
		}

    	properties_ = new SortedProperties();

    	File settingsFile = new File(
    		GuiConstants.WORK_DIR, GuiConstants.GUI_SETTINGS_FILE
    	);
		if (settingsFile.exists()) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(settingsFile);
				properties_.load(fis);
			} catch (IOException e) {
				GuiUtils.showExceptionDialog("Failed to load settings", e);
			} finally {
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
					}
				}
			}
		}

		return properties_;
	}

	private void saveSettings()
	{
		if (properties_ == null || properties_.isEmpty()) {
			return;
		}

		File settingsFile = new File(
			GuiConstants.WORK_DIR, GuiConstants.GUI_SETTINGS_FILE
		);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(settingsFile);
			properties_.store(fos, null);
		} catch (IOException e) {
			GuiUtils.showExceptionDialog("Failed to save settings", e);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public static boolean editSettings()
	{
		for (int i = 0; i < 5; i++) {
			try {
				return editGeneralSettings();
			} catch (IllegalArgumentException e) {
				GuiUtils.showExceptionDialog("INVALID SETTINGS", e);
			}
		}
		
		return false;
	}

	public static boolean editGeneralSettings()
	{
		// present all supported C3 Agent types in combobox
		GuiC3AgentType[] allC3AgentTypes = GuiC3AgentType.values();
		final JComboBox<GuiC3AgentType> guiC3AgentTypeComboBox = new JComboBox<GuiC3AgentType>(allC3AgentTypes);

		// select C3 Agent type from settings in combobox
		GuiC3AgentType guiC3AgentTypeFromSetting = getGuiC3AgentTypeEnum();
		for (GuiC3AgentType guiC3AgentType : allC3AgentTypes) {
			if (guiC3AgentTypeFromSetting == guiC3AgentType) {
				guiC3AgentTypeComboBox.setSelectedItem(guiC3AgentType);
				break;
			}
		}

		// TPV number
		String guiTpvNumberFromSetting = getGuiTpvNumber();
		JTextField guiTpvNumberTextField = new JTextField(guiTpvNumberFromSetting);
		guiTpvNumberTextField.addKeyListener(new GuiUtils.NumericKeyAdapter(guiTpvNumberTextField, 8));

		// Cashier number
		String guiCashierNumberFromSetting = getGuiCashierNumber();
		JTextField guiCashierNumberTextField = new JTextField(guiCashierNumberFromSetting);
		guiCashierNumberTextField.addKeyListener(new GuiUtils.NumericKeyAdapter(guiCashierNumberTextField, 8));

		// Folder number
		String guiFolderNumberFromSetting = getGuiFolderNumber();
		JTextField guiFolderNumberTextField = new JTextField(guiFolderNumberFromSetting);
		guiFolderNumberTextField.addKeyListener(new GuiUtils.NumericKeyAdapter(guiFolderNumberTextField, 12));

		// present all supported currencies in combobox
		GuiCurrency[] allCurrencies = GuiCurrency.values();
		JComboBox<GuiCurrency> guiCurrencyComboBox = new JComboBox<GuiCurrency>(allCurrencies);

		// select currency from settings in combobox
		GuiCurrency guiCurrencyFromSetting = getGuiCurrencyEnum();
		for (GuiCurrency guiCurrency : allCurrencies) {
			if (guiCurrencyFromSetting == guiCurrency) {
				guiCurrencyComboBox.setSelectedItem(guiCurrency);
				break;
			}
		}

		// Amount
		String guiAmountFromSetting = getGuiAmount();
		JTextField guiAmountTextField = new JTextField(guiAmountFromSetting);
		guiAmountTextField.addKeyListener(new GuiUtils.AmountKeyAdapter(guiAmountTextField, 13, guiCurrencyComboBox));

		// LOAD C3 AGENT CONFIG (jc3api-example.properties)
		JButton guiC3AgentSettingsButton = new JButton("C3 AGENT CONFIG");
		// LOAD C3 DRIVER CONFIG (c3config file)
		JButton guiC3DriverSettingsButton = new JButton("C3 DRIVER CONFIG");
		
		// disable buttons by default since we rely on NOTEPAD.EXE
		guiC3AgentSettingsButton.setEnabled(false);
		guiC3DriverSettingsButton.setEnabled(false);

		if (JC3ApiUtils.isOsWindows()) {
			// disable C3 Agent button if C3 Agent config file does not exist
			File c3AgentConfigFile = new File(GuiConstants.WORK_DIR, GuiConstants.C3AGENT_SETTINGS_FILE);
			guiC3AgentSettingsButton.setEnabled(c3AgentConfigFile.exists());
			// add listener to the button if it is enabled
			if (guiC3AgentSettingsButton.isEnabled()) {
				guiC3AgentSettingsButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							JC3ApiUtils.startProcess(
								GuiLogger.getLogger(), 
								"NOTEPAD",
								new String[] {
									"notepad.exe", GuiConstants.C3AGENT_SETTINGS_FILE
								}, 
								GuiConstants.WORK_DIR, 
								null // environment variables
							);
						} catch (Exception ex) {
		   					GuiUtils.showExceptionDialog("Failed to load C3 Agent settings", ex);
						}
					}
				});
			}

			// load C3 Agent parameters from file to get C3 Driver start directory
			try {
				final JC3ApiParams c3AgentParams = new JC3ApiParams(
					GuiConstants.WORK_DIR + JC3ApiConstants.FS + GuiConstants.C3AGENT_SETTINGS_FILE
				);
				// disable C3 Driver button if C3 Driver config file does not exist
				File c3DriverConfigFile = new File(c3AgentParams.getC3NetStartDir(), JC3ApiConstants.C3CONFIG);
				guiC3DriverSettingsButton.setEnabled(c3DriverConfigFile.exists());
				// add listener to the button if it is enabled
				if (guiC3DriverSettingsButton.isEnabled()) {
					guiC3DriverSettingsButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							try {
								JC3ApiUtils.startProcess(
									GuiLogger.getLogger(), 
									"NOTEPAD",
									new String[] {
										"notepad.exe", JC3ApiConstants.C3CONFIG
									},
									// get C3 Driver start directory
									c3AgentParams.getC3NetStartDir(), 
									null // environment variables
								);
							} catch (Exception ex) {
			   					GuiUtils.showExceptionDialog("Failed to load C3 Driver settings", ex);
							}
						}
					});
				}
			} catch (Exception e) {
				GuiUtils.showExceptionDialog("Failed to load C3 Driver settings", e);
			}
		}

		Object[] message = {
			"C3 AGENT TYPE", guiC3AgentTypeComboBox,
			"TPV NUMBER", guiTpvNumberTextField,
			"CASHIER NUMBER", guiCashierNumberTextField,
			"FOLDER NUMBER", guiFolderNumberTextField,
			"AMOUNT", guiAmountTextField,
			"CURRENCY", guiCurrencyComboBox,
			"ADVANCED SETTINGS", guiC3AgentSettingsButton, guiC3DriverSettingsButton
		};

		int option = GuiUtils.showSettingsDialog(message, "GUI SETTINGS");

		boolean settingsChanged = false;
		boolean currencyChanged = false;
		if (option == JOptionPane.OK_OPTION) {
			// currency has changed ?
			currencyChanged = (guiCurrencyComboBox.getSelectedItem() != guiCurrencyFromSetting);
			// save settings if at least one has changed
			if (guiC3AgentTypeComboBox.getSelectedItem() != guiC3AgentTypeFromSetting ||
				!guiTpvNumberTextField.getText().equals(guiTpvNumberFromSetting) ||
				!guiCashierNumberTextField.getText().equals(guiCashierNumberFromSetting) ||
				!guiFolderNumberTextField.getText().equals(guiFolderNumberFromSetting) ||
				!guiAmountTextField.getText().equals(guiAmountFromSetting) ||
				currencyChanged)
			{
				// will throw an exception if the input is invalid
				setGuiC3AgentTypeEnum((GuiC3AgentType) guiC3AgentTypeComboBox.getSelectedItem());
				setGuiTpvNumber(guiTpvNumberTextField.getText());
				setGuiCashierNumber(guiCashierNumberTextField.getText());
				setGuiFolderNumber(guiFolderNumberTextField.getText());
				setGuiAmount(guiAmountTextField.getText());
				setGuiCurrencyEnum((GuiCurrency) guiCurrencyComboBox.getSelectedItem());
				
				// save settings to file
				getSettings().saveSettings();
				
				settingsChanged = true;
			}
		}
		
		return settingsChanged;
	}

	public static String getGuiC3AgentTypeStr()
	{
    	return getSettings().loadSettings().getProperty(SETTINGS_GUI_C3AGENT_TYPE, SETTINGS_GUI_C3AGENT_TYPE_DEFAULT);
	}

	public static GuiC3AgentType getGuiC3AgentTypeEnum()
	{
		return GuiC3AgentType.findC3AgentType(getGuiC3AgentTypeStr());
	}

	private static void setGuiC3AgentTypeStr(String guiC3AgentTypeStr)
	{
		if (guiC3AgentTypeStr == null || GuiC3AgentType.findC3AgentType(guiC3AgentTypeStr) == null) {
			throw new IllegalArgumentException("INVALID C3AGENT TYPE");
		}
		getSettings().loadSettings().setProperty(SETTINGS_GUI_C3AGENT_TYPE, guiC3AgentTypeStr);
	}

	private static void setGuiC3AgentTypeEnum(GuiC3AgentType guiC3AgentType)
	{
		if (guiC3AgentType == null) {
			throw new IllegalArgumentException("INVALID C3AGENT TYPE");
		}
		setGuiC3AgentTypeStr(guiC3AgentType.name());
	}

	public static String getGuiTpvNumber()
	{
    	return getSettings().loadSettings().getProperty(SETTINGS_GUI_TPVNUMBER, SETTINGS_GUI_TPVNUMBER_DEFAULT);
	}

	private static void setGuiTpvNumber(String guiTpvNumber)
	{
		if (guiTpvNumber == null || guiTpvNumber.length() == 0 || guiTpvNumber.length() > 8 || !GuiUtils.isN(guiTpvNumber)) {
			throw new IllegalArgumentException("INVALID TPV NUMBER");
		}
		getSettings().loadSettings().setProperty(SETTINGS_GUI_TPVNUMBER, String.format("%08d", Long.parseLong(guiTpvNumber)));
	}

	public static String getGuiCashierNumber()
	{
    	return getSettings().loadSettings().getProperty(SETTINGS_GUI_CASHIERNUMBER, SETTINGS_GUI_CASHIERNUMBER_DEFAULT);
	}

	private static void setGuiCashierNumber(String guiCashierNumber)
	{
		if (guiCashierNumber == null || guiCashierNumber.length() == 0 || guiCashierNumber.length() > 8 || !GuiUtils.isN(guiCashierNumber)) {
			throw new IllegalArgumentException("INVALID CASHIER NUMBER");
		}
		getSettings().loadSettings().setProperty(SETTINGS_GUI_CASHIERNUMBER, String.format("%08d", Long.parseLong(guiCashierNumber)));
	}

	public static String getGuiFolderNumber()
	{
    	return getSettings().loadSettings().getProperty(SETTINGS_GUI_FOLDERNUMBER, SETTINGS_GUI_FOLDERNUMBER_DEFAULT);
	}

	public static void setGuiFolderNumber(String guiFolderNumber)
	{
		if (guiFolderNumber == null || guiFolderNumber.length() == 0 || guiFolderNumber.length() > 12 || !GuiUtils.isN(guiFolderNumber)) {
			throw new IllegalArgumentException("INVALID FOLDER NUMBER");
		}
		getSettings().loadSettings().setProperty(SETTINGS_GUI_FOLDERNUMBER, String.format("%012d", Long.parseLong(guiFolderNumber)));

    	// save settings to file
		getSettings().saveSettings();
	}

	public static String getGuiAmount()
	{
    	return getSettings().loadSettings().getProperty(SETTINGS_GUI_AMOUNT, getGuiCurrencyEnum().buildDefaultAmount());
	}

	private static void setGuiAmount(String amount)
	{
		if (amount == null || amount.length() == 0 || GuiUtils.amountNumDecimals(amount) > getGuiCurrencyEnum().getNumDecimals()) {
			throw new IllegalArgumentException("INVALID AMOUNT");
		}
		getSettings().loadSettings().setProperty(SETTINGS_GUI_AMOUNT, amount);
	}

	public static String getGuiCurrencyStr()
	{
    	return getSettings().loadSettings().getProperty(SETTINGS_GUI_CURRENCY, SETTINGS_GUI_CURRENCY_DEFAULT);
	}
	
	public static GuiCurrency getGuiCurrencyEnum()
	{
		return GuiCurrency.findCurrencyCodeStr(getGuiCurrencyStr());
	}

	private static void setGuiCurrencyStr(String guiCurrencyStr)
	{
		if (guiCurrencyStr == null || GuiCurrency.findCurrencyCodeStr(guiCurrencyStr) == null) {
			throw new IllegalArgumentException("INVALID CURRENCY");
		}
		getSettings().loadSettings().setProperty(SETTINGS_GUI_CURRENCY, guiCurrencyStr);
	}

	private static void setGuiCurrencyEnum(GuiCurrency guiCurrency)
	{
		if (guiCurrency == null) {
			throw new IllegalArgumentException("INVALID CURRENCY");
		}
		setGuiCurrencyStr(guiCurrency.getCodeStr());
	}

	public static String getGuiOperationStr()
	{
    	return getSettings().loadSettings().getProperty(SETTINGS_GUI_OPERATION, SETTINGS_GUI_OPERATION_DEFAULT);
	}

	public static GuiOperation getGuiOperationEnum()
	{
		return GuiOperation.findOperation(getGuiOperationStr());
	}

	public static void setGuiOperationStr(String guiOperation)
	{
		if (guiOperation == null || GuiOperation.findOperation(guiOperation) == null) {
			throw new IllegalArgumentException("INVALID GUI OPERATION");
		}

		// for this particular method, we force to load the settings from file if not already done
		getSettings().loadSettings().setProperty(SETTINGS_GUI_OPERATION, guiOperation);

    	// save settings to file
		getSettings().saveSettings();
	}

	public static void setGuiOperationEnum(GuiOperation guiOperation)
	{
		if (guiOperation == null) {
			throw new IllegalArgumentException("INVALID GUI OPERATION");
		}
		setGuiOperationStr(guiOperation.name());
	}
}
