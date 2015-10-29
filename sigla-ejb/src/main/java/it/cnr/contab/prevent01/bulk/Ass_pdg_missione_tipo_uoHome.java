/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 20/01/2007
 */
package it.cnr.contab.prevent01.bulk;

import java.sql.Connection;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;

public class Ass_pdg_missione_tipo_uoHome extends BulkHome {
	private static final long serialVersionUID = 1L;

	public Ass_pdg_missione_tipo_uoHome(Connection conn) {
		super(Ass_pdg_missione_tipo_uoBulk.class, conn);
	}
	
	public Ass_pdg_missione_tipo_uoHome(Connection conn, PersistentCache persistentCache) {
		super(Ass_pdg_missione_tipo_uoBulk.class, conn, persistentCache);
	}
}