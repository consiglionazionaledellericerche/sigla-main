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

package it.cnr.contab.messaggio00.bulk;

import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.util.List;

public class MessaggioHome extends BulkHome {
    public MessaggioHome(java.sql.Connection conn) {
        super(MessaggioBulk.class, conn);
    }

    public MessaggioHome(java.sql.Connection conn, PersistentCache persistentCache) {
        super(MessaggioBulk.class, conn, persistentCache);
    }

    public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext, OggettoBulk bulk) throws PersistencyException, it.cnr.jada.comp.ComponentException {
        MessaggioBulk messaggio = (MessaggioBulk) bulk;
        if (messaggio.getPg_messaggio() == null)
            messaggio.setPg_messaggio(new Long(this.fetchNextSequenceValue(userContext, "CNRSEQ00_PG_MESSAGGIO").longValue()));
    }

    /**
     * Ritorna TRUE se il messaggio <messaggio> è stato visionato dall'utente,
     * collegato ed individuato tramite lo usercontext <usercontext>,
     * FALSE altrimenti
     * <p>
     * Parametri:
     * - usercontext
     * - messaggio
     **/
    public boolean isMessaggioVisionato(UserContext usercontext, MessaggioBulk messaggio) throws java.sql.SQLException {

        PersistentHome messaggio_visionatoHome = getHomeCache().getHome(Messaggio_visionatoBulk.class);
        SQLBuilder sql = messaggio_visionatoHome.createSQLBuilder();
        sql.addSQLClause("AND", "PG_MESSAGGIO", SQLBuilder.EQUALS, messaggio.getPg_messaggio());
        sql.addSQLClause("AND", "CD_UTENTE", SQLBuilder.EQUALS, CNRUserContext.getUser(usercontext));

        return sql.executeExistsQuery(getConnection());
    }

    public SQLBuilder selectJobsToDelete() {

        SQLBuilder sql = createSQLBuilder();
        sql.addTableToHeader("PARAMETRI_ENTE");
        sql.addSQLClause("AND", "PARAMETRI_ENTE.ATTIVO", SQLBuilder.EQUALS, "Y");
        sql.addSQLClause("AND", "TRUNC(SYSDATE - MESSAGGIO.DUVA) > Nvl(PARAMETRI_ENTE.CANCELLA_STAMPE,30)");
        return sql;
    }

    public void deleteRiga(MessaggioBulk bulk) throws PersistencyException {
        delete(bulk, null);
    }

	public List<MessaggioBulk> findMessaggiByUser(UserContext userContext) throws PersistencyException {
		SQLBuilder sql = createSQLBuilder();
		sql.openParenthesis(FindClause.AND);
		sql.addClause(FindClause.AND, "cd_utente", SQLBuilder.EQUALS, userContext.getUser());
		sql.addClause(FindClause.OR, "cd_utente", SQLBuilder.ISNULL, null);
		sql.closeParenthesis();
		sql.openParenthesis(FindClause.AND);
		sql.addClause(FindClause.AND, "dt_inizio_validita", SQLBuilder.LESS_EQUALS, EJBCommonServices.getServerTimestamp());
		sql.addClause(FindClause.OR, "dt_inizio_validita", SQLBuilder.ISNULL, null);
		sql.closeParenthesis();
		sql.openParenthesis(FindClause.AND);
		sql.addClause(FindClause.AND, "dt_fine_validita", SQLBuilder.GREATER_EQUALS, EJBCommonServices.getServerTimestamp());
		sql.addClause(FindClause.OR, "dt_fine_validita", SQLBuilder.ISNULL, null);
		sql.closeParenthesis();
		return fetchAll(sql);
	}
}
