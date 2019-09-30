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
* Date 10/10/2005
*/
package it.cnr.contab.incarichi00.bulk;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class V_terzi_da_completareHome extends BulkHome {
	public V_terzi_da_completareHome(java.sql.Connection conn) {
		super(V_terzi_da_completareBulk.class, conn);
	}
	public V_terzi_da_completareHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_terzi_da_completareBulk.class, conn, persistentCache);
	}

	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause)
		throws PersistencyException
	{
		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
		//sql.addSQLClause("AND","V_TERZI_DA_COMPLETARE.ESERCIZIO",sql.EQUALS,CNRUserContext.getEsercizio(usercontext));
		//sql.addSQLClause("AND","V_TERZI_DA_CONGUAGLIARE.CD_CDS",sql.EQUALS,CNRUserContext.getCd_cds(usercontext));

		//	Se uo 999.000 in scrivania: visualizza tutto l'elenco
		//	Se uo ***.000 in scrivania: visualizza l'elenco di tutte le U.O. dello stesso CDS
		Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHomeCache().getHome(Unita_organizzativa_enteBulk.class).findAll().get(0);

		if (!((CNRUserContext) usercontext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa()))
		{
				sql.addSQLClause("AND","V_TERZI_DA_COMPLETARE.CD_CDS",SQLBuilder.EQUALS,CNRUserContext.getCd_cds(usercontext));

				Unita_organizzativaBulk uoScrivania = (Unita_organizzativaBulk)getHomeCache().getHome(Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(usercontext)));
				if(!uoScrivania.isUoCds())
					  sql.addSQLClause("AND","V_TERZI_DA_COMPLETARE.CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,CNRUserContext.getCd_unita_organizzativa(usercontext));  
		}	

		//if (compoundfindclause!= null)
		  // sql.addClause(compoundfindclause);
		return sql;
	}
}