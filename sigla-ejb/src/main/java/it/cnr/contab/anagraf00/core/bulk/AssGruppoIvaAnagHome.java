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

import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.PersistentCache;

public class AssGruppoIvaAnagHome extends BulkHome {
	public AssGruppoIvaAnagHome(java.sql.Connection conn) {
		super(AssGruppoIvaAnagBulk.class,conn);
	}
	public AssGruppoIvaAnagHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(AssGruppoIvaAnagBulk.class,conn,persistentCache);
	}
	@Override
	public void delete(Persistent persistent, UserContext userContext) throws PersistencyException {
		((AssGruppoIvaAnagBulk)persistent).setStato("ANN");
		((AssGruppoIvaAnagBulk)persistent).setDt_cancellazione(new java.sql.Timestamp(System.currentTimeMillis()));
		super.update(persistent, userContext);
	}
}
