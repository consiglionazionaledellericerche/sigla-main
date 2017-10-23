/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 03/10/2017
 */
package it.cnr.contab.ordmag.magazzino.bulk;
import java.sql.Connection;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
public class MovimentiMagHome extends BulkHome {
	public MovimentiMagHome(Connection conn) {
		super(MovimentiMagBulk.class, conn);
	}
	public MovimentiMagHome(Connection conn, PersistentCache persistentCache) {
		super(MovimentiMagBulk.class, conn, persistentCache);
	}
	public Long recuperoProgressivoMovimento(it.cnr.jada.UserContext userContext) throws PersistencyException,it.cnr.jada.comp.ComponentException {
		return new Long(this.fetchNextSequenceValue(userContext,"CNRSEQ00_MOVIMENTI_MAG").longValue());
	}
	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk bulk) throws PersistencyException,it.cnr.jada.comp.ComponentException {
		MovimentiMagBulk movimento = (MovimentiMagBulk)bulk;
		if (movimento.getPgMovimento() == null)
			movimento.setPgMovimento(recuperoProgressivoMovimento(userContext));
	}
}