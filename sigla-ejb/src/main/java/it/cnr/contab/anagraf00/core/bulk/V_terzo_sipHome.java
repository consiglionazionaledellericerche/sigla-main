/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 06/06/2008
 */
package it.cnr.contab.anagraf00.core.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class V_terzo_sipHome extends BulkHome {
	public V_terzo_sipHome(Connection conn) {
		super(V_terzo_sipBulk.class, conn);
	}
	public V_terzo_sipHome(Connection conn, PersistentCache persistentCache) {
		super(V_terzo_sipBulk.class, conn, persistentCache);
	}
}