/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 06/06/2014
 */
package it.cnr.contab.anagraf00.core.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class IpaAmministrazioniHome extends BulkHome {
	public IpaAmministrazioniHome(Connection conn) {
		super(IpaAmministrazioniBulk.class, conn);
	}
	public IpaAmministrazioniHome(Connection conn, PersistentCache persistentCache) {
		super(IpaAmministrazioniBulk.class, conn, persistentCache);
	}
}