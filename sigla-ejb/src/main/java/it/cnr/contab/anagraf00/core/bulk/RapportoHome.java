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

package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class RapportoHome extends BulkHome {
	public RapportoHome(java.sql.Connection conn) {
		super(RapportoBulk.class,conn);
	}
	public RapportoHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(RapportoBulk.class,conn,persistentCache);
	}
	public java.util.Collection findByCdAnagCdTipoRapporto(Integer anag, String tipo_rap) throws IntrospectionException, PersistencyException {
		PersistentHome rHome = getHomeCache().getHome(RapportoBulk.class);
		SQLBuilder sql = rHome.createSQLBuilder();
		sql.addClause("AND","cd_anag",sql.EQUALS,anag);
		sql.addClause("AND","cd_tipo_rapporto",sql.EQUALS,tipo_rap);
		sql.addOrderBy("DT_INI_VALIDITA DESC");
		return rHome.fetchAll(sql);
	}
}
