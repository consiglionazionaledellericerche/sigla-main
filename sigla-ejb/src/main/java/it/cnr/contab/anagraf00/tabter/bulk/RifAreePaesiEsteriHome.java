/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 12/09/2011
 */
package it.cnr.contab.anagraf00.tabter.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class RifAreePaesiEsteriHome extends BulkHome {
	public RifAreePaesiEsteriHome(Connection conn) {
		super(RifAreePaesiEsteriBulk.class, conn);
	}
	public RifAreePaesiEsteriHome(Connection conn, PersistentCache persistentCache) {
		super(RifAreePaesiEsteriBulk.class, conn, persistentCache);
	}
}