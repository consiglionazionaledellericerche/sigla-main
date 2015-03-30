/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 23/02/2015
 */
package it.cnr.contab.docamm00.fatturapa.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class DocumentoEleIvaHome extends BulkHome {
	public DocumentoEleIvaHome(Connection conn) {
		super(DocumentoEleIvaBulk.class, conn);
	}
	public DocumentoEleIvaHome(Connection conn, PersistentCache persistentCache) {
		super(DocumentoEleIvaBulk.class, conn, persistentCache);
	}
}