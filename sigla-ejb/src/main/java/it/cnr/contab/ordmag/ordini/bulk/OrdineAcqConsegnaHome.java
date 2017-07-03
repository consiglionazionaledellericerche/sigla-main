/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 28/06/2017
 */
package it.cnr.contab.ordmag.ordini.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class OrdineAcqConsegnaHome extends BulkHome {
	public OrdineAcqConsegnaHome(Connection conn) {
		super(OrdineAcqConsegnaBulk.class, conn);
	}
	public OrdineAcqConsegnaHome(Connection conn, PersistentCache persistentCache) {
		super(OrdineAcqConsegnaBulk.class, conn, persistentCache);
	}
}