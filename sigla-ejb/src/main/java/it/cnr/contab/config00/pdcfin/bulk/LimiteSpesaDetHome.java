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

import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class LimiteSpesaDetHome extends BulkHome {
	public LimiteSpesaDetHome(Connection conn) {
		super(LimiteSpesaDetBulk.class, conn);
	}
	public LimiteSpesaDetHome(Connection conn, PersistentCache persistentCache) {
		super(LimiteSpesaDetBulk.class, conn, persistentCache);
	}
	public java.util.List getDetailsFor(LimiteSpesaBulk bulk) throws it.cnr.jada.persistency.PersistencyException{
		it.cnr.jada.persistency.sql.SQLBuilder sql = createSQLBuilder();
		sql.addClause("AND","esercizio",sql.EQUALS,bulk.getEsercizio());
		sql.addClause("AND","cd_elemento_voce",sql.EQUALS,bulk.getCd_elemento_voce());
		sql.addClause("AND","ti_gestione",sql.EQUALS,bulk.getTi_gestione());
		sql.addClause("AND","ti_appartenza",sql.EQUALS,bulk.getTi_appartenenza());
		sql.addClause("AND","fonte",sql.EQUALS,bulk.getFonte());
		return fetchAll(sql);
	}
}