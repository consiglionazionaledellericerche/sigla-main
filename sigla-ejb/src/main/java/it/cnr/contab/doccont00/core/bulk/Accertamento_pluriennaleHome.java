/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 21/09/2021
 */
package it.cnr.contab.doccont00.core.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Accertamento_pluriennaleHome extends BulkHome {
	public Accertamento_pluriennaleHome(Connection conn) {
		super(Accertamento_pluriennaleBulk.class, conn);
	}
	public Accertamento_pluriennaleHome(Connection conn, PersistentCache persistentCache) {
		super(Accertamento_pluriennaleBulk.class, conn, persistentCache);
	}
}