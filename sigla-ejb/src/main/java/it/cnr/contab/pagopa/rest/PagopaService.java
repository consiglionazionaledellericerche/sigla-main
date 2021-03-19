package it.cnr.contab.pagopa.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import feign.*;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.ErrorDecoder;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import it.cnr.contab.anagraf00.comp.AnagraficoComponent;
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

    private PagopaClient pagopaClient;

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
                .requestInterceptor(new BasicAuthRequestInterceptor(username, password))
                .errorDecoder(new ErrorDecoder.Default())
                .retryer(new Retryer.Default())
                .target(PagopaClient.class, baseUrl);
    }
}
