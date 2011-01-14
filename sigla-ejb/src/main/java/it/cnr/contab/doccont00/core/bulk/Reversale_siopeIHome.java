/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 07/05/2007
 */
package it.cnr.contab.doccont00.core.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Reversale_siopeIHome extends Reversale_siopeHome {
	public Reversale_siopeIHome(Connection conn) {
		super(Reversale_siopeIBulk.class, conn);
	}
	public Reversale_siopeIHome(Connection conn, PersistentCache persistentCache) {
		super(Reversale_siopeIBulk.class, conn, persistentCache);
	}
}