package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Rappresentante_legaleHome extends BulkHome {
	public Rappresentante_legaleHome(java.sql.Connection conn) {
		super(Rappresentante_legaleBulk.class,conn);
	}
	public Rappresentante_legaleHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(Rappresentante_legaleBulk.class,conn,persistentCache);
	}
	/**
	 * Imposta il pg_banca di un oggetto <code>Rappresentante_legaleBulk</code>.
	 *
	 * @param bank <code>BancaBulk</code>
	 *
	 * @exception PersistencyException
	 */

	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,it.cnr.jada.bulk.OggettoBulk rapp) throws it.cnr.jada.persistency.PersistencyException {
		try {
			((Rappresentante_legaleBulk)rapp).setPg_rapp_legale(
				new Long(
					((Long)findAndLockMax( rapp, "pg_rapp_legale", new Long(0) )).longValue()+1
				)
			);
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}
	}
}
