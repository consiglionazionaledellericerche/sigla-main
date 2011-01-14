package it.cnr.contab.fondecon00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ass_fondo_eco_mandatoHome extends BulkHome {
public Ass_fondo_eco_mandatoHome(java.sql.Connection conn) {
	super(Ass_fondo_eco_mandatoBulk.class,conn);
}
public Ass_fondo_eco_mandatoHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Ass_fondo_eco_mandatoBulk.class,conn,persistentCache);
}
}
