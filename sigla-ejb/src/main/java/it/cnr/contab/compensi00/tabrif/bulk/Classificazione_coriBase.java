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

package it.cnr.contab.compensi00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Classificazione_coriBase extends Classificazione_coriKey implements Keyed {
	// DS_CLASSIFICAZIONE_CORI VARCHAR(100)
	private java.lang.String ds_classificazione_cori;

public Classificazione_coriBase() {
	super();
}
public Classificazione_coriBase(java.lang.String cd_classificazione_cori) {
	super(cd_classificazione_cori);
}
/* 
 * Getter dell'attributo ds_classificazione_cori
 */
public java.lang.String getDs_classificazione_cori() {
	return ds_classificazione_cori;
}
/* 
 * Setter dell'attributo ds_classificazione_cori
 */
public void setDs_classificazione_cori(java.lang.String ds_classificazione_cori) {
	this.ds_classificazione_cori = ds_classificazione_cori;
}
}
