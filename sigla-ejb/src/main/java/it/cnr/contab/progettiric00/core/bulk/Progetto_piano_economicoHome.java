package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;

public class Progetto_piano_economicoHome extends BulkHome {

	public Progetto_piano_economicoHome(java.sql.Connection conn) {
		super(Progetto_piano_economicoBulk.class,conn);
	}
	
	public Progetto_piano_economicoHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(Progetto_piano_economicoBulk.class,conn,persistentCache);
	}

}
