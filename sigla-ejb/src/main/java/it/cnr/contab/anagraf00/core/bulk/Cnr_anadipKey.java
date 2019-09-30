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
* Date 22/11/2005
*/
package it.cnr.contab.anagraf00.core.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Cnr_anadipKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer matricola;
	public Cnr_anadipKey() {
		super();
	}
	public Cnr_anadipKey(java.lang.Integer matricola) {
		super();
		this.matricola=matricola;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Cnr_anadipKey)) return false;
		Cnr_anadipKey k = (Cnr_anadipKey) o;
		if (!compareKey(getMatricola(), k.getMatricola())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getMatricola());
		return i;
	}
	public void setMatricola(java.lang.Integer matricola)  {
		this.matricola=matricola;
	}
	public java.lang.Integer getMatricola () {
		return matricola;
	}
}