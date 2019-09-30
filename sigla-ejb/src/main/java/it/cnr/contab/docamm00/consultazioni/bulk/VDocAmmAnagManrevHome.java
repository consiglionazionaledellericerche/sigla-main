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
 * Date 18/11/2016
 */
package it.cnr.contab.docamm00.consultazioni.bulk;
import java.sql.Connection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.docamm00.docs.bulk.Tipo_documento_ammBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class VDocAmmAnagManrevHome extends BulkHome {
	public VDocAmmAnagManrevHome(Connection conn) {
		super(VDocAmmAnagManrevBulk.class, conn);
	}
	public VDocAmmAnagManrevHome(Connection conn, PersistentCache persistentCache) {
		super(VDocAmmAnagManrevBulk.class, conn, persistentCache);
	}
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause)
			throws PersistencyException
		{
			SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
			//	Se uo 999.000 in scrivania: visualizza tutto l'elenco
			//	Se uo ***.000 in scrivania: visualizza l'elenco di tutte le U.O. dello stesso CDS
			Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHomeCache().getHome(Unita_organizzativa_enteBulk.class).findAll().get(0);

			if (!((CNRUserContext) usercontext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa()))
			{
					sql.addSQLClause("AND","V_DOC_AMM_ANAG_MANREV.CD_CDS_ORIGINE",SQLBuilder.EQUALS,CNRUserContext.getCd_cds(usercontext));

					Unita_organizzativaBulk uoScrivania = (Unita_organizzativaBulk)getHomeCache().getHome(Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(usercontext)));
					if(!uoScrivania.isUoCds())
						  sql.addSQLClause("AND","V_DOC_AMM_ANAG_MANREV.CD_UO_ORIGINE",SQLBuilder.EQUALS,CNRUserContext.getCd_unita_organizzativa(usercontext));  
			}	
			if (compoundfindclause!= null)
			   sql.addClause(compoundfindclause);
			return sql;
		}
}