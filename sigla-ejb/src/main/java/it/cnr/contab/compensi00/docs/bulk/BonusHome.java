/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 19/02/2009
 */
package it.cnr.contab.compensi00.docs.bulk;
import java.sql.Connection;
import java.util.Collection;
import java.util.Dictionary;

import it.cnr.contab.config00.pdcfin.bulk.NaturaHome;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
public class BonusHome extends BulkHome {
	public BonusHome(Connection conn) {
		super(BonusBulk.class, conn);
	}
	public BonusHome(Connection conn, PersistentCache persistentCache) {
		super(BonusBulk.class, conn, persistentCache);
	}
	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk bulk) throws PersistencyException {
		try {
				((BonusBulk)bulk).setPg_bonus(
					new Long(
						((Long)findAndLockMax( bulk, "pg_bonus", new Long(0) )).longValue()+1
					)
				);
			} catch(it.cnr.jada.bulk.BusyResourceException e) {
				throw new PersistencyException(e);
			}
   	}
	
}