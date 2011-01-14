package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class TelefonoHome extends BulkHome {
public TelefonoHome(java.sql.Connection conn) {
	super(TelefonoBulk.class,conn);
}
public TelefonoHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(TelefonoBulk.class,conn,persistentCache);
}
	/**
	 * Imposta il pg_banca di un oggetto <code>TelefonoBulk</code>.
	 *
	 * @param bank <code>BancaBulk</code>
	 *
	 * @exception PersistencyException
	 */

	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk tel) throws PersistencyException {
		try {
			((TelefonoBulk)tel).setPg_riferimento(
				new Long(
					((Long)findAndLockMax( tel, "pg_riferimento", new Long(0) )).longValue()+1
				)
			);
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}
	}
}
