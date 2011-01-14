package it.cnr.contab.cori00.docs.bulk;

public class Liquid_gruppo_cori_detIHome extends Liquid_gruppo_cori_detHome {
/**
 * Liquid_gruppo_cori_detIHome constructor comment.
 * @param conn java.sql.Connection
 */
public Liquid_gruppo_cori_detIHome(java.sql.Connection conn) {
	super(Liquid_gruppo_cori_detIBulk.class, conn);
}
/**
 * Liquid_gruppo_cori_detIHome constructor comment.
 * @param conn java.sql.Connection
 * @param persistentCache it.cnr.jada.persistency.PersistentCache
 */
public Liquid_gruppo_cori_detIHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(Liquid_gruppo_cori_detIBulk.class, conn, persistentCache);
}
}
