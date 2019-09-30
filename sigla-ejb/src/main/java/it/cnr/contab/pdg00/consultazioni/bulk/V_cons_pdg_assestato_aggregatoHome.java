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
* Created by Generator 1.0
* Date 09/11/2005
*/
package it.cnr.contab.pdg00.consultazioni.bulk;
import java.sql.Connection;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class V_cons_pdg_assestato_aggregatoHome extends BulkHome {
	public V_cons_pdg_assestato_aggregatoHome(java.sql.Connection conn) {
		super(V_cons_pdg_assestato_aggregatoBulk.class, conn);
	}
	public V_cons_pdg_assestato_aggregatoHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_cons_pdg_assestato_aggregatoBulk.class, conn, persistentCache);
	}
}