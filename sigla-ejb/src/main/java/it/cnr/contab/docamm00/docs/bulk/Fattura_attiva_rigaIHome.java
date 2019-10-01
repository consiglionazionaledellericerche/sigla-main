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

public class Fattura_attiva_rigaIHome extends Fattura_attiva_rigaHome {
public Fattura_attiva_rigaIHome(java.sql.Connection conn) {
	super(Fattura_attiva_rigaIBulk.class,conn);
}
public Fattura_attiva_rigaIHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Fattura_attiva_rigaIBulk.class,conn,persistentCache);
}
protected SQLBuilder selectForAccertamentoExceptFor(
	it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk scadenza,
	Fattura_attivaBulk fattura) {
	
	SQLBuilder sql = super.selectForAccertamentoExceptFor(scadenza, fattura);

	sql.addSQLClause("AND","FATTURA_ATTIVA.TI_FATTURA",sql.EQUALS, fattura.TIPO_FATTURA_ATTIVA);

	return sql;
}
}
