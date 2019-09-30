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

public class Insieme_laBase extends Insieme_laKey implements Keyed {
	// DS_INSIEME_LA VARCHAR(200) NOT NULL
	private java.lang.String ds_insieme_la;

public Insieme_laBase() {
	super();
}
public Insieme_laBase(java.lang.String cd_centro_responsabilita,java.lang.String cd_insieme_la) {
	super(cd_centro_responsabilita,cd_insieme_la);
}
/* 
 * Getter dell'attributo ds_insieme_la
 */
public java.lang.String getDs_insieme_la() {
	return ds_insieme_la;
}
/* 
 * Setter dell'attributo ds_insieme_la
 */
public void setDs_insieme_la(java.lang.String ds_insieme_la) {
	this.ds_insieme_la = ds_insieme_la;
}
}
