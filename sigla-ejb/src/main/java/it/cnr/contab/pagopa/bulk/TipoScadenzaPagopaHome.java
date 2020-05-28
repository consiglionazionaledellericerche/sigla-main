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
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public class TipoScadenzaPagopaHome extends BulkHome {
	private static final long serialVersionUID = 1L;

	public TipoScadenzaPagopaHome(java.sql.Connection conn) {
		super(TipoScadenzaPagopaBulk.class,conn);
	}
	public TipoScadenzaPagopaHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(TipoScadenzaPagopaBulk.class,conn,persistentCache);
	}

	@Override
	public void initializePrimaryKeyForInsert(UserContext  usercontext,OggettoBulk oggettobulk) throws PersistencyException, ComponentException {
		try {
			TipoScadenzaPagopaBulk tipoAtto = (TipoScadenzaPagopaBulk)oggettobulk;
			tipoAtto.setId(
					new Integer(((Integer)findAndLockMax( oggettobulk, "id", new Integer(0) )).intValue()+1));
			super.initializePrimaryKeyForInsert(usercontext, tipoAtto);
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}
	}
}
