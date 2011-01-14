/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 27/09/2006
 */
package it.cnr.contab.pdg00.cdip.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Stipendi_cofi_cori_dettHome extends BulkHome {
	public Stipendi_cofi_cori_dettHome(Connection conn) {
		super(Stipendi_cofi_cori_dettBulk.class, conn);
	}
	public Stipendi_cofi_cori_dettHome(Connection conn, PersistentCache persistentCache) {
		super(Stipendi_cofi_cori_dettBulk.class, conn, persistentCache);
	}
}