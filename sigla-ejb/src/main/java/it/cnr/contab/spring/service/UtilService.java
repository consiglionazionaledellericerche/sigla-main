/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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

import it.cnr.contab.messaggio00.ejb.CRUDMessaggioComponentSession;
import it.cnr.contab.progettiric00.ejb.ProgettoRicercaPadreComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.mail.SimplePECMail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class UtilService implements InitializingBean {
    public static final String GECO = "GECO";
    private transient final static Logger LOGGER = LoggerFactory.getLogger(UtilService.class);
    private CRUDMessaggioComponentSession crudMessaggioComponentSession;
    private ProgettoRicercaPadreComponentSession progettoRicercaPadreComponentSession;

    @Value("${doccont.max.anni.residui}")
    private Integer anniResidui;

    @Value("${help.base.url}")
    private String helpBaseURL;

    @Value("${pec.host.name}")
    private String pecHostName;
    @Value("${pec.host.sslSmtpPort}")
    private String sslSmtpPort;
    @Value("${pec.host.sSLOnConnect}")
    private Boolean sSLOnConnect;
    @Value("${pec.host.smtpPort}")
    private Integer smtpPort;
    @Value("${pec.host.startTLSEnabled}")
    private Boolean startTLSEnabled;


    public void executeAggiornaGECO() {
        UserContext userContext = new CNRUserContext(GECO, null, LocalDate.now().getYear(), null, null, null);
        try {
            progettoRicercaPadreComponentSession.aggiornaGECO(userContext);
        } catch (Exception e) {
            LOGGER.error("Errore interno del Server Utente: {} with stack trace", GECO, e);
        }
    }

    public void executeDeleteMessaggi() {
        UserContext userContext = new CNRUserContext("MESSAGGI", null, LocalDate.now().getYear(), null, null, null);
        try {
            crudMessaggioComponentSession.deleteMessaggi(userContext);
        } catch (Exception e) {
            LOGGER.error("Cannot delete messaggi", e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.crudMessaggioComponentSession = Optional.ofNullable(EJBCommonServices.createEJB("CNRMESSAGGIO00_EJB_CRUDMessaggioComponentSession"))
                .filter(CRUDMessaggioComponentSession.class::isInstance)
                .map(CRUDMessaggioComponentSession.class::cast)
                .orElseThrow(() -> new DetailedRuntimeException("cannot find ejb CNRMESSAGGIO00_EJB_CRUDMessaggioComponentSession"));
        this.progettoRicercaPadreComponentSession = Optional.ofNullable(EJBCommonServices.createEJB("CNRPROGETTIRIC00_EJB_ProgettoRicercaPadreComponentSession"))
                .filter(ProgettoRicercaPadreComponentSession.class::isInstance)
                .map(ProgettoRicercaPadreComponentSession.class::cast)
                .orElseThrow(() -> new DetailedRuntimeException("cannot find ejb CNRPROGETTIRIC00_EJB_ProgettoRicercaPadreComponentSession"));


    }

    public Integer getAnniResidui() {
        return anniResidui;
    }

    public String getHelpBaseURL() {
        return helpBaseURL;
    }

    public SimplePECMail createSimplePECMail(String userName, String password) {
        SimplePECMail simplePECMail = new SimplePECMail(userName, password);
        simplePECMail.setHostName(pecHostName);
        Optional.ofNullable(sslSmtpPort).ifPresent(s -> simplePECMail.setSslSmtpPort(s));
        Optional.ofNullable(sSLOnConnect).ifPresent(b -> simplePECMail.setSSLOnConnect(b));
        Optional.ofNullable(smtpPort).ifPresent(i -> simplePECMail.setSmtpPort(i));
        Optional.ofNullable(startTLSEnabled).ifPresent(b -> simplePECMail.setStartTLSEnabled(b));
        return simplePECMail;
    }
}
