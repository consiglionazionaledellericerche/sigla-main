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

package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.persistency.sql.SQLBuilder;
/**
 * Insert the type's description here.
 * Creation date: (26/09/2001 11.37.02)
 * @author: Simonetta Costa
 */
public class V_persona_fisicaHome extends V_anagrafico_terzoHome {
/**
 * V_persona_fisicaHome constructor comment.
 * @param conn java.sql.Connection
 */
public V_persona_fisicaHome(java.sql.Connection conn) {
	super(V_persona_fisicaBulk.class, conn);
}
/**
 * V_persona_fisicaHome constructor comment.
 * @param conn java.sql.Connection
 * @param persistentCache it.cnr.jada.persistency.PersistentCache
 */
public V_persona_fisicaHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(V_persona_fisicaBulk.class, conn, persistentCache);
}
/**
 * Ritorna un SQLBuilder con la columnMap del ricevente
 */
public SQLBuilder createSQLBuilder() {
	SQLBuilder sql = super.createSQLBuilder(); 
	sql.addClause( "AND" , "ti_entita", SQLBuilder.EQUALS, AnagraficoBulk.FISICA );
	sql.addClause( "AND" , "dt_fine_rapporto", SQLBuilder.ISNULL, null );
	return sql;
}
}
