/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 03/06/2021
 */
package it.cnr.contab.incarichi00.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Scadenzario_dottoratirataHome extends BulkHome {
	public Scadenzario_dottoratirataHome(Connection conn) {
		super(Scadenzario_dottoratirataBulk.class, conn);
	}
	public Scadenzario_dottoratirataHome(Connection conn, PersistentCache persistentCache) {
		super(Scadenzario_dottoratirataBulk.class, conn, persistentCache);
	}
}