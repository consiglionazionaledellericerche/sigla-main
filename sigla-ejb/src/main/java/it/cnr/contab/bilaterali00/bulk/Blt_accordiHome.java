/*
* Created by Generator 1.0
* Date 19/10/2005
*/
package it.cnr.contab.bilaterali00.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.util.List;
public class Blt_accordiHome extends BulkHome {
	public Blt_accordiHome(java.sql.Connection conn) {
		super(Blt_accordiBulk.class, conn);
	}
	public Blt_accordiHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Blt_accordiBulk.class, conn, persistentCache);
	}
	public java.util.List findBlt_progettiList( it.cnr.jada.UserContext userContext,Blt_accordiBulk accordo ) throws IntrospectionException,PersistencyException 
	{
		PersistentHome progettoHome = getHomeCache().getHome(Blt_progettiBulk.class );
		SQLBuilder sql = progettoHome.createSQLBuilder();
		if (accordo.getCd_accordo()==null)
			sql.addClause(FindClause.AND,"cd_accordo",SQLBuilder.ISNULL, null);
		else
			sql.addClause(FindClause.AND,"cd_accordo",SQLBuilder.EQUALS, accordo.getCd_accordo());
		
		sql.addOrderBy("CD_PROGETTO");
		List l =  progettoHome.fetchAll(sql);
		getHomeCache().fetchAll(userContext);
		return l;
	}
	public java.util.List findBlt_regole_diariaList( it.cnr.jada.UserContext userContext,Blt_accordiBulk accordo ) throws IntrospectionException,PersistencyException 
	{
		PersistentHome regoleDiariaHome = getHomeCache().getHome(Blt_regole_diariaBulk.class );
		SQLBuilder sql = regoleDiariaHome.createSQLBuilder();
		if (accordo.getCd_accordo()==null)
			sql.addClause(FindClause.AND,"cdAccordo",SQLBuilder.ISNULL, null);
		else
			sql.addClause(FindClause.AND,"cdAccordo",SQLBuilder.EQUALS, accordo.getCd_accordo());
		
		sql.addOrderBy("PG_REGOLA");
		List l =  regoleDiariaHome.fetchAll(sql);
		getHomeCache().fetchAll(userContext);
		return l;
	}
}