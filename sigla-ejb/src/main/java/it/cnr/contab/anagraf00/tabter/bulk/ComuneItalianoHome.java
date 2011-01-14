package it.cnr.contab.anagraf00.tabter.bulk;

import it.cnr.jada.persistency.sql.*;
/**
 * Insert the type's description here.
 * Creation date: (30/10/2002 16.50.55)
 * @author: CNRADM
 */
public class ComuneItalianoHome extends ComuneHome {
/**
 * ComuneItalianoHome constructor comment.
 * @param conn java.sql.Connection
 */
public ComuneItalianoHome(java.sql.Connection conn) {
	super(ComuneItalianoBulk.class, conn);
}
/**
 * ComuneItalianoHome constructor comment.
 * @param conn java.sql.Connection
 * @param persistentCache it.cnr.jada.persistency.PersistentCache
 */
public ComuneItalianoHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(ComuneItalianoBulk.class, conn, persistentCache);
}
/**
 * Restituisce tutti i comuni esteri.
 *
 * @return it.cnr.jada.persistency.sql.SQLBuilder
**/
public SQLBuilder createSQLBuilder() {

	SQLBuilder sql = super.createSQLBuilder();
	sql.addClause("AND", "ti_italiano_estero", sql.EQUALS, ComuneBulk.COMUNE_ITALIANO);
	return sql;
}
}
