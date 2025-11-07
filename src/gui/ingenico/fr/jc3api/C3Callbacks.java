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
package gui.ingenico.fr.jc3api;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.IntBuffer;
import java.nio.LongBuffer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.ingenico.fr.jc3api.JC3ApiConstants;
import com.ingenico.fr.jc3api.JC3ApiInterface;
import com.ingenico.fr.jc3api.JC3ApiUtils;

/**
 * C3 Agent callbacks implementation
 */
public class C3Callbacks extends JC3ApiInterface.JC3ApiCallbacksExt
{
	class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			synchronized (keyCodeLock_) {
				if (e.getSource() == ANN_COR_VAL_[0]) {
					keyCode_ = JC3ApiConstants.C3KEY_CANCELLATION;
				} else if (e.getSource() == ANN_COR_VAL_[1]) {
					keyCode_ = JC3ApiConstants.C3KEY_CORRECTION;
				} else if (e.getSource() == ANN_COR_VAL_[2]) {
					keyCode_ = JC3ApiConstants.C3KEY_VALIDATION;
				} else {
					for (int i = 0; i < NUM_.length; i++) {
						if (e.getSource() == NUM_[i]) {
							keyCode_ = JC3ApiConstants.C3KEY_NUMERIC_0 + i;
						}
					}
				}
			}
		}
	}

	protected JTextField posDisplay_;
	protected int keyCode_;
	protected Object keyCodeLock_;
	protected JButton[] NUM_;
	protected JButton[] ANN_COR_VAL_;
	protected boolean printTicketFile_;

	private static C3Callbacks c3Callbacks_ = null;

	private C3Callbacks(JTextField posDisplay, JButton[] NUM, JButton[] ANN_COR_VAL)
	{
		posDisplay_ = posDisplay;
		keyCode_ = JC3ApiConstants.C3KEY_NOKEY;
		keyCodeLock_ = new Object();
		printTicketFile_ = false;

		NUM_ = NUM;
		for (int i = 0; i < NUM_.length; i++) {
			NUM_[i].addActionListener(new ButtonListener());
		}

		ANN_COR_VAL_ = ANN_COR_VAL;
		for (int i = 0; i < ANN_COR_VAL_.length; i++) {
			ANN_COR_VAL_[i].addActionListener(new ButtonListener());
		}

		disableAllButtons();
	}

	public static C3Callbacks getC3Callbacks(JTextField posDisplay, JButton[] NUM, JButton[] ANN_COR_VAL)
    {
        if (c3Callbacks_ == null) {
        	c3Callbacks_ = new C3Callbacks(posDisplay, NUM, ANN_COR_VAL);
        }
        return c3Callbacks_;
    }

	public boolean getPrintTicketFile()
	{
		return printTicketFile_;
	}

	public void setPrintTicketFile(boolean printTicketFile)
	{
		printTicketFile_ = printTicketFile;
	}

	protected void disableANNButton()
	{
		ANN_COR_VAL_[0].setEnabled(false);
	}

	protected void disableCORButton()
	{
		ANN_COR_VAL_[1].setEnabled(false);
	}

	protected void disableVALButton()
	{
		ANN_COR_VAL_[2].setEnabled(false);
	}

	protected void disableNUMButton()
	{
		for (int i = 0; i < NUM_.length; i++) {
			NUM_[i].setEnabled(false);
		}
	}

	protected void disableAllButtons()
	{
		disableANNButton();
		disableCORButton();
		disableVALButton();
		disableNUMButton();
	}
	
	protected void enableANNButton()
	{
		ANN_COR_VAL_[0].setEnabled(true);
	}

	protected void enableCORButton()
	{
		ANN_COR_VAL_[1].setEnabled(true);
	}

	protected void enableVALButton()
	{
		ANN_COR_VAL_[2].setEnabled(true);
	}

	protected void enableNUMButton()
	{
		for (int i = 0; i < NUM_.length; i++) {
			NUM_[i].setEnabled(true);
		}
	}

	protected void enableAllButtons()
	{
		enableANNButton();
		enableCORButton();
		enableVALButton();
		enableNUMButton();
	}

	/**
	 * @see JC3ApiInterface.JC3ApiCallbacks#display(String, int)
	 */
	public void display(String msg, int mode)
	{
		GuiLogger.logInfo(String.format("POS DISPLAY [%s] '%s'", JC3ApiConstants.C3DisplayModes.getInfo(mode), msg));

		posDisplay_.setText(msg);

		disableAllButtons();
		
		switch (mode) {
			case JC3ApiConstants.C3DSP_WAIT_NONE:
			break;
			
			case JC3ApiConstants.C3DSP_WAIT_KEY:
				enableAllButtons();
				while (!keyAvailable()) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
				}
				getKey();
			break;
			
			case JC3ApiConstants.C3DSP_WAIT_1SEC:
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			break;
			
			case JC3ApiConstants.C3DSP_WAIT_NUM:
				enableNUMButton();
			break;
			
			case JC3ApiConstants.C3DSP_BREAKABLE:
			case JC3ApiConstants.C3DSP_WAIT_ANN:
				enableANNButton();
			break;

			case JC3ApiConstants.C3DSP_WAIT_COR:
				enableCORButton();
			break;

			case JC3ApiConstants.C3DSP_WAIT_VAL:
				enableVALButton();
			break;

			case JC3ApiConstants.C3DSP_WAIT_VAL_COR:
				enableCORButton();
				enableVALButton();
			break;

			case JC3ApiConstants.C3DSP_WAIT_VAL_ANN:
				enableANNButton();
				enableVALButton();
			break;
			
			case JC3ApiConstants.C3DSP_WAIT_VAL_ANN_COR:
				enableANNButton();
				enableCORButton();
				enableVALButton();
			break;

			default:
			break;
		}
	}

	/**
	 * @see JC3ApiInterface.JC3ApiCallbacks#keyAvailable()
	 */
	public boolean keyAvailable()
	{
		synchronized (keyCodeLock_) {
			return (keyCode_ != JC3ApiConstants.C3KEY_NOKEY);
		}
	}

	/**
	 * @see JC3ApiInterface.JC3ApiCallbacks#getKey()
	 */
	public int getKey()
	{
		synchronized (keyCodeLock_) {
			int keyCode = keyCode_;

			keyCode_ = JC3ApiConstants.C3KEY_NOKEY;
			disableAllButtons();

			GuiLogger.logInfo(String.format("POS GET KEY [%s]", JC3ApiConstants.C3Keys.getInfo(keyCode)));

			return keyCode;
		}
	}

	/**
	 * @see JC3ApiInterface.JC3ApiCallbacks#getString(StringBuffer, int, String)
	 */
	public int getString(StringBuffer str, int maxlen, String msg)
	{
		GuiLogger.logInfo(String.format("POS GET STRING '%s' (maxlen = %d)", msg, maxlen));

		String inputValue = GuiUtils.showInputDialog(msg, "GET STRING", null, null);
		
		if (inputValue == null) {
			return JC3ApiConstants.C3KEY_CANCELLATION;
		}

		str.setLength(0);
		str.append(inputValue);

		return JC3ApiConstants.C3KEY_VALIDATION;
	}

	/**
	 * @see JC3ApiInterface.JC3ApiCallbacks#getSecurity()
	 */
	public int getSecurity()
	{
		GuiLogger.logInfo("POS GET SECURITY");

		int option = GuiUtils.showConfirmDialog("SUPERVISOR VALIDATION ?", "GET SECURITY");
		
		return (option == JOptionPane.OK_OPTION) ? JC3ApiConstants.C3KEY_VALIDATION : JC3ApiConstants.C3KEY_CANCELLATION;
	}

	/**
	 * @see JC3ApiInterface.JC3ApiCallbacks#getSalesConfirmation()
	 */
	public int getSalesConfirmation()
	{
		GuiLogger.logInfo("POS GET SALES CONFIRMATION");

		int option = GuiUtils.showConfirmDialog("GOODS DELIVERED ?", "GET SALES CONFIRMATION");
		
		return (option == JOptionPane.OK_OPTION) ? JC3ApiConstants.C3KEY_VALIDATION : JC3ApiConstants.C3KEY_CANCELLATION;
	}

	/**
	 * @see JC3ApiInterface.JC3ApiCallbacks#printTicket(String)
	 */
	public void printTicket(String ticket)
	{
		GuiLogger.logInfo("POS PRINT TICKET");

		Reader reader = null;
		
		// ticket parameter contains the path to the ticket
		if (printTicketFile_) {
			File file = new File(ticket);
			if (file.exists()) {
				try {
					InputStream is = new FileInputStream(file);
					reader = new InputStreamReader(is, JC3ApiConstants.CHARSET_JC3API);
				} catch (IOException e) {
				}
			}
		// ticket parameter contains the ticket contents as a buffer
		} else {
			reader = new StringReader(ticket);
		}

		// failed to initialize reader
		if (reader == null) {
			return;
		}
		
		// log ticket lines
		BufferedReader bReader = null;
		StringBuilder sb = new StringBuilder();
		try {
			bReader = new BufferedReader(reader);
			String line;
			sb.append("========================" + JC3ApiConstants.LS);
			while ((line = bReader.readLine()) != null) {
				sb.append(line + JC3ApiConstants.LS);
			}
			sb.append("========================");
		} catch (IOException e) {
		} finally {
			if (bReader != null) {
				try {
					bReader.close();
				} catch (IOException e) {
				}
			}
		}
		
		GuiUtils.showTicketDialog("PRINT TICKET", sb.toString());
	}

	/**
	 * @see JC3ApiInterface.JC3ApiCallbacksExt#getMenu(IntBuffer, String, String, int[], String[])
	 */
	@Override
	public int getMenu(IntBuffer choice, String app, String title, int[] entries, String[] labels)
	{
		GuiLogger.logInfo("POS GET MENU");

		String labelSelected = GuiUtils.showInputDialog(title, "GET MENU", labels, labels[0]);

		if (labelSelected == null) {
			return JC3ApiConstants.C3KEY_CANCELLATION;
		}
		
		for (int i = 0; i < labels.length; i++) {
			if (labelSelected.equals(labels[i])) {
				choice.put(0, entries[i]);
				break;
			}
		}
		
		return JC3ApiConstants.C3KEY_VALIDATION;
	}

	/**
	 * @see JC3ApiInterface.JC3ApiCallbacksExt#getCashback(LongBuffer, long)
	 */
	@Override
	public int getCashback(LongBuffer cashback, long maxCashback)
	{
		GuiLogger.logInfo(String.format("POS GET CASHBACK (max = %d", maxCashback));

		String inputValue;
		do {
			cashback.put(0, -1L);

			inputValue = GuiUtils.showInputDialog(String.format("Cashback ? (max = %d)", maxCashback), "GET CASHBACK", null, null);

			if (inputValue == null) {
				return JC3ApiConstants.C3KEY_CANCELLATION;
			}

			// remove "." and "," decimal separators
			inputValue = inputValue.replaceAll(",", "");
			inputValue = inputValue.replaceAll("\\.", "");

		// loop until the cashback amount is valid	
		} while (!JC3ApiUtils.isN(inputValue) || Long.parseLong(inputValue) > maxCashback);

		cashback.put(0, Long.parseLong(inputValue));

		return JC3ApiConstants.C3KEY_VALIDATION;
	}

	/**
	 * @see JC3ApiInterface.JC3ApiCallbacksExt#displayImage(byte[], String)
	 */
	@Override
	public void displayImage(byte[] img, String imgType)
	{
		GuiLogger.logInfo("POS DISPLAY IMAGE");

		ByteArrayInputStream bais = null;
		try {
			bais = new ByteArrayInputStream(img);
			BufferedImage bufferedImage = ImageIO.read(bais);
			GuiUtils.showImageDialog("DISPLAY IMAGE", new ImageIcon(bufferedImage));
		} catch (IOException e) {
		} finally {
			if (bais != null) {
				try {
					bais.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * @see JC3ApiInterface.JC3ApiCallbacksExt#clearScreen()
	 */
	@Override
	public void clearScreen()
	{
		GuiLogger.logInfo("POS CLEAR SCREEN");
	}

	/**
	 * @see JC3ApiInterface.JC3ApiCallbacksExt#barcodeEvent(String, C3BarcodeSymbologies)
	 */
	@Override
	public void barcodeEvent(String barcode, JC3ApiConstants.C3BarcodeSymbologies symbology) 
	{
		GuiLogger.logInfo("POS BARCODE EVENT : " + barcode + "/" + symbology.getSymbology());

		posDisplay_.setText(barcode + "/" + symbology.getSymbology());
	}
}
