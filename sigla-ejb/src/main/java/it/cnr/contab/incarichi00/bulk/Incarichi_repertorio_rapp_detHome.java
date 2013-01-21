package it.cnr.contab.incarichi00.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;

import java.sql.Connection;
public class Incarichi_repertorio_rapp_detHome extends BulkHome {
	public Incarichi_repertorio_rapp_detHome(Connection conn) {
		super(Incarichi_repertorio_rapp_detBulk.class, conn);
	}
	public Incarichi_repertorio_rapp_detHome(Connection conn, PersistentCache persistentCache) {
		super(Incarichi_repertorio_rapp_detBulk.class, conn, persistentCache);
	}
	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext, OggettoBulk bulk) throws PersistencyException {
		try {
			((Incarichi_repertorio_rapp_detBulk)bulk).setProgressivo_riga(
					new Long(
					((Long)findAndLockMax( bulk, "progressivo_riga", new Long(0) )).longValue()+1
				)
			);
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}
	}
}