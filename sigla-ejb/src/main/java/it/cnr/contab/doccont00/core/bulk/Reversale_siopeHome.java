/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 14/05/2007
 */
package it.cnr.contab.doccont00.core.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Reversale_siopeHome extends BulkHome {
	public Reversale_siopeHome(Class clazz, java.sql.Connection conn) {
		super(clazz,conn);
	}
	public Reversale_siopeHome(Class clazz,java.sql.Connection conn,PersistentCache persistentCache) {
		super(clazz,conn,persistentCache);
	}
	public Reversale_siopeHome(Connection conn) {
		super(Reversale_siopeBulk.class, conn);
	}
	public Reversale_siopeHome(Connection conn, PersistentCache persistentCache) {
		super(Reversale_siopeBulk.class, conn, persistentCache);
	}
}