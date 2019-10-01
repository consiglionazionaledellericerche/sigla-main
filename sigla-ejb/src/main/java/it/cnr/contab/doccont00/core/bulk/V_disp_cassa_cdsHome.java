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

package it.cnr.contab.doccont00.core.bulk;

import it.cnr.contab.config00.sto.bulk.EnteBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

import java.util.*;

public class V_disp_cassa_cdsHome extends BulkHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un V_disp_cassa_cdsHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public V_disp_cassa_cdsHome(java.sql.Connection conn) {
	super(V_disp_cassa_cdsBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un V_disp_cassa_cdsHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public V_disp_cassa_cdsHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(V_disp_cassa_cdsBulk.class,conn,persistentCache);
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param esercizio	
 * @return 
 * @throws PersistencyException	
 */
public Collection findDisponibilitaCassa( Integer esercizio ) throws PersistencyException
{
	EnteBulk ente = (EnteBulk) getHomeCache().getHome(EnteBulk.class).findAll().get(0);
	SQLBuilder sql = createSQLBuilder();
	sql.addClause( "AND" , "esercizio", sql.EQUALS, esercizio );
	sql.addClause( "AND" , "cd_cds", sql.NOT_EQUALS, ente.getCd_unita_organizzativa() );
	return fetchAll( sql );
}
}
