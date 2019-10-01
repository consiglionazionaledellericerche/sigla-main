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
 * Date 29/11/2006
 */
package it.cnr.contab.config00.geco.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Geco_dipartimentiKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cod_dip;
	private java.lang.Long esercizio;
	public Geco_dipartimentiKey() {
		super();
	}
	public Geco_dipartimentiKey(java.lang.String cod_dip, java.lang.Long esercizio) {
		super();
		this.cod_dip=cod_dip;
		this.esercizio=esercizio;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Geco_dipartimentiKey)) return false;
		Geco_dipartimentiKey k = (Geco_dipartimentiKey) o;
		if (!compareKey(getCod_dip(), k.getCod_dip())) return false;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCod_dip());
		i = i + calculateKeyHashCode(getEsercizio());
		return i;
	}
	public void setCod_dip(java.lang.String cod_dip)  {
		this.cod_dip=cod_dip;
	}
	public java.lang.String getCod_dip() {
		return cod_dip;
	}
	public java.lang.Long getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(java.lang.Long esercizio) {
		this.esercizio = esercizio;
	}
}