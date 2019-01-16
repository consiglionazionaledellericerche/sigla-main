package it.cnr.contab.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.cnr.contab.model.Lista;
import it.cnr.contab.model.MessaggioXML;
import it.cnr.contab.model.Parameter;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class GiornaleDiCassaSiopePlusService extends CommonsSiopePlusService{
    private transient static final Logger logger = LoggerFactory.getLogger(GiornaleDiCassaSiopePlusService.class);
    @Value("${siopeplus.url.giornaledicassa}")
    public String urlGiornaleDiCassa;

    public Lista getListaMessaggi(LocalDateTime dataDa, LocalDateTime dataA, Boolean download, Integer pagina) {
        CloseableHttpClient client = null;
        try {
            client = getHttpClient();
            URIBuilder builder = new URIBuilder(urlGiornaleDiCassa);
            Optional.ofNullable(dataDa)
                    .ifPresent(date -> builder.setParameter(Parameter.dataUploadDa.name(), dataDa.format(formatter)));
            Optional.ofNullable(dataA)
                    .ifPresent(date -> builder.setParameter(Parameter.dataUploadA.name(), dataA.format(formatter)));
            Optional.ofNullable(download)
                    .ifPresent(aBoolean -> builder.setParameter(Parameter.download.name(), aBoolean.toString()));
            Optional.ofNullable(pagina)
                    .ifPresent(integer -> builder.setParameter(Parameter.pagina.name(), integer.toString()));
            HttpGet httpGet = new HttpGet(builder.build());
            httpGet.setHeader("Accept", APPLICATION_JSON_UTF8);

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
                            logger.error("CANNOT CLOSE HTTPCLIENT", e);
                        }
                    });
        }
    }
    @Override
    public <T> MessaggioXML<T> getLocation(String location, Class<T> clazz) {
        try {
            return getLocation(location, clazz, JAXBContext.newInstance(
                    it.siopeplus.giornaledicassa.custom.ObjectFactory.class,
                    it.siopeplus.giornaledicassa.ObjectFactory.class
            ));
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
