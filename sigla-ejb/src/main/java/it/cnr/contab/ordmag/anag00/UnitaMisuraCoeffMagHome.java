/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import java.rmi.RemoteException;
import java.sql.Connection;

import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

import javax.ejb.EJBException;

public class UnitaMisuraCoeffMagHome extends BulkHome {
	public UnitaMisuraCoeffMagHome(Connection conn) {
		super(UnitaMisuraCoeffMagBulk.class, conn);
	}
	public UnitaMisuraCoeffMagHome(Connection conn, PersistentCache persistentCache) {
		super(UnitaMisuraCoeffMagBulk.class, conn, persistentCache);
	}

	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause) throws PersistencyException {
		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
		sql.addClause("AND","cdCds",SQLBuilder.EQUALS, CNRUserContext.getCd_cds(usercontext));
		return sql;
	}
	public SQLBuilder selectMagazzinoByClause(it.cnr.jada.UserContext userContext, UnitaMisuraCoeffMagBulk unitaMisuraCoeffMagBulk, MagazzinoHome magazzinoHome, MagazzinoBulk magazzinoBulk, CompoundFindClause clause)  throws ComponentException, EJBException, RemoteException {
		SQLBuilder sql = magazzinoHome.createSQLBuilder();
		sql.addClause("AND","cdCds",SQLBuilder.EQUALS, CNRUserContext.getCd_cds(userContext));
		return sql;
	}
}