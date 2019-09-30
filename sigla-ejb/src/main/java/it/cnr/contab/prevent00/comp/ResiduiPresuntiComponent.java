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

package it.cnr.contab.prevent00.comp;

import it.cnr.contab.prevent00.bulk.*;
import it.cnr.contab.config00.pdcfin.bulk.*;
import it.cnr.jada.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.sql.*;
public class ResiduiPresuntiComponent extends CRUDComponent implements IResiduiPresuntiMgr
{
/**
 * ResiduiPresuntiComponent constructor comment.
 */
public ResiduiPresuntiComponent() {
	super();
}
/**
 * Ricerca voce_f_res_pres
 *
 * Pre-post-conditions:
 *
 * Nome: Ricerca
 * Pre:  L'utente esegue una ricerca 
 * Post: Il sistema restitusce il SQLBuilder con le clausole specificate dall'utente e in aggiunta con l'esercizio
 *       di scrivania
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param   clauses 	le clausole speicificate dall'utene
 * @param	bulk		la Voce_f_res_pres da ricercare
 *
 * @return  il SQLBuilder con tutte le clausole
 */	

public Query select(UserContext userContext,it.cnr.jada.persistency.sql.CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException
{
	SQLBuilder sql = (SQLBuilder)super.select( userContext, clauses, bulk );
	Voce_f_res_presBulk voceResPres = (Voce_f_res_presBulk) bulk;

	sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, voceResPres.getEsercizio());

	return sql;
}
/**
 * Ricerca voce
 *
 * Pre-post-conditions:
 *
 * Nome: Ricerca Voci
 * Pre:  L'utente richiede la ricerca delle voci 
 * Post: Il sistema restituisce le voci della tabella VOCE_F . 
 *		 Tali voci sono dei Mastrini con tipo appartenenza CNR
 *
 * @param	userContext			lo UserContext che ha generato la richiesta
s *
 * @return  il SQLBuilder con tutte le clausole
 */	


public SQLBuilder selectVoceByClause(UserContext aUC, Voce_f_res_presBulk voceResPres, Voce_fBulk aVoce, CompoundFindClause clauses) throws ComponentException 
{
	Voce_fHome home = (Voce_fHome)getHome(aUC, Voce_fBulk.class);
	SQLBuilder sql = home.createSQLBuilder();

	sql.addSQLClause("AND","ESERCIZIO", sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC));	
	sql.addSQLClause("AND","TI_APPARTENENZA", sql.EQUALS, Elemento_voceHome.APPARTENENZA_CNR);
	sql.addSQLClause("AND","FL_MASTRINO", sql.EQUALS, "Y");
	
	sql.addClause(clauses);

	return sql;
}
}
