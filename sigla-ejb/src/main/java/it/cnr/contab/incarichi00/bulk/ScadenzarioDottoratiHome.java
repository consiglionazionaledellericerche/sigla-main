/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 03/06/2021
 */
package it.cnr.contab.incarichi00.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class ScadenzarioDottoratiHome extends BulkHome {
	public ScadenzarioDottoratiHome(Connection conn) {
		super(ScadenzarioDottoratiBulk.class, conn);
	}
	public ScadenzarioDottoratiHome(Connection conn, PersistentCache persistentCache) {
		super(ScadenzarioDottoratiBulk.class, conn, persistentCache);
	}
}