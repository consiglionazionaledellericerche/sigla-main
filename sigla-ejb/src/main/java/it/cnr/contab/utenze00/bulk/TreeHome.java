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

package it.cnr.contab.utenze00.bulk;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.util.List;

public class TreeHome extends BulkHome {
    /**
     * <!-- @TODO: da completare -->
     * Costruisce un Albero_mainHome
     *
     * @param conn La java.sql.Connection su cui vengono effettuate le operazione di persistenza
     */
    public TreeHome(java.sql.Connection conn) {
        super(TreeBulk.class, conn);
    }

    /**
     * <!-- @TODO: da completare -->
     * Costruisce un Albero_mainHome
     *
     * @param conn            La java.sql.Connection su cui vengono effettuate le operazione di persistenza
     * @param persistentCache La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
     */
    public TreeHome(java.sql.Connection conn, PersistentCache persistentCache) {
        super(TreeBulk.class, conn, persistentCache);
    }

    public List<TreeBulk> findAlberoMainByAccessi(UserContext userContext, List<String> accessi) throws PersistencyException {
        SQLBuilder sqlBuilder = createSQLBuilder();
        sqlBuilder.addTableToHeader("ACCESSO");
        sqlBuilder.addSQLJoin("ALBERO_MAIN.CD_ACCESSO", "ACCESSO.CD_ACCESSO");
        sqlBuilder.addTableToHeader("ALBERO_MAIN", "NODO_PADRE");
        sqlBuilder.addSQLJoin("ALBERO_MAIN.CD_NODO_PADRE", "NODO_PADRE.CD_NODO");

        sqlBuilder.openParenthesis(FindClause.AND);
        accessi.stream().forEach(accesso -> {
            sqlBuilder.addClause(FindClause.OR, "cdAccesso", SQLBuilder.EQUALS, accesso);
        });
        sqlBuilder.closeParenthesis();

        final List result = fetchAll(sqlBuilder);
        getHomeCache().fetchAll(userContext);
        return result;
    }
}
