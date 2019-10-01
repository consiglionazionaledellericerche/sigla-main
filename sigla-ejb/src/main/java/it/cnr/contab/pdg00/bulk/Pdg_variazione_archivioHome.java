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
* Created by Generator 1.0
* Date 21/07/2005
*/
package it.cnr.contab.pdg00.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
public class Pdg_variazione_archivioHome extends BulkHome {
	public Pdg_variazione_archivioHome(java.sql.Connection conn) {
		super(Pdg_variazione_archivioBulk.class, conn);
	}
	public Pdg_variazione_archivioHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Pdg_variazione_archivioBulk.class, conn, persistentCache);
	}
	/**
	 * Inizializza il modello per l'inserimento impostando il progressivo ottenuto come max progressivo + 1 o 1
	 * nel caso non ci siano consultazioni archiviate per pdg
	 *
	 * @param pdg_archive dettaglio di archivio
	 */
	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk pdg_archive) throws PersistencyException {
		try {
			((Pdg_variazione_archivioBulk)pdg_archive).setProgressivo_riga(
				new Long(
					((Long)findAndLockMax( pdg_archive, "progressivo_riga", new Long(0) )).longValue()+1
				)
			);
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}
	}

}