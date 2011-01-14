package it.cnr.contab.inventario00.docs.bulk;

import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.docamm00.docs.bulk.*;
/**
 * Insert the type's description here.
 * Creation date: (12/20/2001 11:02:49 AM)
 * @author: Gennaro Borriello
 */
public class RigheFatturaTable extends it.cnr.jada.bulk.PrimaryKeyHashtable {
/**
 * AccertamentiTable constructor comment.
 */
public RigheFatturaTable() {
	super();
}
/**
 * AccertamentiTable constructor comment.
 * @param initialCapacity int
 */
public RigheFatturaTable(int initialCapacity) {
	super(initialCapacity);
}
/**
 * AccertamentiTable constructor comment.
 * @param initialCapacity int
 * @param loadFactor float
 */
public RigheFatturaTable(int initialCapacity, float loadFactor) {
	super(initialCapacity, loadFactor);
}
/**
 * AccertamentiTable constructor comment.
 * @param t java.util.Map
 */
public RigheFatturaTable(java.util.Map t) {
	super(t);
}
public synchronized Object get(Object key) {

	if (key == null)
		return null;
	Object foundValue = super.get(key);
	if (foundValue == null) {
		Fattura_passiva_rigaBulk test = (Fattura_passiva_rigaBulk)key;
		for (java.util.Enumeration e = keys(); e.hasMoreElements();) {
			Fattura_passiva_rigaBulk riga_fattura = (Fattura_passiva_rigaBulk)e.nextElement();
			if (test.equalsByPrimaryKey(riga_fattura)) {
				foundValue = super.get(riga_fattura);
				break;
			}
		}
	}
	return foundValue;
}
public synchronized Fattura_passiva_rigaBulk getKey(Fattura_passiva_rigaBulk key) {

	Fattura_passiva_rigaBulk riga_fattura = null;
	if (key != null)
		for (java.util.Enumeration e = keys(); e.hasMoreElements();) {
			Fattura_passiva_rigaBulk aKey = (Fattura_passiva_rigaBulk)e.nextElement();
			if (aKey.equalsByPrimaryKey(key)) {
				riga_fattura = aKey;
				break;
			}
		}
	return riga_fattura;
}
}
