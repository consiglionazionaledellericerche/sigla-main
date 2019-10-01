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
* Date 20/04/2006
*/
package it.cnr.contab.doccont00.consultazioni.bulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.CdsHome;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class V_cons_mandati_terzoHome extends BulkHome {
	public V_cons_mandati_terzoHome(java.sql.Connection conn) {
		super(V_cons_mandati_terzoBulk.class, conn);
	}
	public V_cons_mandati_terzoHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_cons_mandati_terzoBulk.class, conn, persistentCache);
	}
	
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause)
	throws PersistencyException
{
		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
		
		Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHomeCache().getHome(Unita_organizzativa_enteBulk.class).findAll().get(0);
		
		//	Se uo 999.000 in scrivania: visualizza tutto l'elenco
		if (((CNRUserContext) usercontext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){
			
			sql.addSQLClause("AND","V_CONS_MANDATI_TERZO.ESERCIZIO",sql.EQUALS,CNRUserContext.getEsercizio(usercontext));
			sql.addSQLClause("AND","V_CONS_MANDATI_TERZO.CDS",SQLBuilder.EQUALS,CNRUserContext.getCd_cds(usercontext));
		}
		else {
			sql.addSQLClause("AND","V_CONS_MANDATI_TERZO.ESERCIZIO",sql.EQUALS,CNRUserContext.getEsercizio(usercontext));
			sql.addSQLClause("AND","V_CONS_MANDATI_TERZO.CDS_ORIGINE",SQLBuilder.EQUALS,CNRUserContext.getCd_cds(usercontext));
		}
		return sql;
		
} 
}