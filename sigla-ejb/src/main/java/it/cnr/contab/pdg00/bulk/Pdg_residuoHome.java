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
* Created by Generator 1.0
* Date 30/05/2005
*/
package it.cnr.contab.pdg00.bulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class Pdg_residuoHome extends BulkHome {
	public Pdg_residuoHome(java.sql.Connection conn) {
		super(Pdg_residuoBulk.class, conn);
	}
	public Pdg_residuoHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Pdg_residuoBulk.class, conn, persistentCache);
	}
	public java.util.Collection findDettagli(Pdg_residuoBulk testata) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Pdg_residuo_detBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause("AND","esercizio",sql.EQUALS,testata.getEsercizio());
		sql.addClause("AND","cd_centro_responsabilita",sql.EQUALS,testata.getCd_centro_responsabilita());
		sql.setOrderBy("pg_dettaglio",it.cnr.jada.util.OrderConstants.ORDER_ASC);
		return dettHome.fetchAll(sql);
	}

	public java.util.Collection findDettagli(Pdg_residuoBulk testata, CompoundFindClause clause) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Pdg_residuo_detBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause(clause);
		sql.addClause("AND","esercizio",sql.EQUALS,testata.getEsercizio());
		sql.addClause("AND","cd_centro_responsabilita",sql.EQUALS,testata.getCd_centro_responsabilita());
		sql.setOrderBy("pg_dettaglio",it.cnr.jada.util.OrderConstants.ORDER_ASC);
		return dettHome.fetchAll(sql);
	}

	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk bulk) throws PersistencyException,ApplicationException {
		((Pdg_residuoBulk)bulk).setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
	}
}