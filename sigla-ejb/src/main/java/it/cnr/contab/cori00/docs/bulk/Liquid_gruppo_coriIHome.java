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
