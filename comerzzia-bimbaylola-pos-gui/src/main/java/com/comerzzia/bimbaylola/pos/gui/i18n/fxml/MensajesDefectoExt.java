package com.comerzzia.bimbaylola.pos.gui.i18n.fxml;

import java.util.LinkedList;
import java.util.List;

public class MensajesDefectoExt {

	public static List<String> obtenerCadenas() throws MensajesExceptionExt {
		List<String> res = new LinkedList<>();

		res.add("TALLA");
		res.add("COLOR");
		res.add("Talla");
		res.add("Color");
        
        return res;
	}
}

