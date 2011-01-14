/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 23/04/2007
 */
package it.cnr.contab.anagraf00.tabrif.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
public class Tipologie_istatHome extends BulkHome {
	public Tipologie_istatHome(Connection conn) {
		super(Tipologie_istatBulk.class, conn);
	}
	public Tipologie_istatHome(Connection conn, PersistentCache persistentCache) {
		super(Tipologie_istatBulk.class, conn, persistentCache);
	}
	
	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk cla) throws PersistencyException {
		try {
			((Tipologie_istatBulk)cla).setPg_tipologia(
				new Integer(
					((Integer)findAndLockMax( cla, "pg_tipologia", new Integer(0) )).intValue()+1
				)
			);
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}
	}
}