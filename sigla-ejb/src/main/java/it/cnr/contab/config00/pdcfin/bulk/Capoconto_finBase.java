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

package it.cnr.contab.config00.pdcfin.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Capoconto_finBase extends Capoconto_finKey implements Keyed {
	// DS_CAPOCONTO_FIN VARCHAR(300)
	private java.lang.String ds_capoconto_fin;

public Capoconto_finBase() {
	super();
}
public Capoconto_finBase(java.lang.String cd_capoconto_fin) {
	super(cd_capoconto_fin);
}
/* 
 * Getter dell'attributo ds_capoconto_fin
 */
public java.lang.String getDs_capoconto_fin() {
	return ds_capoconto_fin;
}
/* 
 * Setter dell'attributo ds_capoconto_fin
 */
public void setDs_capoconto_fin(java.lang.String ds_capoconto_fin) {
	this.ds_capoconto_fin = ds_capoconto_fin;
}
}
