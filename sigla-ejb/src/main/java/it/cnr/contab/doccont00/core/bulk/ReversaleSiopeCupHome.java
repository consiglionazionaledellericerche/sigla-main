/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 20/06/2013
 */
package it.cnr.contab.doccont00.core.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class ReversaleSiopeCupHome extends BulkHome {
	public ReversaleSiopeCupHome(Connection conn) {
		super(ReversaleSiopeCupBulk.class, conn);
	}
	public ReversaleSiopeCupHome(Connection conn, PersistentCache persistentCache) {
		super(ReversaleSiopeCupBulk.class, conn, persistentCache);
	}
	public ReversaleSiopeCupHome(Class clazz, java.sql.Connection conn) {
		super(clazz,conn);
	}
	public ReversaleSiopeCupHome(Class clazz,java.sql.Connection conn,PersistentCache persistentCache) {
		super(clazz,conn,persistentCache);
	}
}