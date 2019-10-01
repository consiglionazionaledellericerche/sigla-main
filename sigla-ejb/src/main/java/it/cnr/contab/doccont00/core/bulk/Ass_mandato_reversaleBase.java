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

package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ass_mandato_reversaleBase extends Ass_mandato_reversaleKey implements Keyed {
	// TI_ORIGINE CHAR(1) NOT NULL
	private java.lang.String ti_origine;

public Ass_mandato_reversaleBase() {
	super();
}
public Ass_mandato_reversaleBase(java.lang.String cd_cds_mandato,java.lang.String cd_cds_reversale,java.lang.Integer esercizio_mandato,java.lang.Integer esercizio_reversale,java.lang.Long pg_mandato,java.lang.Long pg_reversale) {
	super(cd_cds_mandato,cd_cds_reversale,esercizio_mandato,esercizio_reversale,pg_mandato,pg_reversale);
}
/* 
 * Getter dell'attributo ti_origine
 */
public java.lang.String getTi_origine() {
	return ti_origine;
}
/* 
 * Setter dell'attributo ti_origine
 */
public void setTi_origine(java.lang.String ti_origine) {
	this.ti_origine = ti_origine;
}
}
