/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 28/09/2006
 */
package it.cnr.contab.pdg00.cdip.bulk;
import java.sql.Connection;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class Stipendi_cofi_obb_scadHome extends BulkHome {
	public Stipendi_cofi_obb_scadHome(Connection conn) {
		super(Stipendi_cofi_obb_scadBulk.class, conn);
	}
	public Stipendi_cofi_obb_scadHome(Connection conn, PersistentCache persistentCache) {
		super(Stipendi_cofi_obb_scadBulk.class, conn, persistentCache);
	}
	/*
	public SQLBuilder selectStipendi_cofi_obbByClause(UserContext usercontext, CompoundFindClause compoundfindclause)
	throws PersistencyException
	{
	SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
	sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,CNRUserContext.getEsercizio(usercontext));
	return sql;
	
	}
	*/
}