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
* Date 07/04/2005
*/
package it.cnr.contab.config00.contratto.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class OrganoKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cd_organo;
	public OrganoKey() {
		super();
	}
	public OrganoKey(java.lang.String cd_tipo_organo) {
		super();
		this.cd_organo=cd_tipo_organo;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof OrganoKey)) return false;
		OrganoKey k = (OrganoKey) o;
		if (!compareKey(getCd_organo(), k.getCd_organo())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCd_organo());
		return i;
	}
	public void setCd_organo(java.lang.String cd_tipo_organo)  {
		this.cd_organo=cd_tipo_organo;
	}
	public java.lang.String getCd_organo () {
		return cd_organo;
	}
}