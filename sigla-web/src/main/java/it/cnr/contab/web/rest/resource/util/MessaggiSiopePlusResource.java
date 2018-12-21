package it.cnr.contab.web.rest.resource.util;

import it.cnr.contab.doccont00.service.DocumentiContabiliService;
import it.cnr.contab.model.Esito;
import it.cnr.contab.model.Risultato;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.web.rest.local.util.MessaggiSiopePlusLocal;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class MessaggiSiopePlusResource implements MessaggiSiopePlusLocal {

    @Override
    public Response esito(HttpServletRequest request, Esito esito, String dataDa, String dataA, Boolean download) throws Exception {
        DocumentiContabiliService documentiContabiliService = SpringUtil.getBean("documentiContabiliService", DocumentiContabiliService.class);
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
                        download
                );
                break;
            }
        }
        return Response.ok(result.collect(Collectors.toList())).build();
    }
}
