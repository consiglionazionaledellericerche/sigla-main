/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 12/10/2007
 */
package it.cnr.contab.doccont00.consultazioni.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class V_cons_gae_competenza_entHome extends BulkHome {
	public V_cons_gae_competenza_entHome(Connection conn) {
		super(V_cons_gae_competenza_entBulk.class, conn);
	}
	public V_cons_gae_competenza_entHome(Connection conn, PersistentCache persistentCache) {
		super(V_cons_gae_competenza_entBulk.class, conn, persistentCache);
	}
}