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
 * Date 11/01/2010
 */
package it.cnr.contab.segnalazioni00.bulk;
import java.sql.Connection;
import java.util.Collection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class ConfrontoSiglaDwhHome extends BulkHome {
	
//	private java.util.Collection dt_elaborazioni;
	
	public ConfrontoSiglaDwhHome(Connection conn) {
		super(ConfrontoSiglaDwhBulk.class, conn);
	}
	public ConfrontoSiglaDwhHome(Connection conn, PersistentCache persistentCache) {
		super(ConfrontoSiglaDwhBulk.class, conn, persistentCache);
	}
	
	public SQLBuilder selectDt_elaborazioniByClause(Stampa_confronto_sigla_dwhBulk bulk) throws PersistencyException
		{
		PersistentHome pHome = getHomeCache().getHome(ConfrontoSiglaDwhBulk.class,"STAMPA");
		SQLBuilder sql = pHome.createSQLBuilder();
		sql.setDistinctClause(true);
		
		return sql;
		}

	public Collection findDt_elaborazioni(Stampa_confronto_sigla_dwhBulk bulk) throws IntrospectionException, PersistencyException {
		SQLBuilder sql = this.selectDt_elaborazioniByClause(bulk);			
			return  fetchAll(sql);
		}
}