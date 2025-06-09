package com.comerzzia.instoreengine.herbnav.servicios.general.articulos.generaciondatos;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for €/kg calculation using base price.
 */
public class HNAServicioGeneracionDatosArticulosImplTest {

    private BigDecimal calcularPrecioKg(BigDecimal precioVenta, BigDecimal factor) {
        return precioVenta.divide(factor, 2, RoundingMode.HALF_UP);
    }

    @Test
    public void testPrecioKgSinPromocion() {
        BigDecimal res = calcularPrecioKg(new BigDecimal("10"), new BigDecimal("2"));
        Assert.assertEquals(new BigDecimal("5.00"), res);
    }

    @Test
    public void testPrecioKgConPromocionActiva() {
        BigDecimal res = calcularPrecioKg(new BigDecimal("8"), new BigDecimal("2"));
        Assert.assertEquals(new BigDecimal("4.00"), res);
    }
}
