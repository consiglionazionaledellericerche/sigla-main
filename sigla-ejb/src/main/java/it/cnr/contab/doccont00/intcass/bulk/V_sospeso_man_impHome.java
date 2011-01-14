/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 14/07/2009
 */
package it.cnr.contab.doccont00.intcass.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class V_sospeso_man_impHome extends BulkHome {
	public V_sospeso_man_impHome(Connection conn) {
		super(V_sospeso_man_impBulk.class, conn);
	}
	public V_sospeso_man_impHome(Connection conn, PersistentCache persistentCache) {
		super(V_sospeso_man_impBulk.class, conn, persistentCache);
	}
}