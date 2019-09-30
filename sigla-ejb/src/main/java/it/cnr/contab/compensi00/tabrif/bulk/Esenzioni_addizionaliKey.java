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
 * Date 13/06/2007
 */
package it.cnr.contab.compensi00.tabrif.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Esenzioni_addizionaliKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cd_catastale;
	public Esenzioni_addizionaliKey() {
		super();
	}
	public Esenzioni_addizionaliKey(java.lang.String cd_catastale) {
		super();
		this.cd_catastale=cd_catastale;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Esenzioni_addizionaliKey)) return false;
		Esenzioni_addizionaliKey k = (Esenzioni_addizionaliKey) o;
		if (!compareKey(getCd_catastale(), k.getCd_catastale())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCd_catastale());
		return i;
	}
	public void setCd_catastale(java.lang.String cd_catastale)  {
		this.cd_catastale=cd_catastale;
	}
	public java.lang.String getCd_catastale() {
		return cd_catastale;
	}
}