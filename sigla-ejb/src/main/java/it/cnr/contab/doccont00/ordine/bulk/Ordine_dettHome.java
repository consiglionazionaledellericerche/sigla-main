package it.cnr.contab.doccont00.ordine.bulk;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class Ordine_dettHome extends BulkHome {
public Ordine_dettHome(java.sql.Connection conn) {
	super(Ordine_dettBulk.class,conn);
}
public Ordine_dettHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Ordine_dettBulk.class,conn,persistentCache);
}
/**
 * Insert the method's description here.
 * Creation date: (01/02/2002 12.08.03)
 * @return it.cnr.jada.bulk.BulkList
 * @param ordine it.cnr.contab.doccont00.ordine.bulk.OrdineBulk
 */
public java.util.List findDetailsFor(OrdineBulk ordine) throws PersistencyException{

	SQLBuilder sql = createSQLBuilder();

	sql.addClause("AND","esercizio",sql.EQUALS,ordine.getEsercizio());
	sql.addClause("AND","cd_cds",sql.EQUALS,ordine.getCd_cds());
	sql.addClause("AND","pg_ordine",sql.EQUALS,ordine.getPg_ordine());

	return fetchAll(sql);
}
public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk bulk) throws PersistencyException,it.cnr.jada.comp.ComponentException {
	
	try{
		Ordine_dettBulk dett = (Ordine_dettBulk)bulk;

		long pg = ((Long)findAndLockMax(dett,"pg_dettaglio",new Long(0))).longValue() + 1;
		dett.setPg_dettaglio(new Long(pg));

	} catch(it.cnr.jada.bulk.BusyResourceException e) {
		throw new it.cnr.jada.persistency.PersistencyException(e);
	}
}
}
