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
 * Date 18/03/2008
 */
package it.cnr.contab.utenze00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Ass_tipo_ruolo_privilegioKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String tipo;
	private java.lang.String cd_privilegio;
	public Ass_tipo_ruolo_privilegioKey() {
		super();
	}
	public Ass_tipo_ruolo_privilegioKey(java.lang.String tipo, java.lang.String cd_privilegio) {
		super();
		this.tipo=tipo;
		this.cd_privilegio=cd_privilegio;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Ass_tipo_ruolo_privilegioKey)) return false;
		Ass_tipo_ruolo_privilegioKey k = (Ass_tipo_ruolo_privilegioKey) o;
		if (!compareKey(getTipo(), k.getTipo())) return false;
		if (!compareKey(getCd_privilegio(), k.getCd_privilegio())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getTipo());
		i = i + calculateKeyHashCode(getCd_privilegio());
		return i;
	}
	public void setTipo(java.lang.String tipo)  {
		this.tipo=tipo;
	}
	public java.lang.String getTipo() {
		return tipo;
	}
	public void setCd_privilegio(java.lang.String cd_privilegio)  {
		this.cd_privilegio=cd_privilegio;
	}
	public java.lang.String getCd_privilegio() {
		return cd_privilegio;
	}
}