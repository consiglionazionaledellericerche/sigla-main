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

package it.cnr.contab.missioni00.docs.bulk;

import it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk;
import it.cnr.contab.doccont00.intcass.bulk.Distinta_cassiere_detBulk;
import it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

import java.util.List;
import java.util.Optional;

public class RimborsoHome extends BulkHome {
	public RimborsoHome(java.sql.Connection conn) {
		super(RimborsoBulk.class,conn);
	}
	public RimborsoHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(RimborsoBulk.class,conn,persistentCache);
	}
	public Optional<RimborsoBulk> findByAnticipo(AnticipoBulk anticipo) throws PersistencyException, ApplicationException {
		SQLBuilder sql = this.createSQLBuilder();
		sql.addClause(FindClause.AND, "esercizio_anticipo", SQLBuilder.EQUALS, anticipo.getEsercizio());
		sql.addClause(FindClause.AND, "cd_cds_anticipo", SQLBuilder.EQUALS, anticipo.getCd_cds());
		sql.addClause(FindClause.AND, "cd_uo_anticipo", SQLBuilder.EQUALS, anticipo.getCd_unita_organizzativa());
		sql.addClause(FindClause.AND, "pg_anticipo", SQLBuilder.EQUALS, anticipo.getPg_anticipo());

		List result = fetchAll(sql);
		if (result.size() > 1)
			throw new ApplicationException("Attenzione esiste piu' di un rimborso associato all'anticipo "+anticipo.getEsercizio()+"/"+anticipo.getCd_cds()+"/"+anticipo.getPg_anticipo());
		return result.stream().findAny();
	}
}
