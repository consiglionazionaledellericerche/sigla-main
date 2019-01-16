package it.cnr.contab.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.cnr.contab.exception.SIOPEPlusServiceUnavailable;
import it.cnr.contab.model.*;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class OrdinativiSiopePlusService extends CommonsSiopePlusService {
    private transient static final Logger logger = LoggerFactory.getLogger(OrdinativiSiopePlusService.class);

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

    public Risultato postFlusso(InputStream input) throws SIOPEPlusServiceUnavailable {
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
            httpPost.setHeader("Accept", APPLICATION_JSON_UTF8);

            final InputStreamEntity inputStreamEntity = new InputStreamEntity(inputZIP);
            inputStreamEntity.setChunked(true);
            inputStreamEntity.setContentType(APPLICATION_ZIP);
            httpPost.setEntity(inputStreamEntity);

            final HttpResponse response = client.execute(httpPost);
            if (!Optional.ofNullable(response).filter(httpResponse -> httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED).isPresent()) {
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_SERVICE_UNAVAILABLE)
                    throw new SIOPEPlusServiceUnavailable();
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
                    .ifPresent(date -> builder.setParameter(getDataA(esito), dataA.format(formatter)));
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
                            logger.error("CANNOT CLOSE HTTPCLIENT");
                        }
                    });
        }
    }

    public <T extends Object> MessaggioXML<T> getLocation(String location, Class<T> clazz) {
        try {
            return getLocation(location, clazz, JAXBContext.newInstance(it.siopeplus.custom.ObjectFactory.class, it.siopeplus.ObjectFactory.class));
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }


    public void validateFlussoOrdinativi(InputStream xml) throws SAXException, IOException {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(new StreamSource(this.getClass().getResourceAsStream("/xsd/OPI_FLUSSO_ORDINATIVI_V_1_3_1.xsd")));
        Validator validator = schema.newValidator();
        validator.validate(new StreamSource(xml));
    }

}
