/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 21/01/2008
 */
package it.cnr.contab.cori00.views.bulk;
import java.sql.Connection;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
public class F24ep_tempTotHome extends BulkHome {
	public F24ep_tempTotHome(Connection conn) {
		super(F24ep_tempTotBulk.class, conn);
	}
	public F24ep_tempTotHome(Connection conn, PersistentCache persistentCache) {
		super(F24ep_tempTotBulk.class, conn, persistentCache);
	}
	public void initializePrimaryKeyForInsert(UserContext usercontext,OggettoBulk oggettobulk)throws PersistencyException, it.cnr.jada.comp.ComponentException {
		F24ep_tempTotBulk F24ep_temp= (F24ep_tempTotBulk)oggettobulk;
		try {
		((F24ep_tempTotBulk)oggettobulk).setProg(
		new Long(
		((Long)findAndLockMax( F24ep_temp, "prog", new Long(0) )).longValue()+1));
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
		 throw new it.cnr.jada.comp.ApplicationException("Operazione effettuata al momento da un'altro utente, riprovare successivamente.");
		} 
		super.initializePrimaryKeyForInsert(usercontext, F24ep_temp);
		}
}