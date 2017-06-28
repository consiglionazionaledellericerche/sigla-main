/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 28/06/2017
 */
package it.cnr.contab.ordmag.ordini;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class OrdineAcqHome extends BulkHome {
	public OrdineAcqHome(Connection conn) {
		super(OrdineAcqBulk.class, conn);
	}
	public OrdineAcqHome(Connection conn, PersistentCache persistentCache) {
		super(OrdineAcqBulk.class, conn, persistentCache);
	}
}