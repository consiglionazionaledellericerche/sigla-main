/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 03/06/2021
 */
package it.cnr.contab.incarichi00.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class ScadenzarioDottoratiRataHome extends BulkHome {
	public ScadenzarioDottoratiRataHome(Connection conn) {
		super(ScadenzarioDottoratiRataBulk.class, conn);
	}
	public ScadenzarioDottoratiRataHome(Connection conn, PersistentCache persistentCache) {
		super(ScadenzarioDottoratiRataBulk.class, conn, persistentCache);
	}
}