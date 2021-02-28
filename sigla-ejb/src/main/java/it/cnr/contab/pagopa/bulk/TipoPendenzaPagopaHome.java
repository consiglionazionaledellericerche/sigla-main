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

package it.cnr.contab.pagopa.bulk;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;

public class TipoPendenzaPagopaHome extends BulkHome {
	private static final long serialVersionUID = 1L;

	public TipoPendenzaPagopaHome(java.sql.Connection conn) {
		super(TipoPendenzaPagopaBulk.class,conn);
	}
	public TipoPendenzaPagopaHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(TipoPendenzaPagopaBulk.class,conn,persistentCache);
	}

	@Override
	public void initializePrimaryKeyForInsert(UserContext  usercontext,OggettoBulk oggettobulk) throws PersistencyException, ComponentException {
		try {
			TipoPendenzaPagopaBulk tipoAtto = (TipoPendenzaPagopaBulk)oggettobulk;
			tipoAtto.setId(
					new Integer(((Integer)findAndLockMax( oggettobulk, "id", new Integer(0) )).intValue()+1));
			super.initializePrimaryKeyForInsert(usercontext, tipoAtto);
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}
	}
}
