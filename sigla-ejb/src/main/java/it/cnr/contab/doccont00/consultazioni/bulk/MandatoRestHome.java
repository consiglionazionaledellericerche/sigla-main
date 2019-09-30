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

package it.cnr.contab.doccont00.consultazioni.bulk;

import it.cnr.contab.rest.bulk.RestServicesHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class MandatoRestHome extends RestServicesHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un MandatoHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public MandatoRestHome(java.sql.Connection conn) {
	super(MandatoRestBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un MandatoHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public MandatoRestHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(MandatoRestBulk.class,conn,persistentCache);
}
@Override
public SQLBuilder selectByClause(UserContext usercontext,
		CompoundFindClause compoundfindclause) throws PersistencyException {
	SQLBuilder builder = super.selectByClause(usercontext, compoundfindclause);
	return super.addConditionCds(usercontext, builder, "cd_cds");
}
}
