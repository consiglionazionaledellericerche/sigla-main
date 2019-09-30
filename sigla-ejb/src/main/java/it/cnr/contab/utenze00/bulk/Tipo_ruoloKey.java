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
* Date 09/01/2006
*/
package it.cnr.contab.utenze00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Tipo_ruoloKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String tipo;
	public Tipo_ruoloKey() {
		super();
	}
	public Tipo_ruoloKey( java.lang.String tipo) {
		super();
		this.tipo=tipo;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Tipo_ruoloKey)) return false;
		Tipo_ruoloKey k = (Tipo_ruoloKey) o;
		if (!compareKey(getTipo(), k.getTipo())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
			i = i + calculateKeyHashCode(getTipo());
		return i;
	}
	
	public void setTipo(java.lang.String tipo)  {
		this.tipo=tipo;
	}
	public java.lang.String getTipo () {
		return tipo;
	}
}