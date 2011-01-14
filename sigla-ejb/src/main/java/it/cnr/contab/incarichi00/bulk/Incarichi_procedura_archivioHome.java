/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 03/04/2008
 */
package it.cnr.contab.incarichi00.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
public class Incarichi_procedura_archivioHome extends BulkHome {
	public Incarichi_procedura_archivioHome(Connection conn) {
		super(Incarichi_procedura_archivioBulk.class, conn);
	}
	public Incarichi_procedura_archivioHome(Connection conn, PersistentCache persistentCache) {
		super(Incarichi_procedura_archivioBulk.class, conn, persistentCache);
	}
	/**
	 * Inizializza il modello per l'inserimento impostando il progressivo ottenuto come max progressivo + 1 o 1
	 * nel caso non ci siano consultazioni archiviate per pdg
	 *
	 * @param pdg_archive dettaglio di archivio
	 */
	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk incarico_archive) throws PersistencyException {
		try {
			((Incarichi_procedura_archivioBulk)incarico_archive).setProgressivo_riga(
				new Long(
					((Long)findAndLockMax( incarico_archive, "progressivo_riga", new Long(0) )).longValue()+1
				)
			);
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}
	}
}