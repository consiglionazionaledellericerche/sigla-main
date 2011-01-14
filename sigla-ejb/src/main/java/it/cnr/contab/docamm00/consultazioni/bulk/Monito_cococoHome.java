/*
* Created by Generator 1.0
* Date 30/08/2005
*/
package it.cnr.contab.docamm00.consultazioni.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.BusyResourceException;
import it.cnr.jada.bulk.OutdatedResourceException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
public class Monito_cococoHome extends BulkHome {
	public Monito_cococoHome(java.sql.Connection conn) {
		super(Monito_cococoBulk.class, conn);
	}
	public Monito_cococoHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Monito_cococoBulk.class, conn, persistentCache);
	}
	public void lock(Monito_cococoBulk bulk) throws PersistencyException, OutdatedResourceException, BusyResourceException{
			//super.lock(bulk);
	}
}