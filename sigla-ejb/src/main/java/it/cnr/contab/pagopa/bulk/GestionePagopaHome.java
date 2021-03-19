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

import it.cnr.contab.bollo00.tabrif.bulk.Tipo_atto_bolloBulk;
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

public class GestionePagopaHome extends BulkHome {
	private static final long serialVersionUID = 1L;

	public GestionePagopaHome(java.sql.Connection conn) {
		super(GestionePagopaBulk.class,conn);
	}
	public GestionePagopaHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(GestionePagopaBulk.class,conn,persistentCache);
	}

	@Override
	public void initializePrimaryKeyForInsert(UserContext  usercontext,OggettoBulk oggettobulk)throws PersistencyException, ComponentException {
		try {
			GestionePagopaBulk atto = (GestionePagopaBulk)oggettobulk;
			atto.setId(
					new Integer(((Integer)findAndLockMax( oggettobulk, "id", new Integer(0) )).intValue()+1));
			super.initializePrimaryKeyForInsert(usercontext, atto);
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}
	}
	public GestionePagopaBulk findGestionePagopa(Timestamp data) throws PersistencyException, ComponentException {
		SQLBuilder sql = this.createSQLBuilder();

		if (Optional.ofNullable(data).isPresent()) {
			sql.addClause(FindClause.AND,"dtIniValidita",SQLBuilder.LESS_EQUALS, data );

			sql.openParenthesis(FindClause.AND);
			sql.addClause(FindClause.OR,"dtFinValidita",SQLBuilder.ISNULL, null);
			sql.addClause(FindClause.OR,"dtFinValidita",SQLBuilder.GREATER_EQUALS, data);
			sql.closeParenthesis();
		} else {
			throw new ApplicationException("Data non indicata per recuperare la gestione PagoPA");
		}

		List<GestionePagopaBulk> l = this.fetchAll(sql);
		if (l.stream().count()>1)
			throw new ApplicationException("Errore nei dati della Gestione PagoPA: esistono periodi incongruenti!");

		return l.stream().findFirst().orElse(null);
	}


}
