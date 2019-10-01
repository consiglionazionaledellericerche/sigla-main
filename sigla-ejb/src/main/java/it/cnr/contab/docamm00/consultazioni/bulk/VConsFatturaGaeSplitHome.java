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
 * Date 22/04/2010
 */
package it.cnr.contab.docamm00.consultazioni.bulk;
import java.sql.Connection;
import java.util.List;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class VConsFatturaGaeSplitHome extends BulkHome {
	public VConsFatturaGaeSplitHome(Connection conn) {
		super(VConsFatturaGaeSplitBulk.class, conn);
	}
	public VConsFatturaGaeSplitHome(Connection conn, PersistentCache persistentCache) {
		super(VConsFatturaGaeSplitBulk.class, conn, persistentCache);
	}
	
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause) throws PersistencyException {
		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
		if (compoundfindclause == null || !compoundfindclause.toString().contains("esercizio "))
			sql.addClause("AND","esercizio",SQLBuilder.EQUALS, CNRUserContext.getEsercizio(usercontext));
	    PersistentHome uoEnteHome = getHomeCache().getHome(Unita_organizzativa_enteBulk.class);
		List result = uoEnteHome.fetchAll( uoEnteHome.createSQLBuilder() );
		String uoEnte = ((Unita_organizzativaBulk) result.get(0)).getCd_unita_organizzativa();
		Unita_organizzativaBulk uoScrivania = (Unita_organizzativaBulk)getHomeCache().getHome(Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(usercontext)));
		if(!uoScrivania.isUoCds())
			  sql.addClause("AND","cdUnitaOrganizzativa",SQLBuilder.EQUALS,CNRUserContext.getCd_unita_organizzativa(usercontext));
	    if(CNRUserContext.getCd_unita_organizzativa(usercontext).compareTo(uoEnte)!=0)
	    	sql.addClause("AND","cdCds",SQLBuilder.EQUALS, CNRUserContext.getCd_cds(usercontext));
		return sql;
	}
}