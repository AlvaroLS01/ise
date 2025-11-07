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
 * Enumeration for C3 Agent types
 */
public enum GuiC3AgentType
{
	NET,
	SIM;

	@Override
	public String toString()
	{
		switch (this) {
		case SIM:
			return "C3 Simulator";
		case NET:
			return "C3 Net";
		}
		return "?";
	}

	public static GuiC3AgentType findC3AgentType(String c3AgentType)
	{
		for (GuiC3AgentType gc3at : GuiC3AgentType.values()) {
			if (gc3at.name().equals(c3AgentType)) {
				return gc3at;
			}
		}
		return null;
	}	
}
