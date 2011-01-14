package it.cnr.contab.config00.pdcep.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

/**
 * Home che gestisce elementi voce associati a voci economico patrimoniali.
 */
public class Ass_ev_voceepHome extends BulkHome {
protected Ass_ev_voceepHome(Class clazz,java.sql.Connection connection) {
	super(clazz,connection);
}
protected Ass_ev_voceepHome(Class clazz,java.sql.Connection connection,PersistentCache persistentCache) {
	super(clazz,connection,persistentCache);
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param conn	
 */
public Ass_ev_voceepHome(java.sql.Connection conn) {
	super(Ass_ev_voceepBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param conn	
 * @param persistentCache	
 */
public Ass_ev_voceepHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Ass_ev_voceepBulk.class,conn,persistentCache);
}
}
