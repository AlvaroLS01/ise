package com.comerzzia.bricodepot.backoffice.admin.cambioprecio.acciones;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import org.springframework.beans.factory.annotation.Autowired;

import com.comerzzia.core.servicios.api.ComerzziaApiManager;
import com.comerzzia.pricechangetracker.client.PriceChangeTrackerApi;
import com.comerzzia.web.base.SpringEnabledServlet;
@WebServlet(loadOnStartup=1, value = "/ly", description ="Price Tracker Change Init Servlet", displayName ="PriceChangeTrackerInitServlet", name ="PriceChangeTrackerInitServlet")
public class PriceChangeTrackerInitServlet extends SpringEnabledServlet{

	private static final long serialVersionUID = -378806546839314585L;
	
	@Autowired
	protected ComerzziaApiManager comerzziaApiManager;

	@Override
    public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		comerzziaApiManager.registerAPI("PriceChangeTrackerApi", PriceChangeTrackerApi.class, "pricechangetracker");
	}
}
