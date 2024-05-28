/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 24/05/2024
 */
package it.cnr.contab.doccont00.consultazioni.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class VControlliPCCHome extends BulkHome {
	public VControlliPCCHome(Connection conn) {
		super(VControlliPCCBulk.class, conn);
	}
	public VControlliPCCHome(Connection conn, PersistentCache persistentCache) {
		super(VControlliPCCBulk.class, conn, persistentCache);
	}
}