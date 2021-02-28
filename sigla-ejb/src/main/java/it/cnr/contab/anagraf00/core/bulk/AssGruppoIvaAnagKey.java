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

package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class AssGruppoIvaAnagKey extends OggettoBulk implements KeyedPersistent {
	// CD_ANAG DECIMAL(8,0) NOT NULL (PK)
	private Integer cd_anag;

	// CD_TERZO DECIMAL(8,0) NOT NULL (PK)
	private Integer cd_anag_gr_iva;

	public AssGruppoIvaAnagKey() {
		super();
	}

	public AssGruppoIvaAnagKey(Integer cd_anag, Integer cd_anag_gr_iva) {
		this.cd_anag = cd_anag;
		this.cd_anag_gr_iva = cd_anag_gr_iva;
	}

	public boolean equalsByPrimaryKey(Object o) {
		if (this == o) return true;
		if (!(o instanceof AssGruppoIvaAnagKey)) return false;
		AssGruppoIvaAnagKey k = (AssGruppoIvaAnagKey)o;
		if(!compareKey(getCd_anag(),k.getCd_anag())) return false;
		if(!compareKey(getCd_anag_gr_iva(),k.getCd_anag_gr_iva())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		return
			calculateKeyHashCode(getCd_anag())+
			calculateKeyHashCode(getCd_anag_gr_iva());
	}
	/* 
	 * Getter dell'attributo cd_anag
	 */
	public Integer getCd_anag() {
		return cd_anag;
	}
	/* 
	 * Getter dell'attributo cd_terzo
	 */
	/* 
	 * Setter dell'attributo cd_anag
	 */
	public void setCd_anag(Integer cd_anag) {
		this.cd_anag = cd_anag;
	}

	public Integer getCd_anag_gr_iva() {
		return cd_anag_gr_iva;
	}

	public void setCd_anag_gr_iva(Integer cd_anag_gr_iva) {
		this.cd_anag_gr_iva = cd_anag_gr_iva;
	}
}
