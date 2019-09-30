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

package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Progetto_uoBase extends Progetto_uoKey implements Keyed {

	// IMPORTO DECIMAL(15,2)
	private java.math.BigDecimal importo;

public Progetto_uoBase() {
	super();
}
public Progetto_uoBase(java.lang.Integer pg_progetto,java.lang.String cd_unita_organizzativa) {
	super(pg_progetto,cd_unita_organizzativa);
}
/* 
 * Getter dell'attributo importo
 */
public java.math.BigDecimal getImporto() {
	return importo;
}
/* 
 * Setter dell'attributo importo
 */
public void setImporto(java.math.BigDecimal importo) {
	this.importo = importo;
}
}