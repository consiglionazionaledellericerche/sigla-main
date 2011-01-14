/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 08/01/2008
 */
package it.cnr.contab.incarichi00.bulk;
import java.sql.Connection;
import java.util.List;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class V_incarichi_cdsHome extends BulkHome {
	public V_incarichi_cdsHome(Connection conn) {
		super(V_incarichi_cdsBulk.class, conn);
	}
	public V_incarichi_cdsHome(Connection conn, PersistentCache persistentCache) {
		super(V_incarichi_cdsBulk.class, conn, persistentCache);
	}
	public java.util.List findIncarichi_uoList( it.cnr.jada.UserContext userContext, V_incarichi_cdsBulk inccds, Unita_organizzativaBulk uo) throws IntrospectionException,PersistencyException 
	{
		PersistentHome incHome = getHomeCache().getHome(V_incarichi_uoBulk.class );
		SQLBuilder sql = incHome.createSQLBuilder();
		if (inccds.getEsercizio_limite()==null)
			sql.addClause("AND","esercizio_limite",SQLBuilder.ISNULL, null);
		else
			sql.addClause("AND","esercizio_limite",SQLBuilder.EQUALS, inccds.getEsercizio_limite());
		
		if (inccds.getCd_cds()==null)
			sql.addClause("AND","cd_cds",SQLBuilder.ISNULL, null);
		else
			sql.addClause("AND","cd_cds",SQLBuilder.EQUALS, inccds.getCd_cds());

		if (uo!=null && uo.getCd_unita_organizzativa()!=null)
			sql.addClause("AND","cd_unita_organizzativa",SQLBuilder.EQUALS, uo.getCd_unita_organizzativa());

		if (inccds.getCd_tipo_limite()==null)
			sql.addClause("AND","cd_tipo_limite",SQLBuilder.ISNULL, null);
		else
			sql.addClause("AND","cd_tipo_limite",SQLBuilder.EQUALS, inccds.getCd_tipo_limite());
		
		sql.addOrderBy("ESERCIZIO_LIMITE");
		List l =  incHome.fetchAll(sql);
		getHomeCache().fetchAll(userContext);
		return l;
	}
	public java.util.List findIncarichi_uoList( it.cnr.jada.UserContext userContext, V_incarichi_cdsBulk inccds ) throws IntrospectionException,PersistencyException 
	{
		return findIncarichi_uoList(userContext, inccds, null);
	}
}