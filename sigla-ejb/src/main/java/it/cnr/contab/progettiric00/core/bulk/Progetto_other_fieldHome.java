package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistentCache;

public class Progetto_other_fieldHome extends BulkHome {

	public final static String TI_IMPORTO_FINANZIATO = "FIN" ;
	public final static String TI_IMPORTO_COFINANZIATO = "COF" ;

	public Progetto_other_fieldHome(java.sql.Connection conn) {
		super(Progetto_other_fieldBulk.class,conn);
	}
	
	public Progetto_other_fieldHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(Progetto_other_fieldBulk.class,conn,persistentCache);
	}
}
