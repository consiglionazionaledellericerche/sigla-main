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
 * Created by Aurelio's BulkGenerator 1.0
 * Date 14/06/2007
 */
package it.cnr.contab.compensi00.tabrif.bulk;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;

import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneHome;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.config00.esercizio.bulk.EsercizioHome;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.CdsHome;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.BusyResourceException;
import it.cnr.jada.bulk.OutdatedResourceException;
import it.cnr.jada.persistency.Broker;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class Esenzioni_addcomHome extends BulkHome {
	public Esenzioni_addcomHome(Connection conn) {
		super(Esenzioni_addcomBulk.class, conn);
	}
	public Esenzioni_addcomHome(Connection conn, PersistentCache persistentCache) {
		super(Esenzioni_addcomBulk.class, conn, persistentCache);
	}
	
	public Esenzioni_addcomBulk findEsenzione(UserContext context,ComuneBulk comune) throws PersistencyException{
		java.util.List result=null;
		Esenzioni_addcomBulk Esenzioni_addcom = new Esenzioni_addcomBulk();
		
		SQLBuilder sql = createSQLBuilder();
		sql.addSQLClause("AND", "pg_comune", sql.EQUALS, comune.getPg_comune());
		sql.addClause("AND", "dt_fine_validita", sql.GREATER, getServerTimestamp());
		sql.addClause("AND", "dt_inizio_validita", sql.LESS_EQUALS, getServerTimestamp());
		SQLBuilder sql_succ = createSQLBuilder();
		sql_succ.addSQLClause("AND", "pg_comune", sql.EQUALS, comune.getPg_comune());
		sql_succ.addClause("AND", "dt_inizio_validita", sql.GREATER, getServerTimestamp());
		
		sql.addSQLNotExistsClause("AND", sql_succ);
		Broker broker = createBroker(sql);
		if (broker.next()){
			Esenzioni_addcom = (Esenzioni_addcomBulk)fetch(broker);
			result = getHomeCache().getHome( Esenzioni_addcomBulk.class ).fetchAll( sql );
			getHomeCache().fetchAll(context);		
		}
		broker.close();
		if (result !=null && result.size()==1)
			return Esenzioni_addcom;
		else
			return null;
	}
	private void addPrimaryClauses(SQLBuilder sql, Esenzioni_addcomBulk esenzione){
		sql.addClause("AND","cd_catastale", sql.EQUALS, esenzione.getCd_catastale());
	}
	/**
	 * Viene ricercata la massima Data Inizio Validita presente in tabella per l'oggetto avente la
	 * stessa chiave dell'oggetto passato come parametro e data inizio validita diversa da <dataInizio>
	 * Viene restituita la data trovata, NULL altrimenti
	 * 
	*/
	private Timestamp findDataInizioPeriodoPrecedente(Esenzioni_addcomBulk esenzione, Timestamp dataInizio) throws PersistencyException, IntrospectionException{

		SQLBuilder sql = createSQLBuilder();

		sql.setHeader("SELECT MIN(DT_INIZIO_VALIDITA) AS DT_INIZIO_VALIDITA");
		addPrimaryClauses(sql, esenzione);
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
	 * Viene ricercata la minima Data Inizio Validita presente in tabella per l'oggetto avente la
	 * stessa chiave dell'oggetto passato come parametro e data inizio validita diversa da <dataInizio>
	 * Viene restituita la data trovata, NULL altrimenti
	 * 
	*/
	private Timestamp findDataInizioPeriodoSuccessivo(Esenzioni_addcomBulk esenzione, Timestamp dataInizio) throws PersistencyException, IntrospectionException{

		SQLBuilder sql = createSQLBuilder();

		sql.setHeader("SELECT MAX(DT_INIZIO_VALIDITA) AS DT_INIZIO_VALIDITA");
		addPrimaryClauses(sql, esenzione);
		sql.addClause("AND", "dt_inizio_validita", sql.NOT_EQUALS, dataInizio);

		Broker broker = createBroker(sql);
		Object value = null;
		if (broker.next()) {
			value = broker.fetchPropertyValue("dt_inizio_validita",getIntrospector().getPropertyType(getPersistentClass(),"dt_inizio_validita"));
			broker.close();
		}
		return (Timestamp)value;
	}
	public Esenzioni_addcomBulk findIntervalloPrecedente(Esenzioni_addcomBulk esenzione, boolean lock) throws PersistencyException, IntrospectionException, OutdatedResourceException, BusyResourceException{

		Timestamp data = findDataInizioPeriodoPrecedente(esenzione, esenzione.getDt_inizio_validita());
		if (data==null)
			return null;
		SQLBuilder sql = createSQLBuilder();
		addPrimaryClauses(sql, esenzione);
		sql.addClause("AND", "dt_inizio_validita", sql.EQUALS, data);

		Esenzioni_addcomBulk obj = null;
		if (lock)
			obj = (Esenzioni_addcomBulk)fetchAndLock(sql);
		else{
			Broker broker = createBroker(sql);
			if (broker.next())
				obj = (Esenzioni_addcomBulk)fetch(broker);
			broker.close();
		}
		ComuneHome comuneh=(ComuneHome)getHomeCache().getHome(ComuneBulk.class);
		SQLBuilder sql2 = comuneh.createSQLBuilder();
		sql2.addClause("AND","pg_comune",sql.EQUALS, esenzione.getPg_comune());
		
		List result = comuneh.fetchAll( sql2 );
		if (result.size() > 0)
			obj.setComune((ComuneBulk) result.get(0));
		
		return  (Esenzioni_addcomBulk)findByPrimaryKey(obj);
	}
	public Esenzioni_addcomBulk findIntervalloSuccessivo(Esenzioni_addcomBulk esenzione, boolean lock) throws PersistencyException, IntrospectionException, OutdatedResourceException, BusyResourceException{

		Timestamp data = findDataInizioPeriodoSuccessivo(esenzione, esenzione.getDt_inizio_validita());
		if (data==null)
			return null;
		SQLBuilder sql = createSQLBuilder();
		addPrimaryClauses(sql, esenzione);
		sql.addClause("AND", "dt_inizio_validita", sql.EQUALS, data);

		Esenzioni_addcomBulk obj = null;
		if (lock)
			obj = (Esenzioni_addcomBulk)fetchAndLock(sql);
		else{
			Broker broker = createBroker(sql);
			if (broker.next())
				obj = (Esenzioni_addcomBulk)fetch(broker);
			broker.close();
		}
		ComuneHome comuneh=(ComuneHome)getHomeCache().getHome(ComuneBulk.class);
		SQLBuilder sql2 = comuneh.createSQLBuilder();
		sql2.addClause("AND","pg_comune",sql.EQUALS, esenzione.getPg_comune());
		
		List result = comuneh.fetchAll( sql2 );
		if (result.size() > 0)
			obj.setComune((ComuneBulk) result.get(0));
		
		return  (Esenzioni_addcomBulk)findByPrimaryKey(obj);
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
	public void validaPeriodoInCreazione(UserContext userContext, Esenzioni_addcomBulk corrente) throws PersistencyException, IntrospectionException, OutdatedResourceException, BusyResourceException, it.cnr.jada.comp.ApplicationException{

		Esenzioni_addcomBulk precedente = findIntervalloPrecedente(corrente, true);
		Esenzioni_addcomBulk successivo = findIntervalloSuccessivo(corrente, true);
		if(precedente!=null){
			if (precedente.getDataFineValidita()!=null && corrente.getDt_inizio_validita().compareTo(precedente.getDataFineValidita())<=0){
				java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
				throw new it.cnr.jada.comp.ApplicationException("La Data Inizio Validita non è valida. Deve essere maggiore di " + sdf.format(precedente.getDataFineValidita()));
			}else if(corrente.getDt_inizio_validita().compareTo(precedente.getDt_inizio_validita())<=0){
				java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
				throw new it.cnr.jada.comp.ApplicationException("La Data Inizio Validita non è valida. Deve essere maggiore di " + sdf.format(precedente.getDt_inizio_validita()));
			}
			
		}
		if (successivo!=null){
			if (successivo.getDataFineValidita()!=null && corrente.getDt_inizio_validita().compareTo(successivo.getDataFineValidita())<=0){
				java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
				throw new it.cnr.jada.comp.ApplicationException("La Data Inizio Validita non è valida. Deve essere maggiore di " + sdf.format(successivo.getDataFineValidita()));
			}else if(corrente.getDt_inizio_validita().compareTo(successivo.getDt_inizio_validita())<=0){
				java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
				throw new it.cnr.jada.comp.ApplicationException("La Data Inizio Validita non è valida. Deve essere maggiore di " + sdf.format(successivo.getDt_inizio_validita()));
			}
		    successivo.setDt_fine_validita(CompensoBulk.decrementaData(corrente.getDt_inizio_validita()));
		    update(successivo, userContext);	
	    }
		corrente.setDt_fine_validita(EsercizioHome.DATA_INFINITO);
	}
}