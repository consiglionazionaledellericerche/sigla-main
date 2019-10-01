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

package it.cnr.contab.compensi00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Contributo_ritenuta_detHome extends BulkHome {
public Contributo_ritenuta_detHome(java.sql.Connection conn) {
	super(Contributo_ritenuta_detBulk.class,conn);
}
public Contributo_ritenuta_detHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Contributo_ritenuta_detBulk.class,conn,persistentCache);
}
public java.util.Collection loadDettagliContributo(Contributo_ritenutaBulk contributo) throws PersistencyException {

	SQLBuilder sql = createSQLBuilder();

	sql.addSQLClause("AND","CD_CDS",sql.EQUALS,contributo.getCd_cds());
	sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS,contributo.getCd_unita_organizzativa());
	sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,contributo.getEsercizio());
	sql.addSQLClause("AND","PG_COMPENSO",sql.EQUALS,contributo.getPg_compenso());
	sql.addSQLClause("AND","CD_CONTRIBUTO_RITENUTA",sql.EQUALS,contributo.getCd_contributo_ritenuta());
	sql.addSQLClause("AND","TI_ENTE_PERCIPIENTE",sql.EQUALS,contributo.getTi_ente_percipiente());
	java.util.List l = fetchAll(sql);

	for (java.util.Iterator i = l.iterator();i.hasNext();)
		((Contributo_ritenuta_detBulk)i.next()).setContributoRitenuta(contributo);

	return l;
}
}
