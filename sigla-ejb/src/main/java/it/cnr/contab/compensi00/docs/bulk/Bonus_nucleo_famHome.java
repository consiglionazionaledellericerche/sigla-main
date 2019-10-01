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
 * Created by Aurelio's BulkGenerator 1.0
 * Date 19/02/2009
 */
package it.cnr.contab.compensi00.docs.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class Bonus_nucleo_famHome extends BulkHome {
	public Bonus_nucleo_famHome(Connection conn) {
		super(Bonus_nucleo_famBulk.class, conn);
	}
	public Bonus_nucleo_famHome(Connection conn, PersistentCache persistentCache) {
		super(Bonus_nucleo_famBulk.class, conn, persistentCache);
	}
	public java.util.List findDetailsFor(BonusBulk bonus) throws PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Bonus_nucleo_famBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		
		if (bonus != null) {
			sql.addSQLClause("AND", "BONUS_NUCLEO_FAM.ESERCIZIO", sql.EQUALS,bonus.getEsercizio());
			sql.addSQLClause("AND", "BONUS_NUCLEO_FAM.PG_BONUS", sql.EQUALS,bonus.getPg_bonus());
		}
		sql.addOrderBy("esercizio,pg_bonus,decode(TIPO_COMPONENTE_NUCLEO,'C',1,'F',2,'A',3)");
		return fetchAll(sql);
	}
}