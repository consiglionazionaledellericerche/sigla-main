/*
* Created by Generator 1.0
* Date 14/09/2005
*/
package it.cnr.contab.prevent00.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Pdg_piano_ripartoHome extends BulkHome {
	public final static String STATO_PROVVISORIO = "P" ;
	public final static String STATO_DEFINITIVO = "D" ;
	
	public Pdg_piano_ripartoHome(java.sql.Connection conn) {
		super(Pdg_piano_ripartoBulk.class, conn);
	}
	public Pdg_piano_ripartoHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Pdg_piano_ripartoBulk.class, conn, persistentCache);
	}
}
