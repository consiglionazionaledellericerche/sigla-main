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

public class NazioneKey extends OggettoBulk implements KeyedPersistent {
	// PG_NAZIONE DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_nazione;

	public NazioneKey() {
		super();
	}

	public NazioneKey(java.lang.Long pg_nazione) {
		this.pg_nazione = pg_nazione;
	}

	public boolean equalsByPrimaryKey(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof NazioneKey))
			return false;
		NazioneKey k = (NazioneKey) o;
		if (!compareKey(getPg_nazione(), k.getPg_nazione()))
			return false;
		return true;
	}

	/*
	 * Getter dell'attributo pg_nazione
	 */
	public java.lang.Long getPg_nazione() {
		return pg_nazione;
	}

	public int primaryKeyHashCode() {
		return calculateKeyHashCode(getPg_nazione());
	}

	/*
	 * Setter dell'attributo pg_nazione
	 */
	public void setPg_nazione(java.lang.Long pg_nazione) {
		this.pg_nazione = pg_nazione;
	}
	
	@Override
	public boolean equals(Object obj) {
		return equalsByPrimaryKey(obj);
	}
	
	public int hashCode() {
		return primaryKeyHashCode();
	};
}
