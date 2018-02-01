package it.cnr.contab.spring.storage;

import it.cnr.contab.spring.storage.config.StoragePropertyNames;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by francesco on 13/07/17.
 */

@RunWith(SpringRunner.class)
@ContextConfiguration("/sigla-storage-s3-test-context.xml")
@ActiveProfiles("S3")
public class S3StoreServiceTest {

    public static final String TEXT = "hello worlds";

    @Autowired
    private StoreService storeService;

    @Test
    @Ignore
    public void testStore() throws IOException {
        InputStream is = IOUtils.toInputStream(TEXT, Charset.defaultCharset());
        Map<String, Object> map = new HashMap();
        map.put(StoragePropertyNames.NAME.value(), "ciaone");
        map.put("email", "francesco@uliana.it");
//        map.put("titolo", "�COLE POLYTECHNIQUE F�D�RALE DE LAUSANNE EPFL");
//        map.put("name", "Raffaella Carr�");
        StorageObject document = storeService.storeSimpleDocument(is, "text/plain", "/foo", map);
        InputStream iss = storeService.getResource("foo/ciaone");
        assertEquals(TEXT, IOUtils.toString(iss, Charset.defaultCharset()));

        final String folderPath = storeService.createFolderIfNotPresent(
                "/my-path",
                "my-name",
                "my-title",
                "my-description");
        assertNotNull(folderPath);
    }

    @Test
    @Ignore
    public void testGetAndDelete() throws IOException {
        InputStream is = storeService.getResource("/foo/ciaone");
        assertEquals(TEXT, IOUtils.toString(is, Charset.defaultCharset()));

        storeService.delete(storeService.getStorageObjectBykey("/foo/ciaone"));
        storeService.delete(storeService.getStorageObjectBykey("/my-path/my-name"));
    }

}