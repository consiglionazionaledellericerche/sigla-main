/*
 * Created on Mar 17, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.prevent00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;
/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Voce_f_saldi_cdr_linea_resHome extends BulkHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Voce_f_saldi_cdr_linea_resHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public Voce_f_saldi_cdr_linea_resHome(java.sql.Connection conn) {
	super(Voce_f_saldi_cdr_linea_resBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Voce_f_saldi_cdr_linea_resHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public Voce_f_saldi_cdr_linea_resHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Voce_f_saldi_cdr_linea_resBulk.class,conn,persistentCache);
}
/**
 * Ritorna un SQLBuilder con la columnMap del ricevente
 */
public SQLBuilder createSQLBuilder() {
	SQLBuilder sql = super.createSQLBuilder();
	sql.addSQLJoin("VOCE_F_SALDI_CDR_LINEA.ESERCIZIO",SQLBuilder.GREATER,"VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES");
	return sql;
}
}
