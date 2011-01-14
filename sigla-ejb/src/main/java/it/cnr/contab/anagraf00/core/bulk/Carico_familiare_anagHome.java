package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Carico_familiare_anagHome extends BulkHome {
	public Carico_familiare_anagHome(java.sql.Connection conn) {
		super(Carico_familiare_anagBulk.class,conn);
	}
	public Carico_familiare_anagHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(Carico_familiare_anagBulk.class,conn,persistentCache);
	}
	/**
	 * Imposta il pg_carico_anag di un oggetto <code>Carico_familiare_anagBulk</code>.
	 *
	 * @param fami <code>Carico_familiare_anagBulk</code>
	 *
	 * @exception PersistencyException
	 */

	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk fami) throws PersistencyException {
		try {
			((Carico_familiare_anagBulk)fami).setPg_carico_anag(
				new Long(
					((Long)findAndLockMax( fami, "pg_carico_anag", new Long(0) )).longValue()+1
				)
			);
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}
	}
}
