/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 07/05/2007
 */
package it.cnr.contab.doccont00.core.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Mandato_siopeHome extends BulkHome {
	public Mandato_siopeHome(Class clazz, java.sql.Connection conn) {
		super(clazz,conn);
	}
	public Mandato_siopeHome(Class clazz,java.sql.Connection conn,PersistentCache persistentCache) {
		super(clazz,conn,persistentCache);
	}
	public Mandato_siopeHome(Connection conn) {
		super(Mandato_siopeBulk.class, conn);
	}
	public Mandato_siopeHome(Connection conn, PersistentCache persistentCache) {
		super(Mandato_siopeBulk.class, conn, persistentCache);
	}
}