package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.contab.compensi00.docs.bulk.EstrazioniFiscaliVBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Dichiarazione_intentoHome extends BulkHome {
	public Dichiarazione_intentoHome(java.sql.Connection conn) {
		super(Dichiarazione_intentoBulk.class,conn);
	}
	public Dichiarazione_intentoHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(Dichiarazione_intentoBulk.class,conn,persistentCache);
	}
	
	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,it.cnr.jada.bulk.OggettoBulk esportatore) throws it.cnr.jada.persistency.PersistencyException {
		try {
			((Dichiarazione_intentoBulk) esportatore).setProgr(
				new Integer(
					((Integer)findAndLockMax( esportatore, "progr", new Integer(0))).intValue()+1
				)
			);
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}
	}
	
}
