package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class ContattoHome extends BulkHome {
	public ContattoHome(java.sql.Connection conn) {
		super(ContattoBulk.class,conn);
	}
	public ContattoHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(ContattoBulk.class,conn,persistentCache);
	}
	/**
	 * Imposta il pg_contatto di un oggetto <code>ContattoBulk</code>.
	 *
	 * @param contatto <code>ContattoBulk</code>
	 *
	 * @exception PersistencyException
	 */

	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk contatto) throws PersistencyException {
		try {
			((ContattoBulk)contatto).setPg_contatto(
				new Long(
					((Long)findAndLockMax( contatto, "pg_contatto", new Long(0) )).longValue()+1
				)
			);
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}
	}
}
