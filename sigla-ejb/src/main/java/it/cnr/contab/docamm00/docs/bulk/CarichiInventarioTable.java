package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk;
/**
 * Insert the type's description here.
 * Creation date: (11/16/2001 5:02:49 PM)
 * @author: Roberto Peli
 */
public class CarichiInventarioTable extends it.cnr.jada.bulk.PrimaryKeyHashtable {
/**
 * AccertamentiTable constructor comment.
 */
public CarichiInventarioTable() {
	super();
}
/**
 * AccertamentiTable constructor comment.
 * @param initialCapacity int
 */
public CarichiInventarioTable(int initialCapacity) {
	super(initialCapacity);
}
/**
 * AccertamentiTable constructor comment.
 * @param initialCapacity int
 * @param loadFactor float
 */
public CarichiInventarioTable(int initialCapacity, float loadFactor) {
	super(initialCapacity, loadFactor);
}
/**
 * AccertamentiTable constructor comment.
 * @param t java.util.Map
 */
public CarichiInventarioTable(java.util.Map t) {
	super(t);
}
public synchronized Object get(Object key) {

	if (key == null)
		return null;
	Object foundValue = super.get(key);
	if (foundValue == null) {
		Buono_carico_scaricoBulk test = (Buono_carico_scaricoBulk)key;
		for (java.util.Enumeration e = keys(); e.hasMoreElements();) {
			Buono_carico_scaricoBulk buonoCS = (Buono_carico_scaricoBulk)e.nextElement();
			if (test.equalsByPrimaryKey(buonoCS)) {
				foundValue = super.get(buonoCS);
				break;
			}
		}
	}
	return foundValue;
}
public synchronized Buono_carico_scaricoBulk getKey(Buono_carico_scaricoBulk key) {

	Buono_carico_scaricoBulk buonoCS = null;
	if (key != null)
		for (java.util.Enumeration e = keys(); e.hasMoreElements();) {
			Buono_carico_scaricoBulk aKey = (Buono_carico_scaricoBulk)e.nextElement();
			if (aKey.equalsByPrimaryKey(key)) {
				buonoCS = aKey;
				break;
			}
		}
	return buonoCS;
}
}
