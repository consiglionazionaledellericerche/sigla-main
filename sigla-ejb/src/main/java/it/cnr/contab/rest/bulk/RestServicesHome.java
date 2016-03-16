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
