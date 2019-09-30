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
