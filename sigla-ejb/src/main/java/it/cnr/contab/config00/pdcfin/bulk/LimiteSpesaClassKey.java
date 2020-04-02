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
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 15/12/2010
 */
package it.cnr.contab.config00.pdcfin.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class LimiteSpesaClassKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer id_classificazione;

	private String cd_cds;

	public LimiteSpesaClassKey() {
		super();
	}

	public LimiteSpesaClassKey(Integer id_classificazione, String Cd_cds) {
		super();
		this.id_classificazione=id_classificazione;
		this.cd_cds = Cd_cds;
	}

	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof LimiteSpesaClassKey)) return false;
		LimiteSpesaClassKey k = (LimiteSpesaClassKey) o;
		if (!compareKey(getId_classificazione(), k.getId_classificazione())) return false;
		if (!compareKey(getCd_cds(), k.getCd_cds())) return false;
		return true;
	}

	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getId_classificazione());
		i = i + calculateKeyHashCode(getCd_cds());
		return i;
	}

	public Integer getId_classificazione() {
		return id_classificazione;
	}

	public void setId_classificazione(Integer id_classificazione) {
		this.id_classificazione = id_classificazione;
	}

	public String getCd_cds() {
		return cd_cds;
	}

	public void setCd_cds(String cd_cds) {
		this.cd_cds = cd_cds;
	}
}