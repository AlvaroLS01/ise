package com.comerzzia.bimbaylola.pos.persistence.articulos.buscar;

import com.comerzzia.pos.persistence.articulos.ArticuloBean;
import com.comerzzia.pos.persistence.articulos.buscar.ArticuloBuscarBean;
import com.comerzzia.pos.persistence.articulos.codBarras.ArticuloCodBarraBean;
import com.comerzzia.pos.persistence.articulos.tarifas.TarifaDetalleBean;

public class ByLArticuloBuscarBean extends ArticuloBuscarBean {
	
	private String codigoBarras;
	
	public ByLArticuloBuscarBean() {
		super();
	}
	
	public ByLArticuloBuscarBean(ArticuloBean articulo, ArticuloCodBarraBean codigoBarras, TarifaDetalleBean tarifa) {
		setCodArticulo(articulo.getCodArticulo());
		setDesArticulo(articulo.getDesArticulo());
		setCodigoBarras(codigoBarras.getCodigoBarras());
		setValorDesglose1(codigoBarras.getDesglose1());
		setValorDesglose2(codigoBarras.getDesglose2());
		if (tarifa != null) {
			setPrecio(tarifa.getPrecioVenta());
			setPrecioTotal(tarifa.getPrecioTotal());
		}
	}

	public String getCodigoBarras() {
		return codigoBarras;
	}

	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}

}
