/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 20/09/2011
 */
package it.cnr.contab.prevent00.consultazioni.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class VLimiteSpesaPdgpHome extends BulkHome {
	public VLimiteSpesaPdgpHome(Connection conn) {
		super(VLimiteSpesaPdgpBulk.class, conn);
	}
	public VLimiteSpesaPdgpHome(Connection conn, PersistentCache persistentCache) {
		super(VLimiteSpesaPdgpBulk.class, conn, persistentCache);
	}
}