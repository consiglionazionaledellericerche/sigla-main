/*
 * Copyright (C) 2022  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.spring.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import feign.Feign;
import feign.Retryer;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.ErrorDecoder;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.httpclient.ApacheHttpClient;
import it.cnr.contab.pagopa.model.AggiornaPendenza;
import it.cnr.contab.pagopa.model.MovimentoCassaPagopa;
import it.cnr.contab.pagopa.model.Pendenza;
import it.cnr.contab.pagopa.model.PendenzaResponse;
import it.cnr.contab.pagopa.rest.PagopaClient;
import it.cnr.contab.util.Utility;
import net.dongliu.gson.GsonJava8TypeAdapterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

@Service
@Profile("pagopa")
public class PagopaService {
    private transient static final Logger log = LoggerFactory.getLogger(PagopaService.class);
    @Value("${pagopa.govpay.base_url}")
    String baseUrl;
    @Value("${pagopa.govpay.username}")
    String username;
    @Value("${pagopa.govpay.password}")
    String password;
    @Value("${pagopa.govpay.usernameApp}")
    String usernameApp;
    @Value("${pagopa.govpay.passwordApp}")
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
                .client(new ApacheHttpClient())
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
    public void aggiornaPendenza(Long idPendenza, AggiornaPendenza aggiornaPendenza){
        ArrayList<AggiornaPendenza> listaAggiornaPendenza = new ArrayList<>();
        listaAggiornaPendenza.add(aggiornaPendenza);
        pagopaClient.aggiornaPendenza(Utility.APPLICATION_TITLE.substring(0, 5), idPendenza, listaAggiornaPendenza);
    }
    public PendenzaResponse creaPendenza(Long idPendenza, Pendenza pendenza){
        return pagopaClient.creaPendenza(Utility.APPLICATION_TITLE.substring(0, 5), idPendenza, true, pendenza);
    }
    public byte[] getAvviso(String idDominio, String numeroAvviso){
        return pagopaDownloadClient.stampaAvviso(idDominio, numeroAvviso);
    }
    public byte[] getRt(String idDominio, String iuv, String ccp){
        return pagopaDownloadClient.stampaRt(idDominio, iuv, ccp, true);
    }
    public MovimentoCassaPagopa riconciliaIncasso(String idDominio, MovimentoCassaPagopa movimentoCassaPagopa){
        return pagopaClient.riconciliaIncasso(idDominio, movimentoCassaPagopa);
    }
}
