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

package it.cnr.contab.progettiric00.consultazioni.bulk;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoComunicaDatiBulk;
import it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk;
import it.cnr.contab.pdg00.bulk.Stampa_pdg_etr_speVBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.*;

import java.sql.Connection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

public class ConsProgettiEcoVociGaeHome extends BulkHome {
	public ConsProgettiEcoVociGaeHome(Connection conn) {
		super(ConsProgettiEcoVociGaeBulk.class, conn);
	}
	public ConsProgettiEcoVociGaeHome(Connection conn, PersistentCache persistentCache) {
		super(ConsProgettiEcoVociGaeBulk.class, conn, persistentCache);
	}

	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause)
			throws PersistencyException
	{
		boolean isSintetica= false;
		final Stream<SimpleFindClause> clauses = Optional.ofNullable(compoundfindclause)
				.map(compoundFindClause -> compoundFindClause.getClauses())
				.map(enumeration -> Collections.list(enumeration).stream())
				.map(stream -> stream
						.filter(SimpleFindClause.class::isInstance)
						.map(SimpleFindClause.class::cast)
				).orElse(Stream.empty());
		if (clauses.filter(clause ->
				clause.getPropertyName() != null &&
						clause.getPropertyName().equals(ConsProgettiEcoVociGaeBulk.SINTETICA))
					.findAny().isPresent())
			isSintetica= true;

		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
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

		sql.addSQLClause("AND", "PROGETTO_SIP.ESERCIZIO", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(usercontext) );
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
		return sql;
	}
}