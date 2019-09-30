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

package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.persistency.*;

public class Anagrafico_terzoBase extends Anagrafico_terzoKey implements Keyed {
	// DT_CANC TIMESTAMP
	private java.sql.Timestamp dt_canc;

	public Anagrafico_terzoBase() {
		super();
	}

	public Anagrafico_terzoBase(java.lang.Integer cd_anag, java.lang.Integer cd_terzo, java.lang.String ti_legame) {
		super(cd_anag, cd_terzo, ti_legame);
	}
	/* 
	 * Getter dell'attributo dt_canc
	 */
	public java.sql.Timestamp getDt_canc() {
		return dt_canc;
	}
	/* 
	 * Setter dell'attributo dt_canc
	 */
	public void setDt_canc(java.sql.Timestamp dt_canc) {
		this.dt_canc = dt_canc;
	}
}
