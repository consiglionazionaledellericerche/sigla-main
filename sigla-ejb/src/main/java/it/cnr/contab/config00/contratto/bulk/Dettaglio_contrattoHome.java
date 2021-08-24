/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 19/08/2021
 */
package it.cnr.contab.config00.contratto.bulk;
import java.sql.Connection;

import it.cnr.contab.ordmag.magazzino.bulk.MovimentiMagBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
public class Dettaglio_contrattoHome extends BulkHome {
	public Dettaglio_contrattoHome(Connection conn) {
		super(Dettaglio_contrattoBulk.class, conn);
	}
	public Dettaglio_contrattoHome(Connection conn, PersistentCache persistentCache) {
		super(Dettaglio_contrattoBulk.class, conn, persistentCache);
	}
	Long recuperoProgressivoDettaglio(UserContext userContext) throws PersistencyException,it.cnr.jada.comp.ComponentException {
		return new Long(this.fetchNextSequenceValue(userContext,"CNRSEQ00_DETTAGLIO_CONTRATTO").longValue());
	}
	public void initializePrimaryKeyForInsert(UserContext userContext, OggettoBulk bulk) throws PersistencyException,it.cnr.jada.comp.ComponentException {
		Dettaglio_contrattoBulk dettaglio = (Dettaglio_contrattoBulk)bulk;
		if (dettaglio.getId() == null)
			dettaglio.setId(recuperoProgressivoDettaglio(userContext));
	}

}