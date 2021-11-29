/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 28/07/2021
 */
package it.cnr.contab.reports.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Print_priorityHome extends BulkHome {
	public Print_priorityHome(Connection conn) {
		super(Print_priorityBulk.class, conn);
	}
	public Print_priorityHome(Connection conn, PersistentCache persistentCache) {
		super(Print_priorityBulk.class, conn, persistentCache);
	}
}