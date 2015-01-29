/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 27/01/2015
 */
package it.cnr.contab.doccont00.consultazioni.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class VConsObbligazioniHome extends BulkHome {
	public VConsObbligazioniHome(Connection conn) {
		super(VConsObbligazioniBulk.class, conn);
	}
	public VConsObbligazioniHome(Connection conn, PersistentCache persistentCache) {
		super(VConsObbligazioniBulk.class, conn, persistentCache);
	}
}