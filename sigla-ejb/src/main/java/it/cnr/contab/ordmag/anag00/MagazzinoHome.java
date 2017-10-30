/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class MagazzinoHome extends BulkHome {
	public MagazzinoHome(Connection conn) {
		super(MagazzinoBulk.class, conn);
	}
	public MagazzinoHome(Connection conn, PersistentCache persistentCache) {
		super(MagazzinoBulk.class, conn, persistentCache);
	}
	public java.util.List findCategoriaGruppoInventList(MagazzinoBulk mag ) throws IntrospectionException,PersistencyException, ApplicationException
	{
		PersistentHome repHome = getHomeCache().getHome(AbilitBeneServMagBulk.class);
		SQLBuilder sql = repHome.createSQLBuilder();
		
		sql.addSQLClause( "AND", "cd_magazzino", SQLBuilder.EQUALS, mag.getCdMagazzino());
		sql.addSQLClause( "AND", "cd_cds", SQLBuilder.EQUALS, mag.getCdCds());
		return repHome.fetchAll(sql);
	}
}