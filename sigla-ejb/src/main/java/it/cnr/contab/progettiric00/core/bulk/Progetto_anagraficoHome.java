/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/08/2021
 */
package it.cnr.contab.progettiric00.core.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
public class Progetto_anagraficoHome extends BulkHome {
	public Progetto_anagraficoHome(Connection conn) {
		super(Progetto_anagraficoBulk.class, conn);
	}
	public Progetto_anagraficoHome(Connection conn, PersistentCache persistentCache) {
		super(Progetto_anagraficoBulk.class, conn, persistentCache);
	}



	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext, OggettoBulk bulk) throws PersistencyException, it.cnr.jada.comp.ComponentException {
		try {

			((Progetto_anagraficoBulk)bulk).setIdPrgAnagrafico(
					new Long(
							((Long)findAndLockMax( bulk, "idPrgAnagrafico", new Long(0) )).longValue()+1
					)
			);
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new it.cnr.jada.comp.ApplicationException("Operazione effettuata al momento da un'altro utente, riprovare successivamente.");
		}
	}
}