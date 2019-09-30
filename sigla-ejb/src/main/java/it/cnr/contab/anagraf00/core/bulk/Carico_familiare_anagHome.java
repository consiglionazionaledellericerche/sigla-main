/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
