/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 18/03/2008
 */
package it.cnr.contab.utenze00.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class PrivilegioHome extends BulkHome {
	public PrivilegioHome(Connection conn) {
		super(PrivilegioBulk.class, conn);
	}
	public PrivilegioHome(Connection conn, PersistentCache persistentCache) {
		super(PrivilegioBulk.class, conn, persistentCache);
	}
}