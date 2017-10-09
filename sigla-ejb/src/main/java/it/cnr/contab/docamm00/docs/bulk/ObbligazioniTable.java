package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;

/**
 * Insert the type's description here.
 * Creation date: (10/19/2001 5:42:52 PM)
 *
 * @author: Roberto Peli
 */
public class ObbligazioniTable extends it.cnr.jada.bulk.PrimaryKeyHashtable {
    /**
     * ObbligazioniTable constructor comment.
     */
    public ObbligazioniTable() {
        super();
    }

    /**
     * ObbligazioniTable constructor comment.
     *
     * @param initialCapacity int
     */
    public ObbligazioniTable(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * ObbligazioniTable constructor comment.
     *
     * @param initialCapacity int
     * @param loadFactor      float
     */
    public ObbligazioniTable(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    /**
     * ObbligazioniTable constructor comment.
     *
     * @param t java.util.Map
     */
    public ObbligazioniTable(java.util.Map t) {
        super(t);
    }

    public synchronized Object get(Object key) {

        if (key == null)
            return null;
        Object foundValue = super.get(key);
        if (foundValue == null) {
            Obbligazione_scadenzarioBulk test = (Obbligazione_scadenzarioBulk) key;
            for (java.util.Enumeration e = keys(); e.hasMoreElements(); ) {
                Obbligazione_scadenzarioBulk obbl = (Obbligazione_scadenzarioBulk) e.nextElement();
                if (test.equalsByPrimaryKey(obbl)) {
                    foundValue = super.get(obbl);
                    break;
                }
            }
        }
        return foundValue;
    }

    public synchronized Obbligazione_scadenzarioBulk getKey(Obbligazione_scadenzarioBulk key) {

        Obbligazione_scadenzarioBulk scadenza = null;
        if (key != null)
            for (java.util.Enumeration e = keys(); e.hasMoreElements(); ) {
                Obbligazione_scadenzarioBulk aKey = (Obbligazione_scadenzarioBulk) e.nextElement();
                if (aKey.equalsByPrimaryKey(key)) {
                    scadenza = aKey;
                    break;
                }
            }
        return scadenza;
    }
}
