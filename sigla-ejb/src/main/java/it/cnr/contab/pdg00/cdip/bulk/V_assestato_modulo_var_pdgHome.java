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
* Date 12/07/2005
*/
package it.cnr.contab.pdg00.cdip.bulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class V_assestato_modulo_var_pdgHome extends BulkHome {
	protected V_assestato_modulo_var_pdgHome(Class clazz,java.sql.Connection connection) {
		super(clazz,connection);
	}
	protected V_assestato_modulo_var_pdgHome(Class clazz,java.sql.Connection connection,PersistentCache persistentCache) {
		super(clazz,connection,persistentCache);
	}
	public V_assestato_modulo_var_pdgHome(java.sql.Connection conn) {
		super(V_assestato_modulo_var_pdgBulk.class, conn);
	}
	public V_assestato_modulo_var_pdgHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_assestato_modulo_var_pdgBulk.class, conn, persistentCache);
	}
}