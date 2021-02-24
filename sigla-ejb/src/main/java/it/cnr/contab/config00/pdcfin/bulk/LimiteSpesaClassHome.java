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
 * Date 15/12/2010
 */
package it.cnr.contab.config00.pdcfin.bulk;

import it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociBulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.sql.Connection;

public class LimiteSpesaClassHome extends BulkHome {
	public LimiteSpesaClassHome(Connection conn) {
		super(LimiteSpesaClassBulk.class, conn);
	}

	public LimiteSpesaClassHome(Connection conn, PersistentCache persistentCache) {
		super(LimiteSpesaClassBulk.class, conn, persistentCache);
	}

	public java.util.List getDetailsFor(Classificazione_vociBulk bulk) throws it.cnr.jada.persistency.PersistencyException{
		it.cnr.jada.persistency.sql.SQLBuilder sql = this.createSQLBuilder();
		sql.addClause(FindClause.AND,"id_classificazione", SQLBuilder.EQUALS, bulk.getId_classificazione());
		return fetchAll(sql);
	}
}