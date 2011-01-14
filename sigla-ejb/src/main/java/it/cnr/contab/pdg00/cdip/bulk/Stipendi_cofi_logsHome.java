/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 18/09/2006
 */
package it.cnr.contab.pdg00.cdip.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.Broker;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class Stipendi_cofi_logsHome extends BulkHome {
	public Stipendi_cofi_logsHome(Connection conn) {
		super(Stipendi_cofi_logsBulk.class, conn);
	}
	public Stipendi_cofi_logsHome(Connection conn, PersistentCache persistentCache) {
		super(Stipendi_cofi_logsBulk.class, conn, persistentCache);
	}
	public Long findProg( Integer es, Integer mese ) throws PersistencyException, IntrospectionException
	{
		SQLBuilder sql = createSQLBuilder();
		sql.setHeader("SELECT PG_ESECUZIONE");
		sql.addClause("AND", "esercizio", sql.EQUALS, es);
		sql.addClause("AND", "mese", sql.EQUALS, mese);

		Broker broker = createBroker(sql);
		Object value = null;
		if (broker.next()) {
			value = broker.fetchPropertyValue("pg_esecuzione",getIntrospector().getPropertyType(getPersistentClass(),"pg_esecuzione"));
			broker.close();
		}
		if (value != null)
		    return (Long)value;
		else
			return new Long(0);
	}	
}