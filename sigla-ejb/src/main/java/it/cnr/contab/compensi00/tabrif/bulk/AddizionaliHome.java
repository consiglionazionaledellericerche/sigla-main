/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 07/03/2007
 */
package it.cnr.contab.compensi00.tabrif.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class AddizionaliHome extends BulkHome {
	public AddizionaliHome(Connection conn) {
		super(AddizionaliBulk.class, conn);
	}
	public AddizionaliHome(Connection conn, PersistentCache persistentCache) {
		super(AddizionaliBulk.class, conn, persistentCache);
	}
}