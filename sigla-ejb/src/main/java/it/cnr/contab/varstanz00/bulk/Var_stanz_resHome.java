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
* Date 15/02/2006
*/
package it.cnr.contab.varstanz00.bulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class Var_stanz_resHome extends BulkHome {
	public Var_stanz_resHome(java.sql.Connection conn) {
		super(Var_stanz_resBulk.class, conn);
	}
	public Var_stanz_resHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Var_stanz_resBulk.class, conn, persistentCache);
	}
	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk var_stanz_res) throws PersistencyException,ApplicationException {
		try {
			it.cnr.contab.config00.tabnum.ejb.Numerazione_baseComponentSession numerazione =
				(it.cnr.contab.config00.tabnum.ejb.Numerazione_baseComponentSession)
					it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_TABNUM_EJB_Numerazione_baseComponentSession",
																	it.cnr.contab.config00.tabnum.ejb.Numerazione_baseComponentSession.class);
			if (((Var_stanz_resBulk)var_stanz_res).getPg_variazione()==null) {
				if (!userContext.isTransactional())
					((Var_stanz_resBulk)var_stanz_res).setPg_variazione(
						 numerazione.creaNuovoProgressivo(userContext,CNRUserContext.getEsercizio(userContext), "VAR_STANZ_RES", "PG_VARIAZIONE", CNRUserContext.getUser(userContext))
					);
				else
					((Var_stanz_resBulk)var_stanz_res).setPg_variazione(
							 numerazione.creaNuovoProgressivoTemp(userContext,CNRUserContext.getEsercizio(userContext), "VAR_STANZ_RES", "PG_VARIAZIONE", CNRUserContext.getUser(userContext))
						);
			}
			((Var_stanz_resBulk)var_stanz_res).setEsercizio(CNRUserContext.getEsercizio(userContext));
		}catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new ApplicationException(e);
		}catch(Throwable e) {
			throw new PersistencyException(e);
		}
	}
	/**
	 * Recupera tutti i dati nella tabella ASS_VAR_STANZ_RES_CDR relativi alla testata in uso.
	 *
	 * @param testata La testata in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>Ass_pdg_variazione_cdrBulk</code>
	 */

	public java.util.Collection findAssociazioneCDR(Var_stanz_resBulk testata) throws PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Ass_var_stanz_res_cdrBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause("AND","esercizio",sql.EQUALS,testata.getEsercizio());
		sql.addClause("AND","pg_variazione",sql.EQUALS,testata.getPg_variazione());
		sql.addOrderBy("CD_CENTRO_RESPONSABILITA");
		return dettHome.fetchAll(sql);
	}	

	public java.util.Collection findVariazioniRiga(Var_stanz_resBulk testata) throws PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Var_stanz_res_rigaBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause("AND","esercizio",sql.EQUALS,testata.getEsercizio());
		sql.addClause("AND","pg_variazione",sql.EQUALS,testata.getPg_variazione());
		sql.addClause("AND","cd_cdr",sql.EQUALS,testata.getCdr()!=null?testata.getCdr().getCd_centro_responsabilita():null);
		return dettHome.fetchAll(sql);
	}	
	public java.util.Collection findAllVariazioniRiga(Var_stanz_resBulk testata) throws PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Var_stanz_res_rigaBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause("AND","esercizio",sql.EQUALS,testata.getEsercizio());
		sql.addClause("AND","pg_variazione",sql.EQUALS,testata.getPg_variazione());
		return dettHome.fetchAll(sql);
	}	

	
}