package it.cnr.contab.doccont00.core.bulk;

public class V_reversale_terzoHome extends ReversaleIHome {
/**
 * V_reversale_terzoHome constructor comment.
 * @param conn java.sql.Connection
 */
public V_reversale_terzoHome(java.sql.Connection conn) {
	super(V_reversale_terzoBulk.class, conn);
}
/**
 * V_reversale_terzoHome constructor comment.
 * @param conn java.sql.Connection
 * @param persistentCache it.cnr.jada.persistency.PersistentCache
 */
public V_reversale_terzoHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(V_reversale_terzoBulk.class, conn, persistentCache);
}
}
