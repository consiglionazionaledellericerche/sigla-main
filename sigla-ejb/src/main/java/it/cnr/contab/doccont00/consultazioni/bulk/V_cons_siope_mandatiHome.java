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
 * Date 06/07/2007
 */
package it.cnr.contab.doccont00.consultazioni.bulk;
import java.sql.Connection;

import it.cnr.contab.consultazioni.bulk.ConsultazioniRestHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class V_cons_siope_mandatiHome extends BulkHome implements ConsultazioniRestHome {
	public V_cons_siope_mandatiHome(Connection conn) {
		super(V_cons_siope_mandatiBulk.class, conn);
	}
	public V_cons_siope_mandatiHome(Connection conn, PersistentCache persistentCache) {
		super(V_cons_siope_mandatiBulk.class, conn, persistentCache);
	}
	@Override
	public SQLBuilder restSelect(UserContext userContext, SQLBuilder sql, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws ComponentException, PersistencyException {
		V_cons_siope_mandatiHome home = (V_cons_siope_mandatiHome) getHomeCache().getHome(V_cons_siope_mandatiBulk.class, "BASEDETT");
		sql = home.selectByClause(userContext, compoundfindclause);
		return sql;
	}
}