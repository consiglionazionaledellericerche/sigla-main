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
 * Date 20/01/2007
 */
package it.cnr.contab.prevent01.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class Ass_pdg_missione_tipo_uoKey extends OggettoBulk implements KeyedPersistent {
	private static final long serialVersionUID = 1L;

	private java.lang.String cd_missione;

	private java.lang.String cd_tipo_unita;
	
	public Ass_pdg_missione_tipo_uoKey() {
		super();
	}
	
	public Ass_pdg_missione_tipo_uoKey(java.lang.String cd_missione, java.lang.String cd_tipo_unita) {
		super();
		this.cd_missione=cd_missione;
		this.cd_tipo_unita=cd_tipo_unita;
	}

	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Ass_pdg_missione_tipo_uoKey)) return false;
		Ass_pdg_missione_tipo_uoKey k = (Ass_pdg_missione_tipo_uoKey) o;
		if (!compareKey(getCd_missione(), k.getCd_missione())) return false;
		if (!compareKey(getCd_tipo_unita(), k.getCd_tipo_unita())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCd_missione());
		i = i + calculateKeyHashCode(getCd_tipo_unita());
		return i;
	}

	public void setCd_missione(java.lang.String cd_missione) {
		this.cd_missione = cd_missione;
	}
	
	public java.lang.String getCd_missione() {
		return cd_missione;
	}
	
	public void setCd_tipo_unita(java.lang.String cd_tipo_unita) {
		this.cd_tipo_unita = cd_tipo_unita;
	}
	
	public java.lang.String getCd_tipo_unita() {
		return cd_tipo_unita;
	}
}