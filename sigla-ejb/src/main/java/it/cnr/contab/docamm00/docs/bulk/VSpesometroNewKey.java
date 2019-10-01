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

package it.cnr.contab.docamm00.docs.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class VSpesometroNewKey extends OggettoBulk implements KeyedPersistent {
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: V_SPESOMETRO
	 **/
	public VSpesometroNewKey() {
		super();
	}
	public VSpesometroNewKey(java.lang.Long progr) {
		super();
		this.prog = progr;
	}

	private java.lang.Long prog;
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof VSpesometroNewKey)) return false;
		VSpesometroNewKey k = (VSpesometroNewKey) o;
		if(!compareKey(getProg(),k.getProg())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		return calculateKeyHashCode(getProg());
	}
	public java.lang.Long getProg() {
		return prog;
	}
	public void setProg(java.lang.Long prog) {
		this.prog = prog;
	}
}