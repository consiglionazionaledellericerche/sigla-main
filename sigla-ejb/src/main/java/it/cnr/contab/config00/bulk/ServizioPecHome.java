/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 14/05/2010
 */
package it.cnr.contab.config00.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class ServizioPecHome extends BulkHome {
	public ServizioPecHome(Connection conn) {
		super(ServizioPecBulk.class, conn);
	}
	public ServizioPecHome(Connection conn, PersistentCache persistentCache) {
		super(ServizioPecBulk.class, conn, persistentCache);
	}
}