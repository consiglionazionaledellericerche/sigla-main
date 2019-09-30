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

public class Gruppo_ivaBase extends Gruppo_ivaKey implements Keyed {
	// DS_GRUPPO_IVA VARCHAR(100) NOT NULL
	private java.lang.String ds_gruppo_iva;

/* 
 * Getter dell'attributo ds_gruppo_iva
 */
public java.lang.String getDs_gruppo_iva() {
	return ds_gruppo_iva;
}

/* 
 * Setter dell'attributo ds_gruppo_iva
 */
public void setDs_gruppo_iva(java.lang.String ds_gruppo_iva) {
	this.ds_gruppo_iva = ds_gruppo_iva;
}

public Gruppo_ivaBase() {
	super();
}

public Gruppo_ivaBase(java.lang.String cd_gruppo_iva) {
	super(cd_gruppo_iva);
}
}
