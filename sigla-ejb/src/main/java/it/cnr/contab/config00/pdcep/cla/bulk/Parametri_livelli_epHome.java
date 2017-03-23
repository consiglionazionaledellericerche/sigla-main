/*
* Created by Generator 1.0
* Date 30/08/2005
*/
package it.cnr.contab.config00.pdcep.cla.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Parametri_livelli_epHome extends BulkHome {
	private static int LIVELLO_MAX = 8; 

	public Parametri_livelli_epHome(java.sql.Connection conn) {
		super(Parametri_livelli_epBulk.class, conn);
	}
	public Parametri_livelli_epHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Parametri_livelli_epBulk.class, conn, persistentCache);
	}
}