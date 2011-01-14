package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;

public class Progetto_esercizioHome extends ProgettoHome {
	public Progetto_esercizioHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(Progetto_esercizioBulk.class,conn,persistentCache);
	}
	protected Progetto_esercizioHome(Class class1, java.sql.Connection connection, PersistentCache persistentcache)
	{
		super(class1, connection, persistentcache);
	}
}
