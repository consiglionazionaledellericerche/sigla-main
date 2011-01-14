package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class InquadramentoHome extends BulkHome {
	public InquadramentoHome(java.sql.Connection conn) {
		super(InquadramentoBulk.class,conn);
	}
	public InquadramentoHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(InquadramentoBulk.class,conn,persistentCache);
	}
}
