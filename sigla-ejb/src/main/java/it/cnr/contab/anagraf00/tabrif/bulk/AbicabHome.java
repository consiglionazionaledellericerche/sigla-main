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

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class AbicabHome extends BulkHome {
    public AbicabHome(java.sql.Connection conn) {
        super(AbicabBulk.class, conn);
    }

    public AbicabHome(java.sql.Connection conn, PersistentCache persistentCache) {
        super(AbicabBulk.class, conn, persistentCache);
    }

	public SQLBuilder createFullSQLBuilder() {
		return super.createSQLBuilder();
	}

    public SQLBuilder createSQLBuilder() {
        SQLBuilder sql = super.createSQLBuilder();
        sql.addClause("AND", "fl_cancellato", SQLBuilder.EQUALS, Boolean.FALSE);
        return sql;
    }

}
