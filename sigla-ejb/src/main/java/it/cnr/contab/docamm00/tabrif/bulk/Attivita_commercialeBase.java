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

package it.cnr.contab.docamm00.tabrif.bulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Attivita_commercialeBase extends Attivita_commercialeKey implements Keyed {
	// DS_ATTIVITA_COMMERCIALE VARCHAR(100) NOT NULL
	private java.lang.String ds_attivita_commerciale;

/* 
 * Getter dell'attributo ds_attivita_commerciale
 */
public java.lang.String getDs_attivita_commerciale() {
	return ds_attivita_commerciale;
}

/* 
 * Setter dell'attributo ds_attivita_commerciale
 */
public void setDs_attivita_commerciale(java.lang.String ds_attivita_commerciale) {
	this.ds_attivita_commerciale = ds_attivita_commerciale;
}

public Attivita_commercialeBase() {
	super();
}

public Attivita_commercialeBase(java.lang.String cd_attivita_commerciale) {
	super(cd_attivita_commerciale);
}
}
