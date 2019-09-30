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
* Creted by Generator 1.0
* Date 13/04/2005
*/
package it.cnr.contab.incarichi00.bulk;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.sql.SQLException;
public class Ass_incarico_uoHome extends BulkHome {
	public Ass_incarico_uoHome(java.sql.Connection conn) {
		super(Ass_incarico_uoBulk.class, conn);
	}
	public Ass_incarico_uoHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Ass_incarico_uoBulk.class, conn, persistentCache);
	}
	/**
	 * @author rpagano
	 * 
	 * @param ass_incarico_uo
	 * @return true se esiste un documento contabile associato, false altrimenti
	 * @throws IntrospectionException
	 * @throws PersistencyException
	 * @throws SQLException
	 */
	public boolean existsDocContForAssIncaricoUo(Ass_incarico_uoBulk ass_incarico_uo) throws IntrospectionException, PersistencyException, SQLException {
		PersistentHome dettHomeObb = getHomeCache().getHome(ObbligazioneBulk.class);
		SQLBuilder sqlObb = dettHomeObb.createSQLBuilder();
		sqlObb.resetColumns();
		sqlObb.addColumn("PG_REPERTORIO");			
		sqlObb.addSQLClause("AND","ESERCIZIO_REP",SQLBuilder.EQUALS,ass_incarico_uo.getEsercizio());
		sqlObb.addSQLClause("AND","PG_REPERTORIO",SQLBuilder.EQUALS,ass_incarico_uo.getPg_repertorio());
		sqlObb.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,ass_incarico_uo.getCd_unita_organizzativa());
	
		if (sqlObb.executeExistsQuery(getConnection())) return true;
		
		PersistentHome dettHomeComp = getHomeCache().getHome(CompensoBulk.class);
		SQLBuilder sqlComp = dettHomeComp.createSQLBuilder();
		sqlComp.resetColumns();
		sqlComp.addColumn("PG_REPERTORIO");			
		sqlComp.addSQLClause("AND","ESERCIZIO_REP",SQLBuilder.EQUALS,ass_incarico_uo.getEsercizio());
		sqlComp.addSQLClause("AND","PG_REPERTORIO",SQLBuilder.EQUALS,ass_incarico_uo.getPg_repertorio());
		sqlComp.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,ass_incarico_uo.getCd_unita_organizzativa());

		if (sqlComp.executeExistsQuery(getConnection())) return true;

		PersistentHome dettHomeMinicarriera = getHomeCache().getHome(MinicarrieraBulk.class);
		SQLBuilder sqlMinicarriera = dettHomeMinicarriera.createSQLBuilder();
		sqlMinicarriera.resetColumns();
		sqlMinicarriera.addColumn("PG_REPERTORIO");			
		sqlMinicarriera.addSQLClause("AND","ESERCIZIO_REP",SQLBuilder.EQUALS,ass_incarico_uo.getEsercizio());
		sqlMinicarriera.addSQLClause("AND","PG_REPERTORIO",SQLBuilder.EQUALS,ass_incarico_uo.getPg_repertorio());
		sqlMinicarriera.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,ass_incarico_uo.getCd_unita_organizzativa());

		if (sqlMinicarriera.executeExistsQuery(getConnection())) return true;

		return false;
	}
	
}