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
 * Date 18/07/2007
 */
package it.cnr.contab.incarichi00.tabrif.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Tipo_prestazioneKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cd_tipo_prestazione;
	public Tipo_prestazioneKey() {
		super();
	}
	public Tipo_prestazioneKey(java.lang.String cd_tipo_prestazione) {
		super();
		this.cd_tipo_prestazione=cd_tipo_prestazione;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Tipo_prestazioneKey)) return false;
		Tipo_prestazioneKey k = (Tipo_prestazioneKey) o;
		if (!compareKey(getCd_tipo_prestazione(), k.getCd_tipo_prestazione())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCd_tipo_prestazione());
		return i;
	}
	public void setCd_tipo_prestazione(java.lang.String cd_tipo_prestazione)  {
		this.cd_tipo_prestazione=cd_tipo_prestazione;
	}
	public java.lang.String getCd_tipo_prestazione() {
		return cd_tipo_prestazione;
	}
}