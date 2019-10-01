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

public class Pdg_missioneKey extends OggettoBulk implements KeyedPersistent {
	private static final long serialVersionUID = 1L;

	private java.lang.String cd_missione;
	
	public Pdg_missioneKey() {
		super();
	}
	
	public Pdg_missioneKey(java.lang.String cd_missione) {
		super();
		this.cd_missione=cd_missione;
	}
	
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Pdg_missioneKey)) return false;
		Pdg_missioneKey k = (Pdg_missioneKey) o;
		if (!compareKey(getCd_missione(), k.getCd_missione())) return false;
		return true;
	}
	
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCd_missione());
		return i;
	}
	
	public void setCd_missione(java.lang.String cd_missione)  {
		this.cd_missione=cd_missione;
	}
	
	public java.lang.String getCd_missione () {
		return cd_missione;
	}
}