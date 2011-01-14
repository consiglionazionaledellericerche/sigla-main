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