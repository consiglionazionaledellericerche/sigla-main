/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 12/06/2008
 */
package it.cnr.contab.compensi00.docs.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class VCompensoSIPHome extends BulkHome {
	public VCompensoSIPHome(Connection conn) {
		super(VCompensoSIPBulk.class, conn);
	}
	public VCompensoSIPHome(Connection conn, PersistentCache persistentCache) {
		super(VCompensoSIPBulk.class, conn, persistentCache);
	}
}