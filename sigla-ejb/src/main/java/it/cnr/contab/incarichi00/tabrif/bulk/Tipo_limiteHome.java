/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 18/07/2007
 */
package it.cnr.contab.incarichi00.tabrif.bulk;
import java.sql.Connection;

import it.cnr.contab.incarichi00.bulk.Repertorio_limitiBulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class Tipo_limiteHome extends BulkHome {
	public Tipo_limiteHome(Connection conn) {
		super(Tipo_limiteBulk.class, conn);
	}
	public Tipo_limiteHome(Connection conn, PersistentCache persistentCache) {
		super(Tipo_limiteBulk.class, conn, persistentCache);
	}
	public java.util.List findRepertorioLimitiList(Tipo_limiteBulk tipo_limite ) throws IntrospectionException,PersistencyException, ApplicationException
	{
		PersistentHome repHome = getHomeCache().getHome(Repertorio_limitiBulk.class);
		SQLBuilder sql = repHome.createSQLBuilder();
		
		sql.addClause( "AND", "cd_tipo_limite", SQLBuilder.EQUALS, tipo_limite.getCd_tipo_limite());
		return repHome.fetchAll(sql);
	}
}