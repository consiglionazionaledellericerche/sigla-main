/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 11/01/2010
 */
package it.cnr.contab.segnalazioni00.bulk;
import java.sql.Connection;
import java.util.Collection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
public class Stampa_confronto_sigla_dwhHome extends BulkHome {
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: CONFRONTO_SIGLA_DWH
	 **/
	public Stampa_confronto_sigla_dwhHome(Connection conn) {
		super(ConfrontoSiglaDwhBulk.class, conn);
	}
	public Stampa_confronto_sigla_dwhHome(Connection conn, PersistentCache persistentCache) {
		super(Stampa_confronto_sigla_dwhBulk.class, conn, persistentCache);
	}
	
 
public Collection findDt_elaborazioni(Stampa_confronto_sigla_dwhBulk bulk, ConfrontoSiglaDwhHome h, ConfrontoSiglaDwhBulk clause) throws PersistencyException, IntrospectionException {

			return h.findDt_elaborazioni(bulk);
	}
	
	
}