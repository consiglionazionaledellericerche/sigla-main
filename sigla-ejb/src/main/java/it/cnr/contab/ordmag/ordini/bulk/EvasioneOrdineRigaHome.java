/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 21/09/2017
 */
package it.cnr.contab.ordmag.ordini.bulk;
import java.sql.Connection;

import it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class EvasioneOrdineRigaHome extends BulkHome {
	public EvasioneOrdineRigaHome(Connection conn) {
		super(EvasioneOrdineRigaBulk.class, conn);
	}
	public EvasioneOrdineRigaHome(Connection conn, PersistentCache persistentCache) {
		super(EvasioneOrdineRigaBulk.class, conn, persistentCache);
	}
}