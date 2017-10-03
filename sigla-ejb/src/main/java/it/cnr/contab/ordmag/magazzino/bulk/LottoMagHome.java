/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 03/10/2017
 */
package it.cnr.contab.ordmag.magazzino;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class LottoMagHome extends BulkHome {
	public LottoMagHome(Connection conn) {
		super(LottoMagBulk.class, conn);
	}
	public LottoMagHome(Connection conn, PersistentCache persistentCache) {
		super(LottoMagBulk.class, conn, persistentCache);
	}
}