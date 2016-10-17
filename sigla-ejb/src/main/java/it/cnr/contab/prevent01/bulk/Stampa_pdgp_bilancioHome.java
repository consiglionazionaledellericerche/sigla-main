package it.cnr.contab.prevent01.bulk;

import it.cnr.jada.bulk.BulkHome;

/**
 * Insert the type's description here.
 * Creation date: (26/03/2003 10.28.22)
 * @author: Gennaro Borriello
 */
public class Stampa_pdgp_bilancioHome extends BulkHome {

	public Stampa_pdgp_bilancioHome(Class clazz, java.sql.Connection conn) {
		super(clazz, conn);
	}
	
	public Stampa_pdgp_bilancioHome(Class clazz, java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
		super(clazz, conn, persistentCache);
	}
}
