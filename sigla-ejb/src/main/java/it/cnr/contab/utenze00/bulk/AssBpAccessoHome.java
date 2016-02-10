/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 10/07/2015
 */
package it.cnr.contab.utenze00.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class AssBpAccessoHome extends BulkHome {
	public AssBpAccessoHome(Connection conn) {
		super(AssBpAccessoBulk.class, conn);
	}
	public AssBpAccessoHome(Connection conn, PersistentCache persistentCache) {
		super(AssBpAccessoBulk.class, conn, persistentCache);
	}
}