/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 12/05/2017
 */
package it.cnr.contab.ordmag.richieste.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class RichiestaUopHome extends BulkHome {
	public RichiestaUopHome(Connection conn) {
		super(RichiestaUopBulk.class, conn);
	}
	public RichiestaUopHome(Connection conn, PersistentCache persistentCache) {
		super(RichiestaUopBulk.class, conn, persistentCache);
	}
}