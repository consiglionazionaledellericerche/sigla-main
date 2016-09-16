/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 12/09/2016
 */
package it.cnr.contab.docamm00.consultazioni.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class VDocAmmBrevettiHome extends BulkHome {
	public VDocAmmBrevettiHome(Connection conn) {
		super(VDocAmmBrevettiBulk.class, conn);
	}
	public VDocAmmBrevettiHome(Connection conn, PersistentCache persistentCache) {
		super(VDocAmmBrevettiBulk.class, conn, persistentCache);
	}
}