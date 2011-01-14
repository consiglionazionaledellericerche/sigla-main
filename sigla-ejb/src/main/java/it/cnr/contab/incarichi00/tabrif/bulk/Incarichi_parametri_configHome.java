/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 03/04/2008
 */
package it.cnr.contab.incarichi00.tabrif.bulk;

import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;

public class Incarichi_parametri_configHome extends BulkHome {
	public Incarichi_parametri_configHome(Connection conn) {
		super(Incarichi_parametri_configBulk.class, conn);
	}
	public Incarichi_parametri_configHome(Connection conn, PersistentCache persistentCache) {
		super(Incarichi_parametri_configBulk.class, conn, persistentCache);
	}
	public Incarichi_parametri_configHome(Class clazz, java.sql.Connection conn) {
		super(clazz,conn);
	}
	public Incarichi_parametri_configHome(Class clazz, java.sql.Connection conn,PersistentCache persistentCache) {
		super(clazz,conn,persistentCache);
	}
}