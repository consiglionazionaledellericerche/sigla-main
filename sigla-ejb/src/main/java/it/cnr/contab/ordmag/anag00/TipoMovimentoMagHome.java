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