/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 03/10/2017
 */
package it.cnr.contab.ordmag.magazzino.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class BollaScaricoMagHome extends BulkHome {
	public BollaScaricoMagHome(Connection conn) {
		super(BollaScaricoMagBulk.class, conn);
	}
	public BollaScaricoMagHome(Connection conn, PersistentCache persistentCache) {
		super(BollaScaricoMagBulk.class, conn, persistentCache);
	}
}