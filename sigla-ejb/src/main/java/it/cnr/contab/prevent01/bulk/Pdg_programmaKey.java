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
* Creted by Generator 1.0
* Date 18/02/2005
*/
package it.cnr.contab.prevent01.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class Pdg_programmaKey extends OggettoBulk implements KeyedPersistent {
	private static final long serialVersionUID = 1L;

	private java.lang.String cd_programma;
	
	public Pdg_programmaKey() {
		super();
	}
	
	public Pdg_programmaKey(java.lang.String cd_programma) {
		super();
		this.cd_programma=cd_programma;
	}
	
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Pdg_programmaKey)) return false;
		Pdg_programmaKey k = (Pdg_programmaKey) o;
		if (!compareKey(getCd_programma(), k.getCd_programma())) return false;
		return true;
	}
	
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCd_programma());
		return i;
	}
	
	public void setCd_programma(java.lang.String cd_programma)  {
		this.cd_programma=cd_programma;
	}
	
	public java.lang.String getCd_programma () {
		return cd_programma;
	}
}