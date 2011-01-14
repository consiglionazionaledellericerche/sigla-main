package it.cnr.contab.gestiva00.core.bulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Report_statoHome extends BulkHome {
public Report_statoHome(java.sql.Connection conn) {
	super(Report_statoBulk.class,conn);
}
public Report_statoHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Report_statoBulk.class,conn,persistentCache);
}
/**
 *	Ritorna tutti i Records
 *  ordinati per la data di inizio
 *  
 *  Parametri:
 *	 - Report_statoBulk reportStato
 *
**/
public java.util.List findAndOrderByDt_inizio(Report_statoBulk reportStato) throws PersistencyException{

	SQLBuilder sql = createSQLBuilder();
	sql.addClausesUsing(reportStato, false);	
	sql.addOrderBy("DT_INIZIO");
	return fetchAll(sql);
}
}
