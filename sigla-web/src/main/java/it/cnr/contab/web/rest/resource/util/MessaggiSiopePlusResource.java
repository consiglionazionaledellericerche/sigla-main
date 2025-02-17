/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
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

package it.cnr.contab.web.rest.resource.util;

import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.doccont00.service.DocumentiContabiliService;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utenze00.bp.WSUserContext;
import it.cnr.contab.web.rest.local.util.MessaggiSiopePlusLocal;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.si.siopeplus.model.Esito;
import it.cnr.si.siopeplus.model.MessaggioXML;
import it.cnr.si.siopeplus.model.Risultato;
import it.cnr.si.siopeplus.service.GiornaleDiCassaSiopePlusService;
import it.cnr.si.siopeplus.service.OrdinativiSiopePlusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class MessaggiSiopePlusResource implements MessaggiSiopePlusLocal {
    private transient static final Logger logger = LoggerFactory.getLogger(MessaggiSiopePlusResource.class);
    @EJB
    private Configurazione_cnrComponentSession configurazione_cnrComponentSession;

    @Override
    public Response esito(HttpServletRequest request, Esito esito, String dataDa, String dataA, Boolean download) throws Exception {
        DocumentiContabiliService documentiContabiliService = SpringUtil.getBean("documentiContabiliService", DocumentiContabiliService.class);
        UserContext userContext = new WSUserContext("SIOPEPLUS", null,
                new Integer(Calendar.getInstance().get(Calendar.YEAR)),
                null, null, null);
        boolean annullaMandati = Boolean.FALSE, annullaReversali = Boolean.FALSE, riportaMandatoDaFirmare = Boolean.FALSE;
        try {
            annullaMandati =
                    Optional.ofNullable(configurazione_cnrComponentSession.getVal01(
                            userContext,
                            Calendar.getInstance().get(Calendar.YEAR),
                            "*",
                            Configurazione_cnrBulk.PK_FLUSSO_ORDINATIVI,
                            Configurazione_cnrBulk.SK_ANNULLA_MANDATI
                    ))
                            .map(s -> Boolean.valueOf(s))
                            .orElse(Boolean.FALSE);
            riportaMandatoDaFirmare =
                    Optional.ofNullable(configurazione_cnrComponentSession.getVal01(
                            userContext,
                            Calendar.getInstance().get(Calendar.YEAR),
                            "*",
                            Configurazione_cnrBulk.PK_FLUSSO_ORDINATIVI,
                            Configurazione_cnrBulk.SK_RIPORTA_MANDATO_DAFIRMARE
                    ))
                            .map(s -> Boolean.valueOf(s))
                            .orElse(Boolean.FALSE);

            annullaReversali =
                    Optional.ofNullable(configurazione_cnrComponentSession.getVal01(
                            userContext,
                            Calendar.getInstance().get(Calendar.YEAR),
                            "*",
                            Configurazione_cnrBulk.PK_FLUSSO_ORDINATIVI,
                            Configurazione_cnrBulk.SK_ANNULLA_REVERSALI
                    ))
                            .map(s -> Boolean.valueOf(s))
                            .orElse(Boolean.FALSE);
        } catch (ComponentException | RemoteException _ex) {
            logger.error("SIOPE+ ESITO ERROR recupera configurazione_cnr per annullamento Mandati e Reversali", _ex);
        }

        Stream<Risultato> result = Stream.empty();
        switch (esito) {
            case ACK: {
                result = documentiContabiliService.downloadMessaggiACK(
                        Optional.ofNullable(dataDa)
                                .map(s -> LocalDateTime.parse(dataDa, DateTimeFormatter.ISO_DATE_TIME))
                                .orElse(null),
                        Optional.ofNullable(dataA)
                                .map(s -> LocalDateTime.parse(dataA, DateTimeFormatter.ISO_DATE_TIME))
                                .orElse(null),
                        download
                );
                break;
            }
            case ESITO: {
                result = documentiContabiliService.downloadMessaggiEsito(
                        Optional.ofNullable(dataDa)
                                .map(s -> LocalDateTime.parse(dataDa, DateTimeFormatter.ISO_DATE_TIME))
                                .orElse(null),
                        Optional.ofNullable(dataA)
                                .map(s -> LocalDateTime.parse(dataA, DateTimeFormatter.ISO_DATE_TIME))
                                .orElse(null),
                        download
                );
                break;
            }
            case ESITOAPPLICATIVO: {
                result = documentiContabiliService.downloadMessaggiEsitoApplicativo(
                        Optional.ofNullable(dataDa)
                                .map(s -> LocalDateTime.parse(dataDa, DateTimeFormatter.ISO_DATE_TIME))
                                .orElse(null),
                        Optional.ofNullable(dataA)
                                .map(s -> LocalDateTime.parse(dataA, DateTimeFormatter.ISO_DATE_TIME))
                                .orElse(null),
                        download, annullaMandati, annullaReversali, riportaMandatoDaFirmare
                );
                break;
            }
            case GIORNALEDICASSA: {
                result = documentiContabiliService.downloadGiornalieraDiCassa(
                        Optional.ofNullable(dataDa)
                                .map(s -> LocalDateTime.parse(dataDa, DateTimeFormatter.ISO_DATE_TIME))
                                .orElse(null),
                        Optional.ofNullable(dataA)
                                .map(s -> LocalDateTime.parse(dataA, DateTimeFormatter.ISO_DATE_TIME))
                                .orElse(null),
                        download,
                        DocumentiContabiliService.SIOPEPLUS
                );
                break;
            }
        }
        return Response.ok(result.collect(Collectors.toList())).build();
    }

    @Override
    public Response downloadxml(HttpServletRequest request, Esito esito, String dataDa, String dataA, Boolean download, String localFolder) throws Exception {
        OrdinativiSiopePlusService ordinativiSiopePlusService = SpringUtil.getBean("ordinativiSiopePlusService", OrdinativiSiopePlusService.class);

        final Stream<Risultato> risultatoStream = Optional.ofNullable(ordinativiSiopePlusService.getAllMessaggi(esito, Optional.ofNullable(dataDa)
                        .map(s -> LocalDateTime.parse(dataDa, DateTimeFormatter.ISO_DATE_TIME))
                        .orElse(null),
                Optional.ofNullable(dataA)
                        .map(s -> LocalDateTime.parse(dataA, DateTimeFormatter.ISO_DATE_TIME))
                        .orElse(null), download, null))
                .orElseGet(() -> Collections.emptyList())
                .stream()
                .map(risultato -> {
                    try {
                        final MessaggioXML<Object> messaggioXML =
                                ordinativiSiopePlusService.getLocation(risultato.getLocation(), Object.class);
                        Files.write(
                                Files.createFile(
                                        Paths.get(localFolder.concat(File.separator).concat(UUID.randomUUID().toString()).concat("-").concat(messaggioXML.getName()))),
                                messaggioXML.getContent()
                        );
                    } catch (Exception _ex) {
                        logger.error("SIOPE+ ERROR for risultato: {}", risultato, _ex);
                    }
                    return risultato;
                });
        if (esito.equals(Esito.GIORNALEDICASSA)) {
            GiornaleDiCassaSiopePlusService giornaleDiCassaSiopePlusService = SpringUtil.getBean("giornaleDiCassaSiopePlusService", GiornaleDiCassaSiopePlusService.class);
            Stream<Risultato> risultatoGiornaliera = Optional.ofNullable(giornaleDiCassaSiopePlusService.getListaMessaggi(Optional.ofNullable(dataDa)
                                    .map(s -> LocalDateTime.parse(dataDa, DateTimeFormatter.ISO_DATE_TIME))
                                    .orElse(null),
                            Optional.ofNullable(dataA)
                                    .map(s -> LocalDateTime.parse(dataA, DateTimeFormatter.ISO_DATE_TIME))
                                    .orElse(null), download, null).getRisultati()).orElseGet(() -> Collections.emptyList())
                    .stream()
                    .map(risultato -> {
                        try {
                            final MessaggioXML<Object> messaggioXML =
                                    giornaleDiCassaSiopePlusService.getLocation(risultato.getLocation(), Object.class);
                                Files.write(
                                        Files.createFile(
                                                Paths.get(localFolder.concat(File.separator).concat(messaggioXML.getName()))),
                                        messaggioXML.getContent()
                                );
                        } catch (Exception _ex) {
                            logger.error("SIOPE+ ERROR for risultato: {}", risultato, _ex);
                        }
                        return risultato;
                    });
            return Response.ok(Stream.concat(risultatoStream, risultatoGiornaliera).collect(Collectors.toList())).build();
        }
        return Response.ok(risultatoStream.collect(Collectors.toList())).build();
    }

    @Override
    public Response messaggiSiopeplus(HttpServletRequest request) throws Exception {
        DocumentiContabiliService documentiContabiliService = SpringUtil.getBean("documentiContabiliService", DocumentiContabiliService.class);
        documentiContabiliService.executeMessaggiSiopeplus();
        return Response.ok().build();
    }
}
