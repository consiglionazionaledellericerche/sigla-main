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

package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class AutofatturaHome extends BulkHome {
	public AutofatturaHome(java.sql.Connection conn) {
		super(AutofatturaBulk.class,conn);
	}
	public AutofatturaHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(AutofatturaBulk.class,conn,persistentCache);
	}
	public AutofatturaBulk findFor(Fattura_passivaBulk fatturaPassiva)
		throws PersistencyException {

		SQLBuilder sql = createSQLBuilder();
		sql.addSQLClause("AND", "CD_CDS_FT_PASSIVA", sql.EQUALS, fatturaPassiva.getCd_cds());
		sql.addSQLClause("AND", "CD_UO_FT_PASSIVA", sql.EQUALS, fatturaPassiva.getCd_unita_organizzativa());
		sql.addSQLClause("AND", "PG_FATTURA_PASSIVA", sql.EQUALS, fatturaPassiva.getPg_fattura_passiva());
		sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, fatturaPassiva.getEsercizio());

		java.util.List result = fetchAll(sql);
		if (result == null || result.isEmpty()) return null;
		if (result.size() != 1)
			throw new PersistencyException("Trovate più autofatture per fattura passiva " + fatturaPassiva.getPg_fattura_passiva().longValue());
		AutofatturaBulk autof = (AutofatturaBulk)result.get(0);
		autof.setFattura_passiva(fatturaPassiva);
		return autof;
	}
}
