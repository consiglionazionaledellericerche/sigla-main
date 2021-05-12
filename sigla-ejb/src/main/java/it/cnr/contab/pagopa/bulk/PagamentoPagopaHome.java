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
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class PagamentoPagopaHome extends BulkHome {
	private static final long serialVersionUID = 1L;

	public PagamentoPagopaHome(java.sql.Connection conn) {
		super(PagamentoPagopaBulk.class,conn);
	}
	public PagamentoPagopaHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(PagamentoPagopaBulk.class,conn,persistentCache);
	}

	@Override
	public void initializePrimaryKeyForInsert(UserContext  usercontext,OggettoBulk oggettobulk)throws PersistencyException, ComponentException {
		try {
			PagamentoPagopaBulk PagamentoPagopaBulk = (PagamentoPagopaBulk)oggettobulk;
			PagamentoPagopaBulk.setId(
					new Long(((Long)findAndLockMax( oggettobulk, "id", new Long(0) )).intValue()+1));
			super.initializePrimaryKeyForInsert(usercontext, PagamentoPagopaBulk);
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}
	}

	@Override
	public void delete(Persistent persistent, UserContext userContext) throws PersistencyException {
//		((PagamentoPagopaBulk)persistent).setStato(PagamentoPagopaBulk.STATO_ANNULLATO);
		super.update(persistent, userContext);
	}
	public SQLBuilder createSQLBuilder() {

		SQLBuilder sql = super.createSQLBuilder();
//		sql.addSQLClause("AND", "STATO", sql.NOT_EQUALS, PagamentoPagopaBulk.STATO_ANNULLATO);
		return sql;
	}
}
