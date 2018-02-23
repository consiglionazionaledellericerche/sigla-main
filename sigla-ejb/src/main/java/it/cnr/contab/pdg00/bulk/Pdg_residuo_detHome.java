/*
* Created by Generator 1.0
* Date 30/05/2005
*/
package it.cnr.contab.pdg00.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
public class Pdg_residuo_detHome extends BulkHome {

	protected Pdg_residuo_detHome(Class clazz,java.sql.Connection connection) {
		super(clazz,connection);
	}
	protected Pdg_residuo_detHome(Class clazz,java.sql.Connection connection,PersistentCache persistentCache) {
		super(clazz,connection,persistentCache);
	}

	public Pdg_residuo_detHome(java.sql.Connection conn) {
		super(Pdg_residuo_detBulk.class, conn);
	}
	public Pdg_residuo_detHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Pdg_residuo_detBulk.class, conn, persistentCache);
	}

	/**
	 * Questo metodo fa in modo di gestire l'inizializzazione della chiave primaria
	 * in automatico, ma si limita ad inizializzare il pg_dettaglio in quanto il resto 
	 * della chiave è stato già valorizzato dal trasporto di chiave dal padre
	 */
	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk bulk) throws PersistencyException, ComponentException {
		Integer max = (Integer)findMax(bulk, "pg_dettaglio", null);
		if (max!=null)
			((Pdg_residuo_detBulk)bulk).setPg_dettaglio(new Integer(max.intValue()+1));
		else
			((Pdg_residuo_detBulk)bulk).setPg_dettaglio(new Integer(1));
	}
}