package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.persistency.sql.*;

public class ObbligazioneOrdHome extends ObbligazioneHome {
public ObbligazioneOrdHome(Class clazz, java.sql.Connection conn) {
	super(clazz, conn);
}
public ObbligazioneOrdHome(Class clazz, java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(clazz, conn, persistentCache);
}
public ObbligazioneOrdHome(java.sql.Connection conn) {
	super(ObbligazioneOrdBulk.class, conn);
}
public ObbligazioneOrdHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(ObbligazioneOrdBulk.class, conn, persistentCache);
}
/**
 * Ritorna un SQLBuilder con la columnMap del ricevente
 */
public SQLBuilder createSQLBuilder() 
{
	SQLBuilder sql = super.createSQLBuilder();
	sql.addClause( "AND", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_OBB );
	sql.addClause( "AND", "fl_pgiro", sql.EQUALS, new Boolean(false) );	
	return sql;
}
}
