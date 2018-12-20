/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 20/12/2018
 */
package it.cnr.contab.siope.plus.bulk;
import java.sql.Connection;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
public class SIOPEPlusRisultatoHome extends BulkHome {
	public SIOPEPlusRisultatoHome(Connection conn) {
		super(SIOPEPlusRisultatoBulk.class, conn);
	}
	public SIOPEPlusRisultatoHome(Connection conn, PersistentCache persistentCache) {
		super(SIOPEPlusRisultatoBulk.class, conn, persistentCache);
	}
	@Override
	public void initializePrimaryKeyForInsert(UserContext usercontext, OggettoBulk oggettobulk) throws PersistencyException, ComponentException {
		try {
			SIOPEPlusRisultatoBulk siopePlusRisultatoBulk = (SIOPEPlusRisultatoBulk)oggettobulk;
			siopePlusRisultatoBulk.setId(
					new Long(((Long)findAndLockMax( oggettobulk, "id", new Long(0) )).intValue()+1));
			super.initializePrimaryKeyForInsert(usercontext, siopePlusRisultatoBulk);
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}
	}
}