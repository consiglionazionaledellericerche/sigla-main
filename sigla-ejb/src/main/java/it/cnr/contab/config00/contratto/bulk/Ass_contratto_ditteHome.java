/*
* Creted by Generator 1.0
* Date 13/04/2005
*/
package it.cnr.contab.config00.contratto.bulk;
import java.sql.SQLException;

import it.cnr.contab.doccont00.core.bulk.AccertamentoBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.prevent01.bulk.Pdg_modulo_speseBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.persistency.sql.SQLUnion;
public class Ass_contratto_ditteHome extends BulkHome {
	public Ass_contratto_ditteHome(java.sql.Connection conn) {
		super(Ass_contratto_ditteBulk.class, conn);
	}
	public Ass_contratto_ditteHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Ass_contratto_ditteBulk.class, conn, persistentCache);
	}
	public void initializePrimaryKeyForInsert(UserContext  usercontext,OggettoBulk oggettobulk)throws PersistencyException, ComponentException {
		try {
			Ass_contratto_ditteBulk ass = (Ass_contratto_ditteBulk)oggettobulk;
			ass.setPg_dettaglio(
					new Integer(((Integer)findAndLockMax( oggettobulk, "pg_dettaglio", new Integer(0) )).intValue()+1));
			super.initializePrimaryKeyForInsert(usercontext, ass);
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}
	} 
	
}