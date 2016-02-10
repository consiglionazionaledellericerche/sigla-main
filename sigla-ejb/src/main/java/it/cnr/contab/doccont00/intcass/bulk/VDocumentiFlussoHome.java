/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 19/07/2013
 */
package it.cnr.contab.doccont00.intcass.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class VDocumentiFlussoHome extends BulkHome {
	public VDocumentiFlussoHome(Connection conn) {
		super(VDocumentiFlussoBulk.class, conn);
	}
	public VDocumentiFlussoHome(Connection conn, PersistentCache persistentCache) {
		super(VDocumentiFlussoBulk.class, conn, persistentCache);
	}
}