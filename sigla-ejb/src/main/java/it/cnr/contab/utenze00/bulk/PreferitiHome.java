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
 * Created by BulkGenerator 1.5 [30/07/2008]
 * Date 17/10/2008
 */
package it.cnr.contab.utenze00.bulk;
import java.sql.Connection;
import java.util.Optional;

import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class PreferitiHome extends BulkHome {
	public PreferitiHome(Connection conn) {
		super(PreferitiBulk.class, conn);
	}
	public PreferitiHome(Connection conn, PersistentCache persistentCache) {
		super(PreferitiBulk.class, conn, persistentCache);
	}
	@Override
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause) throws PersistencyException {
	    SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
        sql.addClause(FindClause.AND, "cd_utente", SQLBuilder.EQUALS, usercontext.getUser());
        sql.addOrderBy("DUVA");
		return sql;
	}

    public SQLBuilder selectAssBpAccessoBulkByClause(UserContext usercontext,
                                                     PreferitiBulk preferiti,
                                                     AssBpAccessoHome assBpAccessoHome,
                                                     AssBpAccessoBulk assBpAccessoBulk,
                                                     CompoundFindClause compoundfindclause) {
	    SQLBuilder sqlBuilder = getHomeCache().getHome(AssBpAccessoBulk.class, "PREFERITI").createSQLBuilder();
        sqlBuilder.addClause(compoundfindclause);
        sqlBuilder.addTableToHeader("V_UTENTE_ACCESSO");
        sqlBuilder.addSQLJoin("ASS_BP_ACCESSO.CD_ACCESSO", "V_UTENTE_ACCESSO.CD_ACCESSO");
        sqlBuilder.addSQLClause(FindClause.AND, "V_UTENTE_ACCESSO.CD_UTENTE", SQLBuilder.EQUALS,
                CNRUserContext.getUser(usercontext));

        sqlBuilder.openParenthesis(FindClause.AND);
        sqlBuilder.addSQLClause(FindClause.OR,"V_UTENTE_ACCESSO.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS,
                CNRUserContext.getCd_unita_organizzativa(usercontext));
        sqlBuilder.addSQLClause(FindClause.OR,"V_UTENTE_ACCESSO.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS,
                "*");
        sqlBuilder.closeParenthesis();
        sqlBuilder.openParenthesis(FindClause.AND);
        sqlBuilder.addSQLClause(FindClause.OR,"V_UTENTE_ACCESSO.ESERCIZIO", SQLBuilder.EQUALS,
                CNRUserContext.getEsercizio(usercontext));
        sqlBuilder.addSQLClause(FindClause.OR,"V_UTENTE_ACCESSO.ESERCIZIO", SQLBuilder.EQUALS, 0);
        sqlBuilder.closeParenthesis();
        sqlBuilder.addTableToHeader("ACCESSO");
        sqlBuilder.addSQLJoin("ASS_BP_ACCESSO.CD_ACCESSO", "ACCESSO.CD_ACCESSO");
        Optional.ofNullable(assBpAccessoBulk)
                .flatMap(assBpAccesso -> Optional.ofNullable(assBpAccesso.getAccesso()))
                .flatMap(accessoBase -> Optional.ofNullable(accessoBase.getDs_accesso()))
                .ifPresent(s -> sqlBuilder.addSQLClause(FindClause.AND, "ACCESSO.DS_ACCESSO", SQLBuilder.EQUALS, s));
        return sqlBuilder;
    }
}