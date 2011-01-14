/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 17/03/2009
 */
package it.cnr.contab.pdg00.cdip.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Cnr_estrazione_coriHome extends BulkHome {
	public Cnr_estrazione_coriHome(Connection conn) {
		super(Cnr_estrazione_coriBulk.class, conn);
	}
	public Cnr_estrazione_coriHome(Connection conn, PersistentCache persistentCache) {
		super(Cnr_estrazione_coriBulk.class, conn, persistentCache);
	}
}