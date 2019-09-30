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

package it.cnr.contab.config00.latt.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Gruppo_linea_attivitaBase extends Gruppo_linea_attivitaKey implements Keyed {
	// DS_GRUPPO_LINEA_ATTIVITA VARCHAR(200) NOT NULL
	private java.lang.String ds_gruppo_linea_attivita;

public Gruppo_linea_attivitaBase() {
	super();
}
public Gruppo_linea_attivitaBase(java.lang.String cd_gruppo_linea_attivita) {
	super(cd_gruppo_linea_attivita);
}
/* 
 * Getter dell'attributo ds_gruppo_linea_attivita
 */
public java.lang.String getDs_gruppo_linea_attivita() {
	return ds_gruppo_linea_attivita;
}
/* 
 * Setter dell'attributo ds_gruppo_linea_attivita
 */
public void setDs_gruppo_linea_attivita(java.lang.String ds_gruppo_linea_attivita) {
	this.ds_gruppo_linea_attivita = ds_gruppo_linea_attivita;
}
}
