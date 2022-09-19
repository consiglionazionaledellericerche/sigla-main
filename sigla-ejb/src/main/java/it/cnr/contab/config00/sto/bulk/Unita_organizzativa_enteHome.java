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

package it.cnr.contab.config00.sto.bulk;

import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.SQLBuilder;

/**
 * <!-- @TODO: da completare -->
 */

public class Unita_organizzativa_enteHome extends it.cnr.jada.bulk.BulkHome {
    /**
     * <!-- @TODO: da completare -->
     * Costruisce un Unita_organizzativa_enteHome
     *
     * @param conn La java.sql.Connection su cui vengono effettuate le operazione di persistenza
     */
    public Unita_organizzativa_enteHome(java.sql.Connection conn) {
        super(Unita_organizzativa_enteBulk.class, conn);
    }

    /**
     * <!-- @TODO: da completare -->
     * Costruisce un Unita_organizzativa_enteHome
     *
     * @param conn            La java.sql.Connection su cui vengono effettuate le operazione di persistenza
     * @param persistentCache La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
     */
    public Unita_organizzativa_enteHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
        super(Unita_organizzativa_enteBulk.class, conn, persistentCache);
    }

    /**
     * Restituisce il SQLBuilder per selezionare fra tutte le Unita Organizzative
     * quella di tipo ENTE
     *
     * @return SQLBuilder
     */

    public SQLBuilder createSQLBuilder() {
        SQLBuilder sql = super.createSQLBuilder();
        sql.addClause("AND", "fl_cds", SQLBuilder.EQUALS, Boolean.FALSE);
        sql.addClause("AND", "cd_tipo_unita", SQLBuilder.EQUALS, Tipo_unita_organizzativaHome.TIPO_UO_ENTE);
        return sql;
    }

    public Boolean isUoEnte(UserContext userContext) throws ComponentException {
        Unita_organizzativa_enteBulk uoEnte = getUoEnte(userContext);
		return ((CNRUserContext) userContext).getCd_unita_organizzativa().equals(uoEnte.getCd_unita_organizzativa());
	}

    public Unita_organizzativa_enteBulk getUoEnte(UserContext userContext) throws ComponentException {
        try {
            return (Unita_organizzativa_enteBulk) getHomeCache().getHome(Unita_organizzativa_enteBulk.class).findAll().get(0);
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw new ComponentException(e);
        }
    }
}