package it.cnr.contab.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.cnr.contab.model.ACK;
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
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.*;
import java.security.cert.CertificateException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class OrdinativiSiopePlusService {
    private transient static final Logger logger = LoggerFactory.getLogger(OrdinativiSiopePlusService.class);

    private static final String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
    @Value("${siopeplus.url.ack}")
    public String urlACK;
    @Value("${siopeplus.url}")
    public String url;
    @Value("${siopeplus.certificate.password}")
    public String password;

    public Risultato postFlusso(InputStream input) {
        CloseableHttpClient client = null;
        try {
            client = getHttpClient();
            URIBuilder builder = new URIBuilder(url);
            HttpPost httpPost = new HttpPost(builder.build());
            httpPost.setHeader("Content-Type", "application/zip");
            httpPost.setHeader("Accept", "application/json;charset=UTF-8");
            //httpPost.setHeader("Content-Length", String.valueOf(IOUtils.toByteArray(input).length));

            final InputStreamEntity inputStreamEntity = new InputStreamEntity(input);
            inputStreamEntity.setChunked(true);
            inputStreamEntity.setContentType("application/zip");
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

    public ACK getACK(LocalDate dataProduzioneDa, LocalDate dataProduzioneA, Boolean download, Integer pagina) {
        CloseableHttpClient client = null;
        try {
            client = getHttpClient();
            URIBuilder builder = new URIBuilder(urlACK);
            Optional.ofNullable(dataProduzioneDa)
                    .ifPresent(date -> builder.setParameter("dataProduzioneDa", dataProduzioneDa.format(formatter)));
            Optional.ofNullable(dataProduzioneA)
                    .ifPresent(date -> builder.setParameter("dataProduzioneA", dataProduzioneDa.format(formatter)));
            Optional.ofNullable(download)
                    .ifPresent(aBoolean -> builder.setParameter("download", aBoolean.toString()));
            Optional.ofNullable(pagina)
                    .ifPresent(integer -> builder.setParameter("pagina", integer.toString()));
            HttpGet httpGet = new HttpGet(builder.build());
            httpGet.setHeader("Accept", "application/json;charset=UTF-8");

            final HttpResponse response = client.execute(httpGet);
            Gson gson = new GsonBuilder().setDateFormat(pattern).create();
            return gson.fromJson(IOUtils.toString(response.getEntity().getContent(), "UTF-8"), ACK.class);
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
