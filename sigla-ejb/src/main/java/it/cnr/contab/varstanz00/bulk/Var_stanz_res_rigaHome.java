/*
* Created by Generator 1.0
* Date 15/02/2006
*/
package it.cnr.contab.varstanz00.bulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
public class Var_stanz_res_rigaHome extends BulkHome {
	public Var_stanz_res_rigaHome(java.sql.Connection conn) {
		super(Var_stanz_res_rigaBulk.class, conn);
	}
	public Var_stanz_res_rigaHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Var_stanz_res_rigaBulk.class, conn, persistentCache);
	}
	public void initializePrimaryKeyForInsert(UserContext usercontext,OggettoBulk oggettobulk)throws PersistencyException, ComponentException {
		try {
			((Var_stanz_res_rigaBulk)oggettobulk).setPg_riga(
				new Long(
					((Long)findAndLockMax( oggettobulk, "pg_riga", new Long(0) )).longValue()+1
				)
			);
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}		
		super.initializePrimaryKeyForInsert(usercontext, oggettobulk);
	}
	
}