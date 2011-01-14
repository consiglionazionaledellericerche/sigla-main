/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 11/01/2008
 */
package it.cnr.contab.incarichi00.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class V_incarichi_terzoHome extends BulkHome {
	public V_incarichi_terzoHome(Connection conn) {
		super(V_incarichi_terzoBulk.class, conn);
	}
	public V_incarichi_terzoHome(Connection conn, PersistentCache persistentCache) {
		super(V_incarichi_terzoBulk.class, conn, persistentCache);
	}
}