/*
* Created by Generator 1.0
* Date 22/09/2005
*/
package it.cnr.contab.logregistry00.logs.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;
public class L_ass_comp_doc_cont_nmpHome extends BulkHome {
	public L_ass_comp_doc_cont_nmpHome(java.sql.Connection conn) {
		super(L_ass_comp_doc_cont_nmpBulk.class, conn);
	}
	public L_ass_comp_doc_cont_nmpHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(L_ass_comp_doc_cont_nmpBulk.class, conn, persistentCache);
	}
}