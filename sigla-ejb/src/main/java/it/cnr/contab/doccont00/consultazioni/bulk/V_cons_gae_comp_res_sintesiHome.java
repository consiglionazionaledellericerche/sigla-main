/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 24/03/2009
 */
package it.cnr.contab.doccont00.consultazioni.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class V_cons_gae_comp_res_sintesiHome extends BulkHome {
	public V_cons_gae_comp_res_sintesiHome(Connection conn) {
		super(V_cons_gae_comp_res_sintesiBulk.class, conn);
	}
	public V_cons_gae_comp_res_sintesiHome(Connection conn, PersistentCache persistentCache) {
		super(V_cons_gae_comp_res_sintesiBulk.class, conn, persistentCache);
	}
}