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
        SQLBuilder sqlBuilder = super.createSQLBuilder();
        sqlBuilder.resetColumns();
        sqlBuilder.addColumn("MOVIMENTO_COGE.PG_SCRITTURA");
        sqlBuilder.addColumn("SCRITTURA_PARTITA_DOPPIA.DT_CONTABILIZZAZIONE");
        sqlBuilder.addColumn("SCRITTURA_PARTITA_DOPPIA.DS_SCRITTURA");

        sqlBuilder.addColumn("MOVIMENTO_COGE.ESERCIZIO");
        sqlBuilder.addColumn("MOVIMENTO_COGE.PG_MOVIMENTO");
        sqlBuilder.addColumn("MOVIMENTO_COGE.CD_UNITA_ORGANIZZATIVA");
        sqlBuilder.addColumn("MOVIMENTO_COGE.CD_CDS");
        sqlBuilder.addColumn("'D'", "CD_RIGA");

        sqlBuilder.addColumn("MOVIMENTO_COGE.CD_VOCE_EP");
        sqlBuilder.addColumn("MOVIMENTO_COGE.CD_TERZO");
        sqlBuilder.addColumn("MOVIMENTO_COGE.TI_RIGA");
        sqlBuilder.addColumn("MOVIMENTO_COGE.CD_CONTRIBUTO_RITENUTA");

        sqlBuilder.addColumn("MOVIMENTO_COGE.ESERCIZIO_DOCUMENTO");
        sqlBuilder.addColumn("MOVIMENTO_COGE.CD_TIPO_DOCUMENTO");
        sqlBuilder.addColumn("MOVIMENTO_COGE.CD_CDS_DOCUMENTO");
        sqlBuilder.addColumn("MOVIMENTO_COGE.CD_UO_DOCUMENTO");
        sqlBuilder.addColumn("MOVIMENTO_COGE.PG_NUMERO_DOCUMENTO");

        sqlBuilder.addColumn(sqlBuilder.addDecode("SEZIONE","'D'", "IM_MOVIMENTO",null,null, BigDecimal.ZERO),"IM_MOVIMENTO_DARE");
        sqlBuilder.addColumn(sqlBuilder.addDecode("SEZIONE","'A'", "IM_MOVIMENTO",null,null, BigDecimal.ZERO),"IM_MOVIMENTO_AVERE");
        sqlBuilder.addColumn("NULL", "DIFFERENZA");
        Optional.ofNullable(compoundfindclause)
                        .ifPresent(compoundFindClause -> sqlBuilder.addClause(compoundFindClause));

        final SQLBuilder sqlBuilderGroupBy = createSQLBuilder();
        sqlBuilderGroupBy.resetColumns();
        sqlBuilderGroupBy.addColumn("MAX(MOVIMENTO_COGE.PG_SCRITTURA)");
        sqlBuilderGroupBy.addColumn("MAX(SCRITTURA_PARTITA_DOPPIA.DT_CONTABILIZZAZIONE)");
        sqlBuilderGroupBy.addColumn("MAX(SCRITTURA_PARTITA_DOPPIA.DS_SCRITTURA)");

        sqlBuilderGroupBy.addColumn("MAX(MOVIMENTO_COGE.ESERCIZIO)");
        sqlBuilderGroupBy.addColumn("MAX(MOVIMENTO_COGE.PG_MOVIMENTO)");
        sqlBuilderGroupBy.addColumn("MAX(MOVIMENTO_COGE.CD_UNITA_ORGANIZZATIVA)");
        sqlBuilderGroupBy.addColumn("MAX(MOVIMENTO_COGE.CD_CDS)");
        sqlBuilderGroupBy.addColumn("'T'", "CD_RIGA");

        sqlBuilderGroupBy.addColumn("MAX(MOVIMENTO_COGE.CD_VOCE_EP)");
        sqlBuilderGroupBy.addColumn("MAX(MOVIMENTO_COGE.CD_TERZO)");
        sqlBuilderGroupBy.addColumn("MAX('SALDO')", "TI_RIGA");
        sqlBuilderGroupBy.addColumn("MAX(MOVIMENTO_COGE.CD_CONTRIBUTO_RITENUTA)");

        sqlBuilderGroupBy.addColumn("MOVIMENTO_COGE.ESERCIZIO_DOCUMENTO");
        sqlBuilderGroupBy.addColumn("MOVIMENTO_COGE.CD_TIPO_DOCUMENTO");
        sqlBuilderGroupBy.addColumn("MOVIMENTO_COGE.CD_CDS_DOCUMENTO");
        sqlBuilderGroupBy.addColumn("MOVIMENTO_COGE.CD_UO_DOCUMENTO");
        sqlBuilderGroupBy.addColumn("MOVIMENTO_COGE.PG_NUMERO_DOCUMENTO");

        sqlBuilderGroupBy.addColumn("NULL","IM_MOVIMENTO_DARE");
        sqlBuilderGroupBy.addColumn("NULL","IM_MOVIMENTO_AVERE");
        sqlBuilderGroupBy.addColumn("SUM(".concat(sqlBuilderGroupBy.addDecode("SEZIONE", "'D'", "NVL(IM_MOVIMENTO,0)", null, null, "NVL(-IM_MOVIMENTO,0))")),"DIFFERENZA");

        Optional.ofNullable(compoundfindclause)
                .ifPresent(compoundFindClause -> sqlBuilderGroupBy.addClause(compoundFindClause));
        sqlBuilderGroupBy.addSQLGroupBy(
                "MOVIMENTO_COGE.ESERCIZIO_DOCUMENTO," +
                        "MOVIMENTO_COGE.CD_TIPO_DOCUMENTO, " +
                        "MOVIMENTO_COGE.CD_CDS_DOCUMENTO," +
                        "MOVIMENTO_COGE.CD_UO_DOCUMENTO," +
                        "MOVIMENTO_COGE.PG_NUMERO_DOCUMENTO");
        final SQLUnion union = sqlBuilder.union(sqlBuilderGroupBy, true);
        int orderIndex = 0;
        union.setOrderBy("esercizio_documento", orderIndex++);
        union.setOrderBy("cd_cds_documento", orderIndex++);
        union.setOrderBy("cd_uo_documento", orderIndex++);
        union.setOrderBy("pg_numero_documento", orderIndex++);
        union.setOrderBy("cd_riga", orderIndex++);
        union.setOrderBy("pg_scrittura", orderIndex++);
        return union;
    }
}
