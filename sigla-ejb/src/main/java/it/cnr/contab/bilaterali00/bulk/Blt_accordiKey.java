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
* Date 19/10/2005
*/
package it.cnr.contab.bilaterali00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Blt_accordiKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cd_accordo;

	public Blt_accordiKey() {
		super();
	}

	public Blt_accordiKey(java.lang.String cd_accordo) {
		super();
		this.cd_accordo=cd_accordo;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Blt_accordiKey)) return false;
		Blt_accordiKey k = (Blt_accordiKey) o;
		if (!compareKey(getCd_accordo(), k.getCd_accordo())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCd_accordo());
		return i;
	}
	public java.lang.String getCd_accordo() {
		return cd_accordo;
	}
	public void setCd_accordo(java.lang.String cd_accordo) {
		this.cd_accordo = cd_accordo;
	}
}