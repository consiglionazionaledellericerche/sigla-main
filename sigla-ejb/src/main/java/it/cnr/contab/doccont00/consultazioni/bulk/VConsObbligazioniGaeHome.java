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

/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 27/01/2015
 */
package it.cnr.contab.doccont00.consultazioni.bulk;
import java.sql.Connection;

import it.cnr.contab.consultazioni.bulk.ConsultazioniRestHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class VConsObbligazioniGaeHome extends BulkHome implements ConsultazioniRestHome{
	public VConsObbligazioniGaeHome(Connection conn) {
		super(VConsObbligazioniGaeBulk.class, conn);
	}
	public VConsObbligazioniGaeHome(Connection conn, PersistentCache persistentCache) {
		super(VConsObbligazioniGaeBulk.class, conn, persistentCache);
	}

	@Override
	public SQLBuilder restSelect(UserContext userContext, SQLBuilder sql, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws ComponentException, PersistencyException {
		sql.addClause( "AND", "cd_uo_origine", sql.EQUALS, ((CNRUserContext)userContext).getCd_unita_organizzativa());
//		sql.addClause( "AND", "esercizio", sql.EQUALS, ((CNRUserContext)userContext).getEsercizio());
		return sql;
	}
}