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

package it.cnr.contab.prevent00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Voce_f_saldi_cmpHome extends BulkHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Voce_f_saldi_cmpHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public Voce_f_saldi_cmpHome(java.sql.Connection conn) {
	super(Voce_f_saldi_cmpBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Voce_f_saldi_cmpHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public Voce_f_saldi_cmpHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Voce_f_saldi_cmpBulk.class,conn,persistentCache);
}
/**
 * Cerca il CDS ENTE
 *
 * @param dettaglio	saldo dettaglio
 * @return Lista contenente il CDS ENTE
 * @throws PersistencyException	
 * @throws IntrospectionException	
 */
public java.util.List cercaCdsEnte(Voce_f_saldi_cmpBulk dettaglio) throws PersistencyException, IntrospectionException
{
	PersistentHome enteHome = getHomeCache().getHome(it.cnr.contab.config00.sto.bulk.EnteBulk.class, "V_CDS_VALIDO");
	SQLBuilder sql = enteHome.createSQLBuilder();
	
	sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS, dettaglio.getEsercizio());
	
	return enteHome.fetchAll(sql);
}
public SQLBuilder createSQLBuilder() {
	SQLBuilder sql = super.createSQLBuilder();
	sql.addClause("AND","ti_competenza_residuo", sql.EQUALS, "C");
	return sql;
}
}
