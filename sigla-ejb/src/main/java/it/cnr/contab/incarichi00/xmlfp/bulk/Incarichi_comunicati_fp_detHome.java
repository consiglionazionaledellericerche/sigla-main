/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 26/07/2007
 */
package it.cnr.contab.incarichi00.xmlfp.bulk;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;

import java.sql.Connection;

public class Incarichi_comunicati_fp_detHome extends BulkHome {
	public Incarichi_comunicati_fp_detHome(Connection conn) {
		super(Incarichi_comunicati_fp_detBulk.class, conn);
	}
	public Incarichi_comunicati_fp_detHome(Connection conn, PersistentCache persistentCache) {
		super(Incarichi_comunicati_fp_detBulk.class, conn, persistentCache);
	}
}
