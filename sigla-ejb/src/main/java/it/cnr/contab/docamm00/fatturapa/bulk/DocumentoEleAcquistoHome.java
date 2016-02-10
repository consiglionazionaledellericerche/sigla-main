/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 23/02/2015
 */
package it.cnr.contab.docamm00.fatturapa.bulk;
import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class DocumentoEleAcquistoHome extends BulkHome {
	public DocumentoEleAcquistoHome(Connection conn) {
		super(DocumentoEleAcquistoBulk.class, conn);
	}
	public DocumentoEleAcquistoHome(Connection conn, PersistentCache persistentCache) {
		super(DocumentoEleAcquistoBulk.class, conn, persistentCache);
	}
}