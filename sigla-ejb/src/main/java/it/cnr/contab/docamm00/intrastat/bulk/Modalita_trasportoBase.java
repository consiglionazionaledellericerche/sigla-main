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

package it.cnr.contab.docamm00.intrastat.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Modalita_trasportoBase extends Modalita_trasportoKey implements Keyed {
	// DS_MODALITA_TRASPORTO VARCHAR(300) NOT NULL
	private java.lang.String ds_modalita_trasporto;

public Modalita_trasportoBase() {
	super();
}
public Modalita_trasportoBase(java.lang.String cd_modalita_trasporto,java.lang.Integer esercizio) {
	super(cd_modalita_trasporto,esercizio);
}
/* 
 * Getter dell'attributo ds_modalita_trasporto
 */
public java.lang.String getDs_modalita_trasporto() {
	return ds_modalita_trasporto;
}
/* 
 * Setter dell'attributo ds_modalita_trasporto
 */
public void setDs_modalita_trasporto(java.lang.String ds_modalita_trasporto) {
	this.ds_modalita_trasporto = ds_modalita_trasporto;
}
}
