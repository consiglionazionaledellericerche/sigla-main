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
 * Created by Aurelio's BulkGenerator 1.0
 * Date 20/05/2009
 */
package it.cnr.contab.segnalazioni00.bulk;
import java.sql.Connection;
import java.util.Collection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class Attivita_siglaHome extends BulkHome {
	public Attivita_siglaHome(Connection conn) {
		super(Attivita_siglaBulk.class, conn);
	}
	public Attivita_siglaHome(Connection conn, PersistentCache persistentCache) {
		super(Attivita_siglaBulk.class, conn, persistentCache);
	}
	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk bulk) throws PersistencyException {
		try {
			Attivita_siglaBulk oggetto=(Attivita_siglaBulk)bulk;
				oggetto.setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
				oggetto.setPg_attivita(
					new Long(
						((Long)findAndLockMax( oggetto, "pg_attivita", new Long(0) )).longValue()+1
					)
				);
			} catch(it.cnr.jada.bulk.BusyResourceException e) {
				throw new PersistencyException(e);
			}
   	}

}