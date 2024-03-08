/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 08/03/2024
 */
package it.cnr.contab.doccont00.consultazioni.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class VConsIndicatorePagamentiHome extends BulkHome {
	public VConsIndicatorePagamentiHome(Connection conn) {
		super(VConsIndicatorePagamentiBulk.class, conn);
	}
	public VConsIndicatorePagamentiHome(Connection conn, PersistentCache persistentCache) {
		super(VConsIndicatorePagamentiBulk.class, conn, persistentCache);
	}
}