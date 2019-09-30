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

public class FunzioneBase extends FunzioneKey implements Keyed {
	// DS_FUNZIONE VARCHAR(100)
	private java.lang.String ds_funzione;
	private java.lang.Boolean fl_utilizzabile;

public FunzioneBase() {
	super();
}
public FunzioneBase(java.lang.String cd_funzione) {
	super(cd_funzione);
}
/* 
 * Getter dell'attributo ds_funzione
 */
public java.lang.String getDs_funzione() {
	return ds_funzione;
}
/* 
 * Setter dell'attributo ds_funzione
 */
public void setDs_funzione(java.lang.String ds_funzione) {
	this.ds_funzione = ds_funzione;
}

/* 
 * Getter dell'attributo ds_funzione
 */
public java.lang.Boolean getFl_utilizzabile() {
	return fl_utilizzabile;
}
/* 
 * Setter dell'attributo ds_funzione
 */
public void setFl_utilizzabile(java.lang.Boolean fl_utilizzabile) {
	this.fl_utilizzabile = fl_utilizzabile;
}
}
