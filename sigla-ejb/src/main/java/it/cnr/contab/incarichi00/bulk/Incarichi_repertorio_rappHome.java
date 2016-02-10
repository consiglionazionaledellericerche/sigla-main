package it.cnr.contab.incarichi00.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.sql.Connection;
public class Incarichi_repertorio_rappHome extends BulkHome {
	public Incarichi_repertorio_rappHome(Connection conn) {
		super(Incarichi_repertorio_rappBulk.class, conn);
	}
	public Incarichi_repertorio_rappHome(Connection conn, PersistentCache persistentCache) {
		super(Incarichi_repertorio_rappBulk.class, conn, persistentCache);
	}
	/**
	 * Inizializza il modello per l'inserimento impostando il progressivo ottenuto come max progressivo + 1
	 *
	 * @param incarico_archive dettaglio di archivio
	 */
	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk incaricorap_archive) throws PersistencyException {
		try {
			((Incarichi_repertorio_rappBulk)incaricorap_archive).setProgressivo_riga(
				new Long(
					((Long)findAndLockMax( incaricorap_archive, "progressivo_riga", new Long(0) )).longValue()+1
				)
			);
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}
	}
}