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

package it.cnr.contab.doccont00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_consegnaBase extends Tipo_consegnaKey implements Keyed {
	// DS_TIPO_CONSEGNA VARCHAR(100) NOT NULL
	private java.lang.String ds_tipo_consegna;

public Tipo_consegnaBase() {
	super();
}
public Tipo_consegnaBase(java.lang.String cd_tipo_consegna) {
	super(cd_tipo_consegna);
}
/* 
 * Getter dell'attributo ds_tipo_consegna
 */
public java.lang.String getDs_tipo_consegna() {
	return ds_tipo_consegna;
}
/* 
 * Setter dell'attributo ds_tipo_consegna
 */
public void setDs_tipo_consegna(java.lang.String ds_tipo_consegna) {
	this.ds_tipo_consegna = ds_tipo_consegna;
}
}
