/*
 * Created on Feb 27, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.prevent00.bulk;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;

import java.sql.Connection;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class V_assestato_residuoHome extends BulkHome {

	/**
	 * @param class1
	 * @param connection
	 */
	public V_assestato_residuoHome(Class class1, Connection connection) {
		super(class1, connection);
	}
	/**
	 * 
	 * @param conn
	 * @param persistentCache
	 */
	public V_assestato_residuoHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_assestato_residuoBulk.class, conn, persistentCache);
	}

	/**
	 * @param class1
	 * @param connection
	 * @param persistentcache
	 */
	public V_assestato_residuoHome(Class class1,Connection connection,PersistentCache persistentcache) {
		super(class1, connection, persistentcache);
	}

}
