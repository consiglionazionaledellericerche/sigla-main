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
package it.cnr.contab.pdg00.consultazioni.bulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class V_dpdg_aggregato_spe_det_dHome extends BulkHome {
	public V_dpdg_aggregato_spe_det_dHome(java.sql.Connection conn) {
		super(V_dpdg_aggregato_spe_det_dBulk.class, conn);
	}
	public V_dpdg_aggregato_spe_det_dHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_dpdg_aggregato_spe_det_dBulk.class, conn, persistentCache);
	}
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause)
		throws PersistencyException
	{
		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
		sql.addTableToHeader("UTENTE");
		sql.addSQLClause("AND","V_DPDG_AGGREGATO_SPE_DET_D.ESERCIZIO",sql.EQUALS,CNRUserContext.getEsercizio(usercontext));
		sql.addSQLClause("AND","UTENTE.CD_UTENTE",sql.EQUALS,CNRUserContext.getUser(usercontext));
		sql.addSQLJoin("UTENTE.CD_CDR","V_DPDG_AGGREGATO_SPE_DET_D.CD_CENTRO_RESPONSABILITA");
		return sql;
	}	
}