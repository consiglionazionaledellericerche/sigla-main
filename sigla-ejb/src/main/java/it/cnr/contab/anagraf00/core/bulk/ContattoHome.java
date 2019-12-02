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
