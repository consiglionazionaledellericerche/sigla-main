/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 21/02/2011
 */
package it.cnr.contab.config00.pdcfin.bulk;
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
public class VLimiteSpesaDetHome extends BulkHome {
	public VLimiteSpesaDetHome(Connection conn) {
		super(VLimiteSpesaDetBulk.class, conn);
	}
	public VLimiteSpesaDetHome(Connection conn, PersistentCache persistentCache) {
		super(VLimiteSpesaDetBulk.class, conn, persistentCache);
	}
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause) throws PersistencyException {
		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
	    sql.addClause("AND","esercizio",SQLBuilder.EQUALS, CNRUserContext.getEsercizio(usercontext));
	    PersistentHome uoEnteHome = getHomeCache().getHome(Unita_organizzativa_enteBulk.class);
		List result = uoEnteHome.fetchAll( uoEnteHome.createSQLBuilder() );
		String cdsEnte = ((Unita_organizzativaBulk) result.get(0)).getCd_cds();
		
	    if(CNRUserContext.getCd_cds(usercontext).compareTo(cdsEnte)!=0)
	    	sql.addClause("AND","cd_cds",SQLBuilder.EQUALS, CNRUserContext.getCd_cds(usercontext));
		return sql;
	}
}