/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 22/03/2021
 */
package it.cnr.contab.incarichi00.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Ciclo_dottoratiHome extends BulkHome {
	public Ciclo_dottoratiHome(Connection conn) {
		super(Ciclo_dottoratiBulk.class, conn);
	}
	public Ciclo_dottoratiHome(Connection conn, PersistentCache persistentCache) {
		super(Ciclo_dottoratiBulk.class, conn, persistentCache);
	}
}