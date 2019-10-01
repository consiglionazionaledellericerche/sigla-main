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

package it.cnr.contab.utenze00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class RuoloBase extends RuoloKey implements Keyed {
	// CD_CDS VARCHAR(30)
	private java.lang.String cd_cds;

	// DS_RUOLO VARCHAR(200)
	private java.lang.String ds_ruolo;

	// TIPO VARCHAR(6)
	private java.lang.String tipo;

public RuoloBase() {
	super();
}
public RuoloBase(java.lang.String cd_ruolo) {
	super(cd_ruolo);
}
/* 
 * Getter dell'attributo cd_cds
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/* 
 * Getter dell'attributo ds_ruolo
 */
public java.lang.String getDs_ruolo() {
	return ds_ruolo;
}
/* 
 * Setter dell'attributo cd_cds
 */
public void setCd_cds(java.lang.String cd_cds) {
	this.cd_cds = cd_cds;
}
/* 
 * Setter dell'attributo ds_ruolo
 */
public void setDs_ruolo(java.lang.String ds_ruolo) {
	this.ds_ruolo = ds_ruolo;
}
	/**
	 * @return
	 */
	public java.lang.String getTipo() {
		return tipo;
	}

	/**
	 * @param string
	 */
	public void setTipo(java.lang.String string) {
		tipo = string;
	}

}
