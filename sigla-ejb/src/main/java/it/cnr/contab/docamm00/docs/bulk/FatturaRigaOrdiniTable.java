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

package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.contab.ordmag.ordini.bulk.FatturaOrdineBulk;
import it.cnr.jada.bulk.BulkList;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.*;
/**
 * Insert the type's description here.
 * Creation date: (10/19/2001 5:42:52 PM)
 *
 * @author: Marco Spasiano
 */
public class FatturaRigaOrdiniTable extends Hashtable<Fattura_passiva_rigaBulk, BulkList<FatturaOrdineBulk>> {
    /**
     * FatturaRigaOrdiniTable constructor comment.
     */
    public FatturaRigaOrdiniTable() {
        super();
    }

    /**
     * FatturaRigaOrdiniTable constructor comment.
     *
     * @param initialCapacity int
     */
    public FatturaRigaOrdiniTable(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * FatturaRigaOrdiniTable constructor comment.
     *
     * @param initialCapacity int
     * @param loadFactor      float
     */
    public FatturaRigaOrdiniTable(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    /**
     * FatturaRigaOrdiniTable constructor comment.
     *
     * @param t java.util.Map
     */
    public FatturaRigaOrdiniTable(java.util.Map t) {
        super(t);
    }

    public synchronized BulkList<FatturaOrdineBulk> get(Object key) {

        return Optional.ofNullable(key)
                .filter(Fattura_passiva_rigaBulk.class::isInstance)
                .map(Fattura_passiva_rigaBulk.class::cast)
                .map(o -> {
                    return Optional.ofNullable(super.get(o))
                            .orElseGet(() -> {
                                final Stream<Fattura_passiva_rigaBulk> stream = Collections.list(keys()).stream()
                                        .filter(Fattura_passiva_rigaBulk.class::isInstance)
                                        .map(Fattura_passiva_rigaBulk.class::cast);
                                return stream.filter(fattura_passiva_rigaBulk -> fattura_passiva_rigaBulk.equalsByPrimaryKey(o))
                                        .findAny()
                                        .map(fattura_passiva_rigaBulk -> super.get(fattura_passiva_rigaBulk))
                                        .orElse(null);
                            });
                }).orElse(null);
    }

    public synchronized Fattura_passiva_rigaBulk getKey(FatturaOrdineBulk value) {
        return Optional.ofNullable(value)
                .map(o -> {
                    return entrySet().stream()
                            .filter(fattura_passiva_rigaBulkListEntry -> fattura_passiva_rigaBulkListEntry.getValue().contains(value))
                            .map(fattura_passiva_rigaBulkListEntry -> fattura_passiva_rigaBulkListEntry.getKey())
                            .findFirst().orElse(null);
                }).orElse(null);
    }
}
