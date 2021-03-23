/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 22/03/2021
 */
package it.cnr.contab.incarichi00.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Phdtipo_dottoratiHome extends BulkHome {
	public Phdtipo_dottoratiHome(Connection conn) {
		super(Phdtipo_dottoratiBulk.class, conn);
	}
	public Phdtipo_dottoratiHome(Connection conn, PersistentCache persistentCache) {
		super(Phdtipo_dottoratiBulk.class, conn, persistentCache);
	}
}