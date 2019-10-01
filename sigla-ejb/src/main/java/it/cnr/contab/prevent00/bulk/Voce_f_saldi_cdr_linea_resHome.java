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
