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

package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_obbligazioneHome extends BulkHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un V_obbligazioneHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public V_obbligazioneHome(java.sql.Connection conn) {
	super(V_obbligazioneBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un V_obbligazioneHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public V_obbligazioneHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(V_obbligazioneBulk.class,conn,persistentCache);
}
/**
 * Ritorna un SQLBuilder con la columnMap del ricevente
 */
public SQLBuilder createSQLBuilder() 
{
	SQLBuilder sql = super.createSQLBuilder();
	//join su OBBLIGAZIONE per recuperare il TIPO DOC CONTABILE
	sql.addSQLClause( "AND", "V_obbligazione.FL_PGIRO", sql.EQUALS, "N");	
	return sql;
}
}
