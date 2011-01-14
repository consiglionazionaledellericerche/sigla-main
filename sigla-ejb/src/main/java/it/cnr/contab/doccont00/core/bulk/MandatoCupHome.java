/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 09/09/2010
 */
package it.cnr.contab.doccont00.core.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class MandatoCupHome extends BulkHome {
	public MandatoCupHome(Class clazz, java.sql.Connection conn) {
		super(clazz,conn);
	}
	public MandatoCupHome(Class clazz,java.sql.Connection conn,PersistentCache persistentCache) {
		super(clazz,conn,persistentCache);
	}
	public MandatoCupHome(Connection conn) {
		super(MandatoCupBulk.class, conn);
	}
	public MandatoCupHome(Connection conn, PersistentCache persistentCache) {
		super(MandatoCupBulk.class, conn, persistentCache);
	}
}