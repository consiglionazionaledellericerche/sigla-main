/*
* Created by Generator 1.0
* Date 21/04/2006
*/
package it.cnr.contab.pdg01.bulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
public class Pdg_variazione_riga_entrata_gestHome extends BulkHome {
	public Pdg_variazione_riga_entrata_gestHome(java.sql.Connection conn) {
		super(Pdg_variazione_riga_entrata_gestBulk.class, conn);
	}
	public Pdg_variazione_riga_entrata_gestHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Pdg_variazione_riga_entrata_gestBulk.class, conn, persistentCache);
	}
	public void initializePrimaryKeyForInsert(UserContext usercontext,OggettoBulk oggettobulk)throws PersistencyException, ComponentException {
		try {
			((Pdg_variazione_riga_gestBulk)oggettobulk).setPg_riga(
				new Integer(
					((Integer)findAndLockMax( oggettobulk, "pg_riga", new Integer(0) )).intValue()+1
				)
			);
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}		
		super.initializePrimaryKeyForInsert(usercontext, oggettobulk);
	}
}