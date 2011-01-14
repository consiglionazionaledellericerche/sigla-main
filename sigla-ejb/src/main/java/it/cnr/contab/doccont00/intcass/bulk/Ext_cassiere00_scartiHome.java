/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 18/01/2008
 */
package it.cnr.contab.doccont00.intcass.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class Ext_cassiere00_scartiHome extends BulkHome {
	public Ext_cassiere00_scartiHome(Connection conn) {
		super(Ext_cassiere00_scartiBulk.class, conn);
	}
	public Ext_cassiere00_scartiHome(Connection conn, PersistentCache persistentCache) {
		super(Ext_cassiere00_scartiBulk.class, conn, persistentCache);
	}
}