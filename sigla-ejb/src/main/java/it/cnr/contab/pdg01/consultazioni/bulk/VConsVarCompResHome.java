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
 * Date 31/03/2014
 */
package it.cnr.contab.pdg01.consultazioni.bulk;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.consultazioni.bulk.ConsultazioniRestHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.persistency.sql.SimpleFindClause;

import java.sql.Connection;
import java.util.Enumeration;

public class VConsVarCompResHome extends BulkHome implements ConsultazioniRestHome {
	private static final long serialVersionUID = 1L;
	public VConsVarCompResHome(Connection conn) {
		super(VConsVarCompResBulk.class, conn);
	}
	public VConsVarCompResHome(Connection conn, PersistentCache persistentCache) {
		super(VConsVarCompResBulk.class, conn, persistentCache);
	}
	
	@Override
	public SQLBuilder selectByClause(UserContext userContext,
			CompoundFindClause compoundfindclause) throws PersistencyException {
		SQLBuilder sql = super.selectByClause(userContext, compoundfindclause);
		sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,CNRUserContext.getEsercizio(userContext));
		Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) 
				getHomeCache().getHome(Unita_organizzativa_enteBulk.class).findAll().get(0);
		if (!CNRUserContext.getCd_unita_organizzativa(userContext).equals(ente.getCd_unita_organizzativa())){
			sql.openParenthesis("AND");
			sql.addSQLClause("AND","CDR_PROPONENTE",SQLBuilder.EQUALS,CNRUserContext.getCd_cdr(userContext));
			sql.addSQLClause("OR","CDR_ASSEGN",SQLBuilder.EQUALS,CNRUserContext.getCd_cdr(userContext));
			sql.closeParenthesis();
		}
		return sql;
	}

	@Override
	public SQLBuilder restSelect(UserContext userContext, SQLBuilder sql, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws ComponentException, PersistencyException {
		if (compoundfindclause != null && compoundfindclause.getClauses() != null){
			Boolean trovataCondizioneCdrPersonale = false;
			CompoundFindClause newClauses = new CompoundFindClause();
			Enumeration e = compoundfindclause.getClauses();
			SQLBuilder sqlCDR = null;
			while(e.hasMoreElements() ){
				FindClause findClause = (FindClause) e.nextElement();
				if (findClause instanceof SimpleFindClause){
					SimpleFindClause clause = (SimpleFindClause)findClause;
					int operator = clause.getOperator();
					if (clause.getPropertyName() != null && clause.getPropertyName().equals("cdrPersonale") &&
							operator == SQLBuilder.EQUALS && "S".equals((String)clause.getValue())){
						trovataCondizioneCdrPersonale = true;
						sqlCDR = getHomeCache().getHome(Configurazione_cnrBulk.class).createSQLBuilder();
						sqlCDR.resetColumns();
						sqlCDR.addColumn("VAL01");
						sqlCDR.addSQLClause("AND", "CD_CHIAVE_PRIMARIA", SQLBuilder.EQUALS, Configurazione_cnrBulk.PK_CDR_SPECIALE);
						sqlCDR.addSQLClause("AND", "CD_CHIAVE_SECONDARIA", SQLBuilder.EQUALS, Configurazione_cnrBulk.SK_CDR_PERSONALE);
					} else {
						newClauses.addClause(clause.getLogicalOperator(), clause.getPropertyName(), clause.getOperator(), clause.getValue());
					}
				}
			}
			if (trovataCondizioneCdrPersonale){
				sql =  selectByClause(userContext, newClauses);
				sql.addSQLClause("AND", "CDR_ASSEGN", sql.EQUALS, sqlCDR);
			}
		}
		return sql;
	}
}