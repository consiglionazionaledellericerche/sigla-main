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
 * Date 23/02/2010
 */
package it.cnr.contab.docamm00.intrastat.bulk;
import java.sql.Connection;

import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneHome;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class Fattura_attiva_intraHome extends BulkHome {
	public Fattura_attiva_intraHome(Connection conn) {
		super(Fattura_attiva_intraBulk.class, conn);
	}
	public Fattura_attiva_intraHome(Connection conn, PersistentCache persistentCache) {
		super(Fattura_attiva_intraBulk.class, conn, persistentCache);
	}
	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk bulk) throws PersistencyException,it.cnr.jada.comp.ComponentException {

		if (bulk == null) return;
		try {
			Fattura_attiva_intraBulk riga = (Fattura_attiva_intraBulk)bulk;
			riga.setPg_riga_intra(new Long(
					((Long)findAndLockMax( bulk, "pg_riga_intra", new Long(0))).longValue()+1));
			
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}
	}

	public SQLBuilder selectNomenclatura_combinataByClause( Fattura_attiva_intraBulk bulk, Nomenclatura_combinataHome home, Nomenclatura_combinataBulk bulkClause,CompoundFindClause clause) throws java.lang.reflect.InvocationTargetException,IllegalAccessException, it.cnr.jada.persistency.PersistencyException{
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio() );
		sql.addClause("AND", "esercizio_inizio", SQLBuilder.LESS_EQUALS, bulk.getEsercizio() );
		sql.addClause("AND", "esercizio_fine", SQLBuilder.GREATER_EQUALS, bulk.getEsercizio() );
		sql.addClause( clause );
		return sql;
	}
	public SQLBuilder selectNatura_transazioneByClause( Fattura_attiva_intraBulk bulk, Natura_transazioneHome home, Natura_transazioneBulk bulkClause,CompoundFindClause clause) throws java.lang.reflect.InvocationTargetException,IllegalAccessException, it.cnr.jada.persistency.PersistencyException{
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio() );		
		sql.addClause( clause );
		return sql;
	}
	public SQLBuilder selectCodici_cpaByClause( Fattura_attiva_intraBulk bulk, Codici_cpaHome home, Codici_cpaBulk bulkClause,CompoundFindClause clause) throws java.lang.reflect.InvocationTargetException,IllegalAccessException, it.cnr.jada.persistency.PersistencyException{
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio() );		
		sql.addClause("AND", "fl_utilizzabile", SQLBuilder.EQUALS,new Boolean(true));
		sql.addClause("AND","ti_bene_servizio", SQLBuilder.EQUALS,"S");
		sql.addClause( clause );
		return sql;
	}
	public SQLBuilder selectNazione_destinazioneByClause( Fattura_attiva_intraBulk bulk,NazioneHome home, NazioneBulk bulkClause,CompoundFindClause clause) throws java.lang.reflect.InvocationTargetException,IllegalAccessException, it.cnr.jada.persistency.PersistencyException{
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause( clause );
		if(bulk.getFattura_attiva().getTi_bene_servizio().compareTo(Bene_servizioBulk.SERVIZIO)!=0)
		{
			sql.addClause("AND","ti_nazione", SQLBuilder.EQUALS,NazioneBulk.CEE);
			sql.addClause("AND","struttura_piva",SQLBuilder.ISNOTNULL,null);
		}
		return sql;
	}
}