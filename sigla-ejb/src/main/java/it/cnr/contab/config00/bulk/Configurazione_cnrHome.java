package it.cnr.contab.config00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Configurazione_cnrHome extends BulkHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Configurazione_cnrHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public Configurazione_cnrHome(java.sql.Connection conn) {
	super(Configurazione_cnrBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Configurazione_cnrHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public Configurazione_cnrHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Configurazione_cnrBulk.class,conn,persistentCache);
}

public java.util.List findTipoVariazioniPdg() throws PersistencyException{

	SQLBuilder sql = createSQLBuilder();

	sql.addClause("AND","esercizio",sql.EQUALS,new Integer(0));
	sql.addClause("AND","cd_unita_funzionale",sql.EQUALS,Configurazione_cnrBulk.PK_PDG_VARIAZIONE);
	sql.addClause("AND","cd_chiave_primaria",sql.EQUALS,Configurazione_cnrBulk.SK_TIPO_VAR_APPROVA_CDS);

	return fetchAll(sql);
}
public java.util.List findTipoVariazioniStanz_res() throws PersistencyException{

	SQLBuilder sql = createSQLBuilder();

	sql.addClause("AND","esercizio",sql.EQUALS,new Integer(0));
	sql.addClause("AND","cd_unita_funzionale",sql.EQUALS,Configurazione_cnrBulk.PK_VAR_STANZ_RES);
	sql.addClause("AND","cd_chiave_primaria",sql.EQUALS,Configurazione_cnrBulk.SK_TIPO_VAR_APPROVA_CDS);

	return fetchAll(sql);
}
public java.util.List findTipoVariazioniEnteStanz_res() throws PersistencyException{

	SQLBuilder sql = createSQLBuilder();

	sql.addClause("AND","esercizio",sql.EQUALS,new Integer(0));
	sql.addClause("AND","cd_unita_funzionale",sql.EQUALS,Configurazione_cnrBulk.PK_VAR_STANZ_RES);
	sql.addClause("AND","cd_chiave_primaria",sql.EQUALS,Configurazione_cnrBulk.SK_TIPO_VAR_APPROVA_CNR);

	return fetchAll(sql);
}

}
