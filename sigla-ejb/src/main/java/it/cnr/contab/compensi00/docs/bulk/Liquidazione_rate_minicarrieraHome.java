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

package it.cnr.contab.compensi00.docs.bulk;

import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;

/**
 * Insert the type's description here.
 * Creation date: (24/12/2002 11.51.51)
 * @author: Roberto Fantino
 */
public class Liquidazione_rate_minicarrieraHome extends Minicarriera_rataHome {
/**
 * Liquidazione_rate_minicarrieraHome constructor comment.
 * @param conn java.sql.Connection
 */
public Liquidazione_rate_minicarrieraHome(java.sql.Connection conn) {
	super(Liquidazione_rate_minicarrieraBulk.class, conn);
}
/**
 * Liquidazione_rate_minicarrieraHome constructor comment.
 * @param conn java.sql.Connection
 * @param persistentCache it.cnr.jada.persistency.PersistentCache
 */
public Liquidazione_rate_minicarrieraHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(Liquidazione_rate_minicarrieraBulk.class, conn, persistentCache);
}
}
