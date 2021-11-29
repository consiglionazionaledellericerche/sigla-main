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

package it.cnr.contab.reports.bulk;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.BusyResourceException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.persistency.sql.SimpleFindClause;
import it.cnr.jada.util.DateUtils;
import it.cnr.jada.util.ejb.EJBCommonServices;

public class Print_spoolerHome extends BulkHome {
	public Print_spoolerHome(java.sql.Connection conn) {
		super(Print_spoolerBulk.class, conn);
	}

	public Print_spoolerHome(java.sql.Connection conn,
			PersistentCache persistentCache) {
		super(Print_spoolerBulk.class, conn, persistentCache);
	}

	/**
	 * Inizializza la chiave primaria di un OggettoBulk per un inserimento. Da
	 * usare principalmente per riempire i progressivi automatici.
	 * 
	 * @param bulk
	 *            l'OggettoBulk da inizializzare
	 */
	public void initializePrimaryKeyForInsert(
			it.cnr.jada.UserContext userContext, OggettoBulk bulk)
			throws PersistencyException, it.cnr.jada.comp.ComponentException {
		((Print_spoolerBulk) bulk).setPgStampa(new Long(fetchNextSequenceValue(
				userContext, "PG_STAMPA").longValue()));
	}

	/**
	 * Ritorna un SQLBuilder con la columnMap del ricevente
	 */
	public SQLBuilder selectJobsToDelete() {

		SQLBuilder sql = createSQLBuilder();
		sql.addTableToHeader("PARAMETRI_ENTE");
		sql
				.addSQLClause("AND", "PARAMETRI_ENTE.ATTIVO",
						SQLBuilder.EQUALS, "Y");
		sql.addSQLClause("AND", "PRINT_SPOOLER.STATO", SQLBuilder.EQUALS, "S");
		sql.addSQLClause("AND", "PRINT_SPOOLER.DT_PROSSIMA_ESECUZIONE",
				SQLBuilder.ISNULL, null);
		sql
				.addSQLClause("AND",
						"TRUNC(SYSDATE - PRINT_SPOOLER.DUVA) > Nvl(PARAMETRI_ENTE.CANCELLA_STAMPE,30)");
		return sql;
	}

	public String getLastServerActive() throws PersistencyException, BusyResourceException{
		Print_spoolerBulk print = new Print_spoolerBulk();
		Long pgStampa = (Long) findMax(print, "pgStampa", null, false, new SimpleFindClause(FindClause.AND,"stato", SQLBuilder.EQUALS, Print_spoolerBulk.STATO_ESEGUITA));
		print.setPgStampa(pgStampa);
		return ((Print_spoolerBulk)findByPrimaryKey(print)).getServer();
	}
	
	public void deleteRiga(Print_spoolerBulk bulk, UserContext userContext)
			throws PersistencyException {
		delete(bulk, userContext);
	}

	public SQLBuilder getJobWaitToJsoDS() throws PersistencyException, BusyResourceException{

		SQLBuilder sql = createSQLBuilder();
		sql.openParenthesis("AND");
		sql.addClause("AND ","dtProssimaEsecuzione",sql.ISNULL,null);
		sql.addClause("AND","stato",sql.EQUALS, Print_spoolerBulk.STATO_IN_CODA_WAITDS);
		sql.closeParenthesis();

		sql.addClause("OR (","dtProssimaEsecuzione",sql.LESS, EJBCommonServices
				.getServerDate());
		sql.addClause("AND","stato",sql.EQUALS, Print_spoolerBulk.STATO_IN_CODA_WAITDS);
		sql.closeParenthesis();
		sql.addOrderBy("pg_stampa desc");

		return sql;
	}

}