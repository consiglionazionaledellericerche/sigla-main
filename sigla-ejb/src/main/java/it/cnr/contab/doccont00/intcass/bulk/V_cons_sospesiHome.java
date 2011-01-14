/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 30/11/2009
 */
package it.cnr.contab.doccont00.intcass.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class V_cons_sospesiHome extends BulkHome {
	public V_cons_sospesiHome(Connection conn) {
		super(V_cons_sospesiBulk.class, conn);
	}
	public V_cons_sospesiHome(Connection conn, PersistentCache persistentCache) {
		super(V_cons_sospesiBulk.class, conn, persistentCache);
	}
}