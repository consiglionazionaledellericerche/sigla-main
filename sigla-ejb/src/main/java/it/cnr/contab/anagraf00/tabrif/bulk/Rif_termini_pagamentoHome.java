package it.cnr.contab.anagraf00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Rif_termini_pagamentoHome extends BulkHome {
public Rif_termini_pagamentoHome(java.sql.Connection conn) {
	super(Rif_termini_pagamentoBulk.class,conn);
}
public Rif_termini_pagamentoHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Rif_termini_pagamentoBulk.class,conn,persistentCache);
}
}
