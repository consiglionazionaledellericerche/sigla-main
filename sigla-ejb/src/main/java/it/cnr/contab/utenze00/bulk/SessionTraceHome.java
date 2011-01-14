/*
 * Created by BulkGenerator 1.5 [30/07/2008]
 * Date 31/07/2008
 */
package it.cnr.contab.utenze00.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class SessionTraceHome extends BulkHome {
	public SessionTraceHome(Connection conn) {
		super(SessionTraceBulk.class, conn);
	}
	public SessionTraceHome(Connection conn, PersistentCache persistentCache) {
		super(SessionTraceBulk.class, conn, persistentCache);
	}
}