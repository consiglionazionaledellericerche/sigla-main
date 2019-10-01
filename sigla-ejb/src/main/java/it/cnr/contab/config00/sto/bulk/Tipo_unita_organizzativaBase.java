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

package it.cnr.contab.config00.sto.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_unita_organizzativaBase extends Tipo_unita_organizzativaKey implements Keyed {
	// DS_TIPO_UNITA VARCHAR(300)
	private java.lang.String ds_tipo_unita;

public Tipo_unita_organizzativaBase() {
	super();
}
public Tipo_unita_organizzativaBase(java.lang.String cd_tipo_unita) {
	super(cd_tipo_unita);
}
/* 
 * Getter dell'attributo ds_tipo_unita
 */
public java.lang.String getDs_tipo_unita() {
	return ds_tipo_unita;
}
/* 
 * Setter dell'attributo ds_tipo_unita
 */
public void setDs_tipo_unita(java.lang.String ds_tipo_unita) {
	this.ds_tipo_unita = ds_tipo_unita;
}
}
