/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 17/02/2011
 */
package it.cnr.contab.docamm00.docs.bulk;
import java.sql.Connection;
import java.util.List;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class VIntrastatInviatoHome extends BulkHome {
	public VIntrastatInviatoHome(Connection conn) {
		super(VIntrastatInviatoBulk.class, conn);
	}
	public VIntrastatInviatoHome(Connection conn, PersistentCache persistentCache) {
		super(VIntrastatInviatoBulk.class, conn, persistentCache);
	}
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause) throws PersistencyException {
		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
	    sql.addClause("AND","esercizio",SQLBuilder.EQUALS, CNRUserContext.getEsercizio(usercontext));
	    PersistentHome uoEnteHome = getHomeCache().getHome(Unita_organizzativa_enteBulk.class);
		List result = uoEnteHome.fetchAll( uoEnteHome.createSQLBuilder() );
		String uoEnte = ((Unita_organizzativaBulk) result.get(0)).getCd_unita_organizzativa();
		
	    if(CNRUserContext.getCd_unita_organizzativa(usercontext).compareTo(uoEnte)!=0)
	    	sql.addClause("AND","uo",SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(usercontext));
		return sql;
	}
}