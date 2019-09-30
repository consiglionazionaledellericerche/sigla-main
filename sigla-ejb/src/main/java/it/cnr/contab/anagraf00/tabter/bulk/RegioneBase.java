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

package it.cnr.contab.anagraf00.tabter.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class RegioneBase extends RegioneKey implements Keyed {
	// DS_REGIONE VARCHAR(100) NOT NULL
	private java.lang.String ds_regione;

public RegioneBase() {
	super();
}
public RegioneBase(java.lang.String cd_regione) {
	super(cd_regione);
}
/* 
 * Getter dell'attributo ds_regione
 */
public java.lang.String getDs_regione() {
	return ds_regione;
}
/* 
 * Setter dell'attributo ds_regione
 */
public void setDs_regione(java.lang.String ds_regione) {
	this.ds_regione = ds_regione;
}
private java.lang.Boolean fl_addreg_aliqmax;

public java.lang.Boolean getFl_addreg_aliqmax() {
	return fl_addreg_aliqmax;
}
public void setFl_addreg_aliqmax(java.lang.Boolean fl_addreg_aliqmax) {
	this.fl_addreg_aliqmax = fl_addreg_aliqmax;
}
}
