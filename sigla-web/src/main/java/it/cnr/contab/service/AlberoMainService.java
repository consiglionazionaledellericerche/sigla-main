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

import it.cnr.contab.domain.TreeNode;
import it.cnr.contab.utenze00.bulk.AccessoBulk;
import it.cnr.contab.utenze00.bulk.Albero_mainBulk;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.util.ejb.EJBCommonServices;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AlberoMainService {
    public static final String ROOT = "ROOT";
    private static final Logger LOGGER = LoggerFactory.getLogger(AlberoMainService.class);
    private CRUDComponentSession crudComponentSession;

    @Autowired
    private AccessoService accessoService;

    @PostConstruct
    public void init() {
        this.crudComponentSession = Optional.ofNullable(EJBCommonServices.createEJB("JADAEJB_CRUDComponentSession"))
                .filter(CRUDComponentSession.class::isInstance)
                .map(CRUDComponentSession.class::cast)
                .orElseThrow(() -> new DetailedRuntimeException("cannot find ejb JADAEJB_CRUDComponentSession"));
    }

    @CacheEvict(value = "tree", key = "{#userId, #esercizio, #unitaOrganizzativa}")
    public boolean evictCacheTree(String userId, Integer esercizio, String unitaOrganizzativa) {
        LOGGER.info("Evict cache Tree for User: {}", userId);
        return true;
    }

    @Cacheable(value = "tree", key = "{#userId, #esercizio, #unitaOrganizzativa}")
    public Map<String, List<TreeNode>> tree(UserContext userContext, String userId, Integer esercizio, String unitaOrganizzativa) throws ComponentException, RemoteException {
        LOGGER.info("Start GET Tree for User: {} and Unita Organizzativa: {}", userId, unitaOrganizzativa);
        final UtenteBulk utenteBulk = (UtenteBulk) crudComponentSession.findByPrimaryKey(userContext, new UtenteBulk(userId));
        List<String> accessi = accessoService.accessi(userContext, userId, esercizio, unitaOrganizzativa);
        if (Optional.ofNullable(utenteBulk.getCd_utente_templ()).isPresent()) {
            accessi.addAll(accessoService.accessi(userContext, utenteBulk.getCd_utente_templ(), esercizio, unitaOrganizzativa));
        }

        List<Albero_mainBulk> listLeafs = new ArrayList<>();
        final AtomicInteger counter = new AtomicInteger(0);
        final Collection<List<String>> values = accessi
                .stream()
                .collect(Collectors.groupingBy(s -> counter.getAndIncrement() / 1000))
                .values();
        for (List<String> result : values) {
            listLeafs.addAll(Optional.ofNullable(result).filter(x -> !x.isEmpty())
                    .map(x -> {
                        try {
                            final List<Albero_mainBulk> findAlberoMainByAccessi =
                                    crudComponentSession.find(userContext, Albero_mainBulk.class, "findAlberoMainByAccessi", userContext, x);
                            return findAlberoMainByAccessi.stream();
                        } catch (ComponentException | RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }).orElse(Stream.empty()).collect(Collectors.toList()));
        }
        Stream<Albero_mainBulk> leafs = listLeafs.stream();


        MultiValuedMap<String, Albero_mainBulk> rawMap = new HashSetValuedHashMap<>();
        leafs.forEach(leaf -> visit(leaf, rawMap));
        LOGGER.debug("mappa {}", rawMap);

        Map<String, List<TreeNode>> tree = rawMap
                .keySet()
                .stream()
                .map(id -> Pair.of(id, orderedValues(rawMap.get(id))))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
        LOGGER.debug("tree: {}", tree);
        LOGGER.info("End GET Tree for User: {} and Unita Organizzativa: {}", userId, unitaOrganizzativa);
        return tree;
    }

    private List<TreeNode> orderedValues(Collection<Albero_mainBulk> values) {
        return values
                .stream()
                .sorted(Comparator.comparingInt(node -> {
                    return Optional.ofNullable(node.getPg_ordinamento()).map(Long::intValue).orElse(-1);
                }))
                .map(node -> new TreeNode(
                        node.getCd_nodo(),
                        node.getDs_nodo(),
                        node.getBusiness_process(),
                        Optional.ofNullable(node.getAccesso())
                                .map(AccessoBulk::getCd_accesso)
                                .orElse(null),
                        Optional.ofNullable(node.getAccesso())
                                .map(AccessoBulk::getDs_accesso)
                                .orElse(null),
                        node.getBreadcrumb())
                )
                .map(TreeNode.class::cast)
                .collect(Collectors.toList());
    }

    private void visit(Albero_mainBulk node, MultiValuedMap<String, Albero_mainBulk> m) {
        Albero_mainBulk parent = node.getNodo_padre();
        if (parent == null) {
            m.put(ROOT, node);
        } else {
            String parentCdNodo = parent.getCd_nodo();
            if (m.containsKey(parentCdNodo)) {
                LOGGER.debug("{} gia' visitato", parentCdNodo);
            } else {
                visit(parent, m);
            }
            m.put(parentCdNodo, node);
        }
    }

}