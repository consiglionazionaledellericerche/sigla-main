package it.cnr.contab.compensi00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Minicarriera_rataHome extends BulkHome {
public Minicarriera_rataHome(Class clazz, java.sql.Connection conn) {
	super(clazz, conn);
}
public Minicarriera_rataHome(Class clazz, java.sql.Connection conn,PersistentCache persistentCache) {
	super(clazz, conn, persistentCache);
}
public Minicarriera_rataHome(java.sql.Connection conn) {
	super(Minicarriera_rataBulk.class,conn);
}
public Minicarriera_rataHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Minicarriera_rataBulk.class,conn,persistentCache);
}
/**
 * Metodo per la restituzione dell'SQL atta alla ricerca delle rate di una 
 * minicarriera data
 */
 
public java.util.List findRate(CompensoBulk compenso) throws PersistencyException{

	SQLBuilder sql = createSQLBuilder();
	sql.addSQLClause("AND","CD_CDS_COMPENSO",sql.EQUALS, compenso.getCd_cds());
	sql.addSQLClause("AND","CD_UO_COMPENSO",sql.EQUALS, compenso.getCd_unita_organizzativa());
	sql.addSQLClause("AND","ESERCIZIO_COMPENSO",sql.EQUALS, compenso.getEsercizio());
	sql.addSQLClause("AND","PG_COMPENSO",sql.EQUALS, compenso.getPg_compenso());

	return fetchAll(sql);
}
}
