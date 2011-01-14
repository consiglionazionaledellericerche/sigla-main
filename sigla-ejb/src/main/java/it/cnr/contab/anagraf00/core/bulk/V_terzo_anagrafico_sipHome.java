package it.cnr.contab.anagraf00.core.bulk;

import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;

public class V_terzo_anagrafico_sipHome extends BulkHome {
	/**
	 * V_terzo_anagrafico_sipHome constructor comment.
	 * @param conn java.sql.Connection
	 */
	public V_terzo_anagrafico_sipHome(java.sql.Connection conn) {
		super(V_terzo_anagrafico_sipBulk.class,conn);
	}
	/**
	 * V_terzo_anagrafico_sipHome constructor comment.
	 * @param conn java.sql.Connection
	 * @param persistentCache it.cnr.jada.persistency.PersistentCache
	 */
	public V_terzo_anagrafico_sipHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
		super(V_terzo_anagrafico_sipBulk.class,conn, persistentCache);
	}
}
