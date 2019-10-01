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
 * Created by Aurelio's BulkGenerator 1.0
 * Date 20/06/2007
 */
package it.cnr.contab.docamm00.consultazioni.bulk;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Iterator;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.persistency.sql.SimpleFindClause;
public class V_estrai_glaHome extends BulkHome {
	public V_estrai_glaHome(Connection conn) {
		super(V_estrai_glaBulk.class, conn);
	}
	public V_estrai_glaHome(Connection conn, PersistentCache persistentCache) {
		super(V_estrai_glaBulk.class, conn, persistentCache);
	}
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause)
	throws PersistencyException
{
	
	SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
	sql.addSQLClause("AND","ESERCIZIO_PAGAMENTO",sql.EQUALS,((CNRUserContext)usercontext).getEsercizio());
	return sql;
}	
}