package it.cnr.contab.compensi00.docs.bulk;

/**
 * Insert the type's description here.
 * Creation date: (12/07/2002 10.31.43)
 * @author: Roberto Fantino
 */
public class V_terzo_per_conguaglioHome extends V_terzo_per_compensoHome {
/**
 * V_terzo_per_conguaglioHome constructor comment.
 * @param conn java.sql.Connection
 */
public V_terzo_per_conguaglioHome(java.sql.Connection conn) {
	super(V_terzo_per_conguaglioBulk.class,conn);
}
/**
 * V_terzo_per_conguaglioHome constructor comment.
 * @param conn java.sql.Connection
 * @param persistentCache it.cnr.jada.persistency.PersistentCache
 */
public V_terzo_per_conguaglioHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(V_terzo_per_conguaglioBulk.class,conn,persistentCache);
}
}
