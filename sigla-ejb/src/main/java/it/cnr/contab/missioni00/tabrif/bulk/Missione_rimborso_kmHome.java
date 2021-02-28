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

import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneHome;
import it.cnr.contab.consultazioni.bulk.ConsultazioniRestHome;
import it.cnr.contab.docamm00.tabrif.bulk.*;

import java.rmi.RemoteException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.config00.esercizio.bulk.EsercizioHome;
import it.cnr.contab.missioni00.ejb.MissioneComponentSession;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

import javax.ejb.EJBException;

public class Missione_rimborso_kmHome extends BulkHome implements ConsultazioniRestHome {
	public Missione_rimborso_kmHome(java.sql.Connection conn) {
		super(Missione_rimborso_kmBulk.class,conn);
	}
	public Missione_rimborso_kmHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(Missione_rimborso_kmBulk.class,conn,persistentCache);
	}
	/**
	 * Vengono aggiunte alla query le clausole realtive alla chiave primaria
	 * tranne la data inizio validita
	 *
	*/
	private void addPrimaryClauses(SQLBuilder sql, Missione_rimborso_kmBulk rimborsoKm){

		sql.addClause("AND", "ti_auto",            sql.EQUALS, rimborsoKm.getTi_auto());
		sql.addClause("AND", "ti_area_geografica", sql.EQUALS, rimborsoKm.getTi_area_geografica());
		sql.addClause("AND", "pg_nazione",         sql.EQUALS, rimborsoKm.getPg_nazione());
	}
	/**
	 * Viene ricercata la massima Data Inizio Validita presente in tabella per l'oggetto avente la
	 * stessa chiave dell'oggetto passato come parametro e data inizio validita diversa da <dataInizio>
	 * Viene restituita la data trovata, NULL altrimenti
	 *
	*/
	private Timestamp findDataInizioPeriodoPrecedente(Missione_rimborso_kmBulk rimborsoKm, Timestamp dataInizio) throws PersistencyException, IntrospectionException{

		SQLBuilder sql = createSQLBuilder();

		sql.setHeader("SELECT MAX(DT_INIZIO_VALIDITA) AS DT_INIZIO_VALIDITA");
		addPrimaryClauses(sql, rimborsoKm);
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
	public Missione_rimborso_kmBulk findIntervalloPrecedente(Missione_rimborso_kmBulk rimborsoKm, boolean lock) throws PersistencyException, IntrospectionException, OutdatedResourceException, BusyResourceException{

		Timestamp data = findDataInizioPeriodoPrecedente(rimborsoKm, rimborsoKm.getDt_inizio_validita());
		if (data==null)
			return null;
		SQLBuilder sql = createSQLBuilder();
		addPrimaryClauses(sql, rimborsoKm);
		sql.addClause("AND", "dt_inizio_validita", sql.EQUALS, data);

		Missione_rimborso_kmBulk obj = null;
		if (lock)
			obj = (Missione_rimborso_kmBulk)fetchAndLock(sql);
		else{
			Broker broker = createBroker(sql);
			if (broker.next())
				obj = (Missione_rimborso_kmBulk)fetch(broker);
			broker.close();
		}
		return obj;
	}
	/**
	 * Verifico se l'oggetto bulk ricevuto come parametro ha una versione con DATA CANCELLAZIONE valorizzata
	 * Il metodo ritorna la versione dell'oggetto Bulk con data cancellazione valorizzata, NULL altrimenti
	 *
	**/
	public Missione_rimborso_kmBulk getBulkLogicamenteCancellato(Missione_rimborso_kmBulk rimborsoKm) throws it.cnr.jada.comp.ApplicationException, java.sql.SQLException, PersistencyException, IntrospectionException {

		SQLBuilder sql = createSQLBuilder();
		addPrimaryClauses(sql, rimborsoKm);
		sql.addSQLClause("AND","DT_CANCELLAZIONE",   sql.ISNOTNULL, null );

		Missione_rimborso_kmBulk obj = null;
		SQLBroker broker = createBroker(sql);
		if(broker.next())
			obj = (Missione_rimborso_kmBulk)fetch(broker);
		broker.close();

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
	public void validaPeriodoInCreazione(UserContext userContext, Missione_rimborso_kmBulk corrente)  throws PersistencyException, it.cnr.jada.comp.ApplicationException, java.sql.SQLException, OutdatedResourceException, BusyResourceException, IntrospectionException{

		Missione_rimborso_kmBulk precedente = findIntervalloPrecedente(corrente, true);
		if(precedente!=null){
			if (precedente.getDataFineValidita()!=null && corrente.getDt_inizio_validita().compareTo(precedente.getDataFineValidita())<=0){
				java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
				throw new it.cnr.jada.comp.ApplicationException("La Data Inizio Validita non è valida. Deve essere maggiore di " + sdf.format(precedente.getDataFineValidita()));
			}else if(corrente.getDt_inizio_validita().compareTo(precedente.getDt_inizio_validita())<=0){
				java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
				throw new it.cnr.jada.comp.ApplicationException("La Data Inizio Validita non è valida. Deve essere maggiore di " + sdf.format(precedente.getDt_inizio_validita()));
			}
			//precedente.setDt_fine_validita(CompensoBulk.decrementaData(corrente.getDt_inizio_validita()));
			java.util.GregorianCalendar gc = (java.util.GregorianCalendar)java.util.GregorianCalendar.getInstance();
			gc.setTime(corrente.getDt_inizio_validita());
			gc.add(java.util.Calendar.DATE, -1);
			gc.set(gc.HOUR,23);
			gc.set(gc.MINUTE,59);
			gc.set(gc.SECOND,59);
			gc.set(gc.MILLISECOND,0);
			gc.set(gc.AM_PM,gc.AM);
			precedente.setDt_fine_validita(new java.sql.Timestamp(gc.getTime().getTime()));
			update(precedente, userContext);
		}
		corrente.setDt_fine_validita(EsercizioHome.DATA_INFINITO);
	}

	@Override
	public SQLBuilder restSelect(UserContext userContext, SQLBuilder sql, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws ComponentException, PersistencyException {
		if (compoundfindclause != null && compoundfindclause.getClauses() != null){
			CompoundFindClause newClauses = new CompoundFindClause();
			Enumeration e = compoundfindclause.getClauses();
			Enumeration e1 = compoundfindclause.getClauses();
			SQLBuilder sqlExists = null;
			if (isCondizioneRimborsoKmMissione(e)){
				NazioneBulk nazioneBulk = null;
				Timestamp dataTappa = null;
				String tipoAuto = null;

				while(e1.hasMoreElements() ){
					FindClause findClause = (FindClause) e1.nextElement();
					if (findClause instanceof SimpleFindClause){
						SimpleFindClause clause = (SimpleFindClause)findClause;
						int operator = clause.getOperator();
						if (clause.getPropertyName() != null && clause.getPropertyName().equals("nazione") &&
								operator == SQLBuilder.EQUALS){
							NazioneHome nazionehome=(NazioneHome)getHomeCache().getHome(NazioneBulk.class);
							Double str = (Double)clause.getValue();
							nazioneBulk = new NazioneBulk(new Long(str.longValue()));
							nazioneBulk = (NazioneBulk)nazionehome.findByPrimaryKey(nazioneBulk);
						}else if (clause.getPropertyName() != null && clause.getPropertyName().equals("tipoAuto") &&
								operator == SQLBuilder.EQUALS)	{
							String str = (String)clause.getValue();
							tipoAuto = str;
						}else if (clause.getPropertyName() != null && clause.getPropertyName().equals("data") &&
								operator == SQLBuilder.EQUALS)	{
							SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
							Date parsedDate;
							try {
								parsedDate = dateFormat.parse((String) clause.getValue());
								dataTappa = new Timestamp(parsedDate.getTime());
							} catch (ParseException e2) {
                                throw new ComponentException(e2);
							}
						} else {
							newClauses.addClause(clause.getLogicalOperator(), clause.getPropertyName(), clause.getOperator(), clause.getValue());
						}
					}
				}
				if (nazioneBulk != null && dataTappa != null && tipoAuto != null){
					try {
						sql = missioneComponent().selectTipo_autoByClause(userContext, dataTappa, nazioneBulk, tipoAuto, new CompoundFindClause());
					} catch (RemoteException | EJBException e2) {
						throw new ComponentException(e2);
					}
				}
			}
		}
		return sql;
	}

	private Boolean isCondizioneRimborsoKmMissione(Enumeration e) {
		while(e.hasMoreElements() ){
			SimpleFindClause clause = (SimpleFindClause) e.nextElement();
			int operator = clause.getOperator();
			if (clause.getPropertyName() != null && clause.getPropertyName().equals("condizioneRimborsoKmMissione") ){
				return true;
			}
		}
		return false;
	}
	private MissioneComponentSession missioneComponent() throws javax.ejb.EJBException, java.rmi.RemoteException {
		return (MissioneComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRMISSIONI00_EJB_MissioneComponentSession");
	}
}
