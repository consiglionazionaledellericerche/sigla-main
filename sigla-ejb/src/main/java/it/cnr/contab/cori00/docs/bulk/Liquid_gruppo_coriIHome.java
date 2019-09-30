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

package it.cnr.contab.cori00.docs.bulk;

public class Liquid_gruppo_coriIHome extends Liquid_gruppo_coriHome {
/**
 * Liquid_gruppo_corIHome constructor comment.
 * @param conn java.sql.Connection
 */
public Liquid_gruppo_coriIHome(java.sql.Connection conn) {
	super(Liquid_gruppo_coriIBulk.class, conn);
}
/**
 * Liquid_gruppo_corIHome constructor comment.
 * @param conn java.sql.Connection
 * @param persistentCache it.cnr.jada.persistency.PersistentCache
 */
public Liquid_gruppo_coriIHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(Liquid_gruppo_coriIBulk.class, conn, persistentCache);
}
}
