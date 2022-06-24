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

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.action.FindBP;

import java.util.Collection;
import java.util.List;

public class Ass_mandato_mandatoHome extends BulkHome {
	public Ass_mandato_mandatoHome(java.sql.Connection conn) {
	super(Ass_mandato_mandatoBulk.class,conn);
}

	public Ass_mandato_mandatoHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Ass_mandato_mandatoBulk.class,conn,persistentCache);
	}

	public Collection findByMandato( it.cnr.jada.UserContext userContext, MandatoBulk mandato ) throws PersistencyException, IntrospectionException
	{
		PersistentHome home = getHomeCache().getHome( Ass_mandato_mandatoBulk.class );
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause(FindClause.AND,"esercizio",SQLBuilder.EQUALS, mandato.getEsercizio() );
		sql.addClause(FindClause.AND,"cd_cds",SQLBuilder.EQUALS, mandato.getCds().getCd_unita_organizzativa() );
		sql.addClause(FindClause.AND,"pg_mandato",SQLBuilder.EQUALS, mandato.getPg_mandato() );
		Collection result = home.fetchAll( sql);
		getHomeCache().fetchAll(userContext);
		return result;
	}

	public Collection findByMandatoCollegato( it.cnr.jada.UserContext userContext, MandatoBulk mandato ) throws PersistencyException {
		return this.findByMandatoCollegato(userContext, mandato, true);
	}

	public Collection findByMandatoCollegato( it.cnr.jada.UserContext userContext, MandatoBulk mandato, boolean fetchAll ) throws PersistencyException {
		PersistentHome home = getHomeCache().getHome( Ass_mandato_mandatoBulk.class );
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause(FindClause.AND,"esercizio_coll",SQLBuilder.EQUALS, mandato.getEsercizio() );
		sql.addClause(FindClause.AND,"cd_cds_coll",SQLBuilder.EQUALS, mandato.getCds().getCd_unita_organizzativa() );
		sql.addClause(FindClause.AND,"pg_mandato_coll",SQLBuilder.EQUALS, mandato.getPg_mandato() );
		Collection result = home.fetchAll( sql);
		if (fetchAll) getHomeCache().fetchAll(userContext);
		return result;
	}
}
