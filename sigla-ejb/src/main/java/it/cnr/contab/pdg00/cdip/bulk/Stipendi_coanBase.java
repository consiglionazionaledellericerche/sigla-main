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

package it.cnr.contab.pdg00.cdip.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Stipendi_coanBase extends Stipendi_coanKey implements Keyed {
	// PG_SCRITTURA_AN DECIMAL(4,0)
	private java.lang.Integer pg_scrittura_an;

public Stipendi_coanBase() {
	super();
}
public Stipendi_coanBase(java.lang.String cd_cds,java.lang.String cd_uo,java.lang.Integer esercizio,java.lang.Integer mese) {
	super(cd_cds,cd_uo,esercizio,mese);
}
/* 
 * Getter dell'attributo pg_scrittura_an
 */
public java.lang.Integer getPg_scrittura_an() {
	return pg_scrittura_an;
}
/* 
 * Setter dell'attributo pg_scrittura_an
 */
public void setPg_scrittura_an(java.lang.Integer pg_scrittura_an) {
	this.pg_scrittura_an = pg_scrittura_an;
}
}
