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

import java.util.*;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ass_mandato_reversaleHome extends BulkHome {
	/**
	 * <!-- @TODO: da completare -->
	 * Costruisce un Ass_mandato_reversaleHome
	 *
	 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
	 */
	public Ass_mandato_reversaleHome(java.sql.Connection conn) {
		super(Ass_mandato_reversaleBulk.class,conn);
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Costruisce un Ass_mandato_reversaleHome
	 *
	 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
	 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
	 */
	public Ass_mandato_reversaleHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(Ass_mandato_reversaleBulk.class,conn,persistentCache);
	}
	/**
	 * Metodo per cercare i mandati associati alla reversale.
	 *
	 * @param reversale <code>ReversaleBulk</code> la reversale
	 *
	 * @return result i mandati associati alla reversale
	 */
	public List findMandati( it.cnr.jada.UserContext userContext,ReversaleBulk reversale ) throws PersistencyException, IntrospectionException
	{
		return this.findMandati(userContext, reversale, true);
	}

	public List findMandati( it.cnr.jada.UserContext userContext,ReversaleBulk reversale, boolean fetchAll) throws PersistencyException
	{
		PersistentHome home = getHomeCache().getHome( Ass_mandato_reversaleBulk.class );
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause("AND","esercizio_reversale",sql.EQUALS, reversale.getEsercizio() );
		sql.addClause("AND","cd_cds_reversale",sql.EQUALS, reversale.getCds().getCd_unita_organizzativa() );
		sql.addClause("AND","pg_reversale",sql.EQUALS, reversale.getPg_reversale() );
		List result = home.fetchAll( sql);
		if (fetchAll) getHomeCache().fetchAll(userContext);
		return result;
	}

	/**
	 * Metodo per cercare le reversali associate al mandato.
	 *
	 * @param mandato <code>MandatoBulk</code> il mandato
	 *
	 * @return result le reversali associate al mandato
	 */
	public Collection findReversali( it.cnr.jada.UserContext userContext,MandatoBulk mandato ) throws PersistencyException, IntrospectionException
	{
		PersistentHome home = getHomeCache().getHome( Ass_mandato_reversaleBulk.class );
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause("AND","esercizio_mandato",sql.EQUALS, mandato.getEsercizio() );
		sql.addClause("AND","cd_cds_mandato",sql.EQUALS, mandato.getCds().getCd_unita_organizzativa() );
		sql.addClause("AND","pg_mandato",sql.EQUALS, mandato.getPg_mandato() );
		Collection result = home.fetchAll( sql);
		getHomeCache().fetchAll(userContext);
		return result;
	}
}
