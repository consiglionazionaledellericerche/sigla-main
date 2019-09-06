package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;

public class Progetto_rimodulazione_voceHome extends BulkHome {

	public Progetto_rimodulazione_voceHome(java.sql.Connection conn) {
		super(Progetto_rimodulazione_voceBulk.class,conn);
	}
	
	public Progetto_rimodulazione_voceHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(Progetto_rimodulazione_voceBulk.class,conn,persistentCache);
	}
}
