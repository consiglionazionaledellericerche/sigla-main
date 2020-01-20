/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class AbilUtenteUopOperMagHome extends BulkHome {
	private static final long serialVersionUID = 1L;

	public AbilUtenteUopOperMagHome(Connection conn) {
		super(AbilUtenteUopOperMagBulk.class, conn);
	}
	public AbilUtenteUopOperMagHome(Connection conn, PersistentCache persistentCache) {
		super(AbilUtenteUopOperMagBulk.class, conn, persistentCache);
	}
}