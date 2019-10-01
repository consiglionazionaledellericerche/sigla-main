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

package it.cnr.contab.anagraf00.comp;

import java.util.GregorianCalendar;
import it.cnr.contab.anagraf00.core.bulk.*;
import it.cnr.contab.anagraf00.tabrif.bulk.*;
import it.cnr.contab.anagraf00.tabter.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.*;
import it.cnr.jada.ejb.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.*;

/**
 * Insert the type's description here.
 * Creation date: (08/08/2002 16:31:47)
 * @author: CNRADM
 */
public class UtilitaAnagraficaComponent extends it.cnr.jada.comp.CRUDComponent {
/**
 * UtilitaAnagraficaComponent constructor comment.
 */
public UtilitaAnagraficaComponent() {
	super();
}
public boolean isTerzoSpeciale(UserContext userContext,TerzoBulk terzo) throws it.cnr.jada.comp.ComponentException {
	try {
		BulkHome home = getHome(userContext,it.cnr.contab.config00.bulk.Configurazione_cnrBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		sql.addSQLClause("AND","CD_CHIAVE_PRIMARIA",sql.EQUALS,"COSTANTI");
		sql.addSQLClause("AND","CD_CHIAVE_SECONDARIA",sql.EQUALS,"CODICE_ANAG_ENTE");
		sql.addSQLClause("AND","IM01",sql.EQUALS,terzo.getCd_terzo());
		if (sql.executeExistsQuery(getConnection(userContext)))
			return true;

		sql = home.createSQLBuilder();
		sql.addSQLClause("AND","CD_CHIAVE_PRIMARIA",SQLBuilder.EQUALS,"TERZO_SPECIALE" );
		sql.addSQLClause("AND","IM01",sql.EQUALS,terzo.getCd_terzo());
		if (sql.executeExistsQuery(getConnection(userContext)))
			return true;

		return false;
	} catch(java.sql.SQLException e) {
		throw handleSQLException(e);
	}
}
}
