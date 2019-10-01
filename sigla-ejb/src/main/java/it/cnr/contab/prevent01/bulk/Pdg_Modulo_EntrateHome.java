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
* Created by Generator 1.0
* Date 16/09/2005
*/
package it.cnr.contab.prevent01.bulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoHome;
import it.cnr.contab.pdg01.bulk.Pdg_modulo_entrate_gestBulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class Pdg_Modulo_EntrateHome extends BulkHome {
	public Pdg_Modulo_EntrateHome(java.sql.Connection conn) {
		super(Pdg_Modulo_EntrateBulk.class, conn);
	}
	public Pdg_Modulo_EntrateHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Pdg_Modulo_EntrateBulk.class, conn, persistentCache);
	}
	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk dett) throws PersistencyException {
		try {
				((Pdg_Modulo_EntrateBulk)dett).setPg_dettaglio(
					new Long(
						((Long)findAndLockMax( dett, "pg_dettaglio", new Long(0) )).longValue()+1
					)
				);
			} catch(it.cnr.jada.bulk.BusyResourceException e) {
				throw new PersistencyException(e);
			}
   	}
	public java.util.List findDetailsFor(Pdg_moduloBulk testata)throws PersistencyException{
		SQLBuilder sql = createSQLBuilder();
		
		if (testata != null) {
			sql.addSQLClause("AND", "PDG_MODULO_ENTRATE.ESERCIZIO", sql.EQUALS,testata.getEsercizio());
			sql.addSQLClause("AND", "PDG_MODULO_ENTRATE.CD_CENTRO_RESPONSABILITA", sql.EQUALS,testata.getCd_centro_responsabilita());
			sql.addSQLClause("AND", "PDG_MODULO_ENTRATE.PG_PROGETTO", sql.EQUALS,testata.getPg_progetto());
		}
		return fetchAll(sql);
	}
	/**
	 * Recupera tutti i dati nella tabella PDG_MODULO_ENTRATE_GEST relativi alla testata in uso.
	 *
	 * @param testata La testata in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>Pdg_modulo_entrate_gestBulk</code>
	 */
	public java.util.Collection findDettagliGestionali(Pdg_Modulo_EntrateBulk testata) throws PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Pdg_modulo_entrate_gestBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,testata.getEsercizio());
		sql.addSQLClause("AND","CD_CENTRO_RESPONSABILITA",sql.EQUALS,testata.getCd_centro_responsabilita());
		sql.addSQLClause("AND","PG_PROGETTO",sql.EQUALS,testata.getPg_progetto());
		sql.addSQLClause("AND","CD_NATURA",sql.EQUALS,testata.getCd_natura());
		sql.addSQLClause("AND","ID_CLASSIFICAZIONE",sql.EQUALS,testata.getId_classificazione());
		sql.addSQLClause("AND","CD_CDS_AREA",sql.EQUALS,testata.getCd_cds_area());
		sql.addSQLClause("AND","PG_DETTAGLIO",sql.EQUALS,testata.getPg_dettaglio());
		return dettHome.fetchAll(sql);
	}	
	/**
	 * Calcola il totale dei dati nella tabella PDG_MODULO_ENTRATE_GEST relativi alla testata in uso.
	 *
	 * @param testata La testata in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>Pdg_modulo_entrate_gestBulk</code>
	 */
	public SQLBuilder calcolaTotaleDettagliGestionali(it.cnr.jada.UserContext userContext,Pdg_Modulo_EntrateBulk testata) throws PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Pdg_modulo_entrate_gestBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.resetColumns();
		sql.addColumn("SUM(PDG_MODULO_ENTRATE_GEST.IM_ENTRATA) TOTALE");
		sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,testata.getEsercizio());
		sql.addSQLClause("AND","CD_CENTRO_RESPONSABILITA",sql.EQUALS,testata.getCd_centro_responsabilita());
		sql.addSQLClause("AND","PG_PROGETTO",sql.EQUALS,testata.getPg_progetto());
		sql.addSQLClause("AND","CD_NATURA",sql.EQUALS,testata.getCd_natura());
		sql.addSQLClause("AND","ID_CLASSIFICAZIONE",sql.EQUALS,testata.getId_classificazione());
		sql.addSQLClause("AND","CD_CDS_AREA",sql.EQUALS,testata.getCd_cds_area());
		sql.addSQLClause("AND","PG_DETTAGLIO",sql.EQUALS,testata.getPg_dettaglio());
		return sql;
	}
	public SQLBuilder selectContraenteByClause( Pdg_Modulo_EntrateBulk bulk, TerzoHome home, TerzoBulk terzo,CompoundFindClause clause) throws java.lang.reflect.InvocationTargetException,IllegalAccessException, it.cnr.jada.persistency.PersistencyException
	{
		SQLBuilder sql = getHomeCache().getHome(TerzoBulk.class , "V_TERZO_CF_PI").createSQLBuilder();
		sql.addClause( clause );
		sql.addSQLClause("AND", "DT_FINE_RAPPORTO", sql.ISNULL, null);
		sql.addSQLClause("AND", "DT_CANC", sql.ISNULL, null);
		sql.addOrderBy("CD_TERZO");	
		return sql;		
	}
}