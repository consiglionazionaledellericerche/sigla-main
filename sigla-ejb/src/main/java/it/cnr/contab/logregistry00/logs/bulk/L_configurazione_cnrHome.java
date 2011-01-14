/*
* Created by Generator 1.0
* Date 22/09/2005
*/
package it.cnr.contab.logregistry00.logs.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class L_configurazione_cnrHome extends BulkHome {
	public L_configurazione_cnrHome(java.sql.Connection conn) {
		super(L_configurazione_cnrBulk.class, conn);
	}
	public L_configurazione_cnrHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(L_configurazione_cnrBulk.class, conn, persistentCache);
	}
}