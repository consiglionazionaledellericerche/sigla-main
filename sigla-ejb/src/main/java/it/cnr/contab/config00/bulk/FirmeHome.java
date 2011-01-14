package it.cnr.contab.config00.bulk;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;

/**
 * Creation date: (24/02/2005)
 * @author Tilde
 * @version 1.0
 */
public class FirmeHome extends BulkHome {
	/**
	 * Costruisce un FirmeHome
	 *
	 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
	 */
	public FirmeHome(java.sql.Connection conn) {
		super(FirmeBulk.class, conn);
	}
	/**
	 * Costruisce un FirmeHome
	 *
	 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
	 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
	 */
	public FirmeHome(
		java.sql.Connection conn,
		PersistentCache persistentCache) {
		super(FirmeBulk.class, conn, persistentCache);
	}
}
