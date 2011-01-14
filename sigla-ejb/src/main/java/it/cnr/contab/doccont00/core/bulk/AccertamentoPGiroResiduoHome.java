package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.persistency.sql.*;
/**
 * Insert the type's description here.
 * Creation date: (18/06/2003 11.41.56)
 * @author: Simonetta Costa
 */
public class AccertamentoPGiroResiduoHome extends AccertamentoPGiroHome {
/**
 * AccertamentoPGiroResiduoHome constructor comment.
 * @param conn java.sql.Connection
 */
public AccertamentoPGiroResiduoHome(java.sql.Connection conn) {
	super(AccertamentoPGiroResiduoBulk.class, conn);
}
/**
 * AccertamentoPGiroResiduoHome constructor comment.
 * @param conn java.sql.Connection
 * @param persistentCache it.cnr.jada.persistency.PersistentCache
 */
public AccertamentoPGiroResiduoHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(AccertamentoPGiroResiduoBulk.class, conn, persistentCache);
}
public SQLBuilder createSQLBuilder() {
	SQLBuilder sql = super.createSQLBuilder();
	sql.addClause( "AND", "fl_pgiro", sql.EQUALS, new Boolean(true));
	sql.addClause( "AND", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_ACR_RES );
	return sql;
}
}
