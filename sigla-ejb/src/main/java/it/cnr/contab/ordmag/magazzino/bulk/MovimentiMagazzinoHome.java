/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 21/09/2017
 */
package it.cnr.contab.ordmag.magazzino.bulk;
import java.sql.Connection;

import it.cnr.jada.persistency.PersistentCache;

public class MovimentiMagazzinoHome extends AbilitazioneMagazzinoHome {
	private static final long serialVersionUID = 1L;

	public MovimentiMagazzinoHome(Class clazz, Connection conn) {
		super(clazz, conn);
	}
	
	public MovimentiMagazzinoHome(Class clazz, Connection conn, PersistentCache persistentCache) {
		super(clazz, conn, persistentCache);
	}
}