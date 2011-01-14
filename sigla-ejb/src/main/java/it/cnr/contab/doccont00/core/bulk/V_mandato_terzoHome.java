package it.cnr.contab.doccont00.core.bulk;

/**
 * Insert the type's description here.
 * Creation date: (10/12/2002 15.02.53)
 * @author: Ilaria Gorla
 */
public class V_mandato_terzoHome extends MandatoIHome {
/**
 * V_manadato_terzoHome constructor comment.
 * @param conn java.sql.Connection
 */
public V_mandato_terzoHome(java.sql.Connection conn) {
	super(V_mandato_terzoBulk.class, conn);
}
/**
 * V_manadato_terzoHome constructor comment.
 * @param conn java.sql.Connection
 * @param persistentCache it.cnr.jada.persistency.PersistentCache
 */
public V_mandato_terzoHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(V_mandato_terzoBulk.class, conn, persistentCache);
}
}
