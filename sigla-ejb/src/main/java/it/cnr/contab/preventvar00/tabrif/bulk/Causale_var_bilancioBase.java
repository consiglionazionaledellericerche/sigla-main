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

package it.cnr.contab.preventvar00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Causale_var_bilancioBase extends Causale_var_bilancioKey implements Keyed {
	// DS_CAUSALE_VAR_BILANCIO VARCHAR(200) NOT NULL
	private java.lang.String ds_causale_var_bilancio;

	// TI_CAUSALE CHAR(1) NOT NULL
	private java.lang.String ti_causale;

public Causale_var_bilancioBase() {
	super();
}
public Causale_var_bilancioBase(java.lang.String cd_causale_var_bilancio) {
	super(cd_causale_var_bilancio);
}
/* 
 * Getter dell'attributo ds_causale_var_bilancio
 */
public java.lang.String getDs_causale_var_bilancio() {
	return ds_causale_var_bilancio;
}
/* 
 * Getter dell'attributo ti_causale
 */
public java.lang.String getTi_causale() {
	return ti_causale;
}
/* 
 * Setter dell'attributo ds_causale_var_bilancio
 */
public void setDs_causale_var_bilancio(java.lang.String ds_causale_var_bilancio) {
	this.ds_causale_var_bilancio = ds_causale_var_bilancio;
}
/* 
 * Setter dell'attributo ti_causale
 */
public void setTi_causale(java.lang.String ti_causale) {
	this.ti_causale = ti_causale;
}
}
