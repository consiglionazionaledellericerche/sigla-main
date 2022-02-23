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

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.progettiric00.consultazioni.bulk.V_saldi_piano_econom_progcdrBulk;
import it.cnr.contab.progettiric00.consultazioni.bulk.V_saldi_piano_econom_progcdrHome;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class ConsProgEcoVociGaeComponent extends CRUDComponent {

	public ConsProgEcoVociGaeComponent() {
		super();
	}

	public SQLBuilder selectFindProgettoForPrintByClause(UserContext userContext, V_saldi_piano_econom_progcdrBulk stampa, ProgettoBulk progetto, CompoundFindClause clauses) throws ComponentException, PersistencyException {
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

	public it.cnr.jada.util.RemoteIterator findProgetti(UserContext userContext, V_saldi_piano_econom_progcdrBulk bulk) throws ComponentException {
		try {
			V_saldi_piano_econom_progcdrHome home = (V_saldi_piano_econom_progcdrHome)getHome(userContext, V_saldi_piano_econom_progcdrBulk.class);

			Unita_organizzativaBulk uoScrivania = (Unita_organizzativaBulk) getHome(userContext,Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(userContext)));

			boolean isSintetica = V_saldi_piano_econom_progcdrBulk.SINTETICA.equals(bulk.getTipoStampa());

			SQLBuilder sql = home.createSQLBuilder();
			sql.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext) );
			sql.addClause(FindClause.AND, "pg_progetto", SQLBuilder.EQUALS, bulk.getPg_progetto());
			if (!uoScrivania.isUoEnte() && !uoScrivania.getCd_unita_padre().equals(bulk.getFindProgettoForPrint().getUnita_organizzativa().getCd_unita_padre()))
				sql.addSQLClause(FindClause.AND, "SUBSTR(CD_CENTRO_RESPONSABILITA,1,7) = " +
						"'" + uoScrivania.getCd_unita_organizzativa()+"'");

			sql.resetColumns();
			sql.addColumn("esercizio");
			sql.addColumn("esercizio_piano");
			sql.addColumn("cd_voce_piano");
			sql.addColumn("im_spesa_finanziato");
			sql.addColumn("im_spesa_cofinanziato");
			sql.addColumn("pg_progetto");
			sql.addColumn("cd_progetto");
			sql.addColumn("ds_progetto");
			sql.addColumn("cd_centro_responsabilita");
			sql.addColumn("ds_cdr");
			if (isSintetica){
				sql.addColumn("sum(NVL(assestato_fin,0))","assestato_fin");
				sql.addColumn("sum(NVL(assestato_cofin,0))","assestato_cofin");
				sql.addColumn("sum(NVL(impacc_cofin,0))","impacc_cofin");
				sql.addColumn("sum(NVL(impacc_fin,0))","impacc_fin");
				sql.addColumn("sum(NVL(manris_cofin,0))","manris_cofin");
				sql.addColumn("sum(NVL(manris_fin,0))","manris_fin");

				sql.addSQLGroupBy("esercizio");
				sql.addSQLGroupBy("esercizio_piano");
				sql.addSQLGroupBy("cd_voce_piano");
				sql.addSQLGroupBy("im_spesa_finanziato");
				sql.addSQLGroupBy("im_spesa_cofinanziato");
				sql.addSQLGroupBy("pg_progetto");
				sql.addSQLGroupBy("cd_progetto");
				sql.addSQLGroupBy("ds_progetto");
				sql.addSQLGroupBy("cd_centro_responsabilita");
				sql.addSQLGroupBy("ds_cdr");
			} else {
				sql.addColumn("cd_elemento_voce");
				sql.addColumn("ds_elemento_voce");
				sql.addColumn("cd_linea_attivita");
				sql.addColumn("NVL(assestato_fin, 0)","assestato_fin");
				sql.addColumn("NVL(assestato_cofin, 0)", "assestato_cofin");
				sql.addColumn("NVL(impacc_cofin,0)","impacc_cofin");
				sql.addColumn("NVL(impacc_fin,0)","impacc_fin");
				sql.addColumn("NVL(manris_cofin,0)","manris_cofin");
				sql.addColumn("NVL(manris_fin,0)","manris_fin");
			}

			sql.addOrderBy("cd_voce_piano");
			sql.addOrderBy("esercizio_piano");
			sql.addOrderBy("CD_CENTRO_RESPONSABILITA");

			if (!isSintetica){
				sql.addOrderBy("cd_elemento_voce");
				sql.addOrderBy("cd_linea_attivita");
			}
			return  iterator(userContext, sql, V_saldi_piano_econom_progcdrBulk.class,null);
		} catch (Exception e) {
			throw handleException(e);
		}
	}
}