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

package it.cnr.contab.rest.bulk;

import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.sql.Connection;

public class RestServicesHome extends BulkHome{

	protected RestServicesHome(Class class1, Connection connection) {
		super(class1, connection);
	}

	protected RestServicesHome(Class class1, Connection connection,
			PersistentCache persistentcache) {
		super(class1, connection, persistentcache);
	}

	public SQLBuilder addConditionCds(UserContext userContext, SQLBuilder sqlBuilder, String fieldName){
		sqlBuilder.addClause("AND",fieldName,SQLBuilder.EQUALS,CNRUserContext.getCd_cds(userContext));
		return sqlBuilder;
	}
	public SQLBuilder addConditionCdr(UserContext userContext, SQLBuilder sqlBuilder, String fieldName){
		sqlBuilder.addClause("AND",fieldName,SQLBuilder.EQUALS,CNRUserContext.getCd_cdr(userContext));
		return sqlBuilder;
	}
	public SQLBuilder addConditionEsercizio(UserContext userContext, SQLBuilder sqlBuilder, String fieldName){
		sqlBuilder.addClause("AND",fieldName,SQLBuilder.EQUALS,CNRUserContext.getEsercizio(userContext));
		return sqlBuilder;
	}
	public SQLBuilder addConditionUnitaOrganizzativa(UserContext userContext, SQLBuilder sqlBuilder, String fieldName){
		sqlBuilder.addClause("AND",fieldName,SQLBuilder.EQUALS,CNRUserContext.getCd_unita_organizzativa(userContext));
		return sqlBuilder;
	}
}
