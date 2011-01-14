package it.cnr.contab.pdg00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_stm_paramin_pdg_variazioneHome extends BulkHome {
public V_stm_paramin_pdg_variazioneHome(java.sql.Connection conn) {
	super(V_stm_paramin_pdg_variazioneBulk.class,conn);
}
public V_stm_paramin_pdg_variazioneHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(V_stm_paramin_pdg_variazioneBulk.class,conn,persistentCache);
}
}
