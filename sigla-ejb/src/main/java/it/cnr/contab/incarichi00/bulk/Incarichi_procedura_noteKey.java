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
 * Date 03/04/2008
 */
package it.cnr.contab.incarichi00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Incarichi_procedura_noteKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.Long pg_procedura;
	private java.lang.Long pg_nota;
	public Incarichi_procedura_noteKey() {
		super();
	}
	public Incarichi_procedura_noteKey(java.lang.Integer esercizio, java.lang.Long pg_procedura, java.lang.Long pg_nota) {
		super();
		this.esercizio=esercizio;
		this.pg_procedura=pg_procedura;
		this.pg_nota=pg_nota;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Incarichi_procedura_noteKey)) return false;
		Incarichi_procedura_noteKey k = (Incarichi_procedura_noteKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getPg_procedura(), k.getPg_procedura())) return false;
		if (!compareKey(getPg_nota(), k.getPg_nota())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getPg_procedura());
		i = i + calculateKeyHashCode(getPg_nota());
		return i;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setPg_procedura(java.lang.Long pg_procedura)  {
		this.pg_procedura=pg_procedura;
	}
	public java.lang.Long getPg_procedura() {
		return pg_procedura;
	}
	public void setPg_nota(java.lang.Long pg_nota)  {
		this.pg_nota=pg_nota;
	}
	public java.lang.Long getPg_nota() {
		return pg_nota;
	}
}