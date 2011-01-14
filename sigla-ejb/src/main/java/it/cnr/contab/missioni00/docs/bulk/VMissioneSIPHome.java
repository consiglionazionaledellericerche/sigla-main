/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 12/06/2008
 */
package it.cnr.contab.missioni00.docs.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class VMissioneSIPHome extends BulkHome {
	public VMissioneSIPHome(Connection conn) {
		super(VMissioneSIPBulk.class, conn);
	}
	public VMissioneSIPHome(Connection conn, PersistentCache persistentCache) {
		super(VMissioneSIPBulk.class, conn, persistentCache);
	}
}