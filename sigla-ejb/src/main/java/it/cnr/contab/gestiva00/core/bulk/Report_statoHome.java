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
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ComponentException;
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
public java.util.List findAndOrderByDt_inizio(UserContext userContext, Report_statoBulk reportStato) throws ComponentException, PersistencyException{
	SQLBuilder sql = createSQLBuilder();
	sql.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, reportStato.getEsercizio());
	sql.addClause(FindClause.AND, "cd_tipo_sezionale", SQLBuilder.EQUALS, reportStato.getCd_tipo_sezionale());
	sql.addClause(FindClause.AND, "tipo_report", SQLBuilder.EQUALS, reportStato.getTipo_report());
	sql.addClause(FindClause.AND, "ti_documento", SQLBuilder.EQUALS, reportStato.getTi_documento());
	if (!((Unita_organizzativa_enteHome)getHomeCache().getHome(Unita_organizzativa_enteBulk.class)).isUoEnte(userContext)) {
		sql.addClause(FindClause.AND, "cd_cds", SQLBuilder.EQUALS, reportStato.getCd_cds());
		sql.addClause(FindClause.AND, "cd_unita_organizzativa", SQLBuilder.EQUALS, reportStato.getCd_unita_organizzativa());
	} else {
		sql.addClause(FindClause.AND, "dt_inizio", SQLBuilder.EQUALS, reportStato.getDt_inizio());
		sql.addClause(FindClause.AND, "dt_fine", SQLBuilder.EQUALS, reportStato.getDt_fine());
	}
	sql.addOrderBy("DT_INIZIO");
	return fetchAll(sql);
}
}
