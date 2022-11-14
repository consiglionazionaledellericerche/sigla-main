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

package it.cnr.contab.web.rest.resource.config00;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.messaggio00.bulk.MessaggioBulk;
import it.cnr.contab.service.ContextService;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.Utente_indirizzi_mailBulk;
import it.cnr.contab.web.rest.local.config00.ContextLocal;
import it.cnr.contab.web.rest.model.PreferitiDTOBulk;
import it.cnr.contab.web.rest.model.UtenteIndirizziMailDTO;
import it.cnr.contab.web.rest.resource.util.AbstractResource;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.CRUDComponentSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Stateless
public class ContextResource implements ContextLocal {
    private final Logger LOGGER = LoggerFactory.getLogger(ContextResource.class);
    @Context
    SecurityContext securityContext;

    @EJB
    private CRUDComponentSession crudComponentSession;

    @Override
    public Response esercizi(HttpServletRequest request, String cds) throws Exception {
        try {
            CNRUserContext userContext = AbstractResource.getUserContext(securityContext, request);
            final List<Integer> esercizi = SpringUtil.getBean(ContextService.class)
                    .esercizi(userContext, userContext.getUser(), Optional.ofNullable(userContext.getCd_cds()).orElse(cds));
            return Response.status(Response.Status.OK).entity(esercizi).build();
        } catch (BadRequestException _ex) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @Override
    public Response findUnitaOrganizzativeAbilitate(HttpServletRequest request, String cds) throws Exception {
        try {
            CNRUserContext userContext = AbstractResource.getUserContext(securityContext, request);
            LOGGER.info("GET UO for User: {} CdS: {}", userContext.getUser(), cds);
            final List<Pair<String, String>> result = SpringUtil.getBean(ContextService.class)
                    .listaUnitaOrganizzativeAbilitate(userContext, userContext.getUser(), userContext.getEsercizio(), cds)
                    .stream()
                    .sorted((x,y) -> x.getCd_unita_organizzativa().compareTo(y.getCd_unita_organizzativa()))
                    .map(x -> Pair.of(x.getCd_unita_organizzativa(), x.getDs_unita_organizzativa()))
                    .distinct()
                    .collect(Collectors.toList());
            return Response.status(Response.Status.OK).entity(result).build();
        } catch (BadRequestException _ex) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @Override
    public Response findCdSAbilitati(HttpServletRequest request, String uo) throws Exception {
        try {
            CNRUserContext userContext = AbstractResource.getUserContext(securityContext, request);
            LOGGER.info("GET CdS for User: {} UO: {}", userContext.getUser(), uo);
            final List<Pair<String, String>> result = SpringUtil.getBean(ContextService.class)
                    .listaCdSAbilitati(userContext, userContext.getUser(), userContext.getEsercizio(), uo)
                    .stream()
                    .sorted((x,y) -> x.getCd_unita_organizzativa().compareTo(y.getCd_unita_organizzativa()))
                    .map(x -> Pair.of(x.getCd_unita_organizzativa(), Optional.ofNullable(x.getDs_unita_organizzativa()).orElse(x.getCd_unita_organizzativa())))
                    .distinct()
                    .collect(Collectors.toList());
            return Response.status(Response.Status.OK).entity(result).build();
        } catch (BadRequestException _ex) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @Override
    public Response findCdR(HttpServletRequest request, String uo) throws Exception {
        try {
            CNRUserContext userContext = AbstractResource.getUserContext(securityContext, request);
            LOGGER.info("GET CdR for User: {} UO: {}", userContext.getUser(), uo);
            final List<Pair<String, String>> result = SpringUtil.getBean(ContextService.class)
                    .listaCdR(userContext, userContext.getUser(), userContext.getEsercizio(), uo)
                    .stream()
                    .sorted((x,y) -> x.getCd_centro_responsabilita().compareTo(y.getCd_centro_responsabilita()))
                    .map(x -> Pair.of(x.getCd_centro_responsabilita(), x.getDs_cdr()))
                    .distinct()
                    .collect(Collectors.toList());
            return Response.status(Response.Status.OK).entity(result).build();
        } catch (BadRequestException _ex) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @Override
    public Response findPreferiti(HttpServletRequest request) throws Exception {
        try {
            CNRUserContext userContext = AbstractResource.getUserContext(securityContext, request);
            LOGGER.info("GET Preferiti for User: {}", userContext.getUser());
            return Response.status(Response.Status.OK).entity(
                    SpringUtil.getBean(ContextService.class)
                            .listaPreferiti(userContext)
            ).build();
        } catch (BadRequestException _ex) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @Override
    public Response findMessaggi(HttpServletRequest request) throws Exception {
        try {
            CNRUserContext userContext = AbstractResource.getUserContext(securityContext, request);
            LOGGER.info("GET Messaggi for User: {}", userContext.getUser());
            return Response.status(Response.Status.OK).entity(
                    SpringUtil.getBean(ContextService.class)
                            .listaMessaggi(userContext)
            ).build();
        } catch (BadRequestException _ex) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @Override
    public Response deleteMessaggi(HttpServletRequest request, List<MessaggioBulk> messaggi) throws Exception {
        try {
            CNRUserContext userContext = AbstractResource.getUserContext(securityContext, request);
            LOGGER.info("GET Messaggi for User: {}", userContext.getUser());
            return Response.status(Response.Status.OK).entity(
                    SpringUtil.getBean(ContextService.class)
                            .deleteMessaggi(userContext, messaggi)
            ).build();
        } catch (BadRequestException _ex) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @Override
    public Response indirizziMail(HttpServletRequest request) throws Exception {
        try {
            CNRUserContext userContext = AbstractResource.getUserContext(securityContext, request);
            LOGGER.info("GET Messaggi for User: {}", userContext.getUser());
            final List<Utente_indirizzi_mailBulk> findUtenteIndirizziMail = crudComponentSession.find(
                    userContext,
                    Utente_indirizzi_mailBulk.class,
                    "findUtenteIndirizziMail",
                    userContext
            );
            return Response.status(Response.Status.OK).entity(
                    findUtenteIndirizziMail.stream().map(UtenteIndirizziMailDTO::new).collect(Collectors.toList())
            ).build();
        } catch (BadRequestException _ex) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @Override
    public Response inserisciIndirizziMail(HttpServletRequest request, List<UtenteIndirizziMailDTO> utente_indirizzi_mailBulks) throws Exception {
        CNRUserContext userContext = AbstractResource.getUserContext(securityContext, request);
        utente_indirizzi_mailBulks.stream()
                .forEach(utenteIndirizziMailDTO -> {
                    try {
                        Utente_indirizzi_mailBulk bulk = utenteIndirizziMailDTO.create();
                        if (utenteIndirizziMailDTO.getCrudStatus() == OggettoBulk.TO_BE_UPDATED) {
                            bulk = utenteIndirizziMailDTO.toUtente_indirizzi_mailBulk((Utente_indirizzi_mailBulk) crudComponentSession.inizializzaBulkPerModifica(userContext,bulk));
                            crudComponentSession.modificaConBulk(userContext, bulk);
                        } else if (utenteIndirizziMailDTO.getCrudStatus() == OggettoBulk.TO_BE_CREATED) {
                            bulk = utenteIndirizziMailDTO.toUtente_indirizzi_mailBulk((Utente_indirizzi_mailBulk) crudComponentSession.inizializzaBulkPerInserimento(userContext,bulk));
                            crudComponentSession.creaConBulk(userContext, bulk);
                        }
                    } catch (ComponentException | RemoteException e) {
                        throw new RuntimeException(e);
                    }
                });
        return indirizziMail(request);
    }

    @Override
    public Response inserisciIndirizziMail(HttpServletRequest request) throws Exception {
        return Response.ok()
                .build();
    }

    @Override
    public Response eliminaIndirizziMail(HttpServletRequest request, String indirizzi) throws Exception {
        CNRUserContext userContext = AbstractResource.getUserContext(securityContext, request);
        final List<String> indirizziMails = Arrays.asList(indirizzi.split("/"));
        indirizziMails.stream()
                .map(s -> new Utente_indirizzi_mailBulk(userContext.getUser(), s))
                .map(utente_indirizzi_mailBulk -> {
                    try {
                        return (Utente_indirizzi_mailBulk)crudComponentSession.findByPrimaryKey(userContext, utente_indirizzi_mailBulk);
                    } catch (ComponentException|RemoteException e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(utente_indirizzi_mailBulk -> Optional.ofNullable(utente_indirizzi_mailBulk).isPresent())
                .forEach(utente_indirizzi_mailBulk -> {
                    try {
                        utente_indirizzi_mailBulk.setToBeDeleted();
                        crudComponentSession.eliminaConBulk(userContext, utente_indirizzi_mailBulk);
                    } catch (ComponentException|RemoteException e) {
                        throw new RuntimeException(e);
                    }
                });
        return indirizziMail(request);
    }

}
