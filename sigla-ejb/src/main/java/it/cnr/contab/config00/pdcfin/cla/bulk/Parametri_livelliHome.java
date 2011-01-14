/*
* Created by Generator 1.0
* Date 30/08/2005
*/
package it.cnr.contab.config00.pdcfin.cla.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Parametri_livelliHome extends BulkHome {
	private static int LIVELLO_MAX = 7; 

	public Parametri_livelliHome(java.sql.Connection conn) {
		super(Parametri_livelliBulk.class, conn);
	}
	public Parametri_livelliHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Parametri_livelliBulk.class, conn, persistentCache);
	}
}