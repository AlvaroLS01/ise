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
package gui.ingenico.fr.jc3api;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.ingenico.fr.jc3api.JC3ApiConstants;

public class GuiUtils
{
	/**
	 * Convert amount string to cents
	 *   0.01 => 1
	 *   0,02 => 2
	 *   0,1  => 10
	 *   10   => 1000
	 */
	public static long amountStr2Long(String amount, GuiCurrency currency)
    {
		// 0,1 => 10 conversion
		// 10  => 1000 conversion
		int numOfDecimals = amountNumDecimals(amount);
		while (numOfDecimals++ < currency.getNumDecimals()) {
			amount += GuiConstants.STRING_ZERO;
		}

		amount = amount.replaceAll(GuiConstants.STRING_COMMA, GuiConstants.STRING_EMPTY);
		amount = amount.replaceAll("\\" + GuiConstants.STRING_DOT, GuiConstants.STRING_EMPTY);
		while (amount.startsWith(GuiConstants.STRING_ZERO) && amount.length() > 1) {
			amount = amount.substring(1);
		}
		
		return Long.parseLong(amount);
    }

	/**
	 * Return number of decimals for the given amount
	 *   101  => 0
	 *   0,02 => 2
	 *   0,1  => 1
	 *   0.03 => 2
	 */
	public static int amountNumDecimals(String amount)
    {
		int idx = amount.lastIndexOf(GuiConstants.STRING_COMMA);
		if (idx == -1) {
			idx = amount.lastIndexOf(GuiConstants.STRING_DOT);
		}

		return (idx != -1) ? amount.substring(idx + 1).length() : 0;
    }

	/**
	 * Check the given string is numeric
	 */
	public static boolean isN(String s)
	{
		return s.matches("^[\\p{Digit}]+$");
	}

    /**
     * Load the given icon from JAR file or working directory
     */
    public static URL loadIcon(String iconName)
    {
	    // try to get icon from JAR file
	    URL imageURL = GuiUtils.class.getResource("/" + iconName);
	    if (imageURL == null) {
	    	try {
	    		// try to get icon file from local icons/ directory
				imageURL = new File(
					GuiConstants.WORK_DIR + JC3ApiConstants.FS + GuiConstants.ICONS_DIR, iconName
				).toURI().toURL();
			} catch (MalformedURLException e) {
				GuiLogger.logException("Failed to load icon file", e);
			}
	    }
	    
	    return imageURL;
    }

	/**
	 * Check input is numeric and not exceeding maximum length
	 */
	public static class NumericKeyAdapter extends KeyAdapter
	{
		JTextField textField_;
		int textFieldMaxSize_;

		public NumericKeyAdapter(JTextField textField, int textFieldMaxSize)
		{
			textField_ = textField;
			textFieldMaxSize_ = textFieldMaxSize;
		}
        
		public void keyTyped(KeyEvent e)
        {
            if (textField_.getText().length() >= textFieldMaxSize_ && 
            	textField_.getSelectedText() == null) {
        		e.consume();
            } else {
                char c = e.getKeyChar();
	            if (!Character.isDigit(c)) {
	                e.consume();
	            }
            }
        }
    }

	/**
	 * Check input corresponds to an amount
	 */
	public static class AmountKeyAdapter extends KeyAdapter
	{
		JTextField textField_;
		int textFieldMaxSize_;
		JComboBox<GuiCurrency> currencyComboBox_; 

		public AmountKeyAdapter(JTextField textField, int textFieldMaxSize, JComboBox<GuiCurrency> currencyComboBox)
		{
			textField_ = textField;
			textFieldMaxSize_ = textFieldMaxSize;
			currencyComboBox_ = currencyComboBox;
		}
        
		private int getNumOfChars(char c)
		{
			int cnt = 0;
			for (int i = 0; i < textField_.getText().length(); i++) {
				if (textField_.getText().charAt(i) == c) {
					cnt++;
				}
			}
			return cnt;
		}

		private int notZeroPosition()
	    {
			for (int i = 0; i < textField_.getText().length(); i++) {
				if (textField_.getText().charAt(i) != GuiConstants.CHAR_ZERO) {
					return i;
				}
			}
			return -1;
	    }
		
		private boolean isSeparatorAt(int index)
		{
			return  (index < textField_.getText().length()) && 
					(textField_.getText().charAt(index) == GuiConstants.CHAR_DOT ||
				 	 textField_.getText().charAt(index) == GuiConstants.CHAR_COMMA);
		}

		public void keyTyped(KeyEvent e)
        {
			if (textField_.getText().length() >= textFieldMaxSize_ &&
            	textField_.getSelectedText() == null) {
        		e.consume();
        		return;
			}

			GuiCurrency guiCurrency = (GuiCurrency) currencyComboBox_.getSelectedItem();
            char c = e.getKeyChar();
            
            // prevent from entering more than one separator
            if (!Character.isDigit(c)) {
            	if (guiCurrency.isSeparatorComma() && c == GuiConstants.CHAR_COMMA && getNumOfChars(GuiConstants.CHAR_COMMA) == 0) {
            		// accept character
            	} else if (guiCurrency.isSeparatorDot() && c == GuiConstants.CHAR_DOT && getNumOfChars(GuiConstants.CHAR_DOT) == 0) {
            		// accept character
            	} else {
            		// character is not allowed
            		e.consume();
            	}
            	return;
            }

            // prevent from entering more than one leading zero
        	if (c == GuiConstants.CHAR_ZERO && textField_.getSelectedText() == null && textField_.getText().length() > 0) {
        		// look for first non-zero character
            	int notZeroPosition = notZeroPosition();
        		if (notZeroPosition == -1 || textField_.getCaretPosition() <= notZeroPosition) {
        			if (textField_.getCaretPosition() == 0 && isSeparatorAt(0)) {
        				// allow to enter one leading zero if text is for example : ",23"
        			} else {
        				// forbid to add a leading zero to "0,23", "10", etc....
	            		e.consume();
	            		return;
        			}
        		}
        	}

            // prevent from entering too many decimals after separator
            if (textField_.getSelectedText() == null) {
    			int numOfDecimals = amountNumDecimals(textField_.getText());
        		// already reached the maximum number of decimals for the currency ?
        		if (numOfDecimals == guiCurrency.getNumDecimals()) {
            		int sepPosition = -1;
            		if (guiCurrency.isSeparatorComma() && getNumOfChars(GuiConstants.CHAR_COMMA) > 0) {
            			sepPosition = textField_.getText().indexOf(GuiConstants.CHAR_COMMA);
            		} else if (guiCurrency.isSeparatorDot() && getNumOfChars(GuiConstants.CHAR_DOT) > 0) {
            			sepPosition = textField_.getText().indexOf(GuiConstants.CHAR_DOT);
            		}
            		// do not allow input if the caret is after the separator
            		if (sepPosition != -1 && textField_.getCaretPosition() > sepPosition) {
            			e.consume();
            		}
        		}
            }
        }
    }

	// change labels for OK and CANCEL buttons
	static {
		UIManager.put(GuiConstants.OPTIONPANE_OKBUTTON_TEXT, GuiConstants.BUTTON_OK_TEXT);
		UIManager.put(GuiConstants.OPTIONPANE_CANCELBUTTON_TEXT, GuiConstants.BUTTON_CANCEL_TEXT);
	}

	public static void showTicketDialog(String title, String ticket)
	{
		JTextArea textArea = new JTextArea(ticket);
		textArea.setFont(GuiConstants.FONT_COURIER_PLAIN_12);

		JOptionPane.showMessageDialog(
			null, textArea, title, JOptionPane.PLAIN_MESSAGE
		);
	}

	public static void showTicketsDialog(String title, String ticket1, String title1, String ticket2, String title2)
	{
		if (ticket1 == null && ticket2 == null) {
			return;
		}
		
		JTabbedPane tabbedPane = new JTabbedPane();
		if (ticket1 != null) {
			JTextArea textArea1 = new JTextArea(ticket1);
			textArea1.setFont(GuiConstants.FONT_COURIER_PLAIN_12);
			tabbedPane.addTab(title1, textArea1);
		}
		if (ticket2 != null) {
			JTextArea textArea2 = new JTextArea(ticket2);
			textArea2.setFont(GuiConstants.FONT_COURIER_PLAIN_12);
			tabbedPane.addTab(title2, textArea2);
		}
		
		JOptionPane.showMessageDialog(
			null, tabbedPane, title, JOptionPane.PLAIN_MESSAGE
		);
	}

	public static void showImageDialog(String title, Icon image)
	{
		JLabel label = new JLabel(image);

		JOptionPane.showMessageDialog(
			null, label, title, JOptionPane.PLAIN_MESSAGE
		);
	}

	public static void showMessageDialog(Object message, String title, int messageType)
	{
		JOptionPane.showMessageDialog(
			null, message, title, messageType
		);
	}

	public static void showExceptionDialog(String error, Throwable t)
	{
		GuiLogger.logException(error, t);
		GuiUtils.showMessageDialog(
			t.getMessage(), error, JOptionPane.ERROR_MESSAGE
		);
	}

	public static void showMessageDialogIngenico(Object message, String title, int messageType)
	{
        URL imageURL = GuiUtils.loadIcon(GuiConstants.ICON_INGENICO);
		JOptionPane.showMessageDialog(
			null, message, title, messageType, new ImageIcon(imageURL)
		);
	}

	public static int showConfirmDialog(Object message, String title)
	{
		JLabel label = new JLabel((String) message);
		label.setFont(GuiConstants.FONT_COURIER_PLAIN_12);

		int option = JOptionPane.showConfirmDialog(
			null, label, title, JOptionPane.OK_CANCEL_OPTION
		);
		
		return option;
	}

	public static int showConfirmDialogWithIcon(Object message, String title, String iconName)
	{
		URL imageURL = GuiUtils.loadIcon(iconName);
		int option = JOptionPane.showConfirmDialog(
			null, message, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon(imageURL)
		);
		
		return option;
	}

	public static int showSettingsDialog(Object message, String title)
	{
		return showConfirmDialogWithIcon(message, title, GuiConstants.ICON_SETTINGS);
	}

	public static String showInputDialog(Object message, String title, String[] selectionValues, String initialValue)
	{
		JLabel label = new JLabel((String) message);
		label.setFont(GuiConstants.FONT_COURIER_PLAIN_12);

		String inputValue = (String) JOptionPane.showInputDialog(
			null, label, title, JOptionPane.QUESTION_MESSAGE, null, selectionValues, initialValue
		);
		
		return inputValue;
	}
}
