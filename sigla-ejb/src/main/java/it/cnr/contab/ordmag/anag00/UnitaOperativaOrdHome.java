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
public class UnitaOperativaOrdHome extends BulkHome {
	public UnitaOperativaOrdHome(Connection conn) {
		super(UnitaOperativaOrdBulk.class, conn);
	}
	public UnitaOperativaOrdHome(Connection conn, PersistentCache persistentCache) {
		super(UnitaOperativaOrdBulk.class, conn, persistentCache);
	}
	public java.util.List findAssUnitaOperativaList(UnitaOperativaOrdBulk uop ) throws IntrospectionException,PersistencyException, ApplicationException
	{
		PersistentHome repHome = getHomeCache().getHome(AssUnitaOperativaOrdBulk.class);
		SQLBuilder sql = repHome.createSQLBuilder();
		
		sql.addSQLClause( "AND", "cd_unita_operativa", SQLBuilder.EQUALS, uop.getCdUnitaOperativa());
		return repHome.fetchAll(sql);
	}
}