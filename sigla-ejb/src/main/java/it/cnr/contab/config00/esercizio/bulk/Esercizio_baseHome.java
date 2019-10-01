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
* Date 20/02/2006
*/
package it.cnr.contab.config00.esercizio.bulk;
import java.util.Collection;

import it.cnr.contab.pdg00.cdip.bulk.Stampa_ripartizione_costiVBulk;
import it.cnr.contab.segnalazioni00.bulk.Stampa_attivita_siglaBulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class Esercizio_baseHome extends BulkHome {
	public Esercizio_baseHome(java.sql.Connection conn) {
		super(Esercizio_baseBulk.class, conn);
	}
	public Esercizio_baseHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Esercizio_baseBulk.class, conn, persistentCache);
	}
	
	
	public SQLBuilder selectEserciziByClause(Stampa_attivita_siglaBulk bulk) throws PersistencyException
	{
	PersistentHome pHome = getHomeCache().getHome(Esercizio_baseBulk.class);
	SQLBuilder sql = pHome.createSQLBuilder();
		
	return sql;
	}	

public Collection findEsercizi(Stampa_attivita_siglaBulk bulk) throws IntrospectionException, PersistencyException {
	SQLBuilder sql = this.selectEserciziByClause(bulk);
		return  fetchAll(sql);
	}

public Collection findEsercizi(Stampa_ripartizione_costiVBulk bulk) throws IntrospectionException, PersistencyException {
	SQLBuilder sql = this.selectEserciziByClause(bulk);
		return  fetchAll(sql);
	}

public SQLBuilder selectEserciziByClause(Stampa_ripartizione_costiVBulk bulk) throws PersistencyException
	{
	PersistentHome pHome = getHomeCache().getHome(Esercizio_baseBulk.class);
	SQLBuilder sql = pHome.createSQLBuilder();
	sql.setOrderBy("esercizio",it.cnr.jada.util.OrderConstants.ORDER_DESC);
	return sql;
	}	

}