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

package it.cnr.contab.compensi00.docs.bulk;

/**
 * Insert the type's description here.
 * Creation date: (12/07/2002 10.31.43)
 * @author: Roberto Fantino
 */
public class V_terzo_per_conguaglioHome extends V_terzo_per_compensoHome {
/**
 * V_terzo_per_conguaglioHome constructor comment.
 * @param conn java.sql.Connection
 */
public V_terzo_per_conguaglioHome(java.sql.Connection conn) {
	super(V_terzo_per_conguaglioBulk.class,conn);
}
/**
 * V_terzo_per_conguaglioHome constructor comment.
 * @param conn java.sql.Connection
 * @param persistentCache it.cnr.jada.persistency.PersistentCache
 */
public V_terzo_per_conguaglioHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(V_terzo_per_conguaglioBulk.class,conn,persistentCache);
}
}
