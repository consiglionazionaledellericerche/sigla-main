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

import it.cnr.jada.UserContext;
import it.cnr.jada.persistency.PersistencyException;

import java.util.Collection;

/**
 * Insert the type's description here.
 * Creation date: (26/03/2003 10.28.22)
 * @author: Gennaro Borriello
 */
public class Stampa_avviso_pag_mandHome extends MandatoHome {
	/**
	 * Stampa_avviso_pag_mandHome constructor comment.
	 * @param clazz java.lang.Class
	 * @param conn java.sql.Connection
	 */
	public Stampa_avviso_pag_mandHome(Class clazz, java.sql.Connection conn) {
		super(clazz, conn);
	}
	/**
	 * Stampa_avviso_pag_mandHome constructor comment.
	 * @param clazz java.lang.Class
	 * @param conn java.sql.Connection
	 * @param persistentCache it.cnr.jada.persistency.PersistentCache
	 */
	public Stampa_avviso_pag_mandHome(Class clazz, java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
		super(clazz, conn, persistentCache);
	}
	/**
	 * Stampa_avviso_pag_mandHome constructor comment.
	 * @param conn java.sql.Connection
	 */
	public Stampa_avviso_pag_mandHome(java.sql.Connection conn) {
		super(conn);
	}
	/**
	 * Stampa_avviso_pag_mandHome constructor comment.
	 * @param conn java.sql.Connection
	 * @param persistentCache it.cnr.jada.persistency.PersistentCache
	 */
	public Stampa_avviso_pag_mandHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
		super(conn, persistentCache);
	}

	public java.util.Collection findMandato_riga(it.cnr.jada.UserContext userContext,MandatoBulk mandato) throws it.cnr.jada.persistency.PersistencyException {
		return null;
	}

	@Override
	public Collection findMandato_riga(UserContext userContext, MandatoBulk mandato, boolean fetchAll) throws PersistencyException {
		return null;
	}
	
	public Mandato_terzoBulk findMandato_terzo(it.cnr.jada.UserContext userContext,MandatoBulk mandato) throws it.cnr.jada.persistency.PersistencyException {
		return null;
	}

	@Override
	public Mandato_terzoBulk findMandato_terzo(UserContext userContext, MandatoBulk mandato, boolean fetchAll) throws PersistencyException {
		return null;
	}
}
