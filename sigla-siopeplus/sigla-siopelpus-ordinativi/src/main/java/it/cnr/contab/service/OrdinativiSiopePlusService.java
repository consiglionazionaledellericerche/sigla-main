package it.cnr.contab.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.cnr.contab.model.Esito;
import it.cnr.contab.model.Lista;
import it.cnr.contab.model.MessaggioXML;
import it.cnr.contab.model.Risultato;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.InputStreamEntity;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Service
public class OrdinativiSiopePlusService {
    public static final String APPLICATION_ZIP = "application/zip";
    private transient static final Logger logger = LoggerFactory.getLogger(OrdinativiSiopePlusService.class);
    private static final String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
    @Value("${siopeplus.certificate.password}")
    public String password;
    @Value("${siopeplus.url.flusso}")
    public String urlFlusso;
    @Value("${siopeplus.url.flusso.ack}")
    public String urlACK;
    @Value("${siopeplus.url.flusso.esito}")
    public String urlEsito;
    @Value("${siopeplus.url.esitoapplicativo}")
    public String urlEsitoApplicativo;

    public String getURL(Esito esito) {
        switch (esito) {
            case ACK:
                return urlACK;
            case ESITO:
                return urlEsito;
            case ESITOAPPLICATIVO:
                return urlEsitoApplicativo;
            default:
                return urlACK;
        }
    }

    public String getDataDa(Esito esito) {
        switch (esito) {
            case ACK:
                return "dataProduzioneDa";
            default:
                return "dataUploadDa";
        }
    }

    public String getDataA(Esito esito) {
        switch (esito) {
            case ACK:
                return "dataProduzioneA";
            default:
                return "dataUploadA";
        }
    }

    public Risultato postFlusso(InputStream input) {
        CloseableHttpClient client = null;
        try {
            client = getHttpClient();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            final ZipOutputStream zos = new ZipOutputStream(out);
            ZipEntry zipEntryChild = new ZipEntry("flusso.xml");
            zos.putNextEntry(zipEntryChild);
            IOUtils.copyLarge(input, zos);
            zos.close();
            InputStream inputZIP = new ByteArrayInputStream(out.toByteArray());

            URIBuilder builder = new URIBuilder(urlFlusso);
            HttpPost httpPost = new HttpPost(builder.build());
            httpPost.setHeader("Content-Type", APPLICATION_ZIP);
            httpPost.setHeader("Accept", "application/json;charset=UTF-8");

            final InputStreamEntity inputStreamEntity = new InputStreamEntity(inputZIP);
            inputStreamEntity.setChunked(true);
            inputStreamEntity.setContentType(APPLICATION_ZIP);
            httpPost.setEntity(inputStreamEntity);

            final HttpResponse response = client.execute(httpPost);
            if (!Optional.ofNullable(response).filter(httpResponse -> httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED).isPresent()) {
                logger.error(response.getStatusLine().getReasonPhrase());
                throw new RuntimeException("Cannot send flusso error code: " + response.getStatusLine().getStatusCode());
            }
            Gson gson = new GsonBuilder().setDateFormat(pattern).create();
            return gson.fromJson(IOUtils.toString(response.getEntity().getContent(), "UTF-8"), Risultato.class);
        } catch (URISyntaxException | KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException | UnrecoverableKeyException | KeyManagementException e) {
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

    public Lista getListaMessaggi(Esito esito, LocalDateTime dataDa, LocalDateTime dataA, Boolean download, Integer pagina) {
        CloseableHttpClient client = null;
        try {
            client = getHttpClient();
            URIBuilder builder = new URIBuilder(getURL(esito));
            Optional.ofNullable(dataDa)
                    .ifPresent(date -> builder.setParameter(getDataDa(esito), dataDa.format(formatter)));
            Optional.ofNullable(dataA)
                    .ifPresent(date -> builder.setParameter(getDataA(esito), dataDa.format(formatter)));
            Optional.ofNullable(download)
                    .ifPresent(aBoolean -> builder.setParameter("download", aBoolean.toString()));
            Optional.ofNullable(pagina)
                    .ifPresent(integer -> builder.setParameter("pagina", integer.toString()));
            HttpGet httpGet = new HttpGet(builder.build());
            httpGet.setHeader("Accept", "application/json;charset=UTF-8");

            final HttpResponse response = client.execute(httpGet);
            if (!Optional.ofNullable(response).filter(httpResponse -> httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK).isPresent()) {
                logger.error(response.getStatusLine().getReasonPhrase());
                return new Lista();
            }
            Gson gson = new GsonBuilder().setDateFormat(pattern).create();
            return gson.fromJson(IOUtils.toString(response.getEntity().getContent(), "UTF-8"), Lista.class);
        } catch (URISyntaxException | KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException | UnrecoverableKeyException | KeyManagementException e) {
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

    public <T extends Object> MessaggioXML<T> getLocation(String location, Class<T> clazz) {
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

            JAXBContext jc = JAXBContext.newInstance(it.siopeplus.custom.ObjectFactory.class, it.siopeplus.ObjectFactory.class);
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

    private InputStream extractFileFromArchive(ZipInputStream stream) {
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
        identityKeyStore.load(OrdinativiSiopePlusService.class.getClassLoader().getResourceAsStream("contab.cnr.it.p12"), password.toCharArray());
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
