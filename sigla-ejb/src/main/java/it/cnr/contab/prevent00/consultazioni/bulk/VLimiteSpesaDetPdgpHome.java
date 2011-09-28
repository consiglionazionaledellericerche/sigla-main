/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 20/09/2011
 */
package it.cnr.contab.prevent00.consultazioni.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class VLimiteSpesaDetPdgpHome extends BulkHome {
	public VLimiteSpesaDetPdgpHome(Connection conn) {
		super(VLimiteSpesaDetPdgpBulk.class, conn);
	}
	public VLimiteSpesaDetPdgpHome(Connection conn, PersistentCache persistentCache) {
		super(VLimiteSpesaDetPdgpBulk.class, conn, persistentCache);
	}
}