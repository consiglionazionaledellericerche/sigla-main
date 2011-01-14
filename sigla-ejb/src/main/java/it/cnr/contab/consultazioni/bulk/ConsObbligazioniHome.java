/*
* Creted by Generator 1.0
* Date 28/01/2005
*/
package it.cnr.contab.consultazioni.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class ConsObbligazioniHome extends BulkHome {
	public ConsObbligazioniHome(java.sql.Connection conn) {
		super(ConsObbligazioniBulk.class, conn);
	}
	public ConsObbligazioniHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(ConsObbligazioniBulk.class, conn, persistentCache);
	}
	public ConsObbligazioniHome(Class clazz,java.sql.Connection conn) {
		super(clazz,conn);
	}
	public ConsObbligazioniHome(Class clazz,java.sql.Connection conn,PersistentCache persistentCache) {
		super( clazz,conn,persistentCache);
	}
}