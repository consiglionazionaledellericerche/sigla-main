package it.cnr.contab.web.rest.resource.util;

import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.doccont00.service.DocumentiContabiliService;
import it.cnr.contab.model.Esito;
import it.cnr.contab.model.Risultato;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utenze00.bp.WSUserContext;
import it.cnr.contab.web.rest.local.util.MessaggiSiopePlusLocal;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Optional;
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
        boolean annullaMandati = Boolean.FALSE, annullaReversali = Boolean.FALSE;
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
                        download, annullaMandati, annullaReversali
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
                        download
                );
                break;
            }
        }
        return Response.ok(result.collect(Collectors.toList())).build();
    }

    @Override
    public Response messaggiSiopeplus(HttpServletRequest request) throws Exception {
        DocumentiContabiliService documentiContabiliService = SpringUtil.getBean("documentiContabiliService", DocumentiContabiliService.class);
        documentiContabiliService.executeMessaggiSiopeplus();
        return Response.ok().build();
    }
}
