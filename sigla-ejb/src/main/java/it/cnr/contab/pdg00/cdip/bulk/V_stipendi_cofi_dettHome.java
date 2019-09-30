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
* Date 07/08/2006
*/
package it.cnr.contab.pdg00.cdip.bulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class V_stipendi_cofi_dettHome extends BulkHome {
	public V_stipendi_cofi_dettHome(java.sql.Connection conn) {
		super(V_stipendi_cofi_dettBulk.class, conn);
	}
	public V_stipendi_cofi_dettHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_stipendi_cofi_dettBulk.class, conn, persistentCache);
	}
	public java.util.List findDett(it.cnr.jada.UserContext userContext,V_stipendi_cofi_dettBulk dett) throws IntrospectionException, PersistencyException 
	{
		Integer esercizio = ((CNRUserContext) userContext).getEsercizio();
		SQLBuilder sql = createSQLBuilder();
		sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,esercizio);
		sql.addSQLClause("AND","MESE",sql.EQUALS,dett.getMese());
		sql.addOrderBy("TIPO_FLUSSO DESC");
		sql.addOrderBy("ENTRATA_SPESA DESC");
		return fetchAll(sql);
	}	
}