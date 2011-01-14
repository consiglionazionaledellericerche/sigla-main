/*
 * Created by BulkGenerator 1.5 [30/07/2008]
 * Date 31/07/2008
 */
package it.cnr.contab.incarichi00.tabrif.bulk;

import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;

public class Incarichi_parametriHome extends BulkHome {
	public Incarichi_parametriHome(Connection conn) {
		super(Incarichi_parametriBulk.class, conn);
	}
	public Incarichi_parametriHome(Connection conn, PersistentCache persistentCache) {
		super(Incarichi_parametriBulk.class, conn, persistentCache);
	}
}