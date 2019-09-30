/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
