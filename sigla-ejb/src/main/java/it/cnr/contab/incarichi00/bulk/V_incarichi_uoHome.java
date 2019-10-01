/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
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
 * Created by Aurelio's BulkGenerator 1.0
 * Date 09/01/2008
 */
package it.cnr.contab.incarichi00.bulk;
import java.sql.Connection;
import java.util.List;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class V_incarichi_uoHome extends BulkHome {
	public V_incarichi_uoHome(Connection conn) {
		super(V_incarichi_uoBulk.class, conn);
	}
	public V_incarichi_uoHome(Connection conn, PersistentCache persistentCache) {
		super(V_incarichi_uoBulk.class, conn, persistentCache);
	}
	public java.util.List findIncarichi_terzoList( it.cnr.jada.UserContext userContext, V_incarichi_uoBulk incuo) throws IntrospectionException,PersistencyException 
	{
		PersistentHome incHome = getHomeCache().getHome(V_incarichi_terzoBulk.class );
		SQLBuilder sql = incHome.createSQLBuilder();
		if (incuo.getEsercizio_limite()==null)
			sql.addClause("AND","esercizio_limite",SQLBuilder.ISNULL, null);
		else
			sql.addClause("AND","esercizio_limite",SQLBuilder.EQUALS, incuo.getEsercizio_limite());
		
		if (incuo.getCd_cds()==null)
			sql.addClause("AND","cd_cds",SQLBuilder.ISNULL, null);
		else
			sql.addClause("AND","cd_cds",SQLBuilder.EQUALS, incuo.getCd_cds());

		if (incuo.getCd_unita_organizzativa()!=null)
			sql.addClause("AND","cd_unita_organizzativa",SQLBuilder.EQUALS, incuo.getCd_unita_organizzativa());

		if (incuo.getCd_tipo_limite()==null)
			sql.addClause("AND","cd_tipo_limite",SQLBuilder.ISNULL, null);
		else
			sql.addClause("AND","cd_tipo_limite",SQLBuilder.EQUALS, incuo.getCd_tipo_limite());
		
		sql.addOrderBy("CD_TERZO");
		List l =  incHome.fetchAll(sql);
		getHomeCache().fetchAll(userContext);
		return l;
	}
	public java.util.List findIncarichi_validiList( it.cnr.jada.UserContext userContext, V_incarichi_uoBulk incuo) throws IntrospectionException,PersistencyException 
	{
		PersistentHome incHome = getHomeCache().getHome(V_incarichi_da_assegnareBulk.class );
		SQLBuilder sql = incHome.createSQLBuilder();

		if (incuo.getEsercizio_limite()==null)
			sql.addClause("AND","esercizio_limite",SQLBuilder.ISNULL, null);
		else
			sql.addClause("AND","esercizio_limite",SQLBuilder.EQUALS, incuo.getEsercizio_limite());
		
		if (incuo.getCd_cds()==null)
			sql.addClause("AND","cd_cds",SQLBuilder.ISNULL, null);
		else
			sql.addClause("AND","cd_cds",SQLBuilder.EQUALS, incuo.getCd_cds());

		if (incuo.getCd_unita_organizzativa()!=null)
			sql.addClause("AND","cd_unita_organizzativa",SQLBuilder.EQUALS, incuo.getCd_unita_organizzativa());

		if (incuo.getCd_tipo_limite()==null)
			sql.addClause("AND","cd_tipo_limite",SQLBuilder.ISNULL, null);
		else
			sql.addClause("AND","cd_tipo_limite",SQLBuilder.EQUALS, incuo.getCd_tipo_limite());
		
		sql.addOrderBy("ESERCIZIO, PG_REPERTORIO");
		List l =  incHome.fetchAll(sql);
		getHomeCache().fetchAll(userContext);
		return l;
	}
}