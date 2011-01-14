package it.cnr.contab.prevent00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Voce_f_saldi_cmpHome extends BulkHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Voce_f_saldi_cmpHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public Voce_f_saldi_cmpHome(java.sql.Connection conn) {
	super(Voce_f_saldi_cmpBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Voce_f_saldi_cmpHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public Voce_f_saldi_cmpHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Voce_f_saldi_cmpBulk.class,conn,persistentCache);
}
/**
 * Cerca il CDS ENTE
 *
 * @param dettaglio	saldo dettaglio
 * @return Lista contenente il CDS ENTE
 * @throws PersistencyException	
 * @throws IntrospectionException	
 */
public java.util.List cercaCdsEnte(Voce_f_saldi_cmpBulk dettaglio) throws PersistencyException, IntrospectionException
{
	PersistentHome enteHome = getHomeCache().getHome(it.cnr.contab.config00.sto.bulk.EnteBulk.class, "V_CDS_VALIDO");
	SQLBuilder sql = enteHome.createSQLBuilder();
	
	sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS, dettaglio.getEsercizio());
	
	return enteHome.fetchAll(sql);
}
public SQLBuilder createSQLBuilder() {
	SQLBuilder sql = super.createSQLBuilder();
	sql.addClause("AND","ti_competenza_residuo", sql.EQUALS, "C");
	return sql;
}
}
