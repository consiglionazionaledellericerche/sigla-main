/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 09/09/2010
 */
package it.cnr.contab.doccont00.core.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class ReversaleCupHome extends BulkHome {
	public ReversaleCupHome(Class clazz, java.sql.Connection conn) {
		super(clazz,conn);
	}
	public ReversaleCupHome(Class clazz,java.sql.Connection conn,PersistentCache persistentCache) {
		super(clazz,conn,persistentCache);
	}
	public ReversaleCupHome(Connection conn) {
		super(ReversaleCupBulk.class, conn);
	}
	public ReversaleCupHome(Connection conn, PersistentCache persistentCache) {
		super(ReversaleCupBulk.class, conn, persistentCache);
	}
}