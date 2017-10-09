package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.ordmag.ordini.bulk.FatturaOrdineBulk;
import it.cnr.jada.bulk.PrimaryKeyHashtable;

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
public class FatturaRigaOrdiniTable extends Hashtable<Fattura_passiva_rigaBulk, List<FatturaOrdineBulk>> {
    /**
     * ObbligazioniTable constructor comment.
     */
    public FatturaRigaOrdiniTable() {
        super();
    }

    /**
     * ObbligazioniTable constructor comment.
     *
     * @param initialCapacity int
     */
    public FatturaRigaOrdiniTable(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * ObbligazioniTable constructor comment.
     *
     * @param initialCapacity int
     * @param loadFactor      float
     */
    public FatturaRigaOrdiniTable(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    /**
     * ObbligazioniTable constructor comment.
     *
     * @param t java.util.Map
     */
    public FatturaRigaOrdiniTable(java.util.Map t) {
        super(t);
    }

    public synchronized List<FatturaOrdineBulk> get(Object key) {

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

    public synchronized Fattura_passiva_rigaBulk getKey(Fattura_passiva_rigaBulk key) {

        return Optional.ofNullable(key)
                .filter(Fattura_passiva_rigaBulk.class::isInstance)
                .map(Fattura_passiva_rigaBulk.class::cast)
                .map(o -> {
                    final Stream<Fattura_passiva_rigaBulk> stream = Collections.list(keys()).stream()
                            .filter(Fattura_passiva_rigaBulk.class::isInstance)
                            .map(Fattura_passiva_rigaBulk.class::cast);
                    return stream.filter(fattura_passiva_rigaBulk -> fattura_passiva_rigaBulk.equalsByPrimaryKey(o))
                            .findAny()
                            .orElse(null);
                }).orElse(null);
    }
}
