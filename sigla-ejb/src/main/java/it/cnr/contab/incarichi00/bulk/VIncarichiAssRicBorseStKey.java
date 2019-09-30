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
 * Date 26/07/2007
 */
package it.cnr.contab.incarichi00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class VIncarichiAssRicBorseStKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.Long pgRepertorio;
	public VIncarichiAssRicBorseStKey() {
		super();
	}
	public VIncarichiAssRicBorseStKey(java.lang.Integer esercizio, java.lang.Long pgRepertorio) {
		super();
		this.esercizio=esercizio;
		this.pgRepertorio=pgRepertorio;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof VIncarichiAssRicBorseStKey)) return false;
		VIncarichiAssRicBorseStKey k = (VIncarichiAssRicBorseStKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getPgRepertorio(), k.getPgRepertorio())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getPgRepertorio());
		return i;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setPgRepertorio(java.lang.Long pgRepertorio)  {
		this.pgRepertorio=pgRepertorio;
	}
	public java.lang.Long getPgRepertorio() {
		return pgRepertorio;
	}
}