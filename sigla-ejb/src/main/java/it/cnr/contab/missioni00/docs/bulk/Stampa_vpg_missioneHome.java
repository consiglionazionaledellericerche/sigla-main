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

package it.cnr.contab.missioni00.docs.bulk;

/**
 * Insert the type's description here.
 * Creation date: (13/02/2003 10.08.41)
 * @author: Roberto Fantino
 */
public class Stampa_vpg_missioneHome extends MissioneHome {
/**
 * Stampa_vpg_missioneHome constructor comment.
 * @param conn java.sql.Connection
 */
public Stampa_vpg_missioneHome(Class aClass, java.sql.Connection conn) {
	super(aClass, conn);
}
/**
 * Stampa_vpg_missioneHome constructor comment.
 * @param conn java.sql.Connection
 * @param persistentCache it.cnr.jada.persistency.PersistentCache
 */
public Stampa_vpg_missioneHome(Class aClass, java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(aClass, conn, persistentCache);
}
/**
 * Stampa_vpg_missioneHome constructor comment.
 * @param conn java.sql.Connection
 */
public Stampa_vpg_missioneHome(java.sql.Connection conn) {
	super(Stampa_vpg_missioneBulk.class, conn);
}
/**
 * Stampa_vpg_missioneHome constructor comment.
 * @param conn java.sql.Connection
 * @param persistentCache it.cnr.jada.persistency.PersistentCache
 */
public Stampa_vpg_missioneHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(Stampa_vpg_missioneBulk.class, conn, persistentCache);
}
}
