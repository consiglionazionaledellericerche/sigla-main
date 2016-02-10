/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 27/11/2006
 */
package it.cnr.contab.progettiric00.geco.bulk;
import java.sql.Connection;
import java.sql.Timestamp;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;

public class Geco_attivitaHome extends BulkHome {
	private static final long serialVersionUID = 1L;

	public Geco_attivitaHome(Connection conn) {
		super(Geco_attivitaBulk.class, conn);
	}
	public Geco_attivitaHome(Connection conn, PersistentCache persistentCache) {
		super(Geco_attivitaBulk.class, conn, persistentCache);
	}

	@Override
	public Timestamp getServerTimestamp() throws PersistencyException {
		return new Timestamp(new java.util.Date().getTime());
	}
	@Override
	public Timestamp getServerDate() throws PersistencyException {
		return new Timestamp(new java.util.Date().getTime());
	}	
}