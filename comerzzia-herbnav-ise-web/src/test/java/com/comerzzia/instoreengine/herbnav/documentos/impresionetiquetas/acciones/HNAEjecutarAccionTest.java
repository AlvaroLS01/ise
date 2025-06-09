package com.comerzzia.instoreengine.herbnav.documentos.impresionetiquetas.acciones;

import org.junit.Assert;
import org.junit.Test;

/**
 * Simple tests for printing trigger logic.
 */
public class HNAEjecutarAccionTest {

    private boolean shouldPrint(long versionTarifa, long versionMaxTarifa) {
        return versionTarifa != versionMaxTarifa;
    }

    @Test
    public void testNoImpresionMismaVersion() {
        Assert.assertFalse(shouldPrint(1L, 1L));
    }

    @Test
    public void testImpresionPorCambioTarifa() {
        Assert.assertTrue(shouldPrint(1L, 2L));
    }
}
