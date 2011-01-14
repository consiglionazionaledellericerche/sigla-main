/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 17/07/2007
 */
package it.cnr.contab.compensi00.tabrif.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Gruppo_cr_uoHome extends BulkHome {
	public Gruppo_cr_uoHome(Connection conn) {
		super(Gruppo_cr_uoBulk.class, conn);
	}
	public Gruppo_cr_uoHome(Connection conn, PersistentCache persistentCache) {
		super(Gruppo_cr_uoBulk.class, conn, persistentCache);
	}
}