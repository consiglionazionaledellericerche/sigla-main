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

public class Utente_unita_ruoloHome extends BulkHome {
    /**
     * <!-- @TODO: da completare -->
     * Costruisce un Utente_unita_ruoloHome
     *
     * @param conn La java.sql.Connection su cui vengono effettuate le operazione di persistenza
     */
    public Utente_unita_ruoloHome(java.sql.Connection conn) {
        super(Utente_unita_ruoloBulk.class, conn);
    }

    /**
     * <!-- @TODO: da completare -->
     * Costruisce un Utente_unita_ruoloHome
     *
     * @param conn            La java.sql.Connection su cui vengono effettuate le operazione di persistenza
     * @param persistentCache La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
     */
    public Utente_unita_ruoloHome(java.sql.Connection conn, PersistentCache persistentCache) {
        super(Utente_unita_ruoloBulk.class, conn, persistentCache);
    }

	public List<Utente_unita_ruoloBulk> findRuoliByCdUtente(UserContext userContext, String cdUtente, String cdUnitaOrganizzativa) throws PersistencyException {
		SQLBuilder sqlBuilder = createSQLBuilder();
		sqlBuilder.addClause(FindClause.AND, "cd_utente", SQLBuilder.EQUALS, cdUtente);
		sqlBuilder.addClause(FindClause.AND, "cd_unita_organizzativa", SQLBuilder.EQUALS, cdUnitaOrganizzativa);
		return fetchAll(sqlBuilder);
	}
}
