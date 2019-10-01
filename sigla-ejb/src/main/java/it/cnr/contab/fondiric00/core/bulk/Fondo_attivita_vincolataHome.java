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

package it.cnr.contab.fondiric00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Fondo_attivita_vincolataHome extends BulkHome {
public Fondo_attivita_vincolataHome(java.sql.Connection conn) {
	super(Fondo_attivita_vincolataBulk.class,conn);
}
public Fondo_attivita_vincolataHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Fondo_attivita_vincolataBulk.class,conn,persistentCache);
}
	/**
	 * Recupera tutti i dati nella tabella Fondo_assegnatario relativi alla testata in uso.
	 *
	 * @param testata La testata in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>Fondo_assegnatarioBulk</code>
	 */

	public java.util.Collection findDettagli(Fondo_attivita_vincolataBulk testata) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = getHomeCache().getHome(Fondo_assegnatarioBulk.class);
		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause("AND","cd_fondo",sql.EQUALS,testata.getCd_fondo());
		return dettHome.fetchAll(sql);
	}

}
