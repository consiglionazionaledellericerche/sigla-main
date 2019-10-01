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

import java.sql.*;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.config00.esercizio.bulk.EsercizioHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipologia_rischioHome extends BulkHome {
public Tipologia_rischioHome(java.sql.Connection conn) {
	super(Tipologia_rischioBulk.class,conn);
}
public Tipologia_rischioHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Tipologia_rischioBulk.class,conn,persistentCache);
}
/**
 * Vengono aggiunte alla query le clausole realtive alla chiave primaria 
 * tranne la data inizio validita
 * 
*/
private void addPrimaryClauses(SQLBuilder sql, Tipologia_rischioBulk rischio){
	sql.addClause("AND","cd_tipologia_rischio", sql.EQUALS, rischio.getCd_tipologia_rischio());
}
/**
 * Viene ricercata la massima Data Inizio Validita presente in tabella per l'oggetto avente la
 * stessa chiave dell'oggetto passato come parametro e data inizio validita diversa da <dataInizio>
 * Viene restituita la data trovata, NULL altrimenti
 * 
*/
private Timestamp findDataInizioPeriodoPrecedente(Tipologia_rischioBulk rischio, Timestamp dataInizio) throws PersistencyException, IntrospectionException{

	SQLBuilder sql = createSQLBuilder();

	sql.setHeader("SELECT MAX(DT_INIZIO_VALIDITA) AS DT_INIZIO_VALIDITA");
	addPrimaryClauses(sql, rischio);
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
 * Viene ricercato l'oggetto bulk avente la stessa chiave dell'oggetto passato come parametro
 * e con data fine validita uguale alla data inizio del parametro meno un giorno
 * Viene restituito l'oggetto trovato, NULL altrimenti
 * 
*/
public Tipologia_rischioBulk findIntervalloPrecedente(Tipologia_rischioBulk rischio, boolean lock) throws PersistencyException, IntrospectionException, OutdatedResourceException, BusyResourceException{

	Timestamp data = findDataInizioPeriodoPrecedente(rischio, rischio.getDt_inizio_validita());
	if (data==null)
		return null;
	SQLBuilder sql = createSQLBuilder();
	addPrimaryClauses(sql, rischio);
	sql.addClause("AND", "dt_inizio_validita", sql.EQUALS, data);

	Tipologia_rischioBulk obj = null;
	if (lock)
		obj = (Tipologia_rischioBulk)fetchAndLock(sql);
	else{
		Broker broker = createBroker(sql);
		if (broker.next())
			obj = (Tipologia_rischioBulk)fetch(broker);
		broker.close();
	}
	return obj;
}
public Tipologia_rischioBulk findTipologiaRischio(String cdTipologia) throws PersistencyException{

	return findTipologiaRischio(cdTipologia, it.cnr.contab.compensi00.docs.bulk.CompensoBulk.getDataOdierna(getServerTimestamp()));
}
public Tipologia_rischioBulk findTipologiaRischio(String cdTipologia, java.sql.Timestamp data) throws PersistencyException{

	SQLBuilder sql = selectTipologiaRischio(cdTipologia, data, null);

	Tipologia_rischioBulk tipologia = null;
	Broker broker = createBroker(sql);
	if (broker.next())
		tipologia = (Tipologia_rischioBulk)fetch(broker);
	broker.close();
		
	return tipologia;
}
public SQLBuilder selectTipologiaRischio(String cdTipologia, java.sql.Timestamp data, CompoundFindClause clauses) throws PersistencyException{

	SQLBuilder sql = createSQLBuilder();
	sql.addClause("AND","cd_tipologia_rischio",sql.EQUALS,cdTipologia);
	sql.addClause("AND","dt_inizio_validita",sql.LESS_EQUALS,data);
	sql.addClause("AND","dt_fine_validita",sql.GREATER_EQUALS,data);
	sql.addClause(clauses);

	return sql;
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
public void validaPeriodoInCreazione(UserContext userContext, Tipologia_rischioBulk corrente) throws PersistencyException, IntrospectionException, OutdatedResourceException, BusyResourceException, it.cnr.jada.comp.ApplicationException{

	Tipologia_rischioBulk precedente = findIntervalloPrecedente(corrente, true);
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
