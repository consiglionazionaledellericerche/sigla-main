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

package it.cnr.contab.inventario00.docs.bulk;

import it.cnr.contab.inventario01.bulk.Buono_carico_scaricoHome;
/**
 * Insert the type's description here.
 * @author: Rosangela Pucciarelli
 */
public class Aggiornamento_inventarioHome extends Buono_carico_scaricoHome {
/**
 * Trasferimento_inventarioHome constructor comment.
 * @param conn java.sql.Connection
 */
public Aggiornamento_inventarioHome(java.sql.Connection conn) {
	super(conn);
}
/**
 * Trasferimento_inventarioHome constructor comment.
 * @param conn java.sql.Connection
 * @param persistentCache it.cnr.jada.persistency.PersistentCache
 */
public Aggiornamento_inventarioHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(conn, persistentCache);
}

}
