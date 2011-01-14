/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 09/06/2008
 */
package it.cnr.contab.docamm00.docs.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class VFatturaPassivaSIPHome extends BulkHome {
	public VFatturaPassivaSIPHome(Connection conn) {
		super(VFatturaPassivaSIPBulk.class, conn);
	}
	public VFatturaPassivaSIPHome(Connection conn, PersistentCache persistentCache) {
		super(VFatturaPassivaSIPBulk.class, conn, persistentCache);
	}
}