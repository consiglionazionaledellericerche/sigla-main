/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 03/04/2008
 */
package it.cnr.contab.incarichi00.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;

import java.sql.Connection;
public class Incarichi_procedura_noteHome extends BulkHome {
	public Incarichi_procedura_noteHome(Connection conn) {
		super(Incarichi_procedura_noteBulk.class, conn);
	}
	public Incarichi_procedura_noteHome(Connection conn, PersistentCache persistentCache) {
		super(Incarichi_procedura_noteBulk.class, conn, persistentCache);
	}
	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext, OggettoBulk bulk) throws PersistencyException {
		try {
			((Incarichi_procedura_noteBulk)bulk).setPg_nota(
					new Long(
					((Long)findAndLockMax( bulk, "pg_nota", new Long(0) )).longValue()+1
				)
			);
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}
	}
}