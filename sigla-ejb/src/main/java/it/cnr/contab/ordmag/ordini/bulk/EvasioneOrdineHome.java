/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 21/09/2017
 */
package it.cnr.contab.ordmag.ordini.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class EvasioneOrdineHome extends BulkHome {
	public EvasioneOrdineHome(Connection conn) {
		super(EvasioneOrdineBulk.class, conn);
	}
	public EvasioneOrdineHome(Connection conn, PersistentCache persistentCache) {
		super(EvasioneOrdineBulk.class, conn, persistentCache);
	}
}