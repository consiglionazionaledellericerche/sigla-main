package it.cnr.contab.anagraf00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Rif_modalita_pagamentoHome extends BulkHome {
public Rif_modalita_pagamentoHome(java.sql.Connection conn) {
	super(Rif_modalita_pagamentoBulk.class,conn);
}
public Rif_modalita_pagamentoHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Rif_modalita_pagamentoBulk.class,conn,persistentCache);
}
}
