package it.cnr.contab.pdg00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Pdg_preventivo_etr_detHome extends BulkHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Pdg_preventivo_etr_detHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public Pdg_preventivo_etr_detHome(java.sql.Connection conn) {
	super(Pdg_preventivo_etr_detBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Pdg_preventivo_etr_detHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public Pdg_preventivo_etr_detHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Pdg_preventivo_etr_detBulk.class,conn,persistentCache);
}
	/**
	 * Inizializza il modello per l'inserimento impostando il progressivo ottenuto come max progressivo + 1 o 1
	 * nel caso non ci siano dettagli di entrata PDG
	 *
	 * @param det_etr dettaglio di entrata PDG
	 */

	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk det_etr) throws PersistencyException {
		try {
			((Pdg_preventivo_etr_detBulk)det_etr).setPg_entrata(
				new Long(
					((Long)findAndLockMax( det_etr, "pg_entrata", new Long(0) )).longValue()+1
				)
			);
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}
	}

}
