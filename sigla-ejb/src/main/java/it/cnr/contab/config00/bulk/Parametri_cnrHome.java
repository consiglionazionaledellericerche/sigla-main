package it.cnr.contab.config00.bulk;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;

/**
 * Creation date: (09/11/2004)
 * @author Aurelio D'Amico
 * @version 1.0
 */
public class Parametri_cnrHome extends BulkHome {
	/**
	 * Costruisce un Configurazione_cnrHome
	 *
	 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
	 */
	public Parametri_cnrHome(java.sql.Connection conn) {
		super(Parametri_cnrBulk.class, conn);
	}
	/**
	 * Costruisce un Configurazione_cnrHome
	 *
	 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
	 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
	 */
	public Parametri_cnrHome(
		java.sql.Connection conn,
		PersistentCache persistentCache) {
		super(Parametri_cnrBulk.class, conn, persistentCache);
	}
}
