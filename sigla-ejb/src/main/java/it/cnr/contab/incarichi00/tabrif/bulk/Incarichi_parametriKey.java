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
 * Created by BulkGenerator 1.5 [30/07/2008]
 * Date 31/07/2008
 */
package it.cnr.contab.incarichi00.tabrif.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.annotation.FieldPropertyAnnotation;
import it.cnr.jada.bulk.annotation.InputType;
import it.cnr.jada.persistency.KeyedPersistent;

public class Incarichi_parametriKey extends OggettoBulk implements KeyedPersistent {
	@FieldPropertyAnnotation(name="cd_parametri",
					inputType=InputType.TEXT,
					inputSize=5,
					maxLength=5,
					enabledOnSearch=true,
					nullable=false,
					label="Codice Parametri")
	private java.lang.String cd_parametri;

	public Incarichi_parametriKey() {
		super();
	}
	public Incarichi_parametriKey(java.lang.String cd_parametri) {
		super();
		this.cd_parametri=cd_parametri;
	}

	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Incarichi_parametriKey)) return false;
		Incarichi_parametriKey k = (Incarichi_parametriKey) o;
		if (!compareKey(getCd_parametri(), k.getCd_parametri())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCd_parametri());
		return i;
	}

	public java.lang.String getCd_parametri() {
		return cd_parametri;
	}
	public void setCd_parametri(java.lang.String cd_parametri) {
		this.cd_parametri = cd_parametri;
	}
}