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
* Date 15/02/2006
*/
package it.cnr.contab.varstanz00.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class Ass_var_stanz_res_cdrHome extends BulkHome {
	public Ass_var_stanz_res_cdrHome(java.sql.Connection conn) {
		super(Ass_var_stanz_res_cdrBulk.class, conn);
	}
	public Ass_var_stanz_res_cdrHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Ass_var_stanz_res_cdrBulk.class, conn, persistentCache);
	}
	public java.util.Collection findDettagliSpesa(Ass_var_stanz_res_cdrBulk testata) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Var_stanz_res_rigaBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,testata.getEsercizio());
		sql.addSQLClause("AND","PG_VARIAZIONE",SQLBuilder.EQUALS,testata.getPg_variazione());
		sql.addSQLClause("AND","CD_CDR",SQLBuilder.EQUALS,testata.getCd_centro_responsabilita());
		return dettHome.fetchAll(sql);
	}
	public java.util.Collection findDettagliSpesa(Var_stanz_resBulk testata) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Var_stanz_res_rigaBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,testata.getEsercizio());
		sql.addSQLClause("AND","PG_VARIAZIONE",SQLBuilder.EQUALS,testata.getPg_variazione());
		return dettHome.fetchAll(sql);
	}	
}