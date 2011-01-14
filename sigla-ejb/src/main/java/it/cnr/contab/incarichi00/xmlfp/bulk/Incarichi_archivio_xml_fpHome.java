/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 26/07/2007
 */
package it.cnr.contab.incarichi00.xmlfp.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;

import java.sql.Connection;
public class Incarichi_archivio_xml_fpHome extends BulkHome {
	public Incarichi_archivio_xml_fpHome(Connection conn) {
		super(Incarichi_archivio_xml_fpBulk.class, conn);
	}
	public Incarichi_archivio_xml_fpHome(Connection conn, PersistentCache persistentCache) {
		super(Incarichi_archivio_xml_fpBulk.class, conn, persistentCache);
	}
	
	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext, OggettoBulk bulk) throws PersistencyException {
		try {
			((Incarichi_archivio_xml_fpBulk)bulk).setId_archivio(
					new Integer(
					((Integer)findAndLockMax( bulk, "id_archivio", new Integer(0) )).intValue()+1
				)
			);
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}
	}
}
