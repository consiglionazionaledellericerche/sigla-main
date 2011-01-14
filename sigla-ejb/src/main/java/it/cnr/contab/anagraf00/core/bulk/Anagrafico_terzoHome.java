package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;

public class Anagrafico_terzoHome extends BulkHome {
	public Anagrafico_terzoHome(java.sql.Connection conn) {
		super(Anagrafico_terzoBulk.class,conn);
	}
	public Anagrafico_terzoHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(Anagrafico_terzoBulk.class,conn,persistentCache);
	}
}
