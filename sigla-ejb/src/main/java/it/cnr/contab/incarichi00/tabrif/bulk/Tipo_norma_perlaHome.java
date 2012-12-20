/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 18/07/2007
 */
package it.cnr.contab.incarichi00.tabrif.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;

import java.sql.Connection;
public class Tipo_norma_perlaHome extends BulkHome {
	public Tipo_norma_perlaHome(Connection conn) {
		super(Tipo_norma_perlaBulk.class, conn);
	}
	public Tipo_norma_perlaHome(Connection conn, PersistentCache persistentCache) {
		super(Tipo_norma_perlaBulk.class, conn, persistentCache);
	}
}