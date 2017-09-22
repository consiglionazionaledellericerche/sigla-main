/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 21/09/2017
 */
package it.cnr.contab.ordmag.ordini.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class EvasioneOrdineRigaHome extends BulkHome {
	public EvasioneOrdineRigaHome(Connection conn) {
		super(EvasioneOrdineRigaBulk.class, conn);
	}
	public EvasioneOrdineRigaHome(Connection conn, PersistentCache persistentCache) {
		super(EvasioneOrdineRigaBulk.class, conn, persistentCache);
	}
}