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

package it.cnr.contab.utenze00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class AccessoBase extends AccessoKey implements Keyed {
	// DS_ACCESSO VARCHAR(200)
	private java.lang.String ds_accesso;

	// TI_ACCESSO CHAR(1)
	private java.lang.String ti_accesso;

public AccessoBase() {
	super();
}
public AccessoBase(java.lang.String cd_accesso) {
	super(cd_accesso);
}
/* 
 * Getter dell'attributo ds_accesso
 */
public java.lang.String getDs_accesso() {
	return ds_accesso;
}
/* 
 * Getter dell'attributo ti_accesso
 */
public java.lang.String getTi_accesso() {
	return ti_accesso;
}
/* 
 * Setter dell'attributo ds_accesso
 */
public void setDs_accesso(java.lang.String ds_accesso) {
	this.ds_accesso = ds_accesso;
}
/* 
 * Setter dell'attributo ti_accesso
 */
public void setTi_accesso(java.lang.String ti_accesso) {
	this.ti_accesso = ti_accesso;
}
}
