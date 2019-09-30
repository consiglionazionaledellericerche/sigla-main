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

import it.cnr.contab.doccont00.core.bulk.*;
/**
 * Insert the type's description here.
 * Creation date: (11/16/2001 5:02:49 PM)
 * @author: Roberto Peli
 */
public class AccertamentiTable extends it.cnr.jada.bulk.PrimaryKeyHashtable {
/**
 * AccertamentiTable constructor comment.
 */
public AccertamentiTable() {
	super();
}
/**
 * AccertamentiTable constructor comment.
 * @param initialCapacity int
 */
public AccertamentiTable(int initialCapacity) {
	super(initialCapacity);
}
/**
 * AccertamentiTable constructor comment.
 * @param initialCapacity int
 * @param loadFactor float
 */
public AccertamentiTable(int initialCapacity, float loadFactor) {
	super(initialCapacity, loadFactor);
}
/**
 * AccertamentiTable constructor comment.
 * @param t java.util.Map
 */
public AccertamentiTable(java.util.Map t) {
	super(t);
}
public synchronized Object get(Object key) {

	if (key == null)
		return null;
	Object foundValue = super.get(key);
	if (foundValue == null) {
		Accertamento_scadenzarioBulk test = (Accertamento_scadenzarioBulk)key;
		for (java.util.Enumeration e = keys(); e.hasMoreElements();) {
			Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk)e.nextElement();
			if (test.equalsByPrimaryKey(scadenza)) {
				foundValue = super.get(scadenza);
				break;
			}
		}
	}
	return foundValue;
}
public synchronized Accertamento_scadenzarioBulk getKey(Accertamento_scadenzarioBulk key) {

	Accertamento_scadenzarioBulk scadenza = null;
	if (key != null)
		for (java.util.Enumeration e = keys(); e.hasMoreElements();) {
			Accertamento_scadenzarioBulk aKey = (Accertamento_scadenzarioBulk)e.nextElement();
			if (aKey.equalsByPrimaryKey(key)) {
				scadenza = aKey;
				break;
			}
		}
	return scadenza;
}
}
