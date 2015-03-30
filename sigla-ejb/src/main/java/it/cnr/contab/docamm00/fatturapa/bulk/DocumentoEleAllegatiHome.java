/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 23/02/2015
 */
package it.cnr.contab.docamm00.fatturapa.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class DocumentoEleAllegatiHome extends BulkHome {
	public DocumentoEleAllegatiHome(Connection conn) {
		super(DocumentoEleAllegatiBulk.class, conn);
	}
	public DocumentoEleAllegatiHome(Connection conn, PersistentCache persistentCache) {
		super(DocumentoEleAllegatiBulk.class, conn, persistentCache);
	}
}