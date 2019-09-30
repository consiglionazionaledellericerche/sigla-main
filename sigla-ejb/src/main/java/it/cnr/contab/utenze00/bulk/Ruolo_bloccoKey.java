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

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class Ruolo_bloccoKey extends OggettoBulk implements KeyedPersistent {
	// CD_RUOLO VARCHAR(20) NOT NULL (PK)
	private java.lang.String cd_ruolo;

	// ESERCIZIO INTEGER NOT NULL (PK)
	private java.lang.Integer esercizio;


	public Ruolo_bloccoKey() {
		super();
	}

	public Ruolo_bloccoKey(java.lang.String cd_ruolo,java.lang.Integer esercizio) {
		super();
		this.cd_ruolo = cd_ruolo;
		this.esercizio = esercizio;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this == o) return true;
		if (!(o instanceof Ruolo_bloccoKey)) return false;
		Ruolo_bloccoKey k = (Ruolo_bloccoKey)o;
		if(!compareKey(getCd_ruolo(),k.getCd_ruolo())) return false;
		if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
		return true;
	}
	public java.lang.String getCd_ruolo() {
		return cd_ruolo;
	}
	public void setCd_ruolo(java.lang.String cd_ruolo) {
		this.cd_ruolo = cd_ruolo;
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio) {
		this.esercizio = esercizio;
	}
	public int primaryKeyHashCode() {
		return
			calculateKeyHashCode(getCd_ruolo())+
			calculateKeyHashCode(getEsercizio());
	}
}
