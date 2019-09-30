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
 * Date 03/04/2008
 */
package it.cnr.contab.incarichi00.tabrif.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class Incarichi_parametri_configKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cd_config;

	public Incarichi_parametri_configKey() {
		super();
	}
	public Incarichi_parametri_configKey(java.lang.String cd_config) {
		super();
		this.cd_config=cd_config;
	}

	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Incarichi_parametri_configKey)) return false;
		Incarichi_parametri_configKey k = (Incarichi_parametri_configKey) o;
		if (!compareKey(getCd_config(), k.getCd_config())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCd_config());
		return i;
	}

	public java.lang.String getCd_config() {
		return cd_config;
	}
	public void setCd_config(java.lang.String cd_config) {
		this.cd_config = cd_config;
	}
}