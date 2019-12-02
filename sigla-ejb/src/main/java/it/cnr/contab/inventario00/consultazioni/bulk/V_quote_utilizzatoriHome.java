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
 * Date 22/02/2008
 */
package it.cnr.contab.inventario00.consultazioni.bulk;
import java.sql.Connection;

import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class V_quote_utilizzatoriHome extends BulkHome {
	public V_quote_utilizzatoriHome(Connection conn) {
		super(V_quote_utilizzatoriBulk.class, conn);
	}
	public V_quote_utilizzatoriHome(Connection conn, PersistentCache persistentCache) {
		super(V_quote_utilizzatoriBulk.class, conn, persistentCache);
	}
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause) throws PersistencyException
	{
		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
		Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHomeCache().getHome(Unita_organizzativa_enteBulk.class).findAll().get(0);
		
		sql.addSQLClause("AND","V_QUOTE_UTILIZZATORI.ESERCIZIO",sql.EQUALS,CNRUserContext.getEsercizio(usercontext));	
		//	Se uo 999.000 in scrivania: visualizza tutto l'elenco
		if (!((CNRUserContext) usercontext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){			
			CdrBulk cdr=(CdrBulk)getHomeCache().getHome(CdrBulk.class).findByPrimaryKey(usercontext, new CdrBulk(CNRUserContext.getCd_cdr(usercontext)));
			cdr.setUnita_padre((Unita_organizzativaBulk)getHomeCache().getHome(Unita_organizzativaBulk.class).findByPrimaryKey(usercontext, new Unita_organizzativaBulk(cdr.getUnita_padre().getCd_unita_organizzativa())));
			if (cdr.isCdrSAC()){
				sql.addTableToHeader("V_UNITA_ORGANIZZATIVA_VALIDA,CDR");
				sql.addSQLClause("AND", "V_UNITA_ORGANIZZATIVA_VALIDA.ESERCIZIO", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(usercontext));
				sql.addSQLClause("AND", "FL_CDS", SQLBuilder.EQUALS,"N");
				sql.addSQLClause("AND", "CD_TIPO_UNITA", SQLBuilder.EQUALS,Tipo_unita_organizzativaHome.TIPO_UO_SAC);
				sql.addSQLJoin("CDR.CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,"V_UNITA_ORGANIZZATIVA_VALIDA.CD_UNITA_ORGANIZZATIVA");
				sql.addSQLJoin("CDR.CD_CENTRO_RESPONSABILITA",SQLBuilder.EQUALS,"V_QUOTE_UTILIZZATORI.CDR");
			}
			else
			{					
				sql.openParenthesis("AND");
				sql.addSQLClause("AND","CDR",SQLBuilder.EQUALS,CNRUserContext.getCd_cdr(usercontext));
				sql.addSQLClause("OR","CD_CDR_AFFERENZA",SQLBuilder.EQUALS,CNRUserContext.getCd_cdr(usercontext));
				sql.closeParenthesis();
			}
		}	
		return sql;
	}	
}