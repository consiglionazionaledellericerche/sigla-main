/*
* Created by Generator 1.0
* Date 30/05/2005
*/
package it.cnr.contab.pdg00.bulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class Pdg_residuoHome extends BulkHome {
	public Pdg_residuoHome(java.sql.Connection conn) {
		super(Pdg_residuoBulk.class, conn);
	}
	public Pdg_residuoHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Pdg_residuoBulk.class, conn, persistentCache);
	}
	public java.util.Collection findDettagli(Pdg_residuoBulk testata) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Pdg_residuo_detBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause("AND","esercizio",sql.EQUALS,testata.getEsercizio());
		sql.addClause("AND","cd_centro_responsabilita",sql.EQUALS,testata.getCd_centro_responsabilita());
		sql.setOrderBy("pg_dettaglio",it.cnr.jada.util.OrderConstants.ORDER_ASC);
		return dettHome.fetchAll(sql);
	}

	public java.util.Collection findDettagli(Pdg_residuoBulk testata, CompoundFindClause clause) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Pdg_residuo_detBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause(clause);
		sql.addClause("AND","esercizio",sql.EQUALS,testata.getEsercizio());
		sql.addClause("AND","cd_centro_responsabilita",sql.EQUALS,testata.getCd_centro_responsabilita());
		sql.setOrderBy("pg_dettaglio",it.cnr.jada.util.OrderConstants.ORDER_ASC);
		return dettHome.fetchAll(sql);
	}

	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk bulk) throws PersistencyException,ApplicationException {
		((Pdg_residuoBulk)bulk).setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
	}
}