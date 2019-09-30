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

package it.cnr.contab.missioni00.tabrif.bulk;

import it.cnr.contab.anagraf00.tabrif.bulk.*;
import java.sql.*;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.config00.esercizio.bulk.EsercizioHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Missione_diariaHome extends BulkHome {
public Missione_diariaHome(java.sql.Connection conn) {
	super(Missione_diariaBulk.class,conn);
}
public Missione_diariaHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Missione_diariaBulk.class,conn,persistentCache);
}
/**
 * Vengono aggiunte alla query le clausole realtive alla chiave primaria 
 * tranne la data inizio validita
 * 
*/
private void addPrimaryClauses(SQLBuilder sql, Missione_diariaBulk diaria){

	sql.addClause("AND","cd_gruppo_inquadramento", sql.EQUALS, diaria.getCd_gruppo_inquadramento());
	sql.addClause("AND","pg_nazione",              sql.EQUALS, diaria.getPg_nazione());
}
/**
 * Viene ricercata la massima Data Inizio Validita presente in tabella per l'oggetto avente la
 * stessa chiave dell'oggetto passato come parametro e data inizio validita diversa da <dataInizio>
 * Viene restituita la data trovata, NULL altrimenti
 * 
*/
private Timestamp findDataInizioPeriodoPrecedente(Missione_diariaBulk diaria, Timestamp dataInizio) throws PersistencyException, IntrospectionException{

	SQLBuilder sql = createSQLBuilder();

	sql.setHeader("SELECT MAX(DT_INIZIO_VALIDITA) AS DT_INIZIO_VALIDITA");
	addPrimaryClauses(sql, diaria);
	sql.addClause("AND", "dt_inizio_validita", sql.NOT_EQUALS, dataInizio);

	Broker broker = createBroker(sql);
	Object value = null;
	if (broker.next()) {
		value = broker.fetchPropertyValue("dt_inizio_validita",getIntrospector().getPropertyType(getPersistentClass(),"dt_inizio_validita"));
		broker.close();
	}
	return (Timestamp)value;
}
/**
 * Esegue la Query per cercare i profili (Tab GRUPPO_INQUADRAMENTO)
 *
*/
public java.util.List findGruppiInquadramento(Missione_diariaBulk aDiaria, Gruppo_inquadramentoHome home,Gruppo_inquadramentoBulk clause) throws PersistencyException,IntrospectionException {
	return home.findGruppiInquadramento();
}
/**
 * Viene ricercato l'oggetto bulk avente la stessa chiave dell'oggetto passato come parametro
 * e con data fine validita uguale alla data inizio del parametro meno un giorno
 * Viene restituito l'oggetto trovato, NULL altrimenti
 * 
*/
public Missione_diariaBulk findIntervalloPrecedente(Missione_diariaBulk diaria, boolean lock) throws PersistencyException, IntrospectionException, OutdatedResourceException, BusyResourceException{

	Timestamp data = findDataInizioPeriodoPrecedente(diaria, diaria.getDt_inizio_validita());
	if (data==null)
		return null;
	SQLBuilder sql = createSQLBuilder();
	addPrimaryClauses(sql, diaria);
	sql.addClause("AND", "dt_inizio_validita", sql.EQUALS, data);

	Missione_diariaBulk obj = null;
	if (lock)
		obj = (Missione_diariaBulk)fetchAndLock(sql);
	else{
		Broker broker = createBroker(sql);
		if (broker.next())
			obj = (Missione_diariaBulk)fetch(broker);
		broker.close();
	}
	return obj;
}
/**
 * Per poter CREARE un nuovo record :
 *
 * 1. Se non ho altri record in tabella con la stessa chiave, procedo con l'nserimento senza ulteriori controlli
 * 2. Se ho altri record con stessa chiave:
 *
 *		2.1. la data di inizio validita' del nuovo record deve essere maggiore di quella del record piu' recente
 *		2.2. la data di inizio validita' del nuovo record deve essere maggiore della data fine validita del record piu' recente
 *			==> la data di fine validita' del record piu' recente viene aggiornata con la data di inizio validita' del nuovo record meno un giorno
 *			==> la data di fine validita' del nuovo record viene posta uguale alla data infinto (31/12/2200)
 * 		Altrimenti viene emesso il messaggio:
 * 			"La Data Inizio Validita non è valida. Deve essere maggiore di xxx"
*/
public void validaPeriodoInCreazione(UserContext userContext, Missione_diariaBulk corrente) throws PersistencyException, IntrospectionException, OutdatedResourceException, BusyResourceException, it.cnr.jada.comp.ApplicationException{

	Missione_diariaBulk precedente = findIntervalloPrecedente(corrente, true);
	if(precedente!=null){
		if (precedente.getDataFineValidita()!=null && corrente.getDt_inizio_validita().compareTo(precedente.getDataFineValidita())<=0){
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
			throw new it.cnr.jada.comp.ApplicationException("La Data Inizio Validita non è valida. Deve essere maggiore di " + sdf.format(precedente.getDataFineValidita()));
		}else if(corrente.getDt_inizio_validita().compareTo(precedente.getDt_inizio_validita())<=0){
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
			throw new it.cnr.jada.comp.ApplicationException("La Data Inizio Validita non è valida. Deve essere maggiore di " + sdf.format(precedente.getDt_inizio_validita()));
		}
		precedente.setDt_fine_validita(CompensoBulk.decrementaData(corrente.getDt_inizio_validita()));
		update(precedente, userContext);
	}
	corrente.setDt_fine_validita(EsercizioHome.DATA_INFINITO);
}
}
