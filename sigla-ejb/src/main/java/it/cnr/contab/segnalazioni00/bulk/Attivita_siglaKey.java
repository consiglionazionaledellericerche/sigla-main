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

/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 20/05/2009
 */
package it.cnr.contab.segnalazioni00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Attivita_siglaKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Long pg_attivita;
	private java.lang.Integer esercizio;
	public Attivita_siglaKey() {
		super();
	}
	public Attivita_siglaKey(java.lang.Long pg_attivita, java.lang.Integer esercizio) {
		super();
		this.pg_attivita=pg_attivita;
		this.esercizio=esercizio;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Attivita_siglaKey)) return false;
		Attivita_siglaKey k = (Attivita_siglaKey) o;
		if (!compareKey(getPg_attivita(), k.getPg_attivita())) return false;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getPg_attivita());
		i = i + calculateKeyHashCode(getEsercizio());
		return i;
	}
	public void setPg_attivita(java.lang.Long pg_attivita)  {
		this.pg_attivita=pg_attivita;
	}
	public java.lang.Long getPg_attivita() {
		return pg_attivita;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
}