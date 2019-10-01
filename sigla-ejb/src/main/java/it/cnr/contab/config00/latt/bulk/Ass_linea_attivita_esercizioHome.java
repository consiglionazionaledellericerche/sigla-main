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

import it.cnr.jada.bulk.BulkHome;

public class Ass_linea_attivita_esercizioHome extends BulkHome {
	private static final long serialVersionUID = 1L;

	public Ass_linea_attivita_esercizioHome(java.sql.Connection conn) {
		super(Ass_linea_attivita_esercizioBulk.class,conn);
	}

	public Ass_linea_attivita_esercizioHome(java.sql.Connection conn,it.cnr.jada.persistency.PersistentCache persistentCache) {
		super(Ass_linea_attivita_esercizioBulk.class,conn,persistentCache);
	}
}
