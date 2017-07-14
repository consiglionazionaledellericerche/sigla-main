package it.cnr.contab.spring.storage;

import it.cnr.contab.spring.storage.config.StoragePropertyNames;
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
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by francesco on 13/07/17.
 */

@RunWith(SpringRunner.class)
@ContextConfiguration("/sigla-storage-azure-test-context.xml")
@ActiveProfiles("Azure")
public class StoreServiceTest {

    public static final String TEXT = "hello worlds";

    @Autowired
    private StoreService storeService;

    @Test
    public void testStoreSimpleDocuments() throws IOException {
        InputStream is = IOUtils.toInputStream(TEXT, Charset.defaultCharset());
        Map<String, Object> map = new HashMap();
        map.put(StoragePropertyNames.NAME.value(), "ciaone");
        map.put("email", "francesco@uliana.it");
//        map.put("titolo", "ÉCOLE POLYTECHNIQUE FÉDÉRALE DE LAUSANNE EPFL");
//        map.put("name", "Raffaella Carrà");
        StorageObject document = storeService.storeSimpleDocument(is, "text/plain", "/foo", map);
        InputStream iss = storeService.getResource("foo/ciaone");
        assertEquals(TEXT, IOUtils.toString(iss, Charset.defaultCharset()));
    }

    @Test
    public void testCreateFolderIfNotPresent() {
        storeService
                .createFolderIfNotPresent("/my-path", "my-name", "my-title", "my-description");
        assertTrue(false);
    }
}