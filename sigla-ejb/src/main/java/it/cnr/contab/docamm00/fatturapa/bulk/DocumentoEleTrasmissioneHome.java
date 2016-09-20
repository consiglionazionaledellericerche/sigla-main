/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 25/02/2015
 */
package it.cnr.contab.docamm00.fatturapa.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class DocumentoEleTrasmissioneHome extends BulkHome {
	public DocumentoEleTrasmissioneHome(Connection conn) {
		super(DocumentoEleTrasmissioneBulk.class, conn);
	}
	public DocumentoEleTrasmissioneHome(Connection conn, PersistentCache persistentCache) {
		super(DocumentoEleTrasmissioneBulk.class, conn, persistentCache);
	}	
}