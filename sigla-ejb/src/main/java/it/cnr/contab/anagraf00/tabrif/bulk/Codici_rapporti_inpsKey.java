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
* Date 12/05/2005
*/
package it.cnr.contab.anagraf00.tabrif.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Codici_rapporti_inpsKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cd_rapporto_inps;
	public Codici_rapporti_inpsKey() {
		super();
	}
	public Codici_rapporti_inpsKey(java.lang.String cd_rapporto_inps) {
		super();
		this.cd_rapporto_inps=cd_rapporto_inps;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Codici_rapporti_inpsKey)) return false;
		Codici_rapporti_inpsKey k = (Codici_rapporti_inpsKey) o;
		if (!compareKey(getCd_rapporto_inps(), k.getCd_rapporto_inps())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCd_rapporto_inps());
		return i;
	}
	public void setCd_rapporto_inps(java.lang.String cd_rapporto_inps)  {
		this.cd_rapporto_inps=cd_rapporto_inps;
	}
	public java.lang.String getCd_rapporto_inps () {
		return cd_rapporto_inps;
	}
}