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

package it.cnr.contab.anagraf00.tabter.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class ProvinciaBase extends ProvinciaKey implements Keyed {
	// CD_REGIONE VARCHAR(10) NOT NULL
	private java.lang.String cd_regione;

	// DS_PROVINCIA VARCHAR(100) NOT NULL
	private java.lang.String ds_provincia;

public ProvinciaBase() {
	super();
}
public ProvinciaBase(java.lang.String cd_provincia) {
	super(cd_provincia);
}
/* 
 * Getter dell'attributo cd_regione
 */
public java.lang.String getCd_regione() {
	return cd_regione;
}
/* 
 * Getter dell'attributo ds_provincia
 */
public java.lang.String getDs_provincia() {
	return ds_provincia;
}
/* 
 * Setter dell'attributo cd_regione
 */
public void setCd_regione(java.lang.String cd_regione) {
	this.cd_regione = cd_regione;
}
/* 
 * Setter dell'attributo ds_provincia
 */
public void setDs_provincia(java.lang.String ds_provincia) {
	this.ds_provincia = ds_provincia;
}
}
