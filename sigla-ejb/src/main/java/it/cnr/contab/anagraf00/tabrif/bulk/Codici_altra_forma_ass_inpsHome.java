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

package it.cnr.contab.anagraf00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Codici_altra_forma_ass_inpsHome extends BulkHome {
public Codici_altra_forma_ass_inpsHome(java.sql.Connection conn) {
	super(Codici_altra_forma_ass_inpsBulk.class,conn);
}
public Codici_altra_forma_ass_inpsHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Codici_altra_forma_ass_inpsBulk.class,conn,persistentCache);
}
@Override
public SQLBuilder selectByClause(CompoundFindClause compoundfindclause) throws PersistencyException {
	SQLBuilder sql = super.selectByClause(compoundfindclause);
	sql.addSQLClause("AND","FL_CANCELLATO",sql.EQUALS, "N");
	return sql;
}
}
