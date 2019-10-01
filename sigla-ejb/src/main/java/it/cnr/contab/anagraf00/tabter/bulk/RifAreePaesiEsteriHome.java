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
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 12/09/2011
 */
package it.cnr.contab.anagraf00.tabter.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.Broker;
import it.cnr.jada.persistency.IntrospectionException;
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
	public java.util.List findAreePaesiEsteri() throws IntrospectionException, PersistencyException{

		SQLBuilder sql = createSQLBuilder();

		return fetchAll(sql);
	}
}