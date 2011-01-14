/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 26/03/2008
 */
package it.cnr.contab.incarichi00.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class V_incarichi_da_assegnareHome extends BulkHome {
	public V_incarichi_da_assegnareHome(Connection conn) {
		super(V_incarichi_da_assegnareBulk.class, conn);
	}
	public V_incarichi_da_assegnareHome(Connection conn, PersistentCache persistentCache) {
		super(V_incarichi_da_assegnareBulk.class, conn, persistentCache);
	}
}