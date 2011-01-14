/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 03/04/2008
 */
package it.cnr.contab.incarichi00.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Incarichi_procedura_annoHome extends BulkHome {
	public Incarichi_procedura_annoHome(Connection conn) {
		super(Incarichi_procedura_annoBulk.class, conn);
	}
	public Incarichi_procedura_annoHome(Connection conn, PersistentCache persistentCache) {
		super(Incarichi_procedura_annoBulk.class, conn, persistentCache);
	}
}