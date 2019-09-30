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
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 12/09/2011
 */
package it.cnr.contab.missioni00.tabrif.bulk;
import java.sql.Connection;
import java.sql.Timestamp;

import it.cnr.contab.anagraf00.tabter.bulk.RifAreePaesiEsteriBulk;
import it.cnr.contab.anagraf00.tabter.bulk.RifAreePaesiEsteriHome;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.config00.esercizio.bulk.EsercizioHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.BusyResourceException;
import it.cnr.jada.bulk.OutdatedResourceException;
import it.cnr.jada.persistency.Broker;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class MissioneQuotaRimborsoHome extends BulkHome {
	public MissioneQuotaRimborsoHome(Connection conn) {
		super(MissioneQuotaRimborsoBulk.class, conn);
	}
	public MissioneQuotaRimborsoHome(Connection conn, PersistentCache persistentCache) {
		super(MissioneQuotaRimborsoBulk.class, conn, persistentCache);
	}
	public java.util.List findGruppiInquadramento(MissioneQuotaRimborsoBulk aRimborso, Gruppo_inquadramentoHome home,Gruppo_inquadramentoBulk clause) throws PersistencyException,IntrospectionException {
		return home.findGruppiInquadramento();
	}
	public java.util.List findAreePaesiEsteri(MissioneQuotaRimborsoBulk aRimborso, RifAreePaesiEsteriHome home,RifAreePaesiEsteriBulk clause) throws PersistencyException,IntrospectionException {
		return home.findAreePaesiEsteri();
	}	
	private void addPrimaryClauses(SQLBuilder sql, MissioneQuotaRimborsoBulk rimborso){

		sql.addClause("AND","cd_gruppo_inquadramento", sql.EQUALS, rimborso.getCd_gruppo_inquadramento());
		sql.addClause("AND","cd_area_estera",          sql.EQUALS, rimborso.getCd_area_estera());
	}
	private Timestamp findDataInizioPeriodoPrecedente(MissioneQuotaRimborsoBulk rimborso, Timestamp dataInizio) throws PersistencyException, IntrospectionException{

		SQLBuilder sql = createSQLBuilder();

		sql.setHeader("SELECT MAX(DT_INIZIO_VALIDITA) AS DT_INIZIO_VALIDITA");
		addPrimaryClauses(sql, rimborso);
		sql.addClause("AND", "dt_inizio_validita", sql.NOT_EQUALS, dataInizio);

		Broker broker = createBroker(sql);
		Object value = null;
		if (broker.next()) {
			value = broker.fetchPropertyValue("dt_inizio_validita",getIntrospector().getPropertyType(getPersistentClass(),"dt_inizio_validita"));
			broker.close();
		}
		return (Timestamp)value;
	}
	public MissioneQuotaRimborsoBulk findIntervalloPrecedente(MissioneQuotaRimborsoBulk rimborso, boolean lock) throws PersistencyException, IntrospectionException, OutdatedResourceException, BusyResourceException{

		Timestamp data = findDataInizioPeriodoPrecedente(rimborso, rimborso.getDt_inizio_validita());
		if (data==null)
			return null;
		SQLBuilder sql = createSQLBuilder();
		addPrimaryClauses(sql, rimborso);
		sql.addClause("AND", "dt_inizio_validita", sql.EQUALS, data);

		MissioneQuotaRimborsoBulk obj = null;
		if (lock)
			obj = (MissioneQuotaRimborsoBulk)fetchAndLock(sql);
		else{
			Broker broker = createBroker(sql);
			if (broker.next())
				obj = (MissioneQuotaRimborsoBulk)fetch(broker);
			broker.close();
		}
		return obj;
	}

	public void validaPeriodoInCreazione(UserContext userContext, MissioneQuotaRimborsoBulk corrente) throws PersistencyException, IntrospectionException, OutdatedResourceException, BusyResourceException, it.cnr.jada.comp.ApplicationException{

		MissioneQuotaRimborsoBulk precedente = findIntervalloPrecedente(corrente, true);
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