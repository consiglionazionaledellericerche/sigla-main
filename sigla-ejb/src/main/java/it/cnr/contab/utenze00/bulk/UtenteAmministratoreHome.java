/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
