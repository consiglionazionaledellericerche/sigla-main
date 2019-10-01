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
 * Date 21/01/2008
 */
package it.cnr.contab.cori00.views.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class F24ep_tempKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Long prog;
	public F24ep_tempKey() {
		super();
	}
	public F24ep_tempKey(java.lang.Long prog) {
		super();
		this.prog=prog;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof F24ep_tempKey)) return false;
		F24ep_tempKey k = (F24ep_tempKey) o;
		if (!compareKey(getProg(), k.getProg())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getProg());
		return i;
	}
	public void setProg(java.lang.Long prog)  {
		this.prog=prog;
	}
	public java.lang.Long getProg() {
		return prog;
	}
}