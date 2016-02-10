/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 31/03/2014
 */
package it.cnr.contab.pdg01.consultazioni.bulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.sql.Connection;
public class VConsVarCompResHome extends BulkHome {
	private static final long serialVersionUID = 1L;
	public VConsVarCompResHome(Connection conn) {
		super(VConsVarCompResBulk.class, conn);
	}
	public VConsVarCompResHome(Connection conn, PersistentCache persistentCache) {
		super(VConsVarCompResBulk.class, conn, persistentCache);
	}
	
	@Override
	public SQLBuilder selectByClause(UserContext userContext,
			CompoundFindClause compoundfindclause) throws PersistencyException {
		SQLBuilder sql = super.selectByClause(userContext, compoundfindclause);
		sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,CNRUserContext.getEsercizio(userContext));
		Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) 
				getHomeCache().getHome(Unita_organizzativa_enteBulk.class).findAll().get(0);
		if (!CNRUserContext.getCd_unita_organizzativa(userContext).equals(ente.getCd_unita_organizzativa())){
			sql.openParenthesis("AND");
			sql.addSQLClause("AND","CDR_PROPONENTE",SQLBuilder.EQUALS,CNRUserContext.getCd_cdr(userContext));
			sql.addSQLClause("OR","CDR_ASSEGN",SQLBuilder.EQUALS,CNRUserContext.getCd_cdr(userContext));
			sql.closeParenthesis();
		}
		return sql;
	}	
}