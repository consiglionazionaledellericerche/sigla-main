/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 22/03/2021
 */
package it.cnr.contab.incarichi00.bulk;
import java.sql.Connection;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
public class Phdtipo_dottoratiHome extends BulkHome {
	private static final long serialVersionUID = 1L;

	public Phdtipo_dottoratiHome(Connection conn) {
		super(Phdtipo_dottoratiBulk.class, conn);
	}
	public Phdtipo_dottoratiHome(Connection conn, PersistentCache persistentCache) {
		super(Phdtipo_dottoratiBulk.class, conn, persistentCache);
	}

	@Override
	public void initializePrimaryKeyForInsert(UserContext usercontext, OggettoBulk oggettobulk)throws PersistencyException, ComponentException {
		try {
			Phdtipo_dottoratiBulk phdtipoDottorati = (Phdtipo_dottoratiBulk) oggettobulk;
			phdtipoDottorati.setId(
					new Long(((Long)findAndLockMax( oggettobulk, "id", new Long(0) )).longValue()+1));
			super.initializePrimaryKeyForInsert(usercontext, phdtipoDottorati);
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}
	}
}