package it.cnr.contab.doccont00.ordine.bulk;

import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.sql.*;

public class OrdineHome extends BulkHome {
public OrdineHome(Class clazz, java.sql.Connection conn) {
	super(clazz,conn);
}
public OrdineHome(Class clazz, java.sql.Connection conn,PersistentCache persistentCache) {
	super(clazz,conn,persistentCache);
}
public OrdineHome(java.sql.Connection conn) {
	super(OrdineBulk.class,conn);
}
public OrdineHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(OrdineBulk.class,conn,persistentCache);
}
/**
 * Insert the method's description here.
 * Creation date: (31/01/2002 15.24.19)
 * @param userContext it.cnr.jada.UserContext
 * @param obblig it.cnr.contab.doccont00.core.bulk.ObbligazioneOrdBulk
 */
public OrdineBulk findOrdineFor(ObbligazioneBulk obblig) throws PersistencyException {

	OrdineBulk ordine = null;
	SQLBuilder sql = createSQLBuilder();

	sql.addClause("AND","cd_cds",sql.EQUALS,obblig.getCd_cds());
	sql.addClause("AND","esercizio",sql.EQUALS,obblig.getEsercizio());
	sql.addClause("AND","esercizio_ori_obbligazione",sql.EQUALS,obblig.getEsercizio_originale());
	sql.addClause("AND","pg_obbligazione",sql.EQUALS,obblig.getPg_obbligazione());

	SQLBroker broker = createBroker(sql);
	if (broker.next())
		ordine = (OrdineBulk)fetch(broker);
	broker.close();

	return ordine;
}
/**
 * Insert the method's description here.
 * Creation date: (31/01/2002 15.24.19)
 * @param userContext it.cnr.jada.UserContext
 * @param obblig it.cnr.contab.doccont00.core.bulk.ObbligazioneOrdBulk
 */
public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk bulk) throws PersistencyException,it.cnr.jada.comp.ComponentException {
	
	try{
		OrdineBulk ordine = (OrdineBulk)bulk;

		long pg = ((Long)findAndLockMax(ordine,"pg_ordine", new Long(0))).longValue() + 1;
		ordine.setPg_ordine(new Long(pg));

	} catch(it.cnr.jada.bulk.BusyResourceException e) {
		throw new it.cnr.jada.persistency.PersistencyException(e);
	}
}
}
