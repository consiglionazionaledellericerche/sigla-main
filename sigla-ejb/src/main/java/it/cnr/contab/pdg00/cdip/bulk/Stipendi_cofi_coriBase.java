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

package it.cnr.contab.pdg00.cdip.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Stipendi_cofi_coriBase extends Stipendi_cofi_coriKey implements Keyed {
	// AMMONTARE DECIMAL(15,2)
	private java.math.BigDecimal ammontare;
	// DT_DA_COMPETENZA_COGE TIMESTAMP
	private java.sql.Timestamp dt_da_competenza_coge;
	// DT_A_COMPETENZA_COGE TIMESTAMP
	private java.sql.Timestamp dt_a_competenza_coge;

public Stipendi_cofi_coriBase() {
	super();
}
public Stipendi_cofi_coriBase(java.lang.Integer esercizio,
							  java.lang.Integer mese,
							  String cd_contributo_ritenuta,
							  String ti_ente_percipiente) {
	super(esercizio,mese,cd_contributo_ritenuta,ti_ente_percipiente);
}
	/**
	 * @return
	 */
	public java.sql.Timestamp getDt_a_competenza_coge() {
		return dt_a_competenza_coge;
	}

	/**
	 * @return
	 */
	public java.sql.Timestamp getDt_da_competenza_coge() {
		return dt_da_competenza_coge;
	}

	/**
	 * @param timestamp
	 */
	public void setDt_a_competenza_coge(java.sql.Timestamp timestamp) {
		dt_a_competenza_coge = timestamp;
	}

	/**
	 * @param timestamp
	 */
	public void setDt_da_competenza_coge(java.sql.Timestamp timestamp) {
		dt_da_competenza_coge = timestamp;
	}

	/**
	 * @return
	 */
	public java.math.BigDecimal getAmmontare() {
		return ammontare;
	}

	/**
	 * @param decimal
	 */
	public void setAmmontare(java.math.BigDecimal decimal) {
		ammontare = decimal;
	}

}
