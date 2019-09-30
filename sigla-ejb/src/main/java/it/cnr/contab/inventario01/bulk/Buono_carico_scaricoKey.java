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
* Created by Generator 1.0
* Date 19/01/2006
*/
package it.cnr.contab.inventario01.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Buono_carico_scaricoKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Long pg_inventario;
	private java.lang.String ti_documento;
	private java.lang.Integer esercizio;
	private java.lang.Long pg_buono_c_s;
	public Buono_carico_scaricoKey() {
		super();
	}
	public Buono_carico_scaricoKey(java.lang.Long pg_inventario, java.lang.String ti_documento, java.lang.Integer esercizio, java.lang.Long pg_buono_c_s) {
		super();
		this.pg_inventario=pg_inventario;
		this.ti_documento=ti_documento;
		this.esercizio=esercizio;
		this.pg_buono_c_s=pg_buono_c_s;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Buono_carico_scaricoKey)) return false;
		Buono_carico_scaricoKey k = (Buono_carico_scaricoKey) o;
		if (!compareKey(getPg_inventario(), k.getPg_inventario())) return false;
		if (!compareKey(getTi_documento(), k.getTi_documento())) return false;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getPg_buono_c_s(), k.getPg_buono_c_s())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getPg_inventario());
		i = i + calculateKeyHashCode(getTi_documento());
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getPg_buono_c_s());
		return i;
	}
	public void setPg_inventario(java.lang.Long pg_inventario)  {
		this.pg_inventario=pg_inventario;
	}
	public java.lang.Long getPg_inventario () {
		return pg_inventario;
	}
	public void setTi_documento(java.lang.String ti_documento)  {
		this.ti_documento=ti_documento;
	}
	public java.lang.String getTi_documento () {
		return ti_documento;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio () {
		return esercizio;
	}
	public void setPg_buono_c_s(java.lang.Long pg_buono_c_s)  {
		this.pg_buono_c_s=pg_buono_c_s;
	}
	public java.lang.Long getPg_buono_c_s () {
		return pg_buono_c_s;
	}
}