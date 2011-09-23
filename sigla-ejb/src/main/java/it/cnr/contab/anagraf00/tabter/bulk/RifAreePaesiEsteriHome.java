/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 12/09/2011
 */
package it.cnr.contab.anagraf00.tabter.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.Broker;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class RifAreePaesiEsteriHome extends BulkHome {
	public RifAreePaesiEsteriHome(Connection conn) {
		super(RifAreePaesiEsteriBulk.class, conn);
	}
	public RifAreePaesiEsteriHome(Connection conn, PersistentCache persistentCache) {
		super(RifAreePaesiEsteriBulk.class, conn, persistentCache);
	}
	
	public RifAreePaesiEsteriBulk findAreePaesiEsteri(String cd_area) throws PersistencyException{
		
		SQLBuilder sql = createSQLBuilder();
		sql.addClause("AND", "cd_area_estera", sql.EQUALS, cd_area);

		RifAreePaesiEsteriBulk area = null;

		Broker broker = createBroker(sql);
		if (broker.next())
			area = (RifAreePaesiEsteriBulk)fetch(broker);
		broker.close();

		return area;
	}
}