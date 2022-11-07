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

/*
 * Created by BulkGenerator 1.5 [30/07/2008]
 * Date 17/10/2008
 */
package it.cnr.contab.utenze00.bulk;

import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class PreferitiAccessoHome extends BulkHome {
	public PreferitiAccessoHome(Connection conn) {
		super(PreferitiAccessoBulk.class, conn);
	}
	public PreferitiAccessoHome(Connection conn, PersistentCache persistentCache) {
		super(PreferitiAccessoBulk.class, conn, persistentCache);
	}

    public List<PreferitiAccessoBulk> findByUser(UserContext usercontext) throws PersistencyException {
        final SQLBuilder sqlBuilder = createSQLBuilder();
        sqlBuilder.addTableToHeader("ASS_BP_ACCESSO");
        sqlBuilder.addSQLJoin("PREFERITI.BUSINESS_PROCESS", "ASS_BP_ACCESSO.BUSINESS_PROCESS");
        sqlBuilder.openParenthesis(FindClause.AND);
            sqlBuilder.addSQLJoin("PREFERITI.TI_FUNZIONE", "ASS_BP_ACCESSO.TI_FUNZIONE");
            sqlBuilder.addSQLClause(FindClause.OR, "ASS_BP_ACCESSO.TI_FUNZIONE", SQLBuilder.ISNULL, null);
        sqlBuilder.closeParenthesis();
        sqlBuilder.addClause(FindClause.AND, "cd_utente", SQLBuilder.EQUALS, usercontext.getUser());
        sqlBuilder.addOrderBy("DUVA");
        return fetchAll(sqlBuilder);
    }
}
