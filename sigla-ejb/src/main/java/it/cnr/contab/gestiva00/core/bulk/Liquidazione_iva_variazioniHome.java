package it.cnr.contab.gestiva00.core.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;

public class Liquidazione_iva_variazioniHome extends BulkHome {
	private static final long serialVersionUID = 1L;
	
	public Liquidazione_iva_variazioniHome(java.sql.Connection conn) {
		super(Liquidazione_iva_variazioniBulk.class,conn);
	}
	
	public Liquidazione_iva_variazioniHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(Liquidazione_iva_variazioniBulk.class,conn,persistentCache);
	}
	
	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk bulk) throws PersistencyException {
		try {
			((Liquidazione_iva_variazioniBulk)bulk).setPg_dettaglio(
				new Long(
					((Long)findAndLockMax( bulk, "pg_dettaglio", new Long(0) )).longValue()+1
				)
			);
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}
	}	
}
