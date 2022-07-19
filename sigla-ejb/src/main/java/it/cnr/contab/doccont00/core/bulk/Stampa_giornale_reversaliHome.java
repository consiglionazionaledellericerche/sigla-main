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
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;

import java.util.Collection;

/**
 * Insert the type's description here.
 * Creation date: (20/01/2003 16.56.33)
 * @author: Simonetta Costa
 */
public class Stampa_giornale_reversaliHome extends ReversaleHome {
	/**
	 * Stampa_giornale_reversaliHome constructor comment.
	 * @param clazz java.lang.Class
	 * @param conn java.sql.Connection
	 */
	public Stampa_giornale_reversaliHome(Class clazz, java.sql.Connection conn) {
		super(clazz, conn);
	}
	/**
	 * Stampa_giornale_reversaliHome constructor comment.
	 * @param clazz java.lang.Class
	 * @param conn java.sql.Connection
	 * @param persistentCache it.cnr.jada.persistency.PersistentCache
	 */
	public Stampa_giornale_reversaliHome(Class clazz, java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
		super(clazz, conn, persistentCache);
	}
	/**
	 * Stampa_giornale_reversaliHome constructor comment.
	 * @param conn java.sql.Connection
	 */
	public Stampa_giornale_reversaliHome(java.sql.Connection conn) {
		super(Stampa_giornale_reversaliBulk.class, conn);
	}
	/**
	 * Stampa_giornale_reversaliHome constructor comment.
	 * @param conn java.sql.Connection
	 * @param persistentCache it.cnr.jada.persistency.PersistentCache
	 */
	public Stampa_giornale_reversaliHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
		super(Stampa_giornale_reversaliBulk.class, conn, persistentCache);
	}
	/**
	 * <!-- @TODO: da completare -->
	 *
	 *
	 * @param reversale
	 * @return
	 * @throws PersistencyException
	 * @throws IntrospectionException
	 */
	public java.util.Collection findReversale_riga(it.cnr.jada.UserContext userContext,ReversaleBulk reversale) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException {
		return null;
	}

	@Override
	public Collection findReversale_riga(UserContext userContext, ReversaleBulk reversale, boolean fetchAll) throws PersistencyException, IntrospectionException {
		return null;
	}

	/**
	 * <!-- @TODO: da completare -->
	 *
	 *
	 * @param reversale
	 * @return
	 * @throws PersistencyException
	 * @throws IntrospectionException
	 */
	public Reversale_terzoBulk findReversale_terzo(it.cnr.jada.UserContext userContext,ReversaleBulk reversale) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException {
		return null;
	}

	@Override
	public Reversale_terzoBulk findReversale_terzo(UserContext userContext, ReversaleBulk reversale, boolean fetchAll) throws PersistencyException, IntrospectionException {
		return null;
	}

	/**
	 * Carica la reversale <reversale> con tutti gli oggetti complessi
	 *
	 * @param reversale
	 * @return
	 * @throws PersistencyException
	 */
	public ReversaleBulk loadReversale(it.cnr.jada.UserContext userContext,String cdCds, Integer esercizio, Long pgReversale) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException {
		return null;
	}
}
