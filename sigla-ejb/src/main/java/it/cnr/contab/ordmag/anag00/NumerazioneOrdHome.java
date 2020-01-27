/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import java.rmi.RemoteException;
import java.sql.Connection;

import javax.ejb.EJBException;

import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class NumerazioneOrdHome extends BulkHome {
	public NumerazioneOrdHome(Connection conn) {
		super(NumerazioneOrdBulk.class, conn);
	}
	public NumerazioneOrdHome(Connection conn, PersistentCache persistentCache) {
		super(NumerazioneOrdBulk.class, conn, persistentCache);
	}
	
	public SQLBuilder selectUnitaOperativaOrdByClause(it.cnr.jada.UserContext userContext, NumerazioneOrdBulk numerazioneOrdBulk, UnitaOperativaOrdHome unitaOperativaOrdHome,UnitaOperativaOrdBulk unitaOperativaOrdBulk,CompoundFindClause clause)  throws ComponentException, EJBException, RemoteException {
		SQLBuilder sql = unitaOperativaOrdHome.createSQLBuilder();
		sql.addSQLClause(FindClause.AND,"CD_UNITA_OPERATIVA",SQLBuilder.EQUALS,CNRUserContext.getCd_cds(userContext));
		sql.addClause(clause);
		return sql;
	}
	
	@Override
	public SQLBuilder selectByClause(UserContext userContext,
			CompoundFindClause compoundfindclause) throws PersistencyException {
		SQLBuilder sql = super.selectByClause(userContext, compoundfindclause);
		sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,CNRUserContext.getEsercizio(userContext));

		return sql;
	}
}