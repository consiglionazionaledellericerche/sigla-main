package it.cnr.contab.config00.latt.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_risultatoHome extends BulkHome {
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param conn	
 */
public Tipo_risultatoHome(java.sql.Connection conn) {
	super(Tipo_risultatoBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param conn	
 * @param persistentCache	
 */
public Tipo_risultatoHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Tipo_risultatoBulk.class,conn,persistentCache);
}
}
