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

/**
 * List of C3 operations supported by the GUI
 */
public enum GuiOperation
{
	// >>> ADMINISTRATION category <<<
	INIT(GuiCategory.ADMINISTRATION),
	VERSION(GuiCategory.ADMINISTRATION),
	SIGNBOX(GuiCategory.ADMINISTRATION),

	// >>> INTEGRATION category <<<
	PING(GuiCategory.INTEGRATION),
	AUTOTEST(GuiCategory.INTEGRATION),
	
	// >>> TRANSACTION category <<<
	PURCHASE(GuiCategory.TRANSACTION),
	PURCHASE_CASHBACK(GuiCategory.TRANSACTION),
	REFUND(GuiCategory.TRANSACTION),
	CANCEL_BY_CARD(GuiCategory.TRANSACTION),
	CANCEL_BY_REF(GuiCategory.TRANSACTION),
	CANCEL_LAST(GuiCategory.TRANSACTION),
	
	// >>> BARCODE category <<<
	BARCODE_OPEN(GuiCategory.BARCODE),
	BARCODE_CLOSE(GuiCategory.BARCODE);

	private GuiCategory guiCategory_;
	private GuiOperation(GuiCategory guiCategory) {
		guiCategory_ = guiCategory;
	}

	public GuiCategory getCategory()
	{
		return guiCategory_;
	}

	public static GuiOperation findOperation(String operation)
	{
		for (GuiOperation go : GuiOperation.values()) {
			if (go.name().equals(operation)) {
				return go;
			}
		}
		return null;
	}	
}
