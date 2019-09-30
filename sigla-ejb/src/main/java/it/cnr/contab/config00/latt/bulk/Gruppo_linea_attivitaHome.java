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

package it.cnr.contab.config00.latt.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Gruppo_linea_attivitaHome extends BulkHome {
/**
 * Costrutture gruppo linea di attività Home
 *
 * @param conn connessione db
 */
public Gruppo_linea_attivitaHome(java.sql.Connection conn) {
	super(Gruppo_linea_attivitaBulk.class,conn);
}
/**
 * Costrutture gruppo linea di attività Home
 *
 * @param conn connessione db
 * @param persistentCache cache modelli
 */
public Gruppo_linea_attivitaHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Gruppo_linea_attivitaBulk.class,conn,persistentCache);
}
}
