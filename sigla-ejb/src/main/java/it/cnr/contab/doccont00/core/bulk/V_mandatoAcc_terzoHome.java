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

/**
 * Insert the type's description here.
 * Creation date: (10/12/2002 15.02.53)
 * @author: Ilaria Gorla
 */
public class V_mandatoAcc_terzoHome extends MandatoAccreditamentoHome {
/**
 * V_manadato_terzoHome constructor comment.
 * @param conn java.sql.Connection
 */
public V_mandatoAcc_terzoHome(java.sql.Connection conn) {
	super(V_mandatoAcc_terzoBulk.class, conn);
}
/**
 * V_manadato_terzoHome constructor comment.
 * @param conn java.sql.Connection
 * @param persistentCache it.cnr.jada.persistency.PersistentCache
 */
public V_mandatoAcc_terzoHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(V_mandatoAcc_terzoBulk.class, conn, persistentCache);
}
}
