package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Termini_pagamentoHome extends BulkHome {
	public Termini_pagamentoHome(java.sql.Connection conn) {
		super(Termini_pagamentoBulk.class,conn);
	}
	public Termini_pagamentoHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(Termini_pagamentoBulk.class,conn,persistentCache);
	}
}
