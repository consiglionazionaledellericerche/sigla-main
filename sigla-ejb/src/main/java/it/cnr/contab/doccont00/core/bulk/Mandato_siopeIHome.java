/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 07/05/2007
 */
package it.cnr.contab.doccont00.core.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Mandato_siopeIHome extends Mandato_siopeHome {
	public Mandato_siopeIHome(Connection conn) {
		super(Mandato_siopeIBulk.class, conn);
	}
	public Mandato_siopeIHome(Connection conn, PersistentCache persistentCache) {
		super(Mandato_siopeIBulk.class, conn, persistentCache);
	}
}