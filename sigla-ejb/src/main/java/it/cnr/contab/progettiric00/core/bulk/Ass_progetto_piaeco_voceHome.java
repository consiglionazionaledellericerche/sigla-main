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

package it.cnr.contab.progettiric00.core.bulk;

import java.util.List;
import java.util.Optional;

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_cnrHome;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class Ass_progetto_piaeco_voceHome extends BulkHome {

	public Ass_progetto_piaeco_voceHome(java.sql.Connection conn) {
		super(Ass_progetto_piaeco_voceBulk.class,conn);
	}
	
	public Ass_progetto_piaeco_voceHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(Ass_progetto_piaeco_voceBulk.class,conn,persistentCache);
	}

	public java.util.Collection<Ass_progetto_piaeco_voceBulk> findAssProgettoPiaecoVoceList( java.lang.Integer pgProgetto, java.lang.String cdUnitaOrganizzativa, java.lang.String cdVocePiano, java.lang.Integer esercizioPiano ) throws PersistencyException {
		SQLBuilder sql = this.createSQLBuilder();
		sql.addClause(FindClause.AND, "pg_progetto", SQLBuilder.EQUALS, pgProgetto);
		sql.addClause(FindClause.AND, "cd_unita_organizzativa", SQLBuilder.EQUALS, cdUnitaOrganizzativa);
		sql.addClause(FindClause.AND, "cd_voce_piano", SQLBuilder.EQUALS, cdVocePiano);
		sql.addClause(FindClause.AND, "esercizio_piano", SQLBuilder.EQUALS, esercizioPiano);
		return this.fetchAll(sql);
	}
	
	public SQLBuilder selectAssProgettoPiaecoVoceList( java.lang.Integer esercizio, java.lang.Integer pgProgetto, Integer idClassificazione ) throws PersistencyException {
    	SQLBuilder sql = this.createSQLBuilder();

    	sql.addSQLClause(FindClause.AND, "ASS_PROGETTO_PIAECO_VOCE.ESERCIZIO_VOCE", SQLBuilder.EQUALS, esercizio);
    	sql.addSQLClause(FindClause.AND, "ASS_PROGETTO_PIAECO_VOCE.PG_PROGETTO", SQLBuilder.EQUALS, pgProgetto);
    	
    	sql.addTableToHeader("ELEMENTO_VOCE");
    
		sql.addSQLJoin("ASS_PROGETTO_PIAECO_VOCE.ESERCIZIO_VOCE", "ELEMENTO_VOCE.ESERCIZIO");
		sql.addSQLJoin("ASS_PROGETTO_PIAECO_VOCE.TI_APPARTENENZA", "ELEMENTO_VOCE.TI_APPARTENENZA");
		sql.addSQLJoin("ASS_PROGETTO_PIAECO_VOCE.TI_GESTIONE", "ELEMENTO_VOCE.TI_GESTIONE");
		sql.addSQLJoin("ASS_PROGETTO_PIAECO_VOCE.CD_ELEMENTO_VOCE", "ELEMENTO_VOCE.CD_ELEMENTO_VOCE");

		sql.addTableToHeader("PARAMETRI_LIVELLI");
		sql.addSQLJoin("ELEMENTO_VOCE.ESERCIZIO", "PARAMETRI_LIVELLI.ESERCIZIO");
		sql.addTableToHeader("V_CLASSIFICAZIONE_VOCI_ALL");
		sql.addSQLJoin("ELEMENTO_VOCE.ID_CLASSIFICAZIONE", "V_CLASSIFICAZIONE_VOCI_ALL.ID_CLASSIFICAZIONE");
		sql.addSQLJoin("V_CLASSIFICAZIONE_VOCI_ALL.NR_LIVELLO", "PARAMETRI_LIVELLI.LIVELLI_SPESA");

		if (Optional.ofNullable(idClassificazione).isPresent()) {
	    	Parametri_cnrHome parCnrhome = (Parametri_cnrHome)getHomeCache().getHome(Parametri_cnrBulk.class);
	    	Parametri_cnrBulk parCnrBulk = (Parametri_cnrBulk)parCnrhome.findByPrimaryKey(new Parametri_cnrBulk(esercizio));
	    	sql.addSQLClause(FindClause.AND, "V_CLASSIFICAZIONE_VOCI_ALL.ID_LIV"+parCnrBulk.getLivello_pdg_decis_spe(), SQLBuilder.EQUALS, idClassificazione);
		}
		return sql;
	}
}
