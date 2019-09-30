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

package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Fattura_passiva_rigaIHome extends Fattura_passiva_rigaHome {
public Fattura_passiva_rigaIHome(java.sql.Connection conn) {
	super(Fattura_passiva_rigaIBulk.class,conn);
}
public Fattura_passiva_rigaIHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Fattura_passiva_rigaIBulk.class,conn,persistentCache);
}
protected SQLBuilder selectForObbligazioneExceptFor(
	it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk scadenza,
	Fattura_passivaBulk fattura) {
	
	SQLBuilder sql = super.selectForObbligazioneExceptFor(scadenza, fattura);

	sql.addSQLClause("AND","FATTURA_PASSIVA.TI_FATTURA",sql.EQUALS, fattura.TIPO_FATTURA_PASSIVA);

	return sql;
}
}
