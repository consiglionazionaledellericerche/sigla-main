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

package it.cnr.contab.config00.pdcep.bulk;

import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.sql.*;

/**
 * Home che gestisce i capoconti.
 */
public class CapocontoHome extends Voce_epHome {
public CapocontoHome(java.sql.Connection conn) {
	super(CapocontoBulk.class,conn);
}
public CapocontoHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(CapocontoBulk.class,conn,persistentCache);
}
/**
 * Restituisce il SQLBuilder per selezionare fra tutte le Voci Economico Patrimoniali quelle relative
 * ai Capoconti.
 * @return SQLBuilder 
 */
public SQLBuilder createSQLBuilder( )	
{
	SQLBuilder sql = super.createSQLBuilder();
	sql.addClause("AND", "ti_voce_ep", SQLBuilder.EQUALS, TIPO_CAPOCONTO);
	return sql; 
}
}
