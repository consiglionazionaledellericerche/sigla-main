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

import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class PartitarioHome extends Movimento_cogeHome {

    public PartitarioHome(Connection conn) {
        super(PartitarioBulk.class, conn);
    }

    public PartitarioHome(Connection conn, PersistentCache persistentCache) {
        super(PartitarioBulk.class, conn, persistentCache);
    }

    public SQLBuilder selectByClauseForPartitario(
            UserContext usercontext,
            PartitarioBulk partitarioBulk,
            CompoundFindClause compoundfindclause,
            Object... documentoAmministrativo
    ) throws PersistencyException {
        setColumnMap("PARTITARIO");
        SQLBuilder sqlBuilder = super.createSQLBuilderWithoutJoin();
        sqlBuilder.openParenthesis(FindClause.AND);
        for (Object obj : documentoAmministrativo) {
            IDocumentoAmministrativoBulk documentoAmministrativoBulk = Optional.ofNullable(obj)
                    .filter(IDocumentoAmministrativoBulk.class::isInstance)
                    .map(IDocumentoAmministrativoBulk.class::cast)
                    .orElseThrow(() -> new PersistencyException("Object is not instance of IDocumentoAmministrativoBulk.class"));
            if (documentoAmministrativoBulk != null) {
                sqlBuilder.openParenthesis(FindClause.OR);
                sqlBuilder.addClause(FindClause.AND, "cd_tipo_documento", SQLBuilder.EQUALS, documentoAmministrativoBulk.getCd_tipo_doc_amm());
                sqlBuilder.addClause(FindClause.AND, "esercizio_documento", SQLBuilder.EQUALS, documentoAmministrativoBulk.getEsercizio());
                sqlBuilder.addClause(FindClause.AND, "cd_cds_documento", SQLBuilder.EQUALS, documentoAmministrativoBulk.getCd_cds());
                sqlBuilder.addClause(FindClause.AND, "cd_uo_documento", SQLBuilder.EQUALS, documentoAmministrativoBulk.getCd_uo());
                sqlBuilder.addClause(FindClause.AND, "pg_numero_documento", SQLBuilder.EQUALS, documentoAmministrativoBulk.getPg_doc_amm());
                sqlBuilder.closeParenthesis();
            }
        }
        sqlBuilder.closeParenthesis();
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
