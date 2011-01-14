package it.cnr.contab.config00.pdcfin.bulk;

public class V_voce_f_partita_giroHome extends it.cnr.jada.bulk.BulkHome {
/**
 * V_voce_f_partita_giroHome constructor comment.
 * @param clazz java.lang.Class
 * @param connection java.sql.Connection
 */
public V_voce_f_partita_giroHome(java.sql.Connection connection) {
	super(V_voce_f_partita_giroBulk.class, connection);
}
/**
 * V_voce_f_partita_giroHome constructor comment.
 * @param clazz java.lang.Class
 * @param connection java.sql.Connection
 * @param persistentCache it.cnr.jada.persistency.PersistentCache
 */
public V_voce_f_partita_giroHome( java.sql.Connection connection, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(V_voce_f_partita_giroBulk.class, connection, persistentCache);
}
}
