/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/05/2010
 */
package it.cnr.contab.docamm00.docs.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class VIntra12Home extends BulkHome {
	public VIntra12Home(Connection conn) {
		super(VIntra12Bulk.class, conn);
	}
	public VIntra12Home(Connection conn, PersistentCache persistentCache) {
		super(VIntra12Bulk.class, conn, persistentCache);
	}
}