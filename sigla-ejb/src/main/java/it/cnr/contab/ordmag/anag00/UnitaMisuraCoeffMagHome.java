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
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import java.rmi.RemoteException;
import java.sql.Connection;

import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

import javax.ejb.EJBException;

public class UnitaMisuraCoeffMagHome extends BulkHome {
	public UnitaMisuraCoeffMagHome(Connection conn) {
		super(UnitaMisuraCoeffMagBulk.class, conn);
	}
	public UnitaMisuraCoeffMagHome(Connection conn, PersistentCache persistentCache) {
		super(UnitaMisuraCoeffMagBulk.class, conn, persistentCache);
	}

	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause) throws PersistencyException {
		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
		sql.addClause("AND","cdCds",SQLBuilder.EQUALS, CNRUserContext.getCd_cds(usercontext));
		return sql;
	}
	public SQLBuilder selectMagazzinoByClause(it.cnr.jada.UserContext userContext, UnitaMisuraCoeffMagBulk unitaMisuraCoeffMagBulk, MagazzinoHome magazzinoHome, MagazzinoBulk magazzinoBulk, CompoundFindClause clause)  throws ComponentException, EJBException, RemoteException {
		SQLBuilder sql = magazzinoHome.createSQLBuilder();
		sql.addClause("AND","cdCds",SQLBuilder.EQUALS, CNRUserContext.getCd_cds(userContext));
		return sql;
	}
}