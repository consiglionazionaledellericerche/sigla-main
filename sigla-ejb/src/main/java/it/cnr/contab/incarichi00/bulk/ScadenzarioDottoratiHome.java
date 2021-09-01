/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 03/06/2021
 */
package it.cnr.contab.incarichi00.bulk;
import java.sql.Connection;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
public class ScadenzarioDottoratiHome extends BulkHome {
	private static final long serialVersionUID = 1L;

	public ScadenzarioDottoratiHome(Connection conn) {
		super(ScadenzarioDottoratiBulk.class, conn);
	}
	public ScadenzarioDottoratiHome(Connection conn, PersistentCache persistentCache) {
		super(ScadenzarioDottoratiBulk.class, conn, persistentCache);
	}

	@Override
	public void initializePrimaryKeyForInsert(UserContext usercontext, OggettoBulk oggettobulk)throws PersistencyException, ComponentException {
		try {
			ScadenzarioDottoratiBulk scadenzarioDottorati = (ScadenzarioDottoratiBulk) oggettobulk;
			scadenzarioDottorati.setId(
					new Long(((Long)findAndLockMax( oggettobulk, "id", new Long(0) )).longValue()+1));
			super.initializePrimaryKeyForInsert(usercontext, scadenzarioDottorati);
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}
	}
}