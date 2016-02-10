/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 02/07/2012
 */
package it.cnr.contab.bilaterali00.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class BltIstitutiHome extends BulkHome {
	public BltIstitutiHome(Connection conn) {
		super(BltIstitutiBulk.class, conn);
	}
	public BltIstitutiHome(Connection conn, PersistentCache persistentCache) {
		super(BltIstitutiBulk.class, conn, persistentCache);
	}
}