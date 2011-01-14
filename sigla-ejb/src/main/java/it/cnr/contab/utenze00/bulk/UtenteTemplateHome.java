package it.cnr.contab.utenze00.bulk;

import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.sql.*;

public class UtenteTemplateHome extends UtenteHome {
public UtenteTemplateHome(java.sql.Connection conn) {
	super(UtenteTemplateBulk.class, conn);
}
public UtenteTemplateHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
		super(UtenteTemplateBulk.class,conn,persistentCache);
}
/**
 * Restituisce il SQLBuilder per selezionare fra tutti gli Utenti quelli di tipo comune che sono template
 * @return SQLBuilder
 */

public SQLBuilder createSQLBuilder()
{
	SQLBuilder sql = super.createSQLBuilder();
	sql.addClause("AND", "ti_utente", SQLBuilder.EQUALS, TIPO_COMUNE);
	sql.addClause("AND", "fl_utente_templ", SQLBuilder.EQUALS, new Boolean(true));		
	return sql; 
}
}
