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

package it.cnr.contab.compensi00.tabrif.bulk;

//import it.cnr.contab.anagraf00.core.bulk.Cnr_anadipBulk;
//import it.cnr.contab.anagraf00.core.bulk.RapportoBulk;
//import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.*;
import it.cnr.contab.compensi00.docs.bulk.*;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.bulk.Configurazione_cnrHome;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

import java.util.Optional;

public class Tipo_trattamentoHome extends BulkHome {
public Tipo_trattamentoHome(java.sql.Connection conn) {
	super(Tipo_trattamentoBulk.class,conn);
}
public Tipo_trattamentoHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Tipo_trattamentoBulk.class,conn,persistentCache);
}
/**
 * Inserisce la clausola di validità.
 * I record vengono filtrati secondo la seguente clausola
 *	DATA_INIZIO_VALIDITA <= data <= DATA_FINE_VALIDITA
 *
 * @param sql SQL statement a cui vengono aggiunte le clausole di validita
 * @param data Data di validita del tipo trattamento
 *
**/
public 	void addClauseValidita(SQLBuilder sql, java.sql.Timestamp data){

	sql.addClause("AND","dt_ini_validita",sql.LESS_EQUALS,data);
	sql.addClause("AND","dt_fin_validita",sql.GREATER_EQUALS,data);
}
private void addSQLTipoTrattamento(SQLBuilder sql, Filtro_trattamentoBulk filtro) {

	sql.setDistinctClause(true);
	sql.addSQLClause("AND","TIPO_TRATTAMENTO.CD_TRATTAMENTO",sql.EQUALS, filtro.getCdTipoTrattamento());
	sql.addSQLClause("AND","TIPO_TRATTAMENTO.TI_ANAGRAFICO",sql.EQUALS, filtro.getTipoAnagrafico());
	addClauseValidita(sql, filtro.getDataValidita());
	sql.addClause("AND","fl_default_conguaglio",sql.EQUALS, filtro.getFlDefaultCongualio());
	sql.addClause("AND","fl_senza_calcoli",sql.EQUALS, filtro.getFlSenzaCalcoli());
	sql.addClause("AND","fl_diaria",sql.EQUALS, filtro.getFlDiaria());
	sql.addClause("AND","ti_commerciale",sql.EQUALS, filtro.getTiCommerciale());
	sql.addClause("AND","fl_tassazione_separata",sql.EQUALS, filtro.getFlTassazioneSeparata());
	sql.addClause("AND","fl_agevolazioni_cervelli",sql.EQUALS, filtro.getFlAgevolazioniCervelli());
	sql.addClause("AND","fl_incarico",sql.EQUALS, filtro.getFlIncarico());
	sql.addClause("AND","fl_tipo_prestazione_obbl",sql.EQUALS, filtro.getFlTipoPrestazioneObbl());
	sql.addClause("AND","fl_agevolazioni_rientro_lav",sql.EQUALS, filtro.getFlAgevolazioniRientroLav());
	sql.addClause("AND","fl_solo_inail_ente",sql.EQUALS, filtro.getFlSoloInailEnte());
	sql.addClause("AND","fl_split_payment",sql.EQUALS, filtro.getFlSplitPayment());
	if (filtro.getCdTipoRapporto() != null && filtro.getCdTipoRapporto().equals("DIP"))
	{
		sql.addClause("AND (","fl_anno_prec",sql.ISNULL,null);
		sql.addClause("OR","fl_anno_prec",sql.EQUALS, filtro.getFlAnnoPrec());
		sql.closeParenthesis();

		sql.addClause("AND (","tipo_rapp_impiego",sql.ISNULL,null);
		sql.addClause("OR","tipo_rapp_impiego",sql.EQUALS, filtro.getTipoRappImpiego());
		sql.closeParenthesis();
	}
	sql.addOrderBy("TIPO_TRATTAMENTO.DS_TI_TRATTAMENTO");
}
public java.util.List caricaIntervalli(Tipo_trattamentoBulk tratt) throws PersistencyException{

	SQLBuilder sql = createSQLBuilder();
	sql.addClause("AND", "cd_trattamento",sql.EQUALS,tratt.getCd_trattamento());
	sql.addOrderBy("dt_ini_validita");
	return fetchAll(sql);
}
public Tipo_trattamentoBulk findIntervallo(Tipo_trattamentoBulk tratt) throws PersistencyException{

	SQLBuilder sql = createSQLBuilder();
	sql.addClause("AND","cd_trattamento",sql.EQUALS,tratt.getCd_trattamento());
	addClauseValidita(sql, tratt.getDt_ini_validita());

	Tipo_trattamentoBulk corrente = null;
	Broker broker = createBroker(sql);
	if (broker.next())
		corrente = (Tipo_trattamentoBulk)fetch(broker);
	broker.close();

	return corrente;
}
/**
 *  Ritorna tutti i tipi trattamento compatibili con il filtro impostato
 *
 * @param filtro
 * @return Collezione dei Tipi_trattamentoBulk validi
**/
public java.util.Collection findTipiTrattamento(Filtro_trattamentoBulk filtro) throws PersistencyException{

	SQLBuilder sql = selectTipoTrattamento(filtro);
	return fetchAll(sql);
}
/**
  * Ritorna il tipo trattamento che verifica le condizione inserite nel filtro
  *
  * @param filtro		Filtro per la ricerca del tipo trattamento
  * @return il Tipo_trattamentoBulk corrispondente alle clausole impostate nel filtro
  *
**/
public Tipo_trattamentoBulk findTipoTrattamentoValido(Filtro_trattamentoBulk filtro) throws PersistencyException{

	SQLBuilder sql = selectTipoTrattamentoValido(filtro);

	Tipo_trattamentoBulk tratt = null;
	Broker broker = createBroker(sql);
	if (broker.next())
		tratt = (Tipo_trattamentoBulk)fetch(broker);
	broker.close();

	return tratt;
}
/**
  * Ritorna TRUE se esiste il tipo trattamento che verifica
  * le condizione inserite nel filtro
  *
  * @param filtro		Filtro per la ricerca del tipo trattamento
  * @return TRUE se esiste il tipo trattamento, FALSE altrimenti
  *
**/
public boolean isTipoTrattamentoValido(Filtro_trattamentoBulk filtro) throws java.sql.SQLException, PersistencyException{

	SQLBuilder sql = selectTipoTrattamento(filtro);
	return sql.executeExistsQuery(getConnection());
}
/**
  * Ritorna l'SQL statement per la ricerca di un tipo trattamento
  * con le seguenti clausole:
  *
  * @param filtro	<true> se associato a missione, <false> altrimenti
  * @return L'SQL statement relativo
  *
**/
private SQLBuilder selectTipoTrattamento(Filtro_trattamentoBulk filtro) {

	SQLBuilder sql = createSQLBuilder();
	sql.addTableToHeader("ASS_TI_RAPP_TI_TRATT");
	sql.addSQLJoin("ASS_TI_RAPP_TI_TRATT.CD_TRATTAMENTO","TIPO_TRATTAMENTO.CD_TRATTAMENTO");
	sql.addSQLClause("AND","ASS_TI_RAPP_TI_TRATT.CD_TIPO_RAPPORTO",sql.EQUALS, filtro.getCdTipoRapporto());
	if (filtro.getCdTipoRapporto().equals("DIP") && Optional.ofNullable(filtro.getEntePrev()).isPresent()){
			sql.addTableToHeader("V_TIPO_TRATTAMENTO_TIPO_CORI");
			sql.addSQLJoin("V_TIPO_TRATTAMENTO_TIPO_CORI.CD_TRATTAMENTO","TIPO_TRATTAMENTO.CD_TRATTAMENTO");
			sql.addSQLClause("AND","((V_TIPO_TRATTAMENTO_TIPO_CORI.CD_ENTE_PREV_STI",sql.EQUALS, filtro.getEntePrev());
			sql.closeParenthesis();
			sql.addSQLClause("OR","(NOT EXISTS ( SELECT 1 FROM V_TIPO_TRATTAMENTO_TIPO_CORI V" +
														   " WHERE V.CD_TRATTAMENTO = V_TIPO_TRATTAMENTO_TIPO_CORI.CD_TRATTAMENTO" +
														   " AND V.CD_CLASSIFICAZIONE_CORI ", sql.EQUALS, "PR");
			sql.closeParenthesis();
			sql.closeParenthesis();
			sql.closeParenthesis();
	}
	addSQLTipoTrattamento(sql, filtro);
	if (filtro.getFlBonus()==null || !filtro.getFlBonus()){
		SQLBuilder sql_not = createSQLBuilder();
		sql_not.resetColumns();
		sql_not.addTableToHeader("TIPO_RAPPORTO");
		sql_not.addColumn("TIPO_TRATTAMENTO.CD_TRATTAMENTO");
		sql_not.addSQLJoin("TIPO_TRATTAMENTO.CD_TRATTAMENTO", sql.EQUALS,"TIPO_RAPPORTO.CD_TRATTAMENTO");
		sql_not.addSQLClause("AND","TIPO_TRATTAMENTO.TI_ANAGRAFICO",sql.EQUALS, filtro.getTipoAnagrafico());
		sql_not.addSQLClause("AND","TIPO_RAPPORTO.TI_DIPENDENTE_ALTRO",sql.EQUALS, filtro.getTipoAnagrafico());
		sql_not.addSQLClause("AND","TIPO_RAPPORTO.CD_TIPO_RAPPORTO",sql.EQUALS, filtro.getCdTipoRapporto());
		//sql.addSQLNotExistsClause("AND", sql_not);
		sql.addSQLNOTINClause("AND", "TIPO_TRATTAMENTO.CD_TRATTAMENTO",sql_not);
	}
	sql.addOrderBy("TIPO_TRATTAMENTO.DS_TI_TRATTAMENTO");
	return sql;
}
/**
  * Ritorna l'SQL statement per la ricerca di un tipo trattamento
  * con le seguenti clausole impostate nel filtro
  *
  * @param filtro
  * @return L'SQL statement relativo
  *
**/
private SQLBuilder selectTipoTrattamentoValido(Filtro_trattamentoBulk filtro) {

	SQLBuilder sql = createSQLBuilder();
	addSQLTipoTrattamento(sql, filtro);
	return sql;
}
public  Tipo_trattamentoBulk findTipoTrattamentoBonusValido(BonusBulk bonus) throws PersistencyException {
	Tipo_trattamentoBulk tratt = null;
	SQLBuilder sql = createSQLBuilder();
	sql.setDistinctClause(true);
	sql.addSQLClause("AND","TIPO_TRATTAMENTO.CD_TRATTAMENTO",sql.EQUALS, bonus.getCd_trattamento());
	sql.addSQLClause("AND","TIPO_TRATTAMENTO.TI_ANAGRAFICO",sql.EQUALS, bonus.getTipo_rapporto().getTi_dipendente_altro());
	addClauseValidita(sql, bonus.getDt_registrazione());
	Broker broker = createBroker(sql);
	if (broker.next())
		tratt = (Tipo_trattamentoBulk)fetch(broker);
	broker.close();

	return tratt;
}
	public  Tipo_trattamentoBulk findTipoTrattamentoCompenso() throws PersistencyException {
		String cdTipoTrattamento = ((Configurazione_cnrHome)getHomeCache().getHome(Configurazione_cnrBulk.class))
				.getConfigurazione(null, Configurazione_cnrBulk.PK_TRATTAMENTO_SPECIALE,Configurazione_cnrBulk.SK_TRATTAMENTO_STIPENDI).getVal01();

		SQLBuilder sql = createSQLBuilder();
		sql.addClause(FindClause.AND,"cd_trattamento",SQLBuilder.EQUALS, cdTipoTrattamento);

		sql.addTableToHeader("ASS_TI_RAPP_TI_TRATT");
		sql.addSQLJoin("ASS_TI_RAPP_TI_TRATT.CD_TRATTAMENTO","TIPO_TRATTAMENTO.CD_TRATTAMENTO");
		sql.addSQLClause(FindClause.AND,"ASS_TI_RAPP_TI_TRATT.CD_TIPO_RAPPORTO",SQLBuilder.EQUALS, "STI");
		final Optional<Tipo_trattamentoBulk> optional = fetchAll(sql).stream().findAny()
				.filter(Tipo_trattamentoBulk.class::isInstance)
				.map(Tipo_trattamentoBulk.class::cast);
		return optional.orElse(null);
	}
}
