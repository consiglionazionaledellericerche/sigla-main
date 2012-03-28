/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 27/11/2006
 */
package it.cnr.contab.progettiric00.geco.bulk;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.util.ejb.EJBCommonServices;
public class Geco_commessa_psHome extends BulkHome {
	public Geco_commessa_psHome(Connection conn) {
		super(Geco_commessa_psBulk.class, conn);
	}
	public Geco_commessa_psHome(Connection conn, PersistentCache persistentCache) {
		super(Geco_commessa_psBulk.class, conn, persistentCache);
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