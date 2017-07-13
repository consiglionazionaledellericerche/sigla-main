package it.cnr.contab.spring.storage;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;

/**
 * Created by francesco on 13/07/17.
 */

@RunWith(SpringRunner.class)
@ContextConfiguration("/sigla-storage-azure-test-context.xml")
@ActiveProfiles("Azure")
public class AzureSiglaStorageServiceTest {

    @Autowired
    private AzureSiglaStorageService azureSiglaStorageService;

    @Test
    public void testGetInputStream() throws IOException {
        InputStream is = azureSiglaStorageService.getInputStream("foo/ciaone");
        assertEquals("hello worlds", IOUtils.toString(is, Charset.defaultCharset()));
    }


}