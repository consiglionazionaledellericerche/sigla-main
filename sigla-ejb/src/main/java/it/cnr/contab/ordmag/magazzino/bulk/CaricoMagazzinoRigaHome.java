/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 21/09/2017
 */
package it.cnr.contab.ordmag.magazzino.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;

public class CaricoMagazzinoRigaHome extends BulkHome {
	private static final long serialVersionUID = 1L;

	public CaricoMagazzinoRigaHome(Connection conn) {
		super(CaricoMagazzinoRigaBulk.class, conn);
	}
	
	public CaricoMagazzinoRigaHome(Connection conn, PersistentCache persistentCache) {
		super(CaricoMagazzinoRigaBulk.class, conn, persistentCache);
	}
}