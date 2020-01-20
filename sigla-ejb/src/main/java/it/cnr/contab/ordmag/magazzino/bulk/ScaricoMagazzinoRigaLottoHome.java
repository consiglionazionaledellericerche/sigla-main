/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 21/09/2017
 */
package it.cnr.contab.ordmag.magazzino.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;

public class ScaricoMagazzinoRigaLottoHome extends BulkHome {
	private static final long serialVersionUID = 1L;

	public ScaricoMagazzinoRigaLottoHome(Connection conn) {
		super(ScaricoMagazzinoRigaBulk.class, conn);
	}
	
	public ScaricoMagazzinoRigaLottoHome(Connection conn, PersistentCache persistentCache) {
		super(ScaricoMagazzinoRigaBulk.class, conn, persistentCache);
	}
}