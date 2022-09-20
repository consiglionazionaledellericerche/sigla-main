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

package it.cnr.contab.compensi00.docs.bulk;

import it.cnr.contab.docamm00.docs.bulk.AutofatturaBulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ApplicationRuntimeException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.util.List;
import java.util.Optional;

public class Ass_compenso_conguaglioHome extends BulkHome {

	public Ass_compenso_conguaglioHome(java.sql.Connection conn) {
	super(Ass_compenso_conguaglioBulk.class,conn);
}

	public Ass_compenso_conguaglioHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Ass_compenso_conguaglioBulk.class,conn,persistentCache);
	}

	public Ass_compenso_conguaglioBulk findAssCompensoConguaglio(CompensoBulk compenso) throws PersistencyException {
		SQLBuilder sql = this.createSQLBuilder();
		sql.addClause(FindClause.AND, "esercizio_compenso", SQLBuilder.EQUALS, compenso.getEsercizio());
		sql.addClause(FindClause.AND, "pg_compenso", SQLBuilder.EQUALS, compenso.getPg_compenso());
		sql.addClause(FindClause.AND, "cd_cds_compenso", SQLBuilder.EQUALS, compenso.getCd_cds());
		sql.addClause(FindClause.AND, "cd_uo_compenso", SQLBuilder.EQUALS, compenso.getCd_unita_organizzativa());

		List<Ass_compenso_conguaglioBulk> result = fetchAll(sql);

		if (result == null || result.isEmpty()) return null;
		if (result.size() != 1)
			throw new ApplicationRuntimeException("Trovati più conguagli associati al compenso " + compenso.getCd_cds()+"/"+compenso.getCd_unita_organizzativa()+
					"/"+compenso.getEsercizio()+"/"+compenso.getPg_compenso());
		return result.get(0);
	}

	public List<Ass_compenso_conguaglioBulk> findAssCompensoConguaglio(ConguaglioBulk conguaglio) throws PersistencyException {
		SQLBuilder sql = this.createSQLBuilder();
		sql.addClause(FindClause.AND, "esercizio_conguaglio", SQLBuilder.EQUALS, conguaglio.getEsercizio());
		sql.addClause(FindClause.AND, "pg_conguaglio", SQLBuilder.EQUALS, conguaglio.getPg_conguaglio());
		sql.addClause(FindClause.AND, "cd_cds_conguaglio", SQLBuilder.EQUALS, conguaglio.getCd_cds());
		sql.addClause(FindClause.AND, "cd_uo_conguaglio", SQLBuilder.EQUALS, conguaglio.getCd_unita_organizzativa());

		return fetchAll(sql);
	}
}
