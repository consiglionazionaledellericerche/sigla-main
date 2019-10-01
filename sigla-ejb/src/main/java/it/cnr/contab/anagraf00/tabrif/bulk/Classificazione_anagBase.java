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

package it.cnr.contab.anagraf00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Classificazione_anagBase extends Classificazione_anagKey implements Keyed {
	// DS_CLASSIFIC_ANAG VARCHAR(100) NOT NULL
	private java.lang.String ds_classific_anag;

	// DT_CANC TIMESTAMP
	private java.sql.Timestamp dt_canc;

public Classificazione_anagBase() {
	super();
}
public Classificazione_anagBase(java.lang.String cd_classific_anag) {
	super(cd_classific_anag);
}
/* 
 * Getter dell'attributo ds_classific_anag
 */
public java.lang.String getDs_classific_anag() {
	return ds_classific_anag;
}
/* 
 * Getter dell'attributo dt_canc
 */
public java.sql.Timestamp getDt_canc() {
	return dt_canc;
}
/* 
 * Setter dell'attributo ds_classific_anag
 */
public void setDs_classific_anag(java.lang.String ds_classific_anag) {
	this.ds_classific_anag = ds_classific_anag;
}
/* 
 * Setter dell'attributo dt_canc
 */
public void setDt_canc(java.sql.Timestamp dt_canc) {
	this.dt_canc = dt_canc;
}
}
