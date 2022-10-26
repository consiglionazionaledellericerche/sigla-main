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

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.utenze00.bulk.Ruolo_accessoBulk;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.utenze00.bulk.Utente_unita_accessoBulk;
import it.cnr.contab.utenze00.bulk.Utente_unita_ruoloBulk;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.util.ejb.EJBCommonServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AccessoService {
    public static final String FIND_RUOLI_BY_CD_UTENTE = "findRuoliByCdUtente";
    public static final String FIND_CODICE_UO_PARENTS = "findCodiceUoParents";
    public static final String FIND_ACCESSI_BY_CD_UTENTE = "findAccessiByCdUtente";
    private static final Logger LOGGER = LoggerFactory.getLogger(AccessoService.class);
    private CRUDComponentSession crudComponentSession;

    @PostConstruct
    public void init() {
        this.crudComponentSession = Optional.ofNullable(EJBCommonServices.createEJB("JADAEJB_CRUDComponentSession"))
                .filter(CRUDComponentSession.class::isInstance)
                .map(CRUDComponentSession.class::cast)
                .orElseThrow(() -> new DetailedRuntimeException("cannot find ejb JADAEJB_CRUDComponentSession"));
    }

    public List<String> accessi(UserContext userContext, String userId, Integer esercizio, String unitaOrganizzativa) throws ComponentException, RemoteException {
        LOGGER.info("Start Accessi for User: {} esercizio {} and Unita Organizzativa: {}", userId, esercizio, unitaOrganizzativa);
        final List<Utente_unita_ruoloBulk> utente_unita_ruoloBulks =
                crudComponentSession.find(userContext, Utente_unita_ruoloBulk.class, FIND_RUOLI_BY_CD_UTENTE, userContext, userId, unitaOrganizzativa);
        final List<Utente_unita_accessoBulk> utente_unita_accessoBulks =
                crudComponentSession.find(userContext, Utente_unita_accessoBulk.class, FIND_ACCESSI_BY_CD_UTENTE, userContext, userId, esercizio, unitaOrganizzativa);

        final List<Unita_organizzativaBulk> unita_organizzativaBulks =
                Optional.ofNullable(unitaOrganizzativa)
                                .map(s -> {
                                    try {
                                        List<Unita_organizzativaBulk> result = crudComponentSession.find(
                                                userContext,
                                                Unita_organizzativaBulk.class,
                                                FIND_CODICE_UO_PARENTS,
                                                userContext,
                                                esercizio,
                                                s
                                        );
                                        return result;
                                    } catch (ComponentException|RemoteException e) {
                                        throw new RuntimeException(e);
                                    }
                                }).orElse(Collections.emptyList());

        unita_organizzativaBulks.stream()
                .map(Unita_organizzativaBulk::getCd_unita_organizzativa)
                .forEach(parentUO -> {
                    try {
                        utente_unita_ruoloBulks.addAll(
                                crudComponentSession.find(
                                        userContext, Utente_unita_ruoloBulk.class, FIND_RUOLI_BY_CD_UTENTE, userContext, userId, parentUO
                                )
                        );
                        utente_unita_accessoBulks.addAll(
                                crudComponentSession.find(
                                        userContext, Utente_unita_accessoBulk.class, FIND_ACCESSI_BY_CD_UTENTE, userContext, userId, esercizio, parentUO
                                )
                        );
                    } catch (ComponentException | RemoteException e) {
                        throw new RuntimeException(e);
                    }
                });
        final UtenteBulk utenteBulk = (UtenteBulk) crudComponentSession.findByPrimaryKey(userContext, new UtenteBulk(userId));
        List<String> findRuoliByCdUtente = utente_unita_ruoloBulks.stream().map(Utente_unita_ruoloBulk::getCd_ruolo).collect(Collectors.toList());
        List<String> findAccessiByCdUtente = utente_unita_accessoBulks.stream().map(Utente_unita_accessoBulk::getCd_accesso).collect(Collectors.toList());
        Optional.of(utenteBulk.getFl_supervisore())
                .filter(isUtenteSupervisore -> isUtenteSupervisore)
                .filter(aBoolean -> Optional.ofNullable(utenteBulk.getCd_ruolo_supervisore()).isPresent())
                .ifPresent(aBoolean -> {
                    findRuoliByCdUtente.add(utenteBulk.getCd_ruolo_supervisore());
                });
        final List<String> result = Stream.concat(
                findAccessiByCdUtente.stream(),
                Optional.ofNullable(findRuoliByCdUtente).filter(x -> !x.isEmpty())
                        .map(x -> {
                            try {
                                final List<Ruolo_accessoBulk> findAccessiByRuoli = crudComponentSession.find(
                                        userContext, Ruolo_accessoBulk.class, "findAccessiByRuoli", userContext, esercizio, findRuoliByCdUtente
                                );
                                return findAccessiByRuoli.stream().map(Ruolo_accessoBulk::getCd_accesso);
                            } catch (ComponentException | RemoteException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .orElse(Stream.empty())
        ).distinct().collect(Collectors.toList());
        LOGGER.info("End Accessi for User: {} esercizio {} and Unita Organizzativa: {}", userId, esercizio, unitaOrganizzativa);
        return result;
    }
}
