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

package it.cnr.contab.fondecon00.core.bulk;

/**
 * Insert the type's description here.
 * Creation date: (06/03/2003 15.49.12)
 * @author: Gennaro Borriello
 */
public class Stampa_vpg_fondo_economaleHome extends Fondo_economaleHome {
public Stampa_vpg_fondo_economaleHome(Class aClass, java.sql.Connection conn) {
	super(aClass,conn);
}
public Stampa_vpg_fondo_economaleHome(Class aClass, java.sql.Connection conn,it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(aClass, conn, persistentCache);
}
public Stampa_vpg_fondo_economaleHome(java.sql.Connection conn) {
	super(Stampa_vpg_fondo_economaleBulk.class,conn);
}
public Stampa_vpg_fondo_economaleHome(java.sql.Connection conn,it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(Stampa_vpg_fondo_economaleBulk.class,conn,persistentCache);
}
}
