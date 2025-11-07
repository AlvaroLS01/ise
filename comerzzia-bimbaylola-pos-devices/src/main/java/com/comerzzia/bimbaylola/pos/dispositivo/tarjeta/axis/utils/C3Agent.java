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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import com.ingenico.fr.jc3api.JC3ApiC3Rspn;
import com.ingenico.fr.jc3api.JC3ApiConstants;
import com.ingenico.fr.jc3api.JC3ApiConstants.C3TenderTypes;
import com.ingenico.fr.jc3api.JC3ApiInterface;
import com.ingenico.fr.jc3api.JC3ApiInterfaceNet;
import com.ingenico.fr.jc3api.JC3ApiInterfaceSim;
import com.ingenico.fr.jc3api.JC3ApiParams;
import com.ingenico.fr.jc3api.JC3ApiUtils;
import com.ingenico.fr.jc3api.json.JsonEnums;
import com.ingenico.fr.jc3api.json.JsonSignBoxSettings;
import com.ingenico.fr.jc3api.pclapi.PclApiBcrListener;
import com.ingenico.fr.jc3api.pclapi.PclApiBcrSettings;

/**
 * C3 Agent integration - operation to be run in a background thread
 */
public class C3Agent
{
	protected JC3ApiInterface c3AgentSim_;
	protected JC3ApiInterface c3AgentNet_;
	protected JC3ApiParams c3AgentParams_;
	protected boolean c3AgentParamsReload_;
	protected Properties c3AgentProperties_;
	protected C3Callbacks c3AgentCallbacks_;

	private static C3Agent c3Agent_ = null;

	private C3Agent(C3Callbacks c3AgentCallbacks)
	{
		c3AgentCallbacks_ = c3AgentCallbacks;
	}
	
	public static C3Agent getC3Agent(C3Callbacks c3AgentCallbacks)
    {
        if (c3Agent_ == null || c3Agent_.c3AgentCallbacks_ != c3AgentCallbacks) {
        	c3Agent_ = new C3Agent(c3AgentCallbacks);
        }
        return c3Agent_;
    }
	
	protected C3Callbacks getCallbacks()
	{
		return c3AgentCallbacks_;
	}
	
	protected JC3ApiParams getParams() throws IOException
	{
		return getParamsFromFile();
		//return getParamsFromMemory();
	}

	// Load C3 Agent parameters from file
	protected void loadParamsFromFile() throws IOException
    {
		if (c3AgentProperties_ == null) {
			c3AgentProperties_ = new Properties();
		} else if (c3AgentParamsReload_) {
			c3AgentProperties_.clear();
			c3AgentParamsReload_ = false;
		} else {
			return; // no need to reload parameters
		}

		// load JC3Api parameters from configuration file
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(GuiConstants.C3AGENT_SETTINGS_FILE);
			c3AgentProperties_.load(fis);
		} finally {
			if (fis != null) {
				fis.close();
			}
		}
    }

	// Configure the C3 Agent from a file
	protected JC3ApiParams getParamsFromFile() throws IOException
    {
		// first, load parameters from file
		loadParamsFromFile();

		// create C3 Agent parameters
		if (c3AgentParams_ == null) {
			c3AgentParams_ = new JC3ApiParams(c3AgentProperties_);
		}
		return c3AgentParams_;
    }

	// Configure the C3 Agent directly by the properties of the class
	protected JC3ApiParams getParamsFromMemory()
    {
		// create C3 Agent parameters
		if (c3AgentParams_ == null) {
			c3AgentParams_ = new JC3ApiParams();
			c3AgentParams_.setC3ApiExtended(true);
			c3AgentParams_.setPclMethodsToC3Api(true);
			c3AgentParams_.setC3NetPrintTicketFile(false);
			c3AgentParams_.setC3NetAddress("127.0.0.1:9518");
			// windows
			if (JC3ApiUtils.isOsWindows()) {
				c3AgentParams_.setC3NetPathBin("C:\\Program Files\\Ingenico\\C3Driver\\bin");
				c3AgentParams_.setC3NetStartDir("C:\\Program Files\\Ingenico\\C3Driver\\bin");
			// linux : path to lib directory must be declared
			} else if (JC3ApiUtils.isOsLinux()) {
				c3AgentParams_.setC3NetPathBin("/opt/ingenico/c3driver/bin");
				c3AgentParams_.setC3NetPathLib("/opt/ingenico/c3driver/lib");
				c3AgentParams_.setC3NetStartDir("/home/user");
			} else {
				// ...
			}
			c3AgentParams_.setC3NetStartBefore(true);
			c3AgentParams_.setC3NetStopAfter(false);
			c3AgentParams_.setLog4jLevel("DEBUG");
		}		
		return c3AgentParams_;
    }
	
	/**
	 * C3 Agent for simulator
	 */
	protected JC3ApiInterface getC3AgentSim() throws IOException
    {
		if (c3AgentSim_ == null) {
			c3AgentSim_ = new JC3ApiInterfaceSim(getCallbacks(), getParams(), GuiLogger.getLogger());
		}
		return c3AgentSim_;
    }

	/**
	 * C3 Agent for C3 Driver NET
	 */
	protected JC3ApiInterface getC3AgentNet() throws IOException
    {
		if (c3AgentNet_ == null) {
			c3AgentNet_ = new JC3ApiInterfaceNet(getCallbacks(), getParams(), GuiLogger.getLogger());
		}
		return c3AgentNet_;
    }

	/**
	 * Run the given C3 operation with C3 Agent
	 * 
	 * @param guiOperation
	 * @return the C3 response
	 * @throws IOException
	 */
	public JC3ApiC3Rspn runC3Operation(GuiOperation guiOperation) throws IOException
	{
		// reload the config file in case it was updated by the user
		c3AgentParamsReload_ = true;

		loadParamsFromFile();

		// select C3 Agent : simulator / real
		JC3ApiInterface c3Agent = null;
		// C3 simulator
		if (GuiSettings.getGuiC3AgentTypeEnum() == GuiC3AgentType.SIM) {
			c3Agent = getC3AgentSim();
			// simulator is providing a file path in printTicket(String ticket) callback
			getCallbacks().setPrintTicketFile(true);
		// C3 NET
		} else if (GuiSettings.getGuiC3AgentTypeEnum() == GuiC3AgentType.NET) {
			c3Agent = getC3AgentNet();
			// synchronize C3 callbacks with C3NET print ticket parameter
			getCallbacks().setPrintTicketFile(getParams().isC3NetPrintTicketFile());
		} else {
			// ???
			return null;
		}
		
		// GET TPV and CASHIER numbers and run C3 operation
		String tpvNumber = GuiSettings.getGuiTpvNumber();
		String cashierNumber = GuiSettings.getGuiCashierNumber();

		JC3ApiC3Rspn c3rspn = null;
		switch (guiOperation) {
			case INIT: {
				c3rspn = c3Agent.processC3Init(tpvNumber);
				// check for partial init
				if (c3rspn != null && c3rspn.getcC3ErrorInt() == 0) {
					if (c3rspn.getcUserData1().equals(c3rspn.getcUserData2())) {
						// init OK
					} else {
						// init partial
					}
				}
			}
			break;

			case VERSION: {
				// do not duplicate admin tickets
   				c3Agent.setCopyAdminCustomerTicketToMerchantTicket(false);
				c3rspn = c3Agent.processC3Version(tpvNumber);
			}
			break;

			case SIGNBOX : {
				int inactivityTimeout = 60;
				boolean returnToIdle = true;
				JsonSignBoxSettings jsonSignBoxSettings = new JsonSignBoxSettings();
				jsonSignBoxSettings.setScreenType(JsonEnums.SignBoxScreenTypes.DEFAULT);
				// Image file format. By default bitmap BMP
				jsonSignBoxSettings.setFileFormat(JsonEnums.SignBoxFileFormats.BMP);
				// If present then image file is compressed with compressed mode specified
				jsonSignBoxSettings.setEncoding(JsonEnums.SignBoxEncodings.GZIP);
				// en, es, fr : 3 merchant languages present by default in the terminal
				jsonSignBoxSettings.setLanguage(JsonEnums.SignBoxLanguages.EN);
				// First line of text at the top of the screen. If missing default text is displayed
				jsonSignBoxSettings.setText("Please sign in the area below:");
				// If true then cashier must acknowledge the signature (terminal waiting the ECR)
				jsonSignBoxSettings.setCheck(true);
				c3rspn = c3Agent.processC3SignBox(tpvNumber, inactivityTimeout, returnToIdle, jsonSignBoxSettings);
			}
			break;

			case PING: {
				c3rspn = c3Agent.processC3PingAxis(tpvNumber);
			}
			break;

			case AUTOTEST: {
				c3rspn = c3Agent.processC3AutoTest(tpvNumber);
			}
			break;
			
			case PURCHASE:
			case PURCHASE_CASHBACK:
			case REFUND:
			case CANCEL_BY_CARD:
			case CANCEL_BY_REF:
			case CANCEL_LAST: {
				GuiCurrency currencyEnum = GuiSettings.getGuiCurrencyEnum();
				String amountStr = GuiSettings.getGuiAmount();
				long amount = GuiUtils.amountStr2Long(amountStr, currencyEnum);
				// unique SALE transaction ID to be incremented after a successful EFT transaction
				String saleTransactionID = "ECR-000001";
				// PURCHASE
				if (guiOperation == GuiOperation.PURCHASE) {
					c3rspn = c3Agent.processC3Debit(
						tpvNumber, cashierNumber, amount, currencyEnum.getCodeNum(), saleTransactionID, null
					);
				// PURCHASE WITH CASHBACK
				} else if (guiOperation == GuiOperation.PURCHASE_CASHBACK) {
					c3rspn = c3Agent.processC3DebitCashback(
						tpvNumber, cashierNumber, amount, currencyEnum.getCodeNum(), saleTransactionID, null
					);
				// REFUND
				} else if (guiOperation == GuiOperation.REFUND) {
					c3rspn = c3Agent.processC3Refund(
						tpvNumber, cashierNumber, amount, currencyEnum.getCodeNum(), saleTransactionID, null
					);
				// CANCEL BY CARD
				// CANCEL LAST
				} else if (guiOperation == GuiOperation.CANCEL_BY_CARD || guiOperation == GuiOperation.CANCEL_LAST) {
					c3Agent.setCancellationLast(guiOperation == GuiOperation.CANCEL_LAST);
					c3rspn = c3Agent.processC3Cancellation(
						tpvNumber, cashierNumber, amount, currencyEnum.getCodeNum(), saleTransactionID, null
					);
					c3Agent.setCancellationLast(false);
				// CANCEL BY REFERENCE
				} else if (guiOperation == GuiOperation.CANCEL_BY_REF) {
					String folderNumber = GuiSettings.getGuiFolderNumber();
					c3rspn = c3Agent.processC3CancellationWithRef(
						C3TenderTypes.C3_TENDERTYPE_CPA.getTenderType(),
						tpvNumber, cashierNumber, folderNumber, saleTransactionID, null
					);
				}
				if (c3rspn != null && c3rspn.getcC3ErrorInt() == 0) {
					// increment unique SALE transaction ID
					
					// save folder number
					if (GuiUtils.isN(c3rspn.getcNumDossier())) {
						GuiSettings.setGuiFolderNumber(c3rspn.getcNumDossier());
					}
				}
			}
			break;

			case BARCODE_OPEN: {
				int bcrInactivityTimeout = 60;	// 60s
				PclApiBcrSettings bcrSettings = getSettingsBarcodeReader();
				//
				// Two different integration methods for barcode reader:
				// 1) legacy method through PCL with PclApiBcrListener and API `public void onBarCodeReceived(byte[] barcode, int symbology, String symbologyLabel)'
				// 2) new recommended method through C3API with JC3ApiInterface.JC3ApiCallbacksExt and API `public void barcodeEvent(String barcode, C3BarcodeSymbologies symbology)'
				//    to enable the new method : set bcrListener = null + activate C3 Agent parameter `c3AgentParams_.setPclMethodsToC3Api(true)'
				PclApiBcrListener bcrListener = getParams().isPclMethodsToC3Api() ? null : new PclApiBcrListenerImpl();
				c3rspn = boolean2C3Rspn(c3Agent.processC3BcrOpen(tpvNumber, bcrInactivityTimeout * 100, bcrSettings, bcrListener));
			}
			break;

			case BARCODE_CLOSE: {
				c3rspn = boolean2C3Rspn(c3Agent.processC3BcrClose(tpvNumber));
			}
			break;
		}
		
		return c3rspn;
	}

	/**
	 * Convert boolean to C3 response object
	 * @param result
	 * @return
	 */
	protected static JC3ApiC3Rspn boolean2C3Rspn(boolean result)
	{
		JC3ApiC3Rspn c3rspn = new JC3ApiC3Rspn();
		c3rspn.setcC3ErrorStr(result ? "0000" : "0311");
		return c3rspn;
	}

	/**
	 * Barcode reader settings
	 */
	protected static PclApiBcrSettings getSettingsBarcodeReader()
	{
		// symbologies
        PclApiBcrSettings.Symbologies[] symbologiesArray = new PclApiBcrSettings.Symbologies[] {
        	PclApiBcrSettings.Symbologies.SYMBOLOGY_EAN8,
        	PclApiBcrSettings.Symbologies.SYMBOLOGY_EAN13,
        	PclApiBcrSettings.Symbologies.SYMBOLOGY_CODE128,
        	PclApiBcrSettings.Symbologies.SYMBOLOGY_QRCODE,
        	PclApiBcrSettings.Symbologies.SYMBOLOGY_AZTEC
        };

		// scan mode
        PclApiBcrSettings.ScanModes scanMode = 
        	PclApiBcrSettings.ScanModes.SCAN_MODE_MULTI_SCAN;

        // good scan beep
        PclApiBcrSettings.GoodScanBeeps goodScanBeep = 
        	PclApiBcrSettings.GoodScanBeeps.GOOD_SCAN_BEEP_ONE_BEEP;
        
        // imager mode
        PclApiBcrSettings.ImagerModes imagerMode = 
        	PclApiBcrSettings.ImagerModes.IMAGER_MODE_1D2D;

        // lighting mode
        PclApiBcrSettings.LightingModes lightingMode = 
        	PclApiBcrSettings.LightingModes.LIGHTING_MODE_ILLUMINATION_PRIORITY;

        // illumination mode
        PclApiBcrSettings.IlluminationModes illuminationMode = 
        	PclApiBcrSettings.IlluminationModes.ILLUMINATION_MODE_AIMER_AND_LEDS;

        // enable trigger
        boolean enableTrigger = true;
        // beep frequency = 1000 Hz
        int beepFrequency = 1000;
        // beep duration = 100 ms
        int beepDuration = 100;

        // create PCL BCR settings instance
        PclApiBcrSettings bcrSettings = new PclApiBcrSettings(
        	symbologiesArray, scanMode, goodScanBeep, imagerMode, lightingMode, illuminationMode, 
        	enableTrigger, beepFrequency, beepDuration
        );

        return bcrSettings;
	}

	/**
	 * Listener for PCL barcode events
	 */
	class PclApiBcrListenerImpl implements PclApiBcrListener
	{
		/**
		 * The barcode reader received barcode data
		 * 
		 * @param barcode the barcode data
		 * @param symbology the barcode symbology
		 * @param symbologyLabel the barcode symbology text representation
		 */
		public void onBarCodeReceived(byte[] barcode, int symbology, String symbologyLabel)
		{
			String barcodeStr;
			try {
				barcodeStr = JC3ApiUtils.bytes2String(barcode).trim();
			} catch (UnsupportedEncodingException e) {
				GuiLogger.logException("Failed to decode barcode data", e);
				return;
			}
			
			getCallbacks().display(barcodeStr + "/" + symbologyLabel, JC3ApiConstants.C3DSP_WAIT_NONE);
		}

		/**
		 * The barcode reader was closed due to an inactivity timeout
		 */
		public void onBarCodeClosed()
		{
			getCallbacks().display("BARCODE READER CLOSED", JC3ApiConstants.C3DSP_WAIT_1SEC);
		}
	};
};
