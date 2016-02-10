/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 24/12/2015
 */
package it.cnr.contab.doccont00.intcass.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;

import java.sql.Connection;
public class DistintaCassiere1210Home extends BulkHome {

	private static final long serialVersionUID = 1L;
	public DistintaCassiere1210Home(Connection conn) {
		super(DistintaCassiere1210Bulk.class, conn);
	}
	public DistintaCassiere1210Home(Connection conn, PersistentCache persistentCache) {
		super(DistintaCassiere1210Bulk.class, conn, persistentCache);
	}	
}