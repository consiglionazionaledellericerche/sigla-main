/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 20/09/2021
 */
package it.cnr.contab.doccont00.core.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Obbligazione_pluriennaleHome extends BulkHome {
	public Obbligazione_pluriennaleHome(Connection conn) {
		super(Obbligazione_pluriennaleBulk.class, conn);
	}
	public Obbligazione_pluriennaleHome(Connection conn, PersistentCache persistentCache) {
		super(Obbligazione_pluriennaleBulk.class, conn, persistentCache);
	}

}