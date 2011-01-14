package it.cnr.contab.anagraf00.core.bulk;

/**
 * Insert the type's description here.
 * Creation date: (31/05/2002 13:04:02)
 * @author: CNRADM
 */
public class V_terzo_persona_fisicaHome extends TerzoHome {
/**
 * V_terzo_persona_fisicaHome constructor comment.
 * @param conn java.sql.Connection
 */
public V_terzo_persona_fisicaHome(java.sql.Connection conn) {
	super(V_terzo_persona_fisicaBulk.class,conn);
}
/**
 * V_terzo_persona_fisicaHome constructor comment.
 * @param conn java.sql.Connection
 * @param persistentCache it.cnr.jada.persistency.PersistentCache
 */
public V_terzo_persona_fisicaHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(V_terzo_persona_fisicaBulk.class,conn, persistentCache);
}
}
