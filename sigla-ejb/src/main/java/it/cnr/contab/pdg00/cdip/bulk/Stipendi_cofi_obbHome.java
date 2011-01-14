/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 02/10/2006
 */
package it.cnr.contab.pdg00.cdip.bulk;
import java.sql.Connection;

import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class Stipendi_cofi_obbHome extends BulkHome {
	public Stipendi_cofi_obbHome(Connection conn) {
		super(Stipendi_cofi_obbBulk.class, conn);
	}
	public Stipendi_cofi_obbHome(Connection conn, PersistentCache persistentCache) {
		super(Stipendi_cofi_obbBulk.class, conn, persistentCache);
	}
	public java.util.Collection find(UserContext context) throws PersistencyException{

		SQLBuilder sql = createSQLBuilder();
		sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,((it.cnr.contab.utenze00.bp.CNRUserContext)context).getEsercizio());
		return fetchAll(sql);
	}
}