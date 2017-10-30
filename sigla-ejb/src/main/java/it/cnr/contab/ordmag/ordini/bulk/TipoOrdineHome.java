/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 28/06/2017
 */
package it.cnr.contab.ordmag.ordini.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class TipoOrdineHome extends BulkHome {
	public TipoOrdineHome(Connection conn) {
		super(TipoOrdineBulk.class, conn);
	}
	public TipoOrdineHome(Connection conn, PersistentCache persistentCache) {
		super(TipoOrdineBulk.class, conn, persistentCache);
	}
}