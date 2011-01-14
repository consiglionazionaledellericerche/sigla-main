/*
* Created by Generator 1.0
* Date 09/11/2005
*/
package it.cnr.contab.pdg00.consultazioni.bulk;
import java.sql.Connection;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class V_cons_pdg_assestato_aggregatoHome extends BulkHome {
	public V_cons_pdg_assestato_aggregatoHome(java.sql.Connection conn) {
		super(V_cons_pdg_assestato_aggregatoBulk.class, conn);
	}
	public V_cons_pdg_assestato_aggregatoHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_cons_pdg_assestato_aggregatoBulk.class, conn, persistentCache);
	}
}