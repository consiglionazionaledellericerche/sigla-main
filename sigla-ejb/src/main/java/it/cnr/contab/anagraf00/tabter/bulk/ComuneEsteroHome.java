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

package it.cnr.contab.anagraf00.tabter.bulk;

import it.cnr.jada.persistency.sql.*;
/**
 * Insert the type's description here.
 * Creation date: (30/10/2002 16.50.28)
 * @author: CNRADM
 */
public class ComuneEsteroHome extends ComuneHome {
/**
 * ComuneEsteroHome constructor.
 *
 * @param conn java.sql.Connection
 */
public ComuneEsteroHome(java.sql.Connection conn) {
	super(ComuneEsteroBulk.class,conn);
}
/**
 * ComuneEsteroHome constructor.
 *
 * @param conn java.sql.Connection
 * @param persistentCache it.cnr.jada.persistency.PersistentCache
 */
public ComuneEsteroHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(ComuneEsteroBulk.class, conn,persistentCache);
}
/**
 * Restituisce tutti i comuni esteri.
 *
 * @return it.cnr.jada.persistency.sql.SQLBuilder
 */
public SQLBuilder createSQLBuilder() {

	SQLBuilder sql = super.createSQLBuilder();
	sql.addClause("AND", "ti_italiano_estero", sql.EQUALS, ComuneBulk.COMUNE_ESTERO);
	sql.addClause("AND", "cd_catastale", sql.EQUALS, ComuneBulk.CODICE_CATASTALE_ESTERO);
	return sql;
}
}
