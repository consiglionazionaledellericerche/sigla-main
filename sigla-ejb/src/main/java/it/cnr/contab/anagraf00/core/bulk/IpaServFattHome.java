/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 06/06/2014
 */
package it.cnr.contab.anagraf00.core.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class IpaServFattHome extends BulkHome {
	public IpaServFattHome(Connection conn) {
		super(IpaServFattBulk.class, conn);
	}
	public IpaServFattHome(Connection conn, PersistentCache persistentCache) {
		super(IpaServFattBulk.class, conn, persistentCache);
	}
}