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

package it.cnr.contab.config00.latt.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Insieme_laHome extends BulkHome {
/**
 * Costrutture insieme linea di attività Home
 *
 * @param conn connessione db
 */

public Insieme_laHome(java.sql.Connection conn) {
	super(Insieme_laBulk.class,conn);
}
/**
 * Costrutture insieme linea di attività Home
 *
 * @param conn connessione db
 * @param persistentCache cache modelli
 */
public Insieme_laHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Insieme_laBulk.class,conn,persistentCache);
}
public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk bulk) throws PersistencyException,it.cnr.jada.comp.ComponentException {
	try {
		Insieme_laBase insieme_la = (Insieme_laBase)bulk;
		if (insieme_la.getCd_insieme_la() == null) {
			String max = findMax(insieme_la,"cd_insieme_la","00000000",true).toString();
			try {
				int maxValue = Integer.parseInt(max);
				insieme_la.setCd_insieme_la(new java.text.DecimalFormat("00000000").format(maxValue+1));
			} catch(Throwable e) {
			}
		}
	} catch(BusyResourceException e) {
		throw new it.cnr.jada.comp.ComponentException(e);
	}
}
/**
 * Ritorna un SQL builder per la selezione di insiemi compatibili con l'utente presente in userContext
 *
 * @param userContext Contesto Utente
 * @param clauses Condizioni di ricerca ereditate
 * @return un'istanza di SQLBuilder
 */
public SQLBuilder selectInsieme_laVisibiliByClauses(it.cnr.jada.UserContext userContext,CompoundFindClause clauses) {
	SQLBuilder sql = createSQLBuilder();
	sql.addTableToHeader("UTENTE");
	sql.addClause( clauses );
	sql.addSQLJoin("INSIEME_LA.CD_CENTRO_RESPONSABILITA","UTENTE.CD_CDR");
	sql.addSQLClause("AND","CD_UTENTE",sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getUser(userContext));
	return sql;
}
}
