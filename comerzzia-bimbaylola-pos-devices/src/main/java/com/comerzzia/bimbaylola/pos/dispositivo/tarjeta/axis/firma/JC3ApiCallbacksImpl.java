package com.comerzzia.bimbaylola.pos.dispositivo.tarjeta.axis.firma;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.IntBuffer;
import java.nio.LongBuffer;

import org.apache.log4j.Logger;

import com.ingenico.fr.jc3api.JC3ApiConstants;
import com.ingenico.fr.jc3api.JC3ApiInterface;
import com.ingenico.fr.jc3api.JC3ApiUtils;

/**
 * JC3API library callbacks implementation example
 */
public class JC3ApiCallbacksImpl extends JC3ApiInterface.JC3ApiCallbacksExt implements JC3ApiConstants{
	
	protected Logger logger_;
	protected JC3ApiGetKeyThread getKeyThread_;
	protected boolean printTicketFile_;
	protected boolean printTicketBinary_;
	protected int displayOptions_;
	protected byte[] displayImage_;
	protected byte[] printTicketImageSignature_;
	
	/**
	 * JC3API library callbacks implementation example - constructor
	 * @param logger the log4j logger
	 * @param getKeyThread thread listening for keyboard events
	 */
	public JC3ApiCallbacksImpl(Logger logger, JC3ApiGetKeyThread getKeyThread){
		/* print ticket with file by default support binary contents by default (not needed for C3 FR configuration) */
		this(logger, getKeyThread, true, true);
	}
	
	/**
	 * JC3API library callbacks implementation example - constructor
	 * @param logger the log4j logger
	 * @param getKeyThread thread listening for keyboard events
	 * @param printTicketFile support for Print Ticket from file / buffer
	 * @param printTicketBinary support for Print Ticket with binary contents
	 */
	public JC3ApiCallbacksImpl(Logger logger, JC3ApiGetKeyThread getKeyThread,
			boolean printTicketFile, boolean printTicketBinary){
		logger_ = logger;
		getKeyThread_ = getKeyThread;
		printTicketFile_ = printTicketFile;
		printTicketBinary_ = printTicketBinary;
		displayOptions_ = C3DSP_WAIT_NONE;
		resetImageSignature();
	}

	public boolean getPrintTicketFile(){
		return printTicketFile_;
	}

	public void setPrintTicketFile(boolean printTicketFile){
		printTicketFile_ = printTicketFile;
	}

	public boolean getPrintTicketBinary(){
		return printTicketBinary_;
	}

	public void setPrintTicketBinary(boolean printTicketBinary){
		printTicketBinary_ = printTicketBinary;
	}

	/**
	 * Get the signature image received in C3 callbacks
	 * @return the raw contents of the signature image
	 */
	public byte[] getImageSignature(){
		if(displayImage_ != null){
			return displayImage_;
		}
		return printTicketImageSignature_;
	}

	/**
	 * Reset the signature image
	 */
	public void resetImageSignature(){
		displayImage_ = null;
		printTicketImageSignature_ = null;
	}

	/**
	 * Log the given message at DEBUG level
	 * @param msg the message to be logged
	 */
	protected void logDebug(String msg){
		if(logger_ != null){
			logger_.debug(msg);
		}
	}

	/**
	 * Log the given message at INFO level
	 * @param msg the message to be logged
	 */
	protected void logInfo(String msg){
		if(logger_ != null){
			logger_.info(msg);
		}
	}

	/**
	 * Log the given message at WARN level
	 * @param msg the message to be logged
	 */
	protected void logWarn(String msg){
		if(logger_ != null){
			logger_.warn(msg);
		}
	}

	/**
	 * Log the given message at ERROR level
	 * @param msg the message to be logged
	 */
	protected void logError(String msg){
		if(logger_ != null){
			logger_.error(msg);
		}
	}

	/**
	 * @see JC3ApiInterface.JC3ApiCallbacks#getSecurity()
	 */
	public int getSecurity(){
		try{
			return getKeyThread_.getSecurity();
		}catch(IOException e){
			return C3KEY_CANCELLATION;
		}
	}

	/**
	 * @see JC3ApiInterface.JC3ApiCallbacks#getSalesConfirmation()
	 */
	public int getSalesConfirmation(){
		try{
			return getKeyThread_.getSalesConfirmation();
		}catch(IOException e){
			return C3KEY_CANCELLATION;
		}
	}

	/**
	 * @see JC3ApiInterface.JC3ApiCallbacks#getKey()
	 */
	public int getKey(){
		if(!getKeyThread_.hasKey()){
			return C3KEY_NOKEY;
		}

		int key = getKeyThread_.getKey();

		/* check that the key is allowed */
		boolean displayOptionsSupported = true;
		switch(displayOptions_){
			/* 0x00, all keys are allowed */
			case C3DSP_WAIT_NONE:
				switch (key) {
					case C3KEY_VALIDATION:
					case C3KEY_CANCELLATION:
					case C3KEY_CORRECTION:
					case C3KEY_NUMERIC_0:
					case C3KEY_NUMERIC_1:
					case C3KEY_NUMERIC_2:
					case C3KEY_NUMERIC_3:
					case C3KEY_NUMERIC_4:
					case C3KEY_NUMERIC_5:
					case C3KEY_NUMERIC_6:
					case C3KEY_NUMERIC_7:
					case C3KEY_NUMERIC_8:
					case C3KEY_NUMERIC_9:
						return key;
				}
			break;
			/* 0xC2, breakable */
			case C3DSP_BREAKABLE:
				switch(key){
					/* VAL key may be used to skip simulator delays (at INSEREZ CARTE msg for ex) */
					case C3KEY_VALIDATION:
					/* ANN key may be used to break pinpad processing */
					case C3KEY_CANCELLATION: 
						return key;
				}
			break;
			/* 0x81, VAL key is allowed */
			case C3DSP_WAIT_VAL:
				switch(key){
					case C3KEY_VALIDATION:
						return key;
				}
			break;
			/* 0x82, ANN key is allowed */
			case C3DSP_WAIT_ANN:
				switch(key){
					case C3KEY_CANCELLATION:
						return key;
				}
			break;
			/* 0x84, COR key is allowed */
			case C3DSP_WAIT_COR:
				switch(key){
					case C3KEY_CORRECTION:
						return key;
				}
			break;
			/* 0x88, NUM keys are allowed */
			case C3DSP_WAIT_NUM:
				switch(key){
					case C3KEY_NUMERIC_0:
					case C3KEY_NUMERIC_1:
					case C3KEY_NUMERIC_2:
					case C3KEY_NUMERIC_3:
					case C3KEY_NUMERIC_4:
					case C3KEY_NUMERIC_5:
					case C3KEY_NUMERIC_6:
					case C3KEY_NUMERIC_7:
					case C3KEY_NUMERIC_8:
					case C3KEY_NUMERIC_9:
					return key;
				}
			break;
			/* 0x83, VAL key and ANN key are allowed */
			case C3DSP_WAIT_VAL_ANN:
				switch(key){
					case C3KEY_VALIDATION:
					case C3KEY_CANCELLATION:
						return key;
				}
			break;
			/* 0x85, VAL key and COR key are allowed */
			case C3DSP_WAIT_VAL_COR:
				switch(key){
					case C3KEY_VALIDATION:
					case C3KEY_CORRECTION:
						return key;
				}
			break;
			/* 0x87, VAL key, ANN key and COR key are allowed */
			case C3DSP_WAIT_VAL_ANN_COR:
				switch(key){
					case C3KEY_VALIDATION:
					case C3KEY_CANCELLATION:
					case C3KEY_CORRECTION:
						return key;
				}
			break;
			default:
				/* Warn if the display options are not supported */
				displayOptionsSupported = false;
				logError("Display options (" + C3DisplayModes.getInfo(displayOptions_) + ") not supported !");
			break;
		}
		
		/* Warn if the display options are supported but the key is not part of the expected keys */
		if(displayOptionsSupported){
			logWarn("Unexpected KEY (" + C3Keys.getInfo(key) + ") for Display options (" + C3DisplayModes.getInfo(displayOptions_) + ")");
		}
		/* Key is not allowed */
		return C3KEY_NOKEY;
	}

	/**
	 * @see JC3ApiInterface.JC3ApiCallbacks#keyAvailable()
	 */
	public boolean keyAvailable(){
		return getKeyThread_.hasKey();
	}

	/**
	 * @see JC3ApiInterface.JC3ApiCallbacks#getString(StringBuffer, int, String)
	 */
	public int getString(StringBuffer str, int maxlen, String msg){
		logInfo(msg);
		
		int ret = C3KEY_CANCELLATION;
		String s = null;
		try{
			s = getKeyThread_.getString(maxlen);
		}catch(IOException e){
			logger_.error("JC3ApiCallbacksImpl/getString() - " + e.getMessage(), e);
		}
		if(s != null){
			str.setLength(0);
			str.append(s);
			ret = C3KEY_VALIDATION;
		}
		return ret;
	}

	/**
	 * @see JC3ApiInterface.JC3ApiCallbacks#display(String, int)
	 */
	public void display(String msg, int mode){
		logInfo(String.format("[0x%02X] '%s'", mode, msg));

		/* reset display options */
		displayOptions_ = C3DSP_WAIT_NONE;
		switch (mode){
			case C3DSP_WAIT_NONE:
			break;
			case C3DSP_WAIT_KEY:
				getKeyThread_.getKey();
			break;
			case C3DSP_WAIT_1SEC:
				try{
					Thread.sleep(1000);
				}catch(InterruptedException e){
					logger_.error("JC3ApiCallbacksImpl/display() - " + e.getMessage(), e);
				}
			break;
			/* Values 0x8X when c3config ICL_MODE=1 (optional) */
			default:
				/* Save display options for later analysis in getKey() method */
				displayOptions_ = mode;
			break;
		}
	}

	/**
	 * Parse Print Ticket request with XML contents
	 * Receive captured signature XML base64-encoded
	 * AUDIT_XML_SIGNATURE=1 must be set in c3config
	 * @param ticket the ticket to be parsed
	 */
	protected void parseXmlPrintTicket(String ticket){
		C3ImageTypes[] sigType = new C3ImageTypes[1]; 
		byte[] signature = JC3ApiUtils.parseSignatureFromXmlPrintTicket(ticket, sigType, logger_);
		if(signature == null){
			logWarn("Failed to get signature from XML ticket");
			return;
		}

		/* Save signature image to electronic journal (format JPG or PNG) */
		FileOutputStream fos = null;
		try{
			/* Determine file extension from image type */
			String sigFileType = (sigType[0] != null) ? sigType[0].getType() : "bin";
			String sigFile = "./signature." + sigFileType;
			fos = new FileOutputStream(sigFile);
			fos.write(signature);
			logInfo("Cardholder signature saved to `" + sigFile + "' (" + signature.length + " bytes)");
			/* Keep a copy of the signature buffer to be included in C3 tickets */
			printTicketImageSignature_ = signature;
		}catch(IOException e){
			logger_.error("JC3ApiCallbacksImpl/parseXmlPrintTicket() - " + e.getMessage(), e);
		}finally{
			if(fos != null){
				try{
					fos.close();
				}catch(IOException e){
					logger_.error("JC3ApiCallbacksImpl/parseXmlPrintTicket() - " + e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * Helper to determine whether the given reader contains binary contents or not
	 * @return true if the given reader contains binary contents, false otherwise
	 */
	protected boolean isBinary(Reader reader){
		try{
			int c;
			while((c = reader.read()) != -1){
				/* Accept CR LF */
				if(c == 0x0A || c == 0x0D){
					continue;
				}
				/* Accept characters from ISO/CEI 8859-1 table */
				if((c < 0x20) || (c > 0x7E && c < 0xA0)){
					return true;
				}
			}
		}catch(IOException e){
			logger_.error("JC3ApiCallbacksImpl/isBinary() - " + e.getMessage(), e);
		}finally{
			try{
				/* Close provided reader */
				reader.close();
			}catch(IOException e){
				logger_.error("JC3ApiCallbacksImpl/isBinary() - " + e.getMessage(), e);
			}
		}
		return false;
	}

	/**
	 * Helper to determine whether the given file contains binary contents or not
	 * @return true if the given file contains binary contents, false otherwise
	 */
	protected boolean isFileBinary(File f) throws IOException{
		InputStream is = new FileInputStream(f);
		Reader reader = new InputStreamReader(is, CHARSET_JC3API);
		return isBinary(reader);
	}
	
	/**
	 * Helper to determine whether the given string contains binary contents or not
	 * @return true if the given string contains binary contents, false otherwise
	 */
	protected boolean isStringBinary(String s){
		Reader reader = new StringReader(s);
		return isBinary(reader);
	}

	/**
	 * Print ticket callback - Handling Ticket with binary contents
	 * The cardholder signature may be sent by C3 as an image or 
	 * an XML base64-encoded string
	 * C3CONFIG :
	 * AUDIT_XML_SIGNATURE = 1 to enable XML base64-encoded string
	 * EXTENDED_CALLBACK = 3 to send signature as JPG image
	 * @param ticket the ticket to be printed
	 */
	protected void printTicketBinary(String ticket){
		Reader reader = null;
		boolean isBinary = false;

		/* Ticket parameter contains the path to the ticket */
		if(printTicketFile_){
			logInfo("PRINT TICKET (from file : `" + ticket + "')");
			File f = new File(ticket);
			if(!f.exists()){
				logError("Ticket file not found !");
			}else{
				try{
					InputStream is = new FileInputStream(f);
					reader = new InputStreamReader(is, CHARSET_JC3API);
					isBinary = isFileBinary(f);
				}catch(IOException e){
					logError("PRINT TICKET IOException : " + e);
				}
			}
		/* Ticket parameter contains the ticket contents as a buffer */
		}else{
			logInfo("PRINT TICKET (from buffer)");
			reader = new StringReader(ticket);
			isBinary = isStringBinary(ticket);
		}
		/* Failed to initialize reader */
		if(reader == null){
			return;
		}
		/* Handle binary contents (not applicable to C3 FR configuration) */
		if(isBinary){
			logInfo("Received Ticket with binary contents");
			try{
				reader.close();
			}catch(IOException e){
				logger_.error("JC3ApiCallbacksImpl/printTicketBinary() - " + e.getMessage(), e);
			}
			return;
		}
		/* Log ticket lines */
		BufferedReader bufferedReader = null;
		try{
			boolean firstLine = true;
			boolean isXmlPrintTicket = false;
			String line;
			StringBuilder xmlString = null;

			bufferedReader = new BufferedReader(reader);
			while((line = bufferedReader.readLine()) != null){
				/* Check if we have received XML contents */
				if(firstLine){
					firstLine = false;
					if(JC3ApiUtils.isXmlPrintTicket(line)){
						isXmlPrintTicket = true;
						xmlString = new StringBuilder();
					}else{
						logInfo("========================");
					}
				}
				/* Store fragments for later processing */
				if(isXmlPrintTicket){
					xmlString.append(line.trim());
				/* Log ticket line */
				}else{
					logInfo(line);
				}
			}
			/* Parse XML string */
			if(isXmlPrintTicket){
				parseXmlPrintTicket(xmlString.toString());
			}else{
				logInfo("========================");
			}
		}catch(IOException e){
			logError("PRINT TICKET IOException : " + e);
		}finally{
			if(bufferedReader != null){
				try{
					bufferedReader.close();
				}catch(IOException e){
					logger_.error("JC3ApiCallbacksImpl/printTicketBinary() - " + e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * Print ticket callback - Handling Ticket with text contents
	 * @param ticket the ticket to be printed
	 */
	public void printTicketStandard(String ticket){
		Reader reader = null;
		/* Ticket parameter contains the path to the ticket */
		if(printTicketFile_){
			logInfo("PRINT TICKET (from file : `" + ticket + "')");
			File f = new File(ticket);
			if(!f.exists()){
				logError("Ticket file not found !");
			}else{
				try{
					InputStream is = new FileInputStream(f);
					reader = new InputStreamReader(is, CHARSET_JC3API);
				}catch(IOException e){
					logError("PRINT TICKET IOException : " + e);
				}
			}
		/* Ticket parameter contains the ticket contents as a buffer */
		}else{
			logInfo("PRINT TICKET (from buffer)");
			reader = new StringReader(ticket);
		}
		/* Failed to initialize reader */
		if(reader == null){
			return;
		}
		/* Log ticket lines */
		BufferedReader bReader = null;
		try{
			bReader = new BufferedReader(reader);
			String line;
			logInfo("========================");
			while((line = bReader.readLine()) != null){
				logInfo(line);
			}
			logInfo("========================");
		}catch(IOException e){
			logError("PRINT TICKET IOException : " + e);
		}finally{
			if(bReader != null){
				try{
					bReader.close();
				}catch(IOException e){
					logger_.error("JC3ApiCallbacksImpl/printTicketStandard() - " + e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * @see JC3ApiInterface.JC3ApiCallbacks#printTicket(String)
	 */
	public void printTicket(String ticket){
		if(printTicketBinary_){
			printTicketBinary(ticket);
		}else{
			printTicketStandard(ticket);
		}
	}

	/**
	 * Check the menu choice is valid (part of possible menu choices)
	 */
	private boolean isMenuChoiceValid(int[] entries, int choice){
		boolean choiceValid = false;
		for(int i = 0; i < entries.length && !choiceValid; i++){
			if(choice == entries[i]){
				choiceValid = true;
			}
		}
		/* Warn if the choice is invalid, but return the choice to C3 */
		if(!choiceValid){
			logWarn("Invalid choice `" + choice + "' !");
		}
		return choiceValid;
	}

	/**
	 * @see JC3ApiInterface.JC3ApiCallbacksExt#getMenu(IntBuffer, String, String, int[], String[])
	 */
	@Override
	public int getMenu(IntBuffer choice, String app, String title, int[] entries, String[] labels){
		logInfo("GET MENU [" + app + "] : " + title);
		
		/* Maxlen is the length of the bigger possible choice */		
		int maxlen = 0;
		for(int i = 0; i < entries.length; i++){
			logInfo(entries[i] + ") " + labels[i]);
			String s = Integer.toString(entries[i]);
			if(maxlen < s.length()){
				maxlen = s.length();
			}
		}
		do{
			choice.put(0, -1);
			String s = null;
			try{
				s = getKeyThread_.getString(maxlen);
			}catch(IOException e){
				logger_.error("JC3ApiCallbacksImpl/getMenu() - " + e.getMessage(), e);
			}
			if(s == null || !JC3ApiUtils.isN(s)){
				return C3KEY_CANCELLATION;
			}
			choice.put(0, Integer.parseInt(s));
		/* Loop until the user choice is valid */	
		}while(!isMenuChoiceValid(entries, choice.get(0)));

		return C3KEY_VALIDATION;
	}

	/**
	 * @see JC3ApiInterface.JC3ApiCallbacksExt#getCashback(LongBuffer, long)
	 */
	@Override
	public int getCashback(LongBuffer cashback, long maxCashback){
		logInfo("GET CASHBACK (MAX " + maxCashback + ")");

		String s = null;
		do{
			cashback.put(0, -1L);
			try{
				s = getKeyThread_.getString(12);
			}catch(IOException e){
				logger_.error("JC3ApiCallbacksImpl/getCashback() - " + e.getMessage(), e);
			}
			if(s == null){
				return C3KEY_CANCELLATION;
			}
			/* Remove "." and "," decimal separators */
			s = s.replaceAll(",", "");
			s = s.replaceAll("\\.", "");
			
		/* Loop until the cashback amount is valid */	
		}while(!JC3ApiUtils.isN(s) || Long.parseLong(s) > maxCashback);

		cashback.put(0, Long.parseLong(s));
		return C3KEY_VALIDATION;
	}

	/**
	 * @see JC3ApiInterface.JC3ApiCallbacksExt#displayImage(byte[], String)
	 */
	@Override
	public void displayImage(byte[] img, String imgType){
		FileOutputStream fos = null;
		try{
			String imgFile = "./image." + imgType;
			fos = new FileOutputStream(imgFile);
			fos.write(img);
			logInfo("IMAGE - DISPLAY `" + imgFile + "' (" + img.length + " bytes)");
			/* Keep a copy of the image */
			displayImage_ = img;
		}catch(IOException e){
			logger_.error("JC3ApiCallbacksImpl/displayImage() - " + e.getMessage(), e);
		}finally{
			if(fos != null){
				try{
					fos.close();
				}catch(IOException e){
					logger_.error("JC3ApiCallbacksImpl/displayImage() - " + e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * @see JC3ApiInterface.JC3ApiCallbacksExt#clearScreen()
	 */
	@Override
	public void clearScreen(){
		logInfo("IMAGE - CLEAR SCREEN");
	}

	/**
	 * @see JC3ApiInterface.JC3ApiCallbacksExt#barcodeEvent(String, C3BarcodeSymbologies)
	 */
	@Override
	public void barcodeEvent(String barcode, C3BarcodeSymbologies symbology){
		logInfo("BARCODE EVENT `" + barcode + "' (" + symbology.getSymbology() + ")");
	}
}
