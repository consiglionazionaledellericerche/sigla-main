/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 09/09/2010
 */
package it.cnr.contab.doccont00.tabrif.bulk;
import java.sql.Connection;

import it.cnr.contab.anagraf00.tabter.bulk.ComuneEsteroHome;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneItalianoHome;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class CupHome extends BulkHome {
	public CupHome(Connection conn) {
		super(CupBulk.class, conn);
	}
	public CupHome(Connection conn, PersistentCache persistentCache) {
		super(CupBulk.class, conn, persistentCache);
	}
}