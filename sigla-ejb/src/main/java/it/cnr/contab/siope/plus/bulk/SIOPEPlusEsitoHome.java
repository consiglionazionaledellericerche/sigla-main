/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 24/04/2019
 */
package it.cnr.contab.siope.plus.bulk;
import java.sql.Connection;
import java.util.Optional;

import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.util.IdHome;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.BusyResourceException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
public class SIOPEPlusEsitoHome extends IdHome<SIOPEPlusEsitoKey> {
	public SIOPEPlusEsitoHome(Connection conn) {
		super(SIOPEPlusEsitoBulk.class, conn);
	}
	public SIOPEPlusEsitoHome(Connection conn, PersistentCache persistentCache) {
		super(SIOPEPlusEsitoBulk.class, conn, persistentCache);
	}

}