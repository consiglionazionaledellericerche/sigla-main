package it.cnr.contab.prevent00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Bilancio_preventivoHome extends BulkHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Bilancio_preventivoHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public Bilancio_preventivoHome(java.sql.Connection conn) {
	super(Bilancio_preventivoBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Bilancio_preventivoHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public Bilancio_preventivoHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Bilancio_preventivoBulk.class,conn,persistentCache);
}
/**
 * Effettua la ricerca dell'ENTE valido nell'esercizio specificato come parametro
 *
 *
 * @param bilancio istanza del bulk del bilancio preventivo
 * @return una lista contenente l'ENTE
 */

public java.util.List cercaCdsEnte(Bilancio_preventivoBulk bilancio) throws PersistencyException, IntrospectionException
{

	PersistentHome enteHome = getHomeCache().getHome(it.cnr.contab.config00.sto.bulk.EnteBulk.class, "V_CDS_VALIDO");
	SQLBuilder sql = enteHome.createSQLBuilder();
	
	sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS, bilancio.getEsercizio());

	return enteHome.fetchAll(sql);
}
}
