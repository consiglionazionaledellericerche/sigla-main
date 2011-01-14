package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.persistency.sql.*;
/**
 * Insert the type's description here.
 * Creation date: (18/06/2003 11.48.15)
 * @author: Simonetta Costa
 */
public class ImpegnoPGiroResiduoHome extends ImpegnoPGiroHome {
/**
 * ImpegnoPGiroResiduoHome constructor comment.
 * @param conn java.sql.Connection
 */
public ImpegnoPGiroResiduoHome(java.sql.Connection conn) {
	super(ImpegnoPGiroResiduoBulk.class, conn);
}
/**
 * ImpegnoPGiroResiduoHome constructor comment.
 * @param conn java.sql.Connection
 * @param persistentCache it.cnr.jada.persistency.PersistentCache
 */
public ImpegnoPGiroResiduoHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(ImpegnoPGiroResiduoBulk.class, conn, persistentCache);
}
public SQLBuilder createSQLBuilder()
{
	SQLBuilder sql = super.createSQLBuilder();
	sql.addClause( "AND", "fl_pgiro", sql.EQUALS, new Boolean(true));
	sql.addClause( "AND", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_IMP_RES );
	return sql;
}
}
