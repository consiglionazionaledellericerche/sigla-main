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

public class Vp_liquid_iva_annualeHome extends BulkHome {
public Vp_liquid_iva_annualeHome(java.sql.Connection conn) {
	super(Vp_liquid_iva_annualeBulk.class,conn);
}
public Vp_liquid_iva_annualeHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Vp_liquid_iva_annualeBulk.class,conn,persistentCache);
}
/**
 * Insert the method's description here.
 * Creation date: (02/12/2003 17.06.34)
 */
public BulkList findDettagliPerTipo(
	Liquidazione_iva_annualeVBulk bulk,
	String tipo) 
	throws PersistencyException {

	SQLBuilder sql = createSQLBuilder();
	sql.addClause("AND", "id", SQLBuilder.EQUALS, bulk.getId_report());
	sql.addClause("AND", "tipo", SQLBuilder.EQUALS, tipo);
	sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio());
	
	return new BulkList(fetchAll(sql));
}
}
