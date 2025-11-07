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
 * List of C3 categories supported by the GUI
 */
public enum GuiCategory
{
	ADMINISTRATION,
	INTEGRATION,
	TRANSACTION,
	BARCODE;

	public static GuiCategory findCategory(String category)
	{
		for (GuiCategory gc : GuiCategory.values()) {
			if (gc.name().equals(category)) {
				return gc;
			}
		}
		return null;
	}	
}
