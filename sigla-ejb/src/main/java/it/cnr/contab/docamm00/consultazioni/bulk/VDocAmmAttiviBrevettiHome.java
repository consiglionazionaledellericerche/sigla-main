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
 * Date 12/09/2016
 */
package it.cnr.contab.docamm00.consultazioni.bulk;
import java.sql.Connection;
import java.util.Enumeration;

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

public class VDocAmmAttiviBrevettiHome extends BulkHome implements ConsultazioniRestHome {
	public VDocAmmAttiviBrevettiHome(Connection conn) {
		super(VDocAmmAttiviBrevettiBulk.class, conn);
	}
	public VDocAmmAttiviBrevettiHome(Connection conn, PersistentCache persistentCache) {
		super(VDocAmmAttiviBrevettiBulk.class, conn, persistentCache);
	}

	@Override
	public SQLBuilder restSelect(UserContext userContext, SQLBuilder sql, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws ComponentException, PersistencyException {
		if (compoundfindclause != null && compoundfindclause.getClauses() != null){
			Boolean trovataCondizioneTrovato = false;
			CompoundFindClause newClauses = new CompoundFindClause();
			Enumeration e = compoundfindclause.getClauses();
			SQLBuilder sqlExists = null;
			while(e.hasMoreElements() ){
				FindClause findClause = (FindClause) e.nextElement();
				if (findClause instanceof SimpleFindClause){
					SimpleFindClause clause = (SimpleFindClause)findClause;
					int operator = clause.getOperator();
					if (clause.getPropertyName() != null && clause.getPropertyName().equals("pgTrovato") &&
							operator == SQLBuilder.EQUALS){
						trovataCondizioneTrovato = true;
						VFatturaAttivaRigaBrevettiHome home = (VFatturaAttivaRigaBrevettiHome) getHomeCache().getHome(VFatturaAttivaRigaBrevettiBulk.class);
						sqlExists = home.createSQLBuilder();
						sqlExists.addSQLJoin("V_DOC_AMM_ATTIVI_BREVETTI.ESERCIZIO", "V_FATTURA_ATTIVA_RIGA_BREVETTI.ESERCIZIO");
						sqlExists.addSQLJoin("V_DOC_AMM_ATTIVI_BREVETTI.CD_CDS", "V_FATTURA_ATTIVA_RIGA_BREVETTI.CD_CDS");
						sqlExists.addSQLJoin("V_DOC_AMM_ATTIVI_BREVETTI.CD_UNITA_ORGANIZZATIVA", "V_FATTURA_ATTIVA_RIGA_BREVETTI.CD_UNITA_ORGANIZZATIVA");
						sqlExists.addSQLJoin("V_DOC_AMM_ATTIVI_BREVETTI.PG_FATTURA_ATTIVA", "V_FATTURA_ATTIVA_RIGA_BREVETTI.PG_FATTURA_ATTIVA");
						sqlExists.addSQLJoin("V_DOC_AMM_ATTIVI_BREVETTI.TIPO_FATTURA", "V_FATTURA_ATTIVA_RIGA_BREVETTI.TIPO_FATTURA");
						sqlExists.addSQLClause("AND","V_FATTURA_ATTIVA_RIGA_BREVETTI.PG_TROVATO",SQLBuilder.EQUALS, clause.getValue() );
					} else {
						newClauses.addClause(clause.getLogicalOperator(), clause.getPropertyName(), clause.getOperator(), clause.getValue());
					}
				}
			}
			if (trovataCondizioneTrovato){
				sql = selectByClause(userContext, newClauses);
				sql.addSQLExistsClause("AND", sqlExists);
			}
		}
		if ( !isUoEnte(userContext)){
			sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext));
			sql.addSQLClause("AND", "CD_CDS", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext));
		}
		return sql;
	}

	private Boolean isUoEnte(UserContext userContext) throws PersistencyException, ComponentException{
		Unita_organizzativa_enteBulk uoEnte = getUoEnte(userContext);
		if ( ((CNRUserContext)userContext).getCd_unita_organizzativa().equals ( uoEnte.getCd_unita_organizzativa() )){
			return true;
		}
		return false;
	}

	private Unita_organizzativa_enteBulk getUoEnte(UserContext userContext)
			throws PersistencyException, ComponentException {
		Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk) getHomeCache().getHome(Unita_organizzativa_enteBulk.class ).findAll().get(0);
		return uoEnte;
	}
}