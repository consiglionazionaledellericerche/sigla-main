/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 03/10/2017
 */
package it.cnr.contab.ordmag.magazzino.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class MovimentiMagHome extends BulkHome {
	public MovimentiMagHome(Connection conn) {
		super(MovimentiMagBulk.class, conn);
	}
	public MovimentiMagHome(Connection conn, PersistentCache persistentCache) {
		super(MovimentiMagBulk.class, conn, persistentCache);
	}
}