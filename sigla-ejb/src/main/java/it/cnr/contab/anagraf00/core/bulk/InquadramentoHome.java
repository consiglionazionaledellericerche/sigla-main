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

package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.contab.consultazioni.bulk.ConsultazioniRestHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.sql.*;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

public class InquadramentoHome extends BulkHome implements ConsultazioniRestHome {
	public InquadramentoHome(java.sql.Connection conn) {
		super(InquadramentoBulk.class,conn);
	}
	public InquadramentoHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(InquadramentoBulk.class,conn,persistentCache);
	}

	@Override
    public SQLBuilder restSelect(UserContext userContext, SQLBuilder sql, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws ComponentException, PersistencyException {
		final Stream<SimpleFindClause> clauses = Optional.ofNullable(compoundfindclause)
				.map(compoundFindClause -> compoundFindClause.getClauses())
				.map(enumeration -> Collections.list(enumeration).stream())
				.map(stream -> stream
						.filter(SimpleFindClause.class::isInstance)
						.map(SimpleFindClause.class::cast)
				).orElse(Stream.empty());
		if (!clauses.filter(clause ->
				clause.getPropertyName() != null &&
				clause.getPropertyName().equals("cd_anag") &&
				clause.getOperator() == SQLBuilder.EQUALS)
				.findAny().isPresent())
			throw new ComponentException("Non e' possibile richiamare il servizio REST degli inquadramenti senza la condizione del codice anagrafico.");
		return sql;
	}
}
