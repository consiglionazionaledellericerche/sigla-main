/*
* Created by Generator 1.0
* Date 30/08/2005
*/
package it.cnr.contab.cori00.docs.bulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class Liquid_gruppo_centroHome extends BulkHome {
	public Liquid_gruppo_centroHome(java.sql.Connection conn) {
		super(Liquid_gruppo_centroBulk.class, conn);
	}
	public Liquid_gruppo_centroHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Liquid_gruppo_centroBulk.class, conn, persistentCache);
	}
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause)
	throws PersistencyException
	{
		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
		sql.addSQLClause("AND","LIQUID_GRUPPO_CENTRO.STATO",sql.NOT_EQUALS,Liquid_gruppo_centroBulk.STATO_CHIUSO);
		sql.addSQLClause("AND","LIQUID_GRUPPO_CENTRO.STATO",sql.NOT_EQUALS,Liquid_gruppo_centroBulk.STATO_ANNULLATO);
		sql.openParenthesis( "AND (");
		sql.addSQLClause("AND","LIQUID_GRUPPO_CENTRO.ESERCIZIO",sql.EQUALS,CNRUserContext.getEsercizio(usercontext));
		sql.addSQLClause("AND","LIQUID_GRUPPO_CENTRO.DA_ESERCIZIO_PRECEDENTE",sql.EQUALS,"Y");
		sql.closeParenthesis();
		sql.openParenthesis( "OR");
		sql.addSQLClause(") OR (","LIQUID_GRUPPO_CENTRO.ESERCIZIO",sql.EQUALS,(CNRUserContext.getEsercizio(usercontext)- 1));
		sql.addSQLClause("AND","LIQUID_GRUPPO_CENTRO.DA_ESERCIZIO_PRECEDENTE",sql.EQUALS,"N");
		sql.closeParenthesis();
		sql.closeParenthesis();
		sql.addOrderBy("LIQUID_GRUPPO_CENTRO.CD_GRUPPO_CR");
		sql.addOrderBy("LIQUID_GRUPPO_CENTRO.CD_REGIONE");
	return sql;
}

}