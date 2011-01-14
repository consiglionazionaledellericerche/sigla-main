/*
* Created by Generator 1.0
* Date 12/07/2005
*/
package it.cnr.contab.pdg00.cdip.bulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class V_assestato_modulo_var_pdgHome extends BulkHome {
	protected V_assestato_modulo_var_pdgHome(Class clazz,java.sql.Connection connection) {
		super(clazz,connection);
	}
	protected V_assestato_modulo_var_pdgHome(Class clazz,java.sql.Connection connection,PersistentCache persistentCache) {
		super(clazz,connection,persistentCache);
	}
	public V_assestato_modulo_var_pdgHome(java.sql.Connection conn) {
		super(V_assestato_modulo_var_pdgBulk.class, conn);
	}
	public V_assestato_modulo_var_pdgHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_assestato_modulo_var_pdgBulk.class, conn, persistentCache);
	}
}