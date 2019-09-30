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
 * Date 18/03/2016
 */
package it.cnr.contab.doccont00.consultazioni.bulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdrHome;
import it.cnr.contab.consultazioni.bulk.ConsultazioniRestHome;
import it.cnr.contab.rest.bulk.RestServicesHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.sql.Connection;
public class VSitGaeResiduiSpesaHome extends RestServicesHome implements ConsultazioniRestHome {
	public VSitGaeResiduiSpesaHome(Connection conn) {
		super(VSitGaeResiduiSpesaBulk.class, conn);
	}
	public VSitGaeResiduiSpesaHome(Connection conn, PersistentCache persistentCache) {
		super(VSitGaeResiduiSpesaBulk.class, conn, persistentCache);
	}
	@Override
	public SQLBuilder selectByClause(UserContext usercontext,
			CompoundFindClause compoundfindclause) throws PersistencyException {
		SQLBuilder builder = super.selectByClause(usercontext, compoundfindclause);
		return super.addConditionCds(usercontext, builder, "cds");
	}

	@Override
	public SQLBuilder restSelect(UserContext userContext, SQLBuilder sql, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws ComponentException, PersistencyException {
		CdrBulk cdrUtente = cdrFromUserContext(userContext);
		if ( !cdrUtente.isCdrILiv() ){
			sql.addSQLClause("AND", "CDS",sql.EQUALS, CNRUserContext.getCd_cdr(userContext));
			sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
		} else {
			sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
			sql.addSQLClause("AND", "CDS", SQLBuilder.EQUALS, CNRUserContext.getCd_cds(userContext));
			sql.openParenthesis("AND");
			sql.addSQLClause("OR", "CD_CENTRO_RESPONSABILITA",sql.EQUALS,CNRUserContext.getCd_cdr(userContext));
			CdrHome cdrHome = (CdrHome) getHomeCache().getHome(CdrBulk.class);
			for (java.util.Iterator j = cdrHome.findCdrAfferenti(cdrUtente).iterator(); j.hasNext();) {
				CdrBulk cdrAfferenti = (CdrBulk) j.next();
				sql.addSQLClause("OR","CD_CENTRO_RESPONSABILITA", sql.EQUALS, cdrAfferenti.getCd_centro_responsabilita());
			}
			sql.closeParenthesis();
		}
		return sql;
	}


	private CdrBulk cdrFromUserContext(UserContext userContext) throws ComponentException {
		try {
			it.cnr.contab.utenze00.bulk.UtenteBulk user = new it.cnr.contab.utenze00.bulk.UtenteBulk(userContext.getUser() );
			user = (it.cnr.contab.utenze00.bulk.UtenteBulk)getHomeCache().getHome(user).findByPrimaryKey(user);

			CdrBulk cdr = new CdrBulk( it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cdr(userContext) );
			return (CdrBulk)getHomeCache().getHome(cdr).findByPrimaryKey(cdr);

		} catch (it.cnr.jada.persistency.PersistencyException e) {
			throw new ComponentException(e);
		}
	}
}