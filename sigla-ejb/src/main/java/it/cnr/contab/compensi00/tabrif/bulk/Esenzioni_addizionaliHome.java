/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 13/06/2007
 */
package it.cnr.contab.compensi00.tabrif.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Esenzioni_addizionaliHome extends BulkHome {
	public Esenzioni_addizionaliHome(Connection conn) {
		super(Esenzioni_addizionaliBulk.class, conn);
	}
	public Esenzioni_addizionaliHome(Connection conn, PersistentCache persistentCache) {
		super(Esenzioni_addizionaliBulk.class, conn, persistentCache);
	}
}