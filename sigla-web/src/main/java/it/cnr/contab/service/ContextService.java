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

package it.cnr.contab.service;

import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.messaggio00.bulk.MessaggioBulk;
import it.cnr.contab.utenze00.bulk.Albero_mainBulk;
import it.cnr.contab.utenze00.bulk.PreferitiAccessoBulk;
import it.cnr.contab.utenze00.bulk.PreferitiBulk;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.web.rest.model.PreferitiDTOBulk;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.ejb.EJBCommonServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.rmi.RemoteException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ContextService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContextService.class);
    public static final String FIND_UNITA_ORGANIZZATIVE_ABILITATE_BY_ACCESSO = "findUnitaOrganizzativeAbilitateByAccesso";
    public static final String FIND_UNITA_ORGANIZZATIVE_ABILITATE_BY_RUOLO = "findUnitaOrganizzativeAbilitateByRuolo";
    public static final String FIND_UNITA_ORGANIZZATIVE_VALIDE = "findUnitaOrganizzativeValide";
    public static final String FIND_ESERCIZI = "findEsercizi";
    public static final String FIND_CDR_BY_UO = "findCdRByUO";
    private CRUDComponentSession crudComponentSession;

    @PostConstruct
    public void init() {
        this.crudComponentSession = Optional.ofNullable(EJBCommonServices.createEJB("JADAEJB_CRUDComponentSession"))
                .filter(CRUDComponentSession.class::isInstance)
                .map(CRUDComponentSession.class::cast)
                .orElseThrow(() -> new DetailedRuntimeException("cannot find ejb JADAEJB_CRUDComponentSession"));
    }

    public List<Integer> esercizi(UserContext userContext, String userId, String cds) throws ComponentException, RemoteException {
        LOGGER.info("Start Esercizi for User: {} and CdS: {}", userId, cds);
        final List<EsercizioBulk> esercizioBulks =
                crudComponentSession.find(userContext, EsercizioBulk.class, FIND_ESERCIZI, userContext, cds);
        LOGGER.info("End Esercizi for User: {} and CdS: {}", userId, cds);
        return esercizioBulks
                .stream()
                .map(EsercizioBulk::getEsercizio)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    public List<Unita_organizzativaBulk> listaUnitaOrganizzativeAbilitate(UserContext userContext, String userId, Integer esercizio, String cds) throws ComponentException, RemoteException {
        final UtenteBulk utenteBulk =
                Optional.ofNullable(crudComponentSession.findByPrimaryKey(userContext, new UtenteBulk(userId)))
                        .filter(UtenteBulk.class::isInstance)
                        .map(UtenteBulk.class::cast)
                        .orElseThrow(() -> new ComponentException("User not found " + userId));
        Stream<Unita_organizzativaBulk> uos = Stream.empty();
        if (utenteBulk.isSupervisore()) {
            uos = crudComponentSession.find(userContext, Unita_organizzativaBulk.class, FIND_UNITA_ORGANIZZATIVE_VALIDE, userContext, esercizio, cds, Boolean.FALSE)
                    .stream()
                    .map(Unita_organizzativaBulk.class::cast);
        } else {
            uos = Stream.concat(
                    crudComponentSession.find(userContext, Unita_organizzativaBulk.class, FIND_UNITA_ORGANIZZATIVE_ABILITATE_BY_ACCESSO, userContext, userContext.getUser(), esercizio, cds, Boolean.FALSE)
                            .stream()
                            .map(Unita_organizzativaBulk.class::cast),
                    crudComponentSession.find(userContext, Unita_organizzativaBulk.class, FIND_UNITA_ORGANIZZATIVE_ABILITATE_BY_RUOLO, userContext, userContext.getUser(), esercizio, cds, Boolean.FALSE)
                            .stream()
                            .map(Unita_organizzativaBulk.class::cast)
            );
            if (Optional.ofNullable(utenteBulk.getCd_utente_templ()).isPresent()) {
                uos = Stream.concat(uos, Stream.concat(
                        crudComponentSession.find(userContext, Unita_organizzativaBulk.class, FIND_UNITA_ORGANIZZATIVE_ABILITATE_BY_ACCESSO, userContext, utenteBulk.getCd_utente_templ(), esercizio, cds, Boolean.FALSE)
                                .stream()
                                .map(Unita_organizzativaBulk.class::cast),
                        crudComponentSession.find(userContext, Unita_organizzativaBulk.class, FIND_UNITA_ORGANIZZATIVE_ABILITATE_BY_RUOLO, userContext, utenteBulk.getCd_utente_templ(), esercizio, cds, Boolean.FALSE)
                                .stream()
                                .map(Unita_organizzativaBulk.class::cast)
                        )
                );
            }
        }
        return uos.collect(Collectors.toList());
    }

    public List<CdsBulk> listaCdSAbilitati(UserContext userContext, String userId, Integer esercizio, String uo) throws ComponentException, RemoteException {
        final UtenteBulk utenteBulk =
                Optional.ofNullable(crudComponentSession.findByPrimaryKey(userContext, new UtenteBulk(userId)))
                        .filter(UtenteBulk.class::isInstance)
                        .map(UtenteBulk.class::cast)
                        .orElseThrow(() -> new ComponentException("User not found" + userId));
        Stream<CdsBulk> cdss = Stream.empty();
        if (utenteBulk.isSupervisore()) {
            cdss = crudComponentSession.find(userContext, Unita_organizzativaBulk.class, FIND_UNITA_ORGANIZZATIVE_VALIDE, userContext, esercizio, uo, Boolean.TRUE)
                    .stream()
                    .map(Unita_organizzativaBulk.class::cast)
                    .map(Unita_organizzativaBulk::getUnita_padre);
        } else {
            cdss = Stream.concat(
                    crudComponentSession.find(userContext, Unita_organizzativaBulk.class, FIND_UNITA_ORGANIZZATIVE_ABILITATE_BY_ACCESSO, userContext, userContext.getUser(), esercizio, uo, Boolean.TRUE)
                            .stream()
                            .map(Unita_organizzativaBulk.class::cast)
                            .map(Unita_organizzativaBulk::getUnita_padre),
                    crudComponentSession.find(userContext, Unita_organizzativaBulk.class, FIND_UNITA_ORGANIZZATIVE_ABILITATE_BY_RUOLO, userContext, userContext.getUser(), esercizio, uo, Boolean.TRUE)
                            .stream()
                            .map(Unita_organizzativaBulk.class::cast)
                            .map(Unita_organizzativaBulk::getUnita_padre)
            );
            if (Optional.ofNullable(utenteBulk.getCd_utente_templ()).isPresent()) {
                cdss = Stream.concat(cdss, Stream.concat(
                                crudComponentSession.find(userContext, Unita_organizzativaBulk.class, FIND_UNITA_ORGANIZZATIVE_ABILITATE_BY_ACCESSO, userContext, utenteBulk.getCd_utente_templ(), esercizio, uo, Boolean.TRUE)
                                        .stream()
                                        .map(Unita_organizzativaBulk.class::cast)
                                        .map(Unita_organizzativaBulk::getUnita_padre),
                                crudComponentSession.find(userContext, Unita_organizzativaBulk.class, FIND_UNITA_ORGANIZZATIVE_ABILITATE_BY_RUOLO, userContext, utenteBulk.getCd_utente_templ(), esercizio, uo, Boolean.TRUE)
                                        .stream()
                                        .map(Unita_organizzativaBulk.class::cast)
                                        .map(Unita_organizzativaBulk::getUnita_padre)
                        )
                );
            }
        }
        return cdss.collect(Collectors.toList());
    }

    public List<CdrBulk> listaCdR(UserContext userContext, String userId, Integer esercizio, String uo) throws ComponentException, RemoteException {
        return crudComponentSession.find(userContext, CdrBulk.class, FIND_CDR_BY_UO, userContext, esercizio, uo);
    }

    public List<PreferitiDTOBulk> listaPreferiti(UserContext userContext) throws ComponentException, RemoteException {
        final List<PreferitiAccessoBulk> preferitiBulks = crudComponentSession.find(userContext, PreferitiAccessoBulk.class, "findByUser", userContext);
        return preferitiBulks.stream()
                .map(preferitiBulk -> {
                    try {
                        Optional<Albero_mainBulk> albero_mainBulk =
                                crudComponentSession
                                .find(
                                        userContext,
                                        Albero_mainBulk.class,
                                        "findNodo",
                                        userContext,
                                        preferitiBulk.getBusiness_process(),
                                        preferitiBulk.getAssBpAccessoBulk().getCdAccesso()
                                )
                                .stream()
                                .findAny().map(Albero_mainBulk.class::cast);
                        if (albero_mainBulk.isPresent()) {
                            return new PreferitiDTOBulk(albero_mainBulk.get().getCd_nodo(), preferitiBulk.getDescrizione());
                        }
                    } catch (ComponentException|RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    return null;
                })
                .filter(preferitiDTOBulk -> Optional.ofNullable(preferitiDTOBulk).isPresent())
                .distinct()
                .collect(Collectors.toList());
    }

    public List<MessaggioBulk> listaMessaggi(UserContext userContext) throws ComponentException, RemoteException {
        return crudComponentSession.find(userContext, MessaggioBulk.class, "findMessaggiByUser", userContext);
    }

    public List<MessaggioBulk> deleteMessaggi(UserContext userContext, List<MessaggioBulk> messaggi) throws ComponentException, RemoteException {
        messaggi.stream()
                .filter(messaggio -> Optional.ofNullable(messaggio.getCd_utente()).filter(s -> s.equals(userContext.getUser())).isPresent())
                .forEach(messaggio -> {
                    try {
                        messaggio.setCrudStatus(MessaggioBulk.TO_BE_DELETED);
                        crudComponentSession.eliminaConBulk(userContext, messaggio);
                    } catch (ComponentException|RemoteException e) {
                        throw new RuntimeException(e);
                    }
                });
        return crudComponentSession.find(userContext, MessaggioBulk.class, "findMessaggiByUser", userContext);
    }

}
