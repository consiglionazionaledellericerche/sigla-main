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