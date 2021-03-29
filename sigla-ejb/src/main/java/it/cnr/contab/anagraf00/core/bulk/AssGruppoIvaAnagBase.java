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

import it.cnr.jada.persistency.Keyed;

import java.sql.Timestamp;

public class AssGruppoIvaAnagBase extends AssGruppoIvaAnagKey implements Keyed {
	// DT_CANC TIMESTAMP
	private java.sql.Timestamp dt_cancellazione;
	private String stato;

	public AssGruppoIvaAnagBase() {
		super();
	}

	public AssGruppoIvaAnagBase(Integer cd_anag, Integer cd_anag_gr_iva) {
		super(cd_anag, cd_anag_gr_iva);
	}

	public Timestamp getDt_cancellazione() {
		return dt_cancellazione;
	}

	public void setDt_cancellazione(Timestamp dt_cancellazione) {
		this.dt_cancellazione = dt_cancellazione;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}
}
