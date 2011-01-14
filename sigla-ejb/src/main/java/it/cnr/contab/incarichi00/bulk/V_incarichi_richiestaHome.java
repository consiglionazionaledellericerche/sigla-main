/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 13/09/2007
 */
package it.cnr.contab.incarichi00.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class V_incarichi_richiestaHome extends BulkHome {
	public V_incarichi_richiestaHome(Connection conn) {
		super(V_incarichi_richiestaBulk.class, conn);
	}
	public V_incarichi_richiestaHome(Connection conn, PersistentCache persistentCache) {
		super(V_incarichi_richiestaBulk.class, conn, persistentCache);
	}
}