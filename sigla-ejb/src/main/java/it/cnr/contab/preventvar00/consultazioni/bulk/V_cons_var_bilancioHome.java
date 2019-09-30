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
* Date 23/05/2006
*/
package it.cnr.contab.preventvar00.consultazioni.bulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class V_cons_var_bilancioHome extends BulkHome {
	public V_cons_var_bilancioHome(java.sql.Connection conn) {
		super(V_cons_var_bilancioBulk.class, conn);
	}
	public V_cons_var_bilancioHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_cons_var_bilancioBulk.class, conn, persistentCache);
	}
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause) throws PersistencyException
{
	SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
	sql.addClause("AND","esercizio",sql.EQUALS,CNRUserContext.getEsercizio(usercontext));
	sql.addClause("AND","cd_cds",SQLBuilder.EQUALS,CNRUserContext.getCd_cds(usercontext));

	return sql;
}	
}