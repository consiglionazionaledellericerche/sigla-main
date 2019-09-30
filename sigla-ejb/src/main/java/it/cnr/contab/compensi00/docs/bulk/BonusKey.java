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
 * Date 19/02/2009
 */
package it.cnr.contab.compensi00.docs.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class BonusKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.Long pg_bonus;
	public BonusKey() {
		super();
	}
	public BonusKey(java.lang.Integer esercizio, java.lang.Long pg_bonus) {
		super();
		this.esercizio=esercizio;
		this.pg_bonus=pg_bonus;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof BonusKey)) return false;
		BonusKey k = (BonusKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getPg_bonus(), k.getPg_bonus())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getPg_bonus());
		return i;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setPg_bonus(java.lang.Long pg_bonus)  {
		this.pg_bonus=pg_bonus;
	}
	public java.lang.Long getPg_bonus() {
		return pg_bonus;
	}
}