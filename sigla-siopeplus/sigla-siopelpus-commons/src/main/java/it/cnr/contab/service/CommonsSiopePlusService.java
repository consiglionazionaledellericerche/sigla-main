package it.cnr.contab.service;

import it.cnr.contab.model.Esito;
import it.cnr.contab.model.MessaggioXML;
import it.cnr.contab.model.Parameter;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.*;
import java.security.cert.CertificateException;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public abstract class CommonsSiopePlusService {
    protected static final String APPLICATION_ZIP = "application/zip";
    protected static final String  APPLICATION_JSON_UTF8 = "application/json;charset=UTF-8";

    private transient static final Logger logger = LoggerFactory.getLogger(CommonsSiopePlusService.class);
    protected static final String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    protected static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

    @Value("${siopeplus.certificate.password}")
    public String password;

    public String getDataDa(Esito esito) {
        switch (esito) {
            case ACK:
                return Parameter.dataProduzioneDa.name();
            default:
                return Parameter.dataUploadDa.name();
        }
    }

    public String getDataA(Esito esito) {
        switch (esito) {
            case ACK:
                return Parameter.dataProduzioneA.name();
            default:
                return Parameter.dataUploadA.name();
        }
    }

    public abstract <T extends Object> MessaggioXML<T> getLocation(String location, Class<T> clazz);

    protected <T extends Object> MessaggioXML<T> getLocation(String location, Class<T> clazz, JAXBContext jc) {
        CloseableHttpClient client = null;
        try {
            client = getHttpClient();
            URIBuilder builder = new URIBuilder(location);

            HttpGet httpGet = new HttpGet(builder.build());
            httpGet.setHeader("Accept", APPLICATION_ZIP);

            final HttpResponse response = client.execute(httpGet);
            final InputStream content = response.getEntity().getContent();
            ZipInputStream zipInputStream = new ZipInputStream(content);
            final ZipEntry nextEntry = zipInputStream.getNextEntry();
            final String name = nextEntry.getName();
            final byte[] bytes = IOUtils.toByteArray(extractFileFromArchive(zipInputStream));

            final Unmarshaller unmarshaller = jc.createUnmarshaller();
            final T object = Optional.ofNullable(
                    unmarshaller.unmarshal(new ByteArrayInputStream(bytes)))
                    .map(jaxbElement -> {
                        try {
                            return (T) jaxbElement;
                        } catch (ClassCastException _ex) {
                            return null;
                        }
                    })
                    .orElse(null);
            return new MessaggioXML<T>(name, bytes, object);
        } catch (URISyntaxException | KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException | UnrecoverableKeyException | KeyManagementException | JAXBException e) {
            throw new RuntimeException(e);
        } finally {
            Optional.ofNullable(client)
                    .ifPresent(httpClient -> {
                        try {
                            httpClient.close();
                        } catch (IOException e) {
                            logger.error("CANNOT CLOSE HTTPCLIENT");
                        }
                    });
        }
    }

    public InputStream extractFileFromArchive(ZipInputStream stream) {
        // build the path to the output file and then create the file
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {

            // create a buffer to copy through
            byte[] buffer = new byte[2048];

            // now copy out of the zip archive until all bytes are copied
            int len;
            while ((len = stream.read(buffer)) > 0) {
                output.write(buffer, 0, len);
            }
            return new ByteArrayInputStream(output.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public CloseableHttpClient getHttpClient() throws KeyStoreException, CertificateException, NoSuchAlgorithmException,
            IOException, UnrecoverableKeyException, KeyManagementException {
        KeyStore identityKeyStore = KeyStore.getInstance("jks");
        identityKeyStore.load(this.getClass().getClassLoader().getResourceAsStream("contab.cnr.it.p12"), password.toCharArray());
        SSLContext sslContext = SSLContexts
                .custom()
                // load identity keystore
                .loadKeyMaterial(identityKeyStore, password.toCharArray())
                // load trust keystore
                .loadTrustMaterial(null, (chain, authType) -> true)
                .build();

        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,
                new String[]{"TLSv1.2", "TLSv1.1"},
                null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());

        return HttpClients.custom()
                .setSSLSocketFactory(sslConnectionSocketFactory)
                .build();
    }
}
