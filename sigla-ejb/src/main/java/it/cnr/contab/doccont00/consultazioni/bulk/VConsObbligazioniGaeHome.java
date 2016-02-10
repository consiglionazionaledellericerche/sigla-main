/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 27/01/2015
 */
package it.cnr.contab.doccont00.consultazioni.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class VConsObbligazioniGaeHome extends BulkHome {
	public VConsObbligazioniGaeHome(Connection conn) {
		super(VConsObbligazioniGaeBulk.class, conn);
	}
	public VConsObbligazioniGaeHome(Connection conn, PersistentCache persistentCache) {
		super(VConsObbligazioniGaeBulk.class, conn, persistentCache);
	}
}