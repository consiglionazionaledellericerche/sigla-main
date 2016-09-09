/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 09/09/2016
 */
package it.cnr.contab.docamm00.consultazioni.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class VConsRiepCompensiHome extends BulkHome {
	public VConsRiepCompensiHome(Connection conn) {
		super(VConsRiepCompensiBulk.class, conn);
	}
	public VConsRiepCompensiHome(Connection conn, PersistentCache persistentCache) {
		super(VConsRiepCompensiBulk.class, conn, persistentCache);
	}
}