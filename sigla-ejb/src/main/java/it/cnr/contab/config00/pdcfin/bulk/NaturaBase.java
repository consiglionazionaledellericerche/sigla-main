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

public class NaturaBase extends NaturaKey implements Keyed {
	// DS_NATURA VARCHAR(100)
	private java.lang.String ds_natura;
    //	TIPO VARCHAR(3)
	private java.lang.String tipo;
public NaturaBase() {
	super();
}
public NaturaBase(java.lang.String cd_natura) {
	super(cd_natura);
}
/* 
 * Getter dell'attributo ds_natura
 */
public java.lang.String getDs_natura() {
	return ds_natura;
}
/* 
 * Setter dell'attributo ds_natura
 */
public void setDs_natura(java.lang.String ds_natura) {
	this.ds_natura = ds_natura;
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
