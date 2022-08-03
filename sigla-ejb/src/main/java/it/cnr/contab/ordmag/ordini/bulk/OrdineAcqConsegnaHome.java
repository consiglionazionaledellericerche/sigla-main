/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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
 * Date 28/06/2017
 */
package it.cnr.contab.ordmag.ordini.bulk;
import java.sql.Connection;

import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class OrdineAcqConsegnaHome extends BulkHome {
	public OrdineAcqConsegnaHome(Connection conn) {
		super(OrdineAcqConsegnaBulk.class, conn);
	}
	public OrdineAcqConsegnaHome(Connection conn, PersistentCache persistentCache) {
		super(OrdineAcqConsegnaBulk.class, conn, persistentCache);
	}
	public SQLBuilder selectUnitaOperativaOrdByClause(UserContext userContext, OrdineAcqConsegnaBulk cons,
											   UnitaOperativaOrdHome unitaOperativaHome, UnitaOperativaOrdBulk unitaOperativaBulk,
											   CompoundFindClause compoundfindclause) throws PersistencyException {
		SQLBuilder sql = unitaOperativaHome.selectByClause(userContext, compoundfindclause);

		sql.addSQLClause("AND","UNITA_OPERATIVA_ORD.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));

		return sql;
	}
}