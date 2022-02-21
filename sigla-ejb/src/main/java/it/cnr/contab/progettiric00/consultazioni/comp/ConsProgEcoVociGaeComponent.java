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

package it.cnr.contab.progettiric00.consultazioni.comp;

import it.cnr.contab.bollo00.bulk.Atto_bolloBulk;
import it.cnr.contab.bollo00.bulk.Atto_bolloHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.progettiric00.consultazioni.bulk.ConsProgettiEcoVociGaeBulk;
import it.cnr.contab.progettiric00.consultazioni.bulk.ConsProgettiEcoVociGaeHome;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.rmi.RemoteException;
import java.util.Optional;

public class ConsProgEcoVociGaeComponent extends CRUDComponent {

	public ConsProgEcoVociGaeComponent() {
		super();
	}

	public SQLBuilder selectFindProgettoForPrintByClause(UserContext userContext, ConsProgettiEcoVociGaeBulk stampa, ProgettoBulk progetto, CompoundFindClause clauses) throws ComponentException, PersistencyException {
		ProgettoHome progettohome = (ProgettoHome) getHome(userContext, ProgettoBulk.class, "V_PROGETTO_PADRE");
		SQLBuilder sql = progettohome.createSQLBuilder();
		sql.addClause(clauses);
		sql.addClause(stampa.buildFindClauses(new Boolean(true)));
		sql.addSQLClause(FindClause.AND, "V_PROGETTO_PADRE.ESERCIZIO", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext));
		sql.addSQLClause(FindClause.AND, "V_PROGETTO_PADRE.LIVELLO", SQLBuilder.EQUALS, ProgettoBulk.LIVELLO_PROGETTO_SECONDO);
		sql.addClause(FindClause.AND, "tipo_fase", SQLBuilder.EQUALS, ProgettoBulk.TIPO_FASE_NON_DEFINITA);

		// Se uo 999.000 in scrivania: visualizza tutti i progetti
		Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome(userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
		if (!((CNRUserContext) userContext).getCd_unita_organizzativa().equals(ente.getCd_unita_organizzativa())) {
			try {
				sql.addSQLExistsClause(FindClause.AND, progettohome.abilitazioniCommesse(userContext));
			} catch (Exception e) {
				throw handleException(e);
			}
		}
		sql.addOrderBy("cd_progetto");
		return sql;
	}

	public it.cnr.jada.util.RemoteIterator findProgetti(UserContext userContext, ConsProgettiEcoVociGaeBulk bulk) throws ComponentException {
		try {
			ConsProgettiEcoVociGaeHome home = (ConsProgettiEcoVociGaeHome)getHome(userContext, ConsProgettiEcoVociGaeBulk.class);

			Unita_organizzativaBulk uoScrivania = (Unita_organizzativaBulk) getHome(userContext,Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(userContext)));

			boolean isSintetica = ConsProgettiEcoVociGaeBulk.SINTETICA.equals(bulk.getTipoStampa());

			SQLBuilder sql = home.createSQLBuilder();
			sql.addClause(FindClause.AND, "pg_progetto", SQLBuilder.EQUALS, bulk.getPg_progetto());
			if (!uoScrivania.isUoEnte() && !uoScrivania.getCd_unita_padre().equals(bulk.getFindProgettoForPrint().getUnita_organizzativa().getCd_unita_padre()))
				sql.addSQLClause(FindClause.AND, "SUBSTR(V_SALDI_GAE_VOCE_PROGETTO.CD_CENTRO_RESPONSABILITA,1,7) = " +
						"'" + uoScrivania.getCd_unita_organizzativa()+"'");

			sql.resetColumns();
			sql.addColumn("PROGETTO_PIANO_ECONOMICO.cd_voce_piano","cd_voce_piano");
			sql.addColumn("PROGETTO_PIANO_ECONOMICO.esercizio_piano","esercizio_piano");
			sql.addColumn("PROGETTO_PIANO_ECONOMICO.im_spesa_finanziato","im_spesa_finanziato");
			sql.addColumn("PROGETTO_PIANO_ECONOMICO.im_spesa_cofinanziato","im_spesa_cofinanziato");
			if (isSintetica){
				sql.addColumn("PROGETTO_PIANO_ECONOMICO.cd_voce_piano","cd_elemento_voce");
				sql.addColumn("PROGETTO_PIANO_ECONOMICO.cd_voce_piano","ds_elemento_voce");
				sql.addColumn("PROGETTO_PIANO_ECONOMICO.cd_voce_piano","cd_linea_attivita");
			} else {
				sql.addColumn("ELEMENTO_VOCE.CD_ELEMENTO_VOCE","cd_elemento_voce");
				sql.addColumn("ELEMENTO_VOCE.DS_ELEMENTO_VOCE","ds_elemento_voce");
				sql.addColumn("V_SALDI_GAE_VOCE_PROGETTO.cd_linea_attivita","cd_linea_attivita");
			}
			sql.addColumn("PROGETTO_SIP.pg_progetto");
			sql.addColumn("PROGETTO_SIP.CD_PROGETTO");
			sql.addColumn("PROGETTO_SIP.DS_PROGETTO");
			sql.addColumn("CDR.CD_CENTRO_RESPONSABILITA", "cd_centro_responsabilita");
			sql.addColumn("CDR.DS_CDR", "ds_cdr");
			if (isSintetica){
				sql.addColumn("sum(NVL(V_SALDI_GAE_VOCE_PROGETTO.stanziamento_fin,0) + NVL(V_SALDI_GAE_VOCE_PROGETTO.VARIAPIU_FIN,0) - NVL(V_SALDI_GAE_VOCE_PROGETTO.VARIAMENO_FIN,0))","assestato_fin");
				sql.addColumn("sum(NVL(V_SALDI_GAE_VOCE_PROGETTO.stanziamento_cofin,0) + NVL(V_SALDI_GAE_VOCE_PROGETTO.VARIAPIU_coFIN,0) - NVL(V_SALDI_GAE_VOCE_PROGETTO.VARIAMENO_coFIN,0))","assestato_cofin");
				sql.addColumn("sum(NVL(V_SALDI_GAE_VOCE_PROGETTO.impacc_cofin,0))","impacc_cofin");
				sql.addColumn("sum(NVL(V_SALDI_GAE_VOCE_PROGETTO.impacc_fin,0))","impacc_fin");
				sql.addColumn("sum(NVL(V_SALDI_GAE_VOCE_PROGETTO.manris_cofin,0))","manris_cofin");
				sql.addColumn("sum(NVL(V_SALDI_GAE_VOCE_PROGETTO.manris_fin,0))","manris_fin");
			} else {
				sql.addColumn("NVL(V_SALDI_GAE_VOCE_PROGETTO.stanziamento_fin,0) + NVL(V_SALDI_GAE_VOCE_PROGETTO.VARIAPIU_FIN,0) - NVL(V_SALDI_GAE_VOCE_PROGETTO.VARIAMENO_FIN,0)","assestato_fin");
				sql.addColumn("NVL(V_SALDI_GAE_VOCE_PROGETTO.stanziamento_cofin,0) + NVL(V_SALDI_GAE_VOCE_PROGETTO.VARIAPIU_coFIN,0) - NVL(V_SALDI_GAE_VOCE_PROGETTO.VARIAMENO_coFIN,0)","assestato_cofin");
				sql.addColumn("NVL(V_SALDI_GAE_VOCE_PROGETTO.impacc_cofin,0)","impacc_cofin");
				sql.addColumn("NVL(V_SALDI_GAE_VOCE_PROGETTO.impacc_fin,0)","impacc_fin");
				sql.addColumn("NVL(V_SALDI_GAE_VOCE_PROGETTO.manris_cofin,0)","manris_cofin");
				sql.addColumn("NVL(V_SALDI_GAE_VOCE_PROGETTO.manris_fin,0)","manris_fin");
			}

			sql.addTableToHeader("PROGETTO_PIANO_ECONOMICO");
			sql.addSQLJoin("PROGETTO_PIANO_ECONOMICO.pg_progetto","PROGETTO_SIP.pg_progetto");


			sql.addTableToHeader("ASS_PROGETTO_PIAECO_VOCE");
			sql.addSQLJoin("PROGETTO_PIANO_ECONOMICO.PG_PROGETTO","ASS_PROGETTO_PIAECO_VOCE.PG_PROGETTO(+)");
			sql.addSQLJoin("PROGETTO_PIANO_ECONOMICO.ESERCIZIO_PIANO","ASS_PROGETTO_PIAECO_VOCE.ESERCIZIO_PIANO(+)");
			sql.addSQLJoin("PROGETTO_PIANO_ECONOMICO.CD_VOCE_PIANO","ASS_PROGETTO_PIAECO_VOCE.CD_VOCE_PIANO(+)");

			sql.addTableToHeader("ELEMENTO_VOCE");
			sql.addSQLJoin("ASS_PROGETTO_PIAECO_VOCE.ESERCIZIO_VOCE", "ELEMENTO_VOCE.ESERCIZIO(+)");
			sql.addSQLJoin("ASS_PROGETTO_PIAECO_VOCE.TI_APPARTENENZA", "ELEMENTO_VOCE.TI_APPARTENENZA(+)");
			sql.addSQLJoin("ASS_PROGETTO_PIAECO_VOCE.TI_GESTIONE", "ELEMENTO_VOCE.TI_GESTIONE(+)");
			sql.addSQLJoin("ASS_PROGETTO_PIAECO_VOCE.CD_ELEMENTO_VOCE", "ELEMENTO_VOCE.CD_ELEMENTO_VOCE(+)");

			sql.addTableToHeader("V_SALDI_GAE_VOCE_PROGETTO");
			if (isSintetica){
				sql.addSQLJoin("ASS_PROGETTO_PIAECO_VOCE.ESERCIZIO_VOCE", "V_SALDI_GAE_VOCE_PROGETTO.ESERCIZIO(+)");
				sql.addSQLJoin("ASS_PROGETTO_PIAECO_VOCE.TI_APPARTENENZA", "V_SALDI_GAE_VOCE_PROGETTO.TI_APPARTENENZA(+)");
				sql.addSQLJoin("ASS_PROGETTO_PIAECO_VOCE.TI_GESTIONE", "V_SALDI_GAE_VOCE_PROGETTO.TI_GESTIONE(+)");
				sql.addSQLJoin("ASS_PROGETTO_PIAECO_VOCE.CD_ELEMENTO_VOCE", "V_SALDI_GAE_VOCE_PROGETTO.CD_ELEMENTO_VOCE(+)");
				sql.addSQLJoin("ASS_PROGETTO_PIAECO_VOCE.PG_PROGETTO", "V_SALDI_GAE_VOCE_PROGETTO.PG_PROGETTO(+)");
			} else {
				sql.addSQLJoin("ASS_PROGETTO_PIAECO_VOCE.ESERCIZIO_VOCE", "V_SALDI_GAE_VOCE_PROGETTO.ESERCIZIO(+)");
				sql.addSQLJoin("ASS_PROGETTO_PIAECO_VOCE.TI_APPARTENENZA", "V_SALDI_GAE_VOCE_PROGETTO.TI_APPARTENENZA(+)");
				sql.addSQLJoin("ASS_PROGETTO_PIAECO_VOCE.TI_GESTIONE", "V_SALDI_GAE_VOCE_PROGETTO.TI_GESTIONE(+)");
				sql.addSQLJoin("ASS_PROGETTO_PIAECO_VOCE.CD_ELEMENTO_VOCE", "V_SALDI_GAE_VOCE_PROGETTO.CD_ELEMENTO_VOCE(+)");
				sql.addSQLJoin("ASS_PROGETTO_PIAECO_VOCE.PG_PROGETTO", "V_SALDI_GAE_VOCE_PROGETTO.PG_PROGETTO(+)");

			}

			sql.addTableToHeader("CDR");
			if (isSintetica){
				sql.addSQLJoin("V_SALDI_GAE_VOCE_PROGETTO.CD_CENTRO_RESPONSABILITA","CDR.CD_CENTRO_RESPONSABILITA(+)");
			} else {
				sql.addSQLJoin("V_SALDI_GAE_VOCE_PROGETTO.CD_CENTRO_RESPONSABILITA","CDR.CD_CENTRO_RESPONSABILITA(+)");
			}

			sql.addSQLClause("AND", "PROGETTO_SIP.ESERCIZIO", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext) );
			sql.addSQLClause("AND", "PROGETTO_SIP.TIPO_FASE", SQLBuilder.EQUALS, ProgettoBulk.TIPO_FASE_GESTIONE );

			if (isSintetica){
				sql.addSQLGroupBy("PROGETTO_PIANO_ECONOMICO.im_spesa_finanziato");
				sql.addSQLGroupBy("PROGETTO_PIANO_ECONOMICO.im_spesa_cofinanziato");
				sql.addSQLGroupBy("PROGETTO_PIANO_ECONOMICO.cd_voce_piano");
				sql.addSQLGroupBy("PROGETTO_PIANO_ECONOMICO.esercizio_piano");
				sql.addSQLGroupBy("PROGETTO_PIANO_ECONOMICO.cd_voce_piano");
				sql.addSQLGroupBy("PROGETTO_PIANO_ECONOMICO.cd_voce_piano");
				sql.addSQLGroupBy("PROGETTO_PIANO_ECONOMICO.cd_voce_piano");
				sql.addSQLGroupBy("PROGETTO_SIP.pg_progetto");
				sql.addSQLGroupBy("PROGETTO_SIP.cd_progetto");
				sql.addSQLGroupBy("PROGETTO_SIP.ds_progetto");
				sql.addSQLGroupBy("CDR.cd_centro_responsabilita");
				sql.addSQLGroupBy("CDR.ds_cdr");
			}
			sql.addOrderBy("PROGETTO_PIANO_ECONOMICO.cd_voce_piano");
			sql.addOrderBy("PROGETTO_PIANO_ECONOMICO.esercizio_piano");
			sql.addOrderBy("CDR.CD_CENTRO_RESPONSABILITA");
			if (!isSintetica){
				sql.addOrderBy("ELEMENTO_VOCE.cd_elemento_voce");
				sql.addOrderBy("ELEMENTO_VOCE.cd_elemento_voce");
				sql.addOrderBy("V_SALDI_GAE_VOCE_PROGETTO.cd_linea_attivita");
			}
			return  iterator(userContext, sql, ConsProgettiEcoVociGaeBulk.class,null);
		} catch (Exception e) {
			throw handleException(e);
		}
	}
}