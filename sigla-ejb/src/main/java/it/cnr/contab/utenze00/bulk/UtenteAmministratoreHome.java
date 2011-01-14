package it.cnr.contab.utenze00.bulk;

import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.sql.*;

public class UtenteAmministratoreHome extends UtenteHome {
public UtenteAmministratoreHome(java.sql.Connection conn) {
		super(UtenteAmministratoreBulk.class,conn);
}
public UtenteAmministratoreHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(UtenteAmministratoreBulk.class,conn,persistentCache);
}
/**
 * Restituisce il SQLBuilder per selezionare fra tutti gli Utenti quelli di tipo amministratore
 * @return SQLBuilder 
 */

public SQLBuilder createSQLBuilder()
{
	SQLBuilder sql = super.createSQLBuilder();
	sql.addClause("AND", "ti_utente", SQLBuilder.EQUALS, TIPO_AMMINISTRATORE);
	return sql; 
}
/**
 * Recupera la lista di accessi (AccessoBulk) associabili ad un amministratore
 * @return List lista di AccessoBulk
 */
public java.util.List findAccessi() throws IntrospectionException,PersistencyException 
{
	AccessoBulk accesso = new AccessoBulk();
	accesso.setTi_accesso( accesso.TIPO_AMMIN_UTENZE );
	return getHomeCache().getHome( accesso.getClass() ).find( accesso );
}
}
