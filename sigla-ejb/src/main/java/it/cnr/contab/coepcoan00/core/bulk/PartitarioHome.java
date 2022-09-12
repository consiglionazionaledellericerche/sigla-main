/*
 * Copyright (C) 2022  Consiglio Nazionale delle Ricerche
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

package it.cnr.contab.coepcoan00.core.bulk;

import it.cnr.jada.UserContext;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.persistency.sql.SQLUnion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Optional;

public class PartitarioHome extends Movimento_cogeHome{

    public PartitarioHome(Connection conn) {
        super(PartitarioBulk.class, conn);
    }

    public PartitarioHome(Connection conn, PersistentCache persistentCache) {
        super(PartitarioBulk.class, conn, persistentCache);
    }

    public SQLBuilder selectByClauseForPartitario(UserContext usercontext, PartitarioBulk partitarioBulk, CompoundFindClause compoundfindclause) throws PersistencyException {
        setColumnMap("PARTITARIO");
        SQLBuilder sqlBuilder = super.createSQLBuilderWithoutJoin();
        Optional.ofNullable(compoundfindclause)
                        .ifPresent(compoundFindClause -> sqlBuilder.addClause(compoundFindClause));
        sqlBuilder.addOrderBy("cd_tipo_documento");
        sqlBuilder.addOrderBy("esercizio_documento");
        sqlBuilder.addOrderBy("cd_cds_documento");
        sqlBuilder.addOrderBy("cd_uo_documento");
        sqlBuilder.addOrderBy("pg_numero_documento");
        sqlBuilder.addOrderBy("cd_contributo_ritenuta");
        sqlBuilder.addOrderBy("cd_riga");
        sqlBuilder.addOrderBy("pg_scrittura");
        return sqlBuilder;
    }
}
