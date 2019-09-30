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

package it.cnr.contab.missioni00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Gruppo_inquadramentoBase extends Gruppo_inquadramentoKey implements Keyed {
	// DS_GRUPPO_INQUADRAMENTO VARCHAR(200) NOT NULL
	private java.lang.String ds_gruppo_inquadramento;

	// FL_DEFAULT CHAR(1) NOT NULL
	private java.lang.Boolean fl_default;

public Gruppo_inquadramentoBase() {
	super();
}
public Gruppo_inquadramentoBase(java.lang.String cd_gruppo_inquadramento) {
	super(cd_gruppo_inquadramento);
}
/* 
 * Getter dell'attributo ds_gruppo_inquadramento
 */
public java.lang.String getDs_gruppo_inquadramento() {
	return ds_gruppo_inquadramento;
}
/* 
 * Getter dell'attributo fl_default
 */
public java.lang.Boolean getFl_default() {
	return fl_default;
}
/* 
 * Setter dell'attributo ds_gruppo_inquadramento
 */
public void setDs_gruppo_inquadramento(java.lang.String ds_gruppo_inquadramento) {
	this.ds_gruppo_inquadramento = ds_gruppo_inquadramento;
}
/* 
 * Setter dell'attributo fl_default
 */
public void setFl_default(java.lang.Boolean fl_default) {
	this.fl_default = fl_default;
}
}
