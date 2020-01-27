/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 28/06/2017
 */
package it.cnr.contab.ordmag.ordini.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class OrdineAcqRigaHome extends BulkHome {
	public OrdineAcqRigaHome(Connection conn) {
		super(OrdineAcqRigaBulk.class, conn);
	}
	public OrdineAcqRigaHome(Connection conn, PersistentCache persistentCache) {
		super(OrdineAcqRigaBulk.class, conn, persistentCache);
	}
}