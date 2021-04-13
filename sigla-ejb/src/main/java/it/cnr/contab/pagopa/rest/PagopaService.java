package it.cnr.contab.pagopa.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import feign.Feign;
import feign.Retryer;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.ErrorDecoder;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import it.cnr.contab.pagopa.model.Pendenza;
import it.cnr.contab.pagopa.model.PendenzaResponse;
import it.cnr.contab.util.Utility;
import net.dongliu.gson.GsonJava8TypeAdapterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class PagopaService {
    private transient static final Logger log = LoggerFactory.getLogger(PagopaService.class);
    String baseUrl;
    String username;
    String password;
    String usernameApp;
    String passwordApp;

    private PagopaClient pagopaClient;
    private PagopaClient pagopaDownloadClient;

    public String getUsernameApp() {
        return usernameApp;
    }

    public void setUsernameApp(String usernameApp) {
        this.usernameApp = usernameApp;
    }

    public String getPasswordApp() {
        return passwordApp;
    }

    public void setPasswordApp(String passwordApp) {
        this.passwordApp = passwordApp;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @PostConstruct
    public void init() {

        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss.SSS", Locale.ITALIAN)
                .withZone(ZoneId.of("Europe/Rome"));

        GsonJava8TypeAdapterFactory typeAdapterFactory;
        typeAdapterFactory = new GsonJava8TypeAdapterFactory();
        typeAdapterFactory.setInstantFormatter(formatter);

        Gson gsonParser = new GsonBuilder().registerTypeAdapterFactory(typeAdapterFactory).create();

        log.info("Building PagopaService connection with {}", baseUrl);

        pagopaClient = Feign.builder()
                .decoder(new GsonDecoder(gsonParser))
                .encoder(new GsonEncoder(gsonParser))
                .requestInterceptor(new BasicAuthRequestInterceptor(usernameApp, passwordApp))
                .errorDecoder(new ErrorDecoder.Default())
                .retryer(new Retryer.Default())
                .target(PagopaClient.class, baseUrl);

        pagopaDownloadClient = Feign.builder()
                .encoder(new GsonEncoder(gsonParser))
                .requestInterceptor(new BasicAuthRequestInterceptor(usernameApp, passwordApp))
                .errorDecoder(new ErrorDecoder.Default())
                .retryer(new Retryer.Default())
                .target(PagopaClient.class, baseUrl);
    }
    public PendenzaResponse creaPendenza(Long idPendenza, Pendenza pendenza){
        return pagopaClient.creaPendenza(Utility.APPLICATION_TITLE.substring(0, 5), idPendenza, true, pendenza);
    }
    public byte[] getAvviso(String idDominio, String numeroAvviso){
        return pagopaDownloadClient.stampaAvviso(idDominio, numeroAvviso);
    }
}
