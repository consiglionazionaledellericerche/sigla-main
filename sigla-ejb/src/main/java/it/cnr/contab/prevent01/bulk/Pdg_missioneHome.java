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
* Creted by Generator 1.0
* Date 18/02/2005
*/
package it.cnr.contab.prevent01.bulk;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class Pdg_missioneHome extends BulkHome {
	private static final long serialVersionUID = 1L;

	public Pdg_missioneHome(java.sql.Connection conn) {
		super(Pdg_missioneBulk.class, conn);
	}
	
	public Pdg_missioneHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Pdg_missioneBulk.class, conn, persistentCache);
	}

	@SuppressWarnings("rawtypes")
	public java.util.List findAss_pdg_missione_tipo_uoList( Pdg_missioneBulk missione ) throws IntrospectionException,PersistencyException 
	{
		PersistentHome home = getHomeCache().getHome(Ass_pdg_missione_tipo_uoBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause(FindClause.AND,"pdgMissione",SQLBuilder.EQUALS, missione);
		return home.fetchAll(sql);
	}
}