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
 * Date 10/07/2015
 */
package it.cnr.contab.utenze00.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class AssBpAccessoHome extends BulkHome {
	public AssBpAccessoHome(Connection conn) {
		super(AssBpAccessoBulk.class, conn);
	}
	public AssBpAccessoHome(Connection conn, PersistentCache persistentCache) {
		super(AssBpAccessoBulk.class, conn, persistentCache);
	}

	@Override
	public SQLBuilder selectByClause(CompoundFindClause compoundfindclause) throws PersistencyException {
		setColumnMap("PREFERITI");
		final SQLBuilder sqlBuilder = super.selectByClause(compoundfindclause);
		sqlBuilder.setAutoJoins(true);
		sqlBuilder.generateJoin("accesso", "ACCESSO");
		return sqlBuilder;
	}
}