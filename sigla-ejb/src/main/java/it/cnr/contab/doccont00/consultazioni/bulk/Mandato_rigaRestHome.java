package it.cnr.contab.doccont00.consultazioni.bulk;

import it.cnr.contab.rest.bulk.RestServicesHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class Mandato_rigaRestHome extends RestServicesHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Mandato_rigaHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public Mandato_rigaRestHome(java.sql.Connection conn) {
	super(Mandato_rigaRestBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Mandato_rigaHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public Mandato_rigaRestHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Mandato_rigaRestBulk.class,conn,persistentCache);
}

@Override
public SQLBuilder selectByClause(UserContext usercontext,
		CompoundFindClause compoundfindclause) throws PersistencyException {
	SQLBuilder builder = super.selectByClause(usercontext, compoundfindclause);
	return super.addConditionCds(usercontext, builder, "cd_cds");
}
}
