package it.cnr.contab.bollo00.tabrif.bulk;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;

public class Tipo_atto_bolloHome extends BulkHome {
	private static final long serialVersionUID = 1L;

	public Tipo_atto_bolloHome(java.sql.Connection conn) {
		super(Tipo_atto_bolloBulk.class,conn);
	}
	public Tipo_atto_bolloHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(Tipo_atto_bolloBulk.class,conn,persistentCache);
	}

	@Override
	public void initializePrimaryKeyForInsert(UserContext  usercontext,OggettoBulk oggettobulk)throws PersistencyException, ComponentException {
		try {
			Tipo_atto_bolloBulk tipoAtto = (Tipo_atto_bolloBulk)oggettobulk;
			tipoAtto.setId(
					new Integer(((Integer)findAndLockMax( oggettobulk, "id", new Integer(0) )).intValue()+1));
			super.initializePrimaryKeyForInsert(usercontext, tipoAtto);
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}
	}
}
