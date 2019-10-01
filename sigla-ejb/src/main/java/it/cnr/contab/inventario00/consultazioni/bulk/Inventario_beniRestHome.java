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

package it.cnr.contab.inventario00.consultazioni.bulk;
import it.cnr.contab.rest.bulk.RestServicesHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class Inventario_beniRestHome extends RestServicesHome {
	
public Inventario_beniRestHome(java.sql.Connection conn) {
	super(Inventario_beniRestBulk.class,conn);
}
public Inventario_beniRestHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Inventario_beniRestBulk.class,conn,persistentCache);
}
@Override
public SQLBuilder selectByClause(UserContext usercontext,
		CompoundFindClause compoundfindclause) throws PersistencyException {
	SQLBuilder builder = super.selectByClause(usercontext, compoundfindclause);
	return super.addConditionCds(usercontext, builder, "cd_cds");
}
}
