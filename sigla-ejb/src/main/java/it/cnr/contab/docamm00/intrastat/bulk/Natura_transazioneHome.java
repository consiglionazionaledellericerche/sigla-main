package it.cnr.contab.docamm00.intrastat.bulk;

import java.util.List;

import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Natura_transazioneHome extends BulkHome {
public Natura_transazioneHome(java.sql.Connection conn) {
	super(Natura_transazioneBulk.class,conn);
}
public Natura_transazioneHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Natura_transazioneBulk.class,conn,persistentCache);
}
public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext, OggettoBulk bulk) throws PersistencyException {
	try {
		((Natura_transazioneBulk)bulk).setId_natura_transazione(
				new Long(
				((Long)findAndLockMax( bulk, "id_natura_transazione", new Long(0) )).longValue()+1
			)
		);
	} catch(it.cnr.jada.bulk.BusyResourceException e) {
		throw new PersistencyException(e);
	}
}
public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause) throws PersistencyException {
	SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
    sql.addClause("AND","esercizio",SQLBuilder.EQUALS, CNRUserContext.getEsercizio(usercontext));
	return sql;
}	
}
