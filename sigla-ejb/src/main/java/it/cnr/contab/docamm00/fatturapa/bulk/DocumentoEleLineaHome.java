/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 25/02/2015
 */
package it.cnr.contab.docamm00.fatturapa.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class DocumentoEleLineaHome extends BulkHome {
	public DocumentoEleLineaHome(Connection conn) {
		super(DocumentoEleLineaBulk.class, conn);
	}
	public DocumentoEleLineaHome(Connection conn, PersistentCache persistentCache) {
		super(DocumentoEleLineaBulk.class, conn, persistentCache);
	}
}