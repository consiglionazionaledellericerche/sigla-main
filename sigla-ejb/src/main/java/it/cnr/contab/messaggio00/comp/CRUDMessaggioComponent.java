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

package it.cnr.contab.messaggio00.comp;

import it.cnr.contab.messaggio00.bulk.MessaggioBulk;
import it.cnr.contab.messaggio00.bulk.MessaggioHome;
import it.cnr.contab.messaggio00.bulk.Messaggio_visionatoBulk;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.sql.SQLException;
import java.util.List;

/**
 * Insert the type's description here.
 * Creation date: (04/09/2002 13:17:51)
 *
 * @author: CNRADM
 */
public class CRUDMessaggioComponent extends it.cnr.jada.comp.CRUDComponent implements ICRUDMessaggioMgr {
    /**
     * CRUDMessaggioComponent constructor comment.
     */
    public CRUDMessaggioComponent() {
        super();
    }

    public void setMessaggioVisionato(UserContext userContext, MessaggioBulk messaggio) throws ComponentException {
        try {
            String user = it.cnr.contab.utenze00.bp.CNRUserContext.getUser(userContext);
            try {
                Messaggio_visionatoBulk messaggio_visionato = new Messaggio_visionatoBulk(user, messaggio.getPg_messaggio());
                messaggio_visionato.setUser(user);
                insertBulk(userContext, messaggio_visionato);
            } catch (it.cnr.jada.persistency.sql.DuplicateKeyException e) {
            }
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(e);
        }
    }

    public boolean isMessaggioVisionato(UserContext userContext, MessaggioBulk messaggio) throws ComponentException {
        try {
            MessaggioHome home = (MessaggioHome) getHome(userContext, messaggio);
            return home.isMessaggioVisionato(userContext, messaggio);
        } catch (SQLException e) {
            throw handleException(e);
        }
    }

    public void deleteMessaggi(UserContext userContext) throws ComponentException {
		MessaggioHome home = (MessaggioHome) getHome(userContext, MessaggioBulk.class);
		try {
			final List<MessaggioBulk> list = home.fetchAll(home.selectJobsToDelete());
			list
					.stream()
					.forEach(o -> {
						try {
							home.deleteRiga(o);
						} catch (PersistencyException e) {
							throw new DetailedRuntimeException(e);
						}
					});
		} catch (PersistencyException e) {
			throw handleException(e);
		}
	}
}
