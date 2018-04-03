package it.cnr.contab.missioni00.docs.bulk;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;

public class Missione_rigaHome extends BulkHome {
	private static final long serialVersionUID = 1L;

	public Missione_rigaHome(java.sql.Connection conn) {
		super(Missione_rigaBulk.class, conn);
	}

	public Missione_rigaHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Missione_rigaBulk.class, conn, persistentCache);
	}
	
	@Override
	public void initializePrimaryKeyForInsert(UserContext usercontext, OggettoBulk oggettobulk)
			throws PersistencyException, ComponentException {
		try {
			((Missione_rigaBulk)oggettobulk).setProgressivo_riga(
				new Long(
					((Long)findAndLockMax( oggettobulk, "progressivo_riga", new Long(0) )).longValue()+1
				)
			);
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}
	}
}
