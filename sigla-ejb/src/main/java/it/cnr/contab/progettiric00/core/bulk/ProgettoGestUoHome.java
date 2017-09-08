package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.ObjectNotFoundException;
import it.cnr.jada.persistency.PersistentCache;

public class ProgettoGestUoHome extends BulkHome {
	public ProgettoGestUoHome(java.sql.Connection conn) {
		super(ProgettoGestUoBulk.class,conn);
	}
	public ProgettoGestUoHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(ProgettoGestUoBulk.class,conn,persistentCache);
	}
	protected ProgettoGestUoHome(Class class1, java.sql.Connection connection, PersistentCache persistentcache)
	{
		super(class1, connection, persistentcache);
	}
	@Override
	public void handleObjectNotFoundException(ObjectNotFoundException objectnotfoundexception) throws ObjectNotFoundException {
	}
}