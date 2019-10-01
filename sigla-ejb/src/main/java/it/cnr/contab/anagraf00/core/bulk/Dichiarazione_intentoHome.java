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

import it.cnr.contab.compensi00.docs.bulk.EstrazioniFiscaliVBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Dichiarazione_intentoHome extends BulkHome {
	public Dichiarazione_intentoHome(java.sql.Connection conn) {
		super(Dichiarazione_intentoBulk.class,conn);
	}
	public Dichiarazione_intentoHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(Dichiarazione_intentoBulk.class,conn,persistentCache);
	}
	
	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,it.cnr.jada.bulk.OggettoBulk esportatore) throws it.cnr.jada.persistency.PersistencyException {
		try {
			((Dichiarazione_intentoBulk) esportatore).setProgr(
				new Integer(
					((Integer)findAndLockMax( esportatore, "progr", new Integer(0))).intValue()+1
				)
			);
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}
	}
	
}
