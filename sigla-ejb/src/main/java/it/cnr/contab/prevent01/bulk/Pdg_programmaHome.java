/*
* Creted by Generator 1.0
* Date 18/02/2005
*/
package it.cnr.contab.prevent01.bulk;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;

public class Pdg_programmaHome extends BulkHome {
	private static final long serialVersionUID = 1L;

	public Pdg_programmaHome(java.sql.Connection conn) {
		super(Pdg_programmaBulk.class, conn);
	}
	
	public Pdg_programmaHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Pdg_programmaBulk.class, conn, persistentCache);
	}
}