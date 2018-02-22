package it.cnr.contab.config00.latt.bulk;

import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ass_tipo_la_cdrHome extends BulkHome {
/**
 * Costruttore associazione tipo/linea attività/cdr Home
 *
 * @param conn connessione db	
 */
public Ass_tipo_la_cdrHome(java.sql.Connection conn) {
	super(Ass_tipo_la_cdrBulk.class,conn);
}
/**
 * Costrutture associazione tipo/linea attività/cdr Home
 *
 * @param conn connessione db
 * @param persistentCache cache modelli
 */
public Ass_tipo_la_cdrHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Ass_tipo_la_cdrBulk.class,conn,persistentCache);
}
/**
 * Volutamente il cdr con codice '*' non deve essere idrato da db: gestisce l'eccezione di object not found
 * @see it.cnr.jada.persistency.ObjectNotFoundHandler
 */
public void handleObjectNotFoundException(it.cnr.jada.persistency.ObjectNotFoundException e) throws it.cnr.jada.persistency.ObjectNotFoundException {
	if(e.getPersistent() instanceof CdrBulk) {
	 CdrBulk aB = (CdrBulk)e.getPersistent();
	 if(aB.getCd_centro_responsabilita().equals("*"))
	  return;
	}
	
	throw e;
}
}
