/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 22/04/2010
 */
package it.cnr.contab.docamm00.docs.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class VIntrastatHome extends BulkHome {
	public VIntrastatHome(Connection conn) {
		super(VIntrastatBulk.class, conn);
	}
	public VIntrastatHome(Connection conn, PersistentCache persistentCache) {
		super(VIntrastatBulk.class, conn, persistentCache);
	}
}