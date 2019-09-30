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
package it.cnr.contab.config00.contratto.bulk;
import java.sql.SQLException;

import it.cnr.contab.doccont00.core.bulk.AccertamentoBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.persistency.sql.SQLUnion;
public class Ass_contratto_uoHome extends BulkHome {
	public Ass_contratto_uoHome(java.sql.Connection conn) {
		super(Ass_contratto_uoBulk.class, conn);
	}
	public Ass_contratto_uoHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Ass_contratto_uoBulk.class, conn, persistentCache);
	}
	/**
	 * @author mspasiano
	 * 
	 * @param ass_contratto_uo
	 * @return true se esiste un documento contabile associato, false altrimenti
	 * @throws IntrospectionException
	 * @throws PersistencyException
	 * @throws SQLException
	 */
	public boolean existsDocContForAssContrattoUo(Ass_contratto_uoBulk ass_contratto_uo) throws IntrospectionException, PersistencyException, SQLException {
		PersistentHome dettHomeAcc = getHomeCache().getHome(AccertamentoBulk.class);
		SQLBuilder sqlAcc = dettHomeAcc.createSQLBuilder();	
		sqlAcc.resetColumns();
		sqlAcc.addColumn("PG_CONTRATTO");	
		sqlAcc.addSQLClause("AND","ESERCIZIO_CONTRATTO",SQLBuilder.EQUALS,ass_contratto_uo.getEsercizio());
		sqlAcc.addSQLClause("AND","STATO_CONTRATTO",SQLBuilder.EQUALS,ass_contratto_uo.getContratto().getStato());
		sqlAcc.addSQLClause("AND","PG_CONTRATTO",SQLBuilder.EQUALS,ass_contratto_uo.getPg_contratto());
		sqlAcc.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,ass_contratto_uo.getCd_unita_organizzativa());

		PersistentHome dettHomeObb = getHomeCache().getHome(ObbligazioneBulk.class);
		SQLBuilder sqlObb = dettHomeObb.createSQLBuilder();
		sqlObb.resetColumns();
		sqlObb.addColumn("PG_CONTRATTO");			
		sqlObb.addSQLClause("AND","ESERCIZIO_CONTRATTO",SQLBuilder.EQUALS,ass_contratto_uo.getEsercizio());
		sqlObb.addSQLClause("AND","STATO_CONTRATTO",SQLBuilder.EQUALS,ass_contratto_uo.getContratto().getStato());
		sqlObb.addSQLClause("AND","PG_CONTRATTO",SQLBuilder.EQUALS,ass_contratto_uo.getPg_contratto());
		sqlObb.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,ass_contratto_uo.getCd_unita_organizzativa());
	
		SQLUnion union = sqlObb.union(sqlAcc,true);
		return union.executeExistsQuery(getConnection());
	}
	
}