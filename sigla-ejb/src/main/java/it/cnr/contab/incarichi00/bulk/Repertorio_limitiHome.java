/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 18/07/2007
 */
package it.cnr.contab.incarichi00.bulk;
import java.sql.Connection;
import java.util.List;

import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class Repertorio_limitiHome extends BulkHome {
	public Repertorio_limitiHome(Connection conn) {
		super(Repertorio_limitiBulk.class, conn);
	}
	public Repertorio_limitiHome(Connection conn, PersistentCache persistentCache) {
		super(Repertorio_limitiBulk.class, conn, persistentCache);
	}
	public java.util.List findIncarichi_cdsList( it.cnr.jada.UserContext userContext, Repertorio_limitiBulk replim, CdsBulk cds ) throws IntrospectionException,PersistencyException 
	{
		PersistentHome incHome = getHomeCache().getHome(V_incarichi_cdsBulk.class );
		SQLBuilder sql = incHome.createSQLBuilder();
		if (replim.getEsercizio()==null)
			sql.addClause("AND","esercizio_limite",SQLBuilder.ISNULL, null);
		else
			sql.addClause("AND","esercizio_limite",SQLBuilder.EQUALS, replim.getEsercizio());
		
		if (cds!=null && cds.getCd_unita_organizzativa()!=null)
			sql.addClause("AND","cd_cds",SQLBuilder.EQUALS, cds.getCd_unita_organizzativa());

		if (replim.getCd_tipo_limite()==null)
			sql.addClause("AND","cd_tipo_limite",SQLBuilder.ISNULL, null);
		else
			sql.addClause("AND","cd_tipo_limite",SQLBuilder.EQUALS, replim.getCd_tipo_limite());
		
		sql.addOrderBy("ESERCIZIO_LIMITE");
		List l =  incHome.fetchAll(sql);
		getHomeCache().fetchAll(userContext);
		return l;
	}
	public java.util.List findIncarichi_cdsList( it.cnr.jada.UserContext userContext, Repertorio_limitiBulk replim ) throws IntrospectionException,PersistencyException 
	{
		return findIncarichi_cdsList(userContext, replim, null);
	}
}