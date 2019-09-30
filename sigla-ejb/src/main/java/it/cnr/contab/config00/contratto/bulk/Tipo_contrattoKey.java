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
public class Tipo_contrattoKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cd_tipo_contratto;
	public Tipo_contrattoKey() {
		super();
	}
	public Tipo_contrattoKey(java.lang.String cd_tipo_contratto) {
		super();
		this.cd_tipo_contratto=cd_tipo_contratto;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Tipo_contrattoKey)) return false;
		Tipo_contrattoKey k = (Tipo_contrattoKey) o;
		if (!compareKey(getCd_tipo_contratto(), k.getCd_tipo_contratto())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCd_tipo_contratto());
		return i;
	}
	public void setCd_tipo_contratto(java.lang.String cd_tipo_contratto)  {
		this.cd_tipo_contratto=cd_tipo_contratto;
	}
	public java.lang.String getCd_tipo_contratto () {
		return cd_tipo_contratto;
	}
}