/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 10/09/2007
 */
package it.cnr.contab.incarichi00.bulk;
import java.sql.Connection;

import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.PersistentCache;
public class Incarichi_repertorio_annoHome extends BulkHome {
	public Incarichi_repertorio_annoHome(Connection conn) {
		super(Incarichi_repertorio_annoBulk.class, conn);
	}
	public Incarichi_repertorio_annoHome(Connection conn, PersistentCache persistentCache) {
		super(Incarichi_repertorio_annoBulk.class, conn, persistentCache);
	}
}