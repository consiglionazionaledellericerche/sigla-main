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

public class Tipo_obbligazioneBase extends Tipo_obbligazioneKey implements Keyed {
	// DS_TIPO_OBBLIGAZIONE VARCHAR(100) NOT NULL
	private java.lang.String ds_tipo_obbligazione;

public Tipo_obbligazioneBase() {
	super();
}
public Tipo_obbligazioneBase(java.lang.String cd_tipo_obbligazione) {
	super(cd_tipo_obbligazione);
}
/* 
 * Getter dell'attributo ds_tipo_obbligazione
 */
public java.lang.String getDs_tipo_obbligazione() {
	return ds_tipo_obbligazione;
}
/* 
 * Setter dell'attributo ds_tipo_obbligazione
 */
public void setDs_tipo_obbligazione(java.lang.String ds_tipo_obbligazione) {
	this.ds_tipo_obbligazione = ds_tipo_obbligazione;
}
}
