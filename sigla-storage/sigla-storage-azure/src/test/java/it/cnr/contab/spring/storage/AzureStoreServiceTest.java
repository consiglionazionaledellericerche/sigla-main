package it.cnr.contab.spring.storage;

import com.microsoft.azure.storage.analytics.StorageService;
import it.cnr.contab.spring.storage.config.StoragePropertyNames;
import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by francesco on 13/07/17.
 */

@RunWith(SpringRunner.class)
@ContextConfiguration("/sigla-storage-azure-test-context.xml")
@ActiveProfiles("AZURE")
public class AzureStoreServiceTest {

    public static final String TEXT = "hello worlds";

    public static final String FOO = SiglaStorageService.SUFFIX + "foo spazio";
    public static final String CIAONE = "ciaone";
    public static final String FOO_CIAONE = FOO + SiglaStorageService.SUFFIX + CIAONE;

    public static final String P_CM_TITLED = "P:cm:titled";
    public static final String TEST_PDF = "test.pdf";

    @Autowired
    private StoreService storeService;

    @Test
    @Ignore
    public void testStore() throws IOException {
        InputStream is = IOUtils.toInputStream(TEXT, Charset.defaultCharset());
        Map<String, Object> map = new HashMap();
        map.put(StoragePropertyNames.NAME.value(), CIAONE);
        map.put(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value(), Arrays.asList(P_CM_TITLED));
        map.put(StoragePropertyNames.TITLE.value(), "Raffaella Carrà");
        StorageObject document = storeService.storeSimpleDocument(is, "text/plain", FOO, map);
        InputStream iss = storeService.getResource(FOO_CIAONE);
        assertEquals(TEXT, IOUtils.toString(iss, Charset.defaultCharset()));

        Map<String, Object> mapPdf = new HashMap();
        mapPdf.put(StoragePropertyNames.NAME.value(), TEST_PDF);
        mapPdf.put(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value(), Arrays.asList(P_CM_TITLED));

        final String folderPath = storeService.createFolderIfNotPresent(
                "/my-path",
                "my-name",
                "my-title",
                "my-description");
        assertNotNull(folderPath);

        storeService.storeSimpleDocument(this.getClass().getResourceAsStream("/" + TEST_PDF), MimeTypes.PDF.mimetype(), "/my-path/my-name", mapPdf);

    }

    @Test
    @Ignore
    public void testGetAndDelete() throws IOException {
        final StorageObject storageObjectByPath = storeService.getStorageObjectByPath(FOO_CIAONE);
        assertEquals(Arrays.asList(P_CM_TITLED),
                storageObjectByPath.<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value()));

        InputStream is = storeService.getResource(FOO_CIAONE);
        assertEquals(TEXT, IOUtils.toString(is, Charset.defaultCharset()));

        assertEquals(storeService.getStorageObjectBykey("/my-path/my-name/test.pdf").getKey(),
                storeService.getChildren("/my-path/my-name").stream()
                .filter(storageObject -> storageObject.getKey().equals("my-path/my-name/test.pdf"))
                .findFirst()
                .get().getKey());

        storeService.delete(storeService.getStorageObjectBykey(FOO_CIAONE));
        storeService.delete(storeService.getStorageObjectBykey("/my-path/my-name"));
        storeService.delete(storeService.getStorageObjectBykey("/my-path/my-name/test.pdf"));
    }

}