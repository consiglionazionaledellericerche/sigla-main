/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 28/02/2020
 */
package it.cnr.contab.util00.bulk;
import java.sql.Connection;

import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.BusyResourceException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
public class HelpHome extends BulkHome {
	public HelpHome(Connection conn) {
		super(HelpBulk.class, conn);
	}
	public HelpHome(Connection conn, PersistentCache persistentCache) {
		super(HelpBulk.class, conn, persistentCache);
	}

	public void initializePrimaryKeyForInsert(
			it.cnr.jada.UserContext userContext, OggettoBulk bulk)
			throws PersistencyException, it.cnr.jada.comp.ComponentException {
		try {
			((HelpBulk) bulk).setId((Integer)findMax(bulk, "id", new Long(0), true) + 1);
		} catch (BusyResourceException e) {
			throw new ComponentException(e);
		}

	}
}