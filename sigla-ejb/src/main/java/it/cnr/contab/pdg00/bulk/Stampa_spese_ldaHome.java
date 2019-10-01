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

package it.cnr.contab.pdg00.bulk;

import it.cnr.jada.persistency.PersistentCache;

/**
 * Insert the type's description here.
 * Creation date: (23/01/2003 16.03.39)
 * @author: Roberto Fantino
 */
public abstract class Stampa_spese_ldaHome extends Pdg_preventivo_spe_detHome {
protected Stampa_spese_ldaHome(Class clazz,java.sql.Connection connection) {
	super(clazz,connection);
}
protected Stampa_spese_ldaHome(Class clazz,java.sql.Connection connection,PersistentCache persistentCache) {
	super(clazz,connection,persistentCache);
}
public Stampa_spese_ldaHome(java.sql.Connection conn) {
	super(Stampa_spese_ldaBulk.class, conn);
}
public Stampa_spese_ldaHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(Stampa_spese_ldaBulk.class, conn, persistentCache);
}
}
