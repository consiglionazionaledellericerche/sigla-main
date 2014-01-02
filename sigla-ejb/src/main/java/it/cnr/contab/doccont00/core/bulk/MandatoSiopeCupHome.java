/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 20/06/2013
 */
package it.cnr.contab.doccont00.core.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class MandatoSiopeCupHome extends BulkHome {
	public MandatoSiopeCupHome(Connection conn) {
		super(MandatoSiopeCupBulk.class, conn);
	}
	public MandatoSiopeCupHome(Connection conn, PersistentCache persistentCache) {
		super(MandatoSiopeCupBulk.class, conn, persistentCache);
	}
	public MandatoSiopeCupHome(Class clazz, java.sql.Connection conn) {
		super(clazz,conn);
	}
	public MandatoSiopeCupHome(Class clazz,java.sql.Connection conn,PersistentCache persistentCache) {
		super(clazz,conn,persistentCache);
	}
}