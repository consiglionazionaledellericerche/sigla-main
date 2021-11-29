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

package it.cnr.contab.inventario00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Condizione_beneBase extends Condizione_beneKey implements Keyed {
	// DS_CONDIZIONE_BENE VARCHAR(100) NOT NULL
	private java.lang.String ds_condizione_bene;
	private java.lang.Boolean flDefaultOrdine;

public Condizione_beneBase() {
	super();
}
public Condizione_beneBase(java.lang.String cd_condizione_bene) {
	super(cd_condizione_bene);
}
/* 
 * Getter dell'attributo ds_condizione_bene
 */
public java.lang.String getDs_condizione_bene() {
	return ds_condizione_bene;
}
/* 
 * Setter dell'attributo ds_condizione_bene
 */
public void setDs_condizione_bene(java.lang.String ds_condizione_bene) {
	this.ds_condizione_bene = ds_condizione_bene;
}

	public Boolean getFlDefaultOrdine() {
		return flDefaultOrdine;
	}

	public void setFlDefaultOrdine(Boolean flDefaultOrdine) {
		this.flDefaultOrdine = flDefaultOrdine;
	}
}
