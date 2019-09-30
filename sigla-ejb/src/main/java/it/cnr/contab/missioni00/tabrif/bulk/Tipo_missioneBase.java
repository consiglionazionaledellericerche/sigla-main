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

public class Tipo_missioneBase extends Tipo_missioneKey implements Keyed {
	// DS_TIPO_MISSIONE VARCHAR(100) NOT NULL
	private java.lang.String ds_tipo_missione;

private java.lang.Boolean fl_valido;
public Tipo_missioneBase() {
	super();
}
public Tipo_missioneBase(java.lang.String cd_tipo_missione) {
	super(cd_tipo_missione);
}
/* 
 * Getter dell'attributo ds_tipo_missione
 */
public java.lang.String getDs_tipo_missione() {
	return ds_tipo_missione;
}
/* 
 * Setter dell'attributo ds_tipo_missione
 */
public void setDs_tipo_missione(java.lang.String ds_tipo_missione) {
	this.ds_tipo_missione = ds_tipo_missione;
}
public java.lang.Boolean getFl_valido() {
	return fl_valido;
}
public void setFl_valido(java.lang.Boolean fl_valido) {
	this.fl_valido = fl_valido;
}
}
