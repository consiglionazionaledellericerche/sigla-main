/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 15/09/2021
 */
package it.cnr.contab.doccont00.core.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;


public class Obbligazione_pluriennaleHome extends BulkHome {
	public Obbligazione_pluriennaleHome(Connection conn) {
		super(Obbligazione_pluriennaleBulk.class, conn);
	}
	public Obbligazione_pluriennaleHome(Connection conn, PersistentCache persistentCache) {
		super(Obbligazione_pluriennaleBulk.class, conn, persistentCache);
	}
	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext, OggettoBulk bulk) throws PersistencyException, it.cnr.jada.comp.ComponentException {
		try {

			((Obbligazione_pluriennaleBulk ) bulk).setIdObbligazionePlur(
					new Long(
							((Long)findAndLockMax( bulk, "idObbligazionePlur", new Long(0) )).longValue()+1
					)
			);
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new it.cnr.jada.comp.ApplicationException("Operazione effettuata al momento da un'altro utente, riprovare successivamente.");
		}
	}
}