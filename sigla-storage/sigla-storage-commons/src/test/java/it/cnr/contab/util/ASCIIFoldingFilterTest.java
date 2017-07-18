package it.cnr.contab.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by francesco on 18/07/17.
 */
public class ASCIIFoldingFilterTest {


    @Test
    public void foldToASCII() throws Exception {
        String input = "EPFL | École polytechnique fédérale de Lausanne";
        String output = ASCIIFoldingFilter.foldToASCII(input);
        assertEquals("EPFL | Ecole polytechnique federale de Lausanne", output);
    }

}