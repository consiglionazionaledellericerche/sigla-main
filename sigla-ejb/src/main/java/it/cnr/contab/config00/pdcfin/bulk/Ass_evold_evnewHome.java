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

package it.cnr.contab.config00.pdcfin.bulk;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class Ass_evold_evnewHome extends BulkHome {
	private static final long serialVersionUID = 1L;

	public Ass_evold_evnewHome(java.sql.Connection conn) {
		super(Ass_evold_evnewBulk.class,conn);
	}

	public Ass_evold_evnewHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(Ass_evold_evnewBulk.class,conn,persistentCache);
	}

	public SQLBuilder selectElemento_voce_oldByClause( Ass_evold_evnewBulk bulk, Elemento_voceHome home, it.cnr.jada.bulk.OggettoBulk bulkClause, CompoundFindClause clause) throws java.lang.reflect.InvocationTargetException,IllegalAccessException, it.cnr.jada.persistency.PersistencyException {
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio_old_search() );
		sql.addClause(FindClause.AND, "ti_gestione", SQLBuilder.EQUALS, bulk.getTi_gestione_search() );

		sql.addTableToHeader("PARAMETRI_CNR");
		sql.addSQLJoin("PARAMETRI_CNR.ESERCIZIO","ELEMENTO_VOCE.ESERCIZIO");

		sql.openParenthesis(FindClause.AND);
			sql.addSQLClause(FindClause.OR,"PARAMETRI_CNR.FL_NUOVO_PDG='Y'");
	
			sql.openParenthesis(FindClause.OR);
//				if (Elemento_voceHome.GESTIONE_ENTRATE.equals(bulk.getTi_gestione_search()))
//					sql.addClause(FindClause.AND, "ti_appartenenza", SQLBuilder.EQUALS, Elemento_voceHome.APPARTENENZA_CNR);
//				else if (Elemento_voceHome.GESTIONE_SPESE.equals(bulk.getTi_gestione_search()))
//					sql.addClause(FindClause.AND, "ti_appartenenza", SQLBuilder.EQUALS, Elemento_voceHome.APPARTENENZA_CDS);
			if (Elemento_voceHome.GESTIONE_SPESE.equals(bulk.getTi_gestione_search()))
				sql.addClause(FindClause.AND, "ti_appartenenza", SQLBuilder.EQUALS, Elemento_voceHome.APPARTENENZA_CDS);

			sql.addClause(FindClause.AND, "ti_elemento_voce", SQLBuilder.EQUALS, Elemento_voceHome.TIPO_CAPITOLO);
//				sql.addClause(FindClause.AND, "fl_azzera_residui", SQLBuilder.EQUALS, Boolean.FALSE);
			sql.closeParenthesis();
		sql.closeParenthesis();
		sql.addClause( clause );
		return sql;
	}
	
	public SQLBuilder selectElemento_voce_newByClause( Ass_evold_evnewBulk bulk, Elemento_voceHome home, it.cnr.jada.bulk.OggettoBulk bulkClause, CompoundFindClause clause) throws java.lang.reflect.InvocationTargetException,IllegalAccessException, it.cnr.jada.persistency.PersistencyException {
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio_new_search() );
		sql.addClause(FindClause.AND, "ti_gestione", SQLBuilder.EQUALS, bulk.getTi_gestione_search() );
		sql.addClause( clause );
		return sql;
	}
	
	@SuppressWarnings("rawtypes")
	public java.util.List findAssElementoVoceNewList( Elemento_voceBulk voceOldBulk ) throws IntrospectionException,PersistencyException 
	{
		PersistentHome evnewHome = getHomeCache().getHome(Elemento_voceBulk.class);
		SQLBuilder sql = evnewHome.createSQLBuilder();
		sql.addTableToHeader("ASS_EVOLD_EVNEW");
		sql.addSQLJoin("ELEMENTO_VOCE.ESERCIZIO", "ASS_EVOLD_EVNEW.ESERCIZIO_NEW");
		sql.addSQLJoin("ELEMENTO_VOCE.TI_APPARTENENZA", "ASS_EVOLD_EVNEW.TI_APPARTENENZA_NEW");
		sql.addSQLJoin("ELEMENTO_VOCE.TI_GESTIONE", "ASS_EVOLD_EVNEW.TI_GESTIONE_NEW");
		sql.addSQLJoin("ELEMENTO_VOCE.CD_ELEMENTO_VOCE", "ASS_EVOLD_EVNEW.CD_ELEMENTO_VOCE_NEW");
		sql.addSQLClause(FindClause.AND, "ASS_EVOLD_EVNEW.ESERCIZIO_OLD", SQLBuilder.EQUALS, voceOldBulk.getEsercizio());
		sql.addSQLClause(FindClause.AND, "ASS_EVOLD_EVNEW.TI_APPARTENENZA_OLD", SQLBuilder.EQUALS, voceOldBulk.getTi_appartenenza());
		sql.addSQLClause(FindClause.AND, "ASS_EVOLD_EVNEW.TI_GESTIONE_OLD", SQLBuilder.EQUALS, voceOldBulk.getTi_gestione());
		sql.addSQLClause(FindClause.AND, "ASS_EVOLD_EVNEW.CD_ELEMENTO_VOCE_OLD", SQLBuilder.EQUALS, voceOldBulk.getCd_elemento_voce());
		return evnewHome.fetchAll(sql);
	}
	
	@SuppressWarnings("rawtypes")
	public java.util.List findAssElementoVoceOldList( Elemento_voceBulk voceNewBulk ) throws IntrospectionException,PersistencyException 
	{
		PersistentHome evnewHome = getHomeCache().getHome(Elemento_voceBulk.class);
		SQLBuilder sql = evnewHome.createSQLBuilder();
		sql.addTableToHeader("ASS_EVOLD_EVNEW");
		sql.addSQLJoin("ELEMENTO_VOCE.ESERCIZIO", "ASS_EVOLD_EVNEW.ESERCIZIO_OLD");
		sql.addSQLJoin("ELEMENTO_VOCE.TI_APPARTENENZA", "ASS_EVOLD_EVNEW.TI_APPARTENENZA_OLD");
		sql.addSQLJoin("ELEMENTO_VOCE.TI_GESTIONE", "ASS_EVOLD_EVNEW.TI_GESTIONE_OLD");
		sql.addSQLJoin("ELEMENTO_VOCE.CD_ELEMENTO_VOCE", "ASS_EVOLD_EVNEW.CD_ELEMENTO_VOCE_OLD");
		sql.addSQLClause(FindClause.AND, "ASS_EVOLD_EVNEW.ESERCIZIO_NEW", SQLBuilder.EQUALS, voceNewBulk.getEsercizio());
		sql.addSQLClause(FindClause.AND, "ASS_EVOLD_EVNEW.TI_APPARTENENZA_NEW", SQLBuilder.EQUALS, voceNewBulk.getTi_appartenenza());
		sql.addSQLClause(FindClause.AND, "ASS_EVOLD_EVNEW.TI_GESTIONE_NEW", SQLBuilder.EQUALS, voceNewBulk.getTi_gestione());
		sql.addSQLClause(FindClause.AND, "ASS_EVOLD_EVNEW.CD_ELEMENTO_VOCE_NEW", SQLBuilder.EQUALS, voceNewBulk.getCd_elemento_voce());
		return evnewHome.fetchAll(sql);
	}
}
