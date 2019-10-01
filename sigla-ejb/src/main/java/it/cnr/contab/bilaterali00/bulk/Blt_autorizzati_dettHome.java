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
 * Date 01/02/2012
 */
package it.cnr.contab.bilaterali00.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.sql.Connection;
import java.util.List;
public class Blt_autorizzati_dettHome extends BulkHome {
	public Blt_autorizzati_dettHome(Connection conn) {
		super(Blt_autorizzati_dettBulk.class, conn);
	}
	public Blt_autorizzati_dettHome(Connection conn, PersistentCache persistentCache) {
		super(Blt_autorizzati_dettBulk.class, conn, persistentCache);
	}
	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext, OggettoBulk bulk) throws PersistencyException {
		try {
			((Blt_autorizzati_dettBulk)bulk).setPgAutorizzazione(
					new Long(
					((Long)findAndLockMax( bulk, "pgAutorizzazione", new Long(0) )).longValue()+1
				)
			);
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}
	}
	public java.util.List findBlt_visiteList( it.cnr.jada.UserContext userContext,Blt_autorizzati_dettBulk autorizzatoDett ) throws IntrospectionException,PersistencyException 
	{
		PersistentHome visiteHome = getHomeCache().getHome(Blt_visiteBulk.class );
		SQLBuilder sql = visiteHome.createSQLBuilder();
		if (autorizzatoDett.getCdAccordo()==null)
			sql.addClause(FindClause.AND,"cdAccordo",SQLBuilder.ISNULL, null);
		else
			sql.addClause(FindClause.AND,"cdAccordo",SQLBuilder.EQUALS, autorizzatoDett.getCdAccordo());

		if (autorizzatoDett.getCdProgetto()==null)
			sql.addClause(FindClause.AND,"cdProgetto",SQLBuilder.ISNULL, null);
		else
			sql.addClause(FindClause.AND,"cdProgetto",SQLBuilder.EQUALS, autorizzatoDett.getCdProgetto());
		
		if (autorizzatoDett.getCdTerzo()==null)
			sql.addClause(FindClause.AND,"cdTerzo",SQLBuilder.ISNULL, null);
		else
			sql.addClause(FindClause.AND,"cdTerzo",SQLBuilder.EQUALS, autorizzatoDett.getCdTerzo());

		if (autorizzatoDett.getPgAutorizzazione()==null)
			sql.addClause(FindClause.AND,"pgAutorizzazione",SQLBuilder.ISNULL, null);
		else
			sql.addClause(FindClause.AND,"pgAutorizzazione",SQLBuilder.EQUALS, autorizzatoDett.getPgAutorizzazione());

		sql.addOrderBy("PG_VISITA");
		List l =  visiteHome.fetchAll(sql);
		getHomeCache().fetchAll(userContext);
		return l;
	}
}