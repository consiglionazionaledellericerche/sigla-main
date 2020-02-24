/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import java.rmi.RemoteException;
import java.sql.Connection;

import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.UserContext;
import it.cnr.jada.persistency.PersistencyException;
import javax.ejb.EJBException;

public class TipoMovimentoMagHome extends BulkHome {
	public TipoMovimentoMagHome(Connection conn) {
		super(TipoMovimentoMagBulk.class, conn);
	}
	public TipoMovimentoMagHome(Connection conn, PersistentCache persistentCache) {
		super(TipoMovimentoMagBulk.class, conn, persistentCache);
	}

	@Override
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause) throws PersistencyException{
		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
		sql.addClause("AND","cdCds",SQLBuilder.EQUALS, CNRUserContext.getCd_cds(usercontext));
		return sql;
	}

	public SQLBuilder selectTipoMovimentoMagStornoByClause(UserContext userContext, TipoMovimentoMagBulk tipoMovimentoMagBulk, TipoMovimentoMagHome tipoMovimentoMagHome, TipoMovimentoMagBulk tipoMovimento, CompoundFindClause clause)  throws PersistencyException,ComponentException, EJBException, RemoteException {
		SQLBuilder sql = this.selectByClause(userContext, clause);

		sql.addSQLClause("AND","CD_CDS_STORNO",SQLBuilder.ISNULL,null);
		sql.addSQLClause("AND","CD_TIPO_MOVIMENTO_STORNO",SQLBuilder.ISNULL,null);
		sql.addSQLClause("AND","CD_CDS_RIF",SQLBuilder.ISNULL,null);
		sql.addSQLClause("AND","CD_TIPO_MOVIMENTO_RIF",SQLBuilder.ISNULL,null);
		return sql;
	}
	public SQLBuilder selectTipoMovimentoMagAltByClause(UserContext userContext, TipoMovimentoMagBulk tipoMovimentoMagBulk, TipoMovimentoMagHome tipoMovimentoMagHome, TipoMovimentoMagBulk tipoMovimento, CompoundFindClause clause)  throws PersistencyException,ComponentException, EJBException, RemoteException {
		SQLBuilder sql =  this.selectByClause(userContext, clause);
		return sql;
	}

	public SQLBuilder selectTipoMovimentoMagRifByClause(UserContext userContext, BeneServizioTipoGestBulk beneServizioTipoGestBulk, TipoMovimentoMagHome tipoMovimentoMagHome, TipoMovimentoMagBulk tipoMovimento, CompoundFindClause clause)  throws PersistencyException,ComponentException, EJBException, RemoteException {
		SQLBuilder sql =  this.selectByClause(userContext, clause);
		return sql;
	}



}