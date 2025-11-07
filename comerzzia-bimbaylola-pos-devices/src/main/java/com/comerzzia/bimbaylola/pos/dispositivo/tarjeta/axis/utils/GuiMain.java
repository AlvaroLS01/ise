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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.UIManager;

import com.ingenico.fr.jc3api.JC3ApiC3Rspn;
import com.ingenico.fr.jc3api.JC3ApiInterface;

/**
 * C3 Agent integration demonstration program
 * 
 * @author atos
 */
@SuppressWarnings("serial")
public class GuiMain extends JFrame
{
	// create GUI components
	protected JComboBox<GuiCategory> categoriesComboBox_ = new JComboBox<GuiCategory>(GuiCategory.values());
	protected ActionListener categoriesComboBoxListener_ = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			resetOperationsCombox();
		}
	};

	protected JComboBox<GuiOperation> operationsComboBox_ = new JComboBox<GuiOperation>(GuiOperation.values());
	protected ActionListener operationsComboBoxListener_ = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// save the newly selected operation to permanent memory
			GuiOperation guiOperation = (GuiOperation) operationsComboBox_.getSelectedItem();
			GuiSettings.setGuiOperationEnum(guiOperation);
		}
	};

	protected JButton buttonGO_ = new JButton("GO");
	protected JButton buttonSettings_ = new JButton("F");

	protected JTextField posDisplay_ = new JTextField();
	protected JTextField statusDisplay_ = new JTextField();
	
	protected JButton[] NUM_ = new JButton[10];
	protected JButton[] ANN_COR_VAL_ = new JButton[3];
	
	protected Timer clearDisplayTimer_ = new Timer(3000, new ActionListener() {
		public void actionPerformed(ActionEvent e) {
        	if (buttonGO_.isEnabled()) {
				posDisplay_.setText(null);
        		statusDisplay_.setText(null);
        	}
		}
	});

	/**
	 * Run C3 operation in a background thread when user presses the GO button
	 */
	class ButtonGOListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
        	SwingWorker<JC3ApiC3Rspn, Void> worker = new SwingWorker<JC3ApiC3Rspn, Void>()
        	{
        		C3Agent c3Agent_;
        		C3Callbacks c3Callbacks_;

        		@Override
        		public JC3ApiC3Rspn doInBackground()
        		{
        			// disable buttons
					buttonGO_.setEnabled(false);
					buttonSettings_.setEnabled(false);
					categoriesComboBox_.setEnabled(false);
					operationsComboBox_.setEnabled(false);

					// clear text areas
					posDisplay_.setText(null);
					statusDisplay_.setText(null);
       				clearDisplayTimer_.stop();

       				// run C3 Agent operation
       				JC3ApiC3Rspn c3rspn = null;
       				// get C3 callbacks singleton
       				c3Callbacks_ = C3Callbacks.getC3Callbacks(posDisplay_, NUM_, ANN_COR_VAL_);
       				c3Agent_ = C3Agent.getC3Agent(c3Callbacks_);
       				try {
	       				c3rspn = c3Agent_.runC3Operation(
	       					(GuiOperation) operationsComboBox_.getSelectedItem()
	       				);
       				} catch (Exception e) {
       					GuiUtils.showExceptionDialog("Failed to run C3 operation", e);
       				}

       				// re-enable buttons
					buttonGO_.setEnabled(true);
					buttonSettings_.setEnabled(true);
					categoriesComboBox_.setEnabled(true);
					operationsComboBox_.setEnabled(true);
					
					return c3rspn;
        		}
        		
        		@Override
        		protected void done()
        		{
					GuiOperation guiOperation = (GuiOperation) operationsComboBox_.getSelectedItem();

    				// process C3 operation result
					JC3ApiC3Rspn c3rspn = null;
					try {
						c3rspn = (JC3ApiC3Rspn) get();
					} catch (Exception e) {
					}
					if (c3rspn != null) {
						// display C3 status
						statusDisplay_.setText(c3rspn.getStrError());

						// check for partial init
						if (guiOperation == GuiOperation.INIT && c3rspn.getcC3ErrorInt() == 0) {
							if (c3rspn.getcUserData1().equals(c3rspn.getcUserData2())) {
								// init OK
							} else {
								// init partial
								statusDisplay_.setText(statusDisplay_.getText() + " (PARTIAL INIT !)");								 
							}
						}
						
						if (c3rspn.isTicketAvailable()) {
							GuiUtils.showTicketsDialog(
								"TICKETS", 
								c3rspn.getCustomerTicket(), "CUSTOMER TICKET",
								c3rspn.getMerchantTicket(), "MERCHANT TICKET"
							);
						}
					}

					try {
						if (guiOperation == GuiOperation.BARCODE_OPEN && !c3Agent_.getParams().isPclMethodsToC3Api()) {
							// do not clear the display if BARCODE events need to be displayed in the area
						} else {
							// clear display areas after 3 seconds, and do not repeat the task
							clearDisplayTimer_.start();
							clearDisplayTimer_.setRepeats(false);
						}
					} catch (IOException e) {
					}
        		}
			};
			worker.execute();            	
		}
	}

	protected void resetOperationsCombox()
	{
        // disable listener while reseting the C3 operations combox
        operationsComboBox_.removeActionListener(operationsComboBoxListener_);
        // reset the list
		operationsComboBox_.removeAllItems();
	    // build the list of operations that match the selected category
	    for (GuiOperation guiOperation : GuiOperation.values()) {
	    	if (guiOperation.getCategory() == (GuiCategory) categoriesComboBox_.getSelectedItem()) {
	            operationsComboBox_.addItem(guiOperation);
	    	}
	    }
	    operationsComboBox_.setSelectedIndex(0);
	    // save selected item to permanent memory
	    GuiOperation guiOperation = (GuiOperation) operationsComboBox_.getSelectedItem();
		GuiSettings.setGuiOperationEnum(guiOperation);
        // listen to changes on C3 operations combox
        operationsComboBox_.addActionListener(operationsComboBoxListener_);
	}

	protected void createAndRunGui() throws IOException
    {
		// Create the Frame
    	setTitle("C3Agent GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setSize(400, 400);

        URL c3agentIconURL = GuiUtils.loadIcon(GuiConstants.ICON_C3AGENT);
        if (c3agentIconURL != null) {
	        Image c3agentIcon = Toolkit.getDefaultToolkit().getImage(c3agentIconURL);
	        setIconImage(c3agentIcon);
        }

        // C3 Operations Area at top
        JPanel topPanel1 = new JPanel();
        topPanel1.setLayout(new BoxLayout(topPanel1, BoxLayout.X_AXIS));

        categoriesComboBox_.setFocusable(false);
        categoriesComboBox_.setFont(GuiConstants.FONT_COURIER_PLAIN_16);
        // load C3 operation saved and select the corresponding category
        GuiOperation guiOperation = GuiSettings.getGuiOperationEnum();
        for (int i = 0; i < categoriesComboBox_.getItemCount(); i++) {
        	if (categoriesComboBox_.getItemAt(i) == guiOperation.getCategory()) {
        		categoriesComboBox_.setSelectedIndex(i);
        		break;
        	}
        }
        // listen to changes on C3 categories combox
        categoriesComboBox_.addActionListener(categoriesComboBoxListener_);
        topPanel1.add(categoriesComboBox_);

        buttonGO_.setFont(GuiConstants.FONT_COURIER_PLAIN_16);
        buttonGO_.setFocusable(false);
        buttonGO_.addActionListener(new ButtonGOListener());
        topPanel1.add(buttonGO_);

        buttonSettings_.setFont(GuiConstants.FONT_COURIER_PLAIN_16);
        buttonSettings_.setFocusable(false);
        buttonSettings_.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GuiSettings.editSettings();
			}
		});
        topPanel1.add(buttonSettings_);

        JPanel topPanel2 = new JPanel();
        topPanel2.setLayout(new BoxLayout(topPanel2, BoxLayout.X_AXIS));

        operationsComboBox_.setFocusable(false);
        operationsComboBox_.setFont(GuiConstants.FONT_COURIER_PLAIN_16);
        // reset operations to match the selected category
        resetOperationsCombox();
        for (int i = 0; i < operationsComboBox_.getItemCount(); i++) {
        	if (operationsComboBox_.getItemAt(i) == guiOperation) {
        		operationsComboBox_.setSelectedIndex(i);
        		break;
        	}
        }
        topPanel2.add(operationsComboBox_);
        topPanel2.add(Box.createRigidArea(new Dimension(96, 0)));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(topPanel1);
        topPanel.add(topPanel2);

        // Display Area at center
        JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));

        posDisplay_.setFont(GuiConstants.FONT_COURIER_BOLD_18);
        posDisplay_.setBackground(Color.WHITE);
        posDisplay_.setEditable(false);
        
        displayPanel.add(Box.createVerticalStrut(20));
        displayPanel.add(posDisplay_);
        displayPanel.add(Box.createVerticalStrut(20));

        statusDisplay_.setFont(GuiConstants.FONT_COURIER_PLAIN_12);
        statusDisplay_.setEditable(false);
        displayPanel.add(statusDisplay_);

        // Keyboard Area at bottom
        JPanel keyboardPanel = new JPanel();
        keyboardPanel.setLayout(new GridLayout(5, 1));
        
        // numeric buttons
        for (int i = 0; i < 10; i++) {
        	NUM_[i] = new JButton(Integer.toString(i));
        	NUM_[i].setEnabled(false);
        	NUM_[i].setFont(GuiConstants.FONT_COURIER_BOLD_18);
        }
        
        JPanel numericButtonsLine1 = new JPanel();
        numericButtonsLine1.setLayout(new GridLayout(1, 3));
       	numericButtonsLine1.add(NUM_[1]);
       	numericButtonsLine1.add(NUM_[2]);
       	numericButtonsLine1.add(NUM_[3]);

        JPanel numericButtonsLine2 = new JPanel();
        numericButtonsLine2.setLayout(new GridLayout(1, 3));
       	numericButtonsLine2.add(NUM_[4]);
       	numericButtonsLine2.add(NUM_[5]);
       	numericButtonsLine2.add(NUM_[6]);

        JPanel numericButtonsLine3 = new JPanel();
        numericButtonsLine3.setLayout(new GridLayout(1, 3));
       	numericButtonsLine3.add(NUM_[7]);
       	numericButtonsLine3.add(NUM_[8]);
       	numericButtonsLine3.add(NUM_[9]);

        JPanel numericButtonsLine4 = new JPanel();
        numericButtonsLine4.setLayout(new GridLayout(1, 3));
        JButton empty1 = new JButton();
        empty1.setEnabled(false);
       	numericButtonsLine4.add(empty1);
       	numericButtonsLine4.add(NUM_[0]);
        JButton empty2 = new JButton();
        empty2.setEnabled(false);
       	numericButtonsLine4.add(empty2);

       	keyboardPanel.add(numericButtonsLine1);
       	keyboardPanel.add(numericButtonsLine2);
       	keyboardPanel.add(numericButtonsLine3);
       	keyboardPanel.add(numericButtonsLine4);

        // ANN / COR / VAL buttons
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(1, 3));

        ANN_COR_VAL_[0] = new JButton("ANN");
        ANN_COR_VAL_[0].setEnabled(false);
        ANN_COR_VAL_[0].setFont(GuiConstants.FONT_COURIER_BOLD_18);
        ANN_COR_VAL_[0].setPreferredSize(new Dimension(40, 40));
        ANN_COR_VAL_[0].setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        buttonsPanel.add(ANN_COR_VAL_[0]);

        ANN_COR_VAL_[1] = new JButton("COR");
        ANN_COR_VAL_[1].setEnabled(false);
        ANN_COR_VAL_[1].setFont(GuiConstants.FONT_COURIER_BOLD_18);
        ANN_COR_VAL_[1].setPreferredSize(new Dimension(40, 40));
        ANN_COR_VAL_[1].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
        buttonsPanel.add(ANN_COR_VAL_[1]);
        
        ANN_COR_VAL_[2] = new JButton("VAL");
        ANN_COR_VAL_[2].setEnabled(false);
        ANN_COR_VAL_[2].setFont(GuiConstants.FONT_COURIER_BOLD_18);
        ANN_COR_VAL_[2].setPreferredSize(new Dimension(40, 40));
        ANN_COR_VAL_[2].setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
        buttonsPanel.add(ANN_COR_VAL_[2]);

    	keyboardPanel.add(buttonsPanel);

        // release C3 Agent resources when the window is closed
        addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					JC3ApiInterface.releaseResources(GuiLogger.getLogger());
				} catch (Throwable t) {
				}
			}
		});

        // add components to the frame
        getContentPane().add(BorderLayout.NORTH, topPanel);
        getContentPane().add(BorderLayout.CENTER, displayPanel);
        getContentPane().add(BorderLayout.SOUTH, keyboardPanel);

        setVisible(true);
    }

	// specify the look and feel to use.  Valid values:
    // null (use the default), "Metal", "System", "Motif", "GTK+"
	static {
        String lookAndFeel = null;

        String LOOKANDFEEL = "System";
        if (LOOKANDFEEL.equals("Metal")) {
            lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
        } else if (LOOKANDFEEL.equals("System")) {
            lookAndFeel = UIManager.getSystemLookAndFeelClassName();
        } else if (LOOKANDFEEL.equals("Motif")) {
            lookAndFeel = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
        } else if (LOOKANDFEEL.equals("GTK+")) {
            lookAndFeel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
        } else {
            lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
        }

        try {
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (Exception e) {
        }
    }

	public static void main(String args[])
    {
    	GuiLogger.logInfo("Starting GUI ...");
    	try {
	    	GuiMain gui = new GuiMain();
			gui.createAndRunGui();
    	} catch (Throwable t) {
    		GuiLogger.logException("GUI Exception", t);
    	}
    }
}
