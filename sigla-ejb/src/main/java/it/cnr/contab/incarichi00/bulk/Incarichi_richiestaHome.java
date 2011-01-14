/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 23/07/2007
 */
package it.cnr.contab.incarichi00.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
public class Incarichi_richiestaHome extends BulkHome {
	public Incarichi_richiestaHome(Connection conn) {
		super(Incarichi_richiestaBulk.class, conn);
	}
	public Incarichi_richiestaHome(Connection conn, PersistentCache persistentCache) {
		super(Incarichi_richiestaBulk.class, conn, persistentCache);
	}
	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext, OggettoBulk bulk) throws PersistencyException {
		try {
			((Incarichi_richiestaBulk)bulk).setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
			((Incarichi_richiestaBulk)bulk).setPg_richiesta(
					new Long(
					((Long)findAndLockMax( bulk, "pg_richiesta", new Long(0) )).longValue()+1
				)
			);
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}
	}
}