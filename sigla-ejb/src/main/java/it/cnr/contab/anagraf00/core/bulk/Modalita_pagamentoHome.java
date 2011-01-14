package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Modalita_pagamentoHome extends BulkHome {
	public Modalita_pagamentoHome(java.sql.Connection conn) {
		super(Modalita_pagamentoBulk.class,conn);
	}
	public Modalita_pagamentoHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(Modalita_pagamentoBulk.class,conn,persistentCache);
	}
}
