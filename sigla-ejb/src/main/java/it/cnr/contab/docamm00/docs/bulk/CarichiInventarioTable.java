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
