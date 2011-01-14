package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaBulk;
/**
 * Insert the type's description here.
 * Creation date: (11/16/2001 5:02:49 PM)
 * @author: Roberto Peli
 */
public class AssociazioniInventarioTable extends it.cnr.jada.bulk.PrimaryKeyHashtable {
/**
 * AccertamentiTable constructor comment.
 */
public AssociazioniInventarioTable() {
	super();
}
/**
 * AccertamentiTable constructor comment.
 * @param initialCapacity int
 */
public AssociazioniInventarioTable(int initialCapacity) {
	super(initialCapacity);
}
/**
 * AccertamentiTable constructor comment.
 * @param initialCapacity int
 * @param loadFactor float
 */
public AssociazioniInventarioTable(int initialCapacity, float loadFactor) {
	super(initialCapacity, loadFactor);
}
/**
 * AccertamentiTable constructor comment.
 * @param t java.util.Map
 */
public AssociazioniInventarioTable(java.util.Map t) {
	super(t);
}
public synchronized Object get(Object key) {

	if (key == null)
		return null;
	Object foundValue = super.get(key);
	if (foundValue == null) {
		Ass_inv_bene_fatturaBulk test = (Ass_inv_bene_fatturaBulk)key;
		for (java.util.Enumeration e = keys(); e.hasMoreElements();) {
			Ass_inv_bene_fatturaBulk ass = (Ass_inv_bene_fatturaBulk)e.nextElement();
			if (test.equalsByPrimaryKey(ass)) {
				foundValue = super.get(ass);
				break;
			}
		}
	}
	return foundValue;
}
public synchronized Ass_inv_bene_fatturaBulk getKey(Ass_inv_bene_fatturaBulk key) {

	Ass_inv_bene_fatturaBulk ass = null;
	if (key != null)
		for (java.util.Enumeration e = keys(); e.hasMoreElements();) {
			Ass_inv_bene_fatturaBulk aKey = (Ass_inv_bene_fatturaBulk)e.nextElement();
			if (aKey.equalsByPrimaryKey(key)) {
				ass = aKey;
				break;
			}
		}
	return ass;
}
}
